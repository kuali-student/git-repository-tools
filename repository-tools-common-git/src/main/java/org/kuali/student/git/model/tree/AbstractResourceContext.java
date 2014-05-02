package org.kuali.student.git.model.tree;

import org.eclipse.jgit.lib.ObjectId;

public abstract class AbstractResourceContext implements ResourceContext {

	protected ObjectId objectId;
	private String type;

	/**
	 * 
	 */
	public AbstractResourceContext(String type, ObjectId objectId) {
		super();
		this.type = type;
		this.objectId = objectId;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.model.tree.GitTreeData.ResourceContext#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return new StringBuilder(" ").append(type).append("Id = ").append(objectId).toString();
	}
	
	
	
}