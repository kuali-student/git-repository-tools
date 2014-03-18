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
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.BranchMergeInfo;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.tools.SvnMergeInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestSvnMergeInfoParser {

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

		List<BranchMergeInfo> bmiList = SvnMergeInfoUtils.extractBranchMergeInfoFromInputStream(input);
		
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
}
