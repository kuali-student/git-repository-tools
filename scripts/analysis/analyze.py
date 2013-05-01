#!/usr/bin/python
#
# analyze.py
#
# analyze the working copy files to find out which ones have the oldest history

import os;
import sys;
import subprocess;
import traceback;
import re;
import pickle;
import string;

class AnalyzeDiff:

	def __init__(self, sourceDir, targetDir):
		self.sourceDir = sourceDir
		self.targetDir = targetDir

		self.samePaths = {}
		self.diffPaths = {}

	def addPath(self, sourceRev, targetRev, path):
	
		if sourceRev == targetRev:
			
			self.samePaths[path] = [sourceRev, targetRev]
		else:
			# different
			self.diffPaths[path] = [sourceRev, targetRev]


	def __computeMinTargetRevision (self):
		
		minRev = 9999999

		for [sourceRev, targetRev] in self.samePaths.values():

			if targetRev < minRev:
				minRev = targetRev


		for [sourceRev, targetRev] in self.diffPaths.values():

			if targetRev < minRev:
				minRev = targetRev

		return minRev



	def __computeMinSourceRevision (self):

		minRev = 9999999

		for [sourceRev, targetRev] in self.samePaths.values():

			if sourceRev < minRev:
				minRev = sourceRev


		for [sourceRev, targetRev] in self.diffPaths.values():

			if sourceRev < minRev:
				minRev = sourceRev

		return minRev

	def __str__(self):

		lines = []

		lines.append("# source: {0}".format(self.sourceDir))
		lines.append("# target: {0}".format(self.targetDir))

		totalSame = len (self.samePaths)
		totalDiff = len (self.diffPaths)

		totalPaths = totalSame + totalDiff

		lines.append("#\n# Total Paths: {0}".format(totalPaths))
		lines.append("# Total Same: {0}".format(totalSame))
		lines.append("# Total Different: {0}".format (totalDiff))


		lines.append("#\n# Min Source Revision: {0}".format(self.__computeMinSourceRevision()))
		lines.append("#\n# Min Target Revision: {0}".format(self.__computeMinTargetRevision()))
		lines.append("")

		paths = self.samePaths.keys()

		paths.sort()

		for path in paths:

			data = self.samePaths[path]

			ourRev = data[0]
			theirRev = data[1]
			
			lines.append("same:{0}:{1}:{2}".format (path, ourRev, theirRev)				)
		
		paths = self.diffPaths.keys()
		paths.sort()

		for path in paths:

			data = self.diffPaths[path]

			ourRev = data[0]
			theirRev = data[1]

			lines.append("diff:{0}:{1}:{2}".format (path, ourRev, theirRev)				)

		return string.join (lines, "\n")

class Analyze:

	def __init__(self, workingCopyDir):

		lastIndex = len (workingCopyDir) - 1
	
		if workingCopyDir[lastIndex] != '/':
			# normalize the path so it aliways contains a / on the last index
			workingCopyDir += "/"
	
			lastIndex += 1	
		
		minRevCountMapFileName = "{0}-rev-map.dat".format(workingCopyDir[:lastIndex])
		pathToMinRevFileName = "{0}-path-to-rev-map.dat".format(workingCopyDir[:lastIndex])

		self.minRevCountMap = pickle.load (open (minRevCountMapFileName, "rb"))

		self.pathToMinRev = pickle.load (open (pathToMinRevFileName, "rb"))

		self.workingCopyDir = workingCopyDir

	def computeMinRevision(self):
	
		revs = self.minRevCountMap.keys()

		revs.sort()

		return revs[0]

	"""
		otherAnalyze - the object that is being compared to us
	"""
	def computeDiffs(self, otherAnalyze):

		"""
		Find the paths that 
		"""

		
		ourPaths = self.pathToMinRev.keys()
		
		theirPaths = otherAnalyze.pathToMinRev.keys()

		processedPaths = [] 

		diff = AnalyzeDiff (self.workingCopyDir, otherAnalyze.workingCopyDir)

		for i in range (0, len (ourPaths)):

			path = ourPaths[i]

#			print "path: {0}".format(path)

			ourRev = self.pathToMinRev[path]

			if path in theirPaths:

				theirRev = otherAnalyze.pathToMinRev[path]

				diff.addPath (ourRev, theirRev, path)
				
		
		"""
		Then compute the differences in the count's per rev
		"""

		return diff

