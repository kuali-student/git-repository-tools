package org.kuali.student.git.model.author;


public interface HostBasedPersonIdentProvider extends PersonIdentProvider {

    /*
     * Set the host email part.  All SVN authors will be come author@emailHostPart
     */
    void setEmailHostPart(String emailHostPart);
}
