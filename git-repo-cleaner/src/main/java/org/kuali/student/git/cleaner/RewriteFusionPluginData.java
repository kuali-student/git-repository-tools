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
import java.util.Arrays;
import java.util.Collection;
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
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceiveCommand.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.student.git.cleaner.model.ObjectIdTranslation;
import org.kuali.student.git.cleaner.model.ObjectIdTranslationService;
import org.kuali.student.git.cleaner.model.ObjectTranslationDataSource;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.SvnExternalsUtils;
import org.kuali.student.git.model.graft.GitGraft;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.GitTreeData.GitTreeDataVisitor;
import org.kuali.student.git.model.tree.GitTreeNodeInitializerImpl;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.utils.ExternalGitUtils;
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
			
			ObjectLoader loader = getRepo().newObjectReader().open(fusionPluginDataBlobId, Constants.OBJ_BLOB);
			
			// rewrite the data here
			List<ExternalModuleInfo> fusionData = SvnExternalsUtils.extractFusionMavenPluginData(loader.openStream());
			
			
			for (ExternalModuleInfo fusion : fusionData) {
				
				ObjectId commitId = fusion.getBranchHeadId();
				
				if (commitId == null) {
					log.warn("commit Id: " + commit.getId().name() + " is missing branch head for module: " + fusion.getModuleName());
					continue;
				}
				
				// check where this originates from
				ObjectId newCommitId = this.translationService.translateObjectId(commitId);
				
				if (this.originalCommitIdToNewCommitIdMap.containsKey(newCommitId)) {
					// the old commit has been rewritten as part of the fusion rewriting
					// so use the most recent id instead.
					newCommitId = this.originalCommitIdToNewCommitIdMap.get(newCommitId);
				}
				
				if (newCommitId != null && !newCommitId.equals(commitId)) {
					
					fusion.setBranchHeadId(newCommitId);
					
					changesToBeCommitted = true;
				}
				
				
			}
			
			if (changesToBeCommitted) {
				
				// save it into the tree
				String updatedFusionData = SvnExternalsUtils.createFusionMavenPluginDataFileString(getRepo(), fusionData);
				
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
