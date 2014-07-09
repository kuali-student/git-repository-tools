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
package org.kuali.student.git.model.graft;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * @author ocleirig
 *
 */
public class GitGraft {

	private ObjectId targetCommitId;
	
	private Set<ObjectId>parentCommitIds;

	/**
	 * @param targetCommitId
	 * @param parentCommitIds
	 */
	public GitGraft(ObjectId targetCommitId, Set<ObjectId> parentCommitIds) {
		super();
		this.targetCommitId = targetCommitId;
		this.parentCommitIds = new HashSet<>(parentCommitIds);
	}

	public GitGraft(ObjectId targetCommitId, Collection<RevCommit> parentRevCommits) {
		super();
		
		Set<ObjectId>parentCommitIds = new HashSet<>();
		
		for (RevCommit revCommit : parentRevCommits) {
			parentCommitIds.add(revCommit.getId());
		}
		
		this.targetCommitId = targetCommitId;
		this.parentCommitIds = parentCommitIds;
	}

	/**
	 * @return the targetCommitId
	 */
	public ObjectId getTargetCommitId() {
		return targetCommitId;
	}

	/**
	 * @return the parentCommitIds
	 */
	public Set<ObjectId> getParentCommitIds() {
		return parentCommitIds;
	}

	/**
	 * @param targetCommitId the targetCommitId to set
	 */
	public GitGraft setTargetCommitId(ObjectId targetCommitId) {
		
		GitGraft copy = new GitGraft(targetCommitId, this.parentCommitIds);
		
		return copy;
	}

	/**
	 * @param parentCommitIds the parentCommitIds to set
	 */
	public GitGraft setParentCommitIds(Set<ObjectId> parentCommitIds) {

		GitGraft copy = new GitGraft(this.targetCommitId, parentCommitIds);
		
		return copy;
		
	}
	
	
	

	
}
