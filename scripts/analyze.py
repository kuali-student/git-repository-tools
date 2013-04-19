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

class AnalyzeDifference:

	def __init__(self):
		pass

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

	def computeDifference(self, otherAnalyze):

		"""
		First compute the differences in the count's per rev
		"""

		pass	
		

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
	filtered = Analyze (originalWorkingCopyDir)

	diffs = original.computeDifferences(filtered)


# eof
