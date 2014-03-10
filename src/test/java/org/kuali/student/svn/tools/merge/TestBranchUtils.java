/*
 * Copyright 2013 The Kuali Foundation
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
package org.kuali.student.svn.tools.merge;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.AbstractKualiStudentBranchDetectorTest;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.kuali.student.svn.tools.merge.tools.BranchUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Kuali Student Team
 *
 */
// TODO: split in two for base branch tests and kuali student base branch tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:base-branch-detector-test-context.xml"})
public class TestBranchUtils extends AbstractKualiStudentBranchDetectorTest {

	/**
	 * 
	 */
	public TestBranchUtils() {
	}

	
	@Test
	public void testBranchUtils() throws VetoBranchException {
		
		BranchData bd = branchDetector.parseBranch(123456L, "poc/personidentity/personidentity-api/branches/personidentity-api-dev/src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeSetDefinition.java");
	
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("poc/personidentity/personidentity-api/branches/personidentity-api-dev", bd.getBranchPath());
		Assert.assertEquals("src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeSetDefinition.java", bd.getPath());
		Assert.assertEquals(Long.valueOf(123456L), bd.getRevision());
		
		
		
		bd = branchDetector.parseBranch(123456L, "deploymentlab/trunk/1.0.x/ks-cfg-dbs/ks-embedded-db/src/main/impex/KREN_CHNL_PRODCR_T.xml");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("deploymentlab/trunk", bd.getBranchPath());
		Assert.assertEquals("1.0.x/ks-cfg-dbs/ks-embedded-db/src/main/impex/KREN_CHNL_PRODCR_T.xml", bd.getPath());
		Assert.assertEquals(Long.valueOf(123456L), bd.getRevision());
		

	
	bd = branchDetector.parseBranch(2237L, "enumeration/enumeration-impl/src/main/java/org/kuali/student/enumeration/entity/ContextDAO.java");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("enumeration", bd.getBranchPath());
		Assert.assertEquals("enumeration-impl/src/main/java/org/kuali/student/enumeration/entity/ContextDAO.java", bd.getPath());
		Assert.assertEquals(Long.valueOf(2237L), bd.getRevision());
	}
	
	@Test 
	public void testMissingDataCase() throws VetoBranchException {
		// 2249:2250::sandbox/team2/branches:branches:lum

		BranchData bd = branchDetector.parseBranch(2249L, "branches");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("branches", bd.getBranchPath());
		Assert.assertEquals ("", bd.getPath());
		
		bd = branchDetector.parseBranch(2250L, "sandbox/team2/lum/branches");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("sandbox/team2/lum/branches", bd.getBranchPath());
		Assert.assertEquals ("", bd.getPath());
		
		bd = branchDetector.parseBranch(2250L, "enumeration/.classpath");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("enumeration", bd.getBranchPath());
		Assert.assertEquals (".classpath", bd.getPath());
		
		bd = branchDetector.parseBranch(2250L, "sandbox/searchwidgets/.classpath");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("sandbox/searchwidgets", bd.getBranchPath());
		Assert.assertEquals (".classpath", bd.getPath());
		
		bd = branchDetector.parseBranch(2250L, "sandbox/team2/tools/DataDictionaryExtractor/trunk/.classpath");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("sandbox/team2/tools/DataDictionaryExtractor/trunk", bd.getBranchPath());
		Assert.assertEquals (".classpath", bd.getPath());
		
		bd = branchDetector.parseBranch(2007L, "dictionary/.classpath");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("dictionary", bd.getBranchPath());
		Assert.assertEquals (".classpath", bd.getPath());
		
		bd = branchDetector.parseBranch(2007L, "dictionary/dict-services/.classpath");
		
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("dictionary", bd.getBranchPath());
		Assert.assertEquals ("dict-services/.classpath", bd.getPath());
		
	}
}
