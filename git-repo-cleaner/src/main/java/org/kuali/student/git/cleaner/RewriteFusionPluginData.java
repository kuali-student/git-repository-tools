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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
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
import org.kuali.student.git.cleaner.model.ObjectIdTranslation;
import org.kuali.student.git.cleaner.model.ObjectIdTranslationService;
import org.kuali.student.git.cleaner.model.ObjectTranslationDataSource;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.SvnExternalsUtils;
import org.kuali.student.git.model.graft.GitGraft;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.GitTreeData.GitTreeDataVisitor;
import org.kuali.student.git.model.tree.GitTreeNodeInitializerImpl;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.utils.ExternalGitUtils;
import org.kuali.student.svn.model.ExternalModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * After a repository has been split into pieces the fusion-maven-plugin.dat file may refer to stale object ids.
 * 
 * This program will rewrite the commits and related commits to those that contain the fusion-maven-plugin.dat file to use the new object ids.
 * 
 * If the to be fused id refers to an object id that is on the left side of the cut line a temporary right side clone of that object will be created.
 * 
 * 
 * @author ocleirig
 * 
 */
public class RewriteFusionPluginData extends AbstractRepositoryCleaner {

	private static final Logger log = LoggerFactory
			.getLogger(RewriteFusionPluginData.class);
	

	private Map<ObjectId, GitGraft>targetCommitToGitGrafts = new HashMap<>();
	
	private Map<ObjectId, ObjectId> oldToNewCommitMap;

	private GitTreeProcessor rightTreeProcessor;


	private ObjectIdTranslationService translationService;
		
	
	/**
	 * 
	 */
	public RewriteFusionPluginData() {
		// TODO Auto-generated constructor stub
	}

	
	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.RepositoryCleaner#validateArgs(java.lang.String[])
	 */
	@Override
	public void validateArgs(List<String> args) throws Exception {

		if (args.size() != 2 && args.size() != 4) {
			log.error("USAGE: <right git repository meta directory> <replaced objects files> [<branchRefSpec> <git command path>]");
			log.error("\t<right git repo meta directory> : the path to the meta directory of the right (target) git repository");
			log.error("\t<grafts file> : existing grafts file created when the left and right repo's were split");
			log.error("\t<replaced objects files> : double colon seperated list of object-translations files. specified in the order of the translations");
			log.error("\t<branchRefSpec> : git refspec from which to source the graph to be rewritten");
			log.error("\t<git command path> : the path to a native git ");
			throw new IllegalArgumentException("invalid arguments");
		}
		
		setRepo(GitRepositoryUtils.buildFileRepository(
				new File (args.get(0)).getAbsoluteFile(), false));
		
		
		List<ObjectIdTranslation>dataSources = new ArrayList<ObjectIdTranslation>();
		
		String objectTranslationDataSources[] = args.get(1).split("::");
		
		
		for (String objectTranslationDataFile : objectTranslationDataSources) {
			
			ObjectTranslationDataSource dataSource = new ObjectTranslationDataSource(objectTranslationDataFile);
			
			// format: new object id <space> original object id
			
			List<String> newToOriginalObjectLines = FileUtils.readLines(new File (objectTranslationDataFile));
			
			for (String rightToLeftObjectLine : newToOriginalObjectLines) {
				
				if (rightToLeftObjectLine.trim().length() == 0 || rightToLeftObjectLine.startsWith("#"))
					continue; // skip blank lines and comments.
				
				String parts[] = rightToLeftObjectLine.split(" ");
				
				dataSource.storeObjectTranslation(parts[1].trim(), parts[0].trim());
				
			}
			
			dataSources.add(dataSource);
		}
		
		translationService = new ObjectIdTranslationService(dataSources);
		
		if (args.size() >= 3)
			setBranchRefSpec(args.get(2).trim());
		
		
		if (args.size() == 4)
			setExternalGitCommandPath(args.get(3).trim());
		
		
		rightTreeProcessor = new GitTreeProcessor(getRepo());
		
		
		
		
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

//		RevWalk leftWalk = new RevWalk(leftRepo);
		
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
				"object-translations-fusion-rewrite-" + dateString + ".txt");

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

		walkRepo.sort(RevSort.TOPO, true);
		walkRepo.sort(RevSort.REVERSE, true);

		Iterator<RevCommit> it = walkRepo.iterator();

