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
package org.kuali.student.git.model.branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.svn.tools.merge.model.BranchData;

/**
 * @author Kuali Student Team
 *
 */
public class BranchDetectorImpl implements BranchDetector {

	private Set<String> invalidBeforeTagPaths = new HashSet<String>();;
	private Set<String> standardBackwardMatchPaths = new HashSet<String>();
	private Set<String> backwardMatchPaths = new HashSet<String>();;
	private Set<String> forwardMatchPaths = new HashSet<String>();;

	/**
	 * 
	 */
	public BranchDetectorImpl() {
		// TODO Auto-generated constructor stub
	}

	

	/**
	 * @param invalidBeforeTagPaths the invalidBeforeTagPaths to set
	 */
	public void setInvalidBeforeTagPaths(Set<String> invalidBeforeTagPaths) {
		this.invalidBeforeTagPaths = invalidBeforeTagPaths;
	}



	/**
	 * @param standardBackwardMatchPaths the standardBackwardMatchPaths to set
	 */
	public void setStandardBackwardMatchPaths(Set<String> standardBackwardMatchPaths) {
		this.standardBackwardMatchPaths = standardBackwardMatchPaths;
	}



	/**
	 * @param backwardMatchPaths the backwardMatchPaths to set
	 */
	public void setBackwardMatchPaths(Set<String> backwardMatchPaths) {
		this.backwardMatchPaths = backwardMatchPaths;
	}



	/**
	 * @param forwardMatchPaths the forwardMatchPaths to set
	 */
	public void setForwardMatchPaths(Set<String> forwardMatchPaths) {
		this.forwardMatchPaths = forwardMatchPaths;
	}



	/* (non-Javadoc)
	 * @see org.kuali.student.git.model.branch.BranchDetector#parseBranch(java.lang.Long, java.lang.String, java.lang.String[])
	 */
	@Override
	public BranchData parseBranch(Long revision, String path)
			throws VetoBranchException {

		String[] parts = path.split("\\/");
		
		List<String> branchPathList = new ArrayList<String>();
		List<String> pathList = new ArrayList<String>();

		boolean foundBranch = false;
		boolean onPath = false;

		boolean foundTrunk = false;
		
		int beforePathRootIndex = Integer.MAX_VALUE;

		beforePathRootIndex = lastIndexOfKey(parts, "trunk", true);

		if (beforePathRootIndex == -1) {
			beforePathRootIndex = lastIndexOfKey(parts, "tags",
					invalidBeforeTagPaths, true);
		}

		if (beforePathRootIndex == -1) {
			beforePathRootIndex = lastIndexOfKey(parts, "old-tags",
					invalidBeforeTagPaths, true);
		}

		if (beforePathRootIndex == -1) {
			beforePathRootIndex = indexOfKey(parts, standardBackwardMatchPaths,
					true);
		}

		if (beforePathRootIndex == -1) {
			beforePathRootIndex = indexOfKey(parts, backwardMatchPaths, false);
		}

		if (beforePathRootIndex == -1) {
			beforePathRootIndex = indexOfKey(parts, forwardMatchPaths, false);
		}

		if (beforePathRootIndex == -1) {

			// no branches tags or sandbox found
			// treat the path as the branch

			branchPathList.addAll(Arrays.asList(parts));

		} else {

			/*
			 * on trunk the next element is the path
			 * 
			 * on branches the next element is the name of the branch and part
			 * of the branches path
			 */

			String canidatePart = parts[beforePathRootIndex].toLowerCase();

			if (canidatePart.equals("trunk")
					|| forwardMatchPaths.contains(canidatePart)) {

				parseTrunkParts(beforePathRootIndex, parts, branchPathList,
						pathList);

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

					// skips over the branch name?

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



	/*
	 * @param backwards if true search the parts from last to first.
	 */
	private  int lastIndexOfKey(String parts[], String key,
			Set<String> invalidBeforeParts, boolean backwards) {

		Map<String, Set<String>> invalidBeforePartsMap = new HashMap<String, Set<String>>();

		invalidBeforePartsMap.put(key, invalidBeforeParts);

		return indexOfKey(parts,
				new HashSet<String>(Arrays.asList(new String[] { key })),
				invalidBeforePartsMap, backwards);
	}

	private  int indexOfKey(String parts[], Set<String> keys,
			Map<String, Set<String>> invalidBeforePartsMap, boolean backwards) {

		if (backwards) {
			for (int i = (parts.length - 1); i >= 0; i--) {
				String part = parts[i].toLowerCase();

				if (keys.contains(part)) {

					Set<String> invalidBeforeParts = invalidBeforePartsMap
							.get(part);

					if (invalidBeforeParts != null) {
						String beforePartString = StringUtils.join(parts, '/',
								0, i);

						boolean invalidBeforePartsFlag = false;

						for (String invalidBeforePart : invalidBeforeParts) {

							if (beforePartString.matches(".*"
									+ invalidBeforePart + ".*")) {
								invalidBeforePartsFlag = true;
								break;
							}
						}

						if (invalidBeforePartsFlag)
							continue; // skip over this part
					}
					return i;
				}
			}
		} else {
			// forward
			for (int i = 0; i < parts.length; i++) {
				String part = parts[i].toLowerCase();

				if (keys.contains(part))
					return i;
			}

		}

		// failed to find a match case

		return -1;
	}

	/**
	 * Split the path into the branch part and the file path part.
	 * 
	 * @param path
	 * @return the determined branch data.
	 * @throws VetoBranchException
	 */

	


	private  int indexOfKey(String[] parts,
			Set<String> standardBackwardMatchPaths, boolean backwards) {
		return indexOfKey(parts, standardBackwardMatchPaths,
				new HashMap<String, Set<String>>(), backwards);
	}

	private  int lastIndexOfKey(String[] parts, String key,
			boolean backwards) {
		return lastIndexOfKey(parts, key, new HashSet<String>(), backwards);
	}

	private  int lastIndexOfKey(String[] parts, String part,
			String disallowedBeforePart, boolean backwards) {

		Set<String> disallowedBeforePartSet = new HashSet<String>();

		disallowedBeforePartSet.add(disallowedBeforePart);

		return lastIndexOfKey(parts, part, disallowedBeforePartSet, backwards);
	}

	private  void parseTrunkParts(int beforePathRootIndex,
			String[] parts, List<String> branchPathList, List<String> pathList) {
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

	}


}
