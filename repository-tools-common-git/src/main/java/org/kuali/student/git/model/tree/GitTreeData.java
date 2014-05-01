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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TreeFormatter;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData;
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

	private interface ResourceContext {
		
		public void storeResource(String name, String[] parts, int partOffset, GitTreeNodeData node);
		
		public String getErrorMessage();
	}
	
	private abstract class AbstractResourceContext implements ResourceContext {

		protected ObjectId objectId;
		private String type;

		/**
		 * 
		 */
		public AbstractResourceContext(String type, ObjectId objectId) {
			super();
			this.type = type;
			this.objectId = objectId;
		}

		/* (non-Javadoc)
		 * @see org.kuali.student.git.model.tree.GitTreeData.ResourceContext#getErrorMessage()
		 */
		@Override
		public String getErrorMessage() {
			return new StringBuilder(" ").append(type).append("Id = ").append(objectId).toString();
		}
		
		
		
	}
	
	private class BlobResourceContext extends AbstractResourceContext {

		/**
		 * 
		 */
		public BlobResourceContext(ObjectId blobId) {
			super("blob", blobId);
		}

		/* (non-Javadoc)
		 * @see org.kuali.student.git.model.tree.GitTreeData.ResourceContext#storeResource(org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData)
		 */
		@Override
		public void storeResource(String name, String[] parts, int partOffset, GitTreeNodeData node) {
			
			ObjectId existing = node.blobReferences.put(name, objectId);
			
			if (existing != null) {
				/*
				 * This is normally ok. This would typically be a change to
				 * a file that existed in the previous commit.
				 * 
				 * This was added originally to find the data loss that had
				 * been occuring.
				 */
				log.debug("overwriting blob = " + name);
			}
		}

	}
	
	private class TreeResourceContext extends AbstractResourceContext {

		/**
		 * @param type
		 * @param objectId
		 */
		public TreeResourceContext(ObjectId objectId) {
			super("tree", objectId);
		}

		/* (non-Javadoc)
		 * @see org.kuali.student.git.model.tree.GitTreeData.ResourceContext#storeResource(java.lang.String, org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData)
		 */
		@Override
		public void storeResource(String name, String[] parts, int partOffset, GitTreeNodeData node) {
			
			GitTreeNodeData newNode = new GitTreeNodeData(name, node.getNodePath(name,
					parts, partOffset));
			
			newNode.setGitTreeObjectId(objectId);
		
			GitTreeNodeData existing = node.subTreeReferences.put(name, newNode);
			
			if (existing != null){
				log.warn("overwriting node " + getErrorMessage());
			}
			
		}
		
		
		
	}
	
	public class GitTreeNodeData {

		private String name;

		private String nodePath;

		private Map<String, ObjectId> blobReferences = new HashMap<String, ObjectId>();

		private Map<String, GitTreeNodeData> subTreeReferences = new HashMap<String, GitTreeData.GitTreeNodeData>();

		private ObjectId originalTreeObjectId;

		// set to true when this node has been changed
		// or a child that crosses this nodes path has been changed.
		private boolean dirty = false;

		// set to true when this nodes subtrees have been loaded
		private boolean initialized = false;

		/**
		 * @param string
		 * 
		 */
		public GitTreeNodeData(String name, String nodePath) {
			super();
			this.name = name;
			this.nodePath = nodePath;
		}

		public String getNodePath(String name, String[] parts, int partOffset) {
			String joinedPath = StringUtils.join(parts, "/", 0, partOffset + 1);

			return joinedPath;
		}
		
		public boolean deletePath(String path) {

			// remove all the blobs contained in the path given.

			String[] parts = path.split("/");

			return deletePath(parts, 0);
		}

		/**
		 * @return the originalTreeObjectId
		 */
		public ObjectId getOriginalTreeObjectId() {
			return originalTreeObjectId;
		}

		private void initializeSubTrees() {

			if (!this.initialized) {

				this.initialized = true;

				try {
					GitTreeData.this.nodeInitializer.initialize(this);
				} catch (Exception e) {
					log.error("failed to initialize node: " + this.nodePath, e);
				}

			}
		}

		/**
		 * @return the subtreeInitialized
		 */
		public boolean isInitialized() {
			return initialized;
		}

		private boolean deletePath(String[] parts, int partOffset) {

			int difference = (parts.length - partOffset);

			if (difference == 0) {
				// should never occur
				throw new RuntimeException(
						"should not delete all branch content should be deleting the branch");
			}

			String name = parts[partOffset];

			if (difference == 1) {
				
				if (!this.isInitialized()) {
					try {
						nodeInitializer.initialize(this);
					} catch (Exception e) {
						throw new RuntimeException("nodeInitializer Failed", e);
					}
				}

				boolean blobReference = false;
				boolean treeReference = false;

				if (blobReferences.containsKey(name)) {
					blobReferences.remove(name);
					blobReference = true;
					setDirty(true);
				} else if (subTreeReferences.containsKey(name)) {
					subTreeReferences.remove(name);
					treeReference = true;
					setDirty(true);
				}

				if (blobReference == false && treeReference == false) {
					String lastPart = parts[parts.length - 1];

					if (lastPart.contains("\\."))
						log.warn("failed to delete any tree or blob for "
								+ name);
				}

				if (blobReference || treeReference)
					return true;
				else
					return false;
			} else {
				// > 1
				// need to get down a level.
				GitTreeNodeData leaf = subTreeReferences.get(name);

				if (leaf == null) {
					String lastPart = parts[parts.length - 1];

					if (lastPart.contains("\\."))
						log.warn("missing leaf blob = " + name);
				} else {

					if (!this.isInitialized()) {
						try {
							nodeInitializer.initialize(leaf);
						} catch (Exception e) {
							throw new RuntimeException("nodeInitializer Failed", e);
						}
					}
					/*
					 * bubble up the dirty flag to the root along the deleted
					 * path.
					 */
					if (leaf.deletePath(parts, partOffset + 1)) {
						setDirty(true);

						return true;
					} else
						return false;
				}
			}

			return false;

		}

		/**
		 * @return the dirty
		 */
		public boolean isDirty() {

			return dirty;

		}

		/**
		 * @param dirty
		 *            the dirty to set
		 */
		public void setDirty(boolean dirty) {
			this.dirty = dirty;
		}

		public boolean addBlob(String filePath, ObjectId blobId) {
			return addResource(filePath, new BlobResourceContext(blobId));
			
		}
		
		private boolean addResource (String filePath, ResourceContext context) {

			String[] parts = filePath.split("\\/");

			if (parts.length > 0) {
				return this.addResource(parts, 0, context);
			} else {
				log.info("failed to add " + context.getErrorMessage());
				return false;
			}

		}
		
		
		private boolean addResource (String[] parts, int partOffset, ResourceContext context) {
			
			int difference = (parts.length - partOffset);

			if (difference == 0) {
				// should never occur
				throw new RuntimeException("too deep");
			}

			String name = parts[partOffset];

			if (name.isEmpty()) {
				log.info("name is empty at partOffset= " + partOffset
						+ context.getErrorMessage());

			}

			if (difference == 1) {
				
				if (!this.isInitialized()) {
					try {
						nodeInitializer.initialize(this);
					} catch (Exception e) {
						throw new RuntimeException("nodeInitializer Failed", e);
					}
				}

				context.storeResource(name, parts, partOffset, this);

				this.setDirty(true);

				return true;
			} else {
				// > 1
				// need to get down a level.
				GitTreeNodeData leaf = subTreeReferences.get(name);

				if (leaf == null) {
					leaf = new GitTreeNodeData(name, getNodePath(name, parts,
							partOffset));
					subTreeReferences.put(name, leaf);
				}

				if (!leaf.isInitialized()) {
					try {
						nodeInitializer.initialize(leaf);
					} catch (Exception e) {
						throw new RuntimeException("nodeInitializer Failed", e);
					}
				}
				if (leaf.addResource(parts, partOffset + 1, context)) {
					setDirty(true);
					return true;
				} else
					return false;
			}
		}

		

		public ObjectId buildTree(ObjectInserter inserter) throws IOException {

			log.debug("buildTree: starting");

			if (!isDirty() && originalTreeObjectId != null) {

				return originalTreeObjectId;
			}

			// else we need to recompute the tree at this level.

			TreeFormatter tree = new TreeFormatter();

			List<JGitTreeData> treeDataList = new ArrayList<JGitTreeData>();

			for (Map.Entry<String, ObjectId> entry : this.blobReferences
					.entrySet()) {
				String name = entry.getKey();
				ObjectId sha1 = entry.getValue();

				treeDataList.add(new JGitTreeData(name, FileMode.REGULAR_FILE,
						sha1));

				log.debug(String.format("added entry (name=%s, sha1=%s", name,
						sha1));

			}

			for (Map.Entry<String, GitTreeNodeData> entry : this.subTreeReferences
					.entrySet()) {

				String name = entry.getKey();
				GitTreeNodeData nodeData = entry.getValue();

				ObjectId subTreeId = nodeData.buildTree(inserter);

				treeDataList.add(new JGitTreeData(name, FileMode.TREE,
						subTreeId));

				log.debug(String.format("added tree (name=%s, sha1=%s", name,
						subTreeId));
			}

			/*
			 * Compare the string sort vs byte sort
			 */

			Collections.sort(treeDataList, JGitTreeData.GIT_SORT_ORDERING);

			for (JGitTreeData treeData : treeDataList) {

				tree.append(treeData.getName(), treeData.getFileMode(),
						treeData.getObjectId());
			}

			log.debug("buildTree: finished");

			return inserter.insert(tree);
		}

		public void visit(GitTreeDataVisitor vistor) {

			for (Map.Entry<String, ObjectId> blobEntry : blobReferences
					.entrySet()) {
				String name = blobEntry.getKey();
				ObjectId objectId = blobEntry.getValue();

				vistor.visitBlob(getPath(name), objectId);
			}

			for (Map.Entry<String, GitTreeNodeData> treeEntry : subTreeReferences
					.entrySet()) {
				GitTreeNodeData subTree = treeEntry.getValue();

				subTree.visit(vistor);
			}
		}

		private String getPath(String name) {

			if (this.nodePath.isEmpty())
				return name;
			else
				return this.nodePath + "/" + name;
		}

		/**
		 * In some initialization cases we want the node to know its been
		 * initialized properly.
		 * 
		 * @param id
		 * @param initialized
		 */
		public void setGitTreeObjectId(ObjectId id) {
			this.originalTreeObjectId = id;
		}

		public void resetDirtyFlag() {

			// can only not be dirty if there is an original
			// always dirty if there is no original
			if (this.originalTreeObjectId != null)
				this.setDirty(false);

			for (GitTreeNodeData subTreeData : this.subTreeReferences.values()) {

				subTreeData.resetDirtyFlag();
			}
		}

		public boolean addTree(String path, ObjectId treeId) {

			return addResource(path, new TreeResourceContext(treeId));

		}

		/**
		 * Find the object id for the path as a string.
		 * 
		 * @param path
		 * @return
		 * @throws IOException
		 * @throws CorruptObjectException
		 * @throws IncorrectObjectTypeException
		 * @throws MissingObjectException
		 */
		public ObjectId find(Repository repo, String path)
				throws MissingObjectException, IncorrectObjectTypeException,
				CorruptObjectException, IOException {

			String parts[] = path.split("/");

			GitTreeNodeData currentNode = this;

			int uptoFile = parts.length - 1;

			for (int i = 0; i < uptoFile; i++) {

				String name = parts[i];

				if (!currentNode.isInitialized()) {
					// check the path using git through the objectid
					ObjectId treeId = currentNode.getOriginalTreeObjectId();

					if (treeId != null) {

						return GitRepositoryUtils.findInTree(repo, treeId,
								StringUtils.join(parts, "/", i, parts.length));

					}

				} else {
					GitTreeNodeData nextNode = currentNode.subTreeReferences
							.get(name);

					if (nextNode == null) {
						return null;
					} else
						currentNode = nextNode;
				}

			}

			String filePart = parts[uptoFile];

			ObjectId blobId = currentNode.blobReferences.get(filePart);

			if (blobId == null) {
				// check if it is a name of a tree

				GitTreeNodeData subTree = currentNode.subTreeReferences
						.get(filePart);

				if (subTree != null) {
					return subTree.getOriginalTreeObjectId();
				} else
					return null;
			} else {
				return blobId;
			}

		}

		/**
		 * Replace ourself with the loaded node
		 * 
		 * @param loadedNode
		 */
		public void replaceWith(GitTreeData loadedNode) {

			if (!initialized) {

				this.blobReferences.clear();
				this.subTreeReferences.clear();

				this.blobReferences.putAll(loadedNode.root.blobReferences);

				this.subTreeReferences
						.putAll(loadedNode.root.subTreeReferences);

				this.initialized = true;
				
				this.dirty = false;

			} else {
				log.warn("replacing an already initialized node!");
			}
		}

		public void setInitialized(boolean initialized) {
			this.initialized = initialized;
		}

	}

	private GitTreeNodeData root;
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
		root = new GitTreeNodeData("", "");
	}

	public void addBlob(String filePath, ObjectId blobSha1) {

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

	public void addTree(String path, ObjectId treeId) {

		this.root.addTree(path, treeId);

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


}
