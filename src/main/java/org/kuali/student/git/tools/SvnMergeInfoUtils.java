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
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.springframework.util.CollectionUtils;

/**
 * @author Kuali Student Team
 *
 */
public class SvnMergeInfoUtils {
	
	
	
	/**
	 * Consume the full content of the input stream and parse out the svn:mergeinfo property content found.
	 * 
	 * You should use a bounded input stream sized for only the length of the svn:mergeinfo data to be consumed.
	 * 
	 * @param inputStream
	 * @return the list of brance merge info
	 * @throws IOException
	 */
	public static List<BranchMergeInfo>extractBranchMergeInfoFromInputStream (InputStream inputStream) throws IOException {
		
		StringBuilder builder = new StringBuilder();
		
		while (true) {
			String line = IOUtils.readLine(inputStream, "UTF-8");
			
			if (line == null)
				break;
			
			if (line.isEmpty())
				continue;
			
			
			builder.append(line).append("\n");
		}
		
		return extractBranchMergeInfoFromString(builder.toString());
	}
	
	public static List<BranchMergeInfo>extractBranchMergeInfoFromString (String inputString) {
	
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
			
			BranchMergeInfo bmi = new BranchMergeInfo(branchPath);
			
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
		
		for (BranchMergeInfo sourceBmi : targetData) {
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
