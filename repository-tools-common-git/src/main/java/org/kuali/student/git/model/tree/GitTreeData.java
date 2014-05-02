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
package org.kuali.student.git.model.tree;

import java.io.IOException;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a git tree or directory of blob's and trees.
 * 
 * @author Kuali Student Team
 * 
 */
public class GitTreeData {

	private static final Logger log = LoggerFactory
			.getLogger(GitTreeData.class);

	public static interface GitTreeDataVisitor {

		/**
		 * 
		 * @param path
		 * @param objectId
		 * @return false if no more blobs should be visted
		 */
		public boolean visitBlob(String path, ObjectId objectId);

	}

	
	
	
	
	
	
	
	
	

	protected GitTreeNodeData root;
	private GitTreeNodeInitializer nodeInitializer;

	public static class GitMergeData {

		private String copyFromRevision;
		private String copyFromPath;
		private String copySha1;
		private boolean directoryAddByCopy;

		/**
		 * @param copyFromRevision
		 * @param copyFromPath
		 * @param copySha1
		 * @param directoryAddByCopy
		 */
		public GitMergeData(String copyFromRevision, String copyFromPath,
				String copySha1, boolean directoryAddByCopy) {
			super();
			this.copyFromRevision = copyFromRevision;
			this.copyFromPath = copyFromPath;
			this.copySha1 = copySha1;
			this.directoryAddByCopy = directoryAddByCopy;
		}

		/**
		 * @return the copyFromRevision
		 */
		public String getCopyFromRevision() {
			return copyFromRevision;
		}

		/**
		 * @return the copyFromPath
		 */
		public String getCopyFromPath() {
			return copyFromPath;
		}

		/**
		 * @return the copySha1
		 */
		public String getCopySha1() {
			return copySha1;
		}

		/**
		 * @return the directoryAddByCopy
		 */
		public boolean isDirectoryAddByCopy() {
			return directoryAddByCopy;
		}

	}

	/**
	 * 
	 */
	public GitTreeData(GitTreeNodeInitializer nodeInitializer) {
		this.nodeInitializer = nodeInitializer;
		root = new GitTreeNodeData(nodeInitializer, "");
	}

	public void addBlob(String filePath, ObjectId blobSha1) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		root.addBlob(filePath, blobSha1);

	}

	public ObjectId buildTree(ObjectInserter inserter) throws IOException {

		return root.buildTree(inserter);

	}

	/**
	 * @param path
	 * @return true if one or more blob(s) were deleted.
	 * @see org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData#deletePath(java.lang.String)
	 */
	public boolean deletePath(String path) {
		return root.deletePath(path);
	}

	public void visit(GitTreeDataVisitor vistor) {
		root.visit(vistor);

	}

	public void setGitTreeObjectId(ObjectId id) {
		this.root.setGitTreeObjectId(id);

	}

	public void addTree(GitTreeProcessor treeProcessor, String path, ObjectId treeId) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		this.root.addTree(treeProcessor, path, treeId);

	}

	/**
	 * 
	 * @see org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData#resetDirtyFlag()
	 */
	public void resetDirtyFlag() {
		root.resetDirtyFlag();
	}

	public ObjectId find(Repository repo, String path)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException {
		return root.find(repo, path);
	}

	public boolean isTreeDirty() {
		return root.isDirty();
	}

	public void setRoot(GitTreeNodeData root) {

		this.root = root;
	}


}
