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
package org.kuali.student.svn.tools.merge.model;

/**
 * @author Kuali Student Team
 *
 */
public class BranchData {
	Long revision;

	String branchPath;
	String path;

	/**
	 * @param branchPath
	 * @param path
	 */
	public BranchData(Long revision, String branchPath, String path) {
		super();
		this.revision = revision;
		this.branchPath = branchPath;
		this.path = path;
	}

	/**
	 * @return the prefix
	 */
	public String getBranchPath() {
		return branchPath;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the revision
	 */
	public Long getRevision() {
		return revision;
	}

	public boolean isSandbox() {
		return branchPathContains("sandbox");
	}

	public boolean isBranch() {
		return branchPathContains("branches");
	}

	public boolean isTag() {

		return branchPathContains("tags");
	}

	private boolean branchPathContains(String test) {

		if (branchPath.contains(test))
			return true;
		else
			return false;

	}

}
