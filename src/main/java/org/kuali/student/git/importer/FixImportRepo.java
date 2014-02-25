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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.iq80.snappy.SnappyInputStream;
import org.kuali.student.git.model.GitBranchData;
import org.kuali.student.git.model.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.git.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.kuali.student.svn.tools.AbstractParseOptions;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
			
			File revisionHeads = new File (new File (gitRepository, "jsvn"), "r" + revision);
			
			List<String> lines = FileUtils.readLines(revisionHeads, "UTF-8");
			
			Map<String, Ref>allRefs = new HashMap<String, Ref>();
			
			for (Map.Entry<String, Ref> entry : repo.getAllRefs().entrySet()) {
				String key = entry.getKey();
				
				/*
				 * only care about active ref's
				 */
				if (!key.contains("@"))
					allRefs.put(entry.getKey(), entry.getValue());
			}

			for (String line : lines) {
				
				String parts[] = line.split("::");
				
				if (parts.length != 2)
					continue;
				
				String branchName = parts[0];
				
				if (branchName.contains("@"))
					continue;
				
				String branchStringId = parts[1];
				
				ObjectId branchId = ObjectId.fromString(branchStringId);
				
				Ref branchRef = repo.getRef(branchName);
				
				if (branchRef == null) {
					// create the reference
					updateRef(repo, branchName, revision, branchId);
				}
				else if (!branchRef.getObjectId().equals(branchId)) {
					// exists but is pointing at the wrong object id.
					// move the reference
					
					updateRef(repo, branchName, revision, branchId);
					
					
				}
				else {
					
					log.info("skipped " + branchName + " as it correctly pointed at " + branchStringId);
				}
				allRefs.remove(branchName);
				
			}
			
			if (allRefs.size() > 0) {
				// should we delete these refs
				log.info("should we delete these?");
				
				for (Ref ref : allRefs.values()) {
					
					deleteRef(repo, ref.getName(), revision);
					
				}
			}
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
