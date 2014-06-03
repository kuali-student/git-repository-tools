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
package org.kuali.student.git.model.branch.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.kuali.student.branch.model.BranchData;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 * 
 */
public class GitBranchUtils {

	private static final Logger log = LoggerFactory.getLogger(GitBranchUtils.class);
	
	protected static final String SPACE_CHARACTER = " ";
	protected static final String TILDE_CHARACTER = "~";
	protected static final String CARET_CHARACTER = "^";
	protected static final String COLON_CHARACTER = ":";
	protected static final String QUESTION_MARK_CHARACTER = "?";
	protected static final String OPEN_SQUARE_BRACE_CHARACTER = "[";
	protected static final String STAR_CHARACTER = "*";
	protected static final String BACKSLASH_CHARACTER = "\\";
	
	protected static final String FORWARD_SLASH_CHARACTER="/";
	protected static final String UNDERSCORE_CHARACTER="_";
	
	// allowed
	protected static final String FORWARD_SLASH_REPLACEMENT="_";
	
	// replacements for not allowed characters in a branch name
	protected static final String SPACE_REPLACEMENT = "---";

	protected static final String UNDERSCORE_REPLACEMENT = "===";

	protected static final String COLON_REPLACEMENT = "--=";
	
	protected static final String TILDE_REPLACEMENT = "-==";
	
	protected static final String CARET_REPLACEMENT = "--+";

	protected static final String QUESTION_MARK_REPLACEMENT = "-++";
	
	protected static final String OPEN_SQUARE_BRACE_REPLACEMENT = "+++";

	protected static final String STAR_REPLACEMENT = "++=";
	
	protected static final String BACKSLASH_REPLACEMENT = "+==";
	
	protected static final List<String>sizeOrderedReplacements = Arrays.asList(new String[] {OPEN_SQUARE_BRACE_REPLACEMENT, STAR_REPLACEMENT, BACKSLASH_REPLACEMENT, TILDE_REPLACEMENT, CARET_REPLACEMENT, QUESTION_MARK_REPLACEMENT, SPACE_REPLACEMENT, UNDERSCORE_REPLACEMENT, COLON_REPLACEMENT} );
	
	protected static final Map<String, String>CHARACTER_TO_REPLACEMENT_MAP = new HashMap<>();
	
