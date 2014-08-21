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
package org.kuali.student.git.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.kuali.student.common.io.IOUtils;
import org.kuali.student.git.model.branch.large.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.branch.utils.GitBranchUtils;
import org.kuali.student.git.model.branch.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.kuali.student.git.model.tree.GitTreeNodeData;
import org.kuali.student.git.model.tree.JGitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.model.tree.utils.JGitTreeUtils;
import org.kuali.student.svn.model.ExternalModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class ExternalModuleUtils {
	
	private static final String REMAINDER = "remainder";

	private static final String FUSION_MAVEN_PLUGIN_DAT = "fusion-maven-plugin.dat";

	private static final String HTTPS_URL = "https://";

	private static final String HTTP_URL = "http://";
	
	private static final Logger log = LoggerFactory.getLogger(ExternalModuleUtils.class);
	
	
	/**
	 * Consume the full content of the input stream and parse out the svn:externals property content found.
	 * 
	 * You should use a bounded input stream sized for only the length of the svn:externals data to be consumed.
	 * 
	 * @param inputStream
	 * @return the list of brance merge info
	 * @throws IOException
	 */
	public static List<ExternalModuleInfo>extractExternalModuleInfoFromSvnExternalsInputStream (long revision, String repositoryPrefixPath, InputStream inputStream) throws IOException {
		
		StringBuilder builder = new StringBuilder();
		
		while (true) {
			String line = IOUtils.readLine(inputStream, "UTF-8");
			
			if (line == null)
				break;
			
			if (line.isEmpty())
				continue;
			
			
			builder.append(line).append("\n");
		}
		
		return extractExternalModuleInfoFromSvnExternalsString(revision, repositoryPrefixPath, builder.toString());
	}
	
	public static interface IBranchHeadProvider {
		
		public ObjectId getBranchHeadObjectId(String branchName);
		
	}
	
	public static String createFusionMavenPluginDataFileString (long currentRevision, final Repository repo, List<ExternalModuleInfo>externals, ILargeBranchNameProvider largeBranchNameProvider) {
		return createFusionMavenPluginDataFileString(currentRevision, new IBranchHeadProvider() {
			
			@Override
			public ObjectId getBranchHeadObjectId(String branchName) {
				
				ObjectId branchHead = null;
				try {

					// use the branch head
					Ref branchRef = repo.getRef(Constants.R_HEADS + branchName);

					if (branchRef != null)
						branchHead = branchRef.getObjectId();
					else {
						log.warn(
								"createFusionMavenPluginDataFileString failed to resolve branch for: {}",
								branchName);
					}
					
				} catch (IOException e) {
					// intentionally fall through
				}
				
				return branchHead;
			}
		}, externals, largeBranchNameProvider);
	}
	public static String createFusionMavenPluginDataFileString(long currentRevision, IBranchHeadProvider branchHeadProvider, 
			List<ExternalModuleInfo> externals, ILargeBranchNameProvider largeBranchNameProvider) {
		
		StringBuilder builder = new StringBuilder();
		
		for (ExternalModuleInfo external : externals) {
			
			String externalModule = external.getModuleName();
			String externalBranchPath = external.getBranchPath();
			long externalRevision = external.getRevision();
			
			builder.append("# module = " + externalModule + " branch Path = " + externalBranchPath + " revision = " + externalRevision + "\n");
			
			String branchName = GitBranchUtils.getCanonicalBranchName(externalBranchPath, externalRevision, largeBranchNameProvider);
			
			ObjectId branchHead = branchHeadProvider.getBranchHeadObjectId(branchName);
			
			if (branchHead != null) {
				// store the branch head		
				external.setBranchHeadId(branchHead);
			}
			
			builder.append(externalModule + "::" + branchName + "::" + (branchHead==null?"UNKNOWN":branchHead.name()) + "\n");
			
		}
		
		return builder.toString();
	}
	
	public static List<ExternalModuleInfo> extractFusionMavenPluginData(InputStream input) throws IOException {
		
		List<String>lines = org.apache.commons.io.IOUtils.readLines(input);
		
		return extractFusionMavenPluginData(lines);
		
	}
	public static List<ExternalModuleInfo> extractFusionMavenPluginData(List<String>lines) {
		
		List<ExternalModuleInfo>externals = new ArrayList<>();

		for (int i = 0; i < lines.size(); i += 2) {
			
			String commentLine = lines.get(i);
			
			String commentParts[] = commentLine.split("revision = ");
			
			String branchPathParts[] = commentParts[0].split("branch Path = ");
			
			String branchPath = branchPathParts[1].trim();
			
			String revisionString = commentParts[1].trim();
			
			String dataLine = lines.get(i+1);
			
			String dataParts[] = dataLine.split("::");
			
			String moduleName = dataParts[0].trim();
			
			String branchName = dataParts[1].trim();
			
			String branchHeadObjectId = dataParts[2];
			
			long revision = 0;
			
			try {
				
				revision =Long.parseLong(revisionString);
			}
			catch (NumberFormatException e) {
				// intentionally do nothing, use revision = 0
			}
			
			String convertedBranchPath = GitBranchUtils.getBranchPath(branchName, revision, new LargeBranchNameProviderMapImpl());
			
			ExternalModuleInfo emi = new ExternalModuleInfo(moduleName, convertedBranchPath, revision);
			
			if (!branchHeadObjectId.equals("UNKNOWN")) {
				emi.setBranchHeadId(ObjectId.fromString(branchHeadObjectId));	
			}
			
			externals.add(emi);
		}
		
		return externals;
		
	}
	
	/**
	 * Extract from the svn:externals format string
	 * 
	 * @param revision
	 * @param repositoryPrefixPath
	 * @param inputString
	 * @return
	 */
	public static List<ExternalModuleInfo>extractExternalModuleInfoFromSvnExternalsString (long revision, String repositoryPrefixPath, String inputString) {
	
		boolean securePrefixPath = false;
		if (repositoryPrefixPath.startsWith(HTTPS_URL)) {
			securePrefixPath = true;
		}
		
		List<ExternalModuleInfo>externalsList = new LinkedList<>();
		
		if (inputString == null)
			return externalsList;
		
		String lines[] = inputString.split("\n");
		
		for (String line : lines) {
		
			if (line.isEmpty() || line.charAt(0) == '#')
				continue; // skip to the next line
			
			
			String [] parts = line.replace("\r", "").split(" ");
			
			if (parts.length != 2)
				continue; // skip to the next line
			
			int branchPathIndex = determineBranchPathIndex (parts, repositoryPrefixPath, securePrefixPath);
			
			String moduleName = null;
			String branchPath = null;
			
			if (branchPathIndex == 0) {
				branchPath = parts[0].trim();
				moduleName = parts[1].trim();
			}
			else {
				branchPath = parts[1].trim();
				moduleName = parts[0].trim();
			}
			
			if (securePrefixPath && branchPath.startsWith(HTTP_URL)) {
				/*
				 * Normalize to https if the prefix is also https.
				 */
				branchPath = HTTPS_URL + branchPath.substring(HTTP_URL.length());
			}
			else if (!securePrefixPath && branchPath.startsWith(HTTPS_URL)) {
				/*
				 * Normalize to http if the prefix starts with http.
				 */
				branchPath = HTTP_URL + branchPath.substring(HTTPS_URL.length());
			}
			
			if (branchPath.startsWith(repositoryPrefixPath)) {
				// trim the leading slash if it exists.
				branchPath = branchPath.substring(repositoryPrefixPath.length()+1);
			}
			else if (branchPath.startsWith("^")){
				// relative external case
				if (branchPath.startsWith("/", 1)) {
					// trim ^/ from the front of the branch path
					branchPath = branchPath.substring(2);
				}
				else {
					// trim ^ from the front of the path
					branchPath = branchPath.substring(1);
				}
				
			}

			
			ExternalModuleInfo external = new ExternalModuleInfo(moduleName, branchPath, revision);
			
			externalsList.add(external);
	}
		
		return externalsList;
	}

	/*
	 * Figure out which part is the branch path part.
	 * 
	 * It can include the url but since 1.5 it can also be relative.
	 * 
	 */
	
	private static boolean matchesSvnURL (String part, String repositoryPrefixPath) {
		
		if (part.startsWith(repositoryPrefixPath))
			return true;
		
		if (part.startsWith("^"))
			return true;
		
		return false;
	}
	
	private static int determineBranchPathIndex(String[] parts, String repositoryPrefixPath, boolean securePrefixPath) {
		
		String firstCandidate = parts[0].trim();
		
		String secondCandidate = parts[1].trim();
		
		if (matchesSvnURL(firstCandidate, repositoryPrefixPath))
			return 0;
		
		else if (matchesSvnURL(secondCandidate, repositoryPrefixPath))
			return 1;
		else
			return -1; // neither matched.
		
	}
	/**
	 * Create a new tree that is the result of the fusion of the existing commit with the other modules indicated in the externals list.
	 * 
	 * We Resolve the branch heads using the SvnRevisionMapper
	 * @param objectReader
	 * @param inserter
	 * @param rw
	 * @param commit
	 * @param externals
	 * @param revisionMapper
	 * @return
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	public static AnyObjectId createFusedTree(ObjectReader objectReader,
			ObjectInserter inserter, RevWalk rw, RevCommit commit,
			List<ExternalModuleInfo> externals) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		// default is to include the fusion-maven-plugin.dat if it is in the directory.
		return createFusedTree(objectReader, inserter, rw, commit, externals, false);
	}
	
	public static AnyObjectId createFusedTree(ObjectReader objectReader,
			ObjectInserter inserter, RevWalk rw, RevCommit commit,
			List<ExternalModuleInfo> externals, boolean excludeFusionPluginData) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		
		List<JGitTreeData> baseData = JGitTreeUtils.extractBaseTreeLevel(objectReader, commit);
		
		for (ExternalModuleInfo externalModuleInfo : externals) {
			
			String moduleName = externalModuleInfo.getModuleName();
			
			ObjectId referencedCommitId = externalModuleInfo.getBranchHeadId();
			
			if (referencedCommitId != null) {
			
				RevCommit referencedCommit = rw.parseCommit(referencedCommitId);
			
				ObjectId sourceTreeId = referencedCommit.getTree().getId();
			
				baseData.add(new JGitTreeData(moduleName, FileMode.TREE, sourceTreeId));
			}
			else 
				log.warn("unknown branch head object id for module {}", moduleName);
			
			
		}
		
		if (excludeFusionPluginData) {

			int targetIndex = -1;
			
			for (int i = 0; i < baseData.size(); i++) {
				
				JGitTreeData data = baseData.get(i);
				
				if (data.getName().equals(FUSION_MAVEN_PLUGIN_DAT)) {
					targetIndex = i;
					break;
				}
					
			}
			
			if (targetIndex != -1)
				baseData.remove(targetIndex);
		}
		
		ObjectId fusedTreeId = JGitTreeUtils.createTree(baseData, inserter);

		/*
		 * 
		 */
		return fusedTreeId;
	}
	
	/**
	 * Look at the given commit and if there is a fusion-maven-plugin.dat in the root of its tree then load and return the contents.
	 * 
	 * @param commit
	 * @return the ExternalsModuleInfo's found, an empty list if none are found.
	 * @throws IOException 
	 * @throws CorruptObjectException 
	 * @throws IncorrectObjectTypeException 
	 * @throws MissingObjectException 
	 */
	public static List<ExternalModuleInfo>findExternalModulesForCommit (Repository repo, RevCommit commit) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		List<ExternalModuleInfo> modules = new LinkedList<ExternalModuleInfo>();
		
		GitTreeProcessor treeProcessor = new GitTreeProcessor(repo);
		
		GitTreeNodeData tree = treeProcessor.extractExistingTreeData(commit.getTree().getId(), "");
		
		ObjectId fusionDataBlobId = tree.find(repo, "fusion-maven-plugin.dat");
		
		if (fusionDataBlobId == null)
			return modules;
		
		ObjectReader reader = repo.newObjectReader();
		
		modules = ExternalModuleUtils.extractFusionMavenPluginData(reader.open(fusionDataBlobId, Constants.OBJ_BLOB).openStream());
		
		reader.release();
		
		return modules;
		
	}

	/**
	 * Split the fused commit into modules.size() tree's for each ExternalModuleInfo provided.
	 * 
	 * Put the remainder of the tree (original - external modules that were removed) under the key 'remainder'.
	 * 
	 * @param fusedCommitId
	 * @return a map of the branch name to split tree id.
	 * @throws IOException 
	 * @throws IncorrectObjectTypeException 
	 * @throws MissingObjectException 
	 */
	public static Map<String, ObjectId> splitFusedTree(ObjectReader objectReader, ObjectInserter inserter, RevWalk rw, ObjectId fusedCommitId, List<ExternalModuleInfo>modules) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		Map<String, ObjectId> splitTreeMap = new HashMap<>();
		
		RevCommit fusedCommit = rw.parseCommit(fusedCommitId);
		
		List<JGitTreeData> baseData = JGitTreeUtils.extractBaseTreeLevel(objectReader, fusedCommit);
		
		Iterator<JGitTreeData> iter = baseData.iterator();
		
		while (iter.hasNext()) {
			
			JGitTreeData data = iter.next();
			
			if (!data.getFileMode().equals(FileMode.TREE)) 
				continue;
			
			String candidateName = data.getName();
			
			// find the external if it exists for this name
			for (ExternalModuleInfo external : modules) {
				
				if (candidateName.equals(external.getModuleName())) {
					// match found
					ObjectId moduleTreeId = data.getObjectId();
					splitTreeMap.put(external.getModuleName(), moduleTreeId);
					iter.remove();
					break;
				}
			}
			
			
		}
		
		ObjectId remainderTreeId = JGitTreeUtils.createTree(baseData, inserter);
		
		splitTreeMap.put(REMAINDER, remainderTreeId);
		
		return splitTreeMap;
		
	}
	public static String createFusionMavenPluginDataFileString(Repository repo,
			List<ExternalModuleInfo> externals) {
		return createFusionMavenPluginDataFileString(0L, repo, externals, new LargeBranchNameProviderMapImpl());
		
	}
	
}
