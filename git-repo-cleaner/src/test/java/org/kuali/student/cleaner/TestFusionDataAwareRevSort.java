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
package org.kuali.student.cleaner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.student.cleaner.model.ObjectIdTranslationMapImpl;
import org.kuali.student.cleaner.model.bitmap.RevCommitBitMapIndex;
import org.kuali.student.cleaner.model.sort.FusionAwareTopoSortComparator;
import org.kuali.student.git.model.DummyGitTreeNodeInitializer;
import org.kuali.student.git.model.ExternalModuleUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.svn.model.AbstractGitRespositoryTestCase;
import org.kuali.student.svn.model.ExternalModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test case for developing a Comparator based RevSort that will resemble a topo sort but also handle the fusion lateral branch
 * dependencies.
 * 
 * @author ocleirig
 *
 */
public class TestFusionDataAwareRevSort extends AbstractGitRespositoryTestCase {

	private static final Logger log = LoggerFactory.getLogger(TestFusionDataAwareRevSort.class);
	private RevCommit branch1HeadRevCommit;
	private RevCommit branch2HeadRevCommit;
	private RevCommit aggregateHeadRevCommit;
	
	/**
	 * @param name
	 */
	public TestFusionDataAwareRevSort() {
		super("fusion-aware-topo-sort");
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.model.AbstractGitRespositoryTestCase#onBefore()
	 */
	@Override
	protected void onBefore() throws Exception {
		/*
		 * Setup base commits
		 * 
		 * base commit
		 * 
		 * aggregate trunk
		 * 
		 * and the different modules
		 * 
		 */
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		// create branch 1
		GitTreeData branch1 = new GitTreeData(new DummyGitTreeNodeInitializer());
		
		String branch1FilePath = "src/main/java/org/kuali/student/enrollment/test.txt";
		storeFile (inserter, branch1, branch1FilePath, "test");
		
		ObjectId b1Id = commit (inserter, branch1, "created branch1");
		
		Result result = createBranch(b1Id, "branch1");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		// create branch 2
		GitTreeData branch2 = new GitTreeData(new DummyGitTreeNodeInitializer());
		
		String branch2FilePath = branch1FilePath;
		
		storeFile (inserter, branch2, branch2FilePath, "test");
		
		ObjectId b2Id = commit (inserter, branch2, "created branch2");
		
		result = createBranch(b2Id, "branch2");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		// create aggregate
		GitTreeData aggregate = new GitTreeData(new DummyGitTreeNodeInitializer());
		
		String aggregate_file_path = "src/main/java/org/kuali/student/enrollment/pom.xml";
		storeFile (inserter, aggregate, aggregate_file_path, "pom test");
		
		ExternalModuleInfo module1 = new ExternalModuleInfo("module1", "branch1");
		ExternalModuleInfo module2 = new ExternalModuleInfo("module2", "branch2");

		module1.setBranchHeadId(b1Id);
		
		module2.setBranchHeadId(b2Id);
		
		
		String fusionDataContent = ExternalModuleUtils.createFusionMavenPluginDataFileString(repo, Arrays.asList(new ExternalModuleInfo[] {module1, module2}));
		
		storeFile(inserter, aggregate, "fusion-maven-plugin.dat", fusionDataContent);
		
		ObjectId aggregateId = commit (inserter, aggregate, "created aggregate");
		
		result = createBranch(aggregateId, "aggregate");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		branch1HeadRevCommit = super.rw.parseCommit(b1Id);
		branch2HeadRevCommit = super.rw.parseCommit(b2Id);
		
		aggregateHeadRevCommit = super.rw.parseCommit(aggregateId);
	}

	@Test
	public void testComparatorTopoEquivilenceWithThreeAggregateCommitsAndTwoPerModule () throws IOException {
		
		GitTreeProcessor treeProcessor = new GitTreeProcessor(repo);
		
		ObjectId initialAggregateHeadId = aggregateHeadRevCommit.getId();
		
		GitTreeData tree = treeProcessor.extractExistingTreeDataFromCommit(initialAggregateHeadId);
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		super.storeFile(inserter, tree, "Readme.txt", "test file content");
		
		ObjectId newCommit = commit(repo.newObjectInserter(), tree, "second commit on aggregate branch", initialAggregateHeadId);
		
		createBranch(newCommit, "aggregate");
		
		aggregateHeadRevCommit = rw.parseCommit(newCommit);
		
		// add a third commit
		
		ObjectId middleAggregateHeadId = newCommit;
		
		tree = treeProcessor.extractExistingTreeDataFromCommit(middleAggregateHeadId);
		
		super.storeFile(inserter, tree, "Readme.txt", "third file content");
		
		newCommit = commit(repo.newObjectInserter(), tree, "third commit on aggregate branch", middleAggregateHeadId);
		
		createBranch(newCommit, "aggregate");
		
		aggregateHeadRevCommit = rw.parseCommit(newCommit);
		
		// second commit to module1
		ObjectId existingModule1CommitId = branch1HeadRevCommit.getId();
		
		
		// second commit to module2
		ObjectId existingModule2CommitId = branch2HeadRevCommit.getId();
		
		
		/*
		 * Sort the commits using a comparator and make sure the results are the same as with the TopoGenerator
		 */
		
		List<RevCommit>commits = new LinkedList<RevCommit>();
		
		RevWalk rw = new RevWalk (repo);
		
		rw.markStart(branch1HeadRevCommit);
		rw.markStart(branch2HeadRevCommit);
		rw.markStart(aggregateHeadRevCommit);
		
		Iterator<RevCommit> iterator = rw.iterator();
		
		ObjectIdTranslationMapImpl translator= new ObjectIdTranslationMapImpl();
		
		RevCommitBitMapIndex index = new RevCommitBitMapIndex(repo, translator, iterator);
		
		commits = new LinkedList<RevCommit>(index.getRevCommitList());
		
		Collections.sort(commits, new FusionAwareTopoSortComparator(index));
		
		commits = new LinkedList<RevCommit>(index.getRevCommitList());
		
		Collections.sort(commits, new FusionAwareTopoSortComparator(index));

		iterator = commits.iterator();
		
		RevCommit candidate = null;
		RevCommit secondLastCandidate = null;
		RevCommit thirdLastCandidate = null;
		
		while (iterator.hasNext()) {
			
			if (secondLastCandidate !=  null)
				thirdLastCandidate = secondLastCandidate;
			
			if (candidate != null)
				secondLastCandidate = candidate;
			
			candidate = iterator.next();
			
			log.info("candidate = " + candidate.getId().name());
			
		}
		
		Assert.assertEquals(initialAggregateHeadId, thirdLastCandidate.getId());
		Assert.assertEquals(middleAggregateHeadId, secondLastCandidate.getId());
		Assert.assertEquals(aggregateHeadRevCommit.getId(), candidate.getId());
		
	}
	@Test
	public void testComparatorTopoEquivilenceWithThreeAggregateCommits () throws IOException {
		
		GitTreeProcessor treeProcessor = new GitTreeProcessor(repo);
		
		ObjectId initialAggregateHeadId = aggregateHeadRevCommit.getId();
		
		GitTreeData tree = treeProcessor.extractExistingTreeDataFromCommit(initialAggregateHeadId);
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		super.storeFile(inserter, tree, "Readme.txt", "test file content");
		
		ObjectId newCommit = commit(repo.newObjectInserter(), tree, "second commit on aggregate branch", initialAggregateHeadId);
		
		createBranch(newCommit, "aggregate");
		
		aggregateHeadRevCommit = rw.parseCommit(newCommit);
		
		// add a third commit
		
		ObjectId middleAggregateHeadId = newCommit;
		
		tree = treeProcessor.extractExistingTreeDataFromCommit(middleAggregateHeadId);
		
		super.storeFile(inserter, tree, "Readme.txt", "third file content");
		
		newCommit = commit(repo.newObjectInserter(), tree, "third commit on aggregate branch", middleAggregateHeadId);
		
		createBranch(newCommit, "aggregate");
		
		aggregateHeadRevCommit = rw.parseCommit(newCommit);
		
		
		/*
		 * Sort the commits using a comparator and make sure the results are the same as with the TopoGenerator
		 */
		
		List<RevCommit>commits = new LinkedList<RevCommit>();
		
		RevWalk rw = new RevWalk (repo);
		
		rw.markStart(branch1HeadRevCommit);
		rw.markStart(branch2HeadRevCommit);
		rw.markStart(aggregateHeadRevCommit);
		
		Iterator<RevCommit> iterator = rw.iterator();
		
		ObjectIdTranslationMapImpl translator= new ObjectIdTranslationMapImpl();
		
		RevCommitBitMapIndex index = new RevCommitBitMapIndex(repo, translator, iterator);
		
		commits = new LinkedList<RevCommit>(index.getRevCommitList());
		
		Collections.sort(commits, new FusionAwareTopoSortComparator(index));
		
		commits = new LinkedList<RevCommit>(index.getRevCommitList());
		
		Collections.sort(commits, new FusionAwareTopoSortComparator(index));

		iterator = commits.iterator();
		
		RevCommit candidate = null;
		RevCommit secondLastCandidate = null;
		RevCommit thirdLastCandidate = null;
		
		while (iterator.hasNext()) {
			
			if (secondLastCandidate !=  null)
				thirdLastCandidate = secondLastCandidate;
			
			if (candidate != null)
				secondLastCandidate = candidate;
			
			candidate = iterator.next();
			
			log.info("candidate = " + candidate.getId().name());
			
		}
		
		Assert.assertEquals(initialAggregateHeadId, thirdLastCandidate.getId());
		Assert.assertEquals(middleAggregateHeadId, secondLastCandidate.getId());
		Assert.assertEquals(aggregateHeadRevCommit.getId(), candidate.getId());
		
	}
	
	@Test
	public void testComparatorTopoEquivilenceWithTwoAggregateCommits () throws IOException {
		
		GitTreeProcessor treeProcessor = new GitTreeProcessor(repo);
		
		ObjectId initialAggregateHeadId = aggregateHeadRevCommit.getId();
		
		GitTreeData tree = treeProcessor.extractExistingTreeDataFromCommit(initialAggregateHeadId);
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		super.storeFile(inserter, tree, "Readme.txt", "test file content");
		
		ObjectId newCommit = commit(repo.newObjectInserter(), tree, "second commit on aggregate branch", initialAggregateHeadId);
		
		createBranch(newCommit, "aggregate");
		
		aggregateHeadRevCommit = rw.parseCommit(newCommit);
		
		/*
		 * Sort the commits using a comparator and make sure the results are the same as with the TopoGenerator
		 */
		
		List<RevCommit>commits = new LinkedList<RevCommit>();
		
		RevWalk rw = new RevWalk (repo);
		
		rw.markStart(branch1HeadRevCommit);
		rw.markStart(branch2HeadRevCommit);
		rw.markStart(aggregateHeadRevCommit);
		
		Iterator<RevCommit> iterator = rw.iterator();
		
		ObjectIdTranslationMapImpl translator= new ObjectIdTranslationMapImpl();
		
		RevCommitBitMapIndex index = new RevCommitBitMapIndex(repo, translator, iterator);
		
		commits = new LinkedList<RevCommit>(index.getRevCommitList());
		
		Collections.sort(commits, new FusionAwareTopoSortComparator(index));
		
		commits = new LinkedList<RevCommit>(index.getRevCommitList());
		
		Collections.sort(commits, new FusionAwareTopoSortComparator(index));

		iterator = commits.iterator();
		
		RevCommit candidate = null;
		RevCommit secondLastCandidate = null;
		
		while (iterator.hasNext()) {
			
			if (candidate != null)
				secondLastCandidate = candidate;
			
			candidate = iterator.next();
			
			log.info("candidate = " + candidate.getId().name());
			
		}
		
		Assert.assertEquals(initialAggregateHeadId, secondLastCandidate.getId());
		Assert.assertEquals(aggregateHeadRevCommit.getId(), candidate.getId());
		
	}
	
	@Test
	public void testComparatorTopoEquivilence() throws MissingObjectException, IncorrectObjectTypeException, IOException {
		/*
		 * Sort the commits using a comparator and make sure the results are the same as with the TopoGenerator
		 */
		
		List<RevCommit>commits = new LinkedList<RevCommit>();
		
		RevWalk rw = new RevWalk (repo);
		
		rw.markStart(branch1HeadRevCommit);
		rw.markStart(branch2HeadRevCommit);
		rw.markStart(aggregateHeadRevCommit);
		
		Iterator<RevCommit> iterator = rw.iterator();
		
		ObjectIdTranslationMapImpl translator= new ObjectIdTranslationMapImpl();
		
		RevCommitBitMapIndex index = new RevCommitBitMapIndex(repo, translator, iterator);
		
		commits = new LinkedList<RevCommit>(index.getRevCommitList());
		
		Collections.sort(commits, new FusionAwareTopoSortComparator(index));

		iterator = commits.iterator();
		
		RevCommit candidate = null;
		
		while (iterator.hasNext()) {
			candidate = iterator.next();
			
			log.info("candidate = " + candidate.getId().name());
			
		}
				
		Assert.assertEquals(aggregateHeadRevCommit.getId(), candidate.getId());
		
	}
	
	
	@Test
	public void testTopoEquivilence() throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		RevWalk rw = new RevWalk (repo);
		
		rw.markStart(branch1HeadRevCommit);
		rw.markStart(branch2HeadRevCommit);
		rw.markStart(aggregateHeadRevCommit);
		
		rw.sort(RevSort.REVERSE);
		
		rw.sort(RevSort.TOPO, true);
		
		Iterator<RevCommit> iterator = rw.iterator();

		Assert.assertEquals(true, iterator.hasNext());
		
		RevCommit candiate = iterator.next();
				
		Assert.assertEquals(branch2HeadRevCommit.getId(), candiate.getId());
		
		Assert.assertEquals(true, iterator.hasNext());
		
		candiate = iterator.next();
				
		Assert.assertEquals(aggregateHeadRevCommit.getId(), candiate.getId());
		
		Assert.assertEquals(true, iterator.hasNext());
		
		candiate = iterator.next();
				
		Assert.assertEquals(branch1HeadRevCommit.getId(), candiate.getId());
		
		rw.close();
	}
	
	

}
