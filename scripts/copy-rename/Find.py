#!/usr/bin/python
"""

NAME: Find.py

PURPOSE: To find the copyfrom details of a given revision/path tuple using git to do the heavy lifting.

The git testsvn remote helper is a native import utility (written in c for speed instead of the existing git-svn.perl) designed for importing larger
repositories.

It is not complete yet for actual use but is very useful for our find history of this specific file purposes.

It can import the entire repository from the root / which is a match for the paths we will be extracting from the svn dump.  Then we can use the
git diff-tree command to find matches for the file of interest in a previous revision.

The diff-tree command expects to operate on tree's so we need to construct new unconnected tree objects (containing a single file) and 
then compare that.

For performance reasons we compare the entire revision to previous revision and log the output.

Then as a seperate step we match the files against the added files to assemble the copy from details.

AUTHOR: Kuali Student Team <ks.collab@kuali.org>

"""

import subprocess
import string
import re
import sys
import fileinput
import os
import shutil
import traceback
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

def extractCommitSha1 (gitDirectory, revision):
    # find the commit
    command = "{0} --git-dir={1} log --grep=\"@{2}\\ \" --pretty --format=\"%H\" origin/master".format(git_command, gitDirectory, revision)
    
    commitSha1 = subprocess.check_output(command, shell=1).strip("\n")

    return commitSha1


    
# extract the sha1 hash of the path at the revision given from the git repository
def extractSha1 (gitDirectory, fileType, revision, path):

    commitSha1 = extractCommitSha1(gitDirectory, revision)

    if fileType == 'file':

        command = "{0} --git-dir={1} ls-tree -r {2} {3}".format(git_command, gitDirectory, commitSha1, path)
    
    elif fileType == 'dir':

        command = "{0} --git-dir={1} ls-tree -d  {2} {3}".format(git_command, gitDirectory, commitSha1, os.path.dirname (path))
    
    output = subprocess.check_output(command, shell=1)
    
    if len (output) == 0:
        return None 

    parts = output.split("\n")

    #print "parts = " + string.join (parts, ", ")

    if len (parts) > 2:
        raise Exception ("multiple matches for {0}@{1}: {2}").format (path, revision, string.join (parts, ", "))

    blobLine = parts[0]

    blobParts = blobLine.split("\t")

    blobPart = blobParts[0]

    sha1Parts = blobPart.split(" ")

    sha1 = sha1Parts[2]

    return sha1


# make a git tree object that only contains the filename given.
def makeTree (gitDirectory, blobSha1, fileName):
    
 
    echo_cmd = "{0} -e \"100644 blob {1}\t{2}\"".format(echo_command, blobSha1, fileName)

    mktree_command = "{0} --git-dir={1} mktree".format(git_command, gitDirectory)

    #print command

    p1 = subprocess.Popen(echo_cmd, shell=True, stdout=subprocess.PIPE)
    p2 = subprocess.Popen(mktree_command, shell=True, stdin=p1.stdout, stdout=subprocess.PIPE)

    p1.stdout.close()

    output = p2.communicate()[0].strip("\n")
    
    return output



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

