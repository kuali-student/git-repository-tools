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
package org.kuali.student.git.model.ref.utils;

import java.io.IOException;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefRename;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.kuali.student.git.model.branch.large.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.branch.utils.GitBranchUtils;
import org.kuali.student.git.model.branch.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.kuali.student.git.model.ref.exception.BranchRefExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 * 
 * Utilities methods related to Git branches and tags.
 *
 */
public final class GitRefUtils {

	private static final Logger log = LoggerFactory.getLogger(GitRefUtils.class);
	/**
	 * 
	 */
	private GitRefUtils() {
	}

	/**
	 * Create a new branch with the name and commit given.
	 * 
	 * @param repo
	 * @param absoluteBranchName
	 * @param commitId
	 * @return
	 * @throws IOException
	 */
	public static Ref createBranch (Repository repo, String absoluteBranchName, ObjectId commitId) throws BranchRefExistsException, IOException {
		
		return createBranch(repo, absoluteBranchName, commitId, false);
	}
	
	
	public static Ref createOrUpdateBranch (Repository repo, String absoluteBranchName, ObjectId commitId) throws IOException {
		
		try {
			return createBranch(repo, absoluteBranchName, commitId, true);
		} catch (BranchRefExistsException e) {
			// should never happen
			throw new IOException("createOrUpdateBranch failed.", e);
		}
	}

	public static Ref createBranch(Repository repo,
			String absoluteBranchName, ObjectId commitId, boolean allowUpdate) throws BranchRefExistsException, IOException {
		
		if (!allowUpdate && repo.getRef(absoluteBranchName) != null)
			throw new BranchRefExistsException(absoluteBranchName + "already exists"); 
		
		RefUpdate update = repo
				.updateRef(absoluteBranchName);

		update.setNewObjectId(commitId);
		update.setRefLogMessage(
				"created new branch "
						+ absoluteBranchName,
				true);
		
		
		Result result = update.forceUpdate();
		
		if (result.equals(Result.LOCK_FAILURE)) {
			log.warn("lockfailure updating " + absoluteBranchName + " to commitId = " + commitId);
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				//fall through
			}
			return createBranch(repo, absoluteBranchName, commitId, allowUpdate);
		}

		if (result == null
				|| !(result.equals(Result.NEW)
						|| result
								.equals(Result.FORCED) || result
							.equals(Result.FAST_FORWARD))) {
			throw new RuntimeException(
					"failed to create new branch: "
							+ absoluteBranchName);
		}
		
		Ref ref = repo.getRef(absoluteBranchName);
		
		return ref;
	}
	
	public static ObjectId insertTag(String tagName, RevCommit commit,
			ObjectInserter objectInserter) throws IOException {

		PersonIdent committer = commit.getCommitterIdent();
		
		TagBuilder tagBuilder = new TagBuilder();
		
		tagBuilder.setMessage(commit.getFullMessage());
		tagBuilder.setObjectId(commit);
		
		tagBuilder.setTagger(committer);
		tagBuilder.setTag(tagName);
		
		ObjectId tagId = objectInserter.insert(tagBuilder);
	
		return tagId;
	}

	public static Result createTagReference(Repository repo, String simpleTagName,
			ObjectId tagId) throws IOException {
		
		String refName = Constants.R_TAGS + simpleTagName;
		RefUpdate tagRef = repo.updateRef(refName);
		tagRef.setNewObjectId(tagId);
		tagRef.setForceUpdate(true);
		tagRef.setRefLogMessage("tagged " + simpleTagName, false); 
		Result updateResult = tagRef.forceUpdate();
		
		return updateResult;
		
		
	}

	public static Ref archiveBranch(Repository repo, ILargeBranchNameProvider largeBranchNameProvider, PersonIdent refLogIdent, String currentBranchName, long currentRevision) throws IOException { 
		
		String archivedBranchName = Constants.R_HEADS + currentBranchName + "@"
				+ (currentRevision - 1);
		
		Ref existingBranchRef = repo.getRef(Constants.R_HEADS + currentBranchName);

		if (existingBranchRef == null) {
			log.warn("trying to rename branch: " + currentBranchName + " to " + archivedBranchName + " but it doesn't exist.");
			return null;
		}
		

		if (archivedBranchName.length() >= GitBranchUtils.FILE_SYSTEM_NAME_LIMIT) {
			archivedBranchName = Constants.R_HEADS
					+ largeBranchNameProvider.storeLargeBranchName(
							archivedBranchName, currentRevision);
		}

		return renameRef(repo, refLogIdent, existingBranchRef.getName(), archivedBranchName);
	}
	
	public static Ref renameRef (Repository repo,  PersonIdent refLogIdent, String fromRefName, String toRefName) throws IOException {
		
		RefRename rename = repo.renameRef(fromRefName, toRefName
				);

		rename.setRefLogIdent(refLogIdent);
		rename.setRefLogMessage(refLogIdent + " archived "
				+ fromRefName + " to " + toRefName);

		Result result = rename.rename();
		
		if (result.equals(Result.LOCK_FAILURE)) {
			log.warn("lockfailure archiving " + fromRefName + " to branch = " + toRefName);
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				//fall through
			}
			
			return renameRef(repo, refLogIdent, fromRefName, toRefName);
		}
		else {
			if (result.equals(Result.RENAMED))
				return repo.getRef(toRefName);
			else
				return null;
		}
	}

public static Result deleteRef(Repository repo, Ref ref, boolean force) throws IOException {
		
		RefUpdate refUpdate = repo.getRefDatabase().newUpdate(ref.getName(), false);
		
		refUpdate.setForceUpdate(force);
		
		return refUpdate.delete();
	}
	public static Result deleteRef(Repository repo, Ref ref) throws IOException {
		
		return deleteRef(repo, ref, false);
	}
}
