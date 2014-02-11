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

/**
 * @author Kuali Student Team
 *
 */
public class GitCommitData {

	private String date;
	
	private String userName;
	
	private String commitMessage;

	/**
	 * @param branchName
	 * @param userName
	 * @param commitMessage
	 */
	public GitCommitData(String date, String userName,
			String commitMessage) {
		super();
		this.date = date;
		this.userName = userName;
		this.commitMessage = commitMessage;
	}

	

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}



	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the commitMessage
	 */
	public String getCommitMessage() {
		return commitMessage;
	}
	
	
	

}
