#!/bin/bash
#
# 

# checkout the module if it does not exist.
checkOut () {

	MODULE=$1

	REPO=$2

	# original or filtered
	TYPE=$3

	DIR_NAME="wc-analysis-$TYPE-$MODULE-trunk"


	if test ! -f $DIR_NAME
	then
		
		echo "svn co $REPO/enrollment/$MODULE/trunk $DIR_NAME"
		svn co $REPO/enrollment/$MODULE/trunk $DIR_NAME
		echo "Checked out $TYPE $MODULE to $DIR_NAME"
	else
		echo "Skipping $DIR_NAME as it already exists."	
	fi
}

ORIGINAL_REPO="http://svn.kuali.org/repos/student"
FILTERED_REPO="http://svn.kuali.org/repos/ks-test"

checkOut "ks-api" "$FILTERED_REPO" "filtered"
checkOut "ks-core" "$FILTERED_REPO" "filtered"
checkOut "ks-lum" "$FILTERED_REPO" "filtered"
checkOut "ks-enroll" "$FILTERED_REPO" "filtered"
checkOut "ks-deployments" "$FILTERED_REPO" "filtered"

checkOut "ks-api" "$ORIGINAL_REPO" "original"
checkOut "ks-core" "$ORIGINAL_REPO" "original"
checkOut "ks-lum" "$ORIGINAL_REPO" "original"
checkOut "ks-enroll" "$ORIGINAL_REPO" "original"
checkOut "ks-deployments" "$ORIGINAL_REPO" "original"

