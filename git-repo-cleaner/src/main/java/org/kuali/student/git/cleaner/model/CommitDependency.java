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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.lib.ObjectId;

/**
 * @author ocleirig
 *
 */
public class CommitDependency {

	private ObjectId currentCommitId;
	
	private Map<ObjectId, CommitDependency>parentDependencies = new HashMap<ObjectId, CommitDependency>();
	
	private Set<ObjectId>aggregatedDependencies = null;
	
	/**
	 * 
	 */
	public CommitDependency(ObjectId commitId) {
		currentCommitId = commitId;
	}

	public void setParentDependencies(Set<CommitDependency> currentDependencies) {
		
		for (CommitDependency parentDependency : currentDependencies) {
			if (parentDependency == null) {
				int x = 1;
				x = x+1;
			}
			
			this.parentDependencies.put(parentDependency.getCurrentCommitId(), parentDependency);
		}
	}

	/**
	 * @return the currentCommitId
	 */
	public ObjectId getCurrentCommitId() {
		return currentCommitId;
	}
	
	public Set<ObjectId>getAggregateDependencies() {
		
		if (this.aggregatedDependencies == null) {
			
			this.aggregatedDependencies = new HashSet<ObjectId>();
			
			for (CommitDependency dependency : this.parentDependencies.values()) {
				
				this.aggregatedDependencies.add(dependency.getCurrentCommitId());
				
				this.aggregatedDependencies.addAll(dependency.getAggregateDependencies());
			}
			
		}		
		return this.aggregatedDependencies;
	}
	

}
