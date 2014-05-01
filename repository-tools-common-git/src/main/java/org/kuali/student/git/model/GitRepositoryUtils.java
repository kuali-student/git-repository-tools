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
package org.kuali.student.git.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * @author Kuali Student Team
 *
 */
public final class GitRepositoryUtils {

	/**
	 * 
	 */
	private GitRepositoryUtils() {
	}
	
	/**
	 * Build a Git Respository given the path to its .git directory.
	 * 
	 * @param repositoryPath
	 * @return
	 * @throws IOException
	 */
	public static Repository buildFileRepository (File repositoryPath, boolean create) throws IOException {
		return buildFileRepository(repositoryPath, create, true);
	}
	
	public static Repository buildFileRepository (File repositoryPath, boolean create, boolean bare) throws IOException {
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		
		if (bare) {
			builder = builder.setGitDir(repositoryPath);
		}
		else {
			builder.setWorkTree(repositoryPath);
		}
			
		
		builder = builder.readEnvironment();

		Repository repo = builder.build();
		
		if (create)
			repo.create(bare);
		
		
		return repo;
	}


	public static ObjectId findInCommit(Repository repository, ObjectId commitId,
			String filePath) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		RevWalk rw = new RevWalk(repository);
		
		RevCommit commit = rw.parseCommit(commitId);
		
		rw.release();
	
		return findInTree(repository, commit.getTree().getId(), filePath);
		
	}

	/**
	 * Return the JGit ObjectId for the blob or tree represented by the path given.
	 * 
	 * The path needs to be relative to the tree provided.
	 * @param repository
	 * @param treeId
	 * @param filePath
	 * @return
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	public static ObjectId findInTree(Repository repository, ObjectId treeId,
			String filePath) throws MissingObjectException,
			IncorrectObjectTypeException, CorruptObjectException, IOException {

		if (treeId == null)
			return null;

		String parts[] = filePath.split("/");

		int currentPartIndex = 0;

		TreeWalk tw = new TreeWalk(repository);

		tw.addTree(treeId);

		while (tw.next()) {

			// the part changes when we decend the tree path
			String currentPart = parts[currentPartIndex];

			String pathName = tw.getNameString();

			if (currentPart.equals(pathName)) {

				if ((currentPartIndex + 1) == parts.length) {
					// at the end so use the current object id.
					tw.release();
					return tw.getObjectId(0);
				} else {
					if (tw.getFileMode(0).equals(FileMode.TYPE_TREE)) {
						tw.enterSubtree();
						currentPartIndex++;
					}
					else {
						tw.release();
						return null;
					}
				}

			}
		}

		tw.release();
		return null;

	}
}
