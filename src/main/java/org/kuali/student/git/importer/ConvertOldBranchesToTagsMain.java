/*
 * Copyright 2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.git.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Intended to be run after the GitImporterMain to prune old branches from the list of active branches.
 * 
 * We don't wan't to loose the commit so we will turn them into tags.
 * 
 * If the commit has only one parent and there is no difference to its parent commit then we will move the tag back to the parent that does have
 * a change to its parent.
 * 
 * @author Kuali Student Team
 * 
 */
public class ConvertOldBranchesToTagsMain {

	private static final Logger log = LoggerFactory.getLogger(ConvertOldBranchesToTagsMain.class);
	
	/**
	 * 
	 */
	public ConvertOldBranchesToTagsMain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 3 && args.length != 4) {
			System.err.println("USAGE: <git repository> <mode> <bare> [<ref prefix>]");
			System.err.println("\t<mode> : tag or delete");
			System.err.println("\t<bare> : 0 (false) or 1 (true)");
			System.err.println("\t<ref prefix> : refs/heads (default) or say refs/remotes/origin (test clone)");
			System.exit(-1);
		}

		boolean bare = false;
		
		if (args[2].trim().equals("1")) {
			bare = true;
		}
		
		boolean tagMode = false;
		
		boolean deleteMode = false;
		
		if (args[1].equals("tag"))
			tagMode = true;
		else if (args[1].equals("delete"))
			deleteMode = true;
		
		String refPrefix = Constants.R_HEADS;
		
		if (args.length == 4)
			refPrefix = args[3].trim();
		
		try {
			
			Repository repo = GitRepositoryUtils.buildFileRepository(new File (args[0]).getAbsoluteFile(), false, bare);
			
			Collection<Ref>repositoryHeads = repo.getRefDatabase().getRefs(refPrefix).values();
			
			RevWalk rw = new RevWalk(repo);
			
			Git git = new Git (repo);
			
			ObjectInserter objectInserter = repo.newObjectInserter();
			
			List<String>branchesToDelete = new ArrayList<>();
			
			for (Ref ref : repositoryHeads) {
				
				if (!ref.getName().contains("@"))
					continue;  // we only want those with @ in the name
				
				if (deleteMode)
					branchesToDelete.add(ref.getName());
				
				if (!tagMode)
					continue;
				
				// else tag mode
				
				String simpleTagName = ref.getName().replaceFirst(refPrefix, "");
				
				RevCommit commit = rw.parseCommit(ref.getObjectId());
				
				ObjectId tagId = null;
				
				// tag this commit
				tagId = tagCommit (simpleTagName, commit, objectInserter);
				
				if (tagId != null) {
					
					
					// update the tag reference
					// copied from JGit's TagCommand
					String refName = Constants.R_TAGS + simpleTagName;
					RefUpdate tagRef = repo.updateRef(refName);
					tagRef.setNewObjectId(tagId);
					tagRef.setForceUpdate(true);
					tagRef.setRefLogMessage("tagged " + simpleTagName, false); 
					Result updateResult = tagRef.update(rw);
					
					if (updateResult != Result.NEW) {
						log.warn("problem creating tag reference for " + simpleTagName + " result = " + updateResult);
					}
				}
				
			}
			
			if (deleteMode) {
				
				for (String branch : branchesToDelete) {
					
					RefUpdate update = repo.updateRef(branch);
					
					update.setForceUpdate(true);
					
					Result result = update.delete(rw);
					
					if (result != Result.FORCED) {
						
						log.warn("failed to delete the branch ref = " + branch);
					}
				
				}
			}
			
			
			objectInserter.flush();
			objectInserter.release();
			
		} catch (Exception e) {
			
			log.error("unexpected Exception ", e);
		}
	}

	private static boolean commitContainsChangesToParent(Git git, Repository repo, AnyObjectId commitTreeId, AnyObjectId parentTreeId) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException, GitAPIException {
		DiffCommand diffCommand = git.diff();
		
		TreeWalk parentWalk = new TreeWalk(repo);
		
		parentWalk.addTree(parentTreeId);
		
		parentWalk.setRecursive(true);
		
		TreeWalk commitWalk = new TreeWalk(repo);
		
		commitWalk.addTree(commitTreeId);
		
		commitWalk.setRecursive(true);
		
		CanonicalTreeParser commitTreeParser = new CanonicalTreeParser(null, commitWalk.getObjectReader(), commitTreeId);
		
		CanonicalTreeParser parentTreeParser = new CanonicalTreeParser(null, parentWalk.getObjectReader(), parentTreeId);
		
		diffCommand.setOldTree(parentTreeParser);
		diffCommand.setNewTree(commitTreeParser);
		
		List<DiffEntry> entries = diffCommand.call();
		
		if (entries.size() > 0)
			return true;
		else
			return false;
	}

	private static ObjectId tagCommit(String tagName, RevCommit commit,
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

}
