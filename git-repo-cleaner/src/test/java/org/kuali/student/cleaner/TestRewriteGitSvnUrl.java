package org.kuali.student.cleaner;

import org.kuali.student.cleaner.model.GitSvnIdUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ocleirig on 3/30/2016.
 */
@org.junit.runner.RunWith (value = org.junit.runners.BlockJUnit4ClassRunner.class)
public class TestRewriteGitSvnUrl {


    @org.junit.Test
    public void testRewriteGitSvnId () {

        String message = "[maven-release-plugin] prepare for next development iteration\n" +
                "\n" +
                "    git-svn-id: https://svn.jenkins-ci.org/trunk@41410 71c3de6d-444a-0410-be80-ed276b4c234a\n";

        String expectedMessage = "[maven-release-plugin] prepare for next development iteration\n" +
                "\n" +
                "    git-svn-id: https://svn.jenkins-ci.org/trunk/src/modules/my-module@41410 71c3de6d-444a-0410-be80-ed276b4c234a\n";

        String convertedMessage = GitSvnIdUtils.applyPathToExistingGitSvnId(message, "src/modules/my-module");

        assertEquals(expectedMessage, convertedMessage);

    }

    @org.junit.Test
    public void testExtractGitSvnId () throws java.io.IOException {
        String message = "[maven-release-plugin] prepare for next development iteration\n" +
                "\n" +
                "    git-svn-id: https://svn.jenkins-ci.org/trunk@41410 71c3de6d-444a-0410-be80-ed276b4c234a \n";

        org.kuali.student.cleaner.model.GitSvnId gitSvnId = org.kuali.student.cleaner.model.GitSvnIdUtils.extractGitSvnId("https://svn.jenkins-ci.org", message);

        assertNotNull(gitSvnId);

        assertEquals("https://svn.jenkins-ci.org", gitSvnId.getSvnHost());
        assertEquals("71c3de6d-444a-0410-be80-ed276b4c234a", gitSvnId.getSvnUUID());
        assertEquals("/trunk", gitSvnId.getBranchPath());
        assertEquals(Integer.valueOf(41410), gitSvnId.getRevision());
    }
}
