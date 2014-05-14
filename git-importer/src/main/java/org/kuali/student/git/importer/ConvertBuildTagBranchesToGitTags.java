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
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.BatchRefUpdate;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceiveCommand.Type;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Intended to create Git Tag's for all build- branches.
 * 
 * We also want to strip off the leading branch name so that only the final module-version-build-number part is on the tag.
 * 
 * I may want to backdate the tag committer information to match the commit date.
 * 
 * I also want to exclude old branches, etc.
 * 
 * @author Kuali Student Team
 * 
 */
public class ConvertBuildTagBranchesToGitTags {

	private static final Logger log = LoggerFactory.getLogger(ConvertBuildTagBranchesToGitTags.class);
	
	/**
	 * 
	 */
	public ConvertBuildTagBranchesToGitTags() {
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
		
		boolean deleteMode = false;
		
		if (args[1].trim().equals("1")) {
			bare = true;
		}
		
		String refPrefix = Constants.R_HEADS;
		
		if (args.length == 3)
			refPrefix = args[2].trim();
		
		try {
			
			Repository repo = GitRepositoryUtils.buildFileRepository(new File (args[0]).getAbsoluteFile(), false, bare);
			
			
			ObjectInserter objectInserter = repo.newObjectInserter();
			
			Collection<Ref>repositoryHeads = repo.getRefDatabase().getRefs(refPrefix).values();
			
			RevWalk rw = new RevWalk(repo);
			
			Map<String, ObjectId>tagNameToTagId = new HashMap<>();
			
			Map<String, Ref>tagNameToRef = new HashMap<>();
			
			for (Ref ref : repositoryHeads) {
				
				String branchName = ref.getName().substring(refPrefix.length()+1);
				
				if (branchName.contains("build-")) {
					
					String branchParts[] = branchName.split("_");
					
					int buildsIndex = ArrayUtils.indexOf(branchParts, "builds");
					
					String moduleName = StringUtils.join(branchParts, "_", buildsIndex+1, branchParts.length);
					
					RevCommit commit = rw.parseCommit(ref.getObjectId());
					
					ObjectId tag = GitRefUtils.insertTag(moduleName, commit, objectInserter);
					
					tagNameToTagId.put(moduleName, tag);
					
					tagNameToRef.put(moduleName, ref);
					
				}
				
			}
			
			BatchRefUpdate batch = repo.getRefDatabase().newBatchUpdate();
			
			for (Entry<String, ObjectId> entry : tagNameToTagId.entrySet()) {
				
				String tagName = entry.getKey();
				
				// create the reference to the tag object
				batch.addCommand(new ReceiveCommand(null, entry.getValue(), Constants.R_TAGS + tagName, Type.CREATE));
				
				// delete the original branch object
				
				Ref branch = tagNameToRef.get(entry.getKey());
				
				batch.addCommand(new ReceiveCommand(branch.getObjectId(), null, branch.getName(), Type.DELETE));
				
			}
			
			batch.execute(rw, new TextProgressMonitor());
			
//			Result result = GitRefUtils.createTagReference(repo, moduleName, tag);
//			
//			if (!result.equals(Result.NEW)) {
//				log.warn("failed to create tag {} for branch {}", moduleName, branchName);
//				continue;
//			}
//			
//			if (deleteMode) {
//			result = GitRefUtils.deleteRef(repo, ref);
//	
//			if (!result.equals(Result.NEW)) {
//				log.warn("failed to delete branch {}", branchName);
//				continue;
//			}
		
			objectInserter.release();
			
			rw.release();
			
			
		} catch (Exception e) {
			
			log.error("unexpected Exception ", e);
		}
	}

	

}
