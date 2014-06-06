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
package org.kuali.student.git.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 * 
 */
public final class ExternalGitUtils {

	private static final Logger log = LoggerFactory
			.getLogger(ExternalGitUtils.class);

	/**
	 * 
	 */
	private ExternalGitUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Checkout the named branch using the branch name provided.
	 * 
	 * @param externalGitCommandPath
	 * @param repo
	 * @param branchName
	 * @param redirectStream
	 *            if not null then output from the sub process will be written
	 *            here.
	 * @return
	 */
	public static boolean checkoutBranch(String externalGitCommandPath,
			Repository repo, String branchName, boolean force,
			OutputStream redirectStream) {

		try {

			Process p = null;

			if (force) {
				p = runGitCommand(externalGitCommandPath, repo, false,
						"checkout", branchName, "--force");
			} else {
				p = runGitCommand(externalGitCommandPath, repo, false,
						"checkout", branchName);
			}

			int result = waitFor(p, redirectStream);

			if (result == 0)
				return true;
			else {
				readStream(p.getErrorStream(), redirectStream);
				return false;
			}

		} catch (IOException e) {
			log.error("failed to checkout branch", e);
			return false;
		} catch (InterruptedException e) {
			return false;
		}

	}

	// set inGitMetaDirectory to false to run in the working copy directory.
	private static Process runGitCommand(String externalGitCommandPath,
			Repository repo, boolean inGitMetaDirectory,
			String... gitCommandArgs) throws IOException {

		List<String> commandArgs = new ArrayList<>();

		commandArgs.add(externalGitCommandPath);
		commandArgs.addAll(Arrays.asList(gitCommandArgs));

		return runGitCommand(repo, inGitMetaDirectory, commandArgs);

	}

	private static Process runGitCommand(Repository repo,
			boolean inGitMetaDirectory, List<String> commandArgs)
			throws IOException {

		File gitDirectory = null;

		if (inGitMetaDirectory)
			gitDirectory = repo.getDirectory();
		else
			gitDirectory = repo.getWorkTree();

		// inherit the parent environment
		// locate in the working copy of the gir directory
		Process p = Runtime.getRuntime().exec(
				commandArgs.toArray(new String[] {}), null, gitDirectory);

		return p;
	}

	private static int waitFor(Process p, OutputStream redirectStream)
			throws InterruptedException, IOException {

		if (redirectStream != null) {

			readStream(p.getInputStream(), redirectStream);
		}

		return p.waitFor();

	}

	private static void readStream(InputStream inputStream,
			OutputStream redirectStream) throws IOException {

		BufferedReader outReader = new BufferedReader(new InputStreamReader(
				inputStream));

		while (true) {

			String errorLine = outReader.readLine();

			if (errorLine == null)
				break;

			redirectStream.write(errorLine.getBytes());
			redirectStream.write("\n".getBytes());

		}

	}

	public static boolean runGarbageCollection(String externalGitCommandPath,
			Repository repo, OutputStream redirectStream) {

		try {
			Process p = runGitCommand(externalGitCommandPath, repo, true, "gc");

			waitFor(p, redirectStream);

			return true;

		} catch (IOException e) {
			return false;
		} catch (InterruptedException e) {
			return false;
		}

	}

	public static boolean updateRef(String externalGitCommand, Repository repo,
			String absoluteRefName, ObjectId objectId, boolean force,
			OutputStream redirectStream) throws IOException {

		List<String> commandOptions = new ArrayList<>();

		commandOptions.add("branch");

		if (force)
			commandOptions.add("-f");

		commandOptions.add(absoluteRefName);
		commandOptions.add(objectId.getName());

		try {
			Process p = runGitCommand(repo, true, commandOptions);

			waitFor(p, redirectStream);

			if (p.exitValue() == 0)
				return true;
			else
				return false;
		} catch (InterruptedException e) {
		}
		
		return false;
	}

	/**
	 * Run the batch ref updates using the external git command instead of the
	 * JGit command.
	 * 
	 * @param externalGitCommand
	 * @param repo
	 * @param deferredReferenceDeletes
	 * @param redirectStream
	 * @throws IOException
	 */
	public static void batchRefUpdate(String externalGitCommand,
			Repository repo, List<ReceiveCommand> deferredReferenceDeletes,
			OutputStream redirectStream) throws IOException {

		for (ReceiveCommand receiveCommand : deferredReferenceDeletes) {

			String[] parts = receiveCommand.getRefName().split("/");

			String refName = parts[parts.length - 1];

			ObjectId refObjectId = receiveCommand.getNewId();

			List<String> commandOptions = new ArrayList<>();

			commandOptions.add(externalGitCommand);

			switch (receiveCommand.getType()) {

			case CREATE:
				commandOptions.add("branch");
				commandOptions.add(refName);
				commandOptions.add(refObjectId.getName());
				break;
			case DELETE:
				commandOptions.add("branch");
				commandOptions.add("-D");
				commandOptions.add(refName);
				break;
			case UPDATE:
			case UPDATE_NONFASTFORWARD:
				commandOptions.add("branch");
				commandOptions.add("-f");
				commandOptions.add(refName);
				commandOptions.add(refObjectId.getName());
				break;
			}

			try {
				Process p = runGitCommand(repo, true, commandOptions);

				waitFor(p, redirectStream);
			} catch (InterruptedException e) {
			}

		}
	}

}
