/**
 * 
 */
package org.kuali.student.git.model.tree;

import org.eclipse.jgit.lib.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public class BlobResourceContext extends AbstractResourceContext {

	private static final Logger log = LoggerFactory.getLogger(BlobResourceContext.class);
	
	/**
	 * 
	 */
	public BlobResourceContext(ObjectId blobId) {
		super("blob", blobId);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.model.tree.GitTreeData.ResourceContext#storeResource(org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData)
	 */
	@Override
	public void storeResource(String name, GitTreeNodeData node) {
		
		ObjectId existing = node.addDirectBlob(name, objectId);
		
		if (existing != null) {
			/*
			 * This is normally ok. This would typically be a change to
			 * a file that existed in the previous commit.
			 * 
			 * This was added originally to find the data loss that had
			 * been occuring.
			 */
			log.debug("overwriting blob = " + name);
		}
	}

}
