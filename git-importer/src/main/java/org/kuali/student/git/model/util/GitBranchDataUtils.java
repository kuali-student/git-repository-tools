/**
 * 
 */
package org.kuali.student.git.model.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.GitBranchData;
import org.kuali.student.git.model.ExternalModuleUtils;
import org.kuali.student.git.model.SvnRevisionMapper;
import org.kuali.student.git.model.branch.large.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.svn.model.ExternalModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 * 
 */
public final class GitBranchDataUtils {

	private static Logger log = LoggerFactory
			.getLogger(GitBranchDataUtils.class);

	/**
	 * 
	 */
	private GitBranchDataUtils() {
	}

	public static void extractExternalModules(Repository repo,
			GitTreeData root, GitBranchData targetBranchData,
			final GitTreeProcessor treeProcessor)
			throws MissingObjectException, IOException {

		final List<ExternalModuleInfo> existingExternals = new ArrayList<>(5);

		// load up any existing svn:externals data
		ObjectId blobId = root.find(repo, "fusion-maven-plugin.dat");

		if (blobId != null) {
			List<String> existingData = treeProcessor
					.getBlobAsStringLines(blobId);

			existingExternals.addAll(ExternalModuleUtils
					.extractFusionMavenPluginData(existingData));
		}

		targetBranchData.setExternals(existingExternals);
	}

	public static void extractAndStoreBranchMerges(long sourceRevision,
			String sourceBranchName, GitBranchData targetBranchData,
			SvnRevisionMapper revisionMapper) throws IOException {
		// load up any existing svn:mergeinfo data
		List<BranchMergeInfo> existingMergeInfo = revisionMapper
				.getMergeBranches(sourceRevision, sourceBranchName);

		if (existingMergeInfo != null && existingMergeInfo.size() > 0)
			targetBranchData.accumulateMergeInfo(existingMergeInfo);

	}

}
