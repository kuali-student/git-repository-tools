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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.plaf.ListUI;

import org.kuali.student.common.io.IOUtils;
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.BranchDetectorImpl;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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
			
			for (String revString : revParts) {
				
				if (revString.contains("-")) {
					// a range
					String rangeParts[] = revString.split("-");
					
					long rangeStart = Long.parseLong(rangeParts[0].trim());
					long rangeEndInclusive = Long.parseLong(rangeParts[1].trim());
					
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
				
					bmi.addMergeRevision(Long.valueOf(revString));
				}
			}
			
			bmiList.add(bmi);
	}
		
		return bmiList;
	}

	public static void consolidateConsecutiveRanges(
			List<BranchMergeInfo> deltas) {
		
		for (BranchMergeInfo delta : deltas) {
			
			List<Long>orderedRevisions = new ArrayList<>(delta.getMergedRevisions());
			
			delta.clearMergedRevisions();
			
			Collections.sort(orderedRevisions);
			
			long logicalNextRevision = -1;
			
			for (int i = 0; i < orderedRevisions.size(); i++) {
				
				Long rev = orderedRevisions.get(i);
				
				if (logicalNextRevision == -1 || logicalNextRevision != rev) {
					// range ends
					
					int endOfRange = i-1;
					
					if (endOfRange >= 0) {
						
						delta.addMergeRevision(orderedRevisions.get(endOfRange));
					}
					
					
				}
				else {
					// range continues
				}
				
				logicalNextRevision = rev + 1L;
			}
			
			Long lastRev = orderedRevisions.get(orderedRevisions.size()-1);
			
			delta.addMergeRevision(lastRev);
			
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
