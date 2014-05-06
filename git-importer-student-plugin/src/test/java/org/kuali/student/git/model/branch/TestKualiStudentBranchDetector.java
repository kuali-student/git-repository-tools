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
package org.kuali.student.git.model.branch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.student.branch.model.BranchData;
import org.kuali.student.git.model.AbstractKualiStudentBranchDetectorTest;
import org.kuali.student.git.model.branch.exceptions.VetoBranchException;
import org.kuali.student.git.model.branch.large.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.branch.utils.GitBranchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Test Git Branch Utils 
 * @author Kuali Student Team
 *
 */
public class TestKualiStudentBranchDetector extends AbstractKualiStudentBranchDetectorTest {

	private static final Logger log = LoggerFactory.getLogger(TestKualiStudentBranchDetector.class);
	
	/**
	 * 
	 */
	public TestKualiStudentBranchDetector() {
	}
	
	@Test
	@Ignore
	public void testAgainstPaths() throws IOException, VetoBranchException {
		
		BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/paths.txt"));
		
		String line = null;
		
		while ((line = reader.readLine()) != null) {
			String parts[] = line.split(":");
			
			String path = parts[1].trim();
			
			line = reader.readLine();
			
			parts = line.split(":");
			
			String kind = parts[1].trim();
			
			testPath (kind, path);
			
			String spacer = reader.readLine();
			
			Assert.assertEquals("--", spacer);
		}
		reader.close();
	}

	@Test
	public void testCanonicalBranchName () {
		String path = "poc/common/brms-dev";
		
		String expectedBranchName = "poc_common_brms-dev";
		
		String actualBranchName = GitBranchUtils.getCanonicalBranchName(path, 1L, new LargeBranchNameProviderMapImpl());
		
		Assert.assertEquals(expectedBranchName, actualBranchName);
		
		path = "tags_ks-old-directory-structure_ks-web_tags_tags_ks-old-directory-structure_ks-web-1.1.0-SNAPSHOT_ks-embedded_src_main_webapp_WEB-INF_tags_rice-portal/tags_ks-old-directory-structure_ks-web_tags_tags_ks-old-directory-structure_ks-web-1.1.0-SNAPSHOT_ks-embedded_src_main_webapp_WEB-INF_tags_rice-portal";
		
		actualBranchName = GitBranchUtils.getCanonicalBranchName(path, 1L, new LargeBranchNameProviderMapImpl());
		
		Assert.assertEquals(40, actualBranchName.length());
		
		
	}
	
	@Test
	public void testBranchesPathVeto() {
		
		String path = "branches";
		
		boolean exception = false;
		try {
			branchDetector.parseBranch(0L, path);
		} catch (VetoBranchException e) {
			exception = true;
		}
		
		Assert.assertEquals(true, exception);
	}
	
