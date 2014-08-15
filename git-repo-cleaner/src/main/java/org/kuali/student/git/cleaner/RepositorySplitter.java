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

import java.io.File;
import java.io.FileNotFoundException;
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
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
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
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.utils.ExternalGitUtils;
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
public class RepositorySplitter extends AbstractRepositoryCleaner {

	private static final Logger log = LoggerFactory
			.getLogger(RepositorySplitter.class);

	private Date splitDate;

	private PrintWriter leftRefsWriter;

	private PrintWriter rightRefsWriter;

	private PrintWriter pw;

	private PrintWriter refChangeWriter;

	private RevWalk walkRight;

	private RevWalk walkLeft;

	private Set<ObjectId> leftSideCommits;

	private Set<ObjectId> leftSidePreventGCCommits;

	private Set<ObjectId> removedParents;

	private Set<ObjectId> newParents;

	private int counter = 1;

	/**
	 * 
	 */
	public RepositorySplitter() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.RepositoryCleaner#validateArgs(java.lang
	 * .String[])
	 */
	@Override
	public void validateArgs(List<String> args) throws Exception {

		if (args.size() != 3 && args.size() != 4 && args.size() != 5) {
			log.error("USAGE: <source git repository meta directory> <grafts file> <split date> [<branchRefSpec> <git command path>]");
			log.error("\t<git repo meta directory> : the path to the meta directory of the source git repository");
			log.error("\t<grafts file> : An existing grafts file if this is a subsequent split");
			log.error("\t<split date> : YYYY-MM-DD [MM:hh]");
			log.error("\t<branchRefSpec> : git refspec from which to source the graph to be rewritten");
			log.error("\t<git command path> : the path to a native git ");
			throw new IllegalArgumentException("invalid arguments["
					+ StringUtils.join(args, ", ") + "]");
		}

		setRepo(GitRepositoryUtils.buildFileRepository(
				new File(args.get(0)).getAbsoluteFile(), false));

		String graftsFile = args.get(1);

		if (new File(graftsFile).exists())
			super.loadGrafts(graftsFile);

		splitDate = null;

		if (args.get(2).contains(":")) {
			splitDate = includeHourAndMinuteDateFormatter.parseDateTime(
					args.get(2)).toDate();
		} else {
			splitDate = formatter.parseDateTime(args.get(2)).toDate();
		}

		if (args.size() == 4)
			setBranchRefSpec(args.get(3).trim());

		if (args.size() == 5)
			setExternalGitCommandPath(args.get(4).trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.AbstractRepositoryCleaner#processCommitTree
	 * (org.eclipse.jgit.lib.ObjectId,
	 * org.kuali.student.git.model.tree.GitTreeData)
	 */
	@Override
	protected boolean processCommitTree(RevCommit commit, GitTreeData tree)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.AbstractRepositoryCleaner#getFileNameSuffix
	 * ()
	 */
	@Override
	protected String getFileNameSuffix() {
		return "repo-splitter";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.AbstractRepositoryCleaner#onBeforeExecute()
	 */
	@Override
	protected void onBeforeExecute() throws FileNotFoundException {
		super.onBeforeExecute();

		leftRefsWriter = new PrintWriter("left-refs-" + dateString + ".txt");
		rightRefsWriter = new PrintWriter("right-refs-" + dateString + ".txt");

		pw = new PrintWriter("grafts-" + dateString + ".txt");

		refChangeWriter = new PrintWriter("ref-changes-" + dateString + ".txt");

		walkRight = new RevWalk(getRepo());
		walkLeft = new RevWalk(getRepo());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.AbstractRepositoryCleaner#onTag(org.eclipse
	 * .jgit.lib.ObjectId, org.eclipse.jgit.lib.ObjectId)
	 */
	@Override
	protected void onTag(ObjectId id, ObjectId commitId)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {
		walkLeft.markStart(walkLeft.parseCommit(commitId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.AbstractRepositoryCleaner#onBranchHead(
	 * org.eclipse.jgit.lib.Ref, org.eclipse.jgit.lib.ObjectId)
	 */
	@Override
	protected void onBranchHead(Ref branchRef, ObjectId commitId)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {
		walkLeft.markStart(walkLeft.parseCommit(commitId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.AbstractRepositoryCleaner#onBeforeRevWalk()
	 */
	@Override
	protected void onBeforeRevWalk() {

		leftSideCommits = new HashSet<>();

		leftSidePreventGCCommits = new HashSet<>();

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
	}

	
	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#onNewCommit(org.eclipse.jgit.revwalk.RevCommit, org.eclipse.jgit.lib.ObjectId)
	 */
	@Override
	protected void onNewCommit(RevCommit commit, ObjectId newCommitId) {
		// intentionally not calling the constructor
		
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
					deferCreate(preventGCBranchName, leftCommitId);
					
					counter++;

					leftSidePreventGCCommits.add(leftCommitId);

					leftRefsWriter.println(preventGCBranchName);

				}

			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.AbstractRepositoryCleaner#onCommitParent
	 * (org.eclipse.jgit.revwalk.RevCommit, org.eclipse.jgit.revwalk.RevCommit)
	 */
	@Override
	protected Set<ObjectId> processParents(RevCommit commit) {

		newParents = new HashSet<>();

		removedParents = new HashSet<>();

		for (RevCommit parentCommit : commit.getParents()) {

			if (leftSideCommits.contains(parentCommit.getId())) {
				removedParents.add(parentCommit.getId());
			} else {

				ObjectId adjustedParentId = super.originalCommitIdToNewCommitIdMap
						.get(parentCommit.getId());

				newParents.add(adjustedParentId);
			}
		}
		
		
		return newParents;
	}

	
	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#onTagCreate(java.lang.String, org.eclipse.jgit.lib.ObjectId)
	 */
	@Override
	protected void onTagRefCreate(String tagName, ObjectId tagId) {
		rightRefsWriter.println(tagName);
	}

}
