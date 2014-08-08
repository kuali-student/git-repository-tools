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
import java.util.Stack;

import org.apache.commons.collections15.Transformer;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public class GitGraphTransformer implements Transformer<RevCommit, Point2D> {

	private static final Logger log = LoggerFactory
			.getLogger(GitGraphTransformer.class);

	private Map<RevCommit, String> branchHeadsToNameMap;

	private Map<RevCommit, Point2D> placedCommitsMap = new HashMap<RevCommit, Point2D>();

	private Map<Point2D, RevCommit> pointToCommitsMap = new HashMap<Point2D, RevCommit>();

	private int nextParentCommitXOffset = 0;

	private boolean simplify;

	/**
	 * @param branchHeadCommitToBranchNameMap
	 * @param simplify
	 * 
	 */
	public GitGraphTransformer(
			Map<RevCommit, String> branchHeadCommitToBranchNameMap,
			boolean simplify) {
		this.branchHeadsToNameMap = branchHeadCommitToBranchNameMap;
		this.simplify = simplify;
	}

	private Point2D findPlacement(RevCommit input) {

		Point2D placement = this.placedCommitsMap.get(input);

		if (placement != null)
			return placement;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 */
	@Override
	public Point2D transform(RevCommit input) {

		Point2D placement = findPlacement(input);

		if (placement != null)
			return placement;
		
		if (input.getParentCount() == 0) {
			// place here
			placement = new Point2D.Double(nextParentCommitXOffset, 0);

			nextParentCommitXOffset += 100;

			this.placedCommitsMap.put(input, placement);
			this.pointToCommitsMap.put(placement, input);
			
			return placement;
			
		}

		for (RevCommit parentCommit : input.getParents()) {

			RevCommit currentParent = parentCommit;

			if (simplify) {

				currentParent = RevCommitVertexUtils.findSimplifiedVertex(
						branchHeadsToNameMap, parentCommit);
			}

			
			if (placement == null)
				placement = place(input, currentParent);
			else
				place(input, currentParent);
		}

		return placement;
	}

	private Point2D place(RevCommit commit, RevCommit parentCommit) {

		Point2D parentPlacement = findPlacement(parentCommit);

		if (parentPlacement == null) {

			parentPlacement = place (parentCommit);

		}

		Point2D.Double placement = new Point2D.Double(parentPlacement.getX(),
				parentPlacement.getY() + 10);

		while (this.pointToCommitsMap.containsKey(placement)) {
			// prevent placements on the same point

			placement = new Point2D.Double(placement.getX() + 10,
					placement.getY() + 10);

		}

		this.placedCommitsMap.put(commit, placement);
		this.pointToCommitsMap.put(placement, commit);

		return placement;

	}

	private Point2D place(RevCommit commit) {
		
		RevCommit currentCommit = commit;
		
		/*
		 * we want to process in a loop and store the intermediaries so that we can place them
		 * once we have placed the lowest level.
		 */
		
		Stack<RevCommit>pendingPlacements = new Stack<RevCommit>();

		Point2D currentPlacement = null;
		
		while (true) {
		
			currentPlacement = findPlacement(currentCommit);
			
			if (currentPlacement != null) {
				// we are ascending the stack
				
				if (pendingPlacements.isEmpty())
					break;
				
				RevCommit pendingCommit = pendingPlacements.pop();
				
				currentPlacement = place (pendingCommit, currentCommit);
				
				currentCommit = pendingCommit;
				
			}
			else {
				// we are descending the stack along the first parent
				
				if (currentCommit.getParentCount() == 0) {
					// place here

					Point2D placement = new Point2D.Double(nextParentCommitXOffset, 0);

					nextParentCommitXOffset += 100;

					this.placedCommitsMap.put(currentCommit, placement);
					this.pointToCommitsMap.put(placement, currentCommit);

				}
				else {
					// continue looking
					pendingPlacements.push(currentCommit);
				
					currentCommit = currentCommit.getParent(0);
				
				}
				
				
			}
		}

		return currentPlacement;
		
	}

}
