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
package org.kuali.student.svn.tools.merge.tools;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.svn.tools.merge.tools.BranchUtils.BranchData;

/**
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestBranchUtils {

	/**
	 * 
	 */
	public TestBranchUtils() {
	}

	
	@Test
	public void testBranchUtils() {
		
		BranchData bd = BranchUtils.parse("poc/personidentity/personidentity-api/branches/personidentity-api-dev/src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeSetDefinition.java");
	
		Assert.assertNotNull(bd);
		
		Assert.assertEquals("poc/personidentity/personidentity-api/branches/personidentity-api-dev", bd.getBranchPath());
		Assert.assertEquals("src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeSetDefinition.java", bd.getPath());
		
	}
}
