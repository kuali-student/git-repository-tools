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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Intended to walk all of the commit graph in the repository to find the branch tags that are not children of the graph
 * 
 * @author Kuali Student Team
 * 
 */
public class IdentifyBranchTipsMain {

	private static final Logger log = LoggerFactory.getLogger(IdentifyBranchTipsMain.class);
	
	/**
	 * 
	 */
	public IdentifyBranchTipsMain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2 && args.length != 3) {
			System.err.println("USAGE: <git repository> <bare> [<ref prefix>]");
			System.err.println("\t<bare> : 0 (false) or 1 (true)");
			System.err.println("\t<ref prefix> : refs/heads (default) or say refs/remotes/origin (test clone)");
			System.exit(-1);
		}

		boolean bare = false;
		
		if (args[1].trim().equals("1")) {
			bare = true;
		}
		
		String refPrefix = Constants.R_HEADS;
		
		if (args.length == 3)
			refPrefix = args[2].trim();
		
		try {
			
			PrintWriter pw = new PrintWriter("branch-tips.dat");
			
			Repository repo = GitRepositoryUtils.buildFileRepository(new File (args[0]).getAbsoluteFile(), false, bare);
			
			Collection<Ref>repositoryHeads = repo.getRefDatabase().getRefs(refPrefix).values();
			
			RevWalk rw = new RevWalk(repo);
			
			Git git = new Git (repo);
			
			Map<String, ObjectId>branchMap = new HashMap<>();
			
			Map<ObjectId, Set<String>>commitToBranchMap = new HashMap<>();
			
			Set<ObjectId>visitedCommits = new HashSet<>();
			
			Set<String>branchTips = new HashSet<>();
			
			for (Ref ref : repositoryHeads) {
				
				String branchName = ref.getName().substring(refPrefix.length()+1);
			
				branchMap.put(branchName, ref.getObjectId());
				
				Set<String>branches = new HashSet<>();
				
				branches.add(branchName);
				
				commitToBranchMap.put(ref.getObjectId(), branches);
				
				branchTips.add(branchName);
				
			}
			
			for (Ref ref : repositoryHeads) {
				
				rw.reset();
				
				// skip over the prefix and the trailing '/'
				
				String branchName = ref.getName().substring(refPrefix.length()+1);
				
				if (visitedCommits.contains(ref.getObjectId())) {
					log.info("skipping {} because it has already been visited.", branchName);
					branchTips.remove(branchName);
					continue;
				}
				
				RevCommit currentCommit = rw.parseCommit(ref.getObjectId());
				
				rw.markStart(currentCommit);
				
				while ((currentCommit = rw.next()) != null) {
					
//					if (visitedCommits.contains(currentCommit.getId())) {
//						log.info("branch = {}, commit = {} skipping since we have already visited this graph.", branchName, currentCommit.getId().getName());
//						break;
//					}
					
					Set<String>currentBranchNames = commitToBranchMap.get(currentCommit.getId());
					
//					visitedCommits.add(currentCommit.getId());
					
					if (currentBranchNames != null) {
					
						for (String currentBranchName : currentBranchNames) {
							
							if (currentBranchName.equals(branchName))
								continue; // skip over our own branch name
							
							branchTips.remove(currentBranchName);
							
						}
						
					}
					
					
				}
				
				
			}
			
			log.info("found {} branch tips", branchTips.size());
			
			pw.println("# " + branchTips.size() + " branch tips");
			
			List<String>orderedBranchTips = new ArrayList<>();
			
			orderedBranchTips.addAll(branchTips);
			
			Collections.sort(orderedBranchTips);
			
			for (String branch : orderedBranchTips) {

				pw.println(branch);
			}
			
			rw.release();
			
			pw.close();
			
			
		} catch (Exception e) {
			
			log.error("unexpected Exception ", e);
		}
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

	

}
