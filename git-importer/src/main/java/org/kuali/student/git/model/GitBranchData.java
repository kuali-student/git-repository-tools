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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.kuali.student.branch.model.BranchData;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.kuali.student.git.model.branch.utils.GitBranchUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.GitTreeData.GitTreeDataVisitor;
import org.kuali.student.git.model.tree.utils.GitTreeDataUtils;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.svn.model.ExternalModuleInfo;
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

	private long revision;

	private BranchDetector branchDetector;

	private GitTreeProcessor treeProcessor;

	private Map<String, BranchMergeInfo>branchPathToMergeInfoMap = new HashMap<String, BranchMergeInfo>();
	
	private boolean alreadyInitialized = false;

	private boolean blobsDeleted = false;

	private List<ExternalModuleInfo> externals = new ArrayList<>();

	private SvnRevisionMapper revisionMapper;

	/**
	 * @param revision
	 * @param branchPath
	 * @param revision 
	 * @param largeBranchNameProvider 
	 * @param path
	 * 
	 */
	public GitBranchData(String branchName, long revision, SvnRevisionMapper revisionMapper, GitTreeProcessor treeProcessor, BranchDetector branchDetector) {
		this.revisionMapper = revisionMapper;
		this.treeProcessor = treeProcessor;
		this.branchDetector = branchDetector;
		this.branchPath = GitBranchUtils.getBranchPath(branchName, revision, revisionMapper);
		this.revision = revision;
		this.branchName = GitBranchUtils.getCanonicalBranchName(this.branchPath, revision, revisionMapper);

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
	
	

	/**
	 * @return the blobsDeleted
	 */
	public boolean isBlobsDeleted() {
		return blobsDeleted;
	}

	public void addBlob(String path, String blobSha1, PrintWriter blobLog)
			throws VetoBranchException, MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {

		
		if (!path.startsWith(this.branchPath)) {
			String errorMessage = String.format("blob absolute path(%s) does not match this branch (%s)", path, this.branchName);
			log.error(errorMessage);
			blobLog.println(errorMessage);
			return;
		}

		initialize();
		
		blobsAdded.addAndGet(1L);

		BranchData db = branchDetector.parseBranch(revision, path);

		String filePath = db.getPath();

		if (filePath.length() == 0) {
			String errorMessage = String.format ("trying to index an empty file path.  Revision = %d, Path = %s, File Path = %s, blobId = %s, ", revision, path, filePath, blobSha1);
			
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
	 * @see org.kuali.student.git.model.tree.GitTreeData#buildTree(org.eclipse.jgit.lib.ObjectInserter)
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

	public void deletePath(String path, long currentRevision) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		initialize();
		
		// should we strip of the branch name part of the path and only pass
		// through the
		// remaining path.

		if (path.startsWith(branchPath)) {

			StringBuilder withinBranchPath = new StringBuilder(path);

			withinBranchPath.delete(0, branchPath.length());

			if (withinBranchPath.charAt(0) == '/')
				withinBranchPath.deleteCharAt(0);

			boolean deletedBlob = branchRoot.deletePath(withinBranchPath.toString());
			
			if (deletedBlob && !blobsDeleted)
				blobsDeleted = true;
			
		} else {
			log.warn("invalid branch");
		}

	}

	
	
	private void initialize() throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		if (alreadyInitialized || parentId == null)
			return;
		
		alreadyInitialized = true;
		
		GitTreeData existingTreeData = GitTreeDataUtils.extractExistingTreeData(treeProcessor, parentId);

		int existingBlobCount = GitTreeDataUtils
				.countBlobs(existingTreeData);

		mergeOntoExistingTreeData(existingTreeData);

		int mergedBlobCount = getBlobCount();

		if (existingBlobCount != mergedBlobCount) {
			throw new RuntimeException(
					"data loss existing count = "
							+ existingBlobCount
							+ ", merged count = " + mergedBlobCount);
		}
		
		// load up any existing svn:mergeinfo data
		List<BranchMergeInfo> existingMergeInfo = revisionMapper.getMergeBranches(this.revision-1L, this.branchName);
		
		if (existingMergeInfo != null && existingMergeInfo.size() > 0)
			accumulateMergeInfo(existingMergeInfo);
		
		final List<ExternalModuleInfo> existingExternals = new ArrayList<>(5); 
		
		// load up any existing svn:externals data
		existingTreeData.visit(new GitTreeDataVisitor() {
			
			@Override
			public boolean visitBlob(String path, String objectId) {
				
				if (path.contains("/")) {
					// only look in the base tree
					return false;
				}
				
				if (path.equals("fusion-maven-plugin.dat")) {
				
					try {
						List<String> existingData = treeProcessor.getBlobAsStringLines(ObjectId.fromString(objectId));
						
						existingExternals.addAll(SvnExternalsUtils.extractFusionMavenPluginData(existingData));
					} catch (MissingObjectException e) {
						log.warn("failed to load fusion-maven-plugin.dat at ", e);
					} catch (IOException e) {
						log.warn("failed to load fusion-maven-plugin.dat at ", e);
					}
					
					return false;
					
				}
				else
					return true;
				
			}
		});

		this.externals.addAll(existingExternals);
		
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

	public List<BranchMergeInfo>getAccumulatedBranchMergeData() {
		return new ArrayList<BranchMergeInfo>(this.branchPathToMergeInfoMap.values());
	}
	
	public void accumulateMergeInfo(
			List<BranchMergeInfo> extractBranchMergeInfoFromString) {
		
		for (BranchMergeInfo branchMergeInfo : extractBranchMergeInfoFromString) {
			
			BranchMergeInfo existingMergeInfo = this.branchPathToMergeInfoMap.get(branchMergeInfo.getBranchName());
			
			if (existingMergeInfo == null) {
				this.branchPathToMergeInfoMap.put(branchMergeInfo.getBranchName(), branchMergeInfo);
			}
			else {
				Set<Long>mergedRevisions = new HashSet<>();
				
				mergedRevisions.addAll(existingMergeInfo.getMergedRevisions());
				mergedRevisions.addAll(branchMergeInfo.getMergedRevisions());
				
				existingMergeInfo.setMergedRevisions(mergedRevisions);
			}
			
		}
		
	}

	public void setExternals(List<ExternalModuleInfo> externals) {
		this.externals = externals;
	}
	
	/**
	 * @return the externals
	 */
	public List<ExternalModuleInfo> getExternals() {
		return Collections.unmodifiableList(externals);
	}

	public void clearMergeInfo() {

		this.branchPathToMergeInfoMap.clear();
	}

	public void clearExternals() {
		
		this.externals.clear();
		
	}
	
	

}
