/*
 * Copyright 2013 The Kuali Foundation
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
package org.kuali.student.git.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.input.BoundedInputStream;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.TemporaryBuffer.LocalFile;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.student.git.model.GitBranchData;
import org.kuali.student.git.model.GitCommitData;
import org.kuali.student.git.model.GitTreeData;
import org.kuali.student.git.model.GitTreeData.GitMergeData;
import org.kuali.student.git.model.SvnRevisionMapper;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.svn.tools.AbstractParseOptions;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.kuali.student.svn.tools.merge.tools.BranchUtils;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kuali Student Team
 * 
 *         We want to parse an svn dump file and then import the changes into a
 *         corresponding git repository.
 */
public class GitImporterMain {

	private static final Logger log = LoggerFactory
			.getLogger(GitImporterMain.class);

	private static DateTimeFormatter SvnDumpDateFormatter = DateTimeFormat
			.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			log.error("USAGE: <svn dump file> <git repository>");
			System.exit(-1);
		}

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"git/GitImporterMain-applicationContext.xml");

			applicationContext.registerShutdownHook();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);

			// final MergeDetectorData detectorData = applicationContext
			// .getBean(MergeDetectorData.class);

			File dumpFile = new File(args[0]);

			if (!dumpFile.exists()) {
				throw new FileNotFoundException(args[0] + " path not found");
			}

			File gitRepository = new File(args[1]);

			if (!gitRepository.getParentFile().exists())
				throw new FileNotFoundException(args[1] + "path not found");

			final Repository repo = GitRepositoryUtils.buildFileRepository(
					gitRepository, false);

			final Map<String, GitBranchData> knownBranchMap = new LinkedHashMap<String, GitBranchData>();

			// extract any known branches from the repository

			filter.parseDumpFile(dumpFile.getAbsolutePath(),
					new AbstractParseOptions() {

						private GitCommitData commitData = null;

						private long currentRevision = 0;

						private SvnRevisionMapper revisionMapper = new SvnRevisionMapper(
								repo);

						/*
						 * (non-Javadoc)
						 * 
						 * @see
						 * org.kuali.student.svn.tools.AbstractParseOptions#
						 * onStreamEnd
						 * (org.kuali.student.svn.tools.model.ReadLineData)
						 */
						@Override
						public void onStreamEnd(ReadLineData lineData) {
							// at the end of the stream flush and branch commits
							// that are pending.

							flushPendingBranchCommits();

						}

						/*
						 * The revision comes before the file content so only
						 * the second call per file and onward is relevant.
						 */
						@Override
						public void onRevisionContentLength(
								long currentRevision, long contentLength,
								long propContentLength, ReadLineData lineData) {

							// for each branch that has commit data update to

							flushPendingBranchCommits();

							this.currentRevision = currentRevision;

							log.info("starting on Revision: " + currentRevision);

							// at this point we should be able to read
							// propContentLength and parse out the

							try {
								Map<String, String> revisionProperties = org.kuali.student.common.io.IOUtils
										.extractRevisionProperties(inputStream,
												propContentLength,
												contentLength);

								// read out the author and commit message
								String author = revisionProperties
										.get("svn:author");

								String commitDate = revisionProperties
										.get("svn:date");

								String commitMessage = revisionProperties
										.get("svn:log");

								// read out the svn:mergeinfo to make sure we
								// are making a merge commit.

								commitData = new GitCommitData(commitDate,
										author, commitMessage);

								// also consider copyfrom or other details that
								// suggest a merge at this point.

							} catch (IOException e) {
								throw new RuntimeException(
										"onRevisionContentLength failed to read revision properties.",
										e);
							}

						}

						private void flushPendingBranchCommits() {

							try {
								for (Map.Entry<String, GitBranchData> entry : knownBranchMap
										.entrySet()) {
									String branchName = entry.getKey();
									GitBranchData data = entry.getValue();

									// only flush if the branch has data to
									// commit for the current revision
									log.debug("branch = " + branchName
											+ " has data to commit");

									Ref ref = repo.getRef(Constants.R_HEADS
											+ branchName);

									ObjectId parentId = null;

									if (ref != null) {
										// load some existing data like the
										// current parent sha1.
										parentId = ref.getObjectId();

										GitTreeData existingTreeData = extractExistingTreeData(parentId);

										data.mergeOntoExistingTreeData(existingTreeData);

									}

									List<ObjectId> parentList = new ArrayList<ObjectId>();

									for (String sourceMergeBranch : data
											.getMergeSourceBranches()) {

										GitMergeData mergeData = data
												.getMergeData(sourceMergeBranch);

										// find the commit id for each of these.

										long copyFromRevision = Long
												.valueOf(mergeData
														.getCopyFromRevision());

										BranchData copyFromBranchData;
										try {
											copyFromBranchData = GitBranchUtils
													.parse(mergeData
																	.getCopyFromPath());
										} catch (VetoBranchException e) {
											log.warn(mergeData.getCopyFromPath() + " vetoed");
											// skip to the next merge source
											continue;
											
										}

										ObjectId copyFromBranchHeadId = revisionMapper
												.getRevisionBranchHead(
														copyFromRevision,
														copyFromBranchData
																.getBranchPath());

										if (copyFromBranchHeadId != null) {
											if (mergeData
													.isDirectoryAddByCopy()) {

												GitTreeData existingTreeData = extractExistingTreeData(copyFromBranchHeadId);

												data.mergeOntoExistingTreeData(existingTreeData);

											}

											parentList
													.add(copyFromBranchHeadId);
										}
										else {
											// copyFromBranchHeadId is null
											log.info("");
										}
									}

									// create the commit
									CommitBuilder commitBuilder = new CommitBuilder();

									ObjectInserter inserter;

									// create the tree
									ObjectId treeId = data
											.buildTree(inserter = repo
													.newObjectInserter());

									log.debug("tree id = " + treeId.name());

									commitBuilder.setTreeId(treeId);

									if (parentList.size() > 0) {

										if (parentId != null) {
											parentList.add(parentId);
										}

										log.info("committing a merge");

										
									}
									else {
										if (parentId != null) {
											parentList.add(parentId);
										}
									}
									
									commitBuilder.setParentIds(parentList);

									String userName = commitData.getUserName();

									if (userName == null)
										userName = "unknown";

									String emailAddress = userName
											+ "@kuali.org";

									SimpleDateFormat sft = new SimpleDateFormat(
											"");

									Date commitDate = convertDate(commitData
											.getDate());

									// TODO parse out the time zone correctly.

									PersonIdent author = new PersonIdent(
											userName, emailAddress, commitDate,
											TimeZone.getTimeZone("EDT"));

									commitBuilder.setAuthor(author);

									commitBuilder.setCommitter(author);
									
									commitBuilder.setMessage(commitData.getCommitMessage());

									ObjectId commitId = inserter
											.insert(commitBuilder);

									inserter.flush();

									inserter.release();

									// post commit update the branch reference.

									// create the branch in git

									String fullBranchNameReference = Constants.R_HEADS
											+ branchName;

									RefUpdate update = repo
											.updateRef(fullBranchNameReference);

									update.setNewObjectId(commitId);
									update.setRefLogMessage(
											"created new branch "
													+ fullBranchNameReference,
											true);

									Result result = update.forceUpdate();

									if (result == null
											|| !(result.equals(Result.NEW)
													|| result
															.equals(Result.FORCED) || result
														.equals(Result.FAST_FORWARD))) {
										throw new RuntimeException(
												"failed to create new branch: "
														+ fullBranchNameReference);
									}
									ref = repo.getRef(fullBranchNameReference);

									ObjectId refObjectId = ref.getObjectId();

									log.info(String.format("updated %s to %s",
											fullBranchNameReference,
											commitId.name()));

									if (!commitId.equals(refObjectId)) {
										log.warn("failed to update ref for "
												+ branchName);
									}

								}

								List<Ref> refs = new ArrayList<Ref>(repo
										.getAllRefs().values());

								revisionMapper.createRevisionMap(
										currentRevision, refs);

								knownBranchMap.clear();

							} catch (IOException e) {
								throw new RuntimeException(
										"flushPendingBranchCommits failed on rev = "
												+ currentRevision, e);
							}
						}

						private GitTreeData extractExistingTreeData(
								ObjectId parentId)
								throws MissingObjectException,
								IncorrectObjectTypeException,
								CorruptObjectException, IOException {

							// a RevWalk allows to walk over commits based on
							// some filtering that is defined
							RevWalk walk = new RevWalk(repo);

							RevCommit commit = walk.parseCommit(parentId);

							// a commit points to a tree
							return extractTreeData(commit.getTree().getId());
						}

						/*
						 * Transform a Git Tree into our GitTreeData structure.
						 */
						private GitTreeData extractTreeData(ObjectId treeId)
								throws MissingObjectException,
								IncorrectObjectTypeException,
								CorruptObjectException, IOException {

							GitTreeData treeData = new GitTreeData();

							TreeWalk treeWalk = new TreeWalk(repo);

							treeWalk.addTree(treeId);

							treeWalk.setRecursive(true);

							if (treeWalk.getTreeCount() == 0) {
								log.warn("no trees to parse");

							}

							while (treeWalk.next()) {

								final FileMode mode = treeWalk.getFileMode(0);

								if (mode != FileMode.REGULAR_FILE)
									continue;

								/*
								 * We only want the blob's
								 */
								ObjectId blobId = treeWalk.getObjectId(0);

								final String path = treeWalk.getPathString();

								treeData.addBlob(path, blobId.name());
							}

							return treeData;
						}

						private Date convertDate(String date) {

							DateTime dt = SvnDumpDateFormatter
									.parseDateTime(date);

							LocalDateTime ldt = new LocalDateTime(dt);

							Date d = ldt.toDate();

							return d;
						}

						
						/* (non-Javadoc)
						 * @see org.kuali.student.svn.tools.AbstractParseOptions#onAfterNode(long, java.lang.String, java.util.Map, org.kuali.student.svn.tools.model.INodeFilter)
						 */
						@Override
						public void onAfterNode(long currentRevision,
								String path,
								Map<String, String> nodeProperties,
								INodeFilter nodeFilter) {
							
							/*
							 * This catches cases that don't have file content.
							 * 
							 * This can be directory adds, file copies, directory copies, etc.
							 */
							
							String kind = nodeProperties.get("Node-kind");

							String action = nodeProperties.get("Node-action");

							boolean validBranch = true;
							
							BranchData branchData = null;
							try {
								branchData = GitBranchUtils.parse(path);
							} catch (VetoBranchException e) {
								validBranch = false;
							}

							if ("add".equals(action)) {

								if ("file".equals(kind)) {

									GitBranchData data = getBranchData(
											knownBranchMap, branchData.getBranchPath());

									detectMerge(data, nodeProperties);

								} else if ("dir".equals(kind)) {
									/*
									 * We care if the directory was copied from
									 * somewhere else
									 * 
									 * It would be useful if we had the subtree
									 * at this point in time.
									 */

									if (nodeProperties
											.containsKey("Node-copyfrom-path")) {
										// if this is a move then we need to
										// populate the data with the existing
										
										if (validBranch) {
										GitBranchData data = getBranchData(
												knownBranchMap, branchData.getBranchPath());

											detectMerge(data, nodeProperties, true);
										
										}
										else {
											
											String copyFromPath = nodeProperties
													.get("Node-copyfrom-path");
											
											long copyFromRevision = Long.valueOf(nodeProperties
													.get("Node-copyfrom-rev"));
											
											try {
												// need to find the branch in the copy from revision that holds the copyFromPath prefix
												
												List<SvnRevisionMap> heads = revisionMapper.getRevisionHeads(copyFromRevision);
												
												for (SvnRevisionMap revMap : heads) {
													
													String currentBranchName = revMap.getBranchName();
													
													if (!currentBranchName.startsWith(Constants.R_HEADS + copyFromPath))
														continue;
													
													String newBranchName = currentBranchName.replace(Constants.R_HEADS, "").replace(copyFromPath, path);
													
													GitBranchData newBranchData = getBranchData(
															knownBranchMap, newBranchName);
													
													String commitId = revMap.getCommitId();
													
													RevWalk walk = new RevWalk(repo);

													RevCommit commit = walk.parseCommit(ObjectId.fromString(commitId));

													// a commit points to a tree
													ObjectId treeId = commit.getTree().getId();
													
													TreeWalk treeWalk = new TreeWalk(repo);

													treeWalk.addTree(treeId);

													treeWalk.setRecursive(true);

													if (treeWalk.getTreeCount() == 0) {
														log.warn("no trees to parse");
													}

													while (treeWalk.next()) {

														final FileMode mode = treeWalk.getFileMode(0);

														if (mode != FileMode.REGULAR_FILE)
															continue;

														/*
														 * We only want the blob's
														 */
														ObjectId blobId = treeWalk.getObjectId(0);

														/*
														 * Store the blob in the new branch location.
														 */
														String blobPath = treeWalk.getPathString();
														
														newBranchData.addBlob(newBranchName + "/" + blobPath, blobId.getName());
														
													}
													
													treeWalk.release();
													walk.release();
												}
												
											} catch (IOException e) {
												throw new RuntimeException("failed to parse directory invalid branch from " + copyFromPath , e);
											}
											
											
										}
									}

								} else {
									// skip this case
								}

							} else if ("change".equals(action)) {

								if ("file".equals(kind)) {

									GitBranchData data = getBranchData(
											knownBranchMap, branchData.getBranchPath());

									detectMerge(data, nodeProperties);

								} else if ("dir".equals(kind)) {
									// we might care about this for merge
									// detection
									log.info("change directory");
								} else {
									// skip this case
								}

							} else if ("delete".equals(action)) {

								if ("file".equals(kind)) {
									GitBranchData data = getBranchData(
											knownBranchMap, branchData.getBranchPath());
									deleteBlob(data, currentRevision, path);
								} else if ("dir".equals(kind)) {
									GitBranchData data = getBranchData(
											knownBranchMap, branchData.getBranchPath());
									deletePath(data, currentRevision, path);
								} else {
									// skip this case
								}
							}

						}

						/*
						 * (non-Javadoc)
						 * 
						 * @see
						 * org.kuali.student.svn.tools.AbstractParseOptions#
						 * onNodeContentLength(long, java.lang.String, long,
						 * long, java.util.Map,
						 * org.kuali.student.svn.tools.model.INodeFilter)
						 */
						@Override
						public void onNodeContentLength(long currentRevision,
								String path, long contentLength,
								long propContentLength,
								Map<String, String> nodeProperties,
								INodeFilter nodeFilter) {

							String kind = nodeProperties.get("Node-kind");

							String action = nodeProperties.get("Node-action");

							BranchData branchData;
							try {
								branchData = GitBranchUtils.parse(path);
							} catch (VetoBranchException e) {
								log.warn("onNodeContentLength: vetoed " + path);
								
								try {
									inputStream.skip(contentLength + 1);
								} catch (IOException e1) {

									throw new RuntimeException(
											String.format(
													"Failed to skip over content after node(%d:%s)"
															+ currentRevision,
													path), e1);

								}
								return;
							}

							String branchName = branchData.getBranchPath();

							if ("add".equals(action)) {

								if ("file".equals(kind)) {

									GitBranchData data = getBranchData(
											knownBranchMap, branchName);

									detectMerge(data, nodeProperties);

									storeBlob(data, currentRevision, path,
											contentLength, propContentLength);
								} else if ("dir".equals(kind)) {
									/*
									 * We care if the directory was copied from
									 * somewhere else
									 * 
									 * It would be useful if we had the subtree
									 * at this point in time.
									 */

									if (nodeProperties
											.containsKey("Node-copyfrom-path")) {
										// if this is a move then we need to
										// populate the data with the existing
										GitBranchData data = getBranchData(
												knownBranchMap, branchName);

										detectMerge(data, nodeProperties, true);
									}

								} else {
									// skip this case
								}

							} else if ("change".equals(action)) {

								if ("file".equals(kind)) {

									GitBranchData data = getBranchData(
											knownBranchMap, branchName);

									detectMerge(data, nodeProperties);

									storeBlob(data, currentRevision, path,
											contentLength, propContentLength);
								} else if ("dir".equals(kind)) {
									// we might care about this for merge
									// detection
									log.info("change directory");
								} else {
									// skip this case
								}

							} else if ("delete".equals(action)) {

								if ("file".equals(kind)) {
									GitBranchData data = getBranchData(
											knownBranchMap, branchName);
									deleteBlob(data, currentRevision, path);
								} else if ("dir".equals(kind)) {
									GitBranchData data = getBranchData(
											knownBranchMap, branchName);
									deletePath(data, currentRevision, path);
								} else {
									// skip this case
								}
							}

						}

						private GitBranchData getBranchData(
								Map<String, GitBranchData> knownBranchMap,
								String branchName) {

							GitBranchData data = knownBranchMap.get(branchName);

							if (data == null) {
								data = new GitBranchData(branchName);
								knownBranchMap.put(branchName, data);
							}

							return data;
						}

						private void detectMerge(GitBranchData data,
								Map<String, String> nodeProperties) {
							detectMerge(data, nodeProperties, false);
						}

						private void detectMerge(GitBranchData data,
								Map<String, String> nodeProperties,
								boolean directoryAddByCopy) {

							String copyFromPath = nodeProperties
									.get("Node-copyfrom-path");

							if (copyFromPath == null)
								return;

							BranchData bd;
							try {
								bd = GitBranchUtils.parse(copyFromPath);
							} catch (VetoBranchException e1) {
								log.warn(copyFromPath + " vetoed");
								return;
							}

							if (!bd.getBranchPath()
									.equals(data.getBranchPath())) {

								String sourceBlobId = nodeProperties
										.get("Text-copy-source-sha1");

								String copyFromRevision = nodeProperties
										.get("Node-copyfrom-rev");

								data.registerMerge(bd.getBranchPath(),
										copyFromRevision, copyFromPath,
										sourceBlobId, directoryAddByCopy);

								/*
								 * Find the most recent commit containing this
								 * blob id at this path.
								 */

								RevWalk rw = new RevWalk(repo);

								rw.setRetainBody(true);

								rw.setTreeFilter(PathFilter
										.create(copyFromPath));

								RevCommit rc = null;

								try {
									while ((rc = rw.next()) != null) {

										String commitId = rc.getId().name();
										String fullMessage = rc
												.getFullMessage();

										log.info("commit = " + commitId);
										log.info("full Message = "
												+ fullMessage);
									}
								} catch (Exception e) {
									throw new RuntimeException("unexpected", e);
								}
							}
						}

						private void deletePath(GitBranchData data,
								long currentRevision, String path) {

							data.deletePath(path);
						}

						private void deleteBlob(GitBranchData data,
								long currentRevision, String path) {
							data.deletePath(path);
						}

						private void storeBlob(GitBranchData gbd,
								long currentRevision, String path,
								long contentLength, long propContentLength) {
							log.debug("branch = " + gbd.getBranchPath()
									+ ", path = " + path + ", at revision "
									+ currentRevision);

							try {

								long adjustedContentLength = contentLength
										- propContentLength + 1;

								inputStream.skip(propContentLength + 1);

								/*
								 * We want to replace the contents of the path
								 * file with the content that we read from the
								 * input stream.
								 */

								ObjectInserter objectInserter = repo
										.getObjectDatabase().newInserter();

								LocalFile streamData = new LocalFile();

								streamData.copy(new BoundedInputStream(
										inputStream, adjustedContentLength));

								InputStream cachedInputStream = streamData
										.openInputStream();

								ObjectId id = objectInserter.idFor(
										Constants.OBJ_BLOB,
										adjustedContentLength,
										cachedInputStream);

								cachedInputStream.close();

								if (repo.getObjectDatabase().has(id)) {
									log.warn(path
											+ " : exists in git with sha1 of "
											+ id);
								} else {
									// need to insert the blob.

									cachedInputStream = streamData
											.openInputStream();

									objectInserter.insert(Constants.OBJ_BLOB,
											adjustedContentLength,
											cachedInputStream);

									cachedInputStream.close();
								}

								streamData.close();

								gbd.addBlob(path, id.getName());

							} catch (Exception e) {

								log.error("onNodeContentChanged failed: ", e);
								throw new RuntimeException(
										"onNodeContentLength failed", e);
							}
						}

					});

		} catch (Exception e) {
			log.error("Processing failed", e);
		}

	}

}
