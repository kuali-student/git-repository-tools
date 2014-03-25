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
package org.kuali.student.git.tools.merge;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.plaf.ListUI;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.AbstractBranchDetectorTest;
import org.kuali.student.git.model.AbstractKualiStudentBranchDetectorTest;
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.branch.BranchDetectorImpl;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.tools.SvnMergeInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class TestSvnMergeInfoParser extends AbstractBranchDetectorTest {

	private static final Logger log = LoggerFactory.getLogger(TestSvnMergeInfoParser.class);
	
	/**
	 * 
	 */
	public TestSvnMergeInfoParser() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testParseMergeInfoDataFile () throws IOException, VetoBranchException {
		
		FileInputStream input = new FileInputStream("src/test/resources/ks-enroll-merge-info.txt");

		List<BranchMergeInfo> bmiList = SvnMergeInfoUtils.extractBranchMergeInfoFromInputStream(new BranchDetectorImpl(), input);
		
		Assert.assertNotNull(bmiList);
		Assert.assertEquals(18, bmiList.size());
		
		BranchMergeInfo bmi = bmiList.get(4);
		
		// /enrollment/ks-enroll/branches/ENR-FR2:61246-63039
		
		Assert.assertEquals("enrollment/ks-enroll/branches/ENR-FR2", bmi.getBranchName());
		
		for (long i = 61246; i <= 63039; i++) {
			Assert.assertTrue(bmi.getMergedRevisions().contains(i));
		}
		
		log.debug("");
		
		
		
	}
	
	@Test
	public void testKSMergeAtRevision57864() throws IOException {
		
		FileInputStream source = new FileInputStream("src/test/resources/svn-mergeinfo-ks-at-r57863");

		List<BranchMergeInfo> sourceBmi = SvnMergeInfoUtils.extractBranchMergeInfoFromInputStream(branchDetector, source);
		
		FileInputStream merge = new FileInputStream("src/test/resources/svn-mergeinfo-ks-at-r57864");

		List<BranchMergeInfo> mergeBmi = SvnMergeInfoUtils.extractBranchMergeInfoFromInputStream(branchDetector, merge);
		
		List<BranchMergeInfo> deltas = SvnMergeInfoUtils.computeDifference(sourceBmi, mergeBmi);
		
		SvnMergeInfoUtils.consolidateConsecutiveRanges(deltas);
		
		assertDeltaRanges (deltas, "enrollment/ks-lum/branches/2.0.0-m8-api-upgrade", new Long[] {54996L});
		
		assertDeltaRanges (deltas, "enrollment/ks-lum/branches/CM-2.0", new Long[] {40608L, 40610L, 40624L, 40652L, 40884L, 40939L, 41045L, 49407L, 49416L});
		
	}

	@Test
	public void testSubPathMergeBranchDetection() throws IOException {
		
		FileInputStream source = new FileInputStream("src/test/resources/svn-mergeinfo-subpath.txt");

		List<BranchMergeInfo> sourceBmi = SvnMergeInfoUtils.extractBranchMergeInfoFromInputStream(branchDetector, source);
		
		Assert.assertEquals(1, sourceBmi.size());
		
		BranchMergeInfo bmi = sourceBmi.get(0);
		
		Assert.assertEquals("enrollment/ks-lum/branches/2.0.0-m8-api-upgrade", bmi.getBranchName());
		
		log.debug("");
	}
	
	private void assertDeltaRanges(List<BranchMergeInfo> deltas, String branchName,
			Long[] expectedRevisions) {
		
		for (BranchMergeInfo delta : deltas) {
			
			if (delta.getBranchName().equals(branchName)) {
				
				List<Long>expectedRevisionsList = Arrays.asList(expectedRevisions);
				
				Assert.assertTrue("incorrect revision for branch " + branchName , CollectionUtils.isEqualCollection(expectedRevisionsList, delta.getMergedRevisions()));
				
				
			}
		}
		
	}
}
