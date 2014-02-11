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

import org.eclipse.jgit.internal.storage.file.FileRepository;
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
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		
		builder = builder.setGitDir(repositoryPath);
		
		builder = builder.readEnvironment();
		
		builder = builder.findGitDir();
		
		Repository repo = builder.build();
		
		if (create)
			repo.create(false);
		
		return repo;
	}

	public static void insertIntoIndex (Repository repo) {
		
		/*
		 * Copied from AddCommand.call()
		 * 
		 * and adapted.
		 * 
		 */
	/*
	ObjectInserter inserter = repo.newObjectInserter();
	try {
		DirCache dc = repo.lockDirCache();
		DirCacheIterator c;

		DirCacheBuilder builder = dc.builder();
		
		builder.add(new DirCacheEntry(newPath));
		
		builder.addTree(pathPrefix, stage, reader, tree);
		
		final TreeWalk tw = new TreeWalk(repo);
		tw.addTree(new DirCacheBuildIterator(builder));
		tw.setRecursive(true);

		String lastAddedFile = null;

		while (tw.next()) {
			String path = tw.getPathString();

			WorkingTreeIterator f = tw.getTree(1, WorkingTreeIterator.class);
			if (tw.getTree(0, DirCacheIterator.class) == null &&
					f != null && f.isEntryIgnored()) {
				// file is not in index but is ignored, do nothing
			}
			// In case of an existing merge conflict the
			// DirCacheBuildIterator iterates over all stages of
			// this path, we however want to add only one
			// new DirCacheEntry per path.
			else if (!(path.equals(lastAddedFile))) {
				if (!(update && tw.getTree(0, DirCacheIterator.class) == null)) {
					c = tw.getTree(0, DirCacheIterator.class);
					if (f != null) { // the file exists
						long sz = f.getEntryLength();
						DirCacheEntry entry = new DirCacheEntry(path);
						if (c == null || c.getDirCacheEntry() == null
								|| !c.getDirCacheEntry().isAssumeValid()) {
							FileMode mode = f.getIndexFileMode(c);
							entry.setFileMode(mode);

							if (FileMode.GITLINK != mode) {
								entry.setLength(sz);
								entry.setLastModified(f
										.getEntryLastModified());
								long contentSize = f
										.getEntryContentLength();
								InputStream in = f.openEntryStream();
								try {
									entry.setObjectId(inserter.insert(
											Constants.OBJ_BLOB, contentSize, in));
								} finally {
									in.close();
								}
							} else
								entry.setObjectId(f.getEntryObjectId());
							builder.add(entry);
							lastAddedFile = path;
						} else {
							builder.add(c.getDirCacheEntry());
						}

					} else if (c != null
							&& (!update || FileMode.GITLINK == c
									.getEntryFileMode()))
						builder.add(c.getDirCacheEntry());
				}
			}
		}
		inserter.flush();
		builder.commit();
		
		*/
		
	}
}
