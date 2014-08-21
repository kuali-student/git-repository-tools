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
package org.kuali.student.git.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.model.utils.GitTestUtils;
import org.kuali.student.svn.model.ExternalModuleInfo;

/**
 * 
 * Test the importer on a new suite of manually created svn dump files designed to test out various properties of the importer.
 * 
 * Primarily related to the diffent variations of copyfrom for the new lazy initializing GitTreeData's.
 * 
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestSampleImport extends AbstractGitImporterMainTestCase {

	/**
	 * @param name
	 */
	public TestSampleImport() {
		super ("sample", false);
	}

	@Test
	public void testSampleImport () throws IOException {
		
		runImporter(repository, 0);
		
		runImporter(repository, 1);
		
		GitTestUtils.assertRefNotNull(repository, "trunk", "expected trunk to exist");
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"pom.xml", "module/pom.xml", "module/src/main/resources/test.txt"}));
		
		GitTestUtils.assertFileContentEquals (repository, "trunk", "pom.xml", "test file\n");
		
		GitTestUtils.assertFileContentEquals (repository, "trunk", "module/src/main/resources/test.txt", " test resource file\n");
		
		runImporter(repository, 2);
		
		GitTestUtils.assertRefNotNull(repository, "branches_branch1", "expected branch1 to exist");
		
		GitTestUtils.assertPathsExist(repository, "branches_branch1", Arrays.asList(new String [] {"pom.xml", "module/pom.xml", "module/src/main/resources/test.txt"}));
		
		runImporter(repository, 3);
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"pom.xml", "module1/pom.xml", "module1/src/main/resources/test.txt", "module2/pom.xml", "module2/src/main/resources/test.txt"}));
		
		runImporter(repository, 4);
		
		GitTestUtils.assertRefNotNull(repository, "branches_branch2", "expected branch2 to exist");
		
		runImporter(repository, 5);
		
		GitTestUtils.assertRefNotNull(repository, "branches_inactive", "expected branches_inactive to exist");
		
		GitTestUtils.assertPathsExist(repository, "branches_inactive", Arrays.asList(new String [] {"branch1/pom.xml", "branch1/module/pom.xml", "branch2/module1/src/main/resources/test.txt", "branch2/module2/pom.xml", "branch2/module2/src/main/resources/test.txt"}));
		
		runImporter(repository, 6);
		
		GitTestUtils.assertRefNotNull(repository, "branches_branch1", "expected branch1 to exist");
		GitTestUtils.assertRefNotNull(repository, "branches_branch2", "expected branch2 to exist");
		
		runImporter(repository, 7);
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module3/pom.xml", "module3/src/main/resources/test.txt"}));
		
		runImporter(repository, 8);
		
		GitTestUtils.assertFileContentEquals (repository, "trunk", "pom.xml", "another pom file only change\n");
		
		runImporter(repository, 9);
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module3/src/main/resources/test.txt", "module3/src/main/resources/A.txt", "module3/src/main/resources/B.txt", "module3/src/main/resources/C.txt"}));
		
		runImporter(repository, 10);
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module4/pom.xml", "module4/src/main/resources/test.txt"}));
		
		runImporter(repository, 11);
		
		GitTestUtils.assertPathsDontExist(repository, "trunk", Arrays.asList(new String [] {"module3/src/main/resources/A.txt", "module3/src/main/resources/B.txt",  "module3/src/main/resources/test.txt"}));
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module3/src/main/resources/C.txt"}));
		
		/*
		 * test deleting a path ending in tags from trunk.
		 */
		runImporter(repository, 12);
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"tags/test-tag/test.txt"}));
		
		runImporter(repository, 13);
		
		GitTestUtils.assertPathsDontExist(repository, "trunk", Arrays.asList(new String [] {"tags/test-tag/test.txt"}));
		
		
		/*
		 * Test copyfrom nested trunk to trunk scenario
		 */
		runImporter(repository, 14);
		
		GitTestUtils.assertPathsExist(repository, "trunk_maven_trunk", Arrays.asList(new String [] {"kuali/test.txt"}));
		
		runImporter(repository, 15);
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"maven/kuali/test.txt"}));
		
		/*
		 * Test that a copyfrom an invalid branch works.
		 */
		runImporter(repository, 16);
		
		GitTestUtils.assertRefNotNull(repository, "invalid-branch-name", "expected invalid-branch-name to exist");
		
		runImporter(repository, 17);
		
		GitTestUtils.assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"invalid-branch-name-resource.txt"}));
		
		runImporter(repository, 18);
		
		GitTestUtils.assertFileContentEquals (repository, "trunk", "module5/pom.xml", " module pom file\n");
		
		runImporter(repository, 19);
		
		GitTestUtils.assertPathsExist(repository, "aggregate_trunk", Arrays.asList(new String[] {"fusion-maven-plugin.dat"}));
		
		ObjectLoader fusionDataFileLoader = GitTestUtils.loadFileContents(repository, "aggregate_trunk", "fusion-maven-plugin.dat");
		
		List<ExternalModuleInfo> externals = ExternalModuleUtils.extractFusionMavenPluginData(fusionDataFileLoader.openStream());
		
		for (ExternalModuleInfo externalModuleInfo : externals) {
			
			if (externalModuleInfo.getModuleName().equals("branch1")) {
				
				ObjectId branch1ExpectedHeadId = externalModuleInfo.getBranchHeadId();
				
				String branchPath = externalModuleInfo.getBranchPath();
				
				Assert.assertEquals("branches/branch1", branchPath);
				
				String branchName = externalModuleInfo.getBranchName();
				
				Assert.assertEquals("branches_branch1", branchName);
				
				Ref actualBranchRef = repository.getRef("branches_branch1");
				
				Assert.assertEquals("expected branch 1 external head id to match repo head id", branch1ExpectedHeadId, actualBranchRef.getObjectId());
				
				
			} else if (externalModuleInfo.getModuleName().equals("branch2")) {	
				
				ObjectId branch2ExpectedHeadId = externalModuleInfo.getBranchHeadId();
				
				String branchPath = externalModuleInfo.getBranchPath();
				
				Assert.assertEquals("branches/branch2", branchPath);
				
				String branchName = externalModuleInfo.getBranchName();
				
				Assert.assertEquals("branches_branch2", branchName);
				
				Ref actualBranchRef = repository.getRef("branches_branch2");
				
				Assert.assertEquals("expected branch 2 external head id to match repo head id", branch2ExpectedHeadId, actualBranchRef.getObjectId());
			}
			else {
				Assert.fail("expected branch1 or branch 2");
			}
		}
		
		GitTreeProcessor processor = new GitTreeProcessor(repository);
		
		Ref aggregateTrunkRef = repository.getRef("aggregate_trunk");
		
		GitTreeData treeAtR19 = processor.extractExistingTreeDataFromCommit(aggregateTrunkRef.getObjectId());
		
		ObjectId fusionMavenPluginDatAtR19 = treeAtR19.find(repository, "fusion-maven-plugin.dat");
		
		Assert.assertNotNull("Expected the fusion-maven-plugin.dat file to exist in the base of the aggregate_trunk branch at r19", fusionMavenPluginDatAtR19);
		
		runImporter(repository, 20);
		
		aggregateTrunkRef = repository.getRef("aggregate_trunk");
		
		GitTreeData treeAtR20 = processor.extractExistingTreeDataFromCommit(aggregateTrunkRef.getObjectId());
		
		ObjectId fusionMavenPluginDatAtR20 = treeAtR20.find(repository, "fusion-maven-plugin.dat");
		
		Assert.assertEquals("Expected the fusion-maven-plugin.dat file to stay the same between rev 19 and rev 20", fusionMavenPluginDatAtR19, fusionMavenPluginDatAtR20);
		
	}

	

	private void runImporter(Repository repository, long importRevision) throws IOException {
		
		runImporter(repository, importRevision, "src/test/resources/sample/sample-r"+importRevision+".dump.bz2", "https://svn.sample.org", "fake-uuid");
		
	}
	
}
