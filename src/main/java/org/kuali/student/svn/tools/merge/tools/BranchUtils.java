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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * @author Kuali Student Team
 * 
 */
public final class BranchUtils {

	private static final Logger log = LoggerFactory
			.getLogger(BranchUtils.class);

	public static final class BranchData {
		Long revision;

		String branchPath;
		String path;

		/**
		 * @param branchPath
		 * @param path
		 */
		public BranchData(Long revision, String branchPath, String path) {
			super();
			this.revision = revision;
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

		/**
		 * @return the revision
		 */
		public Long getRevision() {
			return revision;
		}

		public boolean isSandbox() {
			return branchPathContains("sandbox");
		}

		public boolean isBranch() {
			return branchPathContains("branches");
		}

		public boolean isTag() {

			return branchPathContains("tags");
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
	 * 
	 * @param path
	 * @return the base name of the file on the path.
	 */
	public static String baseFileName(String path) {
		String parts[] = path.split("\\/");

		return parts[parts.length - 1];
	}

	/**
	 * Split the path into the branch part and the file path part.
	 * 
	 * @param path
	 * @return the determined branch data.
	 */
	public static BranchData parse(Long revision, String path) {

		String parts[] = path.trim().split("\\/");

		List<String> branchPathList = new ArrayList<String>();
		List<String> pathList = new ArrayList<String>();

		boolean foundBranch = false;
		boolean onPath = false;

		boolean foundTrunk = false;
		int beforePathRootIndex = Integer.MAX_VALUE;

		for (int i = parts.length - 1; i >= 0; i--) {
			String part = parts[i];

			if (part.toLowerCase().equals("branches")
					|| part.toLowerCase().equals("tags")
					|| part.toLowerCase().equals("sandbox")
					|| part.toLowerCase().equals("trunk")
					|| part.toLowerCase().equals("tools")
					|| part.toLowerCase().equals("examples")
					|| part.toLowerCase().equals("poc")) {

				if (part.toLowerCase().equals("trunk"))
					foundTrunk = true;

				beforePathRootIndex = i;
				break;
			}
		}

		if (beforePathRootIndex == Integer.MAX_VALUE) {

			// special cases
			if (parts[0].equals("enumeration")) {

				branchPathList.add(parts[0]);
				branchPathList.add(parts[1]);

				for (int i = 2; i < parts.length; i++) {
					pathList.add(parts[i]);
				}
			} else {

				// no branches tags or sandbox found
				// treat the path as the branch

				branchPathList.addAll(Arrays.asList(parts));
			}

		} else {

			/*
			 * on trunk the next element is the path
			 * 
			 * on branches the next element is the name of the branch and part
			 * of the branches path
			 */

			if (foundTrunk) {

				int pathNameStartIndex = beforePathRootIndex + 1;

				if (parts.length < pathNameStartIndex) {
					// there is no part after the branches part
					for (int i = 0; i <= beforePathRootIndex; i++) {
						branchPathList.add(parts[i]);
					}

				} else {

					for (int i = 0; i <= beforePathRootIndex; i++) {
						branchPathList.add(parts[i]);
					}

					for (int i = pathNameStartIndex; i < parts.length; i++) {
						pathList.add(parts[i]);
					}

				}
			} else {
				int branchNameIndex = beforePathRootIndex + 1;

				int pathNameStartIndex = branchNameIndex + 1;

				if (parts.length < pathNameStartIndex) {
					// there is no part after the branches part
					for (int i = 0; i <= beforePathRootIndex; i++) {
						branchPathList.add(parts[i]);
					}

				} else {

					for (int i = 0; i <= branchNameIndex; i++) {
						branchPathList.add(parts[i]);
					}

					for (int i = pathNameStartIndex; i < parts.length; i++) {
						pathList.add(parts[i]);
					}

				}
			}
		}

		// make sure there are path elements
		if (pathList.size() == 0) {
			// this is a atypical path with no branches or tags
			// put the whole name into the branches part
			return new BranchData(revision, StringUtils.join(branchPathList,
					"/"), "");
		} else
			return new BranchData(revision, StringUtils.join(branchPathList,
					"/"), StringUtils.join(pathList, "/"));
	}

}
