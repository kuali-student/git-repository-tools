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
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.TreeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a git tree or directory of blob's and trees.
 * 
 * @author Kuali Student Team
 *
 */
public class GitTreeData {
	
	private static final Logger log = LoggerFactory.getLogger(GitTreeData.class);

	public static interface GitTreeDataVisitor {
		
		/**
		 * 
		 * @param path
		 * @param objectId
		 * @return false if no more blobs should be visted
		 */
		public boolean visitBlob (String path, String objectId);
		
	}
	public static class GitTreeNodeData {
		
		private String name;
		
		private String nodePath;
		
		private Map<String, String>blobReferences = new HashMap<String, String>();
		
		private Map<String, GitTreeNodeData>subTreeReferences = new HashMap<String, GitTreeData.GitTreeNodeData>();

		private ObjectId originalTreeObjectId;
		
		private boolean dirty = false;
		
		/**
		 * @param string 
		 * 
		 */
		public GitTreeNodeData(String name, String nodePath) {
			super();
			this.name = name;
			this.nodePath = nodePath;
		}

		public boolean deletePath(String path) {
			
			// remove all the blobs contained in the path given.
			
			String[] parts = path.split("/");
			
			return deletePath(parts, 0);
		}		
		
		
		private boolean deletePath(String[] parts, int partOffset) {

			int difference = (parts.length - partOffset);
			
			if (difference == 0) {
				// should never occur
				throw new RuntimeException("should not delete all branch content should be deleting the branch");
			}
			
			String name = parts[partOffset];
			
			if (difference == 1) {
				
				boolean blobReference = false;
				boolean treeReference = false;
				
				if (blobReferences.containsKey(name)) {
					blobReferences.remove(name);
					blobReference = true;
					setDirty(true);
				}
				else if (subTreeReferences.containsKey(name)) {
					subTreeReferences.remove(name);
					treeReference = true;
					setDirty(true);
				}
				
				if (blobReference == false && treeReference == false) {
					String lastPart = parts[parts.length-1];
					
					if (lastPart.contains("\\."))
						log.warn("failed to delete any tree or blob for " + name);
				}
				
				if (blobReference || treeReference) 
					return true;
				else
					return false;
			}
			else {
				// > 1
				// need to get down a level.
				GitTreeNodeData leaf = subTreeReferences.get(name);
				
				if (leaf == null) {
					String lastPart = parts[parts.length-1];
					
					if (lastPart.contains("\\."))
						log.warn("missing leaf blob = " + name);
				}
				else {
				
					/*
					 * bubble up the dirty flag to the root along the deleted path.
					 */
					if (leaf.deletePath(parts, partOffset+1)) {
						setDirty(true);
						
						return true;
					}
					else
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
		 * @param dirty the dirty to set
		 */
		public void setDirty(boolean dirty) {
			this.dirty = dirty;
		}

		public boolean addBlob(String filePath, String blobSha1) {
			
			String[] parts = filePath.split("\\/");

			
			if (parts.length > 0) {
				return this.addBlob (parts, 0, blobSha1);
			}
			else {
				log.info("failed to add blob");
				return false;
			}
			
		}
		
		public boolean addBlob(String []parts, int partOffset, String blobSha1) {
			
			int difference = (parts.length - partOffset);
			
			if (difference == 0) {
				// should never occur
				throw new RuntimeException("too deep");
			}
			
			String name = parts[partOffset];
			
			if (name.isEmpty()) {
				log.info("name is empty at partOffset= " + partOffset + ", blobId= " + blobSha1);
				
			}
			
			if (difference == 1) {
				// add the blob here
				String existing = blobReferences.put(name, blobSha1);
				
				this.setDirty(true);
				
				if (existing != null) {
					/*
					 * This is normally ok.  This would typically be a change to a file that existed in the previous commit.
					 * 
					 * This was added originally to find the data loss that had been occuring.
					 */
					log.debug("overwriting blob = " + name);
				}
				
				return true;
			}
			else {
				// > 1
				// need to get down a level.
				GitTreeNodeData leaf = subTreeReferences.get(name);
				
				if (leaf == null) {
					leaf = new GitTreeNodeData(name, getNodePath (name, parts, partOffset));
					subTreeReferences.put(name, leaf);
				}
				
				if (leaf.addBlob(parts, partOffset+1, blobSha1)) {
					setDirty(true);
					return true;
				}
				else
					return false;
			}
		}

		private String getNodePath(String name, String[] parts, int partOffset) {
			String joinedPath = StringUtils.join(parts, "/", 0, partOffset+1);
			
			return joinedPath;
		}

		public ObjectId buildTree(ObjectInserter inserter) throws IOException {
			
			log.debug("buildTree: starting");
			
			if (!isDirty() && originalTreeObjectId != null) {
				
					return originalTreeObjectId;
			}

			// else we need to recompute the tree at this level.
			
			TreeFormatter tree = new TreeFormatter();
			
			List<JGitTreeData>treeDataList = new ArrayList<JGitTreeData>();
			
			for (Map.Entry<String, String> entry : this.blobReferences.entrySet()) {
				String name = entry.getKey();
				String sha1 = entry.getValue();
				
				treeDataList.add(new JGitTreeData(name, FileMode.REGULAR_FILE, ObjectId.fromString(sha1)));
				
				log.debug(String.format("added entry (name=%s, sha1=%s", name, sha1));
				
			}
			
			for (Map.Entry<String, GitTreeNodeData> entry : this.subTreeReferences.entrySet()) {
				
				String name = entry.getKey();
				GitTreeNodeData nodeData = entry.getValue();
				
				ObjectId subTreeId = nodeData.buildTree(inserter);
				
				treeDataList.add(new JGitTreeData(name, FileMode.TREE, subTreeId));
				
				log.debug(String.format("added tree (name=%s, sha1=%s", name, subTreeId));
			}
			
			/*
			 * Compare the string sort vs byte sort
			 */
			
			Collections.sort(treeDataList, JGitTreeData.GIT_SORT_ORDERING);
			
			for (JGitTreeData treeData : treeDataList) {
				
				tree.append(treeData.getName(), treeData.getFileMode(), treeData.getObjectId());
			}
			
			log.debug("buildTree: finished");
			
			return inserter.insert(tree);
		}

		public void visit(GitTreeDataVisitor vistor) {

			for (Map.Entry<String, String>blobEntry : blobReferences.entrySet()) {
				String name = blobEntry.getKey();
				String objectId = blobEntry.getValue();
				
				vistor.visitBlob(getPath(name), objectId);
			}
			
			for (Map.Entry<String, GitTreeNodeData>treeEntry : subTreeReferences.entrySet()) {
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

		public void addTree(String path, ObjectId treeId) {
			
			String[] parts = path.split("\\/");

			if (parts.length > 0) {
				this.addTree (parts, 0, treeId);
			}
			else {
				log.info("failed to add tree");
			}
			
		}
		
		public void addTree(String []parts, int partOffset, ObjectId treeId) {
			
			int difference = (parts.length - partOffset);
			
			if (difference == 0) {
				// should never occur
				throw new RuntimeException("too deep");
			}
			
			String name = parts[partOffset];
			
			if (name.isEmpty()) {
				log.info("name is empty at partOffset= " + partOffset + ", treeId= " + treeId);
				
			}
			
			if (difference == 1) {
				// add the tree here
				
				GitTreeNodeData existing = subTreeReferences.get(name);

				if (existing == null) {
					existing = new GitTreeNodeData(name, getNodePath (name, parts, partOffset));
					subTreeReferences.put(name, existing);
				}
				
				existing.setGitTreeObjectId(treeId);
				
			}
			else {
				// > 1
				// need to get down a level.
				GitTreeNodeData leaf = subTreeReferences.get(name);
				
				if (leaf == null) {
					leaf = new GitTreeNodeData(name, getNodePath (name, parts, partOffset));
					subTreeReferences.put(name, leaf);
				}
				
				leaf.addTree(parts, partOffset+1, treeId);
			}
		}
		
	}
	
	private GitTreeNodeData root;
	
	
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
	public GitTreeData() {
		root = new GitTreeNodeData("", "");
	}

	public void addBlob(String filePath, String blobSha1) {
		
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
	
	
}
