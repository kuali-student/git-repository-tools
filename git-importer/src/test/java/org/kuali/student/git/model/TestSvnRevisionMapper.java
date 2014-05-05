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
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
// random access file is terribly slow in linux
@Ignore
public class TestSvnRevisionMapper {

	private static final Logger log = LoggerFactory.getLogger(TestSvnRevisionMapper.class);
	
	private static final File JSVN_DIR = new File ("target", "revision-mapper-test-repo");
	private SvnRevisionMapper revisionMapper;
	private Repository repo;
	private Git git;
	
	/**
	 * 
	 */
	public TestSvnRevisionMapper() {
	}
	
	@Before
	public void setup() throws IOException, NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, GitAPIException {
		
		FileUtils.deleteDirectory(JSVN_DIR);
		
		/*
		 * Create a non-bare repository in the path given by JSVN_DIR
		 */
		repo = GitRepositoryUtils.buildFileRepository(JSVN_DIR, true, false);
		
		revisionMapper = new SvnRevisionMapper(repo);
		
		revisionMapper.initialize();
		
		git = new Git (repo);
		
		createFileContentAndCommit ("README", "test content");
	}
	
	@After
	public void tearDown () throws IOException {
		
		revisionMapper.shutdown();
	}
	
	private RevCommit createFileContentAndCommit(String fileName, String fileContent) throws NoWorkTreeException, IOException, NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, GitAPIException {

		FileUtils.write(new File (repo.getWorkTree(), fileName), fileContent);
		
		AddCommand addCommand = git.add();
		
		addCommand.addFilepattern(fileName);
		
		addCommand.call();
		
		CommitCommand commitCommand = git.commit();
		
		commitCommand.setAuthor(new PersonIdent("test", "test@kuali.org"));
		
		commitCommand.setMessage("test commit");
		
		return commitCommand.call();
		
	}

	private BranchMergeInfo createBranchMergeInfo (String branchName, long ... revisions) {
		
		BranchMergeInfo bmi = new BranchMergeInfo(branchName);
		
		for (long revision : revisions) {
			bmi.addMergeRevision(revision);
		}
		
		return bmi;
	}
	
	@Test
	public void testMergeInfo() throws IOException {
		
		List<BranchMergeInfo> mergeDataList = new ArrayList<>();
		
		mergeDataList.add(createBranchMergeInfo("feature_1", 2L));
		
		revisionMapper.createMergeData(5L, "trunk", mergeDataList);
		
		Set<Long>mergedRevisions = revisionMapper.getMergeBranchRevisions(5L, "trunk", "feature_1");
		
		Assert.assertNotNull(mergedRevisions);
		Assert.assertArrayEquals(new Long[] {2L}, mergedRevisions.toArray(new Long[] {}));
		
		revisionMapper.createMergeData(5L, "master", Arrays.asList(new BranchMergeInfo[] { createBranchMergeInfo("feature_2", 3L, 4L) } ));
		
		mergedRevisions = revisionMapper.getMergeBranchRevisions(5L, "master", "feature_2");
		
		Assert.assertNotNull(mergedRevisions);
		Assert.assertArrayEquals(new Long[] {3L, 4L}, mergedRevisions.toArray(new Long[] {}));

		revisionMapper.shutdown();
		
		revisionMapper.initialize();
		
		mergedRevisions = revisionMapper.getMergeBranchRevisions(5L, "trunk", "feature_1");
		
		Assert.assertNotNull(mergedRevisions);
		Assert.assertArrayEquals(new Long[] {2L}, mergedRevisions.toArray(new Long[] {}));
		
		mergedRevisions = revisionMapper.getMergeBranchRevisions(5L, "master", "feature_2");
		
		Assert.assertNotNull(mergedRevisions);
		Assert.assertArrayEquals(new Long[] {3L, 4L}, mergedRevisions.toArray(new Long[] {}));
		
		
	}
	
	
	@Test
	public void testTwoRevisions () throws IOException {
		
		
		List<Ref> branchHeads = new ArrayList<Ref>(repo.getRefDatabase().getRefs(Constants.R_HEADS).values());
		
		for (int i = 0; i < 500; i++) {
			createRevision(i, branchHeads);
		}
		
		Random r = new Random();
		
		for (int i = 0; i < 500; i++) {
		
			int randomRevision = r.nextInt(499);
			
			testRevision(randomRevision, branchHeads);
			
		}
		
		revisionMapper.repackMapFile();
		
		for (int i = 0; i < 500; i++) {
			
			int randomRevision = r.nextInt(499);
			
			testRevision(randomRevision, branchHeads);
		}
		
		for (int i = 500; i < 1000; i++) {
			createRevision(i, branchHeads);
		}
		
		for (int i = 0; i < 1000; i++) {
			
			int randomRevision = r.nextInt(999);
			
			testRevision(randomRevision, branchHeads);
		}
		
		ObjectId head = revisionMapper.getRevisionBranchHead(98, "master");
		
		Assert.assertNotNull("name should never be null", head);
		
		List<SvnRevisionMap> heads = revisionMapper.getRevisionHeads(650);
		
		Assert.assertEquals(1, heads.size());
		
		revisionMapper.shutdown();
		
		revisionMapper = new SvnRevisionMapper(repo);
		
		revisionMapper.initialize();

		for (int i = 0; i < 1000; i++) {
			
			int randomRevision = r.nextInt(999);
			
			testRevision(randomRevision, branchHeads);
		}
		
		for (int i = 1000; i < 1500; i++) {
			createRevision(i, branchHeads);
		}

		revisionMapper.repackMapFile();
		
		for (int i = 0; i < 1499; i++) {
			
			int randomRevision = r.nextInt(1499);
			
			testRevision(randomRevision, branchHeads);
		}
		
		
	}
	
