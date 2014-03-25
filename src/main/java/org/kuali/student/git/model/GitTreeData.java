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
package org.kuali.student.git.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		
		public void visitBlob (String path, String objectId);
		
	}
	public static class GitTreeNodeData {
		
		private String name;
		
		private String nodePath;
		
		private Map<String, String>blobReferences = new HashMap<String, String>();
		
		private Map<String, GitTreeNodeData>subTreeReferences = new HashMap<String, GitTreeData.GitTreeNodeData>();
		
		/**
		 * @param string 
		 * 
		 */
		public GitTreeNodeData(String name, String nodePath) {
			super();
			this.name = name;
			this.nodePath = nodePath;
		}

		public void deletePath(String path) {
			
			// remove all the blobs contained in the path given.
			
			String[] parts = path.split("/");
			
			deletePath(parts, 0);
		}
		
		
		
		private void deletePath(String[] parts, int partOffset) {

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
				}
				else if (subTreeReferences.containsKey(name)) {
					subTreeReferences.remove(name);
					treeReference = true;
				}
				
				if (blobReference == false && treeReference == false) {
					String lastPart = parts[parts.length-1];
					
					if (lastPart.contains("\\."))
						log.warn("failed to delete any tree or blob for " + name);
				}
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
				
					leaf.deletePath(parts, partOffset+1);
				}
			}
		}

		public void addBlob(String filePath, String blobSha1) {
			
			String[] parts = filePath.split("\\/");

			
			if (parts.length > 0) {
				this.addBlob (parts, 0, blobSha1);
			}
			else {
				log.info("failed to add blob");
			}
			
		}
		
		public void addBlob(String []parts, int partOffset, String blobSha1) {
			
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
				
				if (existing != null) {
					/*
					 * This is normally ok.  This would typically be a change to a file that existed in the previous commit.
					 * 
					 * This was added originally to find the data loss that had been occuring.
					 */
					log.debug("overwriting blob = " + name);
				}
			}
			else {
				// > 1
				// need to get down a level.
				GitTreeNodeData leaf = subTreeReferences.get(name);
				
				if (leaf == null) {
					leaf = new GitTreeNodeData(name, getNodePath (name, parts, partOffset));
					subTreeReferences.put(name, leaf);
				}
				
				leaf.addBlob(parts, partOffset+1, blobSha1);
			}
		}

		private String getNodePath(String name, String[] parts, int partOffset) {
			String joinedPath = StringUtils.join(parts, "/", 0, partOffset+1);
			
			return joinedPath;
		}

		public TreeFormatter buildTree(ObjectInserter inserter) throws IOException {
			
			log.debug("buildTree: starting");
			
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
				
				TreeFormatter subTree = nodeData.buildTree(inserter);
				
				ObjectId subTreeId = inserter.insert(subTree);
				
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
			return tree;
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
		
		TreeFormatter tf = root.buildTree(inserter);
		
		ObjectId treeId = inserter.insert(tf);
		
		return treeId;
	}

	/**
	 * Merge ourself onto the existing data.
	 * 
	 * That means keep our version instead of their version.
	 * 
	 * keep there version unless we have a delete that covers their path.
	 * 
	 * @param existingTreeData
	 */
	public void mergeOntoExisting(GitTreeData existingTreeData) {
		
		existingTreeData.visit(new GitTreeDataVisitor() {
			
			@Override
			public void visitBlob(String path, String objectId) {
				GitTreeData.this.root.addBlob(path, objectId);
			}
		});
		
	}

	/**
	 * @param path
	 * @see org.kuali.student.git.model.GitTreeData.GitTreeNodeData#deletePath(java.lang.String)
	 */
	public void deletePath(String path) {
		root.deletePath(path);
	}

	public void visit(GitTreeDataVisitor vistor) {
		root.visit(vistor);
		
	}
	
}
