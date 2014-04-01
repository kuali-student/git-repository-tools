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
import org.kuali.student.git.model.exceptions.VetoBranchException;

/**
 * @author Kuali Student Team
 *
 */
public class KualiStudentBranchDetectorImpl implements BranchDetector {
	

	private static final String CONTRIB_CM = "contrib/CM";

	private static final String CONTRIB_CM_AGGREGATE = "contrib/CM/aggregate";

	private static final String CONTRIB_CM_KS_LUM = "contrib/CM/ks-lum";

	private static final String CONTRIB_CM_KS_CORE = "contrib/CM/ks-core";

	private static final String CONTRIB_CM_KS_API = "contrib/CM/ks-api";

	private static final String CONTRIB_CM_KS_DEPLOYMENTS = "contrib/CM/ks-deployments";

	private static final String TEST_FUNCTIONAL_AUTOMATION_SAMBAL_RICE_2_3_M2_AFT_BRANCH = "test/functional-automation/sambal/rice_2_3_m2_AFT_branch";

	private static final String SANDBOX_DEPLOYMENTLAB = "sandbox/deploymentlab";

	private static final String POC_SRC = "poc/src";

	private static final String EXAMPLES_TRAINING_CM_MYSCHOOL_CONFIG_PROJECT = "examples/training/cm-myschool-config-project";

	private static final String EXAMPLES_SAMPLE_CONFIG_PROJECT = "examples/sample-config-project";

	private static final String EXAMPLES_KS_CORE_TUTORIAL = "examples/ks-core-tutorial";

	private static final String EXAMPLES = "examples";

	private static final String SUSHIK_COMPONENT_MANUAL_IMPL = "sushik-component-manual-impl";

	protected static final String DATE_STAMP_SVN_REVISION_BUILD_TAG_NAME_PATTERN = "[0-9]{8}-r[0-9]+"; 

	private static final String DEPLOYMENTLAB_KS_1_1_DB_MAVEN_SITE_PLUGIN = "deploymentlab/ks-1.1-db/maven-site-plugin";

	private static final String DEPLOYMENTLAB_KS_1_1_DB_1_0_X = "deploymentlab/ks-1.1-db/1.0.x";

	private static final String MERGES = "merges";

	private static final String TOOLS = "tools";
	
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
	private static final String OLD_TAGS = "old-tags";
	private static final String OLD_BUILD_TAGS = "old-build-tags";
	private static final String KS_OLD_DIRECTORY_STRUCTURE = "old-directory-structure";
	private static final String DEPLOYMENTLAB = "deploymentlab";
	
	private static final String TOOLS_MAVEN_DICTIONARY_GENERATOR = "tools/maven-dictionary-generator";
	private static final String DEPLOYMENTLAB_KS_CUKE_TESTING = "deploymentlab/ks-cuke-testing";
	private static final String DEPLOYMENTLAB_UI_KS_CUKE_TESTING = "deploymentlab/UI/ks-cuke-testing";
	private static final String SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR = "sandbox/team2/ks-rice-standalone/branches/ks-rice-standalone-uberwar";
	private static final String KS_WEB_BRANCHES_KS_WEB_DEV = "ks-web/branches/ks-web-dev";

