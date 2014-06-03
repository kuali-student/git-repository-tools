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
