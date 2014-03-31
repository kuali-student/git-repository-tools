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
package org.kuali.student.git.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.kuali.student.common.io.IOUtils;
import org.kuali.student.git.model.ExternalModuleInfo;
import org.kuali.student.git.model.JGitTreeData;
import org.kuali.student.git.model.SvnRevisionMapper;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMapResults;
import org.kuali.student.git.model.tree.JGitTreeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class SvnExternalsUtils {
	
	private static final Logger log = LoggerFactory.getLogger(SvnExternalsUtils.class);
	
	
	/**
	 * Consume the full content of the input stream and parse out the svn:mergeinfo property content found.
	 * 
	 * You should use a bounded input stream sized for only the length of the svn:mergeinfo data to be consumed.
	 * 
	 * @param inputStream
	 * @return the list of brance merge info
	 * @throws IOException
	 */
	public static List<ExternalModuleInfo>extractExternalModuleInfoFromInputStream (long revision, String repositoryPrefixPath, InputStream inputStream) throws IOException {
		
		StringBuilder builder = new StringBuilder();
		
		while (true) {
			String line = IOUtils.readLine(inputStream, "UTF-8");
			
			if (line == null)
				break;
			
			if (line.isEmpty())
				continue;
			
			
			builder.append(line).append("\n");
		}
		
		return extractExternalModuleInfoFromString(revision, repositoryPrefixPath, builder.toString());
	}
	
	public static List<ExternalModuleInfo>extractExternalModuleInfoFromString (long revision, String repositoryPrefixPath, String inputString) {
	
		List<ExternalModuleInfo>bmiList = new LinkedList<>();
		
		String lines[] = inputString.split("\\n");
		
		for (String line : lines) {
		
			if (line.isEmpty() || line.charAt(0) == '#')
				continue; // skip to the next line
			
			String [] parts = line.split(" ");
			
			if (parts.length != 2)
				continue; // skip to the next line
			
			String moduleName = parts[0].trim();
			
			String branchPath = parts[1].trim();
			
			if (branchPath.startsWith(repositoryPrefixPath)) {
				// trim the leading slash if it exists.
				branchPath = branchPath.substring(repositoryPrefixPath.length()+1);
			}

			
			ExternalModuleInfo external = new ExternalModuleInfo(moduleName, branchPath, revision);
			
			bmiList.add(external);
	}
		
		return bmiList;
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
			List<ExternalModuleInfo> externals, SvnRevisionMapper revisionMapper) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		
		List<JGitTreeData> baseData = JGitTreeUtils.extractBaseTreeLevel(objectReader, commit);
		
		for (ExternalModuleInfo externalModuleInfo : externals) {
			
			String moduleName = externalModuleInfo.getModuleName();
			
			// find the commit that aligns with the path given at the current revision
			
			List<SvnRevisionMapResults> revisionBranches = revisionMapper.getRevisionBranches(externalModuleInfo.getRevision(), externalModuleInfo.getBranchPath());
			
			if (revisionBranches.size() != 1) {
				throw new RuntimeException("can only fuse to a single branch but " + externalModuleInfo.getBranchPath() + " matches " + revisionBranches.size());
			}
			
			SvnRevisionMapResults match = revisionBranches.get(0);
			
			ObjectId referencedCommitId = ObjectId.fromString(match.getRevMap().getCommitId());
			
			RevCommit referencedCommit = rw.parseCommit(referencedCommitId);
			
			ObjectId sourceTreeId = null;
			
			if (match.getSubPath().length() > 0) {
				
				String matchName = null;
				
				// referenced path is a subtree within a known commit
				
				TreeWalk tw = new TreeWalk (objectReader);
				
				tw.addTree(referencedCommit.getTree().getId());
				
				String pathParts[] = match.getSubPath().split("\\/");
				
				if (pathParts.length == 1) {
					// look in the top level commit tree for the subtree
					matchName = pathParts[0];
				}
				else {
					// create a filter to find the tree above and then find the tree we want at that level.
					
					String aboveLevelPath = StringUtils.join(pathParts, '/', 0, pathParts.length-1);
					
					tw.setFilter(PathFilter.create(aboveLevelPath));
					
					matchName = pathParts[pathParts.length-1];
					
				}
				
				
				if (tw.next()) {
					
					if (!tw.getFileMode(0).equals(FileMode.TREE))
						continue;
					
					String candidateName = tw.getNameString();
					
					if (candidateName.equals(matchName)) {
						// found the subtree
						sourceTreeId = tw.getObjectId(0);
						break;
					}
					
				}
				
				tw.release();
			}
			else {
				// use the top level commit tree
				sourceTreeId = referencedCommit.getTree().getId();
			}
			
			// then make a sub directory using that tree id.
			
			baseData.add(new JGitTreeData(moduleName, FileMode.TREE, sourceTreeId));
			
			
		}
		
		
		ObjectId fusedTreeId = JGitTreeUtils.createTree(baseData, inserter);

		/*
		 * 
		 */
		return fusedTreeId;
	}

	/**
	 * Split the fused commit into modules.size() tree's for each ExternalModuleInfo provided.
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
		
		TreeWalk tw = new TreeWalk(objectReader);
		
		tw.addTree(fusedCommit.getTree().getId());
		
		// walk over the top level directories in this commit
		tw.setRecursive(false);
		
		while (tw.next()) {
			
			if (!tw.getFileMode(0).equals(FileMode.TREE)) 
				continue;
			
			String candidateName = tw.getNameString();
			
			// find the external if it exists for this name
			for (ExternalModuleInfo external : modules) {
				
				if (candidateName.equals(external.getModuleName())) {
					// match found
					ObjectId moduleTreeId = tw.getObjectId(0);
					splitTreeMap.put(external.getModuleName(), moduleTreeId);
					
					break;
				}
			}
			
			
			
		}
		
		tw.release();
		
		
		return splitTreeMap;
		
	}
	
}
