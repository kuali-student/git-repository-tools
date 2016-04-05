/**
 * 
 */
package org.kuali.student.git.cleaner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.kuali.student.cleaner.model.GitSvnIdUtils;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use JGit directly to rewrite multi module repos
 *
 */
public class SplitMultiModuleJGitRewriter {

	private static final Logger log = LoggerFactory.getLogger(SplitMultiModuleJGitRewriter.class);
	
	private Repository repo;

	private GitTreeProcessor treeProcessor;

	private ObjectInserter objectInserter;

	private Map <ObjectId, ObjectId> originalCommitIdToNewCommitIdMap;

	private String targetPath;
	
	public SplitMultiModuleJGitRewriter (String gitRepositoryPath, boolean bare, String targetPath) throws IOException {
		
		this.targetPath = targetPath;
		File gitRepository = new File(gitRepositoryPath);


		repo = GitRepositoryUtils
				.buildFileRepository(gitRepository, false, bare);
		
		treeProcessor = new GitTreeProcessor(repo);

		objectInserter = repo.newObjectInserter();
		
		originalCommitIdToNewCommitIdMap = new LinkedHashMap<>();
		
	}
	
	public void execute() throws IOException {
		
		Map<String, Ref> branchHeads = repo.getRefDatabase().getRefs(Constants.R_HEADS);

		Ref trunkHead = branchHeads.remove("trunk");
		
		HashMap<ObjectId, Set<Ref>> commitToBranchMap = new HashMap<ObjectId, Set<Ref>>();

		ObjectReader or = repo.newObjectReader();
		
		RevWalk branchWalk = new RevWalk(or);
		
		for (Ref branchRef : branchHeads.values()) {

			ObjectId branchObjectId = branchRef.getObjectId();

			Set<Ref> refs = commitToBranchMap.get(branchObjectId);

			if (refs == null) {
				refs = new HashSet<>();
				commitToBranchMap.put(branchObjectId, refs);
			}

			refs.add(branchRef);

		}
		
		// rewrite the trunk
		ObjectId newTrunkCommitId = processTrunk(or, trunkHead.getObjectId()); 
		
		GitRefUtils.createOrUpdateBranch(repo, Constants.R_HEADS + "trunk", newTrunkCommitId);
		
		for (Ref branchRef : branchHeads.values()) {
			
			ObjectId branchHeadId = branchRef.getObjectId();
			
			CommitBuilder builder = new CommitBuilder();
			
			RevCommit commit = branchWalk.parseCommit(branchHeadId);
			
			builder.setAuthor(commit.getAuthorIdent());
			builder.setCommitter(commit.getCommitterIdent());

			builder.setMessage(commit.getFullMessage());
			builder.setEncoding(commit.getEncoding());
			
			builder.setTreeId(commit.getTree().getId());

			branchWalk.markStart(Arrays.asList(commit.getParents()));
			
			branchWalk.sort(RevSort.TOPO);
			
			Iterator<RevCommit> branchIter = branchWalk.iterator();
			
			ObjectId convertedParentId = null;
			
			while (branchIter.hasNext()) {
				RevCommit parentCommit = (RevCommit) branchIter.next();
				
				convertedParentId = this.originalCommitIdToNewCommitIdMap.get(parentCommit.getId());
				
				if (convertedParentId != null)
					break;
			}
			
			branchWalk.reset();
			
			if (convertedParentId != null)
				builder.setParentId (convertedParentId);
			
			ObjectId newCommitId = objectInserter.insert(builder);
			
			originalCommitIdToNewCommitIdMap.put(commit.getId(), newCommitId);
			
			GitRefUtils.createOrUpdateBranch(repo, branchRef.getName(), newCommitId);
			
		}
		
		
		or.close();
	}
	

	private ObjectId processTrunk(ObjectReader or, AnyObjectId trunkHeadId) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		RevWalk walkRepo = new RevWalk(or);
		
		walkRepo.markStart(walkRepo.parseCommit(trunkHeadId));
		
		// sort parents before children
		walkRepo.sort(RevSort.TOPO, true);
		walkRepo.sort(RevSort.REVERSE, true);
		
		// only return commits containing the target path that has a non-zero diff to its parent
		// JGit will automatically simplify history so that the commits returned skip over commits that don't match this criteria.
		walkRepo.setTreeFilter(AndTreeFilter.create(PathFilter.create(targetPath), TreeFilter.ANY_DIFF));
		
		Iterator<RevCommit> iter = walkRepo.iterator();
		
		ObjectId lastCommitId = null;
		
		while (iter.hasNext()) {
			RevCommit commit = (RevCommit) iter.next();
			
			GitTreeData tree = treeProcessor
					.extractExistingTreeDataFromCommit(commit.getId());

			
			/*
			 * Process in reverse order from old to new.
			 */
			CommitBuilder builder = new CommitBuilder();
			
			builder.setAuthor(commit.getAuthorIdent());
			builder.setCommitter(commit.getCommitterIdent());

			builder.setMessage(GitSvnIdUtils
					.applyPathToExistingGitSvnId(commit.getFullMessage(), targetPath));
			
			builder.setEncoding(commit.getEncoding());
			
			
			ObjectId targetTreeId = tree.find(repo, targetPath);
			
			builder.setTreeId(targetTreeId);
			
			builder.setParentIds(convertParents(commit));
			
			lastCommitId = objectInserter.insert(builder);
			
			originalCommitIdToNewCommitIdMap.put(commit.getId(), lastCommitId);
			
		}
		
		walkRepo.close();
		
		return lastCommitId;
		
		
		
		
		
	}

	

	private List<ObjectId> convertParents(RevCommit commit) {
		
		Set<ObjectId> convertedParentIds = new LinkedHashSet<>();
		
		for (RevCommit parentCommit : commit.getParents()) {
			
			ObjectId convertedParentId = this.originalCommitIdToNewCommitIdMap.get(parentCommit.getId());
			
			if (convertedParentId == null) {
				convertedParentIds.add(parentCommit.getId());
			}
			else {
				convertedParentIds.add(convertedParentId);
			}
			
		}
		
		return new ArrayList<>(convertedParentIds);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 3) {
			log.error("USAGE: <git repository> <bare:true or false> <target path>");
			System.exit(1);
		}
		
		String gitRepositoryPath = args[0];
		
		boolean bare = Boolean.valueOf(args[1].trim());
		
		String targetPath = args[2];
		
		try {
			SplitMultiModuleJGitRewriter splitter = new SplitMultiModuleJGitRewriter (gitRepositoryPath, bare, targetPath);
			
			splitter.execute();
			
		} catch (Exception e) {
			log.error("unexpected fatal exception = ", e);
		}

	}

}
