/**
 * 
 */
package org.kuali.student.git.model.tree;

import org.kuali.student.git.model.tree.exceptions.GitTreeNodeInitializationException;

/**
 * @author ocleirig
 *
 */
public interface GitTreeNodeInitializer {
	
	public void initialize(GitTreeNodeData node) throws GitTreeNodeInitializationException;
	

}