	@Test
	public void testTruncate () throws IOException {
		
		List<Ref> branchHeads = new ArrayList<Ref>(repo.getRefDatabase().getRefs(Constants.R_HEADS).values());
		
		// create 500 revs
		for (int i = 0; i < 500; i++) {
			createRevision(i, branchHeads);
		}
		
		Random r = new Random();
		
		// test 500 revs
		for (int i = 0; i < 500; i++) {
		
			int randomRevision = r.nextInt(499);
			
			testRevision(randomRevision, branchHeads);
			
		}
		
		revisionMapper.truncateTo(250);
		
		// test first 250 still work
		
		for (int i = 0; i < 250; i++) {
			
			int randomRevision = r.nextInt(250);
			
			testRevision(randomRevision, branchHeads);
		}

		
		// confirm 251 does not exist
		boolean expectToFail = testRevisionHead(251, branchHeads);
		
		Assert.assertEquals(false, expectToFail);
		
		// insert another 250 (total is now 500)
		for (int i = 250; i < 500; i++) {
			createRevision(i, branchHeads);
		}
		
		// test that all 500 revs work again.
		for (int i = 0; i < 500; i++) {
			
			int randomRevision = r.nextInt(499);
			
			testRevision(randomRevision, branchHeads);
		}
		
		
	}
	

	private void createRevision(long revision, List<Ref> branchHeads) throws IOException {
		
		revisionMapper.createRevisionMap(revision, branchHeads);
		
	}
	
	private boolean testRevisionHead (long revision, List<Ref>branchHeads) throws IOException {
	
		for (Ref ref : branchHeads) {
			
			ObjectId head = revisionMapper.getRevisionBranchHead(revision, ref.getName().replaceFirst(Constants.R_HEADS, ""));
			
			if (head == null)
				return false;
		}
		
		return true;
	}
	
	private void testRevision (long revision, List<Ref>branchHeads) throws IOException {
		
		for (Ref ref : branchHeads) {
			
			ObjectId head = revisionMapper.getRevisionBranchHead(revision, ref.getName().replaceFirst(Constants.R_HEADS, ""));
			
			Assert.assertNotNull("head should never be null", head);
			
			Assert.assertEquals(ref.getObjectId(), head);
		}
		
	}

}
