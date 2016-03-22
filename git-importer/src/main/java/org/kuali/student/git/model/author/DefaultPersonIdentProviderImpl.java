package org.kuali.student.git.model.author;


import org.eclipse.jgit.lib.PersonIdent;

public class DefaultPersonIdentProviderImpl implements HostBasedPersonIdentProvider  {

    private String emailHostPart;

    public String getEmailHostPart() {
        return emailHostPart;
    }

    @Override
    public void setEmailHostPart(String emailHostPart) {
        this.emailHostPart = emailHostPart;
    }

    @Override
    public PersonIdent getPerson(String svnAuthor) {

        String userName = svnAuthor;

        String emailAddress = userName + "@" + emailHostPart;

        return new PersonIdent(userName,
                emailAddress);

    }
}
