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
package org.kuali.student.git.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.kuali.student.branch.model.BranchData;
import org.kuali.student.common.io.IOUtils;
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.kuali.student.git.model.branch.utils.GitBranchUtils;
import org.kuali.student.git.model.branch.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class SvnMergeInfoUtils {
	
	private static final Logger log = LoggerFactory.getLogger(SvnMergeInfoUtils.class);
	
	
	/**
	 * Consume the full content of the input stream and parse out the svn:mergeinfo property content found.
	 * 
	 * You should use a bounded input stream sized for only the length of the svn:mergeinfo data to be consumed.
	 * 
	 * @param inputStream
	 * @return the list of brance merge info
	 * @throws IOException
	 */
	public static List<BranchMergeInfo>extractBranchMergeInfoFromInputStream (BranchDetector branchDetector, InputStream inputStream) throws IOException {
		
		StringBuilder builder = new StringBuilder();
		
		while (true) {
			String line = IOUtils.readLine(inputStream, "UTF-8");
			
			if (line == null)
				break;
			
			if (line.isEmpty())
				continue;
			
			
			builder.append(line).append("\n");
		}
		
		return extractBranchMergeInfoFromString(branchDetector, builder.toString());
	}
	
	public static List<BranchMergeInfo>extractBranchMergeInfoFromString (BranchDetector branchDetector, String inputString) {
	
		List<BranchMergeInfo>bmiList = new LinkedList<>();
		
		String lines[] = inputString.split("\\n");
		
		for (String line : lines) {
		
			if (line.isEmpty())
				continue; // skip to the next line
			
			String [] parts = line.split(":");
			
			if (parts.length != 2)
				continue; // skip to the next line
			
			String branchPath = parts[0].trim();
			
			if (branchPath.startsWith("/")) {
				// trim the leading slash if it exists.
				branchPath = branchPath.substring(1);
			}

			BranchData bd = null;
			
			try {
				bd = branchDetector.parseBranch(0L, branchPath);
				
			} catch (VetoBranchException e) {
				log.warn("failed to detect a branch on " + branchPath);
				continue; // skip to the next line
			}
			
			
			BranchMergeInfo bmi = new BranchMergeInfo(bd.getBranchPath());
			
			String revisions = parts[1].trim();
			
			String revParts[] = revisions.split(",");
			
			/*
			 * A star (*) suffix on a number is used to indicate that the revision was record only merged.
			 * 
			 * We strip the value and treat all merged revisions the same.
			 * 
			 */
			for (String revString : revParts) {
				
				if (revString.contains("-")) {
					// a range
					String rangeParts[] = revString.split("-");
					
					long rangeStart = Long.parseLong(rangeParts[0].trim().replaceAll("\\*", ""));
					long rangeEndInclusive = Long.parseLong(rangeParts[1].trim().replaceAll("\\*", ""));
					
					long low = rangeStart;
					long highInclusive = rangeEndInclusive;
					
					if (rangeEndInclusive < rangeStart) {
						// reverse the high and low
						low = rangeEndInclusive;
						highInclusive = rangeStart;
					}
					
					for(long i = low; i <= highInclusive; i++) {
						bmi.addMergeRevision(i);
					}
				}
				else {
					// a single value
				
					bmi.addMergeRevision(Long.valueOf(revString.replaceAll("\\*", "")));
				}
			}
			
			bmiList.add(bmi);
	}
		
		return bmiList;
	}

	public static interface BranchRangeDataProvider {
		
		public boolean areCommitsAdjacent(String branchName, long firstRevision, long secondRevision);
		
	}
	public static void consolidateConsecutiveRanges(BranchRangeDataProvider rangeDataProvider, BranchDetector branchDetector,
			ILargeBranchNameProvider provider, List<BranchMergeInfo> deltas) {
		
		for (BranchMergeInfo delta : deltas) {
			
			List<Long>orderedRevisions = new ArrayList<>(delta.getMergedRevisions());

			if (orderedRevisions.size() < 2)
				continue;
			
			delta.clearMergedRevisions();
			
			Collections.sort(orderedRevisions);
			
			
			long previousRevision = orderedRevisions.get(0);
			
			for (int i = 1; i < orderedRevisions.size(); i++) {
				
				Long rev = orderedRevisions.get(i);
				
				String branchName = GitBranchUtils.getCanonicalBranchName(delta.getBranchName(), 1L, provider);
				
				if (!rangeDataProvider.areCommitsAdjacent(branchName, previousRevision, rev)) {
					// range ends
					
					delta.addMergeRevision(previousRevision);
					
					
				}
				else {
					// range continues
				}
				
				previousRevision = rev;
			}
			
			// always store the last revision
			delta.addMergeRevision(previousRevision); 
			
		}
		
	}	
	/**
	 * Computes the difference, what has been added to the target that is not in the source,  between the two sets of merge info and returns the difference out.
	 * @param sourceData
	 * @param targetData
	 * @return the delta between the target and the source data.
	 */
	public static List<BranchMergeInfo>computeDifference (List<BranchMergeInfo>sourceData, List<BranchMergeInfo>targetData) {
		
		List<BranchMergeInfo>difference = new ArrayList<>();
		
		// branch name to bmi
		HashMap<String, BranchMergeInfo> sourceBmiMap = new HashMap<String, BranchMergeInfo>();
		
		for (BranchMergeInfo sourceBmi : sourceData) {
			sourceBmiMap.put(sourceBmi.getBranchName(), sourceBmi);
		}
		
		for (BranchMergeInfo targetBmi : targetData) {
			
			BranchMergeInfo sourceBmi = sourceBmiMap.get(targetBmi.getBranchName());
			
			if (sourceBmi == null) {
				// add the entire target bmi to the result
				difference.add(targetBmi);
			}
			else {
			}
		}
		
		return difference;
	}
}
