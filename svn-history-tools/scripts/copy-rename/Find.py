#!/usr/bin/python
"""

NAME: Find.py

PURPOSE: To find the copyfrom details of a given revision/path tuple using git to do the heavy lifting.

The git testsvn remote helper is a native import utility (written in c for speed instead of the existing git-svn.perl) designed for importing larger
repositories.

It is not complete yet for actual use but is very useful for our find history of this specific file purposes.

It can import the entire repository from the root / which is a match for the paths we will be extracting from the svn dump.  Then we can use the
git diff-tree command to find matches for the file of interest in a previous revision.

The diff-tree command expects to operate on tree's so we need to construct new unconnected tree objects that contain the files we want to compare for each revision.

For performance reasons we compare the entire revision to previous revision and log the output.

Then as a seperate step we match the files against the added files to assemble the copy from details.

Modes:



AUTHOR: Kuali Student Team <ks.collab@kuali.org>

"""

import subprocess
import string
import re
import sys
import fileinput
import os
import shutil
import logging
import tempfile

# this needs to be changed to md5sum on linux
# windows needs an absolute path to work.
# this one assumes you have git for windows installed.
echo_command="/bin/echo"

md5sum_command="/usr/bin/md5sum"

git_command="/usr/bin/git"

svn_command="/usr/bin/svn"

svnrdump_command="/usr/bin/svnrdump"

# TODO change to using os.path.join instead of this field
file_seperator="/"

# compare the two trees recursively and put the output into the named file.
def compareTrees(gitDirectory, treeASha1, treeBSha1, outputFileName):


    command = "{0} --git-dir={1} diff-tree -r --find-copies-harder --diff-filter=C,R,M {2} {3} | sort -rk5".format(git_command, gitDirectory, treeASha1, treeBSha1)
    
    output = subprocess.check_output(command, shell=1)

    lines = output.split("\n")

    
    outputFile = None

    for line in lines:
  
        if len (line) == 0:
            continue 
         
        if outputFile == None:

            outputFile = open (outputFileName, "w")


        outputFile.write ("{0}\n".format(line))

    if outputFile != None:
        outputFile.close()

# Represent a range of commits
# single = 1 if start and end are the same
class RevisionRange:

    def __init__ (self, start, end, single):
        self.start = start
        self.end = end
        self.single = single

# Represents a file add from the svn dump file
class Add:

    def __init__(self, code, kind, path):
        self.code = code
        self.kind = kind
        self.path = path

