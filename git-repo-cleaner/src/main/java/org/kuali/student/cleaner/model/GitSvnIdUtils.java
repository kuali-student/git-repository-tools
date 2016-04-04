package org.kuali.student.cleaner.model;

/**
 * Created by ocleirig on 3/30/2016.
 */
public final class GitSvnIdUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GitSvnIdUtils.class);

    private GitSvnIdUtils() {
    }

    public static String applyPathToExistingGitSvnId (String originalCommitMessage, String additionalPath) {

        StringBuilder builder = new StringBuilder(originalCommitMessage);

        int index = builder.indexOf("git-svn-id:");
        
        if (index == -1) {
        	// no git-svn-id line so skip
        	return originalCommitMessage;
        }

        int atRevisionIndex = builder.indexOf("@", index);
        
        if (atRevisionIndex == -1) {
        	// not in the correct format so skip
        	return originalCommitMessage;
        }

        String prefix = additionalPath.charAt(0) == '/'?"":"/";

        builder = builder.insert(atRevisionIndex, prefix + additionalPath);

        return builder.toString();
    }

    /**
     * Parse the git-svn-id: line from a commit message and build a GitSvnId object containing the details.
     *
     * @param host the subversion host part to make parsing easier.
     * @param commitMessage the commit message to search for the git-svn-id: line within.
     * @return the GitSvnId object representation of the located git-svn-id line.
     * @throws java.io.IOException
     */
    public static GitSvnId extractGitSvnId (String host, String commitMessage) throws java.io.IOException {

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.StringReader(commitMessage));

        String line = reader.readLine();

        while (line != null) {

            int gitSvnIdIndex = line.indexOf("git-svn-id");

            if (gitSvnIdIndex != -1) {

                int hostOffset = gitSvnIdIndex + "git-svn-id: ".length() + host.length();

                int atRevisionIndex = line.indexOf("@", hostOffset);

                String branchPath = line.substring(hostOffset, atRevisionIndex);

                int spaceAfterRevisionIndex = line.indexOf (" ", atRevisionIndex);

                String revision = line.substring(atRevisionIndex+1, spaceAfterRevisionIndex);

                String uuid = line.substring(spaceAfterRevisionIndex+1, spaceAfterRevisionIndex+37);

                reader.close();
                return new GitSvnId(host, uuid, Integer.parseInt(revision), branchPath);
            }

            line = reader.readLine();
        }

        reader.close();
        return null;
    }



}
