package org.kuali.student.git.model.author;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.PersonIdent;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A PersonIdentProvider backed by an authors file like:
 *
 * svn username = First <space> Last <space> <email@host.com>
 */
public class AuthorsFilePersonIdentProvider implements PersonIdentProvider {

    private Map<String, PersonIdent>svnUserNameToGitAuthorMap = new LinkedHashMap<>();

    private final org.eclipse.jgit.lib.PersonIdent unknownAuthor;

    public AuthorsFilePersonIdentProvider(String fileName, String unknownAuthorHost) throws java.io.IOException {

        List<String> lines = FileUtils.readLines(new File(fileName));

        for (String line : lines) {
            String parts[] = line.split(" = ");

            String svnUserName = parts[0].trim();

            String nameWithEmail = parts[1];

            int startOfEmailIndex = nameWithEmail.indexOf("<");

            String name  = nameWithEmail.substring(0, startOfEmailIndex).trim();

            String email = nameWithEmail.substring(startOfEmailIndex).trim().replace("<", "").replaceAll(">", "");

            svnUserNameToGitAuthorMap.put(svnUserName, new PersonIdent(name, email));
        }

        unknownAuthor = new org.eclipse.jgit.lib.PersonIdent("unknown", "unknown@" + unknownAuthorHost);

    }


    @Override
    public PersonIdent getPerson(String svnAuthor) {

        PersonIdent gitAuthor = svnUserNameToGitAuthorMap.get(svnAuthor);

        if (gitAuthor == null)
            return unknownAuthor;
        else
            return gitAuthor;
    }
}
