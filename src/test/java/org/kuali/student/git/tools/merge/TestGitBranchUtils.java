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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.student.git.model.AbstractKualiStudentBranchDetectorTest;
import org.kuali.student.git.model.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.NodeProcessor;
import org.kuali.student.git.model.branch.BranchDetectorImpl;
import org.kuali.student.git.model.branch.KualiStudentBranchDetectorImpl;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Test Git Branch Utils 
 * @author Kuali Student Team
 *
 */
public class TestGitBranchUtils extends AbstractKualiStudentBranchDetectorTest {

	private static final Logger log = LoggerFactory.getLogger(TestGitBranchUtils.class);
	
	/**
	 * 
	 */
	public TestGitBranchUtils() {
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
		
		assertPath("sandbox/searchwidgets/.classpath", "sandbox/searchwidgets", ".classpath", false);
		
		assertPath("enumeration/.classpath", "enumeration", ".classpath", false);
		assertPath("enumeration/enumeration-api/.classpath", "enumeration", "enumeration-api/.classpath", false);
		
		assertPath("dictionary/dictionary-api/.classpath", "dictionary", "dictionary-api/.classpath", false);
		
		assertPath("ks-cfg-dbs/ks-lum-db/schema.xml", "ks-cfg-dbs", "ks-lum-db/schema.xml", false);
		
		assertPath("deploymentlab/ks-cuke-testing/vendor/rails/activeresource/README", "deploymentlab/ks-cuke-testing", "vendor/rails/activeresource/README", false);
		
		assertPath("deploymentlab/UI/ks-cuke-testing/branches/kualim7/features/feature_definitions/culerity/manage_proposals.feature", "deploymentlab/UI/ks-cuke-testing/branches/kualim7", "features/feature_definitions/culerity/manage_proposals.feature", false);
		
		assertPath("trunk/.classpath", "trunk", ".classpath", false);
		
		assertPath ("poc/personidentity/branches", null, null, true);
		
		assertPath("sandbox/searchwidgets/ks-core-dev/ks-core-ui/src/main/resources/org/kuali/student/core/organization/ui/OrgEntry.gwt.xml", "sandbox/searchwidgets", "ks-core-dev/ks-core-ui/src/main/resources/org/kuali/student/core/organization/ui/OrgEntry.gwt.xml", false);
		
		assertPath("ks-web/branches/ks-web-dev/ks-web/ks-all/src/main/webapp/WEB-INF/tags/rice-portal", "ks-web/branches/ks-web-dev", "ks-web/ks-all/src/main/webapp/WEB-INF/tags/rice-portal", false);
		
		assertPath("enrollment/ks-api/tags/builds/ks-api-2.0/2.0.0-M7-KSAP/build-5/pom.xml", "enrollment/ks-api/tags/builds/ks-api-2.0/2.0.0-M7-KSAP/build-5", "pom.xml", false);
		
		assertPath("tags/builds/student-1.3/1.3.0/20120722-build-70/pom.xml", "tags/builds/student-1.3/1.3.0/20120722-build-70", "pom.xml", false);
		
		assertPath("enrollment/aggregate/branches/inactive/2.0.0-m8-api-upgrade/pom.xml", "enrollment/aggregate/branches/inactive/2.0.0-m8-api-upgrade",  "pom.xml", false);
		
		assertPath("sandbox/team2/ks-rice-standalone/branches/ks-rice-standalone-uberwar/src/main/webapp/WEB-INF/tags/rice-portal", "sandbox/team2/ks-rice-standalone/branches/ks-rice-standalone-uberwar", "src/main/webapp/WEB-INF/tags/rice-portal", false);
	
		assertPath("deploymentlab/ks-cuke-testing/vendor/gems/cucumber-0.6.1/examples/dos_line_endings/Rakefile", "deploymentlab/ks-cuke-testing",  "vendor/gems/cucumber-0.6.1/examples/dos_line_endings/Rakefile", false);
		
		assertPath("tools/maven-dictionary-generator/one-jar/create-one-jar-for-dictionary.cmd", "tools/maven-dictionary-generator", "one-jar/create-one-jar-for-dictionary.cmd", false);
		
		assertPath("deploymentlab/UI/ks-cuke-testing/vendor/rails/activerecord/examples/associations.png", "deploymentlab/UI/ks-cuke-testing", "vendor/rails/activerecord/examples/associations.png", false);
	
		assertPath ("deploymentlab/UI/ks-cuke-testing/trunk/vendor/rails/activerecord/examples/associations.png", "deploymentlab/UI/ks-cuke-testing/trunk", "vendor/rails/activerecord/examples/associations.png", false);
		
		assertPath("deploymentlab/student/trunk/ks-tools/maven-component-sandbox/trunk/.classpath", "deploymentlab/student/trunk/ks-tools/maven-component-sandbox/trunk", ".classpath", false);
		
		assertPath("ks-lum/branches/ks-lum-dev/ks-lum-ui/src/main/resources/org/kuali/student/lum/lu/ui/tools/Tools.gwt.xml", "ks-lum/branches/ks-lum-dev", "ks-lum-ui/src/main/resources/org/kuali/student/lum/lu/ui/tools/Tools.gwt.xml", false);
	
		assertPath("trunk", "trunk", "", false);
		
		assertPath("branches/trunk", "branches/trunk", "", false);
	}
	
