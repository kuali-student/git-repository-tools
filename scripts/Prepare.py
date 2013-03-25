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

# Functions 

def fetchPath(path, rev):
    pass


def computeDiff(gitDirectory, targetRev, copyFromRev):
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
        
        
        


# Main program
if len(sys.argv) != 3:
    print 'USAGE: %s <git directory> <revision data file>' % sys.argv[0]
    sys.exit (-1)
    
gitDirectory = sys.argv[1]
 
revisionData = sys.argv[2]





for line in fileinput.input(revisionData):
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
        
    
    
    
    
    



