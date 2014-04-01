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
package org.kuali.student.git.tools;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
			log.error("USAGE: <path to git repository> <data file>");
			System.exit(-1);
		}

		String pathToGitRepo = args[0];
		
		String comparisonTagDataFile = args[1];
		
		
		
		try {
			
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("git/applicationContext.xml");
			
			applicationContext.registerShutdownHook();
			
			GitExtractor extractor = applicationContext.getBean(GitExtractor.class);
			
			extractor.buildRepository(new File (pathToGitRepo));
			
			List<String> pathsToCompare = FileUtils.readLines(new File (comparisonTagDataFile), "UTF-8");
			
			for (String comparisonPath : pathsToCompare) {
				
				if (comparisonPath.trim().length() == 0 || comparisonPath.trim().startsWith("#"))
					continue; // skip empty lines and comments
				
				String parts[] = comparisonPath.split(":");
				
				String targetTag = parts[0];
				String copyFromTag = parts[1];
				
				extractor.extractDifference (targetTag, copyFromTag);
				
			}
			
			
			
		}
		catch (Exception e) {
			log.error("Unexpected Exception", e);
		}
			

	}

}
