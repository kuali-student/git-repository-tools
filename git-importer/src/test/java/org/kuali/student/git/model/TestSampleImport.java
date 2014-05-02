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

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.util.GitBranchDataUtils;

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
		
		assertRefNotNull(repository, "trunk", "expected trunk to exist");
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"pom.xml", "module/pom.xml", "module/src/main/resources/test.txt"}));
		
		assertFileContentEquals (repository, "trunk", "pom.xml", "test file\n");
		
		assertFileContentEquals (repository, "trunk", "module/src/main/resources/test.txt", " test resource file\n");
		
		runImporter(repository, 2);
		
		assertRefNotNull(repository, "branches_branch1", "expected branch1 to exist");
		
		assertPathsExist(repository, "branches_branch1", Arrays.asList(new String [] {"pom.xml", "module/pom.xml", "module/src/main/resources/test.txt"}));
		
		runImporter(repository, 3);
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"pom.xml", "module1/pom.xml", "module1/src/main/resources/test.txt", "module2/pom.xml", "module2/src/main/resources/test.txt"}));
		
		runImporter(repository, 4);
		
		assertRefNotNull(repository, "branches_branch2", "expected branch2 to exist");
		
		runImporter(repository, 5);
		
		assertRefNotNull(repository, "branches_inactive", "expected branches_inactive to exist");
		
		assertPathsExist(repository, "branches_inactive", Arrays.asList(new String [] {"branch1/pom.xml", "branch1/module/pom.xml", "branch2/module1/src/main/resources/test.txt", "branch2/module2/pom.xml", "branch2/module2/src/main/resources/test.txt"}));
		
		runImporter(repository, 6);
		
		assertRefNotNull(repository, "branches_branch1", "expected branch1 to exist");
		assertRefNotNull(repository, "branches_branch2", "expected branch2 to exist");
		
		runImporter(repository, 7);
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module3/pom.xml", "module3/src/main/resources/test.txt"}));
		
		runImporter(repository, 8);
		
		assertFileContentEquals (repository, "trunk", "pom.xml", "another pom file only change\n");
		
		runImporter(repository, 9);
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module3/src/main/resources/test.txt", "module3/src/main/resources/A.txt", "module3/src/main/resources/B.txt", "module3/src/main/resources/C.txt"}));
		
		runImporter(repository, 10);
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module4/pom.xml", "module4/src/main/resources/test.txt"}));
		
		runImporter(repository, 11);
		
		assertPathsDontExist(repository, "trunk", Arrays.asList(new String [] {"module3/src/main/resources/A.txt", "module3/src/main/resources/B.txt",  "module3/src/main/resources/test.txt"}));
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"module3/src/main/resources/C.txt"}));
		
		/*
		 * test deleting a path ending in tags from trunk.
		 */
		runImporter(repository, 12);
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"tags/test-tag/test.txt"}));
		
		runImporter(repository, 13);
		
		assertPathsDontExist(repository, "trunk", Arrays.asList(new String [] {"tags/test-tag/test.txt"}));
		
		
		/*
		 * Test copyfrom nested trunk to trunk scenario
		 */
		runImporter(repository, 14);
		
		assertPathsExist(repository, "trunk_maven_trunk", Arrays.asList(new String [] {"kuali/test.txt"}));
		
		runImporter(repository, 15);
		
		assertPathsExist(repository, "trunk", Arrays.asList(new String [] {"maven/kuali/test.txt"}));
		
	}

	

	private void runImporter(Repository repository, long importRevision) throws IOException {
		
		runImporter(repository, importRevision, "src/test/resources/sample/sample-r"+importRevision+".dump.bz2", "https://svn.sample.org", "fake-uuid");
		
	}
	
}
