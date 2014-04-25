/**
 * 
 */
package org.kuali.student.git.model.ref.utils;

import java.io.IOException;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
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
		Result updateResult = tagRef.update();
		
		return updateResult;
		
		
	}
}
