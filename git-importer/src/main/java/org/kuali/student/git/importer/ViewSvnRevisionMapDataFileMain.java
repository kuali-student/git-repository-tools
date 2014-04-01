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

import org.apache.commons.lang3.StringUtils;
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
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.SvnRevisionMapper;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * View a specific revision section in an svn revision mapper data file.
 * 
 * @author Kuali Student Team
 * 
 */
public class ViewSvnRevisionMapDataFileMain {

	private static final Logger log = LoggerFactory.getLogger(ViewSvnRevisionMapDataFileMain.class);
	
	/**
	 * 
	 */
	public ViewSvnRevisionMapDataFileMain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 3 && args.length != 4) {
			usage();
		}

		String gitRepositoryFile = args[0];
		
		String mode = args[1];
		
		String revisionString = args[2];
		
		boolean revisionsMode = false;
		
		boolean mergeMode = false;
		
		if (mode.equals("revisions"))
			revisionsMode = true;
		else if (mode.equals("merge"))
			mergeMode = true;

		String targetBranch = null;
		
		if (mergeMode) {
			
			if (args.length != 4) 
				usage();
			else
				targetBranch = args[3];
		}
		
		try {
			
			long revision = Long.parseLong(revisionString);
			
			Repository repo = GitRepositoryUtils.buildFileRepository(new File (args[0]).getAbsoluteFile(), false, true);
			
			SvnRevisionMapper revisionMapper = new SvnRevisionMapper(repo);
			
			revisionMapper.initialize();
			
			if (mergeMode) {
				
				List<BranchMergeInfo> mergeData = revisionMapper.getMergeBranches(revision, targetBranch);
				
				for (BranchMergeInfo bmi : mergeData) {
					
					System.out.println("branchName       = " + bmi.getBranchName());
					
					System.out.println("merged revisions = " + StringUtils.join(bmi.getMergedRevisions(), ", "));
					
				}
			}
			else if (revisionsMode) {
				
				List<SvnRevisionMap> heads = revisionMapper.getRevisionHeads(revision);
			
				for (SvnRevisionMap svnRevisionMap : heads) {
					
					System.out.println("branchName = " + svnRevisionMap.getBranchName());
					System.out.println("branchPath = " + svnRevisionMap.getBranchPath());
					System.out.println("commitId   = " + svnRevisionMap.getCommitId());
				}
			}
			
			
			revisionMapper.shutdown();
			
			
			
		} catch (Exception e) {
			
			log.error("unexpected Exception ", e);
		}
	}

	private static void usage() {
		System.err.println("USAGE: <git repository> <mode> <revision> [<target branch>]");
		System.err.println("\t<mode> : revisions or merge");
		System.exit(-1);
		
	}

	/*
	 * In the future if the commit has no changes from the parent we may apply the tag to the parent instead.
	 */
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
