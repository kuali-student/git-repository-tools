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
package org.kuali.student.svn.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.SvnExternalsUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeDataUtils;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestExternalsFusion  extends AbstractGitRespositoryTestCase {

	private static Logger log = LoggerFactory.getLogger(TestExternalsFusion.class);
	
	/**
	 * 
	 */
	public TestExternalsFusion() {
		super ("externals-fusion");
	}

	@Test
	public void testInverseFusion() throws IOException {
		
		/*
		 * In this case we have a fused commit and we want to break it into seperate commits to the originating branches.
		 */
		ObjectInserter inserter = repo.newObjectInserter();
		
		// create branch 1
		GitTreeData branch1 = new GitTreeData();
		
		String branch1FilePath = "src/main/java/org/kuali/student/enrollment/test.txt";
		storeFile (inserter, branch1, branch1FilePath, "test");
		
		ObjectId b1Id = commit (inserter, branch1, "created branch1");
		
		Result result = createBranch(b1Id, "branch1");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		// create branch 2
		GitTreeData branch2 = new GitTreeData();
		
		String branch2FilePath = branch1FilePath;
		
		storeFile (inserter, branch2, branch2FilePath, "test");
		
		ObjectId b2Id = commit (inserter, branch2, "created branch2");
		
		result = createBranch(b2Id, "branch2");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		// create aggregate
		GitTreeData aggregate = new GitTreeData();
		
		String aggregate_file_path = "src/main/java/org/kuali/student/enrollment/pom.xml";
		storeFile (inserter, aggregate, aggregate_file_path, "pom test");
		
		ObjectId aggregateId = commit (inserter, aggregate, "created aggregate");
		
		result = createBranch(aggregateId, "aggregate");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		// a fused commit
		GitTreeData fusedAggregate = GitTreeDataUtils.extractExistingTreeData(new GitTreeProcessor(repo), aggregateId);

		String fusedAPath = "branch1/src/main/resources/fusedA.txt";
		storeFile(inserter, fusedAggregate, fusedAPath, "fusedA content");
		
		String fusedBPath = "branch2/src/main/resources/fusedB.txt";
		storeFile(inserter, fusedAggregate, fusedBPath, "fusedB content");
		
		// not sure if we need to split the aggregate so don't for now.
		
		aggregateId = commit (inserter, fusedAggregate, "fusion commit to the aggregate");
		
		result = createBranch(aggregateId, "aggregate");
		
		Assert.assertEquals(Result.FORCED, result);
		
		ObjectReader objectReader = repo.newObjectReader();
		RevWalk rw = new RevWalk(objectReader);
		
		TreeWalk tw = new TreeWalk (objectReader);
		
		tw.setRecursive(true);
		
		ExternalModuleInfo branch1Externals = new ExternalModuleInfo("branch1", "branch1", 1);
		ExternalModuleInfo branch2Externals = new ExternalModuleInfo("branch2", "branch2", 1);
		
		Map<String, ObjectId>results = SvnExternalsUtils.splitFusedTree(objectReader, inserter, rw, aggregateId, Arrays.asList(new ExternalModuleInfo[] {branch1Externals, branch2Externals}));

		Assert.assertEquals(true, results.containsKey("branch1"));
		
		tw.addTree(results.get("branch1"));
		
		Assert.assertEquals(true, findPath(tw, fusedAPath.substring("branch1/".length())));
		Assert.assertEquals(false, findPath(tw, fusedBPath.substring("branch1/".length())));
		
		Assert.assertEquals(true, results.containsKey("branch2"));
		
		tw.reset(results.get("branch2"));
		
		Assert.assertEquals(true, findPath(tw, fusedBPath.substring("branch2/".length())));
		Assert.assertEquals(false, findPath(tw, fusedAPath.substring("branch2/".length())));
		
		rw.release();
		
		objectReader.release();
		
		inserter.release();
		
	}
	
	@Test
	public void testFusion() throws IOException {
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		// create branch 1
		GitTreeData branch1 = new GitTreeData();
		
		String branch1FilePath = "src/main/java/org/kuali/student/enrollment/test.txt";
		storeFile (inserter, branch1, branch1FilePath, "test");
		
		ObjectId b1Id = commit (inserter, branch1, "created branch1");
		
		Result result = createBranch(b1Id, "branch1");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		// create branch 2
		GitTreeData branch2 = new GitTreeData();
		
		String branch2FilePath = branch1FilePath;
		
		storeFile (inserter, branch2, branch2FilePath, "test");
		
		ObjectId b2Id = commit (inserter, branch2, "created branch2");
		
		result = createBranch(b2Id, "branch2");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		// create aggregate
		GitTreeData aggregate = new GitTreeData();
		
		String aggregate_file_path = "src/main/java/org/kuali/student/enrollment/pom.xml";
		storeFile (inserter, aggregate, aggregate_file_path, "pom test");
		
		ObjectId aggregateId = commit (inserter, aggregate, "created aggregate");
		
		result = createBranch(aggregateId, "aggregate");
		
		Assert.assertEquals(Result.NEW, result);
		
		inserter.flush();
		
		ExternalModuleInfo branch1Externals = new ExternalModuleInfo("branch1", "branch1", 1, b1Id);
		ExternalModuleInfo branch2Externals = new ExternalModuleInfo("branch2", "branch2", 1, b2Id);
		
		
		ObjectReader objectReader = repo.newObjectReader();
		RevWalk rw = new RevWalk(objectReader);
		
		RevCommit aggregateCommit = rw.parseCommit(aggregateId);
		
		AnyObjectId fusedTreeId = SvnExternalsUtils.createFusedTree(objectReader, inserter, rw, aggregateCommit, Arrays.asList(new ExternalModuleInfo[] {branch1Externals, branch2Externals}));
		
		TreeWalk tw = new TreeWalk (objectReader);
		
		tw.setRecursive(true);
		
		tw.addTree(fusedTreeId);
		
		Assert.assertEquals(true, findPath (tw, "branch1/" + branch1FilePath));
		
		tw.reset(fusedTreeId);
		
		Assert.assertEquals(true, findPath (tw, "branch2/" + branch1FilePath));
		
		tw.reset(fusedTreeId);
		
		tw.setFilter(PathFilter.create(aggregate_file_path));
		
		Assert.assertEquals(true, findPath (tw, aggregate_file_path));
		
		tw.release();
		
		rw.release();
		
		objectReader.release();
		
	}

	private boolean findPath(TreeWalk tw, String targetPath) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		while (tw.next()) {
			
			String candidatePath = tw.getPathString();
			
			if (candidatePath.equals(targetPath))
				return true;
		}
		return false;
	}
}
