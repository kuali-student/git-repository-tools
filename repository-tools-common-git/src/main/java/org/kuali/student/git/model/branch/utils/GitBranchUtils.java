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
package org.kuali.student.git.model.branch.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.branch.model.BranchData;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;

/**
 * @author Kuali Student Team
 * 
 */
public class GitBranchUtils {

	
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
		
		/*
		 * Within the branch the blob path may have directory nesting.
		 * 
		 * This needs to be stripped off so that the altered blob is rooted in the target path.
		 */
		
		StringBuilder alteredBlobSuffixPath = new StringBuilder(blobPath.substring(copyFromBranch.getPath().length()));
		
		if (alteredBlobSuffixPath.charAt(0) == '/')
			alteredBlobSuffixPath.delete(0, 1);
		
		String alteredBlobPath = alteredBlobPrefixPath.append(alteredBlobSuffixPath.toString()).toString();
		
		return alteredBlobPath;
	}

}
