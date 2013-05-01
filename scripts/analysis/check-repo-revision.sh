#!/bin/bash
#
#
# check-repo-revision.sh
#
# Will svn info $DIR for each working copy directory.
# the number should be the same between the filtered and original working copy for the analysis to have accurate information.
#  
ls -1 wc-analysis-* | grep : | cut -d: -f 1 | while read D; do svn info $D | egrep "(URL|Revision)"; done

