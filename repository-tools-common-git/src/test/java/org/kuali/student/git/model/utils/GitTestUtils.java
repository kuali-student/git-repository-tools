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
package org.kuali.student.git.model.utils;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TreeFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Assert;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.exception.BranchRefExistsException;
import org.kuali.student.git.model.ref.utils.GitRefUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author ocleirig
 *
 */
public final class GitTestUtils {

	/**
	 * 
	 */
	private GitTestUtils() {
	}

    public static void assertNumberOfCommitParents(org.eclipse.jgit.lib.Repository repository, String branchName, int expectedNumberOfParents) throws java.io.IOException {
        Ref branchRef = repository.findRef(branchName);

        Assert.assertNotNull("Expected a branch to exist for " + branchName, branchRef);

        RevWalk rw = new org.eclipse.jgit.revwalk.RevWalk(repository);

        org.eclipse.jgit.revwalk.RevCommit commit = rw.parseCommit(branchRef.getObjectId());

        Assert.assertEquals("Expected most recent commit on branch: " + branchName + " to have " + expectedNumberOfParents + " parents.", expectedNumberOfParents, commit.getParentCount());

        rw.close();


    }
	
	public static void assertPathsExist(Repository repository, String branchName,
			List<String> pathList) throws IOException {

		Ref ref = repository.getRef(Constants.R_HEADS + branchName);
		
		Assert.assertNotNull(ref);
		
		RevWalk rw = new RevWalk(repository);
		
		RevCommit commit = rw.parseCommit(ref.getObjectId());
		
		TreeWalk tw = new TreeWalk(repository);
		
		tw.addTree(commit.getTree().getId());
		
		tw.setRecursive(true);
		
		Set<String>unmatchedPaths = new HashSet<>();
		
		unmatchedPaths.addAll(pathList);
		
		while (tw.next()) {
			
			String path = tw.getPathString();
			
			Iterator<String> it = unmatchedPaths.iterator();
			
			while (it.hasNext()) {
				
				String um = it.next();
				
				if (path.startsWith(um))
					it.remove();
				
			}
			
			
		}
		
		Assert.assertEquals(0, unmatchedPaths.size());
		
		tw.close();
		
		rw.close();
		
	}
	
	/**
	 * Ensure that the paths given to not exist in the current branch HEAD.
	 * 
	 * @param repository
	 * @param branchName
	 * @param pathList
	 * @throws IOException
	 */
	public static void assertPathsDontExist(Repository repository, String branchName,
			List<String> pathList) throws IOException {

		Ref ref = repository.getRef(Constants.R_HEADS + branchName);
		
		Assert.assertNotNull(ref);
		
		RevWalk rw = new RevWalk(repository);
		
		RevCommit commit = rw.parseCommit(ref.getObjectId());
		
		TreeWalk tw = new TreeWalk(repository);
		
		tw.addTree(commit.getTree().getId());
		
		tw.setRecursive(true);
		
		Set<String>unmatchedPaths = new HashSet<>();
		
		unmatchedPaths.addAll(pathList);
		
		int originalSize = unmatchedPaths.size();
		
		while (tw.next()) {
			
			String path = tw.getPathString();
			
			Iterator<String> it = unmatchedPaths.iterator();
			
			while (it.hasNext()) {
				
				String um = it.next();
				
				if (path.startsWith(um))
					it.remove();
				
			}
			
			
		}
		
		Assert.assertEquals(originalSize, unmatchedPaths.size());
		
		tw.close();
		
		rw.close();
		
	}

	public static void createBranch(Repository repository, String branchName,
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
		inserter.close();
	}

	public static void assertRefNotNull(Repository repo, String expectedBranchName, String onNullMessage) throws IOException {

		Ref trunk = repo.getRef(expectedBranchName);
		
		Assert.assertNotNull(onNullMessage, trunk);
	}
	
	public static void assertRefNull(Repository repo, String expectedBranchName, String notNullMessage) throws IOException {

		Ref trunk = repo.getRef(expectedBranchName);
		
		Assert.assertNull(notNullMessage, trunk);
	}

	

	public static void assertFileContentEquals(Repository repository, String branchName,
			String filePath, String expectedContent) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		ObjectLoader loader = loadFileContents(repository, branchName, filePath);
		
		String actualContent = new String (loader.getBytes());
		
		Assert.assertEquals(expectedContent, actualContent);
		
	}
	
	/**
	 * Acquire the JGit ObjectLoader reference on the file in the branch in the repository we want.
	 * 
	 * This gives the caller the option of extracting it as a string or passing it as an InputStream into some other functionality.
	 * 
	 * @param repository
	 * @param branchName
	 * @param filePath
	 * @return the JGit ObjectLoader for the branch path given.
	 * @throws IOException
	 */
	public static ObjectLoader loadFileContents(Repository repository, String branchName,
			String filePath) throws IOException {
		
		return repository.newObjectReader().open(findObjectIdInRefByPath(repository, branchName, filePath));
		
	}
	
	public static ObjectId findObjectIdInRefByPath (Repository repo, String refName, String path) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		Ref ref = repo.getRef(refName);
		
		ObjectId objectId = findObjectIdInRefByPath(repo, ref, path);
		
		return objectId;
		
	}
	
	public static ObjectId findObjectIdInRefByPath (Repository repo, Ref ref, String path) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		Assert.assertNotNull(ref);
		
		ObjectId commitId = ref.getObjectId();
		
		ObjectId objectId = GitRepositoryUtils.findInCommit(repo, commitId, path);
		
		Assert.assertNotNull(objectId);
		
		return objectId;
	}

}
