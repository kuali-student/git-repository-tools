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
package org.kuali.student.svn.tools.merge;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.Repository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.student.git.importer.GitImporterMain;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

/**
 * @author Kuali Student Team
 *
 */
public class TestGitImporterMain {
	
	private static final String R1_DUMP_FILE = "target/r1.dump";
	
	private static final String R2_DUMP_FILE = "target/r2.dump";
	
	private static final String R3_DUMP_FILE = "target/r3.dump";

	private static final Logger log = LoggerFactory.getLogger(TestGitImporterMain.class);

	private static final String REPO = "svn-repo";
	

	private static File workingCopy;

	private static SVNURL repo;
	
	/**
	 * 
	 */
	public TestGitImporterMain() {
		// TODO Auto-generated constructor stub
	}
	
	@BeforeClass
	public static void setup() throws SVNException, IOException {
		
		SVNRepositoryUtils.deleteRepositoryArea(REPO);
		
		// create the svn repository
		
		repo = SVNRepositoryUtils.createRepository(REPO);
		
		workingCopy = SVNRepositoryUtils.checkOut(REPO, "", null, null);
		
		File trunk = new File(workingCopy, "trunk");
		
		trunk.mkdirs();
		
		File testFile = new File (trunk, "test.txt");
		
		// r1
		FileUtils.write(testFile, "test output");
		
		SVNRepositoryUtils.addFiles(testFile, null, null);
		
		SVNRepositoryUtils.commit(workingCopy, "Add a test file", null, null);
		
		// r2
		FileUtils.write(testFile, "additional output that should be replacing the original");
		
		SVNRepositoryUtils.addFiles(testFile, null, null);
		
		SVNRepositoryUtils.commit(workingCopy, "Modified a test file", null, null);
		
		File branch1 = new File (new File (workingCopy, "branches"), "branch1");
		FileUtils.copyDirectory(trunk, branch1);
		
		SVNRepositoryUtils.addFiles(branch1, null, null);
		
		SVNRepositoryUtils.commit(workingCopy, "Created branch1", null, null);
		// dump it
		SVNRepositoryUtils.dump(REPO, R1_DUMP_FILE, null, null, 0, 1, false);
		SVNRepositoryUtils.dump(REPO, R2_DUMP_FILE, null, null, 2, 2, false);
		SVNRepositoryUtils.dump(REPO, R3_DUMP_FILE, null, null, 3, 3, false);
		
		log.info("");
		
	}
	@Test
	public void testInitialRevision() throws IOException {
		
		File gitRepository = new File ("target/git-repo");
		
		FileUtils.deleteDirectory(gitRepository);
		
		GitRepositoryUtils
				.buildFileRepository(gitRepository, true);
		
		GitImporterMain.main(new String [] {R1_DUMP_FILE, gitRepository.getAbsolutePath()});
		
		GitImporterMain.main(new String [] {R2_DUMP_FILE, gitRepository.getAbsolutePath()});
		
		GitImporterMain.main(new String [] {R3_DUMP_FILE, gitRepository.getAbsolutePath()});
		
	}

}
