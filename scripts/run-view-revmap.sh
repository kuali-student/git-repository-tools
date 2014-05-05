#!/bin/bash
JAVA_CMD=/opt/jdk1.7.0_03/bin/java

REV=$1

DEBUG=$2

DEBUG_OPTS=""

if test -z $REV
then
	echo "USAGE: <rev>"
	exit 1
fi

if test "$DEBUG" -eq "1"
then
	DEBUG_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9996,suspend=y"
fi


echo "$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -cp \"../git-importer/target/git-importer-0.0.2-SNAPSHOT.jar\" org.kuali.student.git.importer.ViewSvnRevisionMapDataFileMain /home/mike/git/ks-git-bridge revisions $REV"

$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -cp "../git-importer/target/git-importer-0.0.2-SNAPSHOT.jar" org.kuali.student.git.importer.ViewSvnRevisionMapDataFileMain /home/mike/git/ks-git-bridge revisions $REV


