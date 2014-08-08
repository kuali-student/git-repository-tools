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

import java.util.Map;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * @author ocleirig
 *
 */
public final class RevCommitVertexUtils {

	/**
	 * 
	 */
	private  RevCommitVertexUtils() {
	}
	
	/**
	 * Find the simplified vertex of the given commit.  If the commit itself is simplified then we are done.
	 * 
	 * Is designed for the 1 parent case to traverse backwards to the next available simplified vertex.
	 * 
	 * @param branchHeadCommitToBranchNameMap
	 * @param commit
	 * @return the next simplified vertex.
	 */
	public static RevCommit findSimplifiedVertex(Map<RevCommit, String> branchHeadCommitToBranchNameMap, RevCommit commit) {
		
		if (isSimplifiedVertex(branchHeadCommitToBranchNameMap, commit))
			return commit;
		
		if (commit.getParentCount() != 1)
			throw new IllegalArgumentException("commit doesn't have 1 parent.");
		
		
		
		// find the parent commit that is a simplified vertex
		
		RevCommit currentVertex = commit.getParent(0);
		
		while (!RevCommitVertexUtils.isSimplifiedVertex(branchHeadCommitToBranchNameMap, currentVertex)) {
			currentVertex = currentVertex.getParent(0);
		}
		
		return currentVertex;
	}

	public static boolean isSimplifiedVertex(Map<RevCommit, String> branchHeadCommitToBranchNameMap, RevCommit commit) {
		if (branchHeadCommitToBranchNameMap.containsKey(commit) || commit.getParentCount() == 0 || commit.getParentCount() > 1)
			return true;
		else
			return false;
	}

}