	static {
		CHARACTER_TO_REPLACEMENT_MAP.put(SPACE_CHARACTER, SPACE_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(TILDE_CHARACTER, TILDE_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(CARET_CHARACTER, CARET_REPLACEMENT );
		CHARACTER_TO_REPLACEMENT_MAP.put(COLON_CHARACTER, COLON_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(QUESTION_MARK_CHARACTER, QUESTION_MARK_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(OPEN_SQUARE_BRACE_CHARACTER, OPEN_SQUARE_BRACE_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(STAR_CHARACTER, STAR_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(BACKSLASH_CHARACTER, BACKSLASH_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(FORWARD_SLASH_CHARACTER, FORWARD_SLASH_REPLACEMENT);
		CHARACTER_TO_REPLACEMENT_MAP.put(UNDERSCORE_CHARACTER, UNDERSCORE_REPLACEMENT);
		
	}
	
	protected static final Map<String, String>REPLACEMENT_TO_CHARACTER_MAP = new HashMap<>();
	
	static {
		
		for (Map.Entry<String, String> entry : CHARACTER_TO_REPLACEMENT_MAP.entrySet()) {
			String character = entry.getKey();
			String replacement = entry.getValue();
			
			REPLACEMENT_TO_CHARACTER_MAP.put(replacement, character);
			
		}
	}
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
	
	/**
	 * Parts based comparison to make sure that whole parts from path match whole parts from the prefix.
	 * 
	 * @param path
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith (String path, String prefix) {
		
		String[] pathParts = path.split("\\/");
		String[] prefixParts = prefix.split("\\/");
		
		if (pathParts.length < prefixParts.length)
			return false;
		
		// prefixParts.length can be < pathParts.length just not the other way.
		
		for (int i = 0; i < prefixParts.length; i++) {
			
			String pathPart = pathParts[i];
			String prefixPart = prefixParts[i];
			
			if (!pathPart.equals(prefixPart))
				return false;
		}
		
		
		return true;
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
	
	// protected for test purposes
	protected static String convertPathToBranchName(String branchPath) {
		
		
		
		StringBuilder convertedPathBuilder = new StringBuilder();
		
		Map<String, String>characterToReplacementMap = new HashMap<>(CHARACTER_TO_REPLACEMENT_MAP);
		
		/*
		 * Replace spaces first
		 */
		characterToReplacementMap.remove(SPACE_CHARACTER);
		
		String modifiedBranchPath = branchPath.replaceAll(SPACE_REPLACEMENT, SPACE_CHARACTER);
		
		/*
		 * Replace underscores first
		 */
		
		characterToReplacementMap.remove(UNDERSCORE_CHARACTER);
		
		modifiedBranchPath = branchPath.replaceAll(UNDERSCORE_REPLACEMENT, UNDERSCORE_CHARACTER);
		
		char[] charArray = modifiedBranchPath.toCharArray();
		
		
		for (int i = 0; i < charArray.length; i++) {
			
			String character = new Character (charArray[i]).toString();
			
			String replacement = CHARACTER_TO_REPLACEMENT_MAP.get(character);
			
			if (replacement != null)
				convertedPathBuilder.append(replacement);
			else
				convertedPathBuilder.append(character);
		}
				
		return convertedPathBuilder.toString();
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

	// protected for test purposes
	protected static String convertBranchNameToPath(String branchName) {
		
		String convertedBranchPath = branchName;
		
		Map<String,String>replacementToCharacterMap = new HashMap<>(REPLACEMENT_TO_CHARACTER_MAP);
		
		/*
		 * Handle forward slashes first
		 */
		replacementToCharacterMap.remove(FORWARD_SLASH_REPLACEMENT);
		
		convertedBranchPath = convert (convertedBranchPath, FORWARD_SLASH_REPLACEMENT, FORWARD_SLASH_CHARACTER);
		
		int startIndex = 0;
		
		StringBuilder convertedBranchPathBuilder = new StringBuilder();

		while ((startIndex + 3) <= convertedBranchPath.length()) {
			
			String targetCharacters = convertedBranchPath.substring(startIndex, (startIndex+3));
			
			String character = replacementToCharacterMap.get(targetCharacters);
			
			if (character == null) {
				convertedBranchPathBuilder.append(targetCharacters.substring(0, 1));
				startIndex++;
			}
			else {
				convertedBranchPathBuilder.append(character);
				startIndex += 3;
			}
			
			
		}
		
		if (startIndex < convertedBranchPath.length()) {
			convertedBranchPathBuilder.append(convertedBranchPath.substring(startIndex));
		}
		return convertedBranchPathBuilder.toString();
	}

	private static String convert(String reference, String target,
			String replacement) {
		
		StringBuilder convertedBranchPathBuilder = new StringBuilder();
		
		int startIndex = 0;
		
		while (true) {
			
			int nextIndex = reference.indexOf(target, startIndex);
			
			if (nextIndex == -1) {
				break;
			}
			else {
				if (startIndex < nextIndex) {
					convertedBranchPathBuilder.append(reference.substring(startIndex, nextIndex));
					
					startIndex = nextIndex;
				}
				convertedBranchPathBuilder.append(replacement);
				startIndex += target.length();
				
			}
			
		}
		
		if (startIndex < reference.length())
			convertedBranchPathBuilder.append(reference.substring(startIndex));
		
		return convertedBranchPathBuilder.toString();
		
	}
	
	public static class ExistingPathTree {
		private String path;
		private String branchName;
		private ObjectId treeId;
		/**
		 * @param path
		 * @param branchName
		 * @param treeId
		 */
		public ExistingPathTree(String path, String branchName, ObjectId treeId) {
			super();
			this.path = path;
			this.branchName = branchName;
			this.treeId = treeId;
		}
		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}
		/**
		 * @return the branchName
		 */
		public String getBranchName() {
			return branchName;
		}
		/**
		 * @return the treeId
		 */
		public ObjectId getTreeId() {
			return treeId;
		}
		
		
	}
	
	/**
	 * Figure out any suffix to apply to the path and then the git tree object representing the copyfrom branch directory.
	 * 
	 * @param path
	 * @param copyFromPath
	 * @param copyFromBranch
	 * @return
	 */
	public static String convertToTargetPath (String path, String copyFromPath, BranchData copyFromBranch) {
		
		String copyfromBranchPath = copyFromBranch.getBranchPath();
		
		
		if (copyFromPath.length() < copyfromBranchPath.length()) {
			
			String suffixPath = copyfromBranchPath.substring(copyFromPath.length());
			
			if (suffixPath.charAt(0) != '/')
				return path + "/" + suffixPath;
			else
				return path + suffixPath;
			
		}
		
		return path;
		
	}

	/**
	 * Convert the copyfrom blob path into a path rooted on the target branch (branch in path).
	 * 
	 * Handle removal of base copyfrom file path directories but still allow sub directories to be realized in the adjusted blob path.
	 * 
	 * @param path the target path, could be a branch or a sub tree within a branch.
	 * @param copyFromRevision
	 * @param copyFromPath the whole copy from path (copy from branch path + copy from file path)
	 * @param blobPath path of the blob in the copyfrom branch (will overlap somewhat with the copy from file path)
	 * @param copyFromBranch holds the split between the copy from branch path and copy from dile path)
	 * @return the adjusted blob path to be rooted in the target branch with the correct subtree (if applicable).
	 */
	public static String convertToTargetPath(String path, long copyFromRevision, String copyFromPath, String blobPath, BranchData copyFromBranch) {
		
		// remove any base structure that is not part of the copy
		String branchRemovedCopyFromPath = blobPath.substring(copyFromBranch.getPath().length());
		
		// place the altered file path into the target path
		// this may be at the branch root or into some subtree structure.
		StringBuilder alteredBlobPath = new StringBuilder(path);
		
		if (alteredBlobPath.charAt(alteredBlobPath.length()-1) != '/') {
			
			if (branchRemovedCopyFromPath.length() > 0) {
				
				if (branchRemovedCopyFromPath.charAt(0) != '/')
					alteredBlobPath.append("/");
				// else fall through and make no change.
			}
			else
				alteredBlobPath.append("/");
		}
		
		alteredBlobPath.append(branchRemovedCopyFromPath);
		
		return alteredBlobPath.toString();
		
	}

	/**
	 * In some cases a path could refer to a branch that is not detectable using our heuristics.
	 * 
	 * In certain copy from cases we want to save the files even if the branch name is non sensical.
	 * 
	 * This provides a mechanism to find those branches later
	 * 
	 * @return the branch path part from the path.
	 * 
	 */
	public static String extractBranchPath(Repository repo, String path) throws IOException {
		
		String pathPaths[] = path.split("/");
		
		for (int i = pathPaths.length-1; i > 0; i--) {
			
			String candidatePath = StringUtils.join(pathPaths, "/", 0, i);
			
			Ref branchRef = repo.getRef(Constants.R_HEADS + candidatePath);
			
			if (branchRef != null) {
				// found a match
				return candidatePath;
			}
			
		}
		
		return null;
		
	}

}
