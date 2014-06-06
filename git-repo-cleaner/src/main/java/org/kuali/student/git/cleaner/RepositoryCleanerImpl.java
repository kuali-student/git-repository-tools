/*
 *  Copyright 2014 The Kuali Foundation Licensed under the
 *	Educational Community License, Version 2.0 (the "License"); you may
 *	not use this file except in compliance with the License. You may
 *	obtain a copy of the License at
 *
 *	http://www.osedu.org/licenses/ECL-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the License for the specific language governing
 *	permissions and limitations under the License.
 */
package org.kuali.student.git.cleaner;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceiveCommand.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Git Repositories are best kept under 1 GB for a variety of performance
 * reasons.
 * 
 * This tool can be used to split a repository into two parts.
 * 
 * @author ocleirig
 * 
 */
public class RepositoryCleanerImpl implements RepositoryCleaner {

	private static final Logger log = LoggerFactory
			.getLogger(RepositoryCleanerImpl.class);

	/**
	 * 
	 */
	public RepositoryCleanerImpl() {
		// TODO Auto-generated constructor stub
	}

	private static final DateTimeFormatter dateFormat = DateTimeFormat
			.forPattern("YYYY-MM-dd");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.RepositoryCleaner#execute(org.eclipse.jgit
	 * .lib.Repository, java.io.File, long)
	 */
	@Override
	public void execute(Repository repo, String branchRefSpec, Date splitDate)
			throws IOException {

		boolean localBranchSource = true;
		if (!branchRefSpec.equals(Constants.R_HEADS))
			localBranchSource = false;

		String dateString = dateFormat.print(new DateTime(splitDate));

		PrintWriter leftRefsWriter = new PrintWriter("left-refs-" + dateString
				+ ".txt");
		PrintWriter rightRefsWriter = new PrintWriter("right-refs-"
				+ dateString + ".txt");

		PrintWriter pw = new PrintWriter("grafts-" + dateString + ".txt");

		PrintWriter refChangeWriter = new PrintWriter("ref-changes-"
				+ dateString + ".txt");

		PrintWriter objectTranslationWriter = new PrintWriter(
				"object-translations-" + dateString + ".txt");

		ObjectInserter objectInserter = repo.newObjectInserter();

		Map<String, Ref> branchHeads = repo.getRefDatabase().getRefs(
				branchRefSpec);

		Map<ObjectId, Set<Ref>> commitToBranchMap = new HashMap<ObjectId, Set<Ref>>();

		RevWalk walkRight = new RevWalk(repo);
		RevWalk walkLeft = new RevWalk(repo);

		for (Ref branchRef : branchHeads.values()) {

			ObjectId branchObjectId = branchRef.getObjectId();

			Set<Ref> refs = commitToBranchMap.get(branchObjectId);

			if (refs == null) {
				refs = new HashSet<>();
				commitToBranchMap.put(branchObjectId, refs);
			}

			refs.add(branchRef);

			walkLeft.markStart(walkLeft.parseCommit(branchObjectId));
			walkRight.markStart(walkRight.parseCommit(branchObjectId));

		}

		Map<String, Ref> tagHeads = repo.getRefDatabase().getRefs(
				Constants.R_TAGS);

		Map<ObjectId, Set<Ref>> commitToTagMap = new HashMap<ObjectId, Set<Ref>>();

		RevWalk walkRefs = new RevWalk(repo);

		for (Ref tagRef : tagHeads.values()) {

			RevTag tag = walkRefs.parseTag(tagRef.getObjectId());

			ObjectId commitId = tag.getObject().getId();

			Set<Ref> refs = commitToTagMap.get(commitId);

			if (refs == null) {
				refs = new HashSet<>();
				commitToTagMap.put(commitId, refs);
			}

			refs.add(tagRef);

			walkLeft.markStart(walkLeft.parseCommit(commitId));
			walkRight.markStart(walkRight.parseCommit(commitId));
		}

		Set<ObjectId> leftSideCommits = new HashSet<>();

		Set<ObjectId> leftSidePreventGCCommits = new HashSet<>();

		walkLeft.setRevFilter(CommitTimeRevFilter.before(splitDate));

		Iterator<RevCommit> it = walkLeft.iterator();

		while (it.hasNext()) {

			RevCommit commit = it.next();

			ObjectId commitId = commit.getId();

			leftSideCommits.add(commitId);

			Set<Ref> branches = commitToBranchMap.get(commitId);

			if (branches != null) {
				for (Ref ref : branches) {

					leftRefsWriter.println(ref.getName());

				}
			}

			Set<Ref> tags = commitToTagMap.get(commitId);

			if (tags != null) {
				for (Ref ref : tags) {

					leftRefsWriter.println(ref.getName());

				}
			}
		}

		List<ReceiveCommand> deferredReferenceDeletes = new LinkedList<>();
		List<ReceiveCommand> deferredReferenceCreates = new LinkedList<>();

		objectTranslationWriter
				.println("# new-object-id <space> original-object-id");

		int counter = 1;

		// holds the old right side to new right side commit mapping
		Map<ObjectId, ObjectId> rightSideConversionMap = new HashMap<>();

		walkRight.setRevFilter(CommitTimeRevFilter.after(splitDate));

		walkRight.sort(RevSort.TOPO);
		walkRight.sort(RevSort.REVERSE, true);

		it = walkRight.iterator();

		while (it.hasNext()) {

			RevCommit commit = it.next();

			/*
			 * Process the right side.
			 * 
			 * We should be rewriting from old to new.
			 */
			CommitBuilder builder = new CommitBuilder();

			builder.setAuthor(commit.getAuthorIdent());
			builder.setMessage(commit.getFullMessage());

			builder.setCommitter(commit.getCommitterIdent());
			builder.setTreeId(commit.getTree().getId());
			builder.setEncoding("UTF-8");

			Set<ObjectId> newParents = new HashSet<>();

			Set<ObjectId> removedParents = new HashSet<>();

			for (RevCommit parentCommit : commit.getParents()) {

				if (leftSideCommits.contains(parentCommit.getId())) {
					removedParents.add(parentCommit.getId());
				} else {

					ObjectId adjustedParentId = rightSideConversionMap
							.get(parentCommit.getId());

					newParents.add(adjustedParentId);
				}
			}

			builder.setParentIds(new ArrayList<>(newParents));

			ObjectId newCommitId = objectInserter.insert(builder);

			rightSideConversionMap.put(commit.getId(), newCommitId);

			objectTranslationWriter.println(newCommitId.name() + " "
					+ commit.getId().getName());

			if (removedParents.size() > 0) {

				// create the graft.

				List<String> graftData = new ArrayList<>();

				graftData.add(newCommitId.getName());

				for (ObjectId parentId : newParents) {
					graftData.add(parentId.getName());
				}

				for (ObjectId parentId : removedParents) {
					graftData.add(parentId.getName());
				}

				pw.println(StringUtils.join(graftData, " "));

				/*
				 * Make sure there is a branch or tag on the left side ref
				 * 
				 * and if not then put a branch to prevent the graph from being
				 * gc'ed.
				 * 
				 * We use a branch and not a tag because branches are namespaced
				 * but tags are global.
				 */

				for (ObjectId leftCommitId : removedParents) {

					if (!commitToTagMap.containsKey(leftCommitId)
							&& !commitToBranchMap.containsKey(leftCommitId)
							&& !leftSidePreventGCCommits.contains(leftCommitId)) {

						String preventGCBranchName = Constants.R_HEADS
								+ "prevent_gc_" + counter;

						// put a branch
						deferredReferenceCreates
								.add(new ReceiveCommand(null, leftCommitId,
										preventGCBranchName, Type.CREATE));
						counter++;

						leftSidePreventGCCommits.add(leftCommitId);

						leftRefsWriter.println(preventGCBranchName);

					}

				}

			}

			RevWalk commitWalk = new RevWalk(repo);

			RevCommit newCommit = commitWalk.parseCommit(newCommitId);

			// check if any tags need to be moved
			if (commitToTagMap.containsKey(commit.getId())) {

				Set<Ref> tags = commitToTagMap.get(commit.getId());

				Set<TagBuilder> newTagSet = new HashSet<>();

				for (Ref tagRef : tags) {

					RevTag tag = commitWalk.parseTag(tagRef.getObjectId());

					TagBuilder tb = new TagBuilder();

					tb.setMessage(tag.getFullMessage());
					tb.setObjectId(newCommit);
					tb.setTag(tag.getTagName());
					tb.setTagger(tag.getTaggerIdent());

					newTagSet.add(tb);

					deferredReferenceDeletes
							.add(new ReceiveCommand(tagRef.getObjectId(), null,
									tagRef.getName(), Type.DELETE));

					refChangeWriter.println("deleted tagRef "
							+ tagRef.getName() + " original commit id: "
							+ tag.getObject().getId());

					// Result result = GitRefUtils.deleteRef(repo, tagRef,
					// true);
					//
					// if (!result.equals(Result.FORCED))
					// log.warn("failed to delete tag reference " +
					// tagRef.getName());
					// else
					// refChangeWriter.println("deleted tagRef " +
					// tagRef.getName() + " original commit id: " +
					// tag.getObject().getId());

				}

				for (TagBuilder tagBuilder : newTagSet) {

					ObjectId tagId = objectInserter.insert(tagBuilder);

					String tagName = Constants.R_TAGS + tagBuilder.getTag();

					deferredReferenceCreates.add(new ReceiveCommand(null,
							tagId, tagName, Type.CREATE));

					refChangeWriter.println("created tag ref: "
							+ tagBuilder.getTag() + " new commit id: "
							+ tagBuilder.getObjectId());

					rightRefsWriter.println(tagName);

					// Result result = GitRefUtils.createTagReference(repo,
					// tagBuilder.getTag(), tagId);
					//
					// if (!result.equals(Result.NEW))
					// log.warn("unable to create tag " + tagBuilder.getTag() +
					// " now pointed at " + tagId);
					// else
					// refChangeWriter.println("created tag ref: " +
					// tagBuilder.getTag() + " new commit id: " +
					// tagBuilder.getObjectId());
				}

			}

			// check if any branches need to be moved
			if (commitToBranchMap.containsKey(commit.getId())) {

				Set<Ref> refs = commitToBranchMap.get(commit.getId());

				for (Ref branchRef : refs) {

					if (localBranchSource) {

						deferredReferenceDeletes.add(new ReceiveCommand(
								branchRef.getObjectId(), null, branchRef
										.getName(), Type.DELETE));

						refChangeWriter.println("deleted branchRef "
								+ branchRef.getName() + " original commit id: "
								+ branchRef.getObjectId());

					}

					String adjustedBranchName = Constants.R_HEADS
							+ branchRef.getName().substring(
									branchRefSpec.length() + 1);

					deferredReferenceCreates.add(new ReceiveCommand(null,
							newCommitId, adjustedBranchName, Type.CREATE));
					refChangeWriter.println("Updated branchRef: "
							+ branchRef.getName() + " at original commit id: "
							+ branchRef.getObjectId() + " to local branch: "
							+ adjustedBranchName + " at new commit id: "
							+ newCommitId);
					rightRefsWriter.println(adjustedBranchName);

				}

			}

			commitWalk.release();
		}

		objectInserter.flush();

		repo.getRefDatabase().refresh();

		log.info("Applying updates: " + deferredReferenceDeletes.size()
				+ " deletes, " + deferredReferenceCreates.size() + " creates.");

		GitRefUtils.batchRefUpdate(repo, deferredReferenceDeletes,
				NullProgressMonitor.INSTANCE);

		repo.getRefDatabase().refresh();

		GitRefUtils.batchRefUpdate(repo, deferredReferenceCreates,
				NullProgressMonitor.INSTANCE);

		log.info("Completed.");

		walkRefs.release();
		walkLeft.release();
		walkRight.release();

		objectInserter.release();
		repo.close();
		pw.close();

		objectTranslationWriter.close();
		refChangeWriter.close();

		leftRefsWriter.close();
		rightRefsWriter.close();

	}

}
