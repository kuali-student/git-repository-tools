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

import java.util.Arrays;

import org.apache.commons.collections4.CollectionUtils;
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

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length < 1) {
			log.error("USAGE: <module name> [module specific arguments]");
			System.exit(-1);
		}
		try {
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"RepositoryCleanerMain-applicationContext.xml");

			applicationContext.registerShutdownHook();
			
			String beanName = args[0];
			
			RepositoryCleaner repoCleaner = (RepositoryCleaner) applicationContext.getBean(beanName);

			/*
			 * Exclude the module name from the args sent to the module.
			 */
			
			repoCleaner.validateArgs(Arrays.asList(args).subList(1, args.length));
			
			repoCleaner.execute();
			
		} catch (Exception e) {
			log.error ("unexpected exception", e);
		}

		
		

	}

}
