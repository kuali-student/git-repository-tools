/*
 * Copyright 2013 The Kuali Foundation
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
package org.kuali.student.svn.tools.model;

/**
 * @author Kuali Student Team
 *
 */
public class JoinedRevision {

	private long targetRevision = -1;
	
	private long copyFromRevision = -1;
	
	/**
	 * 
	 */
	public JoinedRevision() {
		super();
	}

	public JoinedRevision(long targetRevision, long copyFromRevision) {
		this();
		this.targetRevision = targetRevision;
		this.copyFromRevision = copyFromRevision;
	}

	/**
	 * @return the targetRevision
	 */
	public long getTargetRevision() {
		return targetRevision;
	}

	/**
	 * @param targetRevision the targetRevision to set
	 */
	public void setTargetRevision(long targetRevision) {
		this.targetRevision = targetRevision;
	}

	/**
	 * @return the copyFromRevision
	 */
	public long getCopyFromRevision() {
		return copyFromRevision;
	}

	/**
	 * @param copyFromRevision the copyFromRevision to set
	 */
	public void setCopyFromRevision(long copyFromRevision) {
		this.copyFromRevision = copyFromRevision;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JoinedRevision [targetRevision=");
		builder.append(targetRevision);
		builder.append(", copyFromRevision=");
		builder.append(copyFromRevision);
		builder.append("]");
		return builder.toString();
	}
	
	

}
