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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.git.model.GitTreeData.GitMergeData;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.model.util.GitTreeDataUtils;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Represents the
 * 
 * @author Kuali Student Team
 * 
 */
public class GitBranchData {

	public static final Logger log = LoggerFactory
			.getLogger(GitBranchData.class);

	private GitTreeData branchRoot = new GitTreeData();

	private String branchPath;

	private String branchName;

	private ObjectId parentId;

	private Set<ObjectId> mergeParentIdSet = new HashSet<ObjectId>();

	private AtomicLong blobsAdded = new AtomicLong(0L);

	private boolean created;

	/**
	 * @param revision
	 * @param branchPath
	 * @param path
	 * 
	 */
	public GitBranchData(String branchPath) {
		this.branchPath = branchPath;
		this.branchName = GitBranchUtils.getCanonicalBranchName(branchPath);

	}

	/**
	 * @return the branchPath
	 */
	public String getBranchPath() {
		return branchPath;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	public long getBlobsAdded() {
		return blobsAdded.get();
	}

	public void addBlob(String path, String blobSha1, PrintWriter blobLog)
			throws VetoBranchException {

		if (!path.startsWith(this.branchPath)) {
			String errorMessage = String.format("blob absolute path(%s) does not match this branch (%s)", path, this.branchName);
			log.error(errorMessage);
			blobLog.println(errorMessage);
			return;
		}

		blobsAdded.addAndGet(1L);

		BranchData db = GitBranchUtils.parse(path);

		String filePath = db.getPath();

		if (filePath.length() == 0) {
			String errorMessage = String.format ("trying to index an empty file path.  Path = %s, File Path = %s, blobId = %s, ", path, filePath, blobSha1);
			
			log.warn(errorMessage);
			blobLog.println(errorMessage);
			
			/*
			 * Indexing an empty file breaks the JGit Tree so exclude the file.
			 */
			return;
		}

		branchRoot.addBlob(filePath, blobSha1);

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
	 * 
	 * @param existingTreeData
	 */
	public void mergeOntoExistingTreeData(GitTreeData existingTreeData) {
		branchRoot.mergeOntoExisting(existingTreeData);
	}

	public int getBlobCount() {
		return GitTreeDataUtils.countBlobs(branchRoot);
	}

	public void deletePath(String path, long currentRevision) {
		
		// should we strip of the branch name part of the path and only pass
		// through the
		// remaining path.

		if (path.startsWith(branchPath)) {

			StringBuilder withinBranchPath = new StringBuilder(path);

			withinBranchPath.delete(0, branchPath.length());

			if (withinBranchPath.charAt(0) == '/')
				withinBranchPath.deleteCharAt(0);

			branchRoot.deletePath(withinBranchPath.toString());
		} else {
			log.warn("invalid branch");
		}

	}

	public Set<ObjectId> getMergeParentIds() {

		return Collections.unmodifiableSet(this.mergeParentIdSet);

	}

	public void setParentId(ObjectId parentId) {
		this.parentId = parentId;

	}

	/**
	 * @return the parentId
	 */
	public ObjectId getParentId() {
		return parentId;
	}

	public void addMergeParentId(ObjectId head) {
		this.mergeParentIdSet.add(head);
	}

	public void setCreated(boolean created) {
		this.created = created;

	}

	/**
	 * @return the created
	 */
	public boolean isCreated() {
		return created;
	}

	public void reset() {

		created = false;
		blobsAdded.set(0L);
		branchRoot = new GitTreeData();
		mergeParentIdSet.clear();
		parentId = null;

	}

}
