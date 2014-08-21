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
package org.kuali.student.git.cleaner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.kuali.student.cleaner.model.bitmap.RevCommitBitMapIndex;
import org.kuali.student.cleaner.model.sort.FusionAwareTopoSortComparator;
import org.kuali.student.git.cleaner.model.CommitDependency;
import org.kuali.student.git.cleaner.model.ObjectIdTranslation;
import org.kuali.student.git.cleaner.model.ObjectIdTranslationService;
import org.kuali.student.git.cleaner.model.ObjectTranslationDataSource;
import org.kuali.student.git.model.ExternalModuleUtils;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.svn.model.ExternalModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * After a repository has been split into pieces the fusion-maven-plugin.dat file may refer to stale object ids.
 * 
 * This program will rewrite the commits and related commits to those that contain the fusion-maven-plugin.dat file to use the new object ids.
 * 
 * If the to be fused id refers to an object id that is on the left side of the cut line a temporary right side clone of that object will be created.
 * 
 * 
 * @author ocleirig
 * 
 */
public class RewriteFusionPluginData extends AbstractRepositoryCleaner {

	private static final Logger log = LoggerFactory
			.getLogger(RewriteFusionPluginData.class);

	private ObjectIdTranslationService translationService;
		
	
	/**
	 * 
	 */
	public RewriteFusionPluginData() {
		// TODO Auto-generated constructor stub
	}

	
	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.RepositoryCleaner#validateArgs(java.lang.String[])
	 */
	@Override
	public void validateArgs(List<String> args) throws Exception {

		if (args.size() != 2 && args.size() != 4) {
			log.error("USAGE: <right git repository meta directory> <replaced objects files> [<branchRefSpec> <git command path>]");
			log.error("\t<right git repo meta directory> : the path to the meta directory of the right (target) git repository");
			log.error("\t<grafts file> : existing grafts file created when the left and right repo's were split");
			log.error("\t<replaced objects files> : double colon seperated list of object-translations files. specified in the order of the translations");
			log.error("\t<branchRefSpec> : git refspec from which to source the graph to be rewritten");
			log.error("\t<git command path> : the path to a native git ");
			throw new IllegalArgumentException("invalid arguments");
		}
		
		setRepo(GitRepositoryUtils.buildFileRepository(
				new File (args.get(0)).getAbsoluteFile(), false));
		
		
		List<ObjectIdTranslation>dataSources = new ArrayList<ObjectIdTranslation>();
		
		String objectTranslationDataSources[] = args.get(1).split("::");
		
		
		for (String objectTranslationDataFile : objectTranslationDataSources) {
			
			ObjectTranslationDataSource dataSource = new ObjectTranslationDataSource(objectTranslationDataFile);
			
			// format: new object id <space> original object id
			
			List<String> newToOriginalObjectLines = FileUtils.readLines(new File (objectTranslationDataFile));
			
			for (String rightToLeftObjectLine : newToOriginalObjectLines) {
				
				if (rightToLeftObjectLine.trim().length() == 0 || rightToLeftObjectLine.startsWith("#"))
					continue; // skip blank lines and comments.
				
				String parts[] = rightToLeftObjectLine.split(" ");
				
				dataSource.storeObjectTranslation(parts[1].trim(), parts[0].trim());
				
			}
			
			dataSources.add(dataSource);
		}
		
		translationService = new ObjectIdTranslationService(dataSources);
		
		if (args.size() >= 3)
			setBranchRefSpec(args.get(2).trim());
		
		
		if (args.size() == 4)
			setExternalGitCommandPath(args.get(3).trim());
		
	}

