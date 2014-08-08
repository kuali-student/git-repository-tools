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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceiveCommand.Type;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.joda.time.DateTime;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.utils.ExternalGitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ingest a series of file paths and or globbing patterns like *.mpx and then for each git commit under consideration
 * rewrite the commit by removing the files that match the pattern's given.
 * 
 *  Use the same rewrite technique as the RepositoryBlobRewriter where the matched blob's content is deleted but it still exists in the rewritten tree.  its just that the content says its been deleted as
 *  part of the svn to git conversion.
 *  
 * @author ocleirig
 * 
 */
public class RepositoryRemoveMPXRewriter extends AbstractRepositoryCleaner {

	private static final Logger log = LoggerFactory
			.getLogger(RepositoryRemoveMPXRewriter.class);
	private ObjectId replacementContentBlobId;


	/**
	 * 
	 */
	public RepositoryRemoveMPXRewriter() {
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

		if (args.size() != 2 && args.size() != 4) {
			log.error("USAGE: <source git repository meta directory> <blob content replacement>  [<branchRefSpec> <git command path>]");
			log.error("\t<git repo meta directory> : the path to the meta directory of the source git repository");
			log.error("\t<blob content replacement> : content to replace the matched blob with");
			log.error("\t<branchRefSpec> : git refspec from which to source the graph to be rewritten");
			log.error("\t<git command path> : the path to a native git ");
			throw new IllegalArgumentException("invalid arguments");
		}

		setRepo(GitRepositoryUtils.buildFileRepository(
				new File(args.get(0)).getAbsoluteFile(), false));

		String blobReplacmentContent = args.get(1);
		
		replacementContentBlobId = getRepo().newObjectInserter().insert(Constants.OBJ_BLOB, blobReplacmentContent.getBytes());
		
		if (args.size() == 3)
			setBranchRefSpec(args.get(2).trim());

		if (args.size() == 4)
			setExternalGitCommandPath(args.get(3).trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.RepositoryCleaner#execute(org.eclipse.jgit
	 * .lib.Repository, java.io.File, long)
	 */
	@Override
	public void execute() throws IOException {

		ObjectInserter inserter = getRepo().newObjectInserter();

		boolean localBranchSource = true;

		if (!getBranchRefSpec().equals(Constants.R_HEADS))
			localBranchSource = false;

		String dateString = formatter.print(new DateTime());

		/*
		 * Track the commits that are rewritten.
		 * 
		 * This is important so that we can update the grafts file to relate to
		 * the current parent object ids.
		 */
		PrintWriter objectTranslationWriter = new PrintWriter(
				"object-translations-blob-rewrite" + dateString + ".txt");

		Map<String, Ref> branchHeads = getRepo().getRefDatabase().getRefs(
				getBranchRefSpec());

		Map<ObjectId, Set<Ref>> commitToBranchMap = new HashMap<ObjectId, Set<Ref>>();

		RevWalk walkRepo = new RevWalk(getRepo());

		for (Ref branchRef : branchHeads.values()) {

			ObjectId branchObjectId = branchRef.getObjectId();

			Set<Ref> refs = commitToBranchMap.get(branchObjectId);

			if (refs == null) {
				refs = new HashSet<>();
				commitToBranchMap.put(branchObjectId, refs);
			}

			refs.add(branchRef);

			walkRepo.markStart(walkRepo.parseCommit(branchObjectId));

		}

		Map<String, Ref> tagHeads = getRepo().getRefDatabase().getRefs(
				Constants.R_TAGS);

		Map<ObjectId, Set<Ref>> commitToTagMap = new HashMap<ObjectId, Set<Ref>>();

		RevWalk walkRefs = new RevWalk(getRepo());

		for (Ref tagRef : tagHeads.values()) {

			RevTag tag = walkRefs.parseTag(tagRef.getObjectId());

			ObjectId commitId = tag.getObject().getId();

			Set<Ref> refs = commitToTagMap.get(commitId);

			if (refs == null) {
				refs = new HashSet<>();
				commitToTagMap.put(commitId, refs);
			}

			refs.add(tagRef);

			walkRepo.markStart(walkRepo.parseCommit(commitId));
		}

		Set<ObjectId> leftSidePreventGCCommits = new HashSet<>();

		walkRepo.sort(RevSort.TOPO, true);
		walkRepo.sort(RevSort.REVERSE, true);

		Iterator<RevCommit> it = walkRepo.iterator();

		List<ReceiveCommand> deferredReferenceDeletes = new LinkedList<>();
		List<ReceiveCommand> deferredReferenceCreates = new LinkedList<>();

		objectTranslationWriter
				.println("# new-object-id <space> original-object-id");

		int counter = 1;

		// holds the old right side to new right side commit mapping
		Map<ObjectId, ObjectId> rightSideConversionMap = new HashMap<>();

		GitTreeProcessor treeProcessor = new GitTreeProcessor(getRepo());

		while (it.hasNext()) {

			RevCommit commit = it.next();

			boolean recreateCommit = false;

			for (RevCommit parentCommit : commit.getParents()) {

				if (rightSideConversionMap.containsKey(parentCommit.getId())) {
					recreateCommit = true;
					break;
				}

			}

			GitTreeData tree = treeProcessor
					.extractExistingTreeDataFromCommit(commit.getId());

			TreeWalk tw = new TreeWalk(getRepo());
			
			tw.setRecursive(true);
			
			tw.addTree(commit.getTree().getId());
			
			while (tw.next()) {
				
				if (!tw.getFileMode(0).equals(FileMode.REGULAR_FILE))
					continue;
				
				String pathName = tw.getPathString();
				
				if (pathName.endsWith(".mpx")) {
					
					tree.addBlob(pathName, replacementContentBlobId);
					
					recreateCommit = true;
				}
					
				
			}
			
			
			if (!recreateCommit)
				continue;
			
			/*
			 * Process in reverse order from old to new.
			 */
			CommitBuilder builder = new CommitBuilder();

			builder.setAuthor(commit.getAuthorIdent());
			builder.setMessage(commit.getFullMessage());

			builder.setCommitter(commit.getCommitterIdent());
			
			if (tree.isTreeDirty()) {
				
				ObjectId newTreeId = tree.buildTree(inserter);
				
				builder.setTreeId(newTreeId);
			}
			else {
				builder.setTreeId(commit.getTree().getId());
			}
			
			builder.setEncoding("UTF-8");

			Set<ObjectId> newParents = new HashSet<>();

			for (RevCommit parentCommit : commit.getParents()) {

				ObjectId adjustedParentId = rightSideConversionMap
						.get(parentCommit.getId());

				if (adjustedParentId != null)
					newParents.add(adjustedParentId);
				else
					newParents.add(parentCommit.getId());
			}

			builder.setParentIds(new ArrayList<>(newParents));

			ObjectId newCommitId = inserter.insert(builder);

			rightSideConversionMap.put(commit.getId(), newCommitId);

			objectTranslationWriter.println(newCommitId.name() + " "
					+ commit.getId().getName());


			RevWalk commitWalk = new RevWalk(getRepo());

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

					// refChangeWriter.println("deleted tagRef "
					// + tagRef.getName() + " original commit id: "
					// + tag.getObject().getId());

				}

				for (TagBuilder tagBuilder : newTagSet) {

					ObjectId tagId = inserter.insert(tagBuilder);

					String tagName = Constants.R_TAGS + tagBuilder.getTag();

					deferredReferenceCreates.add(new ReceiveCommand(null,
							tagId, tagName, Type.CREATE));

					// refChangeWriter.println("created tag ref: "
					// + tagBuilder.getTag() + " new commit id: "
					// + tagBuilder.getObjectId());
					//
					// rightRefsWriter.println(tagName);

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

						// refChangeWriter.println("deleted branchRef "
						// + branchRef.getName() + " original commit id: "
						// + branchRef.getObjectId());

					}

					String adjustedBranchName = Constants.R_HEADS
							+ branchRef.getName().substring(
									getBranchRefSpec().length());

					deferredReferenceCreates.add(new ReceiveCommand(null,
							newCommitId, adjustedBranchName, Type.CREATE));

					// refChangeWriter.println("Updated branchRef: "
					// + branchRef.getName() + " at original commit id: "
					// + branchRef.getObjectId() + " to local branch: "
					// + adjustedBranchName + " at new commit id: "
					// + newCommitId);
					// rightRefsWriter.println(adjustedBranchName);

				}

			}

			commitWalk.release();
		}

		inserter.flush();

		getRepo().getRefDatabase().refresh();

		log.info("Applying updates: " + deferredReferenceDeletes.size()
				+ " deletes, " + deferredReferenceCreates.size() + " creates.");

		if (getExternalGitCommandPath() != null) {
			ExternalGitUtils.batchRefUpdate(getExternalGitCommandPath(), getRepo(),
					deferredReferenceDeletes, System.out);
		} else {
			GitRefUtils.batchRefUpdate(getRepo(), deferredReferenceDeletes,
					NullProgressMonitor.INSTANCE);
		}

		getRepo().getRefDatabase().refresh();

		if (getExternalGitCommandPath() != null) {
			ExternalGitUtils.batchRefUpdate(getExternalGitCommandPath(), getRepo(),
					deferredReferenceCreates, System.out);
		} else {

			GitRefUtils.batchRefUpdate(getRepo(), deferredReferenceCreates,
					NullProgressMonitor.INSTANCE);

		}
		log.info("Completed.");

		walkRefs.release();
		walkRepo.release();

		inserter.release();
		
		super.close();

		objectTranslationWriter.close();

	}

}
