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
import java.net.URISyntaxException;
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
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Intended to be run after the GitImporterMain to push changes found on certain
 * branches to an external remote.
 * 
 * In C git there is no way to push a partial refspec. We try to support pushing
 * branches that start with some prefix.
 * 
 * @author Kuali Student Team
 * 
 */
public class JGitPushMain {

	private static final Logger log = LoggerFactory
			.getLogger(JGitPushMain.class);

	/**
	 * 
	 */
	public JGitPushMain() {
		// TODO Auto-generated constructor stub
	}

	/* Copied from PushCommand.call() */
	public static void push(Repository repo, String remoteName,
			List<RemoteRefUpdate> refsToUpdate, String remoteUserName,
			String remotePassword) throws IOException, URISyntaxException {

		ArrayList<PushResult> pushResults = new ArrayList<PushResult>(3);

		RemoteConfig config = new RemoteConfig(repo.getConfig(), remoteName);

		final List<Transport> transports;
		transports = Transport.openAll(repo, config, Transport.Operation.PUSH);

		for (final Transport transport : transports) {
			transport.setPushThin(false);
			transport.setOptionReceivePack(RemoteConfig.DEFAULT_RECEIVE_PACK);
			transport.setDryRun(false);

			configure(transport, remoteUserName, remotePassword);

			try {
				PushResult result = transport.push(new TextProgressMonitor(),
						refsToUpdate, System.out);
				pushResults.add(result);

			} catch (TransportException e) {
				throw new TransportException(e.getMessage(), e);
			} finally {
				transport.close();
			}
		}

	}

	/* copied from TransportCommand */
	private static void configure(Transport transport, String username,
			String password) {

		if (username != null && password != null)
			transport
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
							username, password));

		// wait 30 seconds
		transport.setTimeout(30);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 4 && args.length != 5) {
			System.err
					.println("USAGE: <git repository> <bare> <remote name> <branch prefixes> [<ref prefix>]");
			System.err
					.println("\t<branch prefixes> : like enrollment_ks-enroll (colon seperated for multiple prefixes)");
			System.err
					.println("\t<ref prefix> : like refs/heads/ or refs/remotes/origin/ needs to end with a trailing slash /");
			System.exit(-1);
		}

		boolean bare = false;
		
		if (args[1].trim().equals("true")) {
			bare = true;
		}

		String remoteName = args[2];

		String branchPrefixes[] = args[3].split(":");

		String refPrefix = Constants.R_HEADS;

		if (args.length == 5)
			refPrefix = args[4].trim();

		try {

			Repository repo = GitRepositoryUtils.buildFileRepository(new File(
					args[0]).getAbsoluteFile(), false, bare);

			Collection<Ref> refsToPush = repo.getRefDatabase()
					.getRefs(refPrefix).values();

			List<RemoteRefUpdate> refsToUpdate = new ArrayList<>();

			for (String branchPrefix : branchPrefixes) {

				String adjustedBranchPrefix = refPrefix + branchPrefix;

				for (Ref candidateRef : refsToPush) {

					if (candidateRef.getName().startsWith(adjustedBranchPrefix)) {

						String candidateBranchName = candidateRef.getName()
								.substring(refPrefix.length());

						String targetRefName = Constants.R_HEADS
								+ candidateBranchName;

						log.info("pushing " + adjustedBranchPrefix + " to "
								+ targetRefName);

						refsToUpdate.add(new RemoteRefUpdate(repo,
								candidateRef, targetRefName, true, null, null));
					}
				}
			}

			RevWalk rw = new RevWalk(repo);

			push(repo, remoteName, refsToUpdate, null, null);

			rw.close();

			repo.close();

		} catch (Exception e) {

			log.error("unexpected Exception ", e);
		}
	}

	/*
	 * In the future if the commit has no changes from the parent we may apply
	 * the tag to the parent instead.
	 */
	private static boolean commitContainsChangesToParent(Git git,
			Repository repo, AnyObjectId commitTreeId, AnyObjectId parentTreeId)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException, GitAPIException {
		DiffCommand diffCommand = git.diff();

		TreeWalk parentWalk = new TreeWalk(repo);

		parentWalk.addTree(parentTreeId);

		parentWalk.setRecursive(true);

		TreeWalk commitWalk = new TreeWalk(repo);

		commitWalk.addTree(commitTreeId);

		commitWalk.setRecursive(true);

		CanonicalTreeParser commitTreeParser = new CanonicalTreeParser(null,
				commitWalk.getObjectReader(), commitTreeId);

		CanonicalTreeParser parentTreeParser = new CanonicalTreeParser(null,
				parentWalk.getObjectReader(), parentTreeId);

		diffCommand.setOldTree(parentTreeParser);
		diffCommand.setNewTree(commitTreeParser);

		List<DiffEntry> entries = diffCommand.call();

		if (entries.size() > 0)
			return true;
		else
			return false;
	}

}
