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
package org.kuali.student.git.cleaner.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.cleaner.model.bitmap.Bitmap;
import org.kuali.student.cleaner.model.bitmap.RevCommitBitMapIndex;

import com.googlecode.javaewah.EWAHCompressedBitmap;

/**
 * @author ocleirig
 *
 */
public class CommitDependency {

	private ObjectId currentCommitId;
	
	private Map<ObjectId, CommitDependency>parentDependencies = new HashMap<ObjectId, CommitDependency>();
	
	private RevCommitBitMapIndex indexer;
	
	/**
	 * 
	 */
	public CommitDependency(RevCommitBitMapIndex indexer, ObjectId commitId) {
		this.indexer = indexer;
		currentCommitId = commitId;
	}

	public void setParentDependencies(Set<CommitDependency> currentDependencies) {
		
		Set<ObjectId>parentObjectIds = new HashSet<ObjectId>();
		
		for (CommitDependency parentDependency : currentDependencies) {
			
			ObjectId parentCommitId = parentDependency.getCurrentCommitId();
			
			parentObjectIds.add(parentCommitId);
			
			this.parentDependencies.put(parentCommitId, parentDependency);
		}
		
		indexer.computeBitmap(this.currentCommitId, parentObjectIds);
	}

	/**
	 * @return the currentCommitId
	 */
	public ObjectId getCurrentCommitId() {
		return currentCommitId;
	}
	
	/**
	 * 
	 * @param parentCommitId
	 * @return true if us or our subtree contains the parent commit id given.
	 */
	public boolean containsParent(ObjectId parentCommitId) {
		
		if (this.currentCommitId.equals(parentCommitId))
			return true;
		
		Bitmap aggregate = indexer.getAggregateBitmap (this);
			
		if (aggregate.containsObjectId (parentCommitId))
			return true;
		else
			return false;
			
	}

	public Collection<CommitDependency> getParentDependencies() {
		return this.parentDependencies.values();
	}
		

}
