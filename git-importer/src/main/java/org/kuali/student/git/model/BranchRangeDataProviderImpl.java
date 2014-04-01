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

import java.io.IOException;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.kuali.student.git.tools.SvnMergeInfoUtils.BranchRangeDataProvider;

/**
 * @author Kuali Student Team
 *
 */
public class BranchRangeDataProviderImpl implements BranchRangeDataProvider {

	private SvnRevisionMapper revisionMapper;
	private RevWalk revWalk;

	/**
	 * 
	 */
	public BranchRangeDataProviderImpl(SvnRevisionMapper revisionMapper, RevWalk revWalk) {
		this.revisionMapper = revisionMapper;
		
		this.revWalk = revWalk;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.tools.SvnMergeInfoUtils.BranchRangeDataProvider#areCommitsAjacent(java.lang.String, long, long)
	 */
	@Override
	public boolean areCommitsAdjacent(String branchName, long firstRevision,
			long secondRevision) {

		try {
			
			ObjectId firstHead = revisionMapper.getRevisionBranchHead(firstRevision, branchName);
			
			ObjectId secondHead = revisionMapper.getRevisionBranchHead(secondRevision, branchName);
			
			if (firstHead == null || secondHead == null)
				return false; // insufficent data
			
			if (parentOf (firstHead, secondHead) || parentOf (secondHead, firstHead))
				return true;
			else
				return false;
			
		} catch (IOException e) {
			throw new RuntimeException(String.format ("areCommitsAdjacent (branchName=%s, firstRev=%d, secondRev=%d)", branchName, firstRevision, secondRevision), e);
		}
	}

	private boolean parentOf(ObjectId targetHead, ObjectId parentHead) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		RevCommit targetCommit = revWalk.parseCommit(targetHead);
		
		for (RevCommit parent : targetCommit.getParents()) {
			if (parent.getId().equals(parentHead))
					return true;
		}
		
		// else not directly a parent of
		return false;
		
	}

}
