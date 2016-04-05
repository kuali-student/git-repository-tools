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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
 * As part of the branch cleanup there will be a manual step.
 * 
 * This program will process manual instructions on what to do with a specific
 * branch.
 * 
 * options include: 1. just delete it 2. create a tag with the same name 3.
 * create a tag with a different name
 * 
 * the tag will inherit its details from the commit it is pointed at.
 * 
 * @author Kuali Student Team
 * 
 */
public class ApplyManualBranchCleanup {

	private static final Logger log = LoggerFactory
			.getLogger(ApplyManualBranchCleanup.class);

	/**
	 * 
	 */
	public ApplyManualBranchCleanup() {
		// TODO Auto-generated constructor stub
	}

	private static void usage() {
		System.err
				.println("USAGE: <input file> <git repository> <bare> <ref mode> [<ref prefix> <username> <password>]");
		System.err.println("\t<bare> : 0 (false) or 1 (true)");
		System.err.println("\t<ref mode> : local or name of remote");
		System.err
				.println("\t<ref prefix> : refs/heads (default) or say refs/remotes/origin (test clone)");
		System.exit(-1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 4 || args.length > 7) {
			usage();
		}

		File inputFile = new File(args[0]);

		if (!inputFile.exists())
			usage();

		boolean bare = false;

		if (args[2].trim().equals("1")) {
			bare = true;
		}

		String remoteName = args[3].trim();

		String refPrefix = Constants.R_HEADS;

		if (args.length == 5)
			refPrefix = args[4].trim();

		String userName = null;
		String password = null;

		if (args.length == 6)
			userName = args[5].trim();

		if (args.length == 7)
			password = args[6].trim();

		try {

			Repository repo = GitRepositoryUtils.buildFileRepository(new File(
					args[1]).getAbsoluteFile(), false, bare);

			Git git = new Git(repo);

			RevWalk rw = new RevWalk(repo);

			ObjectInserter objectInserter = repo.newObjectInserter();

			BufferedReader fileReader = new BufferedReader(new FileReader(
					inputFile));

			String line = fileReader.readLine();

			int lineNumber = 1;

			BatchRefUpdate batch = repo.getRefDatabase().newBatchUpdate();

			List<RefSpec> branchesToDelete = new ArrayList<>();

			while (line != null) {

				if (line.startsWith("#") || line.length() == 0) {
					// skip over comments and blank lines
					line = fileReader.readLine();
					lineNumber++;
					
					continue;
				}
				
				String parts[] = line.trim().split(":");

				String branchName = parts[0];

				Ref branchRef = repo.getRef(refPrefix + "/" + branchName);

				if (branchRef == null) {
					log.warn(
							"line: {}, No branch matching {} exists, skipping.",
							lineNumber, branchName);

					line = fileReader.readLine();
					lineNumber++;

					continue;
				}

				String tagName = null;

				if (parts.length > 1)
					tagName = parts[1];

				if (tagName != null) {
					
					if (tagName.equals("keep")) {
						log.info("keeping existing branch for {}", branchName);
						
						line = fileReader.readLine();
						lineNumber++;
						
						continue;
					}
					
					if (tagName.equals("tag")) {
						
						/*
						 * Shortcut to say make the tag start with the same name as the branch.
						 */
						tagName = branchName;
					}
					// create a tag

					RevCommit commit = rw.parseCommit(branchRef.getObjectId());

					ObjectId tag = GitRefUtils.insertTag(tagName, commit,
							objectInserter);

					batch.addCommand(new ReceiveCommand(null, tag,
							Constants.R_TAGS + tagName, Type.CREATE));
					
					log.info("converting branch {} into a tag {}", branchName, tagName);

				}

				if (remoteName.equals("local")) {
					batch.addCommand(new ReceiveCommand(
							branchRef.getObjectId(), null, branchRef.getName(),
							Type.DELETE));
				} else {

					// if the branch is remote then remember its name so we can batch delete after we have the full list.
					branchesToDelete.add(new RefSpec(":" + Constants.R_HEADS
							+ branchName));
				}

				line = fileReader.readLine();
				lineNumber++;

			}

			fileReader.close();

			// run the batch update
			batch.execute(rw, new TextProgressMonitor());

			if (!remoteName.equals("local")) {
				// push the tag to the remote right now
				
				log.info("pushing tags to {}", remoteName);
				
				PushCommand pushCommand = git.push().setRemote(remoteName)
						.setPushTags()
						.setProgressMonitor(new TextProgressMonitor());

				if (userName != null)
					pushCommand
							.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
									userName, password));

				Iterable<PushResult> results = pushCommand.call();

				for (PushResult pushResult : results) {

					if (!pushResult.equals(Result.NEW)) {
						log.warn("failed to push tag "
								+ pushResult.getMessages());
					}
				}

				// delete the branches from the remote
				
				log.info("pushing branch deletes to remote: {}", remoteName);
				
				results = git.push().setRemote(remoteName)
						.setRefSpecs(branchesToDelete)
						.setProgressMonitor(new TextProgressMonitor()).call();
			}

			objectInserter.close();

			rw.close();

		} catch (Exception e) {

			log.error("unexpected Exception ", e);
		}
	}

}
