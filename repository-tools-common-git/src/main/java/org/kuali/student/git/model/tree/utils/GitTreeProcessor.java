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
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.GitTreeNodeData;
import org.kuali.student.git.model.tree.GitTreeNodeInitializer;
import org.kuali.student.git.model.tree.GitTreeNodeInitializerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class GitTreeProcessor  {

	private static final Logger log = LoggerFactory.getLogger(GitTreeProcessor.class);
	
	private Repository repo;

	private ObjectReader objectReader;

	private GitTreeNodeInitializer nodeInitializer;

	/**
	 * 
	 */
	public GitTreeProcessor(Repository repo) {
		this.repo = repo;
		this.nodeInitializer = new GitTreeNodeInitializerImpl(this);
		this.objectReader = repo.newObjectReader();
		
	}
	
	
	
	/**
	 * @return the nodeInitializer
	 */
	public GitTreeNodeInitializer getNodeInitializer() {
		return nodeInitializer;
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

	/**
	 * Extract the existing Git Tree Data for the commit indicated.
	 * 
	 * We index the blob's but also note the object id's of the tree's so that we can optimize
	 * creation of the new tree data at the end.
	 * 
	 * i.e. only have to create new trees for the data that has changed.
	 * 
	 * @param parentId
	 * @return the fully constructed mutable GitTreeData structure representing the tree committed in the indicated parent commit.
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws IOException
	 */
	
	public GitTreeData extractExistingTreeDataFromCommit(ObjectId parentId) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		ObjectId treeId = getTreeId (parentId);
		
		GitTreeData tree = new GitTreeData(nodeInitializer);
		
		if (treeId == null)
			return tree;

		GitTreeNodeData root = extractExistingTreeData(treeId, "");
		
		tree.setRoot(root);
		
		return tree;
		
	}
	
	public GitTreeNodeData extractExistingTreeData (ObjectId treeId, String name) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		GitTreeNodeData treeData = new GitTreeNodeData(nodeInitializer, name);
		
		treeData.setGitTreeObjectId(treeId);
		
		TreeWalk tw = new TreeWalk(repo);
		
		tw.setRecursive(false);
		
		tw.addTree(treeId);
		
		while (tw.next()) {
			
			FileMode fileMode = tw.getFileMode(0);
			
			String entryName = tw.getNameString();
			
			ObjectId objectId = tw.getObjectId(0);
			
			if (fileMode.equals(FileMode.TREE)) {
				
				GitTreeNodeData subTree = new GitTreeNodeData(nodeInitializer, entryName);
				
				subTree.setGitTreeObjectId(objectId);
				
				treeData.addDirectTree(entryName, subTree);
				
				
			}
			else if (fileMode.equals(FileMode.REGULAR_FILE)) {
				treeData.addDirectBlob(entryName, objectId);
			}
		}
		
		/*
		 * This tree is initialized the subtree's are not.
		 */
		treeData.setInitialized(true);
		treeData.setDirty(false);
		
		tw.release();

		return treeData;
		
	}

	public ObjectId getTreeId(ObjectId parentId) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		return getTreeId(parentId, "");
	}



	public ObjectId getTreeId(ObjectId parentId, String branchSubPath) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		ObjectId treeId = null;
		
		RevWalk rw = new RevWalk(repo);
		
		RevCommit parentCommit = rw.parseCommit(parentId);
		
		rw.release();
		
		String[] subPathParts = branchSubPath.split("/");
		
		int currentPartIndex = 0;
		
		if (branchSubPath != null && !branchSubPath.isEmpty()) {
			
			TreeWalk tw = new TreeWalk(repo);
			
			tw.addTree(parentCommit.getTree().getId());
			
			while (tw.next()) {
				
				String currentPathPart = subPathParts[currentPartIndex];
				
				if (tw.getFileMode(0).equals(FileMode.TYPE_TREE)) {
					String name = tw.getNameString();
					
					if (name.equals(currentPathPart)) {
						currentPartIndex++;
						
						if (currentPartIndex >= subPathParts.length) {
							// we are done
							treeId = tw.getObjectId(0);
							break;
						}
						else {
							tw.enterSubtree();
						}
					}
					
				}
			}
			
			tw.release();
			
			return treeId;
		}
		
		
		if (parentCommit == null)
			return null;
		else
			return parentCommit.getTree().getId();
	}

}
