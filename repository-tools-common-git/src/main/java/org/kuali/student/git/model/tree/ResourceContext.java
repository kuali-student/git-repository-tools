/**
 * 
 */
package org.kuali.student.git.model.tree;

import java.io.IOException;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;

/**
 * @author ocleirig
 *
 */
public interface ResourceContext {
	
	public void storeResource(String name, GitTreeNodeData node) throws MissingObjectException, IncorrectObjectTypeException, IOException;
	
	public String getErrorMessage();
}