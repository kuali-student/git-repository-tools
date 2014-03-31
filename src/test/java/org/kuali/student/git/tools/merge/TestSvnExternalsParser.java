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
import org.kuali.student.git.model.AbstractBranchDetectorTest;
import org.kuali.student.git.model.ExternalModuleInfo;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.tools.SvnExternalsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class TestSvnExternalsParser extends AbstractBranchDetectorTest {

	private static final Logger log = LoggerFactory.getLogger(TestSvnExternalsParser.class);
	
	/**
	 * 
	 */
	public TestSvnExternalsParser() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testParseExternalsInfoDataFile () throws IOException, VetoBranchException {
		
		FileInputStream input = new FileInputStream("src/test/resources/ks-externals-for-aggregate-trunk-at-r43000.txt");

		List<ExternalModuleInfo> externalsList = SvnExternalsUtils.extractExternalModuleInfoFromInputStream(43000, "https://svn.kuali.org/repos/student", input);
		
		Assert.assertNotNull(externalsList);
		Assert.assertEquals(5, externalsList.size());
		
		ExternalModuleInfo external = externalsList.get(3);
		
		
		// ks-enroll enrollment/ks-enroll/trunk
		
		Assert.assertEquals("ks-enroll", external.getModuleName());
		
		Assert.assertEquals("enrollment/ks-enroll/trunk", external.getBranchPath());
		
		Assert.assertEquals(43000, external.getRevision());
		
		log.debug("");
		
		
	}
	

}