# represents the files that were added by a specific revision
class RevisionAddData:

    def __init__(self, revision):
        self.revision = revision

        self.paths = []

        self.codeToAddMap = {}

        self.pathToAddMap = {}

    # add a file add path to this revision.
    def addNormalPath (self, nodeKind, path):
       
        code = len (self.paths)

        add = Add (code, nodeKind, path)
 
        self.paths.append (add)

        self.codeToAddMap[code] = add

        self.pathToAddMap[path] = add

    # delegate to git diff-tree to compare the files added at the current revision to the
    # previous revision.
    def compare(self, gitDirectory):

        """
        extract the copyfrom details
        """

   
        compareRevision = self.revision - 1

        outputFileName = "compare-r{0}-to-r{1}.dat".format(compareRevision, self.revision)
        
        skipFileName = "skip-r{0}-to-r{1}.dat".format(compareRevision, self.revision)


        # check if the file exists
        if os.path.exists (skipFileName):
            print "{0} file exists skipping".format(skipFileName)
            return
             
        if os.path.exists (outputFileName):
            print "{0} file exists skipping".format(outputFileName)
            return
        
        # acquire the fake tree data

        treeContent = tempfile.NamedTemporaryFile(mode="w", delete=False)
        treeContentFileName = treeContent.name

        addedAtLeastOneFile = False

        for add in self.paths:

            # skip directories
            if add.kind == 'dir':
                continue
        
           
            # escape problem file characters like $ space and &. 
            dirName = os.path.dirname (add.path).replace("$", "\\$").replace(" ", "\\ ").replace("&", "\\&") 
            
            fileName = os.path.basename (add.path).replace("$", "\\$").replace(" ", "\\ ").replace("&", "\\&")
            
            command = "{0} --git-dir={1} ls-tree r{2}:{3} | grep \"{4}\" ".format(git_command, gitDirectory, self.revision, dirName, fileName)

            result = subprocess.check_output (command, shell=1)

            parts = result.strip().split("\t")

            withoutPath = parts[0]

            
            treeContent.write ("{0}\tP-{1}\n".format(withoutPath, add.code)) 

            addedAtLeastOneFile = True

       
        treeContent.close()


        if addedAtLeastOneFile == False:
            print "skipping r{0} because there are no files of interest".format(self.revision)
            skipFile = open (skipFileName, "w")
            skipFile.write("no files of interest\n")
            skipFile.close()
            return    

        treeContent = open (treeContentFileName, "r")

        command = "{0} --git-dir={1} mktree".format (git_command, gitDirectory)

        tree = subprocess.check_output (command, shell=1, stdin=treeContent).strip()

        treeContent.close()

        print "tree = {0}".format (tree)

        compareTrees(gitDirectory, "r{0}".format(compareRevision), tree, outputFileName)

        if not os.path.exists(outputFileName):

            output = open (skipFileName, "w")

            output.write ("compareTree's found nothing\n")
        
            output.close()

    # create the svn dump filter revision join information from the assumed to exist compare data.
    def process(self, gitDirectory):

        """

        We open up the compare date and match the files and direct
        """


        compareRevision = self.revision - 1
        
        inputFileName = "compare-r{0}-to-r{1}.dat".format(compareRevision, self.revision)

        # this is the naming expected by the filtering program (the reverse of this program)
        outputFileName = "r{0}-r{1}-join.dat".format (self.revision, compareRevision)


        
        skipFileName = "skip-r{0}-to-r{1}.dat".format(compareRevision, self.revision)

        if os.path.exists (outputFileName):
            print "{0} file exists skipping".format (outputFileName)
            return

        if os.path.exists (skipFileName):
            print "{0} file exists skipping".format(skipFileName)
            return
    

        outputFile = open (outputFileName, "w")

        foundAtLeastOneCopyFrom = False

        try:

            for line in fileinput.input (inputFileName):

                # Read the Raw output format section of the git diff-tree man page
                # ref: https://www.kernel.org/pub/software/scm/git/docs/git-diff-tree.html

          #      print line

                parts = line.strip().split("\t")

                statusPiece = parts[0]

                statusParts = statusPiece.split (" ")
            
                # status is the last part
                status = statusParts[4] 
           
                if status == "M":
                    # skip modifies
                    continue

                copyFromSha1 = statusParts[2]
                targetSha1 = statusParts[3]
 
                copyFromPath = parts[1]

                targetPath = parts[2]

                # convert P-{code} back into the add.path

                codeParts = targetPath.split("-")

                code = int (codeParts[1].strip())

                if code not in self.codeToAddMap.keys():
                    print "failed to find target for code {0}".format(code)
                    continue

                add = self.codeToAddMap[code]

                foundAtLeastOneCopyFrom = True

                md5 = self.computeMD5(gitDirectory, compareRevision, copyFromPath)

#                print "md5 = {0}".format(md5)

                outputFile.write ("#{0}\n{1}\n{2}||{3}\n{4}||{5}\n{6}||{7}\n".format(status, "PLACEHOLDER", self.revision, add.path, compareRevision, copyFromPath, copyFromSha1, md5))
                

            outputFile.close()

            if foundAtLeastOneCopyFrom == False:

                os.remove (outputFileName)
        except:
            # if the file does not yet exist we arrive here.
            # the main purpose is so that the program does not 
            # die on a file not found error.
            logging.exception("unexpected failure")
            pass 

    # compute the MD5 hash for the file path given based on its contents in the git repository.
    def computeMD5 (self, gitDirectory, revisionTag, path):
        
        command = "git --git-dir={0} show r{1}:{2}".format(gitDirectory, revisionTag, path)
    
        # print command
        
        p1 = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE)
        p2 = subprocess.Popen(md5sum_command, shell=True, stdin=p1.stdout, stdout=subprocess.PIPE)
        
        p1.stdout.close()
        
        output = p2.communicate()[0]
        
        md5Parts = output.split(" ")
        
        md5 = md5Parts[0]

        return md5
        
