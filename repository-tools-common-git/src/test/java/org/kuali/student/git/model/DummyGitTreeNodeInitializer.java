/**
 * 
 */
package org.kuali.student.git.model;

import java.io.IOException;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData;
import org.kuali.student.git.model.tree.GitTreeNodeInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public class DummyGitTreeNodeInitializer implements GitTreeNodeInitializer {

	private static final Logger log = LoggerFactory.getLogger(DummyGitTreeNodeInitializer.class);
	/**
	 * 
	 */
	public DummyGitTreeNodeInitializer() {
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.model.tree.GitTreeNodeInitializer#initialize(org.kuali.student.git.model.tree.GitTreeData.GitTreeNodeData)
	 */
	@Override
	public void initialize(GitTreeNodeData node) throws MissingObjectException,
			IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		log.info("initialize called for node = " + node);
		node.setInitialized(true);

	}

}
