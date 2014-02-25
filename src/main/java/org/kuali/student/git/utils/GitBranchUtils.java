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
package org.kuali.student.git.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.kuali.student.svn.tools.merge.tools.BranchUtils;
import org.kuali.student.svn.tools.merge.tools.BranchUtils.IBranchTagAssist;

/**
 * @author Kuali Student Team
 * 
 */
public class GitBranchUtils {

	/*
	 * 255 bytes but not sure on the null byte so reducing by one.
	 */
	public static final int FILE_SYSTEM_NAME_LIMIT = 254;

	/*
	 * If a branch name is a long branch name it will be 40 characters long (the length of the sha-1 hash)
	 */
	public static final int LONG_BRANCH_NAME_LENGTH = 40;

	private static final String DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY = "deploymentlab/test/branches/proposalhistory";

	private static final String DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY = "deploymentlab/branches/proposalhistory";

	private static final String DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK = "deploymentlab/student/trunk/ks-tools/maven-component-sandbox/trunk";

	private static final String DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK = "deploymentlab/UI/ks-cuke-testing/trunk";
	
	private static final String BRANCHES_INACTIVE = "branches/inactive";
	private static final String BUILD_DASH = "build-";
	private static final String TAGS_BUILDS = "tags/builds";
	private static final String KS_CFG_DBS = "ks-cfg-dbs";
	private static final String DICTIONARY = "dictionary";
	private static final String SANDBOX = "sandbox";
	private static final String ENUMERATION = "enumeration";
	private static final String TRUNK = "trunk";
	private static final String BRANCHES = "branches";
	private static final String TAGS = "tags";
	private static final String DEPLOYMENTLAB = "deploymentlab";
	
	private static final String TOOLS_MAVEN_DICTIONARY_GENERATOR = "tools/maven-dictionary-generator";
	private static final String DEPLOYMENTLAB_KS_CUKE_TESTING = "deploymentlab/ks-cuke-testing";
	private static final String DEPLOYMENTLAB_UI_KS_CUKE_TESTING = "deploymentlab/UI/ks-cuke-testing";
	private static final String SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR = "sandbox/team2/ks-rice-standalone/branches/ks-rice-standalone-uberwar";
	private static final String KS_WEB_BRANCHES_KS_WEB_DEV = "ks-web/branches/ks-web-dev";

	/**
	 * 
	 */
	public GitBranchUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static interface ILargeBranchNameProvider {

		/**
		 * Pass in the sha-1 id of the branch and if known the full branch name will be returned.
		 * @param longBranchId
		 * @return
		 */
		String getBranchName(String longBranchId, long revision);

		String storeLargeBranchName(String branchName, long revision);
		
	}

	
	public static String getCanonicalBranchName(String branchPath, long revision, ILargeBranchNameProvider provider) {
		
		
		String branchName = convertPathToBranchName(branchPath);
		
		/*
		 * Consider the
		 */
		if ((Constants.R_HEADS.length() + branchPath.length()) >= FILE_SYSTEM_NAME_LIMIT) {
			// need to store the long name
			String shortBranchName = provider.storeLargeBranchName(branchName, revision);
			
			return shortBranchName;
		}
		else 
			return branchName;

	}
	
	private static String convertPathToBranchName(String branchPath) {
		if (branchPath.contains("_")) {
			/*
			 * Special case we need to convert the underscore to === first. 
			 */
			String convertedBranchPath = branchPath.replace("_", "===");

			return convertedBranchPath.replaceAll("\\/", "_");
		}
		else
			return branchPath.replaceAll("\\/", "_");
	}

	/**
	 * Compute the objectid of the branch name given.
	 * 
	 * @param branchName
	 * @return
	 */
	public static ObjectId getBranchNameObjectId(String branchName) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			
			byte[] branchNameBytes = Constants.encode(branchName);
			
			md.update(branchNameBytes);		
			
