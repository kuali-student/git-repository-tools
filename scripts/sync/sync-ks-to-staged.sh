#!/bin/bash
#
# helper script to keep things synced between the ks repo and the rewritten commit history repo.

MODE=$1

usage () {

	echo "USAGE: <mode: FETCH or APPLY> <mode options>"
	echo "FETCH <target repo> <source repo>"
	echo "target is where the start from rev comes from"
	echo "source is where the rev's are coming from"
	echo "APPLY <target repo> <dump file> [user name] [password]"
	exit 1

}

paramPresent () {

	PARAM=$1

	if test -z $PARAM 
	then
		usage
	fi
	
}

paramPresent $MODE

if test "$MODE" == "FETCH"
then
	TARGET_REPO=$2
	SOURCE_REPO=$3

	paramPresent $TARGET_REPO
	paramPresent $SOURCE_REPO
	
	REV=$(svn info $TARGET_REPO | grep Revision | cut -d' ' -f 2)

	TARGET_REV=$(($REV + 1))

	# this is the next rev we need to get from the source repository
	# echo $TARGET_REV

	echo "svnrdump dump $SOURCE_REPO -r$TARGET_REV:HEAD --incremental > r$TARGET_REV-HEAD.dump"
	svnrdump dump $SOURCE_REPO -r$TARGET_REV:HEAD --incremental > r$TARGET_REV-HEAD.dump

	echo "CREATED Dump File: r$TARGET_REV-HEAD.dump"

elif test "$MODE" == "APPLY"
then
	TARGET_REPO=$2
	DUMP_FILE=$3
    USERNAME=$4
    PASSWORD=$5

	paramPresent $TARGET_REPO
	paramPresent $DUMP_FILE

    CMD_OPTIONS=""
 
    if test -n "$USERNAME"
    then
        paramPresent "$PASSWORD"

        CMD_OPTIONS="--username $USERNAME --password $USERNAME"

    fi        	
	echo "svnrdump load $TARGET_REPO $CMD_OPTIONS < $DUMP_FILE"
	svnrdump load $TARGET_REPO $CMD_OPTIONS < $DUMP_FILE

else
	echo "INVALID Mode: $MODE"
	usage
fi

exit 1


