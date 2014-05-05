#!/bin/bash

JAVA_CMD=/opt/jdk1.7.0_03/bin/java

REV=$1

DEBUG=$2

DEBUG_OPTS=""

if test -z $REV
then
	echo "USAGE: <start rev:end rev>"
	exit 1
fi

if test "$DEBUG" -eq "1"
then
	DEBUG_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9996,suspend=y"
fi

echo "$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -cp \"../../git-importer/target/git-importer-0.0.2-SNAPSHOT.jar:../target/git-importer-student-plugin-0.0.2-SNAPSHOT.jar\" -Dspring.profiles.active=configured-plugin org.kuali.student.git.importer.GitImporterMain ~/ks-r$REV.dump.bz2 /home/mike/git/ks-git-bridge ks-r$REV-veto.log ks-r$REV-skipped-copy-from.log ks-r$REV-blob.log 1 https://svn.kuali.org/repos/student 0ac02d80-fb3f-11dc-a574-c57244a850eb /home/mike/bin/git > ks-r$REV-import.log"

$JAVA_CMD -Xmx4096m -XX:MaxPermSize=512m $DEBUG_OPTS -cp "../../git-importer/target/git-importer-0.0.2-SNAPSHOT.jar:../target/git-importer-student-plugin-0.0.2-SNAPSHOT.jar" -Dspring.profiles.active=configured-plugin org.kuali.student.git.importer.GitImporterMain ~/ks-r$REV.dump.bz2 /home/mike/git/ks-git-bridge ks-r$REV-veto.log ks-r$REV-skipped-copy-from.log ks-r$REV-blob.log 1 https://svn.kuali.org/repos/student 0ac02d80-fb3f-11dc-a574-c57244a850eb /home/mike/bin/git > ks-r$REV-import.log

