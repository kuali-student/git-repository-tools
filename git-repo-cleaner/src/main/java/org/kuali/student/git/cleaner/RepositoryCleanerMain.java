/*
 *  Copyright 2014 The Kuali Foundation Licensed under the
 *	Educational Community License, Version 2.0 (the "License"); you may
 *	not use this file except in compliance with the License. You may
 *	obtain a copy of the License at
 *
 *	http://www.osedu.org/licenses/ECL-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the License for the specific language governing
 *	permissions and limitations under the License.
 */
package org.kuali.student.git.cleaner;

import java.io.File;
import java.util.Date;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author ocleirig
 *
 */
public class RepositoryCleanerMain {

	private static final Logger log = LoggerFactory.getLogger(RepositoryCleanerMain.class);
	
	/**
	 * 
	 */
	public RepositoryCleanerMain() {
		// TODO Auto-generated constructor stub
	}

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 2 && args.length != 3 && args.length != 4) {
			log.error("USAGE: <source git repository meta directory> <split date> [<branchRefSpec> <git command path>]");
			log.error("\t<git repo meta directory> : the path to the meta directory of the source git repository");
			log.error("\t<split date> : YYYY-MM-DD");
			log.error("\t<git command path> : the path to a native git ");
			System.exit(-1);
		}
		try {
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"RepositoryCleanerMain-applicationContext.xml");

			applicationContext.registerShutdownHook();
			
			final Repository repo = GitRepositoryUtils.buildFileRepository(
					new File (args[0]).getAbsoluteFile(), false);
			
			RepositoryCleaner repoCleaner = applicationContext.getBean(RepositoryCleaner.class);
			
			Date splitDate = formatter.parseDateTime(args[1]).toDate();
			
			String branchRefSpec = Constants.R_HEADS;
			
			if (args.length == 3)
				branchRefSpec = args[2].trim();
			
			
			repoCleaner.execute(repo, branchRefSpec, splitDate);
			
		} catch (Exception e) {
			log.error ("unexpected exception", e);
		}

		
		

	}

}