def usage(message):
        if message != None:
            print message    
        print "USAGE: {0} <mode:PREPARE-REPO or COMPARE or PROCESS or FETCH-DUMPS or APPLY> <path to git repository> <added file input data> [ mode specific options ]".format (sys.argv[0])
        print "PREPARE-REPO: will emit a script that when run will tag each commit so that for example commit r1 is tagged as r1, r34243 is tagged r34243, etc."
        print "COMPARE [startFromRevision]: will extract the comparison diff data based on the add file data"
        print "PROCESS [specificRevision]: will process the diff data accumulated in the COMPARE phase and use it to create SvnDumpFilter rewrite compatible join.dat files"
        print "FETCH-DUMPS [specificRevision]: will download the version 3 --incremental dump for the revisions that have data to be rewritten as determined in the PROCESS STEP."
        print "APPLY <svn repo url> [startRevision] [recreate]: will prepare a script to extract the range dumps between the rewritten revisions.  If _maximumRevision_ is not set HEAD is used."
        sys.exit (-1)

if len (sys.argv) < 4:
    usage(None)

mode = sys.argv[1]

if mode != "COMPARE" and mode !=  "PROCESS" and mode != "FETCH-DUMPS" and mode != 'PREPARE-REPO' and mode != 'PREPARE-TO-APPLY' and mode != 'APPLY':
    usage ("invalid mode: {0}".format(mode))

specificRevision = None

gitDirectory = sys.argv[2]
addData = sys.argv[3]


normalCount = 0
copyCount = 0

fileCount = 0
dirCount = 0

currentRevision = 0

currentRevisionData = None

commitSha1ToRevisionAddMap = {}

revisionAddData = []

# in both cases we acquire the data from the file
for line in fileinput.input (addData):

        strippedLine = line.strip()
        if len (strippedLine) == 0 or strippedLine[0] == '#':
            continue  # skip empty lines and comments  

        parts = strippedLine.split("::")

        nodeType = parts[0]

        nodeKind = parts[1]

        revision = int (parts[2])

        if revision > currentRevision:

                currentRevision = revision
                currentRevisionData = RevisionAddData (currentRevision)

                revisionAddData.append (currentRevisionData)

                print "starting on revision {0}".format(currentRevision)

        path = parts[3]

        if nodeType == 'normal':
                # print "looking for {0}@{1}".format (path, revision)

                
                currentRevisionData.addNormalPath (nodeKind, path)

                normalCount+=1
        else:
                copyCount+=1


        if nodeKind == 'file':
                fileCount+=1
        else:
                dirCount+=1



print "{0} normal paths\n{1} copied paths\n{2} file paths\n{3} directory paths".format(normalCount, copyCount, fileCount, dirCount)

if mode == 'PREPARE-REPO':

    output = open ("prepare-repo.sh", "w")

    command = "{0} --git-dir={1} rev-list origin/master | while read sha; do git --git-dir={2} log --pretty --format=\"commit:%H%n%B\" -n 1 $sha; done".format(git_command, gitDirectory, gitDirectory)

    output.write ("{0}\n".format(command))
        
    output.close()

    p = subprocess.Popen("/bin/sh ./prepare-repo.sh | /bin/egrep \"^commit:|git-svn-id\"", shell=1, stdout=subprocess.PIPE)

    commitSha1 = None

    tagFile = open ("create-repo-tags.sh",  "w")

    tagFile.write("#!/bin/sh\n")
        
    for line in p.stdout:

        if line.startswith("commit"):
            parts = line.split(":")

            if len(parts) != 2:
                print "invalid commit line = {0}".format(line)
                continue

            commitSha1 = parts[1].strip()

        else:

            parts = line.strip().split (" ")
            
            revisionContainingPart = parts[1]

            revisionParts = revisionContainingPart.split("@")

            revision = int (revisionParts[1])

            tagFile.write("{0} --git-dir={1} tag r{2} {3}\n".format(git_command, gitDirectory, revision, commitSha1))

            commitSha1 = None
        
    tagFile.close()

elif mode == 'COMPARE':

    """
    We need to extract the copyfrom details for each revision under consideration.
    """

    if len (sys.argv) == 5:
        specificRevision = int (sys.argv[4])

    for ad in revisionAddData:

        if ad.revision < specificRevision:
            continue
        else:

            print "Comparing r{0}".format (ad.revision)

            ad.compare(gitDirectory)