	@Test
	public void testBranchPaths() {
		
		assertPath("deploymentlab/branches/proposalhistory/ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal", "deploymentlab/branches/proposalhistory", "ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal", false);
		
		assertPath("tags/ks-old-directory-structure/brms/tags/ks-tags/ks-old-directory-structure/brms-1.0.0-m3/ks-tags/ks-old-directory-structure/brms-execution-api/src/main/resources/org/kuali/student/tags/ks-old-directory-structure/pom.xml", "tags/ks-old-directory-structure/brms/tags/ks-tags", "ks-old-directory-structure/brms-1.0.0-m3/ks-tags/ks-old-directory-structure/brms-execution-api/src/main/resources/org/kuali/student/tags/ks-old-directory-structure/pom.xml", false);
		
		assertPath("merges/ks-1.3-services-merge-deux/ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal/channel/main/ec1Applications.tag", "merges/ks-1.3-services-merge-deux", "ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal/channel/main/ec1Applications.tag", false);
	
		assertPath("merges", "merges/ks-1.3-services-merge-deux", "ks-web/ks-embedded/src/main/webapp/WEB-INF/tags/rice-portal/channel/main/ec1Applications.tag", true);
		
		assertPath("merges/ks-1.3-services-merge-deux", "merges/ks-1.3-services-merge-deux", "", false);
		
		assertPath ("enrollment/ks-api/trunk/pom.xml", "enrollment/ks-api/trunk", "pom.xml", false);
		
		assertPath ("enrollment/aggregate/branches/KSAP/pom.xml", "enrollment/aggregate/branches/KSAP", "pom.xml", false);
	}
	
	@Test
	public void testUnderscorePath() {
		
		String path = "deploymentlab/branches/xapool_43/pom.xml";
		
		String branchPath = "deploymentlab/branches/xapool_43";
		
		assertPath(path, branchPath, "pom.xml", false);
		
		String canonicalName = GitBranchUtils.getCanonicalBranchName(branchPath, 0L, new LargeBranchNameProviderMapImpl());
		
		Assert.assertEquals("deploymentlab_branches_xapool===43", canonicalName);
		
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
		
		String convertedPath = GitBranchUtils.convertToTargetPath(path, 1L, copyFromPath, "pom.xml", new BranchDetectorImpl());
		
		Assert.assertEquals(path + "/pom.xml", convertedPath);
		
		path = "contrib/myplan/tags/ks-myplan-1.1.2/ks-myplan/new_branch";
		
		copyFromPath = "contrib/myplan/tags/ks-myplan-1.1.2";
		
		convertedPath = GitBranchUtils.convertToTargetPath(path, 1L, copyFromPath, "another-directory/pom.xml", new BranchDetectorImpl());
		
		Assert.assertEquals(path + "/another-directory/pom.xml", convertedPath);
		
		log.info("");
		
	}
	private void assertPath (String filePath, String expectedBranchPath, String expectedFilePath, boolean expectVeto) {
		
		try {
			BranchData data = branchDetector.parseBranch(0L, filePath);
			
			Assert.assertEquals(expectedBranchPath, data.getBranchPath());
			Assert.assertEquals(expectedFilePath, data.getPath());
		} catch (VetoBranchException e) {
			Assert.assertTrue(filePath + "vetoed unexpectantly.", expectVeto);
		}
		
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
