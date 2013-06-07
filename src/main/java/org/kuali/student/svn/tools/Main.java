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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.JoinedRevision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kuali Student Team
 * 
 */
public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private static void usage() {
		log.error("USAGE: <join data file 1> [ <join data file 2> ... <join data file n>]");
		log.error("We need a dump file for the target revision to exist in the current working directory in the format of rX.dump");
		System.exit(-1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"svn/applicationContext.xml");

			applicationContext.registerShutdownHook();

			INodeFilter nodeFilter = applicationContext
					.getBean(INodeFilter.class);

			if (args.length == 0) {
				// read the list of files from stdin line by line

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(System.in));

				try {
					while (true) {

						String line = reader.readLine();

						if (line == null)
							break;

						File f = new File(line.trim());
						
						if (!f.exists()) {
							log.warn(line + " does not exist in the current working directory.");
							continue; // skip to the next line
						}
						
						log.info(String.format("Loading Join Data from (%s)",
								line));
						
						nodeFilter.loadFilterData(f);

					}
				} catch (IOException e) {
					log.error("problem reading stdin", e);
				}
			} else {

				// read in the files from the command line
				for (int i = 1; i < args.length; i++) {
					log.info(String.format("Loading Join Data from (%s)",
							args[i]));
					nodeFilter.loadFilterData(new File(args[i]));
				}

			}

			List<JoinedRevision> joinWork = nodeFilter.getRevisionsToBeJoined();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);

			for (JoinedRevision joinedRevision : joinWork) {

				String targetDumpFilename = String.format("r%d.dump",
						joinedRevision.getTargetRevision());

				String filteredDumpFilename = String
						.format("r%d-filtered.dump",
								joinedRevision.getTargetRevision());

				log.info(String.format("Started Joining (r%d) into (r%d)",
						joinedRevision.getTargetRevision(),
						joinedRevision.getCopyFromRevision()));

				filter.applyFilter(targetDumpFilename, filteredDumpFilename,
						nodeFilter);

				log.info(String.format("Finished Joining (r%d) into (r%d)",
						joinedRevision.getTargetRevision(),
						joinedRevision.getCopyFromRevision()));

			}

		} catch (Exception e) {
			log.error("Processing failed", e);
		}

	}

}
