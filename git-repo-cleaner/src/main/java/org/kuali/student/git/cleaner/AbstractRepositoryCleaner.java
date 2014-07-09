/*
 *  Copyright 2014 The Kuali Foundation Licensed under the
 *	Educational Community License, Version 2.0 (the "License"); you may
 *	not use this file except in compliance with the License. You may
 *	obtain a copy of the License at
 *
 *	http://www.osedu.org/licenses/ECL-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the License for the specific language governing
 *	permissions and limitations under the License.
 */
package org.kuali.student.git.cleaner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.student.git.model.graft.GitGraft;

/**
 * @author ocleirig
 *
 */
public abstract class AbstractRepositoryCleaner implements RepositoryCleaner {

	protected static final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");
	
	protected static final DateTimeFormatter includeHourAndMinuteDateFormatter = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm");
	
	
	private Repository repo;

	private String branchRefSpec = Constants.R_HEADS;

	private String externalGitCommandPath = null;

	private Map<ObjectId, GitGraft> grafts = new HashMap<ObjectId, GitGraft>();
	
	/**
	 * 
	 */
	public AbstractRepositoryCleaner() {
		
	}

	/**
	 * @return the repo
	 */
	protected Repository getRepo() {
		return repo;
	}

	/**
	 * @param repo the repo to set
	 */
	protected void setRepo(Repository repo) {
		this.repo = repo;
	}

	/**
	 * @return the branchRefSpec
	 */
	protected String getBranchRefSpec() {
		return branchRefSpec;
	}

	/**
	 * @param branchRefSpec the branchRefSpec to set
	 */
	protected void setBranchRefSpec(String branchRefSpec) {
		this.branchRefSpec = branchRefSpec;
	}

	/**
	 * @return the externalGitCommandPath
	 */
	protected String getExternalGitCommandPath() {
		return externalGitCommandPath;
	}

	/**
	 * @param externalGitCommandPath the externalGitCommandPath to set
	 */
	protected void setExternalGitCommandPath(String externalGitCommandPath) {
		this.externalGitCommandPath = externalGitCommandPath;
	}

	public void close() {
		
		if (repo != null)
			repo.close();
	}

	/*
	 * Load the grafts from the file name.
	 */
	protected void loadGrafts(String graftsFileName) throws IOException {

		List<String> graftLines = FileUtils.readLines(new File (graftsFileName));
		
		for (String graftLine : graftLines) {
			
			String parts[] = graftLine.split(" ");
			
			// part zero is the target commit
			
			Set<ObjectId>parents = new HashSet<>();
			
			for (int i = 1; i < parts.length; i++) {
				ObjectId parent = ObjectId.fromString(parts[i]);
				
				parents.add(parent);
			}
			
			ObjectId targetCommitId = ObjectId.fromString(parts[0]);
			
			GitGraft graft = new GitGraft(targetCommitId, parents);
			
			grafts.put(targetCommitId, graft);
			
		}
		
	}
	
	
	

}