"""
Find the copies and moves.
"""
def computeDiff(gitDirectory, targetTree, copyFromTree, targetRev, copyFromRev):
    
    joinOutputFile = "r{0}-r{1}-join.dat".format(targetRev, copyFromRev)
    
    print "Started on finding join data into: {0}".format(joinOutputFile)
    
    joinOutput = open (joinOutputFile, "w")
    
    # get the files that stayed the same between the copyFromRev and targetRev
    
    # read in the sha1 -> copy from path details
    copyFromSha1ToPath = {}
    
    command = "git --git-dir={0} ls-tree -rt --full-tree {1}".format(gitDirectory, copyFromTree)
    
    #  print command
        
    output = subprocess.check_output(command, shell=True)
    
    lines = output.split("\n")
    
    for line in lines:
        
        afterModeSpaceIndex = line.find(" ")
        
        startOfTypeIndex = afterModeSpaceIndex + 1
        
        afterTypeSpaceIndex = line[startOfTypeIndex:].find(" ")
        
        startOfShaIndex = startOfTypeIndex + afterTypeSpaceIndex + 1
        
        afterShaSpaceIndex = line[startOfShaIndex:].find("\t")
        
        startOfPathIndex = startOfShaIndex + afterShaSpaceIndex + 1
        
        copyFromSha1 = line[startOfShaIndex:startOfPathIndex]
        copyFromPath = line[startOfPathIndex:]
        
        copyFromSha1ToPath[copyFromSha1] = copyFromPath
        
        
    command = "git --git-dir={0} ls-tree -rt --full-tree {1}".format(gitDirectory, targetTree)
    
    #  print command
        
    output = subprocess.check_output(command, shell=True)
    
    lines = output.split("\n")
    
    for line in lines:
        
        if len(line) == 0:
            continue
        
        afterModeSpaceIndex = line.find(" ")
        
        startOfTypeIndex = afterModeSpaceIndex + 1
        
        afterTypeSpaceIndex = line[startOfTypeIndex:].find(" ")
        
        startOfShaIndex = startOfTypeIndex + afterTypeSpaceIndex + 1
        
        afterShaSpaceIndex = line[startOfShaIndex:].find("\t")
        
        startOfPathIndex = startOfShaIndex + afterShaSpaceIndex + 1
        
        targetSha1 = line[startOfShaIndex:startOfPathIndex]
        targetPath = line[startOfPathIndex:]
        
        if targetSha1 in copyFromSha1ToPath.keys():
            
            copyFromPath = copyFromSha1ToPath[targetSha1]
            
            # extract the md5
            command = "git --git-dir={0} cat-file -p {1}".format(gitDirectory, targetSha1)
    
            #print command
        
            p1 = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE)
            p2 = subprocess.Popen(md5sum_command, shell=True, stdin=p1.stdout, stdout=subprocess.PIPE)
        
            p1.stdout.close()
        
            output = p2.communicate()[0]

            targetMd5 = output.split(" ")[0]
            
            copyFromPathPrefix = copyFromRevToPrefix[copyFromRev]
            
            joinOutput.write ("#{0}\n{1}\n{2}||{3}\n{4}||{5}\n{6}||{7}\n".format("Unchanged", copyFromPathPrefix, targetRev, targetPath, copyFromRev, copyFromPath, targetSha1, targetMd5))
            
    
    # get the copied and changes between the revisions
    command = "git --git-dir={0} diff-tree --find-copies-harder --diff-filter=C,R,M -r {1} {2} | sort -rk5 ".format(gitDirectory, targetTree, copyFromTree)
    
    print command
    
    outputFile = "r{0}-r{1}-diff.out".format(targetRev, copyFromRev)
    
    output = open (outputFile, "w")
    
    subprocess.call(command, shell=True, stdout=output)
    
    output.close()
    
    
    
    # now parse that file to write into a smaller format.
    for line in fileinput.input(outputFile):
        strippedLine = line.strip()
        if len (strippedLine) == 0 or strippedLine[0] == '#':
            continue  # skip empty lines and comments  
        
        parts = strippedLine.split (" ")
        
        srcMode = parts[0]
        dstMode = parts[1]
        
        srcSha1 = parts[2]
        dstSha1 = parts[3]
        
        remainder = parts[4]
        
        tabParts = remainder.split("\t")
        
        status = tabParts[0]
        
        srcPath = tabParts[1]
        
        # need to convert the source path back into the original path

        command = "git --git-dir={0} cat-file -p {1}".format(gitDirectory, dstSha1)
    
      #  print command
        
        p1 = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE)
        p2 = subprocess.Popen(md5sum_command, shell=True, stdin=p1.stdout, stdout=subprocess.PIPE)
        
        p1.stdout.close()
        
        output = p2.communicate()[0]
        
        md5Parts = output.split(" ")
        
        dstMd5 = md5Parts[0]
        
        if len (tabParts) == 3:
            # copy
            dstPath = tabParts[2]
            
         #   if verifySameFile (srcPath, dstPath) == False:
         #       continue
            
            joinOutput.write ("#{0}\n{1}||{2}\n{3}||{4}\n{5}||{6}\n".format(status, targetRev, srcPath, copyFromRev, dstPath, dstSha1, dstMd5))
            
        else:
            # in place modify
             joinOutput.write ("#{0}\n{1}||{2}\n{3}||{4}\n{5}||{6}\n".format(status, targetRev, srcPath, copyFromRev, srcPath, dstSha1, dstMd5))
        
       
        
        
    joinOutput.close()


