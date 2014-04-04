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
package org.kuali.student.svn.model;

import org.eclipse.jgit.lib.ObjectId;

/**
 * @author Kuali Student Team
 *
 */
public class ExternalModuleInfo {

	private String moduleName;
	
	private String branchPath;
	
	private long revision;
	
	private ObjectId branchHeadId;

	/**
	 * @param moduleName
	 * @param branchPath
	 * @param revision
	 */
	public ExternalModuleInfo(String moduleName, String branchPath,
			long revision, ObjectId branchHeadId) {
		super();
		this.moduleName = moduleName;
		this.branchPath = branchPath;
		this.revision = revision;
		this.branchHeadId = branchHeadId;
	}

	public ExternalModuleInfo(String moduleName, String branchPath,
			long revision) {
		this (moduleName, branchPath, revision, null);
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @return the branchPath
	 */
	public String getBranchPath() {
		return branchPath;
	}

	/**
	 * @return the revision
	 */
	public long getRevision() {
		return revision;
	}

	/**
	 * @return the branchHeadId
	 */
	public ObjectId getBranchHeadId() {
		return branchHeadId;
	}

	/**
	 * @param branchHeadId the branchHeadId to set
	 */
	public void setBranchHeadId(ObjectId branchHeadId) {
		this.branchHeadId = branchHeadId;
	}
	
	
	
	
	
}
