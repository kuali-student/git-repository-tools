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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefRename;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.kuali.student.git.importer.GitImporterParseOptions;
import org.kuali.student.git.model.CopyFromOperation.OperationType;
import org.kuali.student.git.model.GitTreeProcessor.GitTreeBlobVisitor;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMapResults;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.model.util.GitTreeDataUtils;
import org.kuali.student.git.tools.SvnMergeInfoUtils;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.git.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 * 
 */
public class NodeProcessor implements IGitBranchDataProvider {

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

	public NodeProcessor(Map<String, GitBranchData> knownBranchMap,
			PrintWriter vetoLog, PrintWriter copyFromSkippedLog,
			PrintWriter blobLog, Repository repo,
			SvnRevisionMapper revisionMapper,
			GitImporterParseOptions importerParseOptions,
			BranchDetector branchDetector) {
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
		} catch (VetoBranchException e) {
			vetoLog.println(String
					.format("invalid branch (OnAfterNode) CurrentRevision: %s, Path: %s",
							String.valueOf(currentRevision), path));
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
					largeBranchNameProvider),
					currentRevision);
		}

		if (currentRevision == 7563 || currentRevision == 7446
				|| currentRevision == 7447) {
			// this is faster than the eclipse dynamic breakpoint
			log.info("");
		}

		if ("add".equals(action)) {

			if ("file".equals(kind)) {

				/*
				 * No content length means we add the blob from the copy from
				 * revision
				 */

				if (validBranch)
					applyBlobAdd(data, path, currentRevision, nodeProperties);
				else {
					log.info("skipping non branch containing path: " + path);
				}

			} else if ("dir".equals(kind)) {
				
				/*
				 * We care if the directory was copied from somewhere else
				 */
				
				loadMergeInfo(currentRevision, data, path, nodeProperties);

				applyDirectoryAdd(data, path, currentRevision, nodeProperties);

			} else {
				// skip this case
			}

		} else if ("change".equals(action)) {

			/*
			 * This can happen I think for property changes Not sure if we are
			 * doing the right thing here.
			 */
			if ("file".equals(kind)) {

				if (validBranch)
					applyBlobAdd(data, path, currentRevision, nodeProperties);
				else {
					log.info("skipping non branch containing path: " + path);
				}

			} else if ("dir".equals(kind)) {
				loadMergeInfo(currentRevision, data, path, nodeProperties);
			} else {
				// skip this case
			}

		} else if ("delete".equals(action)) {
			/*
			 * We make no distinction between file and directory deletes.
			 * 
			 * Just that the delete is occurring on a valid branch.
			 */
			if (validBranch) {

				deletePath(data, currentRevision, path);
			} else {
				/*
				 * not a known branch so we need to apply this delete to all
				 * branches starting with the path provided.
				 * 
				 * This may also be a branch delete step.
				 */
				deleteBranches(data, path, currentRevision);
			}
		}
	}

	private void loadMergeInfo(long revision, GitBranchData data, String path, Map<String, String> nodeProperties) {
		
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
							propContentLength,
							contentLength);
			
			if (revisionProperties.containsKey("svn:mergeinfo")) {
				//debugging breakpoint
				log.debug("");
				
				if (data != null) {
					
					String mergeInfoString = revisionProperties.get("svn:mergeinfo");
					
					if (mergeInfoString.length() > 0)
						revisionMapper.createMergeData(revision, data.getBranchPath(), SvnMergeInfoUtils.extractBranchMergeInfoFromString(mergeInfoString));
				}
			}
			
			nodeProperties.putAll(revisionProperties);
			
		} catch (Exception e) {
			throw new RuntimeException("failed to extract revision properties for prop content length = " + propContentLength, e);
		}
		
	}

	@Override
	public GitBranchData getBranchData(
			String branchName, long revision) {

		GitBranchData data = knownBranchMap.get(branchName);

		if (data == null) {
			data = new GitBranchData(branchName, revision,
					largeBranchNameProvider, treeProcessor, branchDetector);

			/*
			 * If the branch already exists lets copy its commit tree as the
			 * basis of this commit.
			 * 
			 * Then all of the detected adds and deletes can apply on this tree.
			 */
			try {
				Ref ref = repo.getRef(Constants.R_HEADS + data.getBranchName());

				ObjectId parentId = null;

				if (ref != null) {
					// load some existing data like the
					// current parent sha1.
					parentId = ref.getObjectId();

					data.setParentId(parentId);

				}
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

		if (copyFromPath != null) {

			long copyFromRevision = Long.valueOf(nodeProperties
					.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_REV));

			BranchData copyFromBranchData;
			try {
				copyFromBranchData = branchDetector.parseBranch(
						copyFromRevision, copyFromPath);
			} catch (VetoBranchException e1) {
				log.warn(copyFromPath + " vetoed");
				vetoLog.println(String
						.format("detect merge vetoed CurrentRevision: %s, Branch: %s, CopyFromRevision: %s",
								"CopyFromPath: %s",
								String.valueOf(currentRevision),
								data.getBranchName(), copyFromRevision,
								copyFromPath));
				return;
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

		ObjectId id = storeBlob(data, path, nodeProperties);

		if (id != null) {
			data.addBlob(path, id.getName(), blobLog);
		} else {

			log.warn("failed to store blob at path = " + path);
		}

	}

	private ObjectId storeBlob(GitBranchData data, String path,
			Map<String, String> nodeProperties) throws VetoBranchException,
			IOException {

		String contentLengthProperty = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_CONTENT_LENGTH);

		if (contentLengthProperty == null) {

			// add case 2 : Add an unchanged copy of an existing file

			String copyFromPath = nodeProperties
					.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_PATH);

			long copyFromRevision = Long.valueOf(nodeProperties
					.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_REV));

			return getBlobId(copyFromPath, copyFromRevision);

		} else {

			// add case 1 or add case 3 : file content exists so save it.

			String propContentLengthProperty = nodeProperties
					.get(SvnDumpFilter.SVN_DUMP_KEY_PROP_CONTENT_LENGTH);

			long contentLength = Long.parseLong(contentLengthProperty);

			long propContentLength = 0L;

			if (propContentLengthProperty != null)
				propContentLength = Long.parseLong(propContentLengthProperty);

			return storeBlob(data, path, contentLength, propContentLength);
		}

	}

	private ObjectId getBlobId(String path, long revision)
			throws VetoBranchException, IOException {

		final BranchData branchData = branchDetector
				.parseBranch(revision, path);

		String branchName = GitBranchUtils.getCanonicalBranchName(
				branchData.getBranchPath(), revision, largeBranchNameProvider);

		ObjectId head = revisionMapper.getRevisionBranchHead(revision,
				branchName);

		if (head == null) {
			log.warn("no branch found for branch path = "
					+ branchData.getBranchPath() + " at " + revision);
			return null;
		}
		/*
		 * A bit of a hack but this gives us a place to put the result when
		 * found in the inner class.
		 */
		final Set<ObjectId> resultSet = new HashSet<ObjectId>();

		treeProcessor.visitBlobs(head, new GitTreeBlobVisitor() {

			@Override
			public boolean visitBlob(ObjectId blobId, String path, String name) {

				if (path.equals(branchData.getPath())) {
					resultSet.add(blobId);
					return false;
				} else
					return true;
			}
		});

		// return out the result if it exists
		if (resultSet.size() == 0)
			return null;
		else
			return resultSet.iterator().next();

	}

	private void deleteBranches(GitBranchData data, String path,
			long currentRevision) throws IOException {

		Map<String, Ref> heads = repo.getRefDatabase().getRefs(
				Constants.R_HEADS);

		for (Map.Entry<String, Ref> entry : heads.entrySet()) {
			String branchName = entry.getKey();

			if (branchName.contains("@"))
				continue; // skip over archived references

			String largeBranchName = largeBranchNameProvider.getBranchName(
					branchName, currentRevision);

			if (largeBranchName != null) {
				branchName = largeBranchName;
			}

			String branchPath = GitBranchUtils.getBranchPath(branchName,
					currentRevision, largeBranchNameProvider);

			if (branchPath.equals(path) || branchPath.startsWith(path + "/")) {

				/*
				 * require the last element to totally match:
				 * 
				 * ks-1.3, ks-1.3-ec1 if we delete ks-1.3 we only want ks-1.3 to be deleted.  Without the trailing "/" ks-1.3-ec1 was also being deleted.
				 */
				deleteBranch(branchName, currentRevision);
			}
		}
	}

	private void deleteBranch(String branchName, long currentRevision)
			throws IOException {

		Ref existingBranchRef = repo.getRef(Constants.R_HEADS + branchName);

		String archivedBranchReference = Constants.R_HEADS + branchName + "@"
				+ (currentRevision - 1);

		if (archivedBranchReference.length() >= GitBranchUtils.FILE_SYSTEM_NAME_LIMIT) {
			archivedBranchReference = Constants.R_HEADS
					+ largeBranchNameProvider.storeLargeBranchName(
							archivedBranchReference, currentRevision);
		}

		RefRename rename = repo.renameRef(existingBranchRef.getName(),
				archivedBranchReference);

		rename.setRefLogIdent(commitData.getPersonIdent());
		rename.setRefLogMessage(commitData.getPersonIdent() + " deleted "
				+ branchName + "renaming the branch");

		log.info("renamed " + branchName + " to " + archivedBranchReference);

		Result result = rename.rename();

		if (result != Result.RENAMED)
			log.warn("problems renaming branch = " + branchName + " to "
					+ archivedBranchReference);
		else {
			log.info("renamed " + branchName + " to " + archivedBranchReference);
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
			CopyFromOperation copyFromOperation = computeTargetBranches (data, path, currentRevision);

			computeCopyFromBranches (copyFromOperation, nodeProperties);
			
			
			for (GitBranchData targetBranch : copyFromOperation.getTargetBranches()) {
				
				
				List<SvnRevisionMapResults> copyFromBranches = copyFromOperation.getCopyFromBranches();

				if (copyFromOperation.getType().equals(OperationType.SINGLE_NEW)) {
					
					/*
					 * If the parent exists then this is the same as a delete and copy.
					 * 
					 * We are no longer connected to the old parent only the copyfrom data.
					 */
					if (data.getParentId() != null) {

						deleteBranch(data.getBranchName(), currentRevision);

						data.reset();
					}
					
				}
				
				for (SvnRevisionMapResults revisionMapResults : copyFromBranches) {
					
					applyDirectoryCopy (currentRevision, path, targetBranch, copyFromOperation.getType(), revisionMapResults);
					
					data.addMergeParentId(ObjectId.fromString(revisionMapResults.getRevMap().getCommitId()));
					
				}
				
				if (data.getBlobsAdded() > 0 && copyFromOperation.getType().equals(OperationType.SINGLE_NEW)) {
					data.setCreated(true);
				}
				
			}
		} catch (IOException e) {
			log.error(String.format("failed to process directory add %s at %d", path, currentRevision), e);
		}
		

	}
	
	/*
	 * Apply the copy from into the target
	 */
	private void applyDirectoryCopy(long currentRevision, String path, GitBranchData targetBranch,
			OperationType type, SvnRevisionMapResults revisionMapResults) {
		
		
		ObjectId commitId = ObjectId.fromString(revisionMapResults.getRevMap().getCommitId());
		
		try {
			treeProcessor.visitBlobs(commitId, new CopyFromTreeBlobVisitor(currentRevision, path, targetBranch, type, revisionMapResults, largeBranchNameProvider, branchDetector, this, vetoLog, blobLog), createPathFilter (revisionMapResults));
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
			
			
			List<SvnRevisionMapResults> copyFromBranches = revisionMapper.getRevisionBranches(copyFromRevision, copyFromPath);
			
			copyOp.setCopyFromBranches(copyFromBranches);

		}
	}

	private GitBranchData getBranchData (SvnRevisionMap revMap) {
		
		return getBranchData(revMap.getBranchName(), revMap.getRevision());
	}
	
	
	private List<SvnRevisionMap>getRevMapList(List<SvnRevisionMapResults>revMapResults) {
		List<SvnRevisionMap>revMaps = new ArrayList<>(revMapResults.size());
		
		for (SvnRevisionMapResults revisionMapResults : revMapResults) {
			
			revMaps.add(revisionMapResults.getRevMap());
		}
		
		return revMaps;
	}
	private List<GitBranchData>getBranchDataList (List<SvnRevisionMap>revMaps) {
		
		List<GitBranchData> gitBranchDataList = new ArrayList<>(revMaps.size());
		
		for (SvnRevisionMap revMap : revMaps) {
			
			gitBranchDataList.add(getBranchData(revMap));
		}
		
		return gitBranchDataList;
	}
	/*
	 * target is multiple branches, a single branch or a sub tree within a single branch
	 */
	private CopyFromOperation computeTargetBranches(GitBranchData data,
			String path, long currentRevision) throws IOException {
		
		CopyFromOperation copyOp = null;
		
		List<SvnRevisionMapResults> targetBranches = revisionMapper.getRevisionBranches(currentRevision, path);
		
		if (targetBranches.size() == 1) {
			SvnRevisionMap revMap = targetBranches.get(0).getRevMap();
			
			String adjustedBranchPath = revMap.getBranchPath().substring(Constants.R_HEADS.length());
			
			if (adjustedBranchPath.length() < path.length()) {
				copyOp = new CopyFromOperation(OperationType.SUBTREE);
			}
			else
				copyOp = new CopyFromOperation(OperationType.SINGLE);
			
			
			copyOp.setTargetBranches(Arrays.asList(new GitBranchData[] {getBranchData(revMap)}));
			
		}
		else {
			
			if (targetBranches.size() == 0 && data != null) {
				
				if (data.getBranchPath().equals(path)) {
					// 	new single branch
					copyOp = new CopyFromOperation(OperationType.SINGLE_NEW);
				}
				else {
					// existing single branch
					copyOp = new CopyFromOperation(OperationType.SINGLE);
				}
				
				copyOp.setTargetBranches(Arrays.asList(new GitBranchData[] {data}));
			}
			else {
				copyOp = new CopyFromOperation(OperationType.MULTI);
			
				copyOp.setTargetBranches(getBranchDataList(getRevMapList(targetBranches)));
			}
		}

		
		
		return copyOp;
	}

	private void applyCopyFromAboveBranchLevel(List<BranchData> copyFromBranches, long copyFromRevision,
			String copyFromPath, String path, long currentRevision)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {

		List<SvnRevisionMap> heads = revisionMapper
				.getRevisionHeads(copyFromRevision);

		for (SvnRevisionMap revMap : heads) {

			String currentBranchPath = revMap.getBranchPath();

			if (currentBranchPath.startsWith(Constants.R_HEADS + copyFromPath)) {
				applyCopyFromDirectoryTree(revMap, copyFromPath, path,
						currentRevision);
			}

		}

	}

	private void applyCopyFromDirectorySubTree(final GitBranchData data,
			final ObjectId copyFromCommitId, final String copyFromPath,
			final String path, final long currentRevision,
			final long copyFromRevision) throws MissingObjectException,
			IncorrectObjectTypeException, IOException {

		/*
		 * The copy from branch could be a single discrete branch.
		 * 
		 * However it could also be a prefix leading to several branches.
		 * 
		 * We need to allow for that posibility where we might find
		 */
		
		List<BranchData> copyFromBranches =  null;
		
//		parseBranches(
//					copyFromRevision, copyFromPath);
			
		for (final BranchData copyFromBranch : copyFromBranches) {

			TreeFilter pathFilter = createPathFilter(copyFromBranch, copyFromPath);

			
			String copyFromBranchName = GitBranchUtils
					.getCanonicalBranchName(
							copyFromBranch.getBranchPath(),
							copyFromRevision, largeBranchNameProvider);

			ObjectId copyFromBranchCommitId = revisionMapper
					.getRevisionBranchHead(copyFromRevision,
							copyFromBranchName);
			
			if (copyFromBranchCommitId == null) {
				log.warn("failed to find a branch commit for copyfrom branch : " + copyFromBranchName);
				continue;
			}
			
			
			
			data.addMergeParentId(copyFromBranchCommitId);

			treeProcessor.visitBlobs(copyFromBranchCommitId,
					new GitTreeBlobVisitor() {

				
						@Override
						public boolean visitBlob(ObjectId blobId,
								String blobPath, String name) throws MissingObjectException, IncorrectObjectTypeException, IOException {

							try {

								String alteredBlobPath = GitBranchUtils
										.convertToTargetPath(path,
												copyFromRevision, copyFromBranch.getPath(),
												blobPath, branchDetector);

								/*
								 * In most cases this blob path will live in the
								 * branch identified as data.
								 * 
								 * But in some cases the branch could be
								 * different. i.e. the full blob path shows a
								 * different branch.
								 * 
								 * Detect the branch path and if different from
								 * data then record this blob separately.
								 */

								BranchData alteredData = null;
								try {

									alteredData = branchDetector.parseBranch(
											currentRevision, alteredBlobPath);

								} catch (VetoBranchException e1) {
									vetoLog.print("vetoed alteredBlobPath = "
											+ alteredBlobPath);
									return false;
								}

								if (alteredData.getBranchPath().equals(
										data.getBranchPath())) {
									// same branch
									data.addBlob(alteredBlobPath,
											blobId.getName(), blobLog);
								} else {
									// a different branch
									GitBranchData alteredBranchData = getBranchData(
											GitBranchUtils.getCanonicalBranchName(
													alteredData.getBranchPath(),
													currentRevision,
													largeBranchNameProvider),
											currentRevision);

									alteredBranchData.addBlob(alteredBlobPath,
											blobId.getName(), blobLog);

								}

							} catch (VetoBranchException e) {
								vetoLog.println(String
										.format("tree walk add blob vetoed. CurrentRevision: %s, Current Branch Name: %s, Blob Path: %s",
												String.valueOf(currentRevision),
												data.getBranchName(), path));
								// intentionally continue

							}

							// visit all of the blobs
							return true;
						}
					}, pathFilter);

		}
		

	}

	private TreeFilter createPathFilter(BranchData copyFromBranch,
			String copyFromPath) {
		
		String candidateBranchPath = copyFromBranch.getBranchPath();
		
		if (candidateBranchPath.startsWith(copyFromPath))
			return null; 
		
		String candidateBranchParts[] = candidateBranchPath.split("\\/");
		
		String copyFromPathParts[] = copyFromPath.split("\\/");
		
		if (copyFromPathParts.length <= candidateBranchParts.length)
			return null;
		
		String adjustedCopyFromPath = StringUtils.join(copyFromPathParts, "/", candidateBranchParts.length, copyFromPathParts.length);
		
		/*
		 * So long as the copyFromPath is deeper than the copyFromBranch we
		 * should use a path filter.
		 */
		return PathFilter.create(adjustedCopyFromPath);

	}

	

	private void applyCopyFromDirectoryTree(final SvnRevisionMap revMap,
			final String copyFromPath, final String path,
			final long currentRevision) throws MissingObjectException,
			IncorrectObjectTypeException, IOException {

		applyCopyFromDirectoryTree(revMap.getCommitId(),
				revMap.getBranchPath(), copyFromPath, path, currentRevision);
	}

	private void applyCopyFromDirectoryTree(final String commitId,
			final String copyFromBranchPath, final String copyFromPath,
			final String path, final long currentRevision)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {

		treeProcessor.visitBlobs(ObjectId.fromString(commitId),
				new GitTreeBlobVisitor() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see org.kuali.student.git.model.GitTreeProcessor.
					 * GitTreeBlobVisitor
					 * #visitBlob(org.eclipse.jgit.lib.ObjectId,
					 * java.lang.String)
					 */
					@Override
					public boolean visitBlob(ObjectId blobId, String blobPath,
							String name) throws MissingObjectException, IncorrectObjectTypeException, IOException {

						StringBuilder alteredBlobPathBuilder = new StringBuilder(path);
						
						String alteredCopyFromPath = null;
						
						if (copyFromBranchPath.startsWith(Constants.R_HEADS))
							alteredCopyFromPath = copyFromBranchPath.substring(Constants.R_HEADS.length() + copyFromPath.length());
						else
							alteredCopyFromPath = copyFromBranchPath.substring(copyFromPath.length());
						
						if (alteredBlobPathBuilder.charAt(alteredBlobPathBuilder.length()-1) != '/' && (alteredCopyFromPath.length() == 0 || alteredCopyFromPath.charAt(0) != '/')) {

							alteredBlobPathBuilder.append("/");
							
						}
						
						
						alteredBlobPathBuilder.append(alteredCopyFromPath);
						
						if (alteredBlobPathBuilder.charAt(alteredBlobPathBuilder.length()-1) != '/' && blobPath.charAt(0) != '/') {

							alteredBlobPathBuilder.append("/");
							
						}
						
						alteredBlobPathBuilder.append(blobPath);

						String alteredBlobPath = alteredBlobPathBuilder
								.toString();

						if (alteredBlobPath.startsWith(Constants.R_HEADS)) {
							alteredBlobPath = alteredBlobPath.replace(
									Constants.R_HEADS, "");
						}

						BranchData data = null;
						
						try {

							data = branchDetector.parseBranch(currentRevision,
									alteredBlobPath);

						} catch (VetoBranchException e1) {
							vetoLog.print("vetoed alteredBlobPath = " + alteredBlobPath);
							return false;
						}

						GitBranchData currentBranchData = getBranchData(
								GitBranchUtils
										.getCanonicalBranchName(
												data.getBranchPath(),
												currentRevision,
												largeBranchNameProvider), currentRevision);

						if (currentBranchData.getParentId() == null)
							currentBranchData.setParentId(ObjectId
									.fromString(commitId));
						else
							currentBranchData.addMergeParentId(ObjectId
									.fromString(commitId));

						try {
							currentBranchData.addBlob(alteredBlobPath,
									blobId.getName(), blobLog);

						} catch (VetoBranchException e) {
							vetoLog.println(String
									.format("tree walk add blob vetoed. CurrentRevision: %s, Current Branch Name: %s, Blob Path: %s, New Branch Path: %s",
											String.valueOf(currentRevision),
											currentBranchData.getBranchName(),
											path, alteredBlobPath));
							
							// intentionally continue

						}

						// visit all of the blobs
						return true;
					}

				});

	}

	private void deletePath(GitBranchData data, long currentRevision,
			String path) throws IOException {

		if (path.equals(data.getBranchPath())) {
			deleteBranches(data, path, currentRevision);
		} else {

			data.deletePath(path, currentRevision);
		}
	}

	private ObjectId storeBlob(GitBranchData gbd, String path,
			long contentLength, long propContentLength) {

		try {

			long adjustedContentLength = contentLength - propContentLength;

			if (propContentLength > 0) {
				getInputStream().skip(propContentLength + 1);
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