	@Test
	public void testEncounteredPaths() {
		
		assertPath("sandbox/searchwidgets/.classpath", "sandbox/searchwidgets", ".classpath");
		
		assertPath("enumeration/.classpath", "enumeration", ".classpath");
		assertPath("enumeration/enumeration-api/.classpath", "enumeration", "enumeration-api/.classpath");
		
		assertPath("dictionary/dictionary-api/.classpath", "dictionary", "dictionary-api/.classpath");
		
		assertPath("ks-cfg-dbs/ks-lum-db/schema.xml", "ks-cfg-dbs", "ks-lum-db/schema.xml");
		
		assertPath("deploymentlab/ks-cuke-testing/vendor/rails/activeresource/README", "deploymentlab/ks-cuke-testing", "vendor/rails/activeresource/README");
		
		assertPath("deploymentlab/UI/ks-cuke-testing/branches/kualim7/features/feature_definitions/culerity/manage_proposals.feature", "deploymentlab/UI/ks-cuke-testing/branches/kualim7", "features/feature_definitions/culerity/manage_proposals.feature");
		
		assertPath("trunk/.classpath", "trunk", ".classpath");
		
		assertPath ("poc/personidentity/branches", null, null, true);
		
		assertPath("sandbox/searchwidgets/ks-core-dev/ks-core-ui/src/main/resources/org/kuali/student/core/organization/ui/OrgEntry.gwt.xml", "sandbox/searchwidgets", "ks-core-dev/ks-core-ui/src/main/resources/org/kuali/student/core/organization/ui/OrgEntry.gwt.xml");
		
		assertPath("ks-web/branches/ks-web-dev/ks-web/ks-all/src/main/webapp/WEB-INF/tags/rice-portal", "ks-web/branches/ks-web-dev", "ks-web/ks-all/src/main/webapp/WEB-INF/tags/rice-portal");
		
		assertPath("enrollment/ks-api/tags/builds/ks-api-2.0/2.0.0-M7-KSAP/build-5/pom.xml", "enrollment/ks-api/tags/builds/ks-api-2.0/2.0.0-M7-KSAP/build-5", "pom.xml");
		
		assertPath("tags/builds/student-1.3/1.3.0/20120722-build-70/pom.xml", "tags/builds/student-1.3/1.3.0/20120722-build-70", "pom.xml");
		
		assertPath("enrollment/aggregate/branches/inactive/2.0.0-m8-api-upgrade/pom.xml", "enrollment/aggregate/branches/inactive/2.0.0-m8-api-upgrade",  "pom.xml");
		
		assertPath("sandbox/team2/ks-rice-standalone/branches/ks-rice-standalone-uberwar/src/main/webapp/WEB-INF/tags/rice-portal", "sandbox/team2/ks-rice-standalone/branches/ks-rice-standalone-uberwar", "src/main/webapp/WEB-INF/tags/rice-portal");
	
		assertPath("deploymentlab/ks-cuke-testing/vendor/gems/cucumber-0.6.1/examples/dos_line_endings/Rakefile", "deploymentlab/ks-cuke-testing",  "vendor/gems/cucumber-0.6.1/examples/dos_line_endings/Rakefile");
		
		assertPath("tools/maven-dictionary-generator/one-jar/create-one-jar-for-dictionary.cmd", "tools/maven-dictionary-generator", "one-jar/create-one-jar-for-dictionary.cmd");
		
		assertPath("deploymentlab/UI/ks-cuke-testing/vendor/rails/activerecord/examples/associations.png", "deploymentlab/UI/ks-cuke-testing", "vendor/rails/activerecord/examples/associations.png");
	
		assertPath ("deploymentlab/UI/ks-cuke-testing/trunk/vendor/rails/activerecord/examples/associations.png", "deploymentlab/UI/ks-cuke-testing/trunk", "vendor/rails/activerecord/examples/associations.png");
		
		assertPath("deploymentlab/student/trunk/ks-tools/maven-component-sandbox/trunk/.classpath", "deploymentlab/student/trunk/ks-tools/maven-component-sandbox/trunk", ".classpath");
		
		assertPath("ks-lum/branches/ks-lum-dev/ks-lum-ui/src/main/resources/org/kuali/student/lum/lu/ui/tools/Tools.gwt.xml", "ks-lum/branches/ks-lum-dev", "ks-lum-ui/src/main/resources/org/kuali/student/lum/lu/ui/tools/Tools.gwt.xml");
	
		assertPath("trunk", "trunk", "");
		
		assertPath("branches/trunk", "branches/trunk", "");
		
		assertPath("deploymentlab/ks-cuke-testing/trunk/pom.xml", "deploymentlab/ks-cuke-testing/trunk", "pom.xml");
		
		assertPath("deploymentlab/ks-1.1-db/1.0.x/ks-lum/ks-lum-ui/src/main/resources/org/kuali/student/lum/lu/ui/tools/Tools.gwt.xml", "deploymentlab/ks-1.1-db/1.0.x", "ks-lum/ks-lum-ui/src/main/resources/org/kuali/student/lum/lu/ui/tools/Tools.gwt.xml");
		
		assertPath("deploymentlab/ks-1.1-db/maven-site-plugin/src/site/apt/examples/creatingskins.apt", "deploymentlab/ks-1.1-db/maven-site-plugin", "src/site/apt/examples/creatingskins.apt");

		assertPath("sandbox/enrollment/ks-deployments/tags/builds/ks-deployments-2.0/2.0.0/20120921-r36420/ks-web/ks-embedded/src/main/resources/org/kuali/rice/standalone/config/build-details.xml", "sandbox/enrollment/ks-deployments/tags/builds/ks-deployments-2.0/2.0.0/20120921-r36420", "ks-web/ks-embedded/src/main/resources/org/kuali/rice/standalone/config/build-details.xml");
		
		assertPath("enrollment/ks-deployments/tags/builds/ks-deployments-2.0/2.0.0-M6/build-143/ks-web/ks-embedded/src/main/resources/org/kuali/rice/standalone/config/build-details.xml", "enrollment/ks-deployments/tags/builds/ks-deployments-2.0/2.0.0-M6/build-143", "ks-web/ks-embedded/src/main/resources/org/kuali/rice/standalone/config/build-details.xml");
	
//		assertPath("", "", "");
		
		assertPath("enrollment/ks-deployments/tags/builds/ks-deployments-2.0/2.0.0-KD-DEMO/build-13/ks-web/ks-embedded/src/main/resources/org/kuali/rice/standalone/config/build-details.xml", "enrollment/ks-deployments/tags/builds/ks-deployments-2.0/2.0.0-KD-DEMO/build-13", "ks-web/ks-embedded/src/main/resources/org/kuali/rice/standalone/config/build-details.xml");

		assertPath("examples/sample-config-project/src/main/resources/ks-sample-courseInfo-dictionary-context.xml", "examples/sample-config-project", "src/main/resources/ks-sample-courseInfo-dictionary-context.xml");
		
		assertPath("examples/training/cm-myschool-config-project/src/main/java/edu/kuali/config/lum/lu/ui/client/course/configuration/SampleCourseProposalConfigurer.java", "examples/training/cm-myschool-config-project", "src/main/java/edu/kuali/config/lum/lu/ui/client/course/configuration/SampleCourseProposalConfigurer.java");
		
		assertPath("sandbox/deploymentlab/ks-1.1-db/maven-site-plugin/src/site/apt/examples/creatingskins.apt", "sandbox/deploymentlab/ks-1.1-db/maven-site-plugin", "src/site/apt/examples/creatingskins.apt");
	
		assertPath("test/functional-automation/sambal/rice_2_3_m2_AFT_branch/lib/sambal-kuali/pages/enrollment_page.rb", "test/functional-automation/sambal/rice_2_3_m2_AFT_branch", "lib/sambal-kuali/pages/enrollment_page.rb");
		
		assertPath("contrib/CM/ks-deployments/trunk/ks-web/pom.xml", "contrib/CM/ks-deployments/trunk", "ks-web/pom.xml");
		
		assertPath("contrib/CM/ks-deployments/branches/feature1/ks-web/pom.xml", "contrib/CM/ks-deployments/branches/feature1", "ks-web/pom.xml");
		
		assertPath("contrib/CM/ks-deployments/ks-web/pom.xml", "contrib/CM/ks-deployments", "ks-web/pom.xml");
		
		assertPath("contrib/CM/aggregate/pom.xml", "contrib/CM/aggregate", "pom.xml");
		
		assertPath("contrib/CM/ks-deployments/ks-web/ks-with-rice-bundled/src/main/webapp/WEB-INF/tags/rice-portal/portalTabs.tag", "contrib/CM/ks-deployments", "ks-web/ks-with-rice-bundled/src/main/webapp/WEB-INF/tags/rice-portal/portalTabs.tag");
	
		assertPath("poc/learningunitmanagement/lum-ri/branches/lum-ri-dev/luipersonrelation-ri/src/main/resources/META-INF/persistence.xml", "poc/learningunitmanagement/lum-ri/branches/lum-ri-dev", "luipersonrelation-ri/src/main/resources/META-INF/persistence.xml");
		
		assertPath("examples/training/cm-myschool-config-project2/catalog.xml", "examples/training/cm-myschool-config-project2", "catalog.xml", true);
		
		assertPath ("test/functional-automation/ksap-aft/lib/sambal-kuali/pages/ks_maintenance_portal_page.rb", "test/functional-automation/ksap-aft", "lib/sambal-kuali/pages/ks_maintenance_portal_page.rb");
		
		assertPath("enrollment/aggregate/branches/inactive/KSAP", "enrollment/aggregate/branches/inactive/KSAP", "");
		
		assertPath("enrollment/ks-deployments/trunk/ks-web/ks-embedded/src/main/webapp/WEB-INF/tags", "enrollment/ks-deployments/trunk", "ks-web/ks-embedded/src/main/webapp/WEB-INF/tags");
	}
	
