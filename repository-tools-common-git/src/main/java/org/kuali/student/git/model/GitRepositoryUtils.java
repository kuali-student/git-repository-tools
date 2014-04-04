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

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

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

	 
}
