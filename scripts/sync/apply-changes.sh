#!/bin/bash
#
# fetch the changes between the rewriten and the source
REV_FILE=$1

if test -z "$REV_FILE"
then
	echo "USAGE: $0 <REV update file to apply>"
	echo "update file will be applied to the /home/mike/subversion/ksenroll-3907/staged-repo"
	exit 1
fi

./sync-ks-to-staged.sh APPLY file:///home/mike/subversion/ksenroll-3907/staged-repo "$REV_FILE"
