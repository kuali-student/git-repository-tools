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

import org.apache.commons.lang3.StringUtils;
import org.kuali.student.branch.model.BranchData;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.kuali.student.git.model.branch.utils.GitBranchUtils;



/**
 * @author Kuali Student Team
 *
 */
public class KualiFoundationBranchDetectorImpl implements BranchDetector {
	

	private static final String RICE_IMPEX_RICE_IMPEX_MASTER_1_0_3 = "rice/impex/rice-impex-master-1.0.3";
	
	private static final String BUILD_DASH = "build-";
	private static final String TAGS_BUILDS = "tags/builds";
	private static final String TRUNK = "trunk";
	private static final String BRANCHES = "branches";
	private static final String TAGS = "tags";
	private static final String OLD_TAGS = "old-tags";
	private static final String OLD_BUILD_TAGS = "old-build-tags";
	

	/**
	 * 
	 */
	public KualiFoundationBranchDetectorImpl() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public BranchData parseBranch(Long revision, String path) throws VetoBranchException {

		String[] parts = path.split("\\/");
		
		String lastPart = "";
		
		if (parts.length > 0) {
			lastPart = parts[parts.length-1];
		}
		if (parts.length == 1 && !path.contains(TRUNK) && !(path.contains(BRANCHES) && !lastPart.equals(BRANCHES))) {

			throw new VetoBranchException(path
					+ " vetoed because length is only one part");
		}
		
		if (!path.contains(TRUNK) && !(path.contains(BRANCHES) && !lastPart.equals(BRANCHES))) {
			if (BRANCHES.equals(lastPart) || TAGS.equals(lastPart) || OLD_TAGS.equals(lastPart) || OLD_BUILD_TAGS.equals(lastPart))
				throw new VetoBranchException(path + " vetoed because it is incomplete.");
		}

		if (!(isPathValidBranchTagOrTrunk(path))) {
			
			if (GitBranchUtils.startsWith(path, RICE_IMPEX_RICE_IMPEX_MASTER_1_0_3)) {
				return buildBranchData(revision, path, RICE_IMPEX_RICE_IMPEX_MASTER_1_0_3);
			}

				throw new VetoBranchException(
						path
								+ "vetoed because it does not contain tags, branches or trunk");
		}

		/*
		 * Custom whitelist for tricky paths
		 */
		
		 if (path.contains(TAGS_BUILDS)) {
			
			int partContainingBuildNameIndex = -1;
			
			for (int i = parts.length-1; i >= 0; i--) { 
				
				String candidatePart = parts[i];
				
				if ((candidatePart.contains(BUILD_DASH) && !candidatePart.equals("build-details.xml"))) {
					partContainingBuildNameIndex = i;
					break;
				}
			}
			
			if (partContainingBuildNameIndex != -1 && partContainingBuildNameIndex < parts.length) {
				return buildBranchData(revision, parts, partContainingBuildNameIndex);
			}
			
			// else fall through to return null below
			
		}
		
		return null;
	}

	
	/*
	 * The pathStartOffset allows the same matching code to be found for both ^deploymentlab and ^sandbox/deploymentlab
	 * 
	 */
	private BranchData handleStartsWithDeploymentlab(Long revision, String path, String[] parts, int pathStartOffset) {
		
		String prefixPath = path.substring(0, pathStartOffset);
		String offsetPath = path.substring(pathStartOffset);
		
		if (offsetPath.contains(TRUNK))
			return null; // let the default logic pick this up.
		
		
		// else
		return null;
	}


	private BranchData buildBranchData(Long revision, String path, int filePathStartIndex) {

		String[] parts = path.split("\\/");
		
		String branchPath = StringUtils.join(parts, '/', 0, filePathStartIndex);
		String filePath = StringUtils.join(parts, '/', filePathStartIndex, parts.length);
		
		return new BranchData(revision, branchPath, filePath);
	}

	private boolean isPathValidBranchTagOrTrunk(String path) {
		if (path.contains(TAGS) || path.contains(BRANCHES) || path
				.contains(TRUNK))
				return true;
		else
			return false;
				
	}

	private BranchData buildBranchData(Long revision, String path, String branchPath) {
		
		StringBuilder filePath = new StringBuilder(path.substring(branchPath.length()));
		
		if (filePath.length() > 0 &&filePath.charAt(0) == '/') 
			filePath.delete(0, 1);
		
		return new BranchData(revision, branchPath, filePath.toString());
		
	}

	private BranchData buildBranchData(Long revision, String[] parts,
			int lastPartOfBranchPathIndex) {
		
		String branchPath = StringUtils.join(parts, '/', 0, lastPartOfBranchPathIndex+1);
		
		String filePath = StringUtils.join(parts, '/', lastPartOfBranchPathIndex+1, parts.length);
		
		return new BranchData(revision, branchPath, filePath);
		
	}
}
