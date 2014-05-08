/**
 * 
 */
package org.kuali.student.git.model.branch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Kuali Student Team (ks.collab@kuali.org)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:kuali-foundation-branch-detector-test-context.xml"})
public class TestKualiFoundationBranchDetectorImpl extends
		AbstractBranchDetectorTest {

	/**
	 * 
	 */
	public TestKualiFoundationBranchDetectorImpl() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testPaths() {
		
		assertPath("rice/impex/rice-impex-master-1.0.3/KREW_DOC_TYP_ATTR_T.xml", "rice/impex/rice-impex-master-1.0.3", "KREW_DOC_TYP_ATTR_T.xml");
		
		assertPath("legacy/mocks/KCUIWG/KC-propdev/p2-prototype-a/assets/js/tags/keywords.html", "legacy/mocks/KCUIWG/KC-propdev/p2-prototype-a/assets/js/tags/keywords.html", "");
		
		assertPath("legacy/mocks/KCUIWG/KC/index.html", "legacy", "mocks/KCUIWG/KC/index.html", true);
		
		// default path is detected in the NodeProcessor the branch detector would call this invalid.
		assertPath("ks/ks-lum-ui-1.2-selenium/src/test/java/org/kuali/student/lum/ui/selenium/CheeseTitlePresentCondtion.java", "ks", "ks-lum-ui-1.2-selenium/src/test/java/org/kuali/student/lum/ui/selenium/CheeseTitlePresentCondtion.java", true);
		
	}
}
