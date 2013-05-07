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
package org.kuali.student.svn.tools.merge.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public final class BranchUtils {

	private static final Logger log = LoggerFactory.getLogger(BranchUtils.class);
	
	public static final class BranchData {
		String branchPath;
		String path;
		/**
		 * @param branchPath
		 * @param path
		 */
		public BranchData(String branchPath, String path) {
			super();
			this.branchPath = branchPath;
			this.path = path;
		}
		/**
		 * @return the prefix
		 */
		public String getBranchPath() {
			return branchPath;
		}
		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}
		
		public boolean isSandbox() {
			return branchPathContains("sandbox");
		}
		
		public boolean isBranch() {
			return branchPathContains("branches");
		}
		
		public boolean isTag() {
			
			return branchPathContains ("tags");
		}
		private boolean branchPathContains(String test) {
			
			if (branchPath.contains(test))
				return true;
			else
				return false;
			
		}
		
		
	}
	
	public static final class PathData {
		private Long addedRevision;
		private Long deletedRevision;
		
		private String path;
		
	}
	/**
	 * 
	 */
	private BranchUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Return the basename of the file on the path given.
	 * @param path
	 * @return the base name of the file on the path.
	 */
	public static String baseFileName (String path) {
		String parts[] = path.split("\\/");
		
		return parts[parts.length-1];
	}
	
	/**
	 * Split the path into the branch part and the file path part.
	 * 
	 * @param path
	 * @return the determined branch data.
	 */
	public static BranchData parse(String path) {
		
		String parts[] = path.trim().split("\\/");
		
		List<String> branchPathList = new ArrayList<String>();
		List<String>pathList = new ArrayList<String>();
		
		boolean foundBranch = false;
		boolean onPath = false;
		
		for (String part : parts) {
			
			if (part.toLowerCase().equals("branches") || part.toLowerCase().equals("tags") || part.toLowerCase().equals("sandbox")) {
				foundBranch = true;
				branchPathList.add(part);
			}
			else {
				if (!onPath && foundBranch) {
					onPath = true;
					branchPathList.add(part);
				}
				else if (onPath)
					pathList.add(part);
				else
					branchPathList.add(part);
					
			}
			
		}
		
		// make sure there are path elements
		if (pathList.size() == 0) {
			// this is a atypical path with no branches or tags
			return new BranchData("", StringUtils.join(branchPathList, "/"));
		}
		else
			return new BranchData(StringUtils.join(branchPathList, "/"), StringUtils.join(pathList, "/"));
	}
	
}
