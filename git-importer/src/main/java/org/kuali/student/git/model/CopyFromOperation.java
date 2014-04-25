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

import java.util.ArrayList;
import java.util.List;

import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMapResults;

/**
 * @author Kuali Student Team
 *
 */
public class CopyFromOperation {
	
	private List<GitBranchData>targetBranches = new ArrayList<>();
	
	private List<SvnRevisionMapResults>copyFromBranches = new ArrayList<>();
	
	
	private OperationType type;

	private String copyFromPath;
	
	/*
	 * MULTI - multiple targets
	 * SINGLE - single target
	 * SINGLE_NEW - single target a new branch
	 * INVALID_SINGLE_NEW - singal target name is invalid but could be a new branch (depends on copy from data)
	 * SUBTREE - target is a subtree in an existing branch.
	 */
	public static enum OperationType { MULTI, SINGLE, SINGLE_NEW, INVALID_SINGLE_NEW, SUBTREE};

	/**
	 * 
	 */
	public CopyFromOperation(OperationType type) {
		super();
		this.type = type;
	}

	/**
	 * @return the targetBranches
	 */
	public List<GitBranchData> getTargetBranches() {
		return targetBranches;
	}

	/**
	 * @return the copyFromBranches
	 */
	public List<SvnRevisionMapResults> getCopyFromBranches() {
		return copyFromBranches;
	}

	/**
	 * @param copyFromBranches the copyFromBranches to set
	 * @param copyFromPath 
	 */
	public void setCopyFromBranches(List<SvnRevisionMapResults> copyFromBranches, String copyFromPath) {
		this.copyFromBranches = copyFromBranches;
		this.copyFromPath = copyFromPath;
	}

	/**
	 * @return the type
	 */
	public OperationType getType() {
		return type;
	}

	
	/**
	 * @return the copyFromPath
	 */
	public String getCopyFromPath() {
		return copyFromPath;
	}

	public void setTargetBranches(List<GitBranchData> targetBranches) {
		this.targetBranches = targetBranches;
		
	}
	
	
	
}
