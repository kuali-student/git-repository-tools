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
package org.kuali.student.cleaner.model.bitmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.kuali.student.git.cleaner.model.CommitDependency;
import org.kuali.student.git.cleaner.model.ObjectIdTranslation;
import org.kuali.student.git.model.ExternalModuleUtils;
import org.kuali.student.svn.model.ExternalModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.javaewah.EWAHCompressedBitmap;

/**
 * @author ocleirig
 * 
 * 
 * Take in the list of revcommits and make a bitmap index at each commit dependency node.
 * 
 * Then depth first search the commit dependency graph .or ing the bitmaps.  If this works we might be able to cache the .ored bitmap.
 * 
 * the index of the bitmap comes from the original List<RevCommit>
 */
public class RevCommitBitMapIndex {

	private static final Logger log = LoggerFactory.getLogger(RevCommitBitMapIndex.class);
	private Repository repo;
	private ObjectIdTranslation translationService;
	
	private List<RevCommit>revCommitList = new LinkedList<RevCommit>();
	
	private Map<Integer, RevCommit>bitsetTranslationMap = new HashMap<Integer, RevCommit>();
	
	private Map<ObjectId, Integer>objectIdToBitsetIndexMap = new HashMap<ObjectId, Integer>();
	
	private Map<ObjectId, Bitmap>objectIdToBitmap = new HashMap<ObjectId, Bitmap>();
	private Map<ObjectId, CommitDependency> commitToDependencyMap;
	
	private Map<ObjectId, Bitmap>objectIdToAggregatedBitmap = new HashMap<ObjectId, Bitmap>();
	
	/**
	 * @throws IOException 
	 * @throws CorruptObjectException 
	 * @throws IncorrectObjectTypeException 
	 * @throws MissingObjectException 
	 * 
	 */
	public RevCommitBitMapIndex(Repository repo, ObjectIdTranslation translationService, Iterator<RevCommit>commits) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		super();
		this.repo = repo;
		this.translationService = translationService;
		
		index(commits);
		
	}
	
	
	private void index (Iterator<RevCommit> iterator) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		List<RevCommit>fusionTopoOrderedList = new ArrayList<RevCommit>();
		
		commitToDependencyMap = new HashMap<ObjectId, CommitDependency>();
		
		HashMap<ObjectId, Set<ObjectId>> commitDependenciesMap = new HashMap<ObjectId, Set<ObjectId>>();
		
		int index = 0;
		
		while (iterator.hasNext()) {
			
			RevCommit revCommit = (RevCommit) iterator.next();
			
			commitToDependencyMap.put(revCommit.getId(), new CommitDependency(this, revCommit.getId()));
			
			Set<ObjectId> dependencies = new HashSet<ObjectId>();

			for (RevCommit parentCommit : revCommit.getParents()) {
				
				dependencies.add(parentCommit.getId());
			}
			
			List<ExternalModuleInfo> externals = ExternalModuleUtils.findExternalModulesForCommit(repo, revCommit);
			
			for (ExternalModuleInfo externalModuleInfo : externals) {
				
				ObjectId branchHeadId = externalModuleInfo.getBranchHeadId();
				
				// translate the fusion reference
				if (branchHeadId != null) {
					ObjectId translatedBranchHeadId = translationService
							.translateObjectId(branchHeadId);

					dependencies.add(translatedBranchHeadId);
				}
			}
			
			commitDependenciesMap.put(revCommit.getId(), dependencies);
			
			fusionTopoOrderedList.add(revCommit);
			
			this.bitsetTranslationMap.put(index, revCommit);
			this.objectIdToBitsetIndexMap.put(revCommit.getId(), index);
			
			this.revCommitList.add(revCommit);
			
			index++;
			
		}

		// link the parents
		
		for (RevCommit revCommit : fusionTopoOrderedList) {
			
			CommitDependency current = commitToDependencyMap.get(revCommit.getId());
			
			Set<CommitDependency>currentDependencies = new HashSet<CommitDependency>();
			
			Set<ObjectId> directDependencies = commitDependenciesMap.get(revCommit.getId());
			
			for (ObjectId directDependencyId : directDependencies) {
				
				CommitDependency parentDependency = commitToDependencyMap.get(directDependencyId);
				
				if (parentDependency == null) {
					log.warn("missing parentDependency");
				}
				currentDependencies.add(parentDependency);
			}
			
			current.setParentDependencies(currentDependencies);
			
		}
		
	}


	public void computeBitmap(ObjectId forCommit, Set<ObjectId> parentObjectIds) {
		
		Bitmap bitmap = new Bitmap(this);
		
		for (ObjectId objectId : parentObjectIds) {
			
			Integer index = this.objectIdToBitsetIndexMap.get(objectId);

			bitmap.set(index);
			
		}
		
		this.objectIdToBitmap.put(forCommit, bitmap);
		
	}
	
	public Bitmap getBitmap (ObjectId commitId) {
		return this.objectIdToBitmap.get(commitId);
	}


	/**
	 * return a bitmap setup with only the index of the parent commit index set to true.
	 * 
	 * @param parentCommitId
	 * @return
	 */
	public Bitmap getTargetBitmap(ObjectId parentCommitId) {
		
		Integer index = this.objectIdToBitsetIndexMap.get(parentCommitId);
		
		Bitmap bitmap = new Bitmap(this);
		
		bitmap.set(index);
		
		return bitmap;
	}


	public Integer getBitmapElementIndex(ObjectId parentCommitId) {
		return this.objectIdToBitsetIndexMap.get(parentCommitId);
	}
	
	public List<RevCommit>getRevCommitList() {
		return this.revCommitList;
	}


	public CommitDependency getCommitDependency(ObjectId commitId) {
		return this.commitToDependencyMap.get(commitId);
	}


	public Bitmap getAggregateBitmap(CommitDependency commitDependency) {
		
		ObjectId currentCommitId = commitDependency.getCurrentCommitId();
		
		Bitmap aggregate = this.objectIdToAggregatedBitmap.get(currentCommitId);
		
		if (aggregate != null)
			return aggregate;
		
		Bitmap seen = new Bitmap(this);
		
		aggregate = new Bitmap(this);
		
		List<CommitDependency>nodesToCheck = new LinkedList<CommitDependency>();
		
		nodesToCheck.addAll(commitDependency.getParentDependencies());
		
		while (nodesToCheck.size() > 0) {
		
			CommitDependency currentNode = nodesToCheck.remove(0);
			
			if (seen.containsObjectId(currentNode.getCurrentCommitId()))
				continue; // skip over nodes that have already been seen. 
			
			Bitmap cachedAggregate = this.objectIdToAggregatedBitmap.get(currentNode.getCurrentCommitId());
			
			if (cachedAggregate != null) {
				
				aggregate = aggregate.or(cachedAggregate);
			}
			else {
			
				Bitmap currentBitmap = this.getBitmap(currentNode
						.getCurrentCommitId());

				aggregate = aggregate.or(currentBitmap);

				nodesToCheck.addAll(currentNode.getParentDependencies());
			}
			
			aggregate.set(currentNode.getCurrentCommitId());
			
			seen = seen.or(aggregate);
		}
		
		this.objectIdToAggregatedBitmap.put(currentCommitId, aggregate);
					
		return aggregate;
	}
	
	

}