"""
use svn info workingCopyDir and extract out the revision number
"""
def computeTargetRevision (workingCopyDir):

	command = "svn info .".format (workingCopyDir)

	output = subprocess.check_output (command, shell=True, cwd=workingCopyDir)

	lines = output.split('\n')

	for line in lines:

		if line.startswith("Revision:"):
			parts = line.split(":")
		
			rev = parts[1].strip()

	#		print "rev = {0}".format (rev)
			return int (rev)

	return -1

def computeMinRevision (workingCopyDir, path, targetRevision):

	command = "svn log {0}@{1}".format(path, targetRevision)

	output = subprocess.check_output (command, shell=True, cwd=workingCopyDir)

	minRev = 9999999

	onRevisionLine = 0

	for line in output.split("\n"):

		l = line.strip()

		if len (l) == 0:
			continue
		
		match = re.match ("^-+-*$", l)

		if match != None:
			onRevisionLine = 1
			continue


		match = re.match ("^\wr[1-9]+[0-9]*", l)
	
		if onRevisionLine == 1:

			onRevisionLine = 0

			if match != None:
				print "error: should be revision line but isn't:\n {0}".format(l)	
				continue

#			print "found revision line: {0}".format(l) 
			parts = l.split("|")

			rev = parts[0].strip()

			# strip the leading r
			minRev = int (rev[1:])


	return minRev

def usage(message):
	if message != None:
		print message	
	print "USAGE: <mode:FETCH|REPORT>"
	print "FETCH Options: <svn working copy base directory>"
	print "REPORT Options: <original working copy directory name> <filtered working copy directory name>"
	print "For REPORT the names are used as the prefixes for the data files."
	sys.exit(23)

if len (sys.argv) < 2:
	usage(None)

mode = sys.argv[1]

workingCopyDir = None

# for REPORT
originalWorkingCopyDir = None
filteredWorkingCopyDir = None

# determine the mode

fetchMode = False
reportMode = False


if mode == 'FETCH':
	fetchMode = True

	if len (sys.argv) != 3:
		usage("Missing Svn Working Copy Directory")

	workingCopyDir = sys.argv[2]

elif mode == 'REPORT':
	reportMode = True

	if len (sys.argv) != 4:
		usage ("Missing source and/or filtered directory names")

	originalWorkingCopyDir = sys.argv[2]
	filteredWorkingCopyDir = sys.argv[3]
	
else:
	usage("invalid mode: {0}".format (mode))


	
if fetchMode:

	# compute the files names
	lastIndex = len (workingCopyDir) - 1

	if workingCopyDir[lastIndex] != '/':
		# normalize the path so it aliways contains a / on the last index
		workingCopyDir += "/"

		lastIndex += 1	
	
	minRevCountMapFileName = "{0}-rev-map.dat".format(workingCopyDir[:lastIndex])
	pathToMinRevFileName = "{0}-path-to-rev-map.dat".format(workingCopyDir[:lastIndex])


	# make sure the working copy dir exists

	if os.path.exists(workingCopyDir) == False:
		print "{0} does not exist.".format(workingCopyDir)
		sys.exit(23)

	targetRevision = computeTargetRevision (workingCopyDir)
	
	pathToMinRev = {}
	
	minRevCountMap = {}
	
	errorLog = open ("{0}-error.log".format(sys.argv[0]), "w")
	
	for parentDir, subDirs, files in os.walk (workingCopyDir):
	
		if (parentDir.find(".svn") != -1):
			continue
	
		for f in files:
	
			start = parentDir.find (workingCopyDir)
	
			length = len (workingCopyDir)
		
			if len (parentDir[length:]) > 0:
				absolutePath = "{0}/{1}".format (parentDir[length:], f)
			else:
				absolutePath = f
	
			try:
				rev = computeMinRevision(workingCopyDir, absolutePath, targetRevision)
	
				pathToMinRev[absolutePath] = rev
	
				counter = 0
	
				if rev in minRevCountMap:
	
					counter = minRevCountMap[rev]
	
				counter += 1
				
				minRevCountMap[rev] = counter				
	
			except:
				errorLog.write("error on path = {0}\n".format (absolutePath))
	
				traceback.print_exc(file=errorLog)
	
	errorLog.close()

	"""
	Write out the data in pickle format
	"""	

	
	pickle.dump (minRevCountMap, open (minRevCountMapFileName, "w"))

	pickle.dump (pathToMinRev, open (pathToMinRevFileName, "w"))


elif reportMode:
	
	originalWorkingCopyDir = sys.argv[2]
	filteredWorkingCopyDir = sys.argv[3]


	original = Analyze (originalWorkingCopyDir)
	filtered = Analyze (filteredWorkingCopyDir)

	diffs = original.computeDiffs(filtered)

	print diffs

# eof
