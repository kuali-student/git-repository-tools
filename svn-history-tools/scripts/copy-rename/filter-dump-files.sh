#!/bin/bash
# Note for some unknown reason running in remote debug mode works but without it it gets stuck.
DEBUG_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=6523"

JAR_FILE=~/svn-history-tools-0.0.1-SNAPSHOT.jar 
PIPE=/tmp/filter-dump-files.pipe
DELETE=$1
DEBUG=$2

# need to pass in the name of the rX-rY-join.dat file to use
SPECIFIC=$3

# only delete files if not in specific mode.
if test -z "$SPECIFIC"
then
    # delete any existing filtered dump files
    if test -n "$DELETE"
    then
    
        echo "rm -f *-filtered.dump"
        rm -f *-filtered.dump

    fi

fi

echo "rm -f \"$PIPE\""

rm -f "$PIPE"

mkfifo $PIPE

JAVA_DEBUG=""

if test -n "$DEBUG" 
then
    JAVA_DEBUG="$DEBUG_OPTS"
fi

echo "exec java $JAVA_DEBUG -jar $JAR_FILE < $PIPE > java.log &"
$(java $JAVA_DEBUG -Xmx1024m -jar $JAR_FILE < $PIPE > java.log) &

JAVA_PID=$!

if test -z "$JAVA_PID"
then
    echo "Failed to Acquire PID"

fi

if test -n "$SPECIFIC"
then

    echo "Processing $SPECIFIC"
    echo "$SPECIFIC" >> $PIPE

else

    JOIN_WORK=$(ls -l *-join.dat | awk '{print $9}')

    for WORK in $JOIN_WORK
    do

        TARGET_REV=$(echo $WORK | cut -d- -f1)

        FILTERED_DUMP="$TARGET_REV-filtered.dump"

    
        if test -f $FILTERED_DUMP
        then
            echo "$FILTERED_DUMP exists, skipping"
            continue
        fi

        echo "Processing $WORK" 
        echo "$WORK" >> $PIPE
    
    done

fi
# hope to close the pipe
cat /dev/null >> $PIPE

echo "wait $JAVA_PID"
wait $JAVA_PID