	@Test
	public void testBranchPaths() {
		
		assertPath("deploymentlab/branches/proposalhistory/ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal", "deploymentlab/branches/proposalhistory", "ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal");
		
		assertPath("tags/ks-old-directory-structure/brms/tags/ks-tags/ks-old-directory-structure/brms-1.0.0-m3/ks-tags/ks-old-directory-structure/brms-execution-api/src/main/resources/org/kuali/student/tags/ks-old-directory-structure/pom.xml", "tags/ks-old-directory-structure/brms/tags/ks-tags", "ks-old-directory-structure/brms-1.0.0-m3/ks-tags/ks-old-directory-structure/brms-execution-api/src/main/resources/org/kuali/student/tags/ks-old-directory-structure/pom.xml");
		
		assertPath("merges/ks-1.3-services-merge-deux/ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal/channel/main/ec1Applications.tag", "merges/ks-1.3-services-merge-deux", "ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal/channel/main/ec1Applications.tag");
	
		assertPath("merges", "merges/ks-1.3-services-merge-deux", "ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal/channel/main/ec1Applications.tag", true);
		
		assertPath("merges/ks-1.3-services-merge-deux", "merges/ks-1.3-services-merge-deux", "");
		
		assertPath ("enrollment/ks-api/trunk/pom.xml", "enrollment/ks-api/trunk", "pom.xml");
		
		assertPath ("enrollment/aggregate/branches/KSAP/pom.xml", "enrollment/aggregate/branches/KSAP", "pom.xml");
		
		assertPath("builds/old-build-tags/ks-1.0.0-rc2/ks-brms-1.0.0-rc2/.classpath", "builds/old-build-tags/ks-1.0.0-rc2", "ks-brms-1.0.0-rc2/.classpath");
		
		assertPath("tags/ks-old-directory-structure/ks-lum/tags/ks-lum-1.0.0-m3/ks-lum-api/src/main/java/org/kuali/student/lum/lo/dto/LoInfo.java", "tags/ks-old-directory-structure/ks-lum/tags/ks-lum-1.0.0-m3", "ks-lum-api/src/main/java/org/kuali/student/lum/lo/dto/LoInfo.java");
	
		assertPath ("sushik-component-manual-impl/src/main/java/org/sushik/client/component/impl/base/ui/ErrorLoggerImpl.java", "sushik-component-manual-impl", "src/main/java/org/sushik/client/component/impl/base/ui/ErrorLoggerImpl.java");
	
		assertPath ("sandbox/ks-1.3-core-slice-demo/modules/ks-pom/trunk/fusion-maven-plugin.dat", "sandbox/ks-1.3-core-slice-demo/modules/ks-pom/trunk", "fusion-maven-plugin.dat");
		
		assertPath ("ks-1.2-umd/ks-admin/src/main/webapp/WEB-INF/tags/kim/attributeLookup.tag", "ks-1.2-umd", "ks-admin/src/main/webapp/WEB-INF/tags/kim/attributeLookup.tag", true);
	}
	
