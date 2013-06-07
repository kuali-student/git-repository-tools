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
package org.kuali.student.svn.tools.add;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.kuali.student.svn.tools.AbstractParseOptions;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kuali Student Team
 * 
 *        We want to identify the files that were added on each revision.
 *        
 *        This is the initial data that will be used to create new copyfrom details for fixing the history where it can be found.
 *        
 *        The output of this program will be a file that can be consumed by the scripts/FindCopiesAndRenames.py python script.
 */
public class AddDetectorMain {

	private static final Logger log = LoggerFactory
			.getLogger(AddDetectorMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			log.error("USAGE: <svn dump file> <added files output file>");
			System.exit(-1);
		}

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"svn/AddDetector-applicationContext.xml");

			applicationContext.registerShutdownHook();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);

			final PrintWriter pw = new PrintWriter(
					new FileOutputStream(args[1]));

			pw.println("#rev:target path");

//			final MergeDetectorData detectorData = applicationContext
//					.getBean(MergeDetectorData.class);

			File dumpFile = new File(args[0]);

			if (!dumpFile.exists()) {
				pw.close();
				throw new FileNotFoundException(args[0] + " path not found");
			}

			filter.parseDumpFile(dumpFile.getAbsolutePath(),
					new AbstractParseOptions() {

						/* (non-Javadoc)
						 * @see org.kuali.student.svn.tools.AbstractParseOptions#onAfterNode(long, java.lang.String, java.util.Map, org.kuali.student.svn.tools.model.INodeFilter)
						 */
						@Override
						public void onAfterNode(long currentRevision,
								String path,
								Map<String, String> nodeProperties,
								INodeFilter nodeFilter) {
							
							String action = nodeProperties.get("Node-action");
							
							if (action.equals("add")) {
								
								String type = "normal";
								
								String copyFromPath = nodeProperties.get("Node-copyfrom-path");
								
								if (copyFromPath != null)
									type = "copyfrom";

								String kind = nodeProperties.get("Node-kind");
								
								pw.println(String.format("%s::%s::%d::%s", type, kind, currentRevision, path));
							}
								
						}

						
					});

			pw.close();

		} catch (Exception e) {
			log.error("Processing failed", e);
		}

	}

}