	/*
	 * If all of the direct dependencies are contained in the aggregated commit map then return true and store the aggregated dependencies  into the set provided.
	 */
	private boolean aggregate (Set<ObjectId>aggregateIntoSet, Set<ObjectId>directDependencies, Map<ObjectId, Set<ObjectId>>aggregatedCommitDependenciesMap) {

		boolean aggregateDataExists = true;
		
		for (ObjectId objectId : directDependencies) {
		
			if (!aggregatedCommitDependenciesMap.containsKey(objectId))
				aggregateDataExists = false;
			
		}
		
		if (aggregateDataExists) {
			
			// just accumulate
			for (ObjectId objectId : directDependencies) {
				
				aggregateIntoSet.addAll(aggregatedCommitDependenciesMap.get(objectId));
				
			}
			
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#provideRevCommitIterator(java.util.Iterator)
	 */
	@Override
	protected Iterator<RevCommit> provideRevCommitIterator(
			Iterator<RevCommit> iterator) {
		
		try {
			
			RevCommitBitMapIndex index = new RevCommitBitMapIndex(getRepo(), translationService, iterator);
			
			List<RevCommit>commitList = new LinkedList<RevCommit>(index.getRevCommitList());
			
			// compute the aggregated dependencies
			log.info("sorting " + commitList.size() + " commits"); 
			
			Collections.sort(commitList, new FusionAwareTopoSortComparator(index));
			
			PrintWriter orderedCommitsFile = new PrintWriter("rewrite-fusion-data-ordered-commits-"+dateString+".txt");
			
			for (RevCommit revCommit : commitList) {
			
				orderedCommitsFile.println(revCommit.getId().name());
			}
			
			orderedCommitsFile.close();
			
			return commitList.iterator();
		} catch (Exception e) {
			throw new RuntimeException ("RewriteFusionPluginData.provideRevCommitIterator(): failed ", e);
		}
	}


	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#processCommitTree(org.eclipse.jgit.lib.ObjectId, org.kuali.student.git.model.tree.GitTreeData)
	 */
	@Override
	protected boolean processCommitTree(RevCommit commit, GitTreeData tree)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException {
		
		ObjectId fusionPluginDataBlobId = tree.find(getRepo(), "fusion-maven-plugin.dat");
		
		boolean changesToBeCommitted = false;
		
		if (fusionPluginDataBlobId != null) {
		
			if (commit.getFullMessage().contains("@71661")) 
				log.info("found target commit");
			
			ObjectLoader loader = getRepo().newObjectReader().open(fusionPluginDataBlobId, Constants.OBJ_BLOB);
			
			// rewrite the data here
			List<ExternalModuleInfo> fusionData = ExternalModuleUtils.extractFusionMavenPluginData(loader.openStream());
			
			
			for (ExternalModuleInfo fusion : fusionData) {
				
				ObjectId commitId = fusion.getBranchHeadId();
				
				if (commitId == null) {
					log.warn("commit Id: " + commit.getId().name() + " is missing branch head for module: " + fusion.getModuleName() + " branch: " + fusion.getBranchPath());
					continue;
				}
				
				if (commitId.name().startsWith("8b608b677a5090080014374d11c0dba909"))
					log.info("target commit: " + commit.getId() + " refers to external: 8b608b677a5090080014374d11c0dba909");
				
				// check where this originates from
				ObjectId newCommitId = this.translationService.translateObjectId(commitId);

				// will exist if the newCommitId from a previous rewite has been rewritted during the current rewrite
				ObjectId currentlyChangedId =  this.originalCommitIdToNewCommitIdMap.get(newCommitId);
				
				if (currentlyChangedId != null)
					newCommitId = currentlyChangedId;
				
				if (newCommitId != null && !newCommitId.equals(commitId)) {
					
					fusion.setBranchHeadId(newCommitId);
					
					changesToBeCommitted = true;
					
					if (currentlyChangedId != null && !super.processedCommits.contains(newCommitId)) {
						log.warn("repo is missing a commit for " + newCommitId);
					}
				}
				else {
					// make sure that the commitId is still valid if its been changed
					
					if (super.originalCommitIdToNewCommitIdMap.keySet().contains(commitId) && !super.processedCommits.contains(commitId)) {
						log.warn("repo is missing a commit for " + commitId);
					}
					
				}
				
				
			}
			
			if (changesToBeCommitted) {
				
				// save it into the tree
				String updatedFusionData = ExternalModuleUtils.createFusionMavenPluginDataFileString(fusionData);
				
				ObjectId updatedBlobId = inserter.insert(Constants.OBJ_BLOB, updatedFusionData.getBytes());
				
				tree.addBlob("fusion-maven-plugin.dat", updatedBlobId);
		
				return true;
			}
			else
				return false;
			
		}
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#getFileNameSuffix()
	 */
	@Override
	protected String getFileNameSuffix() {
		return "rewrite-fusion-plugin-data";
	}


	

}
