/**
 * 
 */
package org.kuali.student.git.model.ref.utils;

import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.git.model.ref.exception.BranchRefExistsException;

/**
 * @author ocleirig
 * 
 * Utilities methods related to Git branches and tags.
 *
 */
public final class GitRefUtils {

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
}
