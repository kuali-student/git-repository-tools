/**
 * 
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

import org.eclipse.jgit.lib.Repository;
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
	 * @param redirectStream if not null then output from the sub process will be written here.
	 * @return
	 */
	public static boolean checkoutBranch(String externalGitCommandPath,
			Repository repo, String branchName, boolean force,  OutputStream redirectStream) {

		try {

			Process p = null;
			
			if (force) {
				p = runGitCommand(externalGitCommandPath, repo, false,
						"checkout", branchName, "--force");
			}
			else {
				p = runGitCommand(externalGitCommandPath, repo, false,
					"checkout", branchName);
			}
			
			int result = waitFor(p, redirectStream);

			if (result == 0)
				return true;
			else {
				readStream (p.getErrorStream(), redirectStream);
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
			Repository repo, boolean inGitMetaDirectory, String ...gitCommandArgs) throws IOException {

		List<String>commandArgs = new ArrayList<>();
		
		commandArgs.add(externalGitCommandPath);
		commandArgs.addAll(Arrays.asList(gitCommandArgs));
		
		File gitDirectory = null;
		
		if (inGitMetaDirectory)
			gitDirectory = repo.getDirectory();
		else 
			gitDirectory = repo.getWorkTree();
		
		// inherit the parent environment
		// locate in the working copy of the gir directory
		Process p = Runtime.getRuntime().exec(commandArgs.toArray(new String[] {}), null, gitDirectory);
		
		return p;
	}

	private static int waitFor(Process p, OutputStream redirectStream)
			throws InterruptedException, IOException {


		if (redirectStream != null) {

			readStream (p.getInputStream(), redirectStream);
		}
		
		return p.waitFor();

	}

	private static void readStream(InputStream inputStream,
			OutputStream redirectStream) throws IOException {
		
		BufferedReader outReader = new BufferedReader(
				new InputStreamReader(inputStream));

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

}
