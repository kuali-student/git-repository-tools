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
then compare that

AUTHOR: Kuali Student Team <ks.collab@kuali.org>

"""

import subprocess
import string
import re
import sys
import fileinput
import os
import shutil