	/**
	 * 
	 */
	public KualiStudentBranchDetectorImpl() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public BranchData parseBranch(Long revision, String path) throws VetoBranchException {

		String[] parts = path.split("\\/");
		
		if (parts.length == 1 && !path.equals(TRUNK)) {

			throw new VetoBranchException(path
					+ " vetoed because length is only one part");
		}
		
		String lastPart = parts[parts.length-1];
		
		if (BRANCHES.equals(lastPart) || TAGS.equals(lastPart) || OLD_TAGS.equals(lastPart) || OLD_BUILD_TAGS.equals(lastPart))
			throw new VetoBranchException(path + " vetoed because it is incomplete.");
		
		
		if (TAGS.equals(parts[0]) && KS_OLD_DIRECTORY_STRUCTURE.equals(lastPart))
			throw new VetoBranchException (TAGS + "/" + KS_OLD_DIRECTORY_STRUCTURE + " is not a valid branch by itself, its children are valid individually.");
		

		if (!(isPathValidBranchTagOrTrunk(path))) {

			if (path.startsWith(SUSHIK_COMPONENT_MANUAL_IMPL)) {
				return buildBranchData(revision, path, 1);
			}
			
			if (path.startsWith(POC_SRC)) {
				
				/*
				 * Create poc branch where there is a src directory directly under it.
				 * See at r30766
				 */
				return buildBranchData(revision, path, 1);
			}
			
			if (path.startsWith(TEST_FUNCTIONAL_AUTOMATION_SAMBAL_RICE_2_3_M2_AFT_BRANCH)) {
				return buildBranchData(revision, path, TEST_FUNCTIONAL_AUTOMATION_SAMBAL_RICE_2_3_M2_AFT_BRANCH);
			}
			
			BranchData bd = handleContribCMRoots(revision, path, parts);
			
			if (bd != null)
				return bd;
			
			/*
			 * If it starts with enumeration allow it to be treated as a
			 * branch.
			 * 
			 * sandbox is also a special case.
			 */
			if (!(path.startsWith(ENUMERATION) 
					|| path.startsWith(SANDBOX)
					|| path.startsWith(DICTIONARY) || path
					.startsWith(KS_CFG_DBS) || path.startsWith(DEPLOYMENTLAB) || path.startsWith(TOOLS) || path.startsWith(MERGES) || path.startsWith(EXAMPLES)))
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
		else if (path.startsWith(DEPLOYMENTLAB)) {
			
			BranchData bd = handleStartsWithDeploymentlab(revision, path, parts);
			
			if (bd != null)
				return bd;
		}
		if (path.startsWith(SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR)) {

			return buildBranchData(revision, path,
					SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR);
		} else if (path.startsWith(SANDBOX_DEPLOYMENTLAB)) {

			BranchData bd = handleStartsWithDeploymentlab(revision, path,
					parts, SANDBOX.length() + 1);

			if (bd != null)
				return bd;

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
				
				String candidatePart = parts[i];
				
				if ((candidatePart.contains(BUILD_DASH) && !candidatePart.equals("build-details.xml")) || candidatePart.matches(DATE_STAMP_SVN_REVISION_BUILD_TAG_NAME_PATTERN)) {
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
		else if (path.startsWith(MERGES)) { 
			int branchNameIndex = 1;
			
			if (branchNameIndex < parts.length) {
			
				return buildBranchData(revision, path, branchNameIndex+1);
			}
			// else fall through and return null
			
		}
		else if (path.startsWith(EXAMPLES)) {
			
			if (path.startsWith(EXAMPLES_KS_CORE_TUTORIAL)) {
				return buildBranchData(revision, path, EXAMPLES_KS_CORE_TUTORIAL);
			}
			else if (path.startsWith(EXAMPLES_SAMPLE_CONFIG_PROJECT)) {
				return buildBranchData(revision, path, EXAMPLES_SAMPLE_CONFIG_PROJECT);
			}
			else if (path.startsWith(EXAMPLES_TRAINING_CM_MYSCHOOL_CONFIG_PROJECT)) {
				return buildBranchData(revision, path, EXAMPLES_TRAINING_CM_MYSCHOOL_CONFIG_PROJECT);
			}
			// else fall through
		}
		else if ((path.startsWith(ENUMERATION) || path.startsWith(DICTIONARY) || path.startsWith(KS_CFG_DBS) || path.startsWith(DEPLOYMENTLAB) ) && !isPathValidBranchTagOrTrunk(path)) {
			return buildBranchData(revision, path, 1);
		}
		else if (path.startsWith(CONTRIB_CM)) {
			return handleContribCMRoots(revision, path, parts);
		}
		
		return null;
	}

	private BranchData handleContribCMRoots(Long revision, String path, String[] parts) {
		
		if (parts.length < 4)
			return null;
		
		String nextPart = parts[3];
		
		if (nextPart.equals(TRUNK) || nextPart.equals(BRANCHES) || nextPart.equals(TAGS))
			return null;  // let the base branch detector handle these.
		
		if (path.startsWith(CONTRIB_CM_KS_DEPLOYMENTS)) {
			return buildBranchData(revision, path, CONTRIB_CM_KS_DEPLOYMENTS);
		}
		else if (path.startsWith(CONTRIB_CM_KS_API)) {
			return buildBranchData(revision, path, CONTRIB_CM_KS_API);
		}
		else if (path.startsWith(CONTRIB_CM_KS_CORE)) {
			return buildBranchData(revision, path, CONTRIB_CM_KS_CORE);
		}
		else if (path.startsWith(CONTRIB_CM_KS_LUM)) {
			return buildBranchData(revision, path, CONTRIB_CM_KS_LUM);
		}
		else if (path.startsWith(CONTRIB_CM_AGGREGATE)) {
			return buildBranchData(revision, path, CONTRIB_CM_AGGREGATE);
		}
		else
			return null;
	}


	private BranchData handleStartsWithDeploymentlab(Long revision,
			String path, String[] parts) {
		return handleStartsWithDeploymentlab(revision, path, parts, 0);
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
		
		if (offsetPath.startsWith(DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY)) {
			return buildBranchData(revision, path, prefixPath + DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY);
		}
		else if (offsetPath.startsWith(DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY)) {
			return buildBranchData (revision, path, prefixPath + DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY);
		}
		else if (offsetPath.startsWith(DEPLOYMENTLAB_UI_KS_CUKE_TESTING) && !isPathValidBranchTagOrTrunk(path)) {
			return buildBranchData(revision, path, prefixPath + DEPLOYMENTLAB_UI_KS_CUKE_TESTING);
		}
		else if (offsetPath.startsWith(DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK)) {
			
			return buildBranchData(revision, path, prefixPath + DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK);
		}
		else if (offsetPath.startsWith(DEPLOYMENTLAB_KS_CUKE_TESTING)) {
			return buildBranchData(revision, path, prefixPath + DEPLOYMENTLAB_KS_CUKE_TESTING);
		}
		else if (offsetPath.startsWith(DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK)) {
			return buildBranchData(revision, path, prefixPath + DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK);
		}
		else if (offsetPath.startsWith(DEPLOYMENTLAB_KS_1_1_DB_1_0_X)) {
			return buildBranchData(revision, path, prefixPath + DEPLOYMENTLAB_KS_1_1_DB_1_0_X);
		}
		else if (offsetPath.startsWith(DEPLOYMENTLAB_KS_1_1_DB_MAVEN_SITE_PLUGIN)) {
			return buildBranchData(revision, path, prefixPath + DEPLOYMENTLAB_KS_1_1_DB_MAVEN_SITE_PLUGIN);
		}
		
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
