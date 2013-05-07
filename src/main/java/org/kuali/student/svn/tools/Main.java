/*
 * Copyright 2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.svn.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.JoinedRevision;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kuali Student Team
 *
 */
public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			log.error("USAGE: <svn dump file version: 2 or 3> <join data file 1> [ <join data file 2> ... <join data file n>]");
			log.error("For version 2 dump files we need a single dump file for the target revision");
			log.error("For version 3 dump files we need a dump file for both the target and copyfrom revision.");
			System.exit(-1);
		}
		
		
		int svnDumpFileVersion = Integer.parseInt(args[0]);
		
		try {
			
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("svn/applicationContext.xml");
			
			applicationContext.registerShutdownHook();
			
			INodeFilter nodeFilter = applicationContext.getBean(INodeFilter.class);
			
			for(int i = 1; i < args.length; i++) {
				log.info(String.format ("Loading Join Data from (%s)", args[i]));
				nodeFilter.loadFilterData(new File (args[i]));
			}
			
			
			List<JoinedRevision> joinWork = nodeFilter.getRevisionsToBeJoined();
			
			SvnDumpFilter filter = applicationContext.getBean(SvnDumpFilter.class);
			
			for (JoinedRevision joinedRevision : joinWork) {
				
				String targetDumpFilename = String.format("r%d.dump", joinedRevision.getTargetRevision());
				
				String filteredDumpFilename = String.format ("r%d-filtered.dump", joinedRevision.getTargetRevision());
				
				String copyFromDumpFilename = String.format("r%d.dump", joinedRevision.getCopyFromRevision());
				
				if (svnDumpFileVersion == 3) {
					
					/*
					 * For version 3 dump's the md5 is on the diff content not the file content so we need to filter the copyfrom revision dump first
					 * to extract the md5's of the diff content for each path being joined into the target revision.
					 */

					log.info(String.format(
							"Extracting CopyFrom Hashes from (%s)",
							copyFromDumpFilename));

					filter.parseDumpFile(copyFromDumpFilename,
							new AbstractParseOptions() {

								/*
								 * (non-Javadoc)
								 * 
								 * @see
								 * org.kuali.student.svn.tools.AbstractParseOptions
								 * #onAfterNode(long, java.lang.String,
								 * java.util.Map,
								 * org.kuali.student.svn.tools.model
								 * .INodeFilter)
								 */
								@Override
								public void onAfterNode(long currentRevision,
										String path,
										Map<String, String> nodeProperties,
										INodeFilter nodeFilter) {

									String sha1 = nodeProperties
											.get("Text-delta-base-sha1");

									if (sha1 == null)
										sha1 = nodeProperties
												.get("Text-content-sha1");

									String md5 = nodeProperties
											.get("Text-delta-base-md5");

									if (md5 == null)
										md5 = nodeProperties
												.get("Text-content-md5");

									if (md5 != null) {

										String fullPath = nodeFilter
												.getFullCopyFromPath(
														currentRevision, path);
										nodeFilter.storeChecksumData(
												currentRevision, fullPath,
												sha1, md5);
									}
								}

							}, nodeFilter);

				}
				
				log.info(String.format("Started Joining (r%d) into (r%d)", joinedRevision.getTargetRevision(), joinedRevision.getCopyFromRevision()));
				
				filter.applyFilter(targetDumpFilename, filteredDumpFilename, nodeFilter);
				
				log.info(String.format("Finished Joining (r%d) into (r%d)", joinedRevision.getTargetRevision(), joinedRevision.getCopyFromRevision()));
				
			}
			
			
			
		} catch (Exception e) {
			log.error("Processing failed", e);
		}


		
	}


}
