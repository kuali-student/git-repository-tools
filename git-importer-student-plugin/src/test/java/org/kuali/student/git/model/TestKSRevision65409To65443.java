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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TreeFormatter;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.importer.GitImporterMain;
import org.kuali.student.git.model.ref.exception.BranchRefExistsException;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.svn.model.ExternalModuleInfo;

/**
 * 
 * Test the importer on KS Revision 65409 and 65443 which lose track of a key branch during renaming processes.
 * 
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestKSRevision65409To65443 {

	/**
	 * @param name
	 */
	public TestKSRevision65409To65443() {
		
	}

	@Test
	public void testImport () throws IOException, BranchRefExistsException {
		File gitRepository = new File ("target/ks-r65409-to-r65443");
		
		FileUtils.deleteDirectory(gitRepository);
		
		Repository repository = GitRepositoryUtils
				.buildFileRepository(gitRepository, true);
		
		// insert some fake copy from data
		
		
	
		runImporter (repository, 65409);
		
		// test that the ks-ap/trunk branch was created
		
		assertRefNotNull(repository, "enrollment_ks-ap_trunk", "ks-ap trunk should exist");
		
		// inject copyfrom data for enrollment/ks-ap/trunk from 65424
		
		runImporter (repository,  65425);
		
		// ks-ap trunk should have been deleted
		assertRefNull(repository, "enrollment_ks-ap_trunk", "ks-ap trunk should be null (deleted");
		
		assertRefNotNull(repository, "enrollment_ks-ap_trunk@65424", "enrollment_ks-ap_trunk@65424 should exist");
		/*
		 * thinking about creating a branch here for ks-ap/branches/inactive/KSAP-M8
		 */
		
		createBranch (repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-M8", "pom.xml", "some text");
		
		// 65430 
		runImporter (repository, 65431); 
		
		assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_trunk", "expected ks-ap trunk to exist");
		
		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP", "expected ks-ap inactive KSAP to not exist");
		
		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-M8", "expected ks-ap inactive KSAP-M8 to not exist");
		
		runImporter (repository, 65434);
		
		// we expect nothing to happen because its just a directory add. (not a copy from)
		
		createBranch (repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-M7", "pom.xml", "some text");
		
		runImporter (repository, 65435);
		
		assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-M7", "expected ks-ap trunk to exist");
		
		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-M7", "expected ks-ap KSAP-M7 to not exist");

		
		createBranch (repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-archived-2013-09-25", "pom.xml", "some text");
		
		runImporter (repository, 65437);
		
		assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-archived-2013-09-25", "expected ks-ap braches inactive KSAP-2013-09-25 to exist");
		
		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-archived-2013-09-25", "expected ks-ap KSAP-archived-2013-09-25 to not exist");
		
		runImporter (repository, 65439);
	
		assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_inactive", "expected ks-ap inactive to exist");
		
		// assert file does not exist in a specific branch
//		assertRefNull(repository, "enrollment, notNullMessage);
		
		runImporter (repository, 65440);
		
		assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP", "expected ks-ap branches KSAP to exist");
		
		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_inactive", "expected ks-ap inactive to not exist");
		
		runImporter (repository, 65441);
		
		assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-M8", "expected ks-ap branches KSAP-M8 to exist");
		
		// check that the 
//		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_inactive", "expected ks-ap inactive to not exist");
		
		runImporter (repository, 65442);
		
		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_trunk", "expected ks-ap trunk to not exist");
		
		runImporter (repository, 65443);
		
		assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_trunk", "expected ks-ap trunk to exist");
		
		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP", "expected ks-ap branches KSAP to not exist");
		
		
	}
	
	private void createBranch(Repository repository, String branchName,
			String fileName, String fileContent) throws IOException, BranchRefExistsException {

		ObjectInserter inserter = repository.newObjectInserter();
		
		// store the blob
		ObjectId blobId = inserter.insert(Constants.OBJ_BLOB, fileContent.getBytes());
		
		// create the tree
		TreeFormatter tf = new TreeFormatter();
		
		tf.append(fileName, FileMode.REGULAR_FILE, blobId);
		
		ObjectId treeId = inserter.insert(tf);
		
		// make the commit
		CommitBuilder cb = new CommitBuilder();

		PersonIdent pi;
		cb.setAuthor(pi = new PersonIdent("admin", "admin@kuali.org"));
		
		cb.setCommitter(pi);
		
		cb.setMessage("committed " + fileName);
		
		cb.setTreeId(treeId);
		
		cb.setEncoding("UTF-8");
		
		// save the branch
		
		ObjectId commit = inserter.insert(cb);
		
		GitRefUtils.createBranch(repository, branchName, commit);
		
		inserter.flush();
		inserter.release();
	}

	private void assertRefNotNull(Repository repo, String expectedBranchName, String onNullMessage) throws IOException {

		Ref trunk = repo.getRef(expectedBranchName);
		
		Assert.assertNotNull(onNullMessage, trunk);
	}
	
	private void assertRefNull(Repository repo, String expectedBranchName, String notNullMessage) throws IOException {

		Ref trunk = repo.getRef(expectedBranchName);
		
		Assert.assertNull(notNullMessage, trunk);
	}

	private void runImporter(Repository repository, long importRevision) throws IOException {
		
		SvnRevisionMapper revisionMapper = new SvnRevisionMapper(repository);
//		
		revisionMapper.initialize();
		
		Map<String, Ref> heads = repository.getRefDatabase().getRefs(Constants.R_HEADS);
		
		if (heads.size() > 0) {
			revisionMapper.createRevisionMap(importRevision-1L, new ArrayList<Ref>(heads.values()));
		}
		
		revisionMapper.shutdown();
		
		System.getProperties().setProperty("spring.profiles.active", "configured-plugin");
		
		GitImporterMain.main(new String [] {"src/test/resources/ks-r" + importRevision + ".dump.bz2", repository.getDirectory().getAbsolutePath(), "target/ks-r"+importRevision+"-ks-veto.log", "target/ks-r"+importRevision+"-ks-copyFrom-skipped.log", "target/ks-r"+importRevision+"-blob.log", "0", "https://svn.kuali.org/repos/student", "uuid"});
		
		
	}

}
