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
package org.kuali.student.git.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.PatchApplyException;
import org.eclipse.jgit.api.errors.PatchFormatException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.ref.exception.BranchRefExistsException;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.utils.ExternalGitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Test the importer on KS CM Contribution Branch Importer.
 * 
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestKSCMContribImport extends AbstractGitImporterMainTestCase {

	private static final Logger log = LoggerFactory.getLogger(TestKSCMContribImport.class);
	
	/**
	 * @param name
	 */
	public TestKSCMContribImport() {
		super("ks-cmc-import", true);
		
		// need a local working copy so we can apply the patch file.
		setBare(false);
	}

	@Test
	public void testImport () throws IOException, BranchRefExistsException, PatchFormatException, PatchApplyException, GitAPIException {
		
		setupBase();
		
		super.runImporter(repository, 53643, "src/test/resources/ks-r53643-to-53644.dump.bz2", "https://svn.kuali.org/repos/student", "fake-uuid");
		
		
		
	}

	private void setupBase() throws FileNotFoundException, IOException, PatchFormatException, PatchApplyException, GitAPIException {

		/*
		 * Create the repository and apply the base patch for the source branch.
		 * 
		 * Also create the Subversion Revision Meta Data.
		 */
		
		ExternalGitUtils.applyPatch("git", repository, new BZip2CompressorInputStream(new FileInputStream("src/test/resources/ks-r53642-base.patch.bz2")), System.out);
		
		Git git = new Git (repository);
		
		DirCache dircache = git.add().addFilepattern(".").call();

		RevCommit result = git.commit().setMessage("create sandbox_ksenroll-8475@53642 base").setCommitter("name", "email").call();
		
		GitTreeProcessor processor = new GitTreeProcessor(repository);
		
		GitTreeData tree = processor.extractExistingTreeDataFromCommit(result);
		
		Assert.assertNotNull (tree.find(repository, "CM"));
		Assert.assertNotNull (tree.find(repository, "aggregate"));
		
		Ref sandboxBranchRef = git.checkout().setStartPoint("master").setName("sandbox_ksenroll-8475").setCreateBranch(true).call();
		
		Assert.assertNotNull(sandboxBranchRef);
		
	}

	private void runImporter(Repository repository, long importRevision) throws IOException {
		super.runImporter(repository, importRevision, "src/test/resources/ks-r" + importRevision + ".dump.bz2", "https://svn.kuali.org/repos/student", "fake-uuid");
	}
	

}
