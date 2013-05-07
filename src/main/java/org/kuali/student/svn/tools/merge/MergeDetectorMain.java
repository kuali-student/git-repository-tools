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
package org.kuali.student.svn.tools.merge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.kuali.student.svn.tools.AbstractParseOptions;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.kuali.student.svn.tools.merge.model.MergeDetectorData;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kuali Student Team
 * 
 * The idea for this search is that we should be able to deduce from the current path if the copy from path is different.
 * 
 * and if the copyfrom path contains "branch" or "tag" or "trunk" then there is a good change it was supposed to be a merge.
 * 
 *
 */
public class MergeDetectorMain {

	private static final Logger log = LoggerFactory.getLogger(MergeDetectorMain.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			log.error("USAGE: <svn dump file> <merged paths output file>");
			System.exit(-1);
		}

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"svn/MergeDetector-applicationContext.xml");

			applicationContext.registerShutdownHook();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);
			
			final PrintWriter pw = new PrintWriter(new FileOutputStream(args[1]));
			
			pw.println("#rev:copyfrom branch:target branch:copyfrom path:target path");
			
			final MergeDetectorData detectorData = applicationContext
					.getBean(MergeDetectorData.class);

			File dumpFile = new File(args[0]);
			
			if (!dumpFile.exists())
				throw new FileNotFoundException(args[0] + " path not found");
					
			
			filter.parseDumpFile(dumpFile.getAbsolutePath(), new AbstractParseOptions() {


				private long currentRevision = 0L;
				
				
				@Override
				public void onRevision(long currentRevision,
						ReadLineData lineData) {
					
					
					if (this.currentRevision > 0)
						detectorData.processRevision(pw, this.currentRevision);
					
					this.currentRevision = currentRevision;
					

				}

				@Override
				public void onStreamEnd(ReadLineData lineData) {
					
					if (this.currentRevision > 0)
						detectorData.processRevision(pw, this.currentRevision);
					
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.kuali.student.svn.tools.AbstractParseOptions
				 * #onAfterNode(long, java.lang.String, java.util.Map,
				 * org.kuali.student.svn.tools.model .INodeFilter)
				 */
				@Override
				public void onAfterNode(long currentRevision, String path,
						Map<String, String> nodeProperties,
						INodeFilter nodeFilter) {

					String copyFromPath = nodeProperties
							.get("Node-copyfrom-path");
					
					if (copyFromPath != null) {
						
						String copyFromRev = nodeProperties
								.get("Node-copyfrom-rev");
						
						String copyFromMD5 = nodeProperties.get("Text-copy-source-md5");
						
						detectorData.storePath(Long.valueOf(copyFromRev), copyFromPath, copyFromMD5, currentRevision, path);
						
					}

					
				}

			});

		} catch (Exception e) {
			log.error("Processing failed", e);
		}


		
	}


}