elif mode == 'PROCESS':

    if len (sys.argv) == 5:
        specificRevision = int (sys.argv[4])
    
    for ad in revisionAddData:
        if specificRevision != None:

            if ad.revision == specificRevision:

                ad.process(gitDirectory)
                break;
        else:
            ad.process(gitDirectory)

elif mode == 'FETCH-DUMPS':

    """

    Fetch the target dump files that are going to be filtered.

    We can determine which ones are needed by running a directory listing looking for rS-rE-join.dat named files (revision S < revision E).

    Then we extract revision E and fetch the dump file of that revision into E.dump.

    The second case is to extract the commits that are not going to be rewritten.

    
    """
    targetRevisions = []
    
    dumpSequenceFile = open ("dump-file-application-sequence.dat", "w")

    for (directory, subDirs, files) in os.walk("."):

        # part 1 make the to be filtered dump files.
        for fileName in files:

            if fileName.endswith("-join.dat"):

                parts = fileName.split ("-")

                target = parts[0].replace("r", "")
                copyFrom = parts[1]

                targetRevisions.append (int (target))

                dumpFile = "r{0}.dump".format(target)

                # check for the file
                try:
                    
                    test = open(dumpFile, "r")

                    test.close()
                    
                    print "skipping {0} because it already exists.".format(dumpFile)
                    continue
                except:
                    pass
                    # fall through
                     
                command = "{0} dump --incremental {1} -r{2}:{2} > {3}".format(svnrdump_command, "https://svn.kuali.org/repos/student", target, dumpFile)
              
                print command 

                subprocess.call (command, shell=1)

    targetRevisions.sort()

    maximumRevision = "HEAD"

    if len (sys.argv) == 5:
        maximumRevision = sys.argv[4]
    
    currentRevision = 1
    
    startRevision = 1
    endRevision = 1

    rangeList = []

    # part 2: build the ordered list of revision ranges.
    # targetRevisions holds the to be filtered revisions so the other logic adds in the original revision ranges.
    for revision in targetRevisions:

        if startRevision < revision:

            rr = RevisionRange (startRevision, (revision - 1), 0)

            rangeList.append (rr)
            
            dumpSequenceFile.write("r{0}-r{1}.dump\n".format(rr.start, rr.end))

        rangeList.append (RevisionRange (revision, revision, 1))

        dumpSequenceFile.write("r{0}-filtered.dump\n".format(revision))
        
        startRevision = revision + 1

    rr = RevisionRange(startRevision, maximumRevision, 0) 
    rangeList.append (rr)

    dumpSequenceFile.write("r{0}-r{1}.dump\n".format(rr.start, rr.end))

    dumpSequenceFile.close()
    
    # acquire the range
    for revRange in rangeList:
        if revRange.single == 0:
        
            dumpFile = "r{0}-r{1}.dump".format(revRange.start, revRange.end)

            # check for the file
            try:
                
                test = open(dumpFile, "r")

                test.close()
                
                print "skipping {0} because it already exists.".format(dumpFile)
                continue
            except:
                pass
                # fall through
                 
            command = "{0} dump --incremental {1} -r{2}:{3} > {4}".format(svnrdump_command, "https://svn.kuali.org/repos/student", revRange.start, revRange.end, dumpFile)
            
            print command 

            subprocess.call (command, shell=1)

 
    
elif mode == 'APPLY':
    """
    read the revision ordering file and apply the dump files in the correct sequence.
    """

    if len (sys.argv) < 4:
        usage ("Missing empty SVN Repository Url")

    repoUrl=sys.argv[4]

    startFile = None
    started = 1

    recreateRepo = 0
   
    if len (sys.argv) == 6:
        startFile = sys.argv[5]
        started = 0
        
    if len (sys.argv) == 7:
        recreateRepo = 1
    

    if recreateRepo:
        # create the repo
        pass
    
    dumpSequenceFile = open ("dump-file-application-sequence.dat", "r")

    for line in dumpSequenceFile:

        dumpFile = line.strip()

        # skip comments or blank lines
        if len (dumpFile) == 0 or dumpFile[0] == '#':
            continue

        if not started:

            if startFile == dumpFile:
                started = 1

        if not started:
            continue
            # skip over this file

        command = "{0} load {1} < {2}".format(svnrdump_command, repoUrl, dumpFile)
              
        print command 

        subprocess.call (command, shell=1)


    dumpSequenceFile.close()

     
