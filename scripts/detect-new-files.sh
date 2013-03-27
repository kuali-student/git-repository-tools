#!/bin/bash


WORKING_COPY=$1
CURRENT_REV=$2

if [ -z "$WORKING_COPY" ]; then

	echo "USAGE: <svn working copy directory> <revision of working copy>"
	exit 1;
fi

if [ -z "$CURRENT_REV" ]; then

	echo "USAGE: <svn working copy directory> <revision of working copy>"
	exit 1;
fi


cd $WORKING_COPY;
pwd
echo "CURRENT_REV=$CURRENT_REV"

find . -type f | grep -v .svn | while read F; do

	REV=`svn log $F | grep ^r | cut -d' ' -f 1 | tail -n 1`

	#echo "REV=$REV"
	if [ "$REV" == "r$CURRENT_REV"  ]; then
		echo "$F CREATED AT CURRENT REV ($CURRENT_REV)"
# TODO put this on a switch
#	else
#		echo "$F CREATED AT $REV"
	fi
done
