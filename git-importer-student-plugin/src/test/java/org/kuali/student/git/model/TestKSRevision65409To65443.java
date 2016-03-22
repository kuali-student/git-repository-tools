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

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.model.ref.exception.BranchRefExistsException;
import org.kuali.student.git.model.utils.GitTestUtils;

/**
 * 
 * Test the importer on KS Revision 65409 and 65443 which lose track of a key branch during renaming processes.
 * 
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestKSRevision65409To65443 extends AbstractGitImporterMainTestCase {

	/**
	 * @param name
	 */
	public TestKSRevision65409To65443() {
		super("ks-r65409-to-r65443", true);
	}

	@Test
	public void testImport () throws IOException, BranchRefExistsException {
		
		runImporter (repository, 65409);
		
		// test that the ks-ap/trunk branch was created
		
		GitTestUtils.assertRefNotNull(repository, "enrollment_ks-ap_trunk", "ks-ap trunk should exist");
		
		GitTestUtils.assertPathsExist (repository, "enrollment_ks-ap_trunk", Arrays.asList(new String [] {"ks-ap-ui/src/main/resources", "ks-ap-web"}));
		
		// inject copyfrom data for enrollment/ks-ap/trunk from 65424
		
		runImporter (repository,  65425);
		
		// ks-ap trunk should have been deleted
		GitTestUtils.assertRefNull(repository, "enrollment_ks-ap_trunk", "ks-ap trunk should be null (deleted)");
		
		GitTestUtils.assertRefNotNull(repository, "enrollment_ks-ap_branches_inactive_KSAP", "enrollment_ks-ap_branches_inactive_KSAP should exist");
		
		GitTestUtils.assertRefNotNull(repository, "enrollment_ks-ap_trunk@65424", "enrollment_ks-ap_trunk@65424 should exist");
		
		GitTestUtils.assertPathsExist (repository, "enrollment_ks-ap_trunk@65424", Arrays.asList(new String [] {"ks-ap-ui/src/main/resources", "ks-ap-web"}));
		
		
		/*
		 * thinking about creating a branch here for ks-ap/branches/inactive/KSAP-M8
		 */
		
		GitTestUtils.createBranch (repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-M8", "pom.xml", "some text");
		
		GitTestUtils.assertRefNotNull(repository, "enrollment_ks-ap_branches_inactive_KSAP-M8", "enrollment_ks-ap_branches_inactive_KSAP-M8 should exist");
		
		GitTestUtils.assertPathsExist (repository, "enrollment_ks-ap_branches_inactive_KSAP-M8", Arrays.asList(new String [] {"pom.xml"}));
		
		// 65430 
		runImporter (repository, 65431); 
		
		GitTestUtils.assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_trunk", "expected ks-ap trunk to exist");
		
		GitTestUtils.assertPathsExist (repository, "enrollment_ks-ap_trunk", Arrays.asList(new String [] {"KSAP", "KSAP-M8", "KSAP/ks-ap-ui/src/main/resources", "KSAP-M8/pom.xml"}));
		
		GitTestUtils.assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP", "expected ks-ap inactive KSAP to not exist");
		
		GitTestUtils.assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-M8", "expected ks-ap inactive KSAP-M8 to not exist");
		
		runImporter (repository, 65434);
		
		// we expect nothing to happen because its just a directory add. (not a copy from)
		
		GitTestUtils.createBranch (repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-M7", "pom.xml", "some text");
		
		runImporter (repository, 65435);
		
		GitTestUtils.assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-M7", "expected ks-ap trunk to exist");
		
		GitTestUtils.assertPathsExist (repository, "enrollment_ks-ap_branches_inactive_KSAP-M7", Arrays.asList(new String [] {"pom.xml"}));
		
		GitTestUtils.assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-M7", "expected ks-ap KSAP-M7 to not exist");

		
		GitTestUtils.createBranch (repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-archived-2013-09-25", "pom.xml", "some text");
		
		runImporter (repository, 65437);
		
		GitTestUtils.assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_inactive_KSAP-archived-2013-09-25", "expected ks-ap braches inactive KSAP-2013-09-25 to exist");
		
		GitTestUtils.assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-archived-2013-09-25", "expected ks-ap KSAP-archived-2013-09-25 to not exist");
		
		runImporter (repository, 65439);
	
		GitTestUtils.assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_inactive", "expected ks-ap inactive to exist");
		
		// assert file does not exist in a specific branch
//		assertRefNull(repository, "enrollment, notNullMessage);
		
		runImporter (repository, 65440);
		
		GitTestUtils.assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP", "expected ks-ap branches KSAP to exist");
		
		GitTestUtils.assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_inactive", "expected ks-ap inactive to not exist");
		
		runImporter (repository, 65441);
		
		GitTestUtils.assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP-M8", "expected ks-ap branches KSAP-M8 to exist");
		
		GitTestUtils.assertPathsExist (repository, "enrollment_ks-ap_branches_KSAP-M8", Arrays.asList(new String [] {"pom.xml"}));
		
		// check that the 
//		assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_inactive", "expected ks-ap inactive to not exist");
		
		runImporter (repository, 65442);
		
		GitTestUtils.assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_trunk", "expected ks-ap trunk to not exist");
		
		runImporter (repository, 65443);
		
		GitTestUtils.assertRefNotNull(repository, Constants.R_HEADS + "enrollment_ks-ap_trunk", "expected ks-ap trunk to exist");
		
		GitTestUtils.assertRefNull(repository, Constants.R_HEADS + "enrollment_ks-ap_branches_KSAP", "expected ks-ap branches KSAP to not exist");
		
		
	}

	private void runImporter(Repository repository, long importRevision) throws IOException {
		super.runImporter(repository, importRevision, "src/test/resources/ks-r" + importRevision + ".dump.bz2", "https://svn.kuali.org/repos/student", "fake-uuid", "kuali.org");
	}
	

}
