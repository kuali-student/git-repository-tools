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

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.SvnMergeInfoUtils;
import org.kuali.student.git.model.SvnMergeInfoUtils.BranchRangeDataProvider;
import org.kuali.student.git.model.branch.AbstractBranchDetectorTest;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.kuali.student.git.model.branch.large.LargeBranchNameProviderMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    public void testMergeInfo() {

        String r21322MergeInfo = "/sis/branches/r19928_Confirm_Courses_Report:19958-19974\n" +
                "/sis/branches/r19928_Scheduled_Courses_Report:19975-20135\n" +
                "/sis/trunk:19778,20057,20077,20099,20114,20118,20171,20175-20176,20178-20181,20183,20186-20203,20208-20210,20212-20215,20217-20218,20221-20238,20242-20281,20283-20288,20290-20295," +
                "20299-20300,20304-20305,20307,20327,20353,20357,20360,20363-20365,20368-20369,20472,20476,20478,20481,20484-20485,20487,20489,20491,20494,20500-20501,20508-20509,20511,20514,20519," +
                "20521,20523-20525,20528-20529,20552,20556-20557,20566,20568,20570,20597-20598,20727,20781,20794,20834,20838,20843,20861,20881-20882,20910,20915-20916,20920,20977,20979,21090,21155," +
                "21224,21293,21316\n";

        String r21359MergeInfo = "/sis/branches/r19928_Confirm_Courses_Report:19958-19974\n" +
                "/sis/branches/r19928_Scheduled_Courses_Report:19975-20135\n" +
                "/sis/trunk:19778,20057,20077,20099,20114,20118,20171,20175-20176,20178-20181,20183,20186-20203,20208-20210,20212-20215,20217-20218,20221-20238,20242-20281,20283-20288,20290-20295," +
                "20299-20300,20304-20305,20307,20327,20353,20357,20360,20363-20365,20368-20369,20472,20476,20478,20481,20484-20485,20487,20489,20491,20494,20500-20501,20508-20509,20511,20514," +
                "20519,20521,20523-20525,20528-20529,20552,20556-20557,20566,20568,20570,20597-20598,20727,20781,20794,20834,20838,20843,20861,20881-20882,20910,20915-20916,20920,20977,20979,21090," +
                "21155,21224,21293,21316,21358\n";

        List<BranchMergeInfo>r31322BMIList = SvnMergeInfoUtils.extractBranchMergeInfoFromString(branchDetector, r21322MergeInfo);

        BranchMergeInfo r21322Trunk = null,
                        r21359Trunk = null;

        for(BranchMergeInfo bmi : r31322BMIList) {

            if (bmi.getBranchName().equals("sis/trunk")) {
                Assert.assertFalse("Expected source branch to not contain revision 21358", bmi.getMergedRevisions().contains(21358L));
                r21322Trunk = bmi;
            }

        }

        List<BranchMergeInfo>r31359BMIList = SvnMergeInfoUtils.extractBranchMergeInfoFromString(branchDetector, r21359MergeInfo);

        for(BranchMergeInfo bmi : r31359BMIList) {

            if (bmi.getBranchName().equals("sis/trunk")) {
                Assert.assertTrue("Expected target branch to contain revision 21358", bmi.getMergedRevisions().contains(21358L));
                r21359Trunk = bmi;
            }

        }

        List<BranchMergeInfo> difference = SvnMergeInfoUtils.computeDifference(Arrays.asList(r21322Trunk), Arrays.asList(r21359Trunk));

        Assert.assertEquals("Expected there to be one difference (21358)", 1, difference.get(0).getMergedRevisions().size());
    }

	@Test
	public void testParseMergeInfoDataFile () throws IOException, VetoBranchException {
		
		FileInputStream input = new FileInputStream("src/test/resources/ks-enroll-merge-info.txt");

		List<BranchMergeInfo> bmiList = SvnMergeInfoUtils.extractBranchMergeInfoFromInputStream(branchDetector, input);
		
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
		
		SvnMergeInfoUtils.consolidateConsecutiveRanges(new BranchRangeDataProvider() {
			
			@Override
			public boolean areCommitsAdjacent(String branchName, long firstRevision,
					long secondRevision) {
				if ((firstRevision + 1L) == secondRevision)
					return true;
				else
					return false;
			}
			
		}, branchDetector, new LargeBranchNameProviderMapImpl(), deltas);
		
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
