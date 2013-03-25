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

		if (args.length != 2) {
			log.error("USAGE: <source dump file> <target dump file>");
			System.exit(-1);
		}

		String sourceDumpFile = args[0];
		String targetDumpFile = args[1];
		
		try {
			
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
			
			applicationContext.registerShutdownHook();
			
			SvnDumpFilter filter = applicationContext.getBean(SvnDumpFilter.class);
			
			log.info(String.format ("Started Filtering (%s) into (%s)", sourceDumpFile, targetDumpFile));
			
			filter.applyFilter(sourceDumpFile, targetDumpFile);
			
			log.info(String.format ("Finished Filtering (%s) into (%s)", sourceDumpFile, targetDumpFile));
			
		} catch (Exception e) {
			log.error("Processing failed", e);
		}


		
	}


}
