'''
Created on Mar 11, 2013

@author: Kuali Student Team

The purpose of this file is to generate a shell script that can be run within the git directory.

It will populate it with the different paths specified by the data file.

'''

import subprocess
import string
import sys
import fileinput
import os

# this needs to be changed to md5sum on linux
# windows needs an absolute path to work.
# this one assumes you have git for windows installed.
md5sum_command="C:\\Program Files (x86)\\Git\\bin\\md5sum.exe"

# Functions 

def fetchPath(path, rev):
    pass


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
def computeDiff(gitDirectory, targetRev, copyFromRev):
    
    joinOutputFile = "r{0}-r{1}-join.dat".format(targetRev, copyFromRev)
    
    joinOutput = open (joinOutputFile, "w")
    
    # get the files that stayed the same between the copyFromRev and targetRev
    
    # read in the sha1 -> copy from path details
    copyFromSha1ToPath = {}
    
    command = "git --git-dir={0} ls-tree -r --full-tree r{1}".format(gitDirectory, copyFromRev)
    
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
        
        
    command = "git --git-dir={0} ls-tree -r --full-tree r{1}".format(gitDirectory, targetRev)
    
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
            
            joinOutput.write ("#{0}\n{1}||{2}\n{3}||{4}\n{5}||{6}\n".format("Unchanged", targetRev, targetPath, copyFromRev, copyFromPath, targetSha1, targetMd5))
            
            
    
    # get the copied and changes between the revisions
    command = "git --git-dir={0} diff-tree --find-copies-harder --diff-filter=C,R,M -r r{1} r{2}".format(gitDirectory, targetRev, copyFromRev)
    
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
    
    if type == 'FETCH':
        path = parts[1]
    
        rev = int (parts[2])
        
        fetchPath (path, rev)
        
    elif type == 'DIFF':
        targetRev = parts[1]
        copyFromRev = parts[2]
        computeDiff (gitDirectory, targetRev, copyFromRev)
    else:
        print 'USAGE: %s <git directory> <revision data file>' % sys.argv[0]
        sys.exit (-1)
        
    
    
    
    
    



