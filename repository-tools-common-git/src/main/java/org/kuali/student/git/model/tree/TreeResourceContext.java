/**
 * 
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

	private static final Logger log = LoggerFactory.getLogger(BlobResourceContext.class);
	
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
			log.warn("overwriting " + getErrorMessage());
		}

		
	}
	
}