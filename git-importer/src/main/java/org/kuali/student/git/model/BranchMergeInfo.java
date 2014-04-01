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
package org.kuali.student.git.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kuali Student Team
 *
 */
public class BranchMergeInfo {

	private String branchName;
	
	private Set<Long>mergedRevisions = new HashSet<Long>();
	
	/**
	 * 
	 */
	public BranchMergeInfo(String branchName) {
		this.branchName = branchName;
	}
	
	public void addMergeRevision(long revision) {
		this.mergedRevisions.add(revision);
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @return the mergedRevisions
	 */
	public Set<Long> getMergedRevisions() {
		return Collections.unmodifiableSet(mergedRevisions);
	}

	
	/**
	 * @param mergedRevisions the mergedRevisions to set
	 */
	public void setMergedRevisions(Set<Long> mergedRevisions) {
		this.mergedRevisions = mergedRevisions;
	}

	public void clearMergedRevisions() {

		this.mergedRevisions.clear();
	}
	
	

}
