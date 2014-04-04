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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.tree.GitTreeData;

/**
 * 
 * Helpful for creating test cases against a test git repository.
 * 
 * @author Kuali Student Team
 *
 */
public abstract class AbstractGitRespositoryTestCase {

	protected Repository repo;
	
	private String name;

	private boolean bare;
	
	/**
	 * 
	 */
	public AbstractGitRespositoryTestCase(String name) {
		this (name, false);
	}
	public AbstractGitRespositoryTestCase(String name, boolean bare) {
		this.name = name;
		this.bare = bare;
		
	}
	
	@Before
	public final void before() throws Exception {
		
		File gitRepository = new File ("target/"+name+"/git-repo");
		
		FileUtils.deleteDirectory(gitRepository);
		
		gitRepository.mkdirs();
		
		repo = GitRepositoryUtils
				.buildFileRepository(gitRepository, true, bare);
		
		onBefore();
	}
	
	@After
	public final void after () throws Exception {
		onAfter();
	}

	protected void onAfter() throws Exception {
		
	}
	
	protected void onBefore() throws Exception {

	}
	
	protected Result createBranch(ObjectId objectId, String branchName) throws IOException {

		RefUpdate update = repo.updateRef(Constants.R_HEADS + branchName);
		
		update.setNewObjectId(objectId);
		
		update.setForceUpdate(true);
		
		return update.update();
	}

	protected ObjectId commit(ObjectInserter inserter, GitTreeData branch, String commitMessage) throws IOException {
		return commit (inserter, branch, commitMessage, null);
	}
	protected ObjectId commit(ObjectInserter inserter, GitTreeData branch, String commitMessage, ObjectId parentId) throws IOException {

		ObjectId treeId = branch.buildTree(inserter);
		
		CommitBuilder cb = new CommitBuilder();
		
		cb.setMessage(commitMessage);
		
		PersonIdent pid = new PersonIdent("admin", "admin@kuali.org");
		
		cb.setAuthor(pid);
		
		cb.setCommitter(pid);

		cb.setTreeId(treeId);
		
		if (parentId != null)
			cb.setParentId(parentId);
		
		return inserter.insert(cb);
	}

	protected void storeFile(ObjectInserter inserter, GitTreeData branch, String path, String fileContent) throws IOException {

		ObjectId blobSha1 = inserter.insert(Constants.OBJ_BLOB, fileContent.getBytes());
		
		branch.addBlob(path, blobSha1.name());
		
	}

}
