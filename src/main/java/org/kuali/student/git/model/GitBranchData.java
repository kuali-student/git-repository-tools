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
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.git.model.GitTreeData.GitMergeData;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Represents the
 * @author Kuali Student Team
 *
 */
public class GitBranchData {

	public static final Logger log = LoggerFactory.getLogger(GitBranchData.class);
	
	private GitTreeData branchRoot = new GitTreeData();
	
	private String branchPath;

	/**
	 * @param revision 
	 * @param branchPath 
	 * @param path 
	 * 
	 */
	public GitBranchData(String branchPath) {
		this.branchPath = branchPath;
		
	}
	
	

	/**
	 * @return the branchPath
	 */
	public String getBranchPath() {
		return branchPath;
	}



	public void addBlob(String path, String blobSha1) {
		
		if (!path.startsWith(this.branchPath)) {
			throw new RuntimeException("blob is not in this branch.");
		}
		
		try {
			BranchData db = GitBranchUtils.parse(path);
			
			String filePath = db.getPath();
			
			branchRoot.addBlob(filePath, blobSha1);
		} catch (VetoBranchException e) {
			
			log.warn(path + "is not a branch veto blob.");
		}
		
		
	}

	/**
	 * @param inserter
	 * @return
	 * @throws IOException
	 * @see org.kuali.student.git.model.GitTreeData#buildTree(org.eclipse.jgit.lib.ObjectInserter)
	 */
	public ObjectId buildTree(ObjectInserter inserter) throws IOException {
		return branchRoot.buildTree(inserter);
		
		
	}

	/**
	 * Apply our from the dump tree data onto the existing data
	 * @param existingTreeData
	 */
	public void mergeOntoExistingTreeData(GitTreeData existingTreeData) {
		branchRoot.mergeOntoExisting(existingTreeData);
	}



	public void deletePath(String path) {
		branchRoot.deletePath(path);
	}



	/**
	 * @param copyFromBranch
	 * @param copyFromRevision
	 * @param copyFromPath
	 * @param copySha1
	 * @param directoryAddByCopy 
	 * @see org.kuali.student.git.model.GitTreeData#registerMerge(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void registerMerge(String copyFromBranch, String copyFromRevision,
			String copyFromPath, String copySha1, boolean directoryAddByCopy) {
		branchRoot.registerMerge(copyFromBranch, copyFromRevision,
				copyFromPath, copySha1, directoryAddByCopy);
	}
	
	
	public List<ObjectId>getMergeParentIds(Repository repo) {
		
		return branchRoot.getMergeParentIds(repo);
		
	}



	/**
	 * @return
	 * @see org.kuali.student.git.model.GitTreeData#getMergeSourceBranches()
	 */
	public Set<String> getMergeSourceBranches() {
		return branchRoot.getMergeSourceBranches();
	}



	/**
	 * @param branchName
	 * @return
	 * @see org.kuali.student.git.model.GitTreeData#getMergeData(java.lang.String)
	 */
	public GitMergeData getMergeData(String branchName) {
		return branchRoot.getMergeData(branchName);
	}





	
	
	
}
