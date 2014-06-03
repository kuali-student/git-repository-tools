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

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.git.model.tree.exceptions.GitTreeNodeInitializationException;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;

/**
 * @author ocleirig
 *
 */
public class GitTreeNodeInitializerImpl implements GitTreeNodeInitializer {

	private GitTreeProcessor treeProcessor;
	
	/**
	 * 
	 */
	public GitTreeNodeInitializerImpl(GitTreeProcessor treeProcessor) {
		this.treeProcessor = treeProcessor;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.model.tree.GitTreeNodeInitializer#initialize(org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData)
	 */
	@Override
	public void initialize(GitTreeNodeData node) throws GitTreeNodeInitializationException {
		
		try {
			ObjectId originalTreeId = node.getOriginalTreeObjectId();
			
			if (originalTreeId != null) {
				
				GitTreeNodeData loadedNode = treeProcessor.extractExistingTreeData(originalTreeId, node.getName());
			
				node.replaceWith (loadedNode);
				
			}
			
			node.setInitialized(true);
		} catch (Exception e) {
			throw new GitTreeNodeInitializationException("initialize(node name="+node!=null?node.getName():"null"+") failed unexpectantly: ", e);
		}
		
	}
	
	

}
