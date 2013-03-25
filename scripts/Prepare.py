'''
Created on Mar 11, 2013

@author: Kuali Student Team
'''

import subprocess
import string
import sys
import fileinput

if len(sys.argv) != 3:
	print 'USAGE: %s <git directory> <revision data file>' % sys.argv[0]
	sys.exit (-1)
    
gitDirectory = sys.argv[1]
 
revisionData = sys.argv[2]



for line in fileinput.input(revisionData):
    strippedLine = line.stripspaces()
    if len (strippedLine) == 0 or strippedLine[0] == '#':
        continue  # skip empty lines and comments  


copyFromDataFile = open ("copyFromDetails.dat")

subprocess.call("git")
