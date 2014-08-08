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

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public class GitGraphTransformer implements Transformer<RevCommit, Point2D> {

	private static final Logger log = LoggerFactory.getLogger(GitGraphTransformer.class);
	
	private Map<RevCommit, String> branchHeadsToNameMap;
	
	private Map<RevCommit, Point2D>placedCommitsMap = new HashMap<RevCommit, Point2D>();
	
	
	private Map<Point2D, RevCommit>pointToCommitsMap = new HashMap<Point2D, RevCommit>();

	private int nextParentCommitXOffset = 0;
	/**
	 * @param branchHeadCommitToBranchNameMap 
	 * 
	 */
	public GitGraphTransformer(Map<RevCommit, String> branchHeadCommitToBranchNameMap) {
		this.branchHeadsToNameMap = branchHeadCommitToBranchNameMap;
	}

	private Point2D findPlacement(RevCommit input) {

		Point2D placement = this.placedCommitsMap.get(input);
		
		if (placement != null)
			return placement;
		else
			return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 */
	@Override
	public Point2D transform(RevCommit input) {
		
		Point2D placement = findPlacement(input);
		
		if (placement != null)
			return placement;
		
		for (RevCommit parentCommit : input.getParents()) {
		
			if (placement == null)
				placement = place (input, parentCommit);
			else
				place (input, parentCommit);
		}
		
		return placement;
	}

	
	private Point2D place(RevCommit commit, RevCommit parentCommit) {
		
		Point2D parentPlacement = findPlacement(parentCommit);
		
		if (parentPlacement == null) {
			
			if (parentCommit.getParentCount() > 0) {
				for (RevCommit parents : parentCommit.getParents()) {
					
					Point2D p = place (parentCommit, parents);
					
					if (parentPlacement == null)
						parentPlacement = p;
					
				}
			}
			else {
				// no parents
				parentPlacement = new Point2D.Double(nextParentCommitXOffset, 0);
				
				nextParentCommitXOffset += 100;
				
				this.placedCommitsMap.put(parentCommit, parentPlacement);
				this.pointToCommitsMap.put(parentPlacement, parentCommit);
				
			}
			
		}
			
		Point2D.Double placement =  new Point2D.Double(parentPlacement.getX(), parentPlacement.getY() + 10);
		
		while (this.pointToCommitsMap.containsKey(placement)) {
			// prevent placements on the same point
			
			placement =  new Point2D.Double(placement.getX()+10, placement.getY() + 10);
			
		}
		
		this.placedCommitsMap.put(commit, placement);
		this.pointToCommitsMap.put(placement, commit);
		
		return placement;
		
	}

}
