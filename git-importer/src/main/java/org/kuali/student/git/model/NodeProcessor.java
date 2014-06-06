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
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.input.BoundedInputStream;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.kuali.student.branch.model.BranchData;
import org.kuali.student.git.importer.GitImporterParseOptions;
import org.kuali.student.git.model.CopyFromOperation.OperationType;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMapResults;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.kuali.student.git.model.branch.utils.GitBranchUtils;
import org.kuali.student.git.model.branch.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.kuali.student.git.model.exception.InvalidBlobChangeException;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor.GitTreeBlobVisitor;
import org.kuali.student.git.model.util.GitBranchDataUtils;
import org.kuali.student.subversion.SvnDumpFilter;
import org.kuali.student.svn.model.ExternalModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 * 
 */
public class NodeProcessor implements IGitBranchDataProvider {

	private static final String SVN_EXTERNALS_PROPERTY_KEY = "svn:externals";

	private static final String SVN_MERGEINFO_PROPERTY_KEY = "svn:mergeinfo";

	private static final String DELETE_ACTION = "delete";

	private static final String REPLACE_ACTION = "replace";

	private static final String CHANGE_ACTION = "change";

	private static final String DIR_KIND = "dir";

	private static final String FILE_KIND = "file";

	private static final String ADD_ACTION = "add";

	private static final Logger log = LoggerFactory
			.getLogger(NodeProcessor.class);

	private PrintWriter vetoLog;
	private Map<String, GitBranchData> knownBranchMap;
	private Repository repo;
	private SvnRevisionMapper revisionMapper;
	private PrintWriter copyFromSkippedLog;
	private PrintWriter blobLog;

	private GitTreeProcessor treeProcessor;
	private GitImporterParseOptions importerParseOptions;
	private GitCommitData commitData;
	private ILargeBranchNameProvider largeBranchNameProvider;

	private BranchDetector branchDetector;

	private String repositoryBaseUrl;

	public NodeProcessor(Map<String, GitBranchData> knownBranchMap,
			PrintWriter vetoLog, PrintWriter copyFromSkippedLog,
			PrintWriter blobLog, Repository repo,
			SvnRevisionMapper revisionMapper,
			GitImporterParseOptions importerParseOptions,
			BranchDetector branchDetector, String repositoryBaseUrl) {
		super();
		this.knownBranchMap = knownBranchMap;
		this.vetoLog = vetoLog;
		this.copyFromSkippedLog = copyFromSkippedLog;
		this.blobLog = blobLog;
		this.repo = repo;
		this.revisionMapper = revisionMapper;
		this.largeBranchNameProvider = revisionMapper;
		this.importerParseOptions = importerParseOptions;
		this.branchDetector = branchDetector;
		this.repositoryBaseUrl = repositoryBaseUrl;

		this.treeProcessor = new GitTreeProcessor(repo);

	}

	private InputStream getInputStream() {
		return importerParseOptions.getInputStream();
	}

	public void processNode(String path, long currentRevision,
			Map<String, String> nodeProperties) throws IOException,
			VetoBranchException {

		/*
		 * This catches cases that don't have file content.
		 * 
		 * This can be directory adds, file copies, directory copies, etc.
		 */

		String kind = nodeProperties.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_KIND);