		List<ReceiveCommand> deferredReferenceDeletes = new LinkedList<>();
		List<ReceiveCommand> deferredReferenceCreates = new LinkedList<>();

		objectTranslationWriter
				.println("# new-object-id <space> original-object-id");


		oldToNewCommitMap = new HashMap<>();

		while (it.hasNext()) {

			RevCommit commit = it.next();

			boolean recreateCommit = false;

			for (RevCommit parentCommit : commit.getParents()) {

				if (oldToNewCommitMap.containsKey(parentCommit.getId())) {
					recreateCommit = true;
					break;
				}

			}

			GitTreeData tree = rightTreeProcessor
					.extractExistingTreeDataFromCommit(commit.getId());

			ObjectId fusionPluginDataBlobId = tree.find(getRepo(), "fusion-maven-plugin.dat");
			
			boolean changesToBeCommitted = false;
			
			if (fusionPluginDataBlobId != null) {
				
				ObjectLoader loader = getRepo().newObjectReader().open(fusionPluginDataBlobId, Constants.OBJ_BLOB);
				
				// rewrite the data here
				List<ExternalModuleInfo> fusionData = SvnExternalsUtils.extractFusionMavenPluginData(loader.openStream());
				
				
				for (ExternalModuleInfo fusion : fusionData) {
					
					ObjectId commitId = fusion.getBranchHeadId();
					
					if (commitId == null) {
						log.warn("commit Id: " + commit.getId().name() + " is missing branch head for module: " + fusion.getModuleName());
						continue;
					}
					
					// check where this originates from
					ObjectId newCommitId = this.translationService.translateObjectId(commitId);
					
					if (newCommitId != null && !newCommitId.equals(commitId)) {
						
						/*
						 * It might be possible for the new commit to have been rewritten as part of the fusion plugin changes
						 * so check for an even newer commit id
						 */
						
						ObjectId newerCommitId = this.oldToNewCommitMap.get(newCommitId);
						
						if (newerCommitId != null)
							fusion.setBranchHeadId(newerCommitId);
						else
							fusion.setBranchHeadId(newCommitId);
						
						changesToBeCommitted = true;
					}
					
					
				}
				
				if (changesToBeCommitted) {
					
					// save it into the tree
					String updatedFusionData = SvnExternalsUtils.createFusionMavenPluginDataFileString(getRepo(), fusionData);
					
					ObjectId updatedBlobId = inserter.insert(Constants.OBJ_BLOB, updatedFusionData.getBytes());
					
					tree.addBlob("fusion-maven-plugin.dat", updatedBlobId);
			
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

				ObjectId adjustedParentId = oldToNewCommitMap
						.get(parentCommit.getId());

				if (adjustedParentId != null)
					newParents.add(adjustedParentId);
				else
					newParents.add(parentCommit.getId());
			}

			builder.setParentIds(new ArrayList<>(newParents));

			ObjectId newCommitId = inserter.insert(builder);

			oldToNewCommitMap.put(commit.getId(), newCommitId);
			
			updateGrafts(commit.getId(), newCommitId);

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


	
	private void updateGrafts(ObjectId targetId, ObjectId newCommitId) {
		
		/*
		 * Update the grafts that refer to targetId to now refer to newCommitId
		 * 
		 * This could be in the target or the parent commits
		 */
		
		Collection<GitGraft> grafts = this.targetCommitToGitGrafts.values();

		this.targetCommitToGitGrafts.clear();
		
		for (GitGraft gitGraft : grafts) {
			
			GitGraft newGraft = gitGraft;
			
			ObjectId graftTargetId = gitGraft.getTargetCommitId();
			
			ObjectId newId = this.oldToNewCommitMap.get(graftTargetId);
			
			if (newId != null)
				newGraft = gitGraft.setTargetCommitId(newId);
			
			Set<ObjectId>parentCommitIds = new HashSet<>();
			
			for (ObjectId parentId : gitGraft.getParentCommitIds()) {
			
				ObjectId newParentId = this.oldToNewCommitMap.get(graftTargetId);
				
				if (newParentId != null)
					parentCommitIds.add(newParentId);
				else
					parentCommitIds.add(parentId);
			}
			
			newGraft = newGraft.setParentCommitIds(parentCommitIds);
			
			this.targetCommitToGitGrafts.put(newGraft.getTargetCommitId(), newGraft);
			
		}
	}


	

}
