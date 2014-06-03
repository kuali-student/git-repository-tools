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
package org.kuali.student.git.model.tree;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public class TreeResourceContext extends AbstractResourceContext {

	private static final Logger log = LoggerFactory.getLogger(TreeResourceContext.class);
	
	private GitTreeProcessor treeProcessor;

	/**
	 * @param type
	 * @param objectId
	 */
	public TreeResourceContext(GitTreeProcessor treeProcessor, ObjectId objectId) {
		super("tree", objectId);
		this.treeProcessor = treeProcessor;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.model.tree.GitTreeData.ResourceContext#storeResource(java.lang.String, org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData)
	 */
	@Override
	public void storeResource(String name, GitTreeNodeData node) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		GitTreeNodeData newTree = treeProcessor.extractExistingTreeData (objectId, name);
		
		GitTreeNodeData existing = node.addDirectTree(name, newTree);
		
		if (existing != null) {
			
			ObjectId existingObjectId = existing.getOriginalTreeObjectId();
			
			if (existingObjectId != null && !existingObjectId.equals(objectId)) {
				
				/*
				 * lets merge the tree.  We take the changes even if there are conflicts on the blob id's.
				 */
				
				existing.merge(treeProcessor.extractExistingTreeData(objectId, existing.getName()));
				
				log.warn("merging conflicting trees " + getErrorMessage());
			}
		}

		
	}
	
}