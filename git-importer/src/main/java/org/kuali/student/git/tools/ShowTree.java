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

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.model.GitRepositoryUtils;
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

		if (args.length != 3) {
			usage();
		}
		
		
		try {
			Repository repo = GitRepositoryUtils.buildFileRepository(
					new File (args[0]), false);
			
			
			String mode = args[1];
			
			String objectId = args[2];
			
			TreeWalk tw = new TreeWalk(repo);
			
			if (mode.equals("tree")) {
				
				tw.addTree(ObjectId.fromString(args[2].trim()));
				
				processTreeWalk(tw);
			}
			else if (mode.equals("follow-commit")) {
				
				RevWalk rw = new RevWalk(repo);
				
				RevCommit currentCommit = rw.parseCommit(ObjectId.fromString(objectId));
				
				while (currentCommit != null) {
				
					log.info("current commit id = " + currentCommit.getId());
					log.info("current commit message = " + currentCommit.getFullMessage());
					
					tw.reset(currentCommit.getTree().getId());
					
					processTreeWalk(tw);
					
					currentCommit = currentCommit.getParent(0);
				}
				
				rw.release();
			}
			else {
				usage();
			}
			
			
			tw.release();
			
			
			
		} catch (IOException e) {
			log.error("unexpected Exception ", e);
		}
	}

	private static void processTreeWalk(TreeWalk tw) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		while (tw.next()) {
			byte[] rawPath = tw.getRawPath();
		
			ObjectId elementObjectId = tw.getObjectId(0);
			
			String path = new String (rawPath);
			
			log.info("path = " + path + " object Id = " + elementObjectId);
		}
		
	}

	private static void usage() {
		System.err.println("USAGE: <path to .git repository> <mode> <tree sha1>");
		System.err.println("\tmode: <tree: print tree> <follow-commit: print out base tree for each commit");
		System.exit(-1);		
	}

}
