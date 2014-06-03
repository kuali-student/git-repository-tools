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


import java.util.Map;

import org.eclipse.jgit.api.GitCommand;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author ocleirig
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestGitBranchUtils {

	/**
	 * 
	 */
	public TestGitBranchUtils() {
	}

	@Test
	public void testLayeredPathConversions() {
	
//		private static final String SPACE_CHARACTER = " ";
//		private static final String TILDE_CHARACTER = "~";
//		private static final String CARET_CHARACTER = "^";
//		private static final String COLON_CHARACTER = ":";
//		private static final String QUESTION_MARK_CHARACTER = "?";
//		private static final String OPEN_SQUARE_BRACE_CHARACTER = "[";
//		private static final String STAR_CHARACTER = "*";
//		private static final String BACKSLASH_CHARACTER = "\\";
//		
//		private static final String FORWARD_SLASH_CHARACTER="/";
//		private static final String UNDERSCORE_CHARACTER="_";

		
//		private static final String OPEN_SQUARE_BRACE_REPLACEMENT = "-----";
//		private static final String TILDE_REPLACEMENT = "----";		
//		private static final String SPACE_REPLACEMENT = "---";
		
		String branchPath = "[~ ";
		
		String convertedBranchName = GitBranchUtils.convertPathToBranchName(branchPath);
		
		Assert.assertEquals((GitBranchUtils.OPEN_SQUARE_BRACE_REPLACEMENT.length() + GitBranchUtils.TILDE_REPLACEMENT.length() + GitBranchUtils.SPACE_REPLACEMENT.length()), convertedBranchName.length());
		
		Assert.assertEquals(GitBranchUtils.OPEN_SQUARE_BRACE_REPLACEMENT + GitBranchUtils.TILDE_REPLACEMENT + GitBranchUtils.SPACE_REPLACEMENT, convertedBranchName);
		
		String convertedBranchPath = GitBranchUtils.convertBranchNameToPath(convertedBranchName);
		
		Assert.assertEquals(branchPath, convertedBranchPath);

//		private static final String STAR_REPLACEMENT = "=====";
//		private static final String CARET_REPLACEMENT = "====";
//		private static final String UNDERSCORE_REPLACEMENT = "===";
		
//

//		private static final String BACKSLASH_REPLACEMENT = "+++++";
//		private static final String QUESTION_MARK_REPLACEMENT = "++++";
//		private static final String COLON_REPLACEMENT = "+++";
//		

		
	}
	
	
	@Test
	public void testPathConversions() {

		String branchPath = "branch path with spaces";
		
		String converted = GitBranchUtils.convertPathToBranchName(branchPath);

		Assert.assertEquals("branch---path---with---spaces", converted);
		
		String convertedBranchPath = GitBranchUtils.convertBranchNameToPath(converted);
		
		Assert.assertEquals(branchPath, convertedBranchPath);
		
		branchPath = "branch_path_with_underscores";
		
		converted = GitBranchUtils.convertPathToBranchName(branchPath);
		
		Assert.assertEquals("branch===path===with===underscores", converted);

		convertedBranchPath = GitBranchUtils.convertBranchNameToPath(converted);
		
		Assert.assertEquals(branchPath, convertedBranchPath);
		
		branchPath = "branch:path:with:colons";
		
		converted = GitBranchUtils.convertPathToBranchName(branchPath);
		
		Assert.assertEquals("branchXpathXwithXcolons".replaceAll("X", GitBranchUtils.COLON_REPLACEMENT), converted);
		
		convertedBranchPath = GitBranchUtils.convertBranchNameToPath(converted);
		
		Assert.assertEquals(branchPath, convertedBranchPath);
		
		branchPath = "branch:path:with:colons";
		
		converted = GitBranchUtils.convertPathToBranchName(branchPath);
		
		Assert.assertEquals("branchXpathXwithXcolons".replaceAll("X", GitBranchUtils.COLON_REPLACEMENT), converted);
		
		convertedBranchPath = GitBranchUtils.convertBranchNameToPath(converted);
		
		Assert.assertEquals(branchPath, convertedBranchPath);
		
		
		StringBuilder testPath = new StringBuilder();
		StringBuilder expectedConvertedBranchName = new StringBuilder();
		
		for (Map.Entry<String, String> entry : GitBranchUtils.CHARACTER_TO_REPLACEMENT_MAP.entrySet()) {

			/*
			 * Test with pattern X patten
			 */
			testPath.append("X");
			testPath.append(entry.getKey());
			
			expectedConvertedBranchName.append("X");
			expectedConvertedBranchName.append(entry.getValue());
			
		}
		
		String testBranchPath = testPath.toString();
		
		String convertedBranchName = GitBranchUtils.convertPathToBranchName(testBranchPath);
		
		Assert.assertEquals(expectedConvertedBranchName.toString(), convertedBranchName);
		
		convertedBranchPath = GitBranchUtils.convertBranchNameToPath(convertedBranchName);
		
		Assert.assertEquals(testBranchPath, convertedBranchPath);
		
		for (Map.Entry<String, String> entry : GitBranchUtils.CHARACTER_TO_REPLACEMENT_MAP.entrySet()) {

			/*
			 * Test with the patterns directly adjacent to each other.
			 */
			testPath.append(entry.getKey());
			
			expectedConvertedBranchName.append(entry.getValue());
			
		}
		
		testBranchPath = testPath.toString();
		
		convertedBranchName = GitBranchUtils.convertPathToBranchName(testBranchPath);
		
		Assert.assertEquals(expectedConvertedBranchName.toString(), convertedBranchName);
		
		convertedBranchPath = GitBranchUtils.convertBranchNameToPath(convertedBranchName);
		
		Assert.assertEquals(testBranchPath, convertedBranchPath);
		
		
		
	}

}
