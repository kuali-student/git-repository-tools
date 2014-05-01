/**
 * 
 */
package org.kuali.student.git.model.tree.utils;

import java.io.IOException;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.git.model.tree.GitTreeData;

/**
 * @author ocleirig
 *
 */
public interface TreeProcessor {

	/**
	 * Extract in GitTreeData (mutable) form the tree data pointed at by the Git Tree Id provided.
	 * 
	 * @param treeId
	 * @param shallow
	 * @return
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	GitTreeData extractTreeData(ObjectId treeId, boolean shallow)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException;

}
