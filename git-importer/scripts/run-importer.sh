#!/bin/bash

JAVA_CMD=/opt/jdk1.7.0_03/bin/java
VERSION=0.0.2-SNAPSHOT

REV=$1

GIT_REPO_PATH=$2

SVN_REPO_URL=$3
SVN_REPO_UUID=$4
C_GIT_COMMAND=$5
DEBUG=$6

DEBUG_OPTS=""

testParam () {

	PARAM=$1

	if test -z $PARAM
	then
		echo "USAGE: <start rev:end rev> <absolute path to git repo> <public svn url> <public svn uuid> <absolute path to C git command> <debug mode: 0 (off) or 1 (on)>"
		exit 1
	fi

}

testParam $REV
testParam $GIT_REPO_PATH
testParam $SVN_REPO_URL
testParam $SVN_REPO_UUID
testParam $CGIT_COMMMAND
testParam $DEBUG

if test "$DEBUG" -eq "1"
then
	DEBUG_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9996,suspend=y"
fi

echo "$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -jar ../target/git-importer-$VERSION.jar ~/ks-r$REV.dump.bz2 $REPO ks-r$REV-veto.log ks-r$REV-skipped-copy-from.log ks-r$REV-blob.log 1 $SVN_REPO_URL $SVN_REPO_UUID $CGIT_COMMAND > ks-r$REV-import.log"

$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -jar ../target/git-importer-$VERSION.jar ~/ks-r$REV.dump.bz2 $REPO ks-r$REV-veto.log ks-r$REV-skipped-copy-from.log ks-r$REV-blob.log 1 $SVN_REPO_URL $SVN_REPO_UUID $CGIT_COMMAND > ks-r$REV-import.log


