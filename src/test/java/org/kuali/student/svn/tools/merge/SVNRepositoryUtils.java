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
package org.kuali.student.svn.tools.merge;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.SVNExternal;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.admin.SVNAdminClient;

/**
 * @author Kuali Student Team
 *
 */
public class SVNRepositoryUtils {

		private static final String MAIN_WC = "main-wc";

		private static final String REPO_NAME = "repo";
		
		private static final File BUILD_DIR = new File (System.getProperty("user.dir"), "target");
		
		
		/**
		 * 
		 */
		private SVNRepositoryUtils() {
			
		}
		
		private static File getRepositoryFile (String repositoryName) {
			File repositoryFile = new File (new File( BUILD_DIR, repositoryName), REPO_NAME);
			
			repositoryFile.mkdirs();
			
			return repositoryFile;
		}
		/**
		 * Create a new repository of the given name
		 * 
		 * @param repositoryName
		 * @return
		 * @throws SVNException
		 */
		public static SVNURL createRepository (String repositoryName) throws SVNException {

		      return SVNRepositoryFactory.createLocalRepository(getRepositoryFile(repositoryName), true , true );
		}
		
		/**
		 * Delete the named repository from the temporary directory
		 * 
		 * @param repositoryName
		 * @throws IOException
		 */
		public static void deleteRepository(String repositoryName) throws IOException {
			FileUtils.deleteDirectory(getRepositoryFile(repositoryName));
		}
		
		/**
		 * Delete the repository area target/$repositoryName
		 * 
		 * @param repositoryName
		 * @throws IOException 
		 */
		public static void deleteRepositoryArea (String repositoryName) throws IOException {
			FileUtils.deleteDirectory(new File( BUILD_DIR, repositoryName));
		}
		
		public static void deleteRepositoryWorkingCopy(String repositoryName, String workingCopyName) throws IOException {
			FileUtils.deleteDirectory(getWorkingCopyFile(repositoryName, workingCopyName));
		}
		
		public static void deleteRepositoryWorkingCopy(String testRespositoryName) throws IOException {

			deleteRepositoryWorkingCopy(testRespositoryName, testRespositoryName + "-wc");
		}
		
		private static File getWorkingCopyFile(String repositoryName,
				String workingCopyName) {
			
			File workingCopyFile = new File (new File( BUILD_DIR, repositoryName), workingCopyName);
			
			return workingCopyFile;
		}
		
		private static boolean createModuleStructure (File workingCopy, String moduleName) {
			
			File module = new File (workingCopy, moduleName);
		
			if (!module.mkdir()) {
				return false;
			}
			
			File tags = new File (module, "tags");
			File branches = new File (module, "branches");
			File trunk = new File (module, "trunk");
			
			if (tags.mkdir() && branches.mkdir() && trunk.mkdir())
				return true;
			else
				return false;
			
		}
		public static void createExternalsBaseStructure (String repositoryName) throws IOException {
			
			// checkout at the root to create the initial structure
			File workingCopy = checkOut(repositoryName, null, MAIN_WC, null, null);
			
			// make an aggregate
			createModuleStructure(workingCopy, "aggregate");
			
			// make a module 1
			createModuleStructure(workingCopy, "module1");
			
			// make a module 2
			createModuleStructure(workingCopy, "module2");
			
			// create some files
			
			
//			SVNUtils.getInstance().addFiles(new File (workingCopy, "aggregate"), null, null);
//			SVNUtils.getInstance().addFiles(new File (workingCopy, "module1"), null, null);
//			SVNUtils.getInstance().addFiles(new File (workingCopy, "module2"), null, null);
//			
			
			List<SVNExternal> externals = new ArrayList<SVNExternal>();
			
//			externals.add(new SVNExternal("file://" + getRepositoryPath(repositoryName, "/module1/trunk"), "module1", null));
//			externals.add(new SVNExternal("file://" + getRepositoryPath(repositoryName, "/module2/trunk"), "module2", null));
			
//			SVNCommitInfo commitInfo = SVNUtils.getInstance().commit(workingCopy, "initial commit", null, null);
//			
//			commitInfo = SVNUtils.getInstance().setExternals(getRepositoryPath(repositoryName, "/aggregate/trunk"), externals);
			
			// clean up the working copy
			deleteRepositoryWorkingCopy(repositoryName, MAIN_WC);
			
			
		}

		
		
		public static String getRepositoryPath (String repositoryName, String repositorySubPath) {
			String repo = getRepositoryFile(repositoryName).getAbsolutePath();
			
			String repoPath = repo;
			
			if (repositorySubPath != null && !repositorySubPath.isEmpty()) {
				repoPath = repo + "/" + repositorySubPath;
			}
			
			return repoPath;
			
		}

