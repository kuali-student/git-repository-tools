/*
 * Copyright 2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.git.model.tree.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.TreeFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.model.tree.JGitTreeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class JGitTreeUtils {

	private static final Logger log = LoggerFactory.getLogger(JGitTreeUtils.class);
	
	/**
	 * 
	 */
	public JGitTreeUtils() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Extract the list of elements in the commit tree at the base or root level.
	 * 
	 * @param or
	 * @param commit
	 * @return
	 * @throws IOException 
	 * @throws CorruptObjectException 
	 * @throws IncorrectObjectTypeException 
	 * @throws MissingObjectException 
	 */
	public static List<JGitTreeData>extractBaseTreeLevel (ObjectReader or, RevCommit commit) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		ArrayList<JGitTreeData> treeData = new ArrayList<>();
		
		TreeWalk tw = new TreeWalk(or);
		
		tw.addTree(commit.getTree().getId());
		
		tw.setRecursive(false);
		
		while (tw.next()) {
		
			String name = tw.getNameString();
			
			FileMode fileMode = tw.getFileMode(0);
			ObjectId objectId = tw.getObjectId(0);
			
			JGitTreeData jgtd = new JGitTreeData(name, fileMode, objectId);
			
			treeData.add(jgtd);
			
		}
		
		tw.release();
		
		return treeData;
	}
	
	
	public static ObjectId createTree (List<JGitTreeData>data, ObjectInserter inserter) throws IOException {
		
		TreeFormatter tf = new TreeFormatter();
		
		Collections.sort(data, JGitTreeData.GIT_SORT_ORDERING);
		
		for (JGitTreeData treeData : data) {
			
			tf.append(treeData.getName(), treeData.getFileMode(), treeData.getObjectId());
		}
		
		ObjectId treeId = inserter.insert(tf);
		
		return treeId;
		
	}

}
