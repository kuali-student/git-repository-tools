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
package org.kuali.student.svn.model;

import org.eclipse.jgit.lib.ObjectId;

/**
 * 
 * Holds a representation of the svn:externals information.
 * 
 * But it is also used by the fusion-maven-plugin using a different set of fields.
 * 
 * <pre>
 * {@code 
 *  # module = ks-api branch Path = enrollment/ks-api/branches/2.0.0-Mx revision = 68768
 *  ks-api::enrollment_ks-api_branches_2.0.0-Mx::a8cfd20224cc829ef8ab01e1003c3ddfb5137371
 * } 
 * </pre>
 * 
 * <ol>
 * <li><b>moduleName</b> : in subversion this is the subdirectory named in the svn:externals property line</li>
 * <li><b>branchPath</b> : in subversion this is the branch path (stripped of the base url) named in the svn:externals property line</li>
 * <li><b>revision</b> : in subversion this is the revision of the commit that contains the svn:externals property at the point the file was created.</li>
 * <li><b>branchName</b> : in git this is the name of the branch (may have been converted from the branchPath if this was originally a svn external).</li>
 * <li><b>branchHeadId</b> : in git this is the object id of the commit on the given branch that should be used in the fusion process</li>
 * <li><b>subtreePath</b> : (Optional) in git this is the subtree in the commit given of the directory to use in the fusion process</li>
 * </ol>
 * 
 * 
 * 
 * @author Kuali Student Team
 *
 */
public class ExternalModuleInfo {

	private String moduleName;
	
	private String branchPath;
	
	private String branchName;
	
	private long revision = -1;
	
	private ObjectId branchHeadId;

	private String subTreePath;
	
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

	public ExternalModuleInfo(String moduleName, String branchName,
			ObjectId headCommitId) {
		
		this.moduleName = moduleName;
		this.branchName = branchName;
		this.branchHeadId = headCommitId;
		
	}

	public ExternalModuleInfo(String moduleName, String branchName) {
		this.moduleName = moduleName;
		this.branchName = branchName;
	}


	public ExternalModuleInfo(String moduleName, String branchPath,
			String branchName, long revision) {
			this (moduleName, branchPath, branchName, revision, null);
	}


	public ExternalModuleInfo(String moduleName, String branchPath,
			String branchName, long revision, String subTreePath) {
		this.moduleName = moduleName;
		this.branchPath = branchPath;
		this.branchName = branchName;
		this.revision = revision;
		this.subTreePath = subTreePath;
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

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @return the subTreePath
	 */
	public String getSubTreePath() {
		return subTreePath;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	
	
	
	
}