def fetchPath(gitDirectory, path, rev):
    
    # check if the tag already exists
    tagName = "r{0}".format(rev)
    
    command = "{0} --git-dir={1} rev-parse {2}".format(git_command, gitDirectory, tagName)
    
    try:
        
        output = subprocess.check_output(command, shell=1).strip("\n")
    
        if output != tagName:
            print "{0} exists skipping export.".format(tagName)
            return

    except:
        
        # this is fine it means the tag does not exist
        print "tag {0} does not exist, fetching.".format(tagName)

    # compute the top level path
    indexAfterBaseUrl = len("http://svn.kuali.org/repos/student/")
    
    topLevelPath = path[indexAfterBaseUrl:]
    
    # find the top level working copy
    workingCopyDir = os.path.dirname(gitDirectory)
    
    print workingCopyDir
 
    # checkout something with not a lot of data
    command = "{0} checkout master".format(git_command)
   
    print command 
    executeCommandInWorkingDirectory(command, workingCopyDir)
    
    # delete the export branch if it exists
    command = "{0} branch -D svn-export".format(git_command)
    
    executeCommandInWorkingDirectory(command, workingCopyDir)
    
    # create the new empty revision to hold the export
    command = "{0} checkout --orphan svn-export".format(git_command)
    
    executeCommandInWorkingDirectory(command, workingCopyDir)
    
    # remove any files (make sure we always specify the workingCopyDir so that this works.
    command = "{0} rm -rf *".format(git_command)
    
    executeCommandInWorkingDirectory(command, workingCopyDir)
    
    # remove any other files from the working copy directory

    for (directory, subDirs, files) in os.walk(workingCopyDir):
    
        topLevelDirs = subDirs
    
        topLevelDirs.remove('.git')
        
        for dirToDelete in topLevelDirs:
            
            fullDeletePath = directory + file_seperator + dirToDelete
            
            shutil.rmtree(fullDeletePath)
        
        break;
        
    command = "{0} export {1}@{2} -r {3} {4}".format(svn_command, path, rev, rev, topLevelPath)
    
    executeCommandInWorkingDirectory(command, workingCopyDir)

    # add place holder files into any empty directories
    command = "find . -type d -empty | grep -v .git"

    output = None

    try:
        output = subprocess.check_output(command, shell=True, cwd=workingCopyDir)
    except:
        print "No EmptyDirectories for {0}. Skipping placeholder files.".format(tagName)
        output = ""
        
    emptyDirs = output.split("\n")

    for ed in emptyDirs:

        if len (ed) == 0:
            break;
        
        executeCommandInWorkingDirectory ("touch {0}/placeholder.txt".format(ed), workingCopyDir)
    
    command = "{0} add *".format(git_command)
    
    executeCommandInWorkingDirectory(command, workingCopyDir)
    
    command = "{0} commit -m\"{1} {2}\"".format(git_command, path, tagName)
    
    executeCommandInWorkingDirectory(command, workingCopyDir)
    
    # force tag (this may result in the tag moving) 
    command = "{0} tag -f {1}".format(git_command, tagName)
    
    executeCommandInWorkingDirectory(command, workingCopyDir)

class Add:

    def __init__(self, code, kind, path):
        self.code = code
        self.kind = kind
        self.path = path

