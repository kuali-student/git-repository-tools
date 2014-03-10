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

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.svn.tools.merge.model.BranchData;

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
			
			/*
			 * And convert any spaces to ---
			 */
			convertedBranchPath = branchPath.replace(" ", "---");

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
		
		String path = branchName.replaceAll("_", "/").replaceAll("===", "_").replaceAll(" ", "---");
		
		return path;
	}

	public static String convertToTargetPath(String path, long copyFromRevision, String copyFromPath, String blobPath, BranchDetector branchDetector) throws VetoBranchException {
		
		BranchData copyFromBranch = branchDetector.parseBranch(copyFromRevision, copyFromPath);
		
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
