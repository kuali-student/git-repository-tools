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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
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
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceiveCommand.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.student.git.model.graft.GitGraft;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.utils.ExternalGitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public abstract class AbstractRepositoryCleaner implements RepositoryCleaner {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractRepositoryCleaner.class);

	protected static final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");
	
	protected static final DateTimeFormatter includeHourAndMinuteDateFormatter = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm");
	
	
	private Repository repo;

	private String branchRefSpec = Constants.R_HEADS;

	private String externalGitCommandPath = null;

	private Map<ObjectId, GitGraft> grafts = new HashMap<ObjectId, GitGraft>();

	protected ObjectInserter inserter;

	protected Map<String, Ref> branchHeads;

	protected Map<ObjectId, Set<Ref>> commitToBranchMap;

	protected Map<String, Ref> tagHeads;

	protected Map<ObjectId, Set<Ref>> commitToTagMap;

	protected String dateString;

	protected Map<ObjectId, ObjectId> originalCommitIdToNewCommitIdMap;

	private List<ReceiveCommand> deferredReferenceDeletes;

	private List<ReceiveCommand> deferredReferenceCreates;

	private RevWalk walkRepo;
	
	/**
	 * 
	 */
	public AbstractRepositoryCleaner() {
		
	}

	/**
	 * @return the repo
	 */
	protected Repository getRepo() {
		return repo;
	}

	/**
	 * @param repo the repo to set
	 */
	protected void setRepo(Repository repo) {
		this.repo = repo;
		
	}

	/**
	 * @return the branchRefSpec
	 */
	protected String getBranchRefSpec() {
		return branchRefSpec;
	}

	/**
	 * @param branchRefSpec the branchRefSpec to set
	 */
	protected void setBranchRefSpec(String branchRefSpec) {
		this.branchRefSpec = branchRefSpec;
	}

	/**
	 * @return the externalGitCommandPath
	 */
	protected String getExternalGitCommandPath() {
		return externalGitCommandPath;
	}

	/**
	 * @param externalGitCommandPath the externalGitCommandPath to set
	 */
	protected void setExternalGitCommandPath(String externalGitCommandPath) {
		this.externalGitCommandPath = externalGitCommandPath;
	}

	public void close() {
		
		if (repo != null)
			repo.close();
	}

	/*
	 * Load the grafts from the file name.
	 */
	protected void loadGrafts(String graftsFileName) throws IOException {

		List<String> graftLines = FileUtils.readLines(new File (graftsFileName));
		
		for (String graftLine : graftLines) {
			
			String parts[] = graftLine.split(" ");
			
			// part zero is the target commit
			
			Set<ObjectId>parents = new HashSet<>();
			
			for (int i = 1; i < parts.length; i++) {
				ObjectId parent = ObjectId.fromString(parts[i]);
				
				parents.add(parent);
			}
			
			ObjectId targetCommitId = ObjectId.fromString(parts[0]);
			
			GitGraft graft = new GitGraft(targetCommitId, parents);
			
			grafts.put(targetCommitId, graft);
			
		}
		
	}
	
	@Override
	public final void execute() throws IOException {

		onBeforeExecute();
		
		inserter = getRepo().newObjectInserter();

		boolean localBranchSource = true;

		if (!getBranchRefSpec().equals(Constants.R_HEADS))
			localBranchSource = false;

		dateString = formatter.print(new DateTime());

		/*
		 * Track the commits that are rewritten.
		 * 
		 * This is important so that we can update the grafts file to relate to
		 * the current parent object ids.
		 */
		PrintWriter objectTranslationWriter = new PrintWriter(
				"object-translations-" + getFileNameSuffix() + "-" + dateString + ".txt");

		branchHeads = getRepo().getRefDatabase().getRefs(
				getBranchRefSpec());

		commitToBranchMap = new HashMap<ObjectId, Set<Ref>>();

		walkRepo = new RevWalk(getRepo());

		for (Ref branchRef : branchHeads.values()) {

			ObjectId branchObjectId = branchRef.getObjectId();

			Set<Ref> refs = commitToBranchMap.get(branchObjectId);

			if (refs == null) {
				refs = new HashSet<>();
				commitToBranchMap.put(branchObjectId, refs);
			}

			refs.add(branchRef);

			walkRepo.markStart(walkRepo.parseCommit(branchObjectId));
			
			onBranchHead(branchRef, branchObjectId);

		}

		if (includeTagsInRevWalk()) {
			
			tagHeads = getRepo().getRefDatabase().getRefs(
				Constants.R_TAGS);
		}
		else {
			tagHeads = new HashMap<String, Ref>();
		}

		commitToTagMap = new HashMap<ObjectId, Set<Ref>>();

		for (Ref tagRef : tagHeads.values()) {

			RevTag tag = walkRepo.parseTag(tagRef.getObjectId());

			ObjectId commitId = tag.getObject().getId();

			Set<Ref> refs = commitToTagMap.get(commitId);

			if (refs == null) {
				refs = new HashSet<>();
				commitToTagMap.put(commitId, refs);
			}

			refs.add(tagRef);

			walkRepo.markStart(walkRepo.parseCommit(commitId));
			
			onTag(tag.getId(), commitId);
		}

		
		onBeforeRevWalk();
		
		Set<ObjectId> leftSidePreventGCCommits = new HashSet<>();

		walkRepo.sort(RevSort.TOPO, true);
		walkRepo.sort(RevSort.REVERSE, true);

		Iterator<RevCommit> it = provideRevCommitIterator(walkRepo.iterator());

		deferredReferenceDeletes = new LinkedList<>();
		deferredReferenceCreates = new LinkedList<>();

		objectTranslationWriter
				.println("# new-object-id <space> original-object-id");

		originalCommitIdToNewCommitIdMap = new HashMap<>();

		GitTreeProcessor treeProcessor = new GitTreeProcessor(getRepo());

		while (it.hasNext()) {

			RevCommit commit = it.next();

			boolean recreateCommitByTranslatedParent = false;

			for (RevCommit parentCommit : commit.getParents()) {

				if (originalCommitIdToNewCommitIdMap.containsKey(parentCommit.getId())) {
					recreateCommitByTranslatedParent = true;
					break;
				}

			}

			GitTreeData tree = treeProcessor
					.extractExistingTreeDataFromCommit(commit.getId());

			boolean recreate = processCommitTree(commit, tree);

			if (!(recreateCommitByTranslatedParent || recreate))
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

			Set<ObjectId> newParents = processParents(commit);

			builder.setParentIds(new ArrayList<>(newParents));

			ObjectId newCommitId = inserter.insert(builder);
			
			onNewCommit (commit, newCommitId);

			originalCommitIdToNewCommitIdMap.put(commit.getId(), newCommitId);

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

					deferDelete(tagRef.getName(), tagRef.getObjectId());
					
				}

				for (TagBuilder tagBuilder : newTagSet) {

					ObjectId tagId = inserter.insert(tagBuilder);

					String tagName = Constants.R_TAGS + tagBuilder.getTag();

					deferCreate(tagName, tagId);
					
					onTagRefCreate(tagName, tagId);
					
				}

			}

			// check if any branches need to be moved
			if (commitToBranchMap.containsKey(commit.getId())) {

				Set<Ref> refs = commitToBranchMap.get(commit.getId());

				for (Ref branchRef : refs) {

					if (localBranchSource) {

						deferDelete (branchRef.getName(), branchRef.getObjectId());

					}

					String adjustedBranchName = Constants.R_HEADS
							+ branchRef.getName().substring(
									getBranchRefSpec().length());

					deferCreate (adjustedBranchName, newCommitId);
					
					onBranchRefCreate(adjustedBranchName, newCommitId);
					
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

		walkRepo.release();

		inserter.release();
		
		close();

		objectTranslationWriter.close();

	}


	/**
	 * An extension point where the ordering of the commits can be changed.
	 * 
	 * Defaults to use the provided iterator.
	 * 
	 * @param iterator
	 * @return
	 */
	protected Iterator<RevCommit> provideRevCommitIterator(
			Iterator<RevCommit> iterator) {
		return iterator;
	}

	private void onBranchRefCreate(String adjustedBranchName,
			ObjectId newCommitId) {
		// TODO Auto-generated method stub
		
	}

	protected void onTagRefCreate(String tagName, ObjectId tagId) {
		
	}

	protected void deferCreate(String adjustedBranchName, ObjectId newCommitId) {
		deferredReferenceCreates.add(new ReceiveCommand(null,
				newCommitId, adjustedBranchName, Type.CREATE));

		
	}

	protected void deferDelete(String name, ObjectId objectId) {
		deferredReferenceDeletes.add(new ReceiveCommand(
				objectId, null, name, Type.DELETE));
	}

	protected void onNewCommit(RevCommit commit, ObjectId newCommitId) {
		
	}

	/**
	 * Default is to change parents that have been rewritten.
	 * 
	 * @param commit
	 * @return altered list of parents for the commit given.
	 */
	protected Set<ObjectId> processParents(RevCommit commit) {
		
		Set<ObjectId>newParents = new HashSet<ObjectId>();
		
		for (RevCommit parentCommit : commit.getParents()) {

			ObjectId adjustedParentId = originalCommitIdToNewCommitIdMap
					.get(parentCommit.getId());

			if (adjustedParentId != null)
				newParents.add(adjustedParentId);
			else
				newParents.add(parentCommit.getId());
		}
		
		return newParents;
	}

	protected void onBeforeRevWalk() {
		
	}

	protected void onTag(ObjectId id, ObjectId commitId) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
	}

	protected void onBranchHead(Ref branchRef, ObjectId branchObjectId) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
	}

	protected boolean processCommitTree(RevCommit commit, GitTreeData tree) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		// default is to not change the commit.
		
		// the commit might still be rewritten if its parent has changed.
		return false;
	}

	protected abstract String getFileNameSuffix();

	/**
	 * By default include tags in the rev walk
	 * 
	 * @return
	 */
	protected boolean includeTagsInRevWalk() {
		
		return true;
	}
	
	protected void onBeforeExecute() throws FileNotFoundException {
	}
}
	
