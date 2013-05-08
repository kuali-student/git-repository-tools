#!/usr/bin/python

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

		self.svnMergeInfo = None

		try:
			cache = "r{0}-svn:mergeinfo.cache".format(self.revision)
			self.svnMergeInfo = pickle.load (open (cache, "rb"))
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
		command = "{0} propset svn:mergeinfo --revprop -r {1} '{2}' {3}@{4}".format (svn_command, self.revision, self.svnMergeInfo, repoPath, self.revision)

		return command

	def verifyBranchMergeInfo(self, baseRepoUrl):

		targetRepoAndPath = "{0}/{1}".format (baseRepoUrl, self.branchName)


		if self.svnMergeInfo == None:
 
			command = "{0} propget svn:mergeinfo {1}@{2}".format (svn_command, targetRepoAndPath, self.revision)
		
	#	print command

			output = subprocess.check_output (command, shell=1)

			cache = "r{0}-svn:mergeinfo.cache".format(self.revision)
		
			self.svnMergeInfo = output.strip("\n")

			pickle.dump (self.svnMergeInfo, open (cache, "w"))

		
		if len (self.svnMergeInfo) == 0:
			return ""
#			return  self.getFixMergeInfoCommand(targetRepoAndPath)
		else:
			# merge info exists

			return self.svnMergeInfo

	
if len (sys.argv) != 3:
	print "USAGE: {0} <merge data file> <repo base url>".format(sys.argv[0])
	sys.exit(-1)


dataFileName = sys.argv[1]

repoUrl = sys.argv[2]

branchToPaths = {}

for line in fileinput.input (dataFileName):

	tl = line.strip()

	if len (tl) == 0 or tl[0] == '#':
		continue

	parts = string.split (tl, ":")
	
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

#	for mp in paths:
#		print mp


	mp = paths[0]


	output = mp.verifyBranchMergeInfo(repoUrl)

	if len(output) > 0:
		print "BEGIN"

		print b
		print mp
		print output
		print "END"
