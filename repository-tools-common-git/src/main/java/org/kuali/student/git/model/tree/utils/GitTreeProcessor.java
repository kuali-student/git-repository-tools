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
package org.kuali.student.git.model.tree.utils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class GitTreeProcessor {

	private static final Logger log = LoggerFactory.getLogger(GitTreeProcessor.class);
	
	private Repository repo;

	private ObjectReader objectReader;

	/**
	 * 
	 */
	public GitTreeProcessor(Repository repo) {
		this.repo = repo;
		this.objectReader = repo.newObjectReader();
		
	}
	
	public static interface GitTreeBlobVisitor {
		/**
		 * 
		 * @param blobId
		 * @param path
		 * @return true if the visiting should continue; false if it should stop.
		 * 
		 */
		public boolean visitBlob(ObjectId blobId, String path, String name) throws MissingObjectException, IncorrectObjectTypeException, IOException;
	}
	
	public static interface GitTreePathVisitor {
		
		public boolean visitPath (String path, String name);
		
	}
	
	public void visitBlobs (ObjectId commitId, GitTreeBlobVisitor visitor) throws MissingObjectException, IncorrectObjectTypeException, IOException {
	
		visitBlobs(commitId, visitor, null);
	}
	
	public void visitBlobs (ObjectId commitId, GitTreeBlobVisitor visitor, TreeFilter treeFilter) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		RevWalk walk = new RevWalk(repo);

		RevCommit commit = walk.parseCommit(commitId);

		// a commit points to a tree
		ObjectId treeId = commit.getTree().getId();
		
		TreeWalk treeWalk = new TreeWalk(repo);

		treeWalk.addTree(treeId);

		treeWalk.setRecursive(true);
		
		if (treeFilter != null)
			treeWalk.setFilter(treeFilter);

		if (treeWalk.getTreeCount() == 0) {
			log.warn("no trees to parse");
		}

		while (treeWalk.next()) {

			final FileMode mode = treeWalk.getFileMode(0);

			if (mode != FileMode.REGULAR_FILE)
				continue;

			/*
			 * We only want the blob's
			 */
			ObjectId blobId = treeWalk.getObjectId(0);

			String path = treeWalk.getPathString();
			
			String name = treeWalk.getNameString();
			
			if (!visitor.visitBlob(blobId, path, name))
				return; // stop when the visitor indicates its done.
		}
		
		treeWalk.release();
		walk.release();
		
	}
	
	/**
	 * Compute if the path given exists in the commit tree.
	 * 
	 * @param commitId
	 * @param path
	 * @return true if the path exists in the commit tree.
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws IOException
	 */
	public boolean treeContainsPath (ObjectId commitId, String path) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		String pathParts[] = path.split("\\/");
		
		RevWalk walk = new RevWalk(repo);

		RevCommit commit = walk.parseCommit(commitId);

		// a commit points to a tree
		ObjectId treeId = commit.getTree().getId();
		
		TreeWalk treeWalk = new TreeWalk(repo);
		
		treeWalk.addTree(treeId);

		boolean containsPath = checkPath (treeWalk, pathParts, 0);
		
		treeWalk.release();
		walk.release();

		return containsPath;
	}

	private boolean checkPath(TreeWalk treeWalk, String[] pathParts, int currentPathPartIndex) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		if (currentPathPartIndex == pathParts.length) {
			return true;
		}
		
		String currentPathPart = pathParts[currentPathPartIndex];
		
		while (treeWalk.next()) {
			
			if (treeWalk.getFileMode(0) != FileMode.TREE)
				continue;
			
			if (currentPathPart.equals(treeWalk.getNameString())) {
				
				ObjectId treeId = treeWalk.getObjectId(0);
				
				treeWalk.reset(treeId);
				
				return checkPath(treeWalk, pathParts, (currentPathPartIndex+1));
			}
				
		}
		
		return false;
		
	}
	
	public ObjectLoader getBlob(ObjectId blobId) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		return objectReader.open(blobId, Constants.OBJ_BLOB);
	}
	
	public List<String> getBlobAsStringLines(ObjectId blobId) throws MissingObjectException, IOException {
		
		ObjectLoader loader = getBlob(blobId);
		
		return IOUtils.readLines(loader.openStream());
		
	}

}
