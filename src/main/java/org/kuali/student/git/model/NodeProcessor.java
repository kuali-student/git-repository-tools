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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.input.BoundedInputStream;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefRename;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.TemporaryBuffer.LocalFile;
import org.kuali.student.git.importer.GitImporterParseOptions;
import org.kuali.student.git.model.GitTreeProcessor.GitTreeBlobVisitor;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.BranchDetectorImpl;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.model.util.GitTreeDataUtils;
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
public class NodeProcessor {

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
			data = getBranchData(knownBranchMap,
					GitBranchUtils.getCanonicalBranchName(
							branchData.getBranchPath(), currentRevision,
							largeBranchNameProvider), currentRevision);
		}

		if (currentRevision == 255 || currentRevision == 7446
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
				// we might care about this for merge
				// detection
				// log.info("change directory");
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

	private GitBranchData getBranchData(
			Map<String, GitBranchData> knownBranchMap, String branchName,
			long revision) {

		GitBranchData data = knownBranchMap.get(branchName);

		if (data == null) {
			data = new GitBranchData(branchName, revision,
					largeBranchNameProvider, branchDetector);

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

					GitTreeData existingTreeData = extractExistingTreeData(parentId);

					int existingBlobCount = GitTreeDataUtils
							.countBlobs(existingTreeData);

					data.mergeOntoExistingTreeData(existingTreeData);

					int mergedBlobCount = data.getBlobCount();

					if (existingBlobCount != mergedBlobCount) {
						throw new RuntimeException(
								"data loss existing count = "
										+ existingBlobCount
										+ ", merged count = " + mergedBlobCount);
					}

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

		final String copyFromPath = nodeProperties
				.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_PATH);

		if (copyFromPath != null) {

			if (copyFromPath.isEmpty()) {
				log.warn("copyfrom path is empty which implies a copy of the repository root.  this is not supported right now.");
				return;
			}

			long copyFromRevision = Long.valueOf(nodeProperties
					.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_REV));

			try {

				/*
				 * Cases 1. branch copy 2. above branch level (find the branches
				 * to which it applies) 3. within a branch level (subtree
				 * directory copy)
				 */

				if (data == null) {
					// this is an above branch level copy
					applyCopyFromAboveBranchLevel(copyFromRevision,
							copyFromPath, path, currentRevision);
					log.info("applyDirectoryAdd: data is null so attempting copy from above the branch level.");
					return;
				}

				try {
					BranchData copyFromBranchData = branchDetector.parseBranch(
							copyFromRevision, copyFromPath);

					String copyFromBranchName = GitBranchUtils
							.getCanonicalBranchName(
									copyFromBranchData.getBranchPath(),
									copyFromRevision, largeBranchNameProvider);

					ObjectId copyFromCommitId = revisionMapper
							.getRevisionBranchHead(copyFromRevision,
									copyFromBranchName);

					if (copyFromCommitId == null) {
						
						List<BranchData> copyFromBranches = parseBranches(
								copyFromRevision, copyFromPath);

							for (BranchData copyFromBranch : copyFromBranches) {
								
								/*
								 * the changes should apply properly
								 */
								
								ObjectId currentCopyFromHeadId = revisionMapper.getRevisionBranchHead(copyFromRevision, GitBranchUtils.getCanonicalBranchName(copyFromBranch.getBranchPath(), copyFromRevision, largeBranchNameProvider));
								
								applyCopyFromDirectoryTree(currentCopyFromHeadId.name(), copyFromBranch.getBranchPath(), copyFromBranch.getPath(), path, currentRevision);
								
							}
						
						
						return;
					}

					if (data.getBranchPath().equals(path)
							&& copyFromBranchData.getBranchPath().equals(
									copyFromPath)) {

						// branch copy case

						/*
						 * If the target branch exists then what we are in
						 * effect doing is a delete followed by an add.
						 */
						if (data.getParentId() != null) {

							deleteBranch(data.getBranchName(), currentRevision);

							data.reset();
						}

						// copy from the entire source tree.

						GitTreeData copyFromTreeData = null;

						copyFromTreeData = extractExistingTreeData(copyFromCommitId);

						int copyFromTreeSize = GitTreeDataUtils
								.countBlobs(copyFromTreeData);

						data.mergeOntoExistingTreeData(copyFromTreeData);

						int actual = data.getBlobCount();

						if (copyFromTreeSize != actual) {
							throw new RuntimeException(
									"data loss copy from count = "
											+ copyFromTreeSize
											+ ", copied count = " + actual);
						}

						data.setParentId(copyFromCommitId);

						data.setCreated(true);
					} else {

						if (data.getBranchPath().equals(path)) {
							// target is a branch but the source is a subtree
							// within an existing branch.

							if (data.getParentId() != null) {

								deleteBranch(data.getBranchName(),
										currentRevision);

								data.reset();
							}

							data.setParentId(copyFromCommitId);

							data.setCreated(true);

							// fall through and run the subtree copy
						}

						if (copyFromBranchData.getPath().isEmpty()) {
							// target is a directory of a branch
							// source is a branch itself.

							applyCopyFromDirectoryTree(copyFromCommitId.name(),
									copyFromBranchData.getBranchPath(), copyFromPath,
									path, currentRevision);

						} else {
							// target is a subtree of a branch and source is a
							// subtree within another branch (possibly the same
							// one)
							applyCopyFromDirectorySubTree(data,
									copyFromCommitId, copyFromPath, path,
									currentRevision, copyFromRevision);
						}
					}

				} catch (VetoBranchException e) {

					// this is an above branch level copy
					applyCopyFromAboveBranchLevel(copyFromRevision,
							copyFromPath, path, currentRevision);
				}

			} catch (IOException e) {
				throw new RuntimeException(
						"failed to parse directory invalid branch from "
								+ copyFromPath, e);
			}
		}

	}

	private void applyCopyFromAboveBranchLevel(long copyFromRevision,
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
		final List<BranchData> copyFromBranches = parseBranches(
				copyFromRevision, copyFromPath);

		if (copyFromBranches.size() == 1) {

			BranchData copyFromBranch = copyFromBranches.get(0);

			TreeFilter pathFilter = null;

			if (!copyFromBranch.getBranchPath().equals(copyFromPath) && copyFromBranch.getPath().length() > 0) {
				/*
				 * So long as the copyFromPath is deeper than the copyFromBranch
				 * we should use a path filter.
				 */
				pathFilter = PathFilter.create(copyFromBranch.getPath());
			}

			data.addMergeParentId(copyFromCommitId);

			treeProcessor.visitBlobs(copyFromCommitId,
					new GitTreeBlobVisitor() {

						@Override
						public boolean visitBlob(ObjectId blobId,
								String blobPath, String name) {

							try {

								String alteredBlobPath = GitBranchUtils
										.convertToTargetPath(path,
												copyFromRevision, copyFromPath,
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
											knownBranchMap,
											GitBranchUtils.getCanonicalBranchName(
													alteredData.getBranchPath(),
													currentRevision,
													largeBranchNameProvider),
											currentRevision);

									alteredBranchData.addBlob(alteredBlobPath,
											blobId.getName(), blobLog);

									// TODO: should a merge happen here
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

	private List<BranchData> parseBranches(long copyFromRevision,
			String copyFromPath) throws IOException {

		ArrayList<BranchData> branches = new ArrayList<>();

		List<SvnRevisionMap> heads = revisionMapper
				.getRevisionHeads(copyFromRevision);

		for (SvnRevisionMap revMap : heads) {

			String currentBranchPath = revMap.getBranchPath();

			if (currentBranchPath.startsWith(Constants.R_HEADS + copyFromPath)) {

				try {
					BranchData currentData = branchDetector.parseBranch(
							copyFromRevision, currentBranchPath);

					branches.add(currentData);

				} catch (VetoBranchException e) {
					log.warn("parseBranches: skipping " + currentBranchPath);
					continue;
				}

			}

		}

		return branches;
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
							String name) {

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
								knownBranchMap, GitBranchUtils
										.getCanonicalBranchName(
												data.getBranchPath(),
												currentRevision,
												largeBranchNameProvider),
								currentRevision);

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

	private GitTreeData extractExistingTreeData(ObjectId parentId)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException {

		final GitTreeData treeData = new GitTreeData();

		treeProcessor.visitBlobs(parentId, new GitTreeBlobVisitor() {

			@Override
			public boolean visitBlob(ObjectId blobId, String path, String name) {

				treeData.addBlob(path, blobId.name());

				// visit all of the blobs.
				return true;

			}
		});

		return treeData;

	}

	public void setCommitData(GitCommitData commitData) {
		this.commitData = commitData;

	}

}
