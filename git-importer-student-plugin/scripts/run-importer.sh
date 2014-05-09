#!/bin/bash

JAVA_CMD=/opt/jdk1.7.0_03/bin/java

REV=$1

REPO=$2
DEBUG=$3

DEBUG_OPTS=""

testParam () {

	PARAM=$1

	if test -z $PARAM
	then
		echo "USAGE: <start rev:end rev> <absolute path to git repo> <debug mode: 0 (off) or 1 (on)>"
		exit 1
	fi

}

testParam $REV
testParam $REPO
testParam $DEBUG

if test "$DEBUG" -eq "1"
then
	DEBUG_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9996,suspend=y"
fi

echo "$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -cp \"../../git-importer/target/git-importer-0.0.2-SNAPSHOT.jar:../target/git-importer-student-plugin-0.0.2-SNAPSHOT.jar\" -Dspring.profiles.active=configured-plugin org.kuali.student.git.importer.GitImporterMain ~/ks-r$REV.dump.bz2 $REPO ks-r$REV-veto.log ks-r$REV-skipped-copy-from.log ks-r$REV-blob.log 1 https://svn.kuali.org/repos/student 0ac02d80-fb3f-11dc-a574-c57244a850eb /home/mike/bin/git > ks-r$REV-import.log"

$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -cp "../../git-importer/target/git-importer-0.0.2-SNAPSHOT.jar:../target/git-importer-student-plugin-0.0.2-SNAPSHOT.jar" -Dspring.profiles.active=configured-plugin org.kuali.student.git.importer.GitImporterMain ~/ks-r$REV.dump.bz2 $REPO ks-r$REV-veto.log ks-r$REV-skipped-copy-from.log ks-r$REV-blob.log 1 https://svn.kuali.org/repos/student 0ac02d80-fb3f-11dc-a574-c57244a850eb /home/mike/bin/git > ks-r$REV-import.log

