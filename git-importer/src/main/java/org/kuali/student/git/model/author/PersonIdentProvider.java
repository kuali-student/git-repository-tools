package org.kuali.student.git.model.author;

import org.eclipse.jgit.lib.PersonIdent;

/**
 * Created by ocleirig on 3/18/2016.
 */
public interface PersonIdentProvider {

    /**
     * @param svnAuthor
     * @return person ident for the given svn author
     */
    public PersonIdent getPerson(String svnAuthor);
}
