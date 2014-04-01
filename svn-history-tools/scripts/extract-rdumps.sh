#!/bin/bash

usage() {

	echo "USAGE: <mode: FETCH, LINK or APPLY>"
	echo "FETCH [alternate repository url]"
	echo "LINK <directory containing filtered dump files>" 
	echo "APPLY <svn repository path> [directory prefix for dump files (no trailing /)]"
	exit 1;
}

linkFiltered () {
	REV=$1
	FILTERED_SRC_DIR=$2

	if [ -z "$REV" ]; then 

		echo "Missing revision to copy"
		usage
	fi

	if [ -z "$FILTERED_SRC_DIR" ]; then 

		echo "Missing copy src dir"
		usage
	fi

	echo "ln -s $FILTERED_SRC_DIR/r$REV-filtered.dump ./r$REV-r$REV.dump"
	ln -s $FILTERED_SRC_DIR/r$REV-filtered.dump ./r$REV-r$REV.dump
}

extract() {
	# Allows you to say specify a mirror
	ROOT_URL=$1
	FROM=$2
	TO=$3

	if [ -z "$ROOT_URL" ]; then
		echo "Missing Root Url"
		usage
	fi

	if [ -z "$FROM" ]; then
		echo "Missing From"
		usage
	fi

	if [ -z "$TO" ]; then
		echo "Missing To"
		usage
	fi

	OUT="r$FROM-r$TO.dump"
	ERR="r$FROM-r$TO.log"

	if [ ! -f $OUT ]; then
		# does not exist so dump it 
		echo "Creating Dump File: '$OUT'"
		echo "Writing Errors File: '$ERR'"
		svnrdump dump $ROOT_URL --incremental -r $FROM:$TO > $OUT 2> $ERR
	else
		echo "$OUT exists skipping over it."
	fi

}

load () {


	# Allows you to say specify a mirror
	ROOT_URL=$1
	DATA=$2

	if [ -z "$ROOT_URL" ]; then
		echo "Missing Root Url"
		usage
	fi

	if [ -z "$DATA" ]; then
		echo "Missing Data file name."
		usage
	fi

	echo "load '$ROOT_URL' '$DATA'"

	if [ ! -f $DATA ]; then
		echo "Missing data file = $DATA"
	else
		echo "svnrdump load $ROOT_URL < $DATA"
		svnrdump load $ROOT_URL < $DATA
	fi
}



MODE=$1

if [ -z "$MODE" ]; then

	usage
fi


if [ "$MODE" == "FETCH" ]; then

	LOCAL_REPO=$2

	REMOTE_REPO="http://svn.kuali.org/repos/student"

	REPO="NONE"

	if [ -z "$LOCAL_REPO" ]; then
		REPO=$REMOTE_REPO;
	else
		REPO=$LOCAL_REPO;

	fi

	echo "REPO = $REPO"

	extract $REPO 0 32813
	# 32814
	extract $REPO 32815 34770
	# 34771 
	extract $REPO 34772 34793
	# 34794 
	extract $REPO 34795 35175
	# 35176 
	extract $REPO 35177 35180
	# 35181 
	extract $REPO 35182 35198
	# 35199 
	extract $REPO 35200 35201
	# 35202 
	# 35203 
	extract $REPO 35204 35204
	# 35205 
	extract $REPO 35206 35206
	# 35207 
	extract $REPO 35208 35209
	# 35210 
	extract $REPO 35211 35378
	# 35379 
	extract $REPO 35380 35430
	# 35431 
	# 35432 
	
	HEAD=`svn info $REPO | grep Revision | cut -d' ' -f 2`
	
	extract $REPO 35433 $HEAD 

elif [ "$MODE" == "LINK" ]; then

	FILTERED_DUMP_PATH=$2
	
	if [ -z "$FILTERED_DUMP_PATH" ]; then
		echo "Missing Filtered Dump Path"
		usage
	fi
	
	echo "FILTERED_DUMP_PATH = $FILTERED_DUMP_PATH"

	linkFiltered 34771 $FILTERED_DUMP_PATH
	linkFiltered 34794 $FILTERED_DUMP_PATH
	linkFiltered 35176 $FILTERED_DUMP_PATH
	linkFiltered 35181 $FILTERED_DUMP_PATH
	linkFiltered 35199 $FILTERED_DUMP_PATH
	linkFiltered 35202 $FILTERED_DUMP_PATH
	linkFiltered 35203 $FILTERED_DUMP_PATH
	linkFiltered 35205 $FILTERED_DUMP_PATH
	linkFiltered 35207 $FILTERED_DUMP_PATH
	linkFiltered 35210 $FILTERED_DUMP_PATH
	linkFiltered 35379 $FILTERED_DUMP_PATH
	linkFiltered 35431 $FILTERED_DUMP_PATH
	linkFiltered 35432 $FILTERED_DUMP_PATH


elif [ "$MODE" == "APPLY" ]; then

	REPO=$2
	PREFIX=$3

	if [ -z "$REPO" ]; then
		echo "Missing Svn Respository"
		usage
	fi

	DUMP_PATTERN="r*-r*.dump"

	if [ ! -z "$PREFIX" ]; then
		# prefix exists
		DUMP_PATTERN="$PREFIX/$DUMP_PATTERN"	
		ls -l $DUMP_PATTERN | sort -k 9 | awk '{print $9}' | while read F; do load $REPO $PREFIX/$F; done; 

	else
		# no prefix (current dir)

		ls -l $DUMP_PATTERN | sort -k 9 | awk '{print $9}' | while read F; do load $REPO $F; done; 
	fi


else
	echo "INVALID Mode: $MODE"
	usage
fi