	@Test
	public void testUnderscorePath() {
		
		String path = "deploymentlab/branches/xapool_43/pom.xml";
		
		String branchPath = "deploymentlab/branches/xapool_43";
		
		assertPath(path, branchPath, "pom.xml");
		
		String canonicalName = GitBranchUtils.getCanonicalBranchName(branchPath, 0L, new LargeBranchNameProviderMapImpl());
		
		Assert.assertEquals("deploymentlab_branches_xapool===43", canonicalName);
		
		String branch = GitBranchUtils.getBranchPath(canonicalName, 0, new LargeBranchNameProviderMapImpl());
		
		Assert.assertEquals(branchPath, branch);
		
	}
	
	@Test
	public void testSpacePath() {
		
		String path = "contrib/myplan/branches/cm 2.0/ks-bundled-bootstrap-db/src/main/impex/KRIM_ENT_NM_TYP_T.xml";
		
		String branchPath = "contrib/myplan/branches/cm 2.0";
		
		assertPath(path, branchPath, "ks-bundled-bootstrap-db/src/main/impex/KRIM_ENT_NM_TYP_T.xml");
		
		String canonicalName = GitBranchUtils.getCanonicalBranchName(branchPath, 0L, new LargeBranchNameProviderMapImpl());
		
		Assert.assertEquals("contrib_myplan_branches_cm---2.0", canonicalName);
		
		String branch = GitBranchUtils.getBranchPath(canonicalName, 0, new LargeBranchNameProviderMapImpl());
		
		Assert.assertEquals(branchPath, branch);
		
	}
	
