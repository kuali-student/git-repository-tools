#!/usr/bin/python
"""

validate-mergeinfo.py

The Java based Svn Merge Dectector finds all merges that exist based on reading a given svn repository dump file.

Then this program is used to check if svn:mergeinfo properties exist on the merged paths.

It will also emit the svn:mergeinfo we should use to correct the problem.  With the 'pre-revprop-change' svn hook enabled we can insert this into older revisions.

inserting does not support eliding (removing and consolidating subtree svn:mergeinfo data) so steps have to be taken to generate the correct svn:mergeinfo data.

We need to checkout each target path at the revision specified and then apply an 'svn merge --record-only <copyfrom path>@copy-from-rev>

Then read in and store the svn:mergeinfo contents (and cache it)

We then need a report to quantify the kinds of each case:
1. svn:mergeinfo is missing and needs to be created from scratch
2. svn:mergeinfo in incomplete and needs to be updated
3. svn:mergeinfo contains the copyfrom path and revision already.

The svn:mergeinfo properties would be modified for cases 1 and 2 but not 3.
 
"""
import os
import fileinput
import sys
import string
import subprocess
import pickle

svn_command="svn"

class MergedPath:

	def __init__(self, revision, branch, path, copyFromRevision, copyFromBranch, copyFromPath):

		self.revision = revision
		self.branchName = branch
		self.pathName = path
		
		self.copyFromRevision = copyFromRevision
		self.copyFromBranch = copyFromBranch
		self.copyFromPath = copyFromPath

		self.mergedPaths = []

		self.fetchedSvnMergeInfo = None

		try:

			os.makedirs("fetched-merge-info")

			branchName = self.branchName.replace("/", "=")

			cache = "fetched-merge-info/r{0}-{1}-svn:mergeinfo.cache".format(self.revision, branchName)
			self.fetchedSvnMergeInfo = pickle.load (open (cache, "rb"))
		except:

			pass

		self.createdSvnMergeInfo = None

		try:

			os.makedirs("created-merge-info")

			branchName = self.branchName.replace("/", "=")
			
			cache = "created-merge-info/r{0}-{1}-svn:mergeinfo.cache".format(self.revision, branchName)
			self.createdSvnMergeInfo = pickle.load (open (cache, "rb"))
		except:

			pass


	def __str__(self):

		return "{0}:{1}:{2}\n{3}:{4}:{5}".format(self.revision, self.branchName, self.pathName, self.copyFromRevision, self.copyFromBranch, self.copyFromPath)

	def addMergedPath (self, mergedPath):

		self.mergedPaths.append (mergedPath)


	def getFixMergeInfoCommand(self, repoPath):

		# svn merge --record-only 
		# this would elide and clean thigs up but is not what we can do after the revision is locked into the past.
		# instead what we want to do is to svn propset --revprop -r targetRevision svn:mergeinfo ''
		# we may need to acqure the ranges seperately.
		# if we checkout the target path and then run a merge --record-only it will tell us the svn:mergeinfo that we should use.
		command = "{0} propset svn:mergeinfo --revprop -r {1} '{2}' {3}@{4}".format (svn_command, self.revision, self.fetchedSvnMergeInfo, repoPath, self.revision)

		return command

	def fetchSvnMergeInfo(self, baseRepoUrl):

		targetRepoAndPath = "{0}/{1}".format (baseRepoUrl, self.branchName)


		if self.fetchedSvnMergeInfo == None:
 
			command = "{0} propget svn:mergeinfo {1}@{2}".format (svn_command, targetRepoAndPath, self.revision)
		
	#	print command

			output = subprocess.check_output (command, shell=1)

			branchName = self.branchName.replace("/", "=")
			
			cache = "fetched-merge-info/r{0}-{1}-svn:mergeinfo.cache".format(self.revision, branchName)
		
			self.fetchedSvnMergeInfo = output.strip("\n")

			pickle.dump (self.fetchedSvnMergeInfo, open (cache, "w"))

		
		if len (self.fetchedSvnMergeInfo) == 0:
			return False
#			return  self.getFixMergeInfoCommand(targetRepoAndPath)
		else:
			# merge info exists

			return True


def findExisting (branchKey, mergePaths, outputFile):

	mp = mergePaths[0]

	if mp.fetchSvnMergeInfo(repoUrl) == True:
		# found the merge info
		outputFile.write ("found:{0}\n".format (branchKey))
	else:
		outputFile.write ("missing:{0}\n".format (branchKey))

def createMergeData (branchKey, mergePaths, outputFile):
	pass

def injectMergeData (branchKey, mergePaths, outputFile):
	pass

if len (sys.argv) != 4:
	print "USAGE: {0} <mode:find-existing,create-merge-data,inject-merge-data <merge data file> <repo base url>".format(sys.argv[0])
	sys.exit(-1)

mode = sys.argv[1]


if mode not in ["find-existing", "create-merge-data", "inject-merge-data"]:
	print "{0} is not a valid mode".format(mode)
	print "USAGE: {0} <mode:find-existing,create-merge-data,inject-merge-data <merge data file> <repo base url>".format(sys.argv[0])
	sys.exit(-1)

outputFile = open ("{0}.log".format(mode), "w")	

dataFileName = sys.argv[2]

repoUrl = sys.argv[3]

branchToPaths = {}

for line in fileinput.input (dataFileName):

	tl = line.strip()

	if len (tl) == 0 or tl[0] == '#':
		continue

	parts = string.split (tl, ":")

	n_parts = len (parts)

	if n_parts != 6:
		print "{0} != 6 parts for line = {1}".format(n_parts, tl)
		continue
	
	copyFromRevision = int (parts[0])
	
	targetRevision = int (parts[1])

	copyFromBranchName = parts[2]
	
	targetBranchName = parts[3]

	copyFromPathName = parts[4]

	targetPathName = parts[5]


	key = "{0}:{1}".format (targetRevision, targetBranchName)
	
	paths = None

	try:
		paths = branchToPaths[key]
	except:
#		print "no paths exist for key: {0}".format(key)
		paths = []
		
		branchToPaths[key] = paths

	mp = MergedPath(targetRevision, targetBranchName, targetPathName, copyFromRevision, copyFromBranchName, copyFromPathName)

	paths.append (mp)

branches = branchToPaths.keys()

branches.sort()

for b in branches:


	paths = branchToPaths[b]
	
	if mode == 'find-existing':

		findExisting(b, paths, outputFile)

	elif mode == 'create-merge-data':
		createMergeData (b, paths, outputFile)

	elif mode == 'inject-merge-data':
		injectMergeData(b, paths, outputFile)
	else:
		print "ERROR unexpected mode {0}".format(mode)



outputFile.close()
