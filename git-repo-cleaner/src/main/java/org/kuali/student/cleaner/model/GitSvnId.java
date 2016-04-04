package org.kuali.student.cleaner.model;

public class GitSvnId {
    private String svnHost;
    private String svnUUID;
    private Integer revision;
    private String branchPath;

    public GitSvnId(String svnHost, String svnUUID, Integer revision, String branchPath) {
        this.svnHost = svnHost;
        this.svnUUID = svnUUID;
        this.revision = revision;
        this.branchPath = branchPath;
    }

    public String getSvnHost() {
        return svnHost;
    }

    public String getSvnUUID() {
        return svnUUID;
    }

    public Integer getRevision() {
        return revision;
    }

    public String getBranchPath() {
        return branchPath;
    }
}