		public static File checkOut(String repositoryName, String repositorySubPath, String workingCopyName, String userName, String password) {
			
			File workingCopy = getWorkingCopyFile(repositoryName, workingCopyName);
			
			String repoPath = getRepositoryPath(repositoryName, repositorySubPath);
			
			long rev = checkout(repoPath, workingCopy, null, null);
			
			return workingCopy;
		}
		private static long checkout(String repoPath, File workingCopy, String username, String password) {
			try {
				SVNClientManager manager = SVNClientManager.newInstance(null, username, password);
				SVNUpdateClient client = manager.getUpdateClient();
				client.setIgnoreExternals(false);
				SVNURL svnUrl = getSvnUrl(repoPath);
				
				return client.doCheckout(svnUrl, workingCopy, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false);
			} catch (SVNException e) {
				throw new IllegalStateException(e);
			}
		}

		public static File checkOut(String repositoryName, String repositorySubPath, String	userName, String password) {
			return checkOut(repositoryName, repositorySubPath, repositoryName + "-wc", userName, password);
		}
		
		public static void addFiles (File workingCopyPath, String username, String password) {
			
			try {
				SVNClientManager manager = SVNClientManager.newInstance(null, username, password);
				SVNWCClient client = manager.getWCClient();
				client.setIgnoreExternals(false);
				client.doAdd(workingCopyPath, true, false, true, SVNDepth.INFINITY, true, true);
				
			} catch (SVNException e) {
				throw new IllegalStateException(e);
			}
		}
		
		public static SVNCommitInfo commit(List<File> workingCopyPaths, String message, String username, String password) {
			File[] fileArray = workingCopyPaths.toArray(new File[workingCopyPaths.size()]);
			return commit(fileArray, message, username, password);
		}

		public static SVNCommitInfo commit(File[] workingCopyPaths, String message, String username, String password) {
			try {
				SVNClientManager manager = SVNClientManager.newInstance(null, username, password);
				SVNCommitClient client = manager.getCommitClient();
				client.setIgnoreExternals(false);
				return client.doCommit(workingCopyPaths, true, message, null, null, true, false, SVNDepth.INFINITY);
			} catch (SVNException e) {
				throw new IllegalStateException(e);
			}
		}
		
		public static void dump(String repositoryName, String dumpFileName,
			String username, String password, long startRevision,
			long endRevision, boolean delta)
			throws SVNException, IOException {
		
			dump(getRepositoryFile(repositoryName), dumpFileName, username, password, SVNRevision.create(startRevision), SVNRevision.create(endRevision), true, delta);
			
		}
		
	public static void dump(File repository, String dumpFileName,
			String username, String password, SVNRevision startRevision,
			SVNRevision endRevision, boolean isIncremental, boolean useDeltas)
			throws SVNException, IOException {

		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(dumpFileName));

		SVNClientManager manager = SVNClientManager.newInstance(null, username,
				password);

		SVNAdminClient client = manager.getAdminClient();

		client.doDump(repository, out, startRevision, endRevision,
				isIncremental, useDeltas);

		out.close();
	}

		public static SVNCommitInfo commit(File workingCopyPath, String message, String username, String password) {
			return commit(new File[] { workingCopyPath }, message, username, password);
		}
			
		@SuppressWarnings("deprecation")
		protected static SVNURL getSvnUrl(String url) {
			try {
				return SVNURL.parseURIDecoded(url);
			} catch (SVNException e) {
				
				try {
					return SVNURL.fromFile(new File (url));
				} catch (SVNException e1) {
					throw new IllegalStateException(e1);
				}
				
			}
		}
		
		public static SVNCommitInfo copy(String username, String password, String source, String destination, String commitMessage) {
			SVNClientManager manager = SVNClientManager.newInstance(null, username, password);
			SVNCopyClient client = manager.getCopyClient();
			SVNURL dstUrl = getSvnUrl(destination);
			SVNURL srcUrl = getSvnUrl(source);
			SVNRevision revision = SVNRevision.HEAD;
			
			SVNCopySource svnCopySource = new SVNCopySource(SVNRevision.HEAD, revision, srcUrl);
			SVNCopySource[] sources = new SVNCopySource[] { svnCopySource };
			try {
				return client.doCopy(sources, dstUrl, false, true, true, commitMessage, null);
			} catch (SVNException e) {
				throw new IllegalStateException(e);
			}
		}
		

}
