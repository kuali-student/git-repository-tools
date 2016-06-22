/*
 * Copyright 2013 The Kuali Foundation Licensed under the
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
package org.kuali.student.git.importer;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.author.AuthorsFilePersonIdentProvider;
import org.kuali.student.git.model.author.DefaultPersonIdentProviderImpl;
import org.kuali.student.git.model.author.HostBasedPersonIdentProvider;
import org.kuali.student.git.model.author.PersonIdentProvider;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.subversion.SvnDumpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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

		if (args.length != 9 && args.length != 10) {
			log.error("USAGE: <svn dump file> <git repository> <veto.log> <skipped-copy-from.log> <blob.log> <gc enabled> <svn repo base url> <repo uuid> <email host part> [<git command path>]");
			log.error("\t<veto.log> : which paths were veto's as not being a valid branch");
			log.error("\t<skipped-copy-from.log> : which copy-from-paths were skipped");
			log.error("\t<blob.log> : issues related to blobs (typically directory copy related)");
			log.error("\t<gc enabled> : set to 1 (true ever 500 revs) or 0 (false) to disable");
			log.error("\t<svn repo base url> : the svn repo base url to use in the git-svn-id");
			log.error("\t<repo uuid> : The svn repository uuid to use in the git-svn-id.\n\tIt you are importing from a clone use this to set the field to the real repositories uuid.");
            log.error("\t<host:email host part|authors:authors.txt path> : The svn author becomes author@emailHostPart when imported.");
			log.error("\t<git command path> : the path to a native git to use for gc's which occur every 500 revs");
			System.exit(-1);
		}

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"GitImporterMain-applicationContext.xml");

			applicationContext.registerShutdownHook();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);

			BranchDetector branchDetector = applicationContext.getBean("branchDetector", BranchDetector.class);
			
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
			
			boolean gcEnabled = true;
			
			final boolean printGitSvnIds = true; // not optional anymore
			
			String repositoryBaseUrl = null;

			String repositoryUUID = null;
			
			if (args[5].trim().equals("0"))
				gcEnabled = false;
			
			repositoryBaseUrl = args[6].trim();
			
			repositoryUUID = args[7].trim();
			
			String nativeGitCommandPath = null;

            String identType = args[8];



           PersonIdentProvider personIdentProvider = createIdentProvider(identType);

			if (args.length == 10)
				nativeGitCommandPath = args[9].trim();
			
			final Repository repo = GitRepositoryUtils.buildFileRepository(
					gitRepository, false);

			// extract any known branches from the repository
			
			BZip2CompressorInputStream compressedInputStream = new BZip2CompressorInputStream(new FileInputStream (dumpFile));
			
			filter.parseDumpFile(compressedInputStream, new GitImporterParseOptions(repo, vetoLog, copyFromSkippedLog, blobLog, printGitSvnIds, repositoryBaseUrl, repositoryUUID, branchDetector, personIdentProvider, gcEnabled, nativeGitCommandPath));
			
			vetoLog.close();
			copyFromSkippedLog.close();
			blobLog.close();
			
			compressedInputStream.close();

		} catch (Exception e) {
			log.error("Processing failed", e);
		}

	}

    private static PersonIdentProvider createIdentProvider(String identType) throws java.io.IOException {

        String parts[] = identType.split(":");

        if (parts.length < 2)
            throw new IllegalArgumentException("Expected: 'host:email host part|authors:authors.txt:unknown user host part' path not '"+identType+"'");

        if (parts[0].toLowerCase().equals("host")) {
            String emailHostPart = parts[1];

            HostBasedPersonIdentProvider personIdentProvider = new DefaultPersonIdentProviderImpl();

            personIdentProvider.setEmailHostPart(emailHostPart);

            return personIdentProvider;
        }
        else if (parts[0].toLowerCase().equals("authors")) {

            if (parts.length != 3)
                throw new IllegalArgumentException("Expected: 'authors:authors.txt:unknown user host part' path not '"+identType+"'");

            String authorsFileName = parts[1].trim();

            String unknownEmailHostPart = parts[2].trim();

            return new AuthorsFilePersonIdentProvider(authorsFileName, unknownEmailHostPart);

        }
        else {
            throw new IllegalArgumentException("Expected: 'host:email host part|authors:authors.txt:unknown user host part' path not '"+identType+"'");
        }


    }


}
