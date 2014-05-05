/**
 * 
 */
package org.kuali.student.git.model.tree;

import java.io.IOException;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
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
	public void initialize(GitTreeNodeData node) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		ObjectId originalTreeId = node.getOriginalTreeObjectId();
		
		if (originalTreeId != null) {
			
			GitTreeNodeData loadedNode = treeProcessor.extractExistingTreeData(originalTreeId, "");
		
			node.replaceWith (loadedNode);
			
		}
		
		node.setInitialized(true);
		
	}
	
	

}