class RevisionAddData:

    def __init__(self, revision):
        self.revision = revision

        self.paths = []

        self.codeToAddMap = {}

        self.pathToAddMap = {}


    def setCommitSha1(self,  commitSha1):
        self.commitSha1 = commitSha1
    
    def addNormalPath (self, nodeKind, path):
       
        code = len (self.paths)

        add = Add (code, nodeKind, path)
 
        self.paths.append (add)

        self.codeToAddMap[code] = add

        self.pathToAddMap[path] = add

    def compare(self, gitDirectory):

        """
        extract the copyfrom details
        """

   
        compareRevision = self.revision - 1

        outputFileName = "compare-r{0}-to-r{1}.dat".format(compareRevision, self.revision)


        # acquire the take tree data

        treeContent = tempfile.NamedTemporaryFile(mode="w", delete=False)
        treeContentFileName = treeContent.name

        addedAtLeastOneFile = False

        for add in self.paths:

            if add.kind == 'dir':
                continue
        
            dirName = os.path.dirname (add.path)
            
            fileName = os.path.basename (add.path)
            
            command = "{0} --git-dir={1} ls-tree r{2}:{3} | grep {4} ".format(git_command, gitDirectory, self.revision, dirName, fileName)

            result = subprocess.check_output (command, shell=1)

            parts = result.strip().split("\t")

            withoutPath = parts[0]

            
            treeContent.write ("{0}\tP-{1}\n".format(withoutPath, add.code)) 

            addedAtLeastOneFile = True

       
        treeContent.close()


        if addedAtLeastOneFile == False:
            print "skipping r{0} because there are no files of interest".format(self.revision)
            return    

        treeContent = open (treeContentFileName, "r")

        command = "{0} --git-dir={1} mktree".format (git_command, gitDirectory)

        tree = subprocess.check_output (command, shell=1, stdin=treeContent).strip()

        treeContent.close()

        print "tree = {0}".format (tree)

        compareTrees(gitDirectory, "r{0}".format(compareRevision), tree, outputFileName)

    def process(self, gitDirectory):

        """

        We open up the compare date and match the files and direct
        """


        compareRevision = self.revision - 1
        
        inputFileName = "compare-r{0}-to-r{1}.dat".format(compareRevision, self.revision)

        # this is the naming expected by the filtering program (the reverse of this program)
        outputFileName = "r{0}-r{1}-join.dat".format (self.revision, compareRevision)

        outputFile = open (outputFileName, "w")

        foundAtLeastOneCopyFrom = False

        for line in fileinput.input (inputFileName):

            # Read the Raw output format section of the git diff-tree man page
            # ref: https://www.kernel.org/pub/software/scm/git/docs/git-diff-tree.html

            print line

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

            if targetPath in self.pathToAddMap.keys():

                foundAtLeastOneCopyFrom = True

                ad = self.pathToAddMap[targetPath]
        
                outputFile.write ("#{0}\n{1}\n{2}||{3}\n{4}||{5}\n{6}||{7}\n".format(status, "PLACEHOLDER", self.revision, ad.path, compareRevision, copyFromPath, copyFromSha1, "MISSING-MD5"))
                

        outputFile.close()

        if foundAtLeastOneCopyFrom == False:

            os.remove (outputFileName)
                
      
def usage(message):
        if message != None:
            print message    
        print "USAGE: {0} <mode:PREPARE-REPO or COMPARE or PROCESS or FETCH-DUMPS> <path to git repository> <added file input data>".format (sys.argv[0])
        print "PREPARE-REPO: will tag each commit so that for example commit r1 is tagged as r1"
        print "COMPARE [startFromRevision]: will extract the comparison diff data based on the add file data"
        print "PROCESS [specificRevision]: will process the diff data accumulated in the COMPARE phase and use it to create SvnDumpFilter rewrite compatible join.dat files"
        print "FETCH [specificRevision]: will download the version 3 --incremental dump for the revisions that have data to be rewritten as determined in the PROCESS STEP."
        """

        The PROCESS-SINGLE option is for debugging/development purpopses.

        The FETCH-SINGLE option is for debugginh/development purposes
        """
        sys.exit (-1)

if len (sys.argv) < 4:
    usage(None)

mode = sys.argv[1]

if mode != "COMPARE" and mode !=  "PROCESS" and mode != "FETCH" and mode != 'PREPARE-REPO':
    usage ("invalid mode: {0}".format(mode))

specificRevision = None

if len (sys.argv) == 6:
    specificRevision = int (sys.argv[2])
    gitDirectory = sys.argv[3]
    addData = sys.argv[4]
else:
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

    for ad in revisionAddData:
        if specificRevision != None:

            if ad.revision == specificRevision:

                ad.compare(gitDirectory)
                break;
        else:

            print "Comparing r{0}".format (ad.revision)

            ad.compare(gitDirectory)

elif mode == 'PROCESS':

    for ad in revisionAddData:
        if specificRevision != None:

            if ad.revision == specificRevision:

                ad.process(gitDirectory)
                break;
        else:
            ad.process(gitDirectory)

elif mode == 'FETCH':

    for (directory, subDirs, files) in os.walk("."):

        for fileName in files:

            if fileName.endswith("-join.dat"):

                parts = fileName.split ("-")

                target = parts[0]
                copyFrom = parts[1]

                
