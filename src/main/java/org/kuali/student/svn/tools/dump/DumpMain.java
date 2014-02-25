/*
 * Copyright 2014 The Kuali Foundation
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
package org.kuali.student.svn.tools.dump;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.iq80.snappy.SnappyOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.admin.SVNAdminClient;

/**
 * 
 * Use svnkit on the file system based repository to extract an incremental but not delta's dump file.
 * 
 * This will be used in testing.
 * 
 * And later will be the way to acquire the dump file.
 * 
 * @author Kuali Student Team
 *
 */
public class DumpMain {

	private static final Logger log = LoggerFactory.getLogger(DumpMain.class);
	
	/**
	 * 
	 */
	public DumpMain() {
	}
	
	
	private static SVNRevision[] computeRevisions (String revision) {
		
		try {
			long singleRevision = Long.parseLong(revision);
			
			SVNRevision r = SVNRevision.create(singleRevision);
			
			return new SVNRevision[] {r, r};
			
		} catch (NumberFormatException e) {
			
			String parts[] = revision.split(":");
			
			long startRevision = Long.parseLong (parts[0].trim());
			
			SVNRevision startR = SVNRevision.create(startRevision);
			
			SVNRevision endR = null;
			
			String endPart = parts[1].trim();
			
			if (endPart.toLowerCase().equals("head")) {
				endR = SVNRevision.HEAD;
			}
			else {

				long endRevision = Long.parseLong (endPart);
			
				endR = SVNRevision.create(endRevision);
			}
			
			return new SVNRevision[] {startR, endR};
			
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if  (args.length != 3 && args.length != 5) {
			log.error("USAGE: <path to svn repository> <name of the dump file> <revision> [<username> <password>]");
			System.exit(-1);
		}
		
		File pathToRepository = new File (args[0]);
		
		if (!pathToRepository.exists()) {
			log.error(pathToRepository + " does not exist.");
			System.exit(-2);
		}
		
		String username = null;
		String password = null;
		
		if (args.length == 5) {
			username = args[3];
			password = args[4];
		}
		
		try {
			SnappyOutputStream out = new SnappyOutputStream(new BufferedOutputStream(
					new FileOutputStream(args[1])));

			SVNClientManager manager = SVNClientManager.newInstance(null, username,
					password);

			SVNAdminClient client = manager.getAdminClient();

			
			SVNRevision[] revisions = computeRevisions(args[2]);
			
			
			client.doDump(pathToRepository, out, revisions[0], revisions[1],
					true, false);

			out.close();
		} catch (Exception e) {
			log.error("terminated by an unexpected exception: ", e);
		}
		
		
	}

}
