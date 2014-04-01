'''
Created on Mar 11, 2013

@author: Kuali Student Team

The purpose of this file is to generate a shell script that can be run within the git directory.

It will populate it with the different paths specified by the data file.

'''

import subprocess
import string
import re
import sys
import fileinput
import os
import shutil

# this needs to be changed to md5sum on linux
# windows needs an absolute path to work.
# this one assumes you have git for windows installed.
md5sum_command="/usr/bin/md5sum"

git_command="/usr/bin/git"

svn_command="/usr/bin/svn"

svnrdump_command="/usr/bin/svnrdump"

# TODO change to using os.path.join instead of this field
file_seperator="/"

svn_repository_url = "http://svn.kuali.org/repos/student"

# Functions 

copyFromRevToPrefix = {}

def executeCommandInWorkingDirectory(command, workingDirectory):
   
    print "command = ", command
    print "working Directory = ", workingDirectory 
    p = subprocess.Popen(command, cwd=workingDirectory,shell=1)
    
    p.wait()
    
    return p

"""
 Get the incremental dump file on the full repository path.
 
 # svnrdump dump http://svn.kuali.org/repos/student -r XYZ --incremental
 
 This should be used on the target revision (the one that will be rewritten).
 
"""
def fetchIncrementalDumpfile (path, rev):
    
    dumpFile = open ("r{0}.dump".format(rev), "w")
    
    # svnrdump dump http://svn.kuali.org/repos/student -r XYZ --incremental
    command = "{0} dump {1} -r {2} --incremental".format(svnrdump_command, svn_repository_url, rev)
    
    print command
    
    subprocess.call(command, stdout=dumpFile, shell=1)
    
    dumpFile.close()
    
"""
 Get the complete dump file on a partial repository path.
 
 # svnrdump dump http://svn.kuali.org/repos/student/some/narrower/path -r XYZ 
 
 This should be used on the copyfrom revision (the one that will be provide the md5sum data to be used in the rewrite of the target revision).
 
"""
def fetchDumpFile (path, rev):
    
    dumpFile = open ("r{0}.dump".format(rev), "w")
    
    command = "{0} dump {1} -r {2} ".format(svnrdump_command, path, rev)
    
    print command
    
    subprocess.call(command, stdout=dumpFile, shell=1)
    
    dumpFile.close()
    
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
    
def verifySameFile(srcPath, dstPath):

    srcParts = srcPath.split("/")
    dstParts = dstPath.split ("/")
    
    srcName = srcParts[len(srcParts)-1]
    dstName = dstParts[len(dstParts)-1]
    
    if srcName == dstName:
        return True
    else:
        return False


"""
Find the copies and moves.
"""
def computeDiff(gitDirectory, targetRev, copyFromRev, filePathPrefix):
    
    joinOutputFile = "r{0}-r{1}-join.dat".format(targetRev, copyFromRev)
    
    print "Started on finding join data into: {0}".format(joinOutputFile)
   
    if os.path.exists(joinOutputFile):

        if filePathPrefix != None:
            joinOutputFile = "{0}-{1}".format (filePathPrefix, joinOutputFile)
        else:
            print "ERROR: {0} already exists.  You need to specify a prefix"
            sys.exit (-1)
 
    joinOutput = open (joinOutputFile, "w")
    
    # get the files that stayed the same between the copyFromRev and targetRev
    
    # read in the sha1 -> copy from path details
    copyFromSha1ToPath = {}
    
    command = "git --git-dir={0} ls-tree -rt --full-tree r{1}".format(gitDirectory, copyFromRev)
    
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
        
        
    command = "git --git-dir={0} ls-tree -rt --full-tree r{1}".format(gitDirectory, targetRev)
    
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
    command = "git --git-dir={0} diff-tree --find-copies-harder --diff-filter=C,R,M -r r{1} r{2} | sort -rk5 ".format(gitDirectory, targetRev, copyFromRev)
    
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

# Main program
if len(sys.argv) != 3:
    print 'USAGE: %s <git directory> <revision data file>' % sys.argv[0]
    sys.exit (-1)
    
gitDirectory = sys.argv[1]
 
revisionData = sys.argv[2]



for line in open(revisionData):
    strippedLine = line.strip()
    if len (strippedLine) == 0 or strippedLine[0] == '#':
        continue  # skip empty lines and comments  
    
    parts = strippedLine.split (";")
    
    type = parts[0]
    
    if type == 'FETCH-TARGET':
        
        path = parts[1]
    
        rev = int (parts[2])
        
        fetchPath (gitDirectory, path, rev)
        
        fetchIncrementalDumpfile(path, rev)
        
        
    elif type == 'FETCH-COPY-FROM':
        path = parts[1]
    
        rev = int (parts[2])
        
        fetchPath (gitDirectory, path, rev)
        fetchDumpFile (path, rev)
        
        # compute the top level path
        indexAfterBaseUrl = len("http://svn.kuali.org/repos/student/")
    
        topLevelPath = path[indexAfterBaseUrl:]
        
        copyFromRevToPrefix[rev] = topLevelPath
        
    elif type == 'DIFF':
        targetRev = int (parts[1])
        copyFromRev = int (parts[2])

        pathPrefix = None

        if len (parts) == 4:
            pathPrefix = parts[3]

        computeDiff (gitDirectory, targetRev, copyFromRev, pathPrefix)
    else:
        print 'USAGE: %s <git directory> <revision data file>' % sys.argv[0]
        sys.exit (-1)
        
    
    
    
    
    



