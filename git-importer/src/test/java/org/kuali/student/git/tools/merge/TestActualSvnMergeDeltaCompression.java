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
package org.kuali.student.git.tools.merge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.BranchRangeDataProviderImpl;
import org.kuali.student.git.model.DummyGitTreeNodeInitializer;
import org.kuali.student.git.model.SvnMergeInfoUtils;
import org.kuali.student.git.model.SvnRevisionMapper;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.BranchDetectorImpl;
import org.kuali.student.git.model.branch.large.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.svn.model.AbstractGitRespositoryTestCase;

/**
 * @author Kuali Student Team
 *
 */
public class TestActualSvnMergeDeltaCompression extends
		AbstractGitRespositoryTestCase {

	private SvnRevisionMapper revisionMapper;
	
	/**
	 * @param name
	 */
	public TestActualSvnMergeDeltaCompression() {
		super("actual-svn-merge-delta-compression");
	}
	
	

	/* (non-Javadoc)
	 * @see org.kuali.student.git.tools.AbstractGitRespositoryTestCase#onBefore()
	 */
	@Override
	protected void onBefore() throws Exception {
		revisionMapper = new SvnRevisionMapper(repo);
		
		revisionMapper.initialize();
	}

	

	

	/* (non-Javadoc)
	 * @see org.kuali.student.git.tools.AbstractGitRespositoryTestCase#onAfter()
	 */
	@Override
	protected void onAfter() throws Exception {
		revisionMapper.shutdown();
	}



	@Test
	public void testActualBranchAdjacencyDeltaCompression () throws IOException {
	
		ObjectInserter inserter = repo.newObjectInserter();
		
		BranchDetector branchDetector = new BranchDetectorImpl();

		GitTreeData trunk = new GitTreeData(new DummyGitTreeNodeInitializer());
		
		String branchFilePath = "src/main/java/org/kuali/student/enrollment/test.txt";
		storeFile (inserter, trunk, branchFilePath, "test");

		ObjectId commitId = commit (inserter, trunk, "created branch");
		
		Result result = createBranch(commitId, "trunk");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		revisionMapper.createRevisionMap(1L, new ArrayList<>(repo.getAllRefs().values()));
		
		storeFile (inserter, trunk, "readme.txt", "readme");
		
		ObjectId secondCommitId = commit (inserter, trunk, "created branch", commitId);
		
		result = createBranch(secondCommitId, "trunk");
		
		Assert.assertEquals(Result.FORCED, result);
		
		inserter.flush();
		
		revisionMapper.createRevisionMap(1000L, new ArrayList<>(repo.getAllRefs().values()));
		
		BranchMergeInfo bmi = new BranchMergeInfo("trunk");
		
		bmi.addMergeRevision(1);
		
		bmi.addMergeRevision(1000);
		
		List<BranchMergeInfo> deltas = Arrays.asList(new BranchMergeInfo[] {bmi});
		
		RevWalk revWalk = new RevWalk(repo);
		
		SvnMergeInfoUtils.consolidateConsecutiveRanges(new BranchRangeDataProviderImpl(revisionMapper, revWalk), branchDetector, new LargeBranchNameProviderMapImpl(), deltas);
		
		revWalk.close();
		
		assertDeltaRanges (deltas, "trunk", new Long[] {1000L});
		
		
		storeFile (inserter, trunk, "another.dat", "test");

		ObjectId thirdCommit = commit (inserter, trunk, "created branch", secondCommitId);
		
		result = createBranch(thirdCommit, "trunk");
		
		Assert.assertEquals(Result.FORCED, result);
		
		inserter.flush();
		
		revisionMapper.createRevisionMap(1998L, new ArrayList<>(repo.getAllRefs().values()));
		

		bmi = new BranchMergeInfo("trunk");
		
		bmi.addMergeRevision(1);
		
		bmi.addMergeRevision(1998);
		
		deltas = Arrays.asList(new BranchMergeInfo[] {bmi});
		
		revWalk = new RevWalk(repo);
		
		SvnMergeInfoUtils.consolidateConsecutiveRanges(new BranchRangeDataProviderImpl(revisionMapper, revWalk), branchDetector, new LargeBranchNameProviderMapImpl(), deltas);
		
		revWalk.close();
		
		assertDeltaRanges (deltas, "trunk", new Long[] {1L, 1998L});
		
	}
	
	private void assertDeltaRanges(List<BranchMergeInfo> deltas, String branchName,
			Long[] expectedRevisions) {
		
		for (BranchMergeInfo delta : deltas) {
			
			if (delta.getBranchName().equals(branchName)) {
				
				List<Long>expectedRevisionsList = Arrays.asList(expectedRevisions);
				
				Assert.assertTrue("incorrect revision for branch " + branchName , CollectionUtils.isEqualCollection(expectedRevisionsList, delta.getMergedRevisions()));
				
				
			}
		}
		
	}

}
