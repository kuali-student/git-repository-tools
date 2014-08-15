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
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.student.git.model.ExternalModuleUtils;
import org.kuali.student.git.model.ExternalModuleUtils.IBranchHeadProvider;
import org.kuali.student.git.model.branch.AbstractBranchDetectorTest;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.kuali.student.git.model.branch.large.LargeBranchNameProviderMapImpl;
import org.kuali.student.svn.model.ExternalModuleInfo;
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

	private void testExternalsDataFile (String name) throws IOException {
		
		FileInputStream input = new FileInputStream(name);

		List<ExternalModuleInfo> externalsList = ExternalModuleUtils.extractExternalModuleInfoFromSvnExternalsInputStream(43000, "https://svn.kuali.org/repos/student", input);
		
		Assert.assertNotNull(externalsList);
		Assert.assertEquals(5, externalsList.size());
		
		ExternalModuleInfo external = externalsList.get(3);
		
		
		// ks-enroll enrollment/ks-enroll/trunk
		
		Assert.assertEquals("ks-enroll", external.getModuleName());
		
		Assert.assertEquals("enrollment/ks-enroll/trunk", external.getBranchPath());
		
		Assert.assertEquals(43000, external.getRevision());
		
		String reversedFusionDataFile = ExternalModuleUtils.createFusionMavenPluginDataFileString(43000L, new IBranchHeadProvider() {
			
			@Override
			public ObjectId getBranchHeadObjectId(String branchName) {
				return null;
			}
		}, externalsList, new LargeBranchNameProviderMapImpl());

		List<ExternalModuleInfo> reversedExternals = ExternalModuleUtils.extractFusionMavenPluginData(Arrays.asList(reversedFusionDataFile.split("\\n")));
		
		Assert.assertEquals(externalsList.size(), reversedExternals.size());
		
		for (int i = 0; i < externalsList.size(); i++) {
			
			ExternalModuleInfo expectedExternal = externalsList.get(i);
			
			ExternalModuleInfo actualExternal = reversedExternals.get(i);
			
			Assert.assertEquals(expectedExternal.getBranchPath(), actualExternal.getBranchPath());
			Assert.assertEquals(expectedExternal.getModuleName(), actualExternal.getModuleName());
			Assert.assertEquals(expectedExternal.getRevision(), actualExternal.getRevision());
		}
	}
	
	@Test
	public void testRelativeExternalsDataFile() throws IOException {
		testExternalsDataFile("src/test/resources/ks-relative-externals-for-aggregate-trunk.txt");
		
	}
	
	@Test
	public void testParseExternalsInfoDataFile () throws IOException, VetoBranchException {
		
		testExternalsDataFile("src/test/resources/ks-externals-for-aggregate-trunk-at-r43000.txt");
	}
	
	@Test
	public void testParseExternalsReversedDataFile () throws IOException, VetoBranchException {
		
		testExternalsDataFile("src/test/resources/ks-externals-for-aggregate-trunk-at-r43000-reversed.txt");
	}
	
	
	
	@Test
	public void testParseFusionMavenPluginsDat () throws IOException {
		
		FileInputStream input = new FileInputStream("src/test/resources/fusion-maven-plugin.dat");

		List<ExternalModuleInfo> externalsList = ExternalModuleUtils.extractFusionMavenPluginData(input);
		
		Assert.assertNotNull("externals are null", externalsList);
		
		Assert.assertEquals(5, externalsList.size());
		
		
	}

}
