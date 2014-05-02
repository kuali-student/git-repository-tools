/**
 * 
 */
package org.kuali.student.git.model.tree;

import java.io.IOException;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;

/**
 * @author ocleirig
 *
 */
public interface GitTreeNodeInitializer {
	
	public void initialize(GitTreeNodeData node) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException;
	

}
