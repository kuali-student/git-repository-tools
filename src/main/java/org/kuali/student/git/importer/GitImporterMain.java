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
package org.kuali.student.git.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kuali Student Team
 * 
 *         We want to parse an svn dump file and then import the changes into a
 *         corresponding git repository.
 */
public class GitImporterMain {

	private static final Logger log = LoggerFactory
			.getLogger(GitImporterMain.class);

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		if (args.length != 5 && args.length != 6) {
			log.error("USAGE: <svn dump file> <git repository> <veto.log> <skipped-copy-from.log> <blob.log> [ <svn repo base url> ]");
			log.error("\t<veto.log> : which paths were veto's as not being a valid branch");
			log.error("\t<skipped-copy-from.log> : which copy-from-paths were skipped");
			log.error("\t<blob.log> : issues related to blobs (typically directory copy related)");
			log.error("\t<svn repo base url> : the svn repo base url to use in the git-svn-id");
			System.exit(-1);
		}

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"git/GitImporterMain-applicationContext.xml");

			applicationContext.registerShutdownHook();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);

			// final MergeDetectorData detectorData = applicationContext
			// .getBean(MergeDetectorData.class);

			File dumpFile = new File(args[0]);

			if (!dumpFile.exists()) {
				throw new FileNotFoundException(args[0] + " path not found");
			}

			File gitRepository = new File(args[1]).getAbsoluteFile();

			if (!gitRepository.getParentFile().exists())
				throw new FileNotFoundException(args[1] + "path not found");

			
			final PrintWriter vetoLog = new PrintWriter(args[2]);
			
			final PrintWriter copyFromSkippedLog = new PrintWriter (args[3]);
			
			final PrintWriter blobLog = new PrintWriter(args[4]);
			
			final boolean printGitSvnIds;
			String repositoryBaseUrl = null;
			
			if (args.length == 6) {
				printGitSvnIds = true;
				repositoryBaseUrl = args[5].trim();
			}
			else
				printGitSvnIds = false;
				
			final Repository repo = GitRepositoryUtils.buildFileRepository(
					gitRepository, false);

			// extract any known branches from the repository
			
			BZip2CompressorInputStream compressedInputStream = new BZip2CompressorInputStream(new FileInputStream (dumpFile));
			
			filter.parseDumpFile(compressedInputStream, new GitImporterParseOptions(repo, vetoLog, copyFromSkippedLog, blobLog, printGitSvnIds, repositoryBaseUrl));
			
			vetoLog.close();
			copyFromSkippedLog.close();

		} catch (Exception e) {
			log.error("Processing failed", e);
		}

	}
	
	

}
