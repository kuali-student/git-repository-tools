README

SVN History Tools provide two core functions:

1. determine the per file linkage between two subersion revisions.  Including where the content is the same (copy/rename) and also where it is different (copy/rename followed by a modification)

2. Apply this linkage data to an SVN repository dump file for the target revision such that in the exported repository dump file the file history is joined using 'copyfrom' details from phase 1.

Note: SVN version 2 and 3 dump's were used when writing this tool but v3 dumps were used for the actual conversion.

Running the tools:

scripts/Prepare.py

Program Arguments: </abs/path/to/git/repo/.git dir> <repair.dat>

The .git directory needs to be specified in the path.  It needs to already exist.  The preparation process will be creating tags named rXYZ for each XYZ revision.

If the git repository does not exist you should create one (mkdir /some/dir; cd /some/dir; git init)

If the tag already exists the svn export of that tree won't occur again.

You need to raise or define in ~/.gitconfig the diff.renamelimit configuration variable this will allow --find-copies-harder to find partial and very changed files.


repair.dat Format:

# comment lines
FETCH-TARGET;http://svn.kuali.org/repos/student/somepath1;rX1
FETCH-COPY-FROM;http://svn.kuali.org/repos/student/somepath2;rX2
DIFF;rX2;rX3

The FETCH-TARGET line means that we should get the --incremental dump of that path.  This is the set of files and directories that changed on that revision.

The FETCH-COPY-FROM line means we should get the full dump of that path.  This includes all of the files that existed at that revision even if they were added by previous revisions.

For each DIFF line the copyfrom details will be determined targetting the first tag sourcing from the second tag.

A file will be created for each DIFF line like rX2-rX3-join.dat 

These files can be passed into the SvnDumpFilter and are used to rewrite history 

Svn Dump Filter

The maven shade plugin is used to create an uber jar that contains all of the dependency jars bundled in a single jar.

mvn clean install then java -jar target/svn-history-tools-0.0.1-SNAPSHOT-shaded.jar for the command line options.

Main Class: org.kuali.student.svn.tools.Main

Arguments: <svn dump file version: 2 or 3> <revision joining detail n> ... <revision joining details n>

We expect an rXYZ.dump file to exist for both the target and copyfrom revision in the current working directory.

multiple revision joining details files can be added.  These are the output files from the Prepare.py program.

once rewritten the target dump file needs to be loaded into a new svn repository and tested out.


svnadmin create test

create a test/hooks/pre-revprop-change file with the following contents:
#!/bin/bash

exit 0

chmod +x test/hooks/pre-revprop-change

This tool only rewrites single svn repository revisions so in order to make the change stick you need to load the non rewritten changes before and after the change.

svnrdump --incremental -r 0:XYZ-1 https://path to original repo > r0-XYZ-1.dump

svnrdump --incremental -r XYZ+1:HEAD https://path to original repo > rXYZ+1-HEAD.dump

Then apply the changes like:

svnrdump load file://absolute/path/to/test < r0-XYZ-1.dump

Now apply the modified revision

svnrdump load file://absolute/path/to/test < rXYZ-filtered.dump

Now apply the other repo changes on top

svnrdump load file://absolute/path/to/test < rXYZ+1-HEAD.dump


Test svn log file://absolute/path/to/test/path/to/file and verify you see history to an earlier point then with the original repository.


The Java code uses FileInputStream's and FileOutputStream's and some code copied from SVNKit that allows us to stream.readLine() in a way similiar to how a BufferedReader works.

See IOUtils for the usage of the methods.

SVN Kit Licence:
The TMate Open Source License.


This license applies to all portions of TMate SVNKit library, which 
are not externally-maintained libraries (e.g. Ganymed SSH library).

All the source code and compiled classes in package org.tigris.subversion.javahl
except SvnClient class are covered by the license in JAVAHL-LICENSE file

Copyright (c) 2004-2012 TMate Software. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, 
      this list of conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, 
      this list of conditions and the following disclaimer in the documentation 
      and/or other materials provided with the distribution.
      
    * Redistributions in any form must be accompanied by information on how to 
      obtain complete source code for the software that uses SVNKit and any 
      accompanying software that uses the software that uses SVNKit. The source 
      code must either be included in the distribution or be available for no 
      more than the cost of distribution plus a nominal fee, and must be freely 
      redistributable under reasonable conditions. For an executable file, complete 
      source code means the source code for all modules it contains. It does not 
      include source code for modules or files that typically accompany the major 
      components of the operating system on which the executable file runs.
      
    * Redistribution in any form without redistributing source code for software 
      that uses SVNKit is possible only when such redistribution is explictly permitted 
      by TMate Software. Please, contact TMate Software at support@svnkit.com to 
      get such permission.

THIS SOFTWARE IS PROVIDED BY TMATE SOFTWARE ``AS IS'' AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT, ARE 
DISCLAIMED. 

IN NO EVENT SHALL TMATE SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

rewritten repository was applied on June 13, 2013
