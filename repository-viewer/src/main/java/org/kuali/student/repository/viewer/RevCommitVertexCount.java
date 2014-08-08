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
package org.kuali.student.repository.viewer;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * @author ocleirig
 *
 */
public class RevCommitVertexCount {

	private RevCommit vertex;
	
	private int count;

	/**
	 * @param vertex
	 * @param count
	 */
	public RevCommitVertexCount(RevCommit vertex, int count) {
		super();
		this.vertex = vertex;
		this.count = count;
	}

	/**
	 * @return the vertex
	 */
	protected RevCommit getVertex() {
		return vertex;
	}

	/**
	 * @return the count
	 */
	protected int getCount() {
		return count;
	}

	
	
	
}