	@Test
	public void testTargetBranchPathFromBranchDirectoryAdd () throws VetoBranchException {
		/*
		 * From ks revisoin 43095
		 * 
		 * 
		 * Node-path: contrib/myplan/tags/ks-myplan-1.1.2/ks-myplan/new_branch
		 * Node-kind: dir
		 * Node-action: add
		 * Node-copyfrom-rev: 43095
		 * Node-copyfrom-path: contrib/myplan/tags/ks-myplan-1.1.2
		 */
		
		String path = "contrib/myplan/tags/ks-myplan-1.1.2/ks-myplan/new_branch";
		
		String copyFromPath = "contrib/myplan/tags/ks-myplan-1.1.2";
		
		BranchData copyFromBranch = branchDetector.parseBranch(0L, copyFromPath);
		
		String convertedPath = GitBranchUtils.convertToTargetPath(path, 1L, copyFromPath, "pom.xml", copyFromBranch);
		
		Assert.assertEquals(path + "/pom.xml", convertedPath);
		
		path = "contrib/myplan/tags/ks-myplan-1.1.2/ks-myplan/new_branch";
		
		copyFromPath = "contrib/myplan/tags/ks-myplan-1.1.2";
		
		copyFromBranch = branchDetector.parseBranch(0L, copyFromPath);
		
		convertedPath = GitBranchUtils.convertToTargetPath(path, 1L, copyFromPath, "another-directory/pom.xml", copyFromBranch);
		
		Assert.assertEquals(path + "/another-directory/pom.xml", convertedPath);
		
		log.info("");
		
	}
	
	@Test
	public void testDateRevisionFormatRegex () {
		
		String target ="20120921-r36420";
		
		Assert.assertEquals(true, target.matches (KualiStudentBranchDetectorImpl.DATE_STAMP_SVN_REVISION_BUILD_TAG_NAME_PATTERN));
				
				
	}
	
	
	private void testPath(String kind, String path) throws VetoBranchException {
		
		if ("file".equals(kind)) {
			BranchData data = branchDetector.parseBranch(0L, path);
			
			log.info("path = " + path);
			log.info("branchPath = " + data.getBranchPath());
			log.info("");
		}
		else if ("dir".equals(kind)) {
			
		}
		else {
			throw new RuntimeException("invalid kind");
		}
		
	}

}
