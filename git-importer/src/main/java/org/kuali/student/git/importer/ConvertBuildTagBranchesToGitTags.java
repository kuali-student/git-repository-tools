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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.awtui.AwtCredentialsProvider;
import org.eclipse.jgit.console.ConsoleCredentialsProvider;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.lib.BatchRefUpdate;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.ReceiveCommand.Type;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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

		if (args.length < 3 || args.length > 6) {
			System.err.println("USAGE: <git repository> <bare> <ref mode> [<ref prefix> <username> <password>]");
			System.err.println("\t<bare> : 0 (false) or 1 (true)");
			System.err.println("\t<ref mode> : local or name of remote");
			System.err.println("\t<ref prefix> : refs/heads (default) or say refs/remotes/origin (test clone)");
			System.exit(-1);
		}

		boolean bare = false;
		
		if (args[1].trim().equals("1")) {
			bare = true;
		}

		String remoteName = args[2].trim();
		
		String refPrefix = Constants.R_HEADS;
		
		if (args.length == 4)
			refPrefix = args[3].trim();
		
		String userName = null;
		String password = null;
		
		if (args.length == 5)
			userName = args[4].trim();
		
		if (args.length == 6)
			password = args[5].trim();
		
		try {
			
			Repository repo = GitRepositoryUtils.buildFileRepository(new File (args[0]).getAbsoluteFile(), false, bare);
			
			Git git = new Git(repo);
			
			ObjectInserter objectInserter = repo.newObjectInserter();
			
			Collection<Ref>repositoryHeads = repo.getRefDatabase().getRefs(refPrefix).values();
			
			RevWalk rw = new RevWalk(repo);
			
			Map<String, ObjectId>tagNameToTagId = new HashMap<>();
			
			Map<String, Ref>tagNameToRef = new HashMap<>();
			
			for (Ref ref : repositoryHeads) {
				
				String branchName = ref.getName().substring(refPrefix.length()+1);
				
				if (branchName.contains("tag") && branchName.contains("builds")) {
					
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
			
			List<RefSpec>branchesToDelete = new ArrayList<>();
			
			for (Entry<String, ObjectId> entry : tagNameToTagId.entrySet()) {
				
				String tagName = entry.getKey();
				
				// create the reference to the tag object
				batch.addCommand(new ReceiveCommand(null, entry.getValue(), Constants.R_TAGS + tagName, Type.CREATE));
				
				// delete the original branch object
				
				Ref branch = tagNameToRef.get(entry.getKey());
				
				if (remoteName.equals("local")) {
				
					batch.addCommand(new ReceiveCommand(branch.getObjectId(), null, branch.getName(), Type.DELETE));
				
				}
				else {
					String adjustedBranchName = branch.getName().substring(refPrefix.length()+1);
					
					branchesToDelete.add(new RefSpec(":" + Constants.R_HEADS + adjustedBranchName));
				}
				
			}
			
			// create the tags
			batch.execute(rw, new TextProgressMonitor());
			
			if (!remoteName.equals("local")) {
				// push the tag to the remote right now
				PushCommand pushCommand = git.push().setRemote(remoteName).setPushTags().setProgressMonitor(new TextProgressMonitor());
				
				if (userName != null)
					pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password));
				
				Iterable<PushResult> results = pushCommand.call();
				
				for (PushResult pushResult : results) {
					
					if (!pushResult.equals(Result.NEW)) {
						log.warn("failed to push tag " + pushResult.getMessages());
					}
				}
				
				// delete the branches from the remote
				results = git.push().setRemote(remoteName).setRefSpecs(branchesToDelete).setProgressMonitor(new TextProgressMonitor()).call();
				
				log.info("");
				
					
			}
			
			
			
			
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
		
			objectInserter.close();
			
			rw.close();
			
			
		} catch (Exception e) {
			
			log.error("unexpected Exception ", e);
		}
	}

	

}
