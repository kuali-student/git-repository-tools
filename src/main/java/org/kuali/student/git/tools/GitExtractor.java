/*
 * Copyright 2013 The Kuali Foundation
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
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.diff.DiffAlgorithm.SupportedAlgorithm;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AbbreviatedObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class GitExtractor {

	private static final Logger log = LoggerFactory.getLogger(GitExtractor.class);
	private Repository repository;
	
	/**
	 * 
	 */
	public GitExtractor() {
		
	}
	
	public void buildRepository (File repositoryPath) throws IOException {
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		
		builder = builder.setGitDir(repositoryPath);
		
		builder = builder.readEnvironment();
		
		builder = builder.findGitDir();
		
		repository = builder.build();
		
	}



	public void extractDifference(String targetTagName, String copyFromTagName) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		RevTree target = extractTag(targetTagName);
		RevTree copyFrom = extractTag(copyFromTagName);
		
		DiffFormatter formatter = new DiffFormatter(System.out);
		
		formatter.setRepository(repository);

		formatter.setDetectRenames(true);
		
		formatter.setDiffAlgorithm(DiffAlgorithm.getAlgorithm(SupportedAlgorithm.MYERS));
		
		formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
		
		List<DiffEntry> results = formatter.scan(target, copyFrom);
		
		for (DiffEntry entry : results) {

			if (entry.getChangeType().equals(ChangeType.COPY)) {
				
				AbbreviatedObjectId copyFromBlobId = entry.getOldId();
				
				
				// Cscore:md5:target-path:copy-from-path
				log.info(String.format("C%d:%s:%s:%s",  entry.getScore(), "md5", entry.getNewPath(), entry.getOldPath()));
			}
			else if (entry.getChangeType().equals(ChangeType.MODIFY)) {
				// Cscore:md5:target-path:copy-from-path
				log.info(String.format("M%d:%s:%s:%s",  entry.getScore(), "md5", entry.getNewPath(), entry.getOldPath()));
			}
		}
		
		
		
	}

	private RevTree extractTag(String tagName) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		RevWalk walk = new RevWalk(repository);
		
		Map<String, Ref> tags = repository.getTags();
		
		Ref tag = tags.get(tagName);

		RevTree target = walk.parseTree(tag.getObjectId());

		return target;
	}

	
}
