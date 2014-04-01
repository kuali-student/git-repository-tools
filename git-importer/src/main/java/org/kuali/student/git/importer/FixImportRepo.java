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
package org.kuali.student.git.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.git.model.SvnRevisionMapper;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 * 
 * If the importer crashed at a certain revision this program can be used to reset the branch pointers to where they were at a previous revision.
 * 
 * This should allow the dump program to be resumed without having to incur the cost of rexporting to this point.
 * 
 * This will only work if the problem was with the commit where the program was terminated.  
 * 
 * If there is a structural issue like branch detection you may still need to restart from the beginning so that the branches would be detected in earlier revisions which is needed
 * for interpreting the copy from  lines properly.
 * 
 */
public class FixImportRepo {

	private static final Logger log = LoggerFactory
			.getLogger(FixImportRepo.class);

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		if (args.length != 2) {
			log.error("USAGE: <git repository> <reset branches to revision>");
			System.exit(-1);
		}


		try {

			File gitRepository = new File(args[0]).getAbsoluteFile();

			if (!gitRepository.getParentFile().exists())
				throw new FileNotFoundException(args[1] + "path not found");

			final Repository repo = GitRepositoryUtils.buildFileRepository(
					gitRepository, false);

			String revision = args[1];
			
			SvnRevisionMapper mapper = new SvnRevisionMapper(repo);
			
			mapper.initialize();
			
			Map<String, Ref>allRefs = new HashMap<String, Ref>();

			Map<String, Ref> existingRefs = repo.getRefDatabase().getRefs(Constants.R_HEADS);
			
			for (SvnRevisionMap entry : mapper.getRevisionHeads(Long.parseLong(revision))) {
				
				updateRef(repo, entry.getBranchName(), revision, ObjectId.fromString(entry.getCommitId()));
			
				existingRefs.remove(entry.getBranchName());
			}

			
			if (allRefs.size() > 0) {
				// delete all of the left over refs that weren't updated.
				for (Ref ref : allRefs.values()) {
					
					deleteRef(repo, ref.getName(), revision);
					
				}
			}
			
			mapper.shutdown();
			
		} catch (Exception e) {
			log.error("Processing failed", e);
		}


	}

	private static void updateRef(Repository repo, String branchName, String revision, AnyObjectId branchId) throws IOException {
		
		RefUpdate u = repo.updateRef(branchName, true);
		
		u.setForceUpdate(true);
		
		String resultMessage = "resetting branch "+branchName+" to revision " + revision + " at " + branchId.name();
		
		u.setNewObjectId(branchId);
		
		u.setRefLogMessage(resultMessage , true);
		
		Result result = u.update();

		log.info(resultMessage + " result = " + result);
		
	}
	
	
private static void deleteRef(Repository repo, String branchName, String revision) throws IOException {
		
		RefUpdate u = repo.updateRef(branchName, true);
		
		u.setForceUpdate(true);
		
		String resultMessage = "deleting branch "+branchName+" at revision " + revision;
		
		u.setRefLogMessage(resultMessage , true);
		
		Result result = u.delete();

		log.info(resultMessage + " result = " + result);
		
	}
	

}
