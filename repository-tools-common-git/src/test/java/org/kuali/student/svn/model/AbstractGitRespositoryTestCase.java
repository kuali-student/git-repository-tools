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
package org.kuali.student.svn.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.junit.After;
import org.junit.Assert;
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
	
	protected RevWalk rw;

	private String name;

	private boolean bare;

	/**
	 * 
	 */
	public AbstractGitRespositoryTestCase(String name) {
		this(name, false);
	}

	public AbstractGitRespositoryTestCase(String name, boolean bare) {
		this.name = name;
		this.bare = bare;

	}

	@Before
	public final void before() throws Exception {

		File gitRepository = new File("target/" + name + "/git-repo");

		FileUtils.deleteDirectory(gitRepository);

		gitRepository.mkdirs();

		repo = GitRepositoryUtils
				.buildFileRepository(gitRepository, true, bare);

		this.rw = new RevWalk(repo);
		
		onBefore();
	}

	@After
	public final void after() throws Exception {
		onAfter();
		
		this.rw.release();
		this.repo.close();
	}

	protected void onAfter() throws Exception {

	}

	protected void onBefore() throws Exception {

	}

	protected List<String>getBlobContents (ObjectId blobId) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		ObjectLoader loader = repo.newObjectReader().open(blobId, Constants.OBJ_BLOB);
		
		List<String> lines = IOUtils.readLines(loader.openStream(), "UTF-8");
		
		return lines;
	}
	
	protected void assertBlobContents (ObjectId blobId, int lineNumber, String expectedContent) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		List<String> contents = getBlobContents(blobId);
		
		Assert.assertTrue (lineNumber < contents.size());
		
		String line = contents.get(lineNumber);
		
		Assert.assertEquals(expectedContent, line);
		
		
		
	}
	protected String diffTrees(ObjectId tree1, ObjectId tree2) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {

		StringBuilder builder = new StringBuilder();

		final TreeWalk walk = new TreeWalk(repo);
		walk.setRecursive(true);

		walk.addTree(tree1);
		walk.addTree(tree2);

		walk.setFilter(TreeFilter.ANY_DIFF);

		int nTree = 2;

		while (walk.next()) {
			for (int i = 1; i < nTree; i++)
				builder.append(':');
			for (int i = 0; i < nTree; i++) {
				final FileMode m = walk.getFileMode(i);
				final String s = m.toString();
				for (int pad = 6 - s.length(); pad > 0; pad--)
					builder.append('0');
				builder.append(s);
				builder.append(' ');
			}

			for (int i = 0; i < nTree; i++) {
				builder.append(walk.getObjectId(i).name());
				builder.append(' ');
			}

			char chg = 'M';
			if (nTree == 2) {
				final int m0 = walk.getRawMode(0);
				final int m1 = walk.getRawMode(1);
				if (m0 == 0 && m1 != 0)
					chg = 'A';
				else if (m0 != 0 && m1 == 0)
					chg = 'D';
				else if (m0 != m1 && walk.idEqual(0, 1))
					chg = 'T';
			}
			builder.append(chg);

			builder.append('\t');
			builder.append(walk.getPathString());
			builder.append("\n");

		}

		walk.release();
		
		return builder.toString();
	}

	protected Result createBranch(ObjectId objectId, String branchName)
			throws IOException {

		RefUpdate update = repo.updateRef(Constants.R_HEADS + branchName);

		update.setNewObjectId(objectId);

		update.setForceUpdate(true);

		return update.update();
	}

	protected ObjectId commit(ObjectInserter inserter, GitTreeData branch,
			String commitMessage) throws IOException {
		return commit(inserter, branch, commitMessage, null);
	}

	protected ObjectId commit(ObjectInserter inserter, GitTreeData branch,
			String commitMessage, ObjectId parentId) throws IOException {

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

	protected void storeFile(ObjectInserter inserter, GitTreeData branch,
			String path, String fileContent) throws IOException {

		ObjectId blobSha1 = inserter.insert(Constants.OBJ_BLOB,
				fileContent.getBytes());

		branch.addBlob(path, blobSha1);

	}

}