		String action = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_ACTION);

		boolean validBranch = true;

		BranchData branchData = null;
		try {
			branchData = branchDetector.parseBranch(currentRevision, path);

			if (kind != null && kind.equals(FILE_KIND)
					&& branchData.getPath().length() == 0)
				throw new VetoBranchException(
						"A file add or change requires part of the path to be a subpath in the branch.");

		} catch (VetoBranchException e) {
			validBranch = false;

		}

		if (validBranch) {
			// check that there is actually a branch of this name
			String canonicalBranchName = GitBranchUtils.getCanonicalBranchName(
					branchData.getBranchPath(), currentRevision,
					largeBranchNameProvider);

			try {
				Ref mergeBranchRef = repo.getRef(Constants.R_HEADS
						+ canonicalBranchName);

			} catch (IOException e) {
				validBranch = false;
				// intentionally fall through to the if statements below.
			}
		}

		GitBranchData data = null;

		if (validBranch) {
			data = getBranchData(GitBranchUtils.getCanonicalBranchName(
					branchData.getBranchPath(), currentRevision,
					largeBranchNameProvider), currentRevision);
		}

		if (ADD_ACTION.equals(action)) {

			if (FILE_KIND.equals(kind)) {

				/*
				 * No content length means we add the blob from the copy from
				 * revision
				 */

				if (!validBranch) {
					// an add on an invalid branch means we have a gap and the
					// path should be stored in a default branch
					// for now this will be the first directory in the path.

					data = getDefaultBranchData(path, currentRevision);
				}

				if (data != null)
					applyBlobAdd(data, path, currentRevision, nodeProperties);

			} else if (DIR_KIND.equals(kind)) {

				/*
				 * We care if the directory was copied from somewhere else
				 */

				loadRevisionProperties(currentRevision, data, path,
						nodeProperties);

				applyDirectoryAdd(data, path, currentRevision, nodeProperties);

			} else {
				// skip this case
			}

		} else if (CHANGE_ACTION.equals(action)) {

			/*
			 * This can happen I think for property changes Not sure if we are
			 * doing the right thing here.
			 */
			if (FILE_KIND.equals(kind)) {

				if (!validBranch)
					data = getDefaultBranchData(path, currentRevision);

				if (data != null)
					applyBlobAdd(data, path, currentRevision, nodeProperties);

			} else if (DIR_KIND.equals(kind)) {
				loadRevisionProperties(currentRevision, data, path,
						nodeProperties);
			} else {
				// skip this case
			}

		} else if (REPLACE_ACTION.equals(action)) {

			/*
			 * Copy of add action section to start with.
			 */
			if (FILE_KIND.equals(kind)) {

				log.info("file replace on " + path);

				deletePath(data, currentRevision, path);

				/*
				 * No content length means we add the blob from the copy from
				 * revision
				 */

				if (!validBranch) {
					// an add on an invalid branch means we have a gap and the
					// path should be stored in a default branch
					// for now this will be the first directory in the path.

					data = getDefaultBranchData(path, currentRevision);
				}

				if (data != null)
					applyBlobAdd(data, path, currentRevision, nodeProperties);

			} else if (DIR_KIND.equals(kind)) {

				log.info("directory replace on " + path);


				deletePath(data, currentRevision, path);

				/*
				 * We care if the directory was copied from somewhere else
				 */

				loadRevisionProperties(currentRevision, data, path,
						nodeProperties);

				applyDirectoryAdd(data, path, currentRevision, nodeProperties);

			} else {
				// skip this case
			}
		} else if (DELETE_ACTION.equals(action)) {
			/*
			 * We make no distinction between file and directory deletes.
			 * 
			 * Just that the delete is occurring on a valid branch.
			 */

			deletePath(data, currentRevision, path);
			
		}
	}

	/*
	 * This is called if we didn't find a branch through the normal route.
	 * 
	 * We don't want to loose blobs so we will instead create a branch based on
	 * the first path part and then use that.
	 */
	private GitBranchData getDefaultBranchData(String path, long currentRevision)
			throws IOException {

		// First check if there is an existing branch that equals or contains
		// the path given

		String branchPart = GitBranchUtils.extractBranchPath(repo, path);

		if (branchPart != null) {
			String branchName = GitBranchUtils.getCanonicalBranchName(
					branchPart, currentRevision, largeBranchNameProvider);

			return getBranchData(branchName, currentRevision);

		}

		// if there is no existing branch then use the first part of the path as
		// the branch name.

		int firstSlashIndex = path.indexOf('/');

		if (firstSlashIndex == -1) {
			blobLog.println(String
					.format("(revision=%d) could not save %s because it is stored at the repository root.",
							currentRevision, path));
			return null;
		}

		/*
		 * Check if there are any known branches that contain this path.
		 * 
		 * One case if the svn:externals exists on the path so we create a
		 * branch.
		 * 
		 * The branch detector will still veto for blob adds so we just do a
		 * quick check before defaulting to the first part branch naming
		 * strategy.
		 */

		for (GitBranchData data : this.knownBranchMap.values()) {

			if (path.startsWith(data.getBranchPath())) {
				return data;
			}
		}

		String firstPart = path.substring(0, firstSlashIndex);

		String branchName = GitBranchUtils.getCanonicalBranchName(firstPart,
				currentRevision, largeBranchNameProvider);

		return getBranchData(branchName, currentRevision);
	}

	private void loadRevisionProperties(long revision, GitBranchData data,
			String path, Map<String, String> nodeProperties) {

		String contentLengthProperty = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_CONTENT_LENGTH);

		String propContentLengthProperty = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_PROP_CONTENT_LENGTH);

		if (contentLengthProperty == null || propContentLengthProperty == null)
			return;

		long contentLength = Long.parseLong(contentLengthProperty);

		long propContentLength = Long.parseLong(propContentLengthProperty);

		try {
			Map<String, String> revisionProperties = org.kuali.student.common.io.IOUtils
					.extractRevisionProperties(getInputStream(),
							propContentLength, contentLength);

			if (revisionProperties.containsKey(SVN_MERGEINFO_PROPERTY_KEY)) {

				if (data != null) {

					String mergeInfoString = revisionProperties
							.get(SVN_MERGEINFO_PROPERTY_KEY);

					if (mergeInfoString.length() > 0)
						data.accumulateMergeInfo(SvnMergeInfoUtils
								.extractBranchMergeInfoFromString(
										branchDetector, mergeInfoString));
					else {
						data.clearMergeInfo();
					}
				}
			}

			// intentionally not an else-if
			if (revisionProperties.containsKey(SVN_EXTERNALS_PROPERTY_KEY)) {

				String externalString = revisionProperties
						.get(SVN_EXTERNALS_PROPERTY_KEY);

				List<ExternalModuleInfo> externals = SvnExternalsUtils
						.extractExternalModuleInfoFromString(revision,
								repositoryBaseUrl, externalString);

				if (externals.size() > 0) {

					if (data == null) {

						/*
						 * not detected as a branch but externals makes it a
						 * branch.
						 */
						String branchName = GitBranchUtils
								.getCanonicalBranchName(path, revision,
										largeBranchNameProvider);

						data = getBranchData(branchName, revision);

						data.setCreated(true);

					}

					data.setExternals(externals);

				} else {
					if (data != null)
						data.clearExternals();
				}
			}

			nodeProperties.putAll(revisionProperties);

		} catch (Exception e) {
			throw new RuntimeException(
					"failed to extract revision properties for prop content length = "
							+ propContentLength, e);
		}

	}

	@Override
	public GitBranchData getBranchData(String branchName, long revision) {

		GitBranchData data = knownBranchMap.get(branchName);

		if (data == null) {
			data = new GitBranchData(repo, branchName, revision, revisionMapper,
					treeProcessor, treeProcessor.getNodeInitializer());

			/*
			 * Notice if there is already a branch of the same name (it should be the parent of this new commit).
			 */ 
			try {
				
				ObjectId parentId = revisionMapper.getRevisionBranchHead((revision-1), data.getBranchName());
				
				if (parentId != null)
					data.setParentId(parentId);

			} catch (Exception e) {
				log.debug("no existing reference for branch = "
						+ data.getBranchName());
			}

			knownBranchMap.put(branchName, data);
		}

		return data;
	}

	/*
	 * Cases: 1. Add a new File 2. Add an unchanged copy of an existing file 3.
	 * Add a changed copy of an existing file.
	 * 
	 * In case 2 we will have to look up the blob id using the revision mapper.
	 * 
	 * For 1 and 3 because we are using version 2 streams the blob content will
	 * be present and retrievable using the content-length property.
	 * 
	 * For 2 and 3 we determine the branch head to use in the merge commit.
	 */
	private void applyBlobAdd(GitBranchData data, String path,
			long currentRevision, Map<String, String> nodeProperties)
			throws IOException, VetoBranchException {

		/*
		 * Part A: determine the merge branch
		 */

		String copyFromPath = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_PATH);
		
		BranchData copyFromBranchData = null;

		if (copyFromPath != null) {

			long copyFromRevision = Long.valueOf(nodeProperties
					.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_REV));

			
			try {
				copyFromBranchData = branchDetector.parseBranch(
						copyFromRevision, copyFromPath);
			} catch (VetoBranchException e1) {

				// check the default branch
				List<SvnRevisionMapResults> copyFromBranches = revisionMapper
						.getRevisionBranches(copyFromRevision, copyFromPath);

				
				if (copyFromBranches.size() == 1) {
					
					SvnRevisionMapResults results = copyFromBranches.get(0);
					copyFromBranchData = new BranchData (copyFromRevision, results.getRevMap().getBranchPath().substring(Constants.R_HEADS.length()), results.getSubPath());
					
				}
				else {
					
					if (copyFromBranches.size() == 0) {
						log.warn("no copyfrom branch found for: " + copyFromPath);
						vetoLog.println(String
								.format("no copyfrom branch fround at blob add CurrentRevision: %s, Branch: %s, CopyFromRevision: %s, CopyFromPath: %s",
										String.valueOf(currentRevision),
										data.getBranchName(), copyFromRevision,
										copyFromPath));
					}
					else if (copyFromBranches.size() > 1) {
						log.warn("multiple copyfrom branches found for " + copyFromPath);
						vetoLog.println(String
								.format("multiple copyfrom branch fround at blob add CurrentRevision: %s, Branch: %s, CopyFromRevision: %s, CopyFromPath: %s",
										String.valueOf(currentRevision),
										data.getBranchName(), copyFromRevision,
										copyFromPath));
					}
				
					
				return;
				
				}
			}

			if (!copyFromBranchData.getBranchPath()
					.equals(data.getBranchPath())) {

				String copyFromBranchName = GitBranchUtils
						.getCanonicalBranchName(
								copyFromBranchData.getBranchPath(),
								copyFromRevision, largeBranchNameProvider);

				// register the merge
				ObjectId head = revisionMapper.getRevisionBranchHead(
						copyFromRevision, copyFromBranchName);

				if (head == null) {
					/*
					 * We don't have a branch for the copy from file so skip the
					 * merge on this one.
					 */
					this.copyFromSkippedLog.println(String.format(
							"no branch for (path=%s, revision = %d",
							copyFromPath, copyFromRevision));
				} else {
					data.addMergeParentId(head);

				}

			}

		}

		/*
		 * Step B: store the blob or find the copy from blob and add it into the
		 * current branch at the current path.
		 */

		log.debug("branch = " + data.getBranchPath() + ", path = " + path
				+ ", at revision " + currentRevision);

		try {
			ObjectId id = storeBlob(data, path, copyFromBranchData, nodeProperties);

			if (id != null) {
				data.addBlob(path, id, blobLog);
			} else {

				log.warn("failed to store blob at path = " + path);
			}
		} catch (InvalidBlobChangeException e) {
			// this is ok but lets log to make sure we are getting all of them.
			log.warn("invalid blob change occured at " + currentRevision
					+ " on path: " + path);
		}

	}

	private ObjectId storeBlob(GitBranchData data, String path,
			BranchData copyFromBranchData, Map<String, String> nodeProperties) throws VetoBranchException,
			InvalidBlobChangeException, IOException {

		String contentLengthProperty = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_CONTENT_LENGTH);

		String copyFromPath = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_PATH);

		String copyFromRevisionString = nodeProperties.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_REV);
		
		String propContentLengthProperty = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_PROP_CONTENT_LENGTH);
		
		String textContentLengthProperty = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_TEXT_CONTENT_LENGTH);
		
		long copyFromRevision = -1;
		
		if (copyFromRevisionString != null)
			copyFromRevision = Long.valueOf(copyFromRevisionString);
		
		if (contentLengthProperty == null) {
			// add case 2 : Add an unchanged copy of an existing file
			return getBlobId(copyFromPath, copyFromBranchData, copyFromRevision);

		} else {

			// add case 1 or add case 3 : file content exists so save it.

			long contentLength = Long.parseLong(contentLengthProperty);

			long propContentLength = 0L;

			if (propContentLengthProperty != null)
				propContentLength = Long.parseLong(propContentLengthProperty);
			
			long textContentLength = 0L;
			
			if (textContentLengthProperty != null)
				textContentLength = Long.parseLong(textContentLengthProperty);
				
			if (propContentLength == contentLength) {
				
				/*
				 * Normally skip over the change (return null case below).
				 * 
				 * If there is a copyfrom source find the blob id that way.
				 * 
				 * If the content length is specified as 0 create an empty file
				 */

				if (copyFromPath != null) {
					// use the copyfrom data to get an existing blob id.
					return getBlobId(copyFromPath, copyFromBranchData, copyFromRevision);
				}
				else if (textContentLength == 0) {
					
					// if the content was specified as zero then create the file
					// but with no content.

					// skip over the spacer line
					int spacer = getInputStream().read();

					if (spacer != '\n') {
						log.error("SPACER LINE HAS DATA: ");
					}

					return storeBlob(data, path, contentLength,
							propContentLength);
					
				}
				else {
					// there is no file change so don't do anything
					log.warn(SvnDumpFilter.SVN_DUMP_KEY_PROP_CONTENT_LENGTH
							+ " size equals "
							+ SvnDumpFilter.SVN_DUMP_KEY_CONTENT_LENGTH + " of "
							+ contentLength + " for path = " + path);
					return null;
				}
			}
			
			/*
			 * Standard case ingest the blob content and store it in git.
			 */

			// skip over the spacer line
			int spacer = getInputStream().read();

			if (spacer != '\n') {
				log.error("SPACER LINE HAS DATA: ");
			}
						
			return storeBlob(data, path, contentLength, propContentLength);
		}

	}

	private ObjectId getBlobId(String path, final BranchData copyFromBranchData, long revision)
			throws VetoBranchException, IOException {

		if (copyFromBranchData == null) {
			log.warn("getBlobId no copyfrom branch for path " + path + " at " + revision);
			return null;
		}
		

		String branchName = GitBranchUtils.getCanonicalBranchName(
				copyFromBranchData.getBranchPath(), revision, largeBranchNameProvider);

		ObjectId head = revisionMapper.getRevisionBranchHead(revision,
				branchName);

		if (head == null) {
			log.warn("(copy-from) no branch found for branch path = "
					+ copyFromBranchData.getBranchPath() + " at " + revision);
			return null;
		}

		GitTreeData copyFromTreeData = treeProcessor.extractExistingTreeDataFromCommit(head);
		
		ObjectId blobId = copyFromTreeData.find(repo, copyFromBranchData.getPath());
		
		return blobId;
		
	}

	private void deleteBranch(String branchName, long currentRevision)
			throws IOException {

		Ref archivedBranchRef = GitRefUtils.archiveBranch(repo, largeBranchNameProvider, commitData.getPersonIdent(), branchName, currentRevision);
		
		if (archivedBranchRef == null) {
			log.warn("problems archiving branch = " + branchName);
		}
		else {
			
			log.info("archived " + branchName + " to " + archivedBranchRef.getName());
		}
		
		// also remove this branch from the known branches
		knownBranchMap.remove(branchName);
	}

	private void applyDirectoryAdd(final GitBranchData data, final String path,
			final long currentRevision, Map<String, String> nodeProperties) {

		/*
		 * A directory add can occur within a branch
		 * 
		 * but it can also take place above, in which case we need to look at
		 * the path in relationship to the branches that it matches and apply
		 * the blobs accordingly.
		 * 
		 * It can also take place within an existing branch in which case we
		 * need to navigate the subtree to find the blobs.
		 */

		try {
			CopyFromOperation copyFromOperation = computeTargetBranches(data,
					path, currentRevision);

			computeCopyFromBranches(copyFromOperation, nodeProperties);

			for (GitBranchData targetBranch : copyFromOperation
					.getTargetBranches()) {

				List<SvnRevisionMapResults> copyFromBranches = copyFromOperation
						.getCopyFromBranches();

				if (copyFromOperation.getType()
						.equals(OperationType.SINGLE_NEW)) {

					/*
					 * If the parent exists then this is the same as a delete
					 * and copy.
					 * 
					 * We are no longer connected to the old parent only the
					 * copyfrom data.
					 */
					if (data.getParentId() != null) {

						deleteBranch(data.getBranchName(), currentRevision);

						data.reset();
					}

					if (copyFromBranches.size() == 1) {
						// this is a new branch copied from the old branch
						// copy the svn:externals and svn:mergeinfo data aswell.
						SvnRevisionMapResults results = copyFromBranches.get(0);

						if (results != null && results.getSubPath() != null
								&& results.getSubPath().length() == 0) {

							GitBranchDataUtils.extractAndStoreBranchMerges(
									results.getRevMap().getRevision(), results
											.getRevMap().getBranchName(), data,
									revisionMapper);

							ObjectId parentId = ObjectId.fromString(results
									.getRevMap().getCommitId());

							// make a shallow copy of the parent tree. only the
							// blobs in the root directory
							GitTreeData parentTreeData = treeProcessor
									.extractExistingTreeDataFromCommit(parentId);

							// shallow because this method extract the externals from a
							// fusion-maven-plugin.dat file at the root of the
							// tree.
							// then it will set the details into the target branch.
							GitBranchDataUtils.extractExternalModules(repo, 
									parentTreeData, data, treeProcessor);

						}
					} else if (copyFromBranches.size() > 1) {

						log.warn("multiple copy from case at rev = "
								+ currentRevision + " path = " + path);
					}

				} else if (copyFromOperation.getType().equals(
						OperationType.INVALID_SINGLE_NEW)) {

					if (copyFromBranches.size() > 0) {

						/*
						 * Normally we would not store into the path given but
						 * because it is a copy from we will do it.
						 */

						String branchName = GitBranchUtils
								.getCanonicalBranchName(path, currentRevision,
										largeBranchNameProvider);

						if (targetBranch != null) {
							log.warn("overriting target branch at rev = "
									+ currentRevision + " and path = " + path);
						}

						targetBranch = getBranchData(branchName,
								currentRevision);

					}

				}

				/*
				 * In this case the number of copyfrom branches can be very
				 * high.
				 * 
				 * And we need an efficient way to perform the copy.
				 * 
				 * To start with we will ignore the possible child branches and
				 * just use the existing git tree objects represented by the
				 * copyfrom branches.
				 */

				for (SvnRevisionMapResults revisionMapResults : copyFromBranches) {

					ObjectId copyFromBranchCommitId = ObjectId
							.fromString(revisionMapResults.getRevMap()
									.getCommitId());

					String copyFromPath = revisionMapResults.getCopyFromPath();

					String copyFromBranchPath = revisionMapResults.getRevMap()
							.getBranchPath()
							.substring(Constants.R_HEADS.length());

					String copyFromBranchSubPath = revisionMapResults
							.getSubPath();

					String targetPath = GitBranchUtils.convertToTargetPath(
							path, copyFromPath, new BranchData(currentRevision,
									copyFromBranchPath, copyFromBranchSubPath));

					try {

						BranchData adjustedBranch = branchDetector.parseBranch(
								currentRevision, targetPath);

						String branchName = GitBranchUtils
								.getCanonicalBranchName(
										adjustedBranch.getBranchPath(),
										currentRevision,
										largeBranchNameProvider);

						GitBranchData adjustedTargetBranch = getBranchData(
								branchName, currentRevision);
						
						applyCopy(adjustedTargetBranch, targetPath, copyFromBranchSubPath, copyFromBranchCommitId);

						// create the new branch
						
						Ref ref = repo.getRef(Constants.R_HEADS + branchName);
						
						if (ref == null || copyFromOperation.getType().equals(
								OperationType.INVALID_SINGLE_NEW)
								|| copyFromOperation.getType().equals(
										OperationType.SINGLE_NEW)) {

							adjustedTargetBranch.setCreated(true);

							// copy over any svn merge data

							GitBranchDataUtils.extractAndStoreBranchMerges(
									revisionMapResults.getRevMap()
											.getRevision(), revisionMapResults
											.getRevMap().getBranchName(),
									adjustedTargetBranch, revisionMapper);

							ObjectId parentId = ObjectId
									.fromString(revisionMapResults.getRevMap()
											.getCommitId());

							// make a shallow copy of the parent tree. only the
							// blobs in the root directory
							GitTreeData parentTreeData = treeProcessor
									.extractExistingTreeDataFromCommit(parentId);

							// shallow because this method will insert a
							// fusion-maven-plugin.dat file at the root of the
							// tree.
							GitBranchDataUtils.extractExternalModules(repo, 
									parentTreeData, adjustedTargetBranch,
									treeProcessor);
						}

						adjustedTargetBranch
								.addMergeParentId(copyFromBranchCommitId);

					} catch (VetoBranchException e) {

						// add the branch tree into the target branch

						applyCopy(targetBranch, targetPath, copyFromBranchSubPath, copyFromBranchCommitId);
						
						targetBranch.setCreated(true);

						targetBranch.addMergeParentId(copyFromBranchCommitId);

					}

				}

				if (copyFromOperation.getType()
						.equals(OperationType.SINGLE_NEW)
						|| copyFromOperation.getType().equals(
								OperationType.INVALID_SINGLE_NEW)) {
					if (targetBranch != null
							&& (targetBranch.getBlobsAdded() > 0 || targetBranch.isTreeDirty()))
						targetBranch.setCreated(true);
				}

			}
		} catch (IOException e) {
			log.error(String.format("failed to process directory add %s at %d",
					path, currentRevision), e);
		}

	}

	private void applyCopy(GitBranchData adjustedTargetBranch, String targetPath, String copyFromBranchSubPath, ObjectId copyFromBranchCommitId) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		/*
		 * There are two kinds of subtree paths:
		 * 
		 * 1. where the copy from source is a subdirectory of
		 * the copy from branch
		 * 
		 * 2. where the target path is deeper then the path
		 * (i.e. we matched a branch with a name longer than the
		 * svn target path)
		 */

		String targetSubtreePath = "";
		
		if (adjustedTargetBranch.getBranchName().length() <= targetPath.length()) {
			
			targetSubtreePath = targetPath
					.substring(adjustedTargetBranch.getBranchPath()
							.length());
		}
		
		

		if (targetSubtreePath.startsWith("/")) {
			targetSubtreePath = targetSubtreePath.substring(1);
		}

		ObjectId treeId = null;

		if (copyFromBranchSubPath != null
				&& !copyFromBranchSubPath.isEmpty()) {
			treeId = treeProcessor.getObjectId(
					copyFromBranchCommitId,
					copyFromBranchSubPath);
		} else {

			treeId = treeProcessor.getTreeId(copyFromBranchCommitId);
		}
		
		adjustedTargetBranch.addTree(targetSubtreePath, treeId);
		
	}

	/*
	 * Apply the copy from into the target
	 */
	private void applyDirectoryCopy(long currentRevision, String path,
			OperationType operationType, String copyFromPath,
			GitBranchData targetBranch, OperationType type,
			SvnRevisionMapResults revisionMapResults) {

		ObjectId commitId = ObjectId.fromString(revisionMapResults.getRevMap()
				.getCommitId());

		try {

			boolean fallbackOnTargetBranch = false;

			if (operationType.equals(OperationType.INVALID_SINGLE_NEW))
				fallbackOnTargetBranch = true;

			treeProcessor.visitBlobs(commitId, new CopyFromTreeBlobVisitor(
					currentRevision, path, targetBranch, type, copyFromPath,
					fallbackOnTargetBranch, revisionMapResults,
					largeBranchNameProvider, branchDetector, this, vetoLog,
					blobLog), createPathFilter(revisionMapResults));
		} catch (Exception e) {
			log.error("failed to visit blobs", e);
		}

	}

	private TreeFilter createPathFilter(SvnRevisionMapResults revisionMapResults) {

		if (revisionMapResults.getSubPath().length() > 0)
			return PathFilter.create(revisionMapResults.getSubPath());
		else
			return null;
	}

	private void computeCopyFromBranches(CopyFromOperation copyOp,
			Map<String, String> nodeProperties) throws IOException {

		final String copyFromPath = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_PATH);

		if (copyFromPath != null) {

			if (copyFromPath.isEmpty()) {
				log.warn("copyfrom path is empty which implies a copy of the repository root.  this is not supported right now.");
				return;
			}

			long copyFromRevision = Long.valueOf(nodeProperties
					.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_REV));

			List<SvnRevisionMapResults> copyFromBranches = revisionMapper
					.getRevisionBranches(copyFromRevision, copyFromPath);

			copyOp.setCopyFromBranches(copyFromBranches, copyFromPath);

		}
	}

	private GitBranchData getBranchData(SvnRevisionMap revMap) {

		return getBranchData(normalizeBranchName (revMap.getBranchName()), revMap.getRevision());
	}

	private String normalizeBranchName(String branchName) {
		
		if (branchName.startsWith(Constants.R_HEADS)) 
			branchName = branchName.substring(Constants.R_HEADS.length());
		
		return branchName;
	}

	private List<SvnRevisionMap> getRevMapList(
			List<SvnRevisionMapResults> revMapResults) {
		List<SvnRevisionMap> revMaps = new ArrayList<>(revMapResults.size());

		for (SvnRevisionMapResults revisionMapResults : revMapResults) {

			revMaps.add(revisionMapResults.getRevMap());
		}

		return revMaps;
	}

	private List<GitBranchData> getBranchDataList(List<SvnRevisionMap> revMaps) {

		List<GitBranchData> gitBranchDataList = new ArrayList<>(revMaps.size());

		for (SvnRevisionMap revMap : revMaps) {

			gitBranchDataList.add(getBranchData(revMap));
		}

		return gitBranchDataList;
	}

	/*
	 * target is multiple branches, a single branch or a sub tree within a
	 * single branch
	 */
	private CopyFromOperation computeTargetBranches(GitBranchData data,
			String path, long currentRevision) throws IOException {
		
		CopyFromOperation copyOp = null;

		List<SvnRevisionMapResults> targetBranches = revisionMapper
				.getRevisionBranches(currentRevision-1, path);

		if (targetBranches.size() == 1) {
			SvnRevisionMap revMap = targetBranches.get(0).getRevMap();

			String adjustedBranchPath = revMap.getBranchPath().substring(
					Constants.R_HEADS.length());

			if (adjustedBranchPath.length() < path.length()) {
				copyOp = new CopyFromOperation(OperationType.SUBTREE);
			} else {
				copyOp = new CopyFromOperation(OperationType.SINGLE);
			}

			copyOp.setTargetBranches(Arrays
					.asList(new GitBranchData[] { getBranchData(revMap) }));

		} else {

			if (targetBranches.size() == 0) {

				if (data == null) {
					/*
					 * target is not a branch but is a copy op.
					 */
					copyOp = new CopyFromOperation(
							OperationType.INVALID_SINGLE_NEW);
				} else {
					if (data.getBranchPath().equals(path)) {
						// new single branch
						copyOp = new CopyFromOperation(OperationType.SINGLE_NEW);
					} else {
						// existing single branch
						copyOp = new CopyFromOperation(OperationType.SINGLE);
					}
				}

				copyOp.setTargetBranches(Arrays
						.asList(new GitBranchData[] { data }));
			} else {
				copyOp = new CopyFromOperation(OperationType.MULTI);

				copyOp.setTargetBranches(getBranchDataList(getRevMapList(targetBranches)));
			}
		}

		return copyOp;
	}

	private void deletePath(GitBranchData data, long currentRevision,
			String path) throws IOException {

		// TODO: refactor this logic to work with actual Ref's or branches that represent those refs.
		List<SvnRevisionMapResults> targetBranches = revisionMapper
				.getRevisionBranches(currentRevision-1, path);
		
		boolean processedCurrentBranch = false;
		
		
		for (SvnRevisionMapResults revisionMapResults : targetBranches) {
			
			String candidateBranchName = normalizeBranchName(revisionMapResults.getRevMap().getBranchName());
			
			if (revisionMapResults.getSubPath().length() > 0) {
				// delete the path from this branch
				GitBranchData branchData = getBranchData(revisionMapResults.getRevMap());
				
				if (data != null && data.getBranchName().equals(candidateBranchName)) {
					processedCurrentBranch = true;
				}
				branchData.deletePath(path, currentRevision);
			}
			else {
				
				if (data != null && data.getBranchName().equals(candidateBranchName)) {
					processedCurrentBranch = true;
				}
				
				deleteBranch(candidateBranchName, currentRevision);
			}
		}
		
		if (data != null && !processedCurrentBranch) {
			

			if (data.getBranchPath().equals(path)) {
				// delete the current branch
				deleteBranch(data.getBranchName(), currentRevision);
			}
			else {
				// delete a path in the current branch
				data.deletePath(path, currentRevision);
			}
			
			
		}
		
	}

	private ObjectId storeBlob(GitBranchData gbd, String path,
			long contentLength, long propContentLength) {

		try {

			long adjustedContentLength = contentLength - propContentLength;

			if (propContentLength > 0) {
				getInputStream().skip(propContentLength);
			}

			/*
			 * We want to replace the contents of the path file with the content
			 * that we read from the input stream.
			 */

			ObjectInserter objectInserter = repo.newObjectInserter();

			ObjectId id = objectInserter.insert(Constants.OBJ_BLOB,
					adjustedContentLength, new BoundedInputStream(
							getInputStream(), adjustedContentLength));

			objectInserter.flush();
			objectInserter.release();

			return id;

		} catch (Exception e) {

			log.error("onNodeContentChanged failed: ", e);
			throw new RuntimeException("onNodeContentLength failed", e);
		}
	}

	public void setCommitData(GitCommitData commitData) {
		this.commitData = commitData;

	}

}
