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
package org.kuali.student.git.tools;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class ShowTree {

	private static final Logger log = LoggerFactory.getLogger(ShowTree.class);
	
	/**
	 * 
	 */
	public ShowTree() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			System.err.println("USAGE: <path to .git repository> <tree sha1>");
			System.exit(-1);
		}
		
		
		try {
			Repository repo = GitRepositoryUtils.buildFileRepository(
					new File (args[0]), false);
			
			
			TreeWalk tw = new TreeWalk(repo);
			
			tw.addTree(ObjectId.fromString(args[1].trim()));
			
			while (tw.next()) {
				byte[] rawPath = tw.getRawPath();
			
				String path = new String (rawPath);
				
				log.info("path = " + path);
			}
			
			
		} catch (IOException e) {
			log.error("unexpected Exception ", e);
		}
	}

}
