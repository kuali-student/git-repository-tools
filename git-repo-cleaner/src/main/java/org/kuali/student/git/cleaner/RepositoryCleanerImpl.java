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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public class RepositoryCleanerImpl implements RepositoryCleaner {

	private static final Logger log = LoggerFactory.getLogger(RepositoryCleanerImpl.class);
	
	/**
	 * 
	 */
	public RepositoryCleanerImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.RepositoryCleaner#execute(org.eclipse.jgit.lib.Repository, java.io.File, long)
	 */
	@Override
	public void execute(Repository repo, Date splitDate) throws IOException {
		
		PrintWriter pw = new PrintWriter ("grafts-" + DateFormat.getDateInstance().format(splitDate) + ".txt");
		
		ObjectInserter objectInserter = repo.newObjectInserter();
		
		RevWalk rw = new RevWalk(repo);
		
		Set<RevCommit>startingPoints = new HashSet<>();
		
		Map<String, Ref> branchHeads = repo.getRefDatabase().getRefs(Constants.R_HEADS);
		
		Map<ObjectId, Set<Ref>>commitToBranchMap = new HashMap<ObjectId, Set<Ref>>();

		for (Ref branchRef : branchHeads.values()) {
			
			ObjectId branchObjectId = branchRef.getObjectId();
			
			Set<Ref>refs = commitToBranchMap.get(branchObjectId);
			
			if (refs == null) {
				refs = new HashSet<>();
				commitToBranchMap.put(branchObjectId, refs);
			}
			
			refs.add(branchRef);
		
			startingPoints.add(rw.parseCommit(branchObjectId));
			
		}
		
		Map<String, Ref> tagHeads = repo.getRefDatabase().getRefs(Constants.R_TAGS);
		
		Map<ObjectId, Set<Ref>>commitToTagMap = new HashMap<ObjectId, Set<Ref>>();

		for (Ref tagRef : tagHeads.values()) {
			
			RevTag tag = rw.parseTag(tagRef.getObjectId());
			
			ObjectId commitId = tag.getObject().getId();
			
			Set<Ref>refs = commitToTagMap.get(commitId);
			
			if (refs == null) {
				refs = new HashSet<>();
				commitToBranchMap.put(commitId, refs);
			}
			
			refs.add(tagRef);
		}
		
		Set<ObjectId>leftSideCommits = new HashSet<>();
		
		rw.setRevFilter(CommitTimeRevFilter.before(splitDate));
		
		rw.markStart(startingPoints);
		
		Iterator<RevCommit> it = rw.iterator();
		
		while (it.hasNext()) {
			
			RevCommit commit = it.next();
		
			leftSideCommits.add(commit.getId());
			
		}
		
		rw.setRevFilter(CommitTimeRevFilter.after(splitDate));
		
		rw.sort(RevSort.TOPO);
		rw.sort(RevSort.REVERSE, true);
		
		rw.reset();
		
		it = rw.iterator();
		
		while (it.hasNext()) {
			
			RevCommit commit = it.next();

			/*
			 * Process the right side.
			 * 
			 * We should be rewriting from old to new.
			 * 
			 */
			CommitBuilder builder = new CommitBuilder();
			
			builder.setAuthor(commit.getAuthorIdent());
			builder.setMessage(commit.getFullMessage());
			
			builder.setCommitter(commit.getCommitterIdent());
			builder.setTreeId(commit.getTree().getId());
			builder.setEncoding("UTF-8");

			Set<ObjectId>newParents = new HashSet<>();

			Set<ObjectId>removedParents = new HashSet<>();
			
			for (RevCommit parentCommit : commit.getParents()) {
				
				if (leftSideCommits.contains(parentCommit.getId())) {
					removedParents.add(parentCommit.getId());
				}
				else {
					newParents.add(parentCommit.getId());
				}
			}
			
			builder.setParentIds(new ArrayList<>(newParents));

			ObjectId newCommitId = objectInserter.insert(builder);
			
		
			if (removedParents.size() > 0) {
				
				// create the graft.
			
				List<String>graftData = new ArrayList<>();
				
				graftData.add(newCommitId.getName());
				
				for (ObjectId parentId : newParents) {
					graftData.add(parentId.getName());
				}
				
				for (ObjectId parentId : removedParents) {
					graftData.add(parentId.getName());
				}
				
				pw.println(StringUtils.join(graftData, " "));
			
			}
			
			RevWalk commitWalk = new RevWalk (repo);
			
			RevCommit newCommit = commitWalk.parseCommit(newCommitId);
			
			// check if any tags need to be moved
			if (commitToTagMap.containsKey(commit.getId())) {
				
				Set<Ref>tags = commitToTagMap.get(commit.getId());
				
				Set<TagBuilder>newTagSet = new HashSet<>();
				
				for (Ref tagRef : tags) {
				
					RevTag tag = commitWalk.parseTag(tagRef.getObjectId());
					
					TagBuilder tb = new TagBuilder();
					
					tb.setMessage(tag.getFullMessage());
					tb.setObjectId(newCommit);
					tb.setTag(tag.getTagName());
					tb.setTagger(tag.getTaggerIdent());

					newTagSet.add(tb);
					
					Result result = GitRefUtils.deleteRef(repo, tagRef);

					log.info("");
					
				}
				
				repo.getRefDatabase().refresh();
				
				for (TagBuilder tagBuilder : newTagSet) {
					
					ObjectId tagId = objectInserter.insert(tagBuilder);
					
					Result result = GitRefUtils.createTagReference(repo, tagBuilder.getTag(), tagId);
					
					log.info("");
				}
				
				
				
				
			}
			
			// check if any branches need to be moved
			if (commitToBranchMap.containsKey(commit.getId())) {
				
				Set<Ref>refs = commitToBranchMap.get(commit.getId());
				
				for (Ref branchRef : refs) {
					
					GitRefUtils.createOrUpdateBranch(repo, branchRef.getName(), newCommitId);
					
				}
				
			}
			
			commitWalk.release();
		}
		
		objectInserter.release();
		repo.close();
		pw.close();
		
	}

	private RevCommit duplicateCommit(RevCommit commit,
			Set<ObjectId> leftSideCommits) {
		
		
		
		Set
		builder.setParentIds(newParents);
		return null;
	}

	
	
	

}