			return ObjectId.fromRaw(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("failed to get SHA-1 digest Message Digest.", e);
		}
	}

	public static String getBranchPath(String branchName, long revision, ILargeBranchNameProvider provider) {
		
		if (branchName.length() == LONG_BRANCH_NAME_LENGTH) {
			// could be a large branch path
			String longBranchName = provider.getBranchName(branchName, revision);
			
			if (longBranchName != null)
				return convertBranchNameToPath(longBranchName);
			
			// else fall through to the logic below
		}
		
		return convertBranchNameToPath(branchName);
	}

	private static String convertBranchNameToPath(String branchName) {
		
		String path = branchName.replaceAll("_", "/").replaceAll("===", "_");
		
		return path;
	}

	public static BranchData parse(String path) throws VetoBranchException {
		return BranchUtils.parse(0L, path, new IBranchTagAssist() {

			@Override
			public BranchData parseBranch(Long revision, String path,
					String[] parts) throws VetoBranchException {

				if (parts.length == 1 && !path.equals(TRUNK)) {

					throw new VetoBranchException(path
							+ " vetoed because length is only one part");
				}
				
				String lastPart = parts[parts.length-1];
				
				if (BRANCHES.equals(lastPart) || TAGS.equals(lastPart))
					throw new VetoBranchException(path + " vetoed because it is incomplete.");

				if (!(isPathValidBranchTagOrTrunk(path))) {

					/*
					 * If it starts with enumeration allow it to be treated as a
					 * branch.
					 * 
					 * sandbox is also a special case.
					 */
					if (!(path.startsWith(ENUMERATION)
							|| path.startsWith(SANDBOX)
							|| path.startsWith(DICTIONARY) || path
							.startsWith(KS_CFG_DBS) || path.startsWith(DEPLOYMENTLAB) || path.startsWith("tools")))
						throw new VetoBranchException(
								path
										+ "vetoed because it does not contain tags, branches or trunk");
				}

				/*
				 * Custom whitelist for tricky paths
				 */
				
				if (path.startsWith(KS_WEB_BRANCHES_KS_WEB_DEV)) {
					
					return buildBranchData(revision, path, KS_WEB_BRANCHES_KS_WEB_DEV);
				}
				else if (path.startsWith(DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY)) {
					return buildBranchData(revision, path, DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY);
				}
				else if (path.startsWith(DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY)) {
					return buildBranchData (revision, path, DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY);
				}
				else if (path.startsWith(SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR)) {
				
					return buildBranchData(revision, path, SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR);
				}
				else if (path.startsWith(DEPLOYMENTLAB_UI_KS_CUKE_TESTING) && !isPathValidBranchTagOrTrunk(path)) {
					return buildBranchData(revision, path, DEPLOYMENTLAB_UI_KS_CUKE_TESTING);
				}
				else if (path.startsWith(DEPLOYMENTLAB_KS_CUKE_TESTING)) {
					return buildBranchData(revision, path, DEPLOYMENTLAB_KS_CUKE_TESTING);
				}
				else if (path.startsWith(DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK)) {
					
					return buildBranchData(revision, path, DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK);
				}
				else if (path.startsWith(DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK)) {
					return buildBranchData(revision, path, DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK);
				}
				else if (path.startsWith(TOOLS_MAVEN_DICTIONARY_GENERATOR) && !isPathValidBranchTagOrTrunk(path)) {
					/*
					 * BranchUtils.parse can find the trunk if it exists.
					 * 
					 * only make the branch if there is no trunk in the path.
					 */
					return buildBranchData(revision, path, TOOLS_MAVEN_DICTIONARY_GENERATOR);
				}
				else if (path.contains(TAGS_BUILDS)) {
					
					int partContainingBuildNameIndex = -1;
					
					for (int i = parts.length-1; i >= 0; i--) {
						
						String canidatePart = parts[i];
						
						if (canidatePart.contains(BUILD_DASH)) {
							partContainingBuildNameIndex = i;
							break;
						}
					}
					
					if (partContainingBuildNameIndex != -1 && partContainingBuildNameIndex < parts.length) {
						return buildBranchData(revision, parts, partContainingBuildNameIndex);
					}
					
					// else fall through to return null below
					
				}
				else if (path.contains(BRANCHES_INACTIVE)) {
					
					int branchesPartIndex = -1;
					
					for (int i = 0; i < parts.length; i++) {
						String canidatePart = parts[i];
						
						if (canidatePart.equals(BRANCHES)) {
							branchesPartIndex = i;
							break;
						}
					}
					
					if (branchesPartIndex != -1) {
						int inactvePartIndex = branchesPartIndex+1;
						int branchNamePartIndex = inactvePartIndex+1;
						
						if (branchNamePartIndex < parts.length) {
							return buildBranchData(revision, parts, branchNamePartIndex);
						}
						// else fall through
					}
					
					// else fall through to return null below
					
				}
				else if ((path.startsWith(ENUMERATION) || path.startsWith(DICTIONARY) || path.startsWith(KS_CFG_DBS) || path.startsWith(DEPLOYMENTLAB)) && !isPathValidBranchTagOrTrunk(path)) {
					return buildBranchData(revision, path, 1);
				}
				
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
		});
	}

	public static String convertToTargetPath(String path, String copyFromPath, String blobPath) throws VetoBranchException {
		
		BranchData copyFromBranch = parse(copyFromPath);
		
		StringBuilder alteredBlobPrefixPath = new StringBuilder(path);
		
		/*
		 * Make sure that the point we join has only one slash.
		 * 
		 * We put the slash on the prefix and remove if found on the suffix.
		 * 
		 */
		if (alteredBlobPrefixPath.charAt(alteredBlobPrefixPath.length()-1) != '/')
			alteredBlobPrefixPath.append("/");
		
		StringBuilder alteredBlobSuffixPath = new StringBuilder(blobPath.substring(copyFromBranch.getPath().length()));
		
		if (alteredBlobSuffixPath.charAt(0) == '/')
			alteredBlobSuffixPath.delete(0, 1);
		
		String alteredBlobPath = alteredBlobPrefixPath.append(alteredBlobSuffixPath.toString()).toString();
		
		return alteredBlobPath;
	}

}
