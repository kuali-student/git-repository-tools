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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

/**
 * 
 * @author Kuali Student Team
 *
 */
public class SvnRevisionMapper {

	public static class SvnRevisionMap {
		private long revision;
		private String branchName;
		private String commitId;
		/**
		 * @param branchName
		 * @param commitId
		 */
		public SvnRevisionMap(long revision, String branchName, String commitId) {
			super();
			this.revision = revision;
			this.branchName = branchName;
			this.commitId = commitId;
		}
		/**
		 * @return the branchName
		 */
		public String getBranchName() {
			return branchName;
		}
		/**
		 * @return the commitId
		 */
		public String getCommitId() {
			return commitId;
		}
		/**
		 * @return the revision
		 */
		public long getRevision() {
			return revision;
		}
		/**
		 * @param revision the revision to set
		 */
		public void setRevision(long revision) {
			this.revision = revision;
		}
		
	}
	private File revisonMappings;

	/**
	 * 
	 */
	public SvnRevisionMapper(Repository repo) {
		
		revisonMappings = new File (repo.getDirectory(), "jsvn");
		
		revisonMappings.mkdirs();
		
	}

	public void createRevisionMap(long revision, List<Ref>branchHeads) throws FileNotFoundException {
		
		File revisionFile = new File (revisonMappings, "r" + revision);
		
		PrintWriter pw = new PrintWriter(revisionFile);
		
		for (Ref branchHead : branchHeads) {
			pw.println(branchHead.getName() + "::" + branchHead.getObjectId().name());
		}
		
		pw.close();
	}
	
	/**
	 * Get the list of all references at the svn revision number given.
	 * 
	 * @param revision
	 * @return
	 * @throws IOException
	 */
	public List<SvnRevisionMap> getRevisionHeads (long revision) throws IOException {
		
		List<SvnRevisionMap>revisionHeads = new ArrayList<SvnRevisionMap>();
		
		File revisionFile = new File (revisonMappings, "r" + revision);
		
		List<String> lines = FileUtils.readLines(revisionFile, "UTF-8");
		
		for (String line : lines) {
			
			String[] parts = line.split ("::");
			
			revisionHeads.add(new SvnRevisionMap(revision, parts[0], parts[1]));
			
		}
		
		return revisionHeads;
		
	}

	/**
	 * Get the object id of the commit refered to by the branch at the revision given.
	 * 
	 * @param revision
	 * @param branchPath
	 * @return
	 * @throws IOException
	 */
	public ObjectId getRevisionBranchHead(long revision,
			String branchPath) throws IOException {
		
		File revisionFile = new File (revisonMappings, "r" + revision);
		
		List<String> lines = FileUtils.readLines(revisionFile, "UTF-8");
		
		for (String line : lines) {
			
			String[] parts = line.split ("::");
			
			if (parts[0].equals(Constants.R_HEADS + branchPath)) {
				ObjectId id = ObjectId.fromString(parts[1]);
				
				return id;
				
			}
			
		}
		
		// this is actually an exceptional case
		// if not found it means that the reference can't be found.
		return null;
	}
	
	
}
