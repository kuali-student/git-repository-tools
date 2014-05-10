#!/bin/bash
REPO=/home/mike/subversion/ks
REV=$1

if test -z $REV
then
	echo "USAGE: <start rev:end rev>"
	exit 1
fi

svnadmin dump --incremental -r $REV $REPO | bzip2 > ~/ks-r$REV.dump.bz2
