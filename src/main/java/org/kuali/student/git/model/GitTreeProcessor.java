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
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.utils.GitBranchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class GitTreeProcessor {

	private static final Logger log = LoggerFactory.getLogger(GitTreeProcessor.class);
	
	private Repository repo;

	/**
	 * 
	 */
	public GitTreeProcessor(Repository repo) {
		this.repo = repo;
	}
	
	public static interface GitTreeBlobVisitor {
		/**
		 * 
		 * @param blobId
		 * @param path
		 * @return true if the visiting should continue; false if it should stop.
		 * 
		 */
		public boolean visitBlob(ObjectId blobId, String path, String name);
	}
	
	public void visitBlobs (ObjectId commitId, GitTreeBlobVisitor visitor) throws MissingObjectException, IncorrectObjectTypeException, IOException {
	
		visitBlobs(commitId, visitor, null);
	}
	
	public void visitBlobs (ObjectId commitId, GitTreeBlobVisitor visitor, TreeFilter treeFilter) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		RevWalk walk = new RevWalk(repo);

		RevCommit commit = walk.parseCommit(commitId);

		// a commit points to a tree
		ObjectId treeId = commit.getTree().getId();
		
		TreeWalk treeWalk = new TreeWalk(repo);

		treeWalk.addTree(treeId);

		treeWalk.setRecursive(true);
		
		if (treeFilter != null)
			treeWalk.setFilter(treeFilter);

		if (treeWalk.getTreeCount() == 0) {
			log.warn("no trees to parse");
		}

		while (treeWalk.next()) {

			final FileMode mode = treeWalk.getFileMode(0);

			if (mode != FileMode.REGULAR_FILE)
				continue;

			/*
			 * We only want the blob's
			 */
			ObjectId blobId = treeWalk.getObjectId(0);

			String path = treeWalk.getPathString();
			
			String name = treeWalk.getNameString();
			
			if (!visitor.visitBlob(blobId, path, name))
				return; // stop when the visitor indicates its done.
		}
	}

}
