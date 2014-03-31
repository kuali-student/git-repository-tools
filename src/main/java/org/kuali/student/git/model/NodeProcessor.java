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
import org.kuali.student.git.tools.SvnExternalsUtils;
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

	private static final String SVN_EXTERNALS_PROPERTY_KEY = "svn:externals";

	private static final String SVN_MERGEINFO_PROPERTY_KEY = "svn:mergeinfo";

	private static final String DELETE_ACTION = "delete";

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
					largeBranchNameProvider),
					currentRevision);
		}
		

		if (ADD_ACTION.equals(action)) {

			if (FILE_KIND.equals(kind)) {

				/*
				 * No content length means we add the blob from the copy from
				 * revision
				 */

				if (!validBranch) {
					// an add on an invalid branch means we have a gap and the path should be stored in a default branch
					// for now this will be the first directory in the path.
					
					data = getDefaultBranchData(path, currentRevision);
				}
				
				if (data != null)
					applyBlobAdd(data, path, currentRevision, nodeProperties);

			} else if (DIR_KIND.equals(kind)) {
				
				/*
				 * We care if the directory was copied from somewhere else
				 */
				
				loadRevisionProperties(currentRevision, data, path, nodeProperties);

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
				loadRevisionProperties(currentRevision, data, path, nodeProperties);
			} else {
				// skip this case
			}

		} else if (DELETE_ACTION.equals(action)) {
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

	/*
	 * This is called if we didn't find a branch through the normal route.
	 * 
	 * We don't want to loose blobs so we will instead create a branch based on the first path part and then use that.
	 * 
	 */
	private GitBranchData getDefaultBranchData(String path, long currentRevision) {
		
		int firstSlashIndex = path.indexOf('/');
		
		if (firstSlashIndex == -1) {
			blobLog.println(String.format("(revision=%d) could not save %s because it is stored at the repository root.", currentRevision, path));
			return null;
		}
		
		String firstPart = path.substring(0, firstSlashIndex);
		
		String branchName = GitBranchUtils.getCanonicalBranchName(firstPart, currentRevision, largeBranchNameProvider);
		
		return getBranchData(branchName, currentRevision);
	}

	private void loadRevisionProperties(long revision, GitBranchData data, String path, Map<String, String> nodeProperties) {
		
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
			
			if (revisionProperties.containsKey(SVN_MERGEINFO_PROPERTY_KEY)) {
				//debugging breakpoint
				log.debug("");
				
				if (data != null) {
					
					String mergeInfoString = revisionProperties.get(SVN_MERGEINFO_PROPERTY_KEY);
					
					if (mergeInfoString.length() > 0)
						data.accumulateMergeInfo(SvnMergeInfoUtils.extractBranchMergeInfoFromString(branchDetector, mergeInfoString));
				}
			}
			
			// intentionally not an else-if
			if (revisionProperties.containsKey(SVN_EXTERNALS_PROPERTY_KEY)) {
				
				String externalString = revisionProperties.get(SVN_EXTERNALS_PROPERTY_KEY);
				
				List<ExternalModuleInfo> externals = SvnExternalsUtils.extractExternalModuleInfoFromString(revision, repositoryBaseUrl, externalString);
				
				if (externals.size() > 0) {
					
					ObjectInserter objectInserter = repo.newObjectInserter();

					ObjectId id = objectInserter.insert(Constants.OBJ_BLOB, createFusionMavenPluginDataFileString(externals).getBytes());
					
					data.addBlob("fusion-maven-plugin.dat", id.name(), blobLog);

					objectInserter.flush();
					objectInserter.release();
					
				}
			}
			
			nodeProperties.putAll(revisionProperties);
			
		} catch (Exception e) {
			throw new RuntimeException("failed to extract revision properties for prop content length = " + propContentLength, e);
		}
		
	}

	private String createFusionMavenPluginDataFileString(
			List<ExternalModuleInfo> externals) {
		StringBuilder builder = new StringBuilder();
		
		for (ExternalModuleInfo external : externals) {
			
			String externalModule = external.getModuleName();
			String externalBranchPath = external.getBranchPath();
			long externalRevision = external.getRevision();
			
			builder.append("# module = " + externalModule + " branch Path = " + externalBranchPath + " revision = " + externalRevision + "\n");
			
			String branchName = GitBranchUtils.getCanonicalBranchName(externalBranchPath, externalRevision, largeBranchNameProvider);
			
			ObjectId branchHead = null;
			
			try {
				branchHead = revisionMapper.getRevisionBranchHead(externalRevision, branchName);
			} catch (IOException e) {
				// intentionally fall through
			}
			
			builder.append(externalModule + "::" + branchName + "::" + branchHead==null?"UNKNOWN":branchHead.name() + "\n");
			
		}
		
		return builder.toString();
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
