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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
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
	
	public static class GitTreeNodeData {
		
		private String name;
		
		private Map<String, String>blobReferences = new HashMap<String, String>();
		
		private Map<String, GitTreeNodeData>subTreeReferences = new HashMap<String, GitTreeData.GitTreeNodeData>();
		
		private Set<String>deletedPathSet = new HashSet<String>();

		/**
		 * 
		 */
		public GitTreeNodeData(String name) {
			super();
		}

		public void deletePath(String path) {
			this.deletedPathSet.add(path);
		}
		
		public void addBlob(String filePath, String blobSha1) {
			
			String[] parts = filePath.split("\\/");

			List<String>partsList = Arrays.asList(parts);
			
			if (partsList.size() > 0) {
				this.addBlob (partsList, blobSha1);
			}
			
		}
		
		public void addBlob(List<String>parts, String blobSha1) {
			
			if (parts.size() == 0) {
				// should never occur
				throw new RuntimeException("too deep");
			}
			
			String name = parts.get(0);
			
			if (parts.size() == 1) {
				// add the blob here
				blobReferences.put(name, blobSha1);
			}
			else {
				// > 1
				// need to get down a level.
				GitTreeNodeData leaf = subTreeReferences.get(name);
				
				if (leaf == null) {
					leaf = new GitTreeNodeData(name);
					subTreeReferences.put(name, leaf);
				}
				
				leaf.addBlob(parts.subList(1, parts.size()), blobSha1);
			}
		}

		public TreeFormatter buildTree(ObjectInserter inserter) throws IOException {
			
			log.debug("buildTree: starting");
			
			TreeFormatter tree = new TreeFormatter();
			
			for (Map.Entry<String, String> entry : this.blobReferences.entrySet()) {
				String name = entry.getKey();
				String sha1 = entry.getValue();
				
				tree.append(name, FileMode.REGULAR_FILE, ObjectId.fromString(sha1));
				
				log.debug(String.format("added entry (name=%s, sha1=%s", name, sha1));
				
			}
			
			for (Map.Entry<String, GitTreeNodeData> entry : this.subTreeReferences.entrySet()) {
				
				String name = entry.getKey();
				GitTreeNodeData nodeData = entry.getValue();
				
				TreeFormatter subTree = nodeData.buildTree(inserter);
				
				ObjectId subTreeId = inserter.insert(subTree);
				
				tree.append (name, FileMode.TREE, subTreeId);
				
				log.debug(String.format("added tree (name=%s, sha1=%s", name, subTreeId));
			}
			
			log.debug("buildTree: finished");
			return tree;
		}

		public void mergeOntoExisting(GitTreeNodeData existingRoot) {
			
			for (Map.Entry<String, String>entry : existingRoot.blobReferences.entrySet()) {
				
				String path = entry.getKey();
				
				/*
				 * Take the existing value unless:
				 * 1. file exists in our (from the dump) data
				 * 2. file is deleted
				 */
				
				if (this.blobReferences.get(path) == null && !isDeletedPath (path)) {
					
					this.addBlob(path, entry.getValue());
				}
				
			}
		}

		private boolean isDeletedPath(String path) {
			
			for (String deletedPath : this.deletedPathSet) {
				
				if (path.startsWith(deletedPath))
					return true;
				
			}
			
			return false;
			
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
	
	private Map<String, GitMergeData>mergeData = new HashMap<String, GitTreeData.GitMergeData>();
		
	public void registerMerge (String copyFromBranch, String copyFromRevision, String copyFromPath, String copySha1, boolean directoryAddByCopy) {
		if (!mergeData.containsKey(copyFromBranch))
			mergeData.put(copyFromBranch, new GitMergeData(copyFromRevision, copyFromPath, copySha1, directoryAddByCopy));
	}
	
	/**
	 * 
	 */
	public GitTreeData() {
		root = new GitTreeNodeData("");
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
		
		this.root.mergeOntoExisting (existingTreeData.root);
		
	}

	/**
	 * @param path
	 * @see org.kuali.student.git.model.GitTreeData.GitTreeNodeData#deletePath(java.lang.String)
	 */
	public void deletePath(String path) {
		root.deletePath(path);
	}

	public List<ObjectId> getMergeParentIds(Repository repo) {
		// this is assuming a merge from the latest head
		// later use the copy from data to include more infor
		ArrayList<ObjectId> parentIds = new ArrayList<ObjectId>();
		
		try {
			for (String mergeBranchName : this.mergeData.keySet()) {
				Ref ref = repo.getRef(mergeBranchName);
			
				if (ref == null) {
					// this is a missing branch.
					log.warn("can't merge with a non existant branch = " + mergeBranchName);
					continue;
				}
				
				ObjectId id = ref.getObjectId();
				
				parentIds.add(id);
				
			}
		} catch (IOException e) {
			throw new RuntimeException("failed to get merge parent Ids", e);
		}
		
		return parentIds;
	}

	public Set<String> getMergeSourceBranches() {
		return mergeData.keySet();
	}
	
	public GitMergeData getMergeData(String branchName) {
		return mergeData.get(branchName);
	}
	
}
