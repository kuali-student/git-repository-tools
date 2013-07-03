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
package org.kuali.student.svn.tools.revprops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.student.svn.tools.AbstractParseOptions;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
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
public class ExtractRevisionPropsMain {

	private static final Logger log = LoggerFactory
			.getLogger(ExtractRevisionPropsMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			log.error("USAGE: <svn dump file> <revision props output file>");
			System.exit(-1);
		}

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"svn/ExtractRevisionProps-applicationContext.xml");

			applicationContext.registerShutdownHook();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);

			final PrintWriter pw = new PrintWriter(
					new FileOutputStream(args[1]));

			pw.println("#!/bin/sh\n#\n#Generated");

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
						 * @see org.kuali.student.svn.tools.AbstractParseOptions#onRevisionContentLength(long, long, long, org.kuali.student.svn.tools.model.ReadLineData)
						 */
						@Override
						public void onRevisionContentLength(
								long currentRevision, long contentLength,
								long propContentLength, ReadLineData lineData) {
							
							if (propContentLength != -1) {

								try {
									// read any revision properties 

									Map<String, String> revisionProperties = org.kuali.student.common.io.IOUtils
											.extractRevisionProperties(
													inputStream,
													propContentLength,
													contentLength);
									
									for (String key : revisionProperties.keySet()) {
										
										String value = revisionProperties.get(key);
										
										pw.println (String.format("svn propset --revprop -r %d %s \"%s\" $REPO_URL", currentRevision, key, value));
									}
									
									pw.flush();

								} catch (Exception e) {

									log.error("Failed to process revision", e);
									
									throw new RuntimeException(String.format(
											"Failed to process revision %d", currentRevision), e);

								}

							}
						}

						
						
					});

			pw.close();

		} catch (Exception e) {
			log.error("Processing failed", e);
		}

	}

}
