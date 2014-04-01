#!/bin/bash
#
# Prepare the revprops fix script.

MODE=$1

usage () {
    MESSAGE=$1
    if test -n "$MESSAGE"
    then
        echo "$MESSAGE"
    fi

    echo "USAGE: <mode: GEN or AGG>"
    echo "GENerate the fix-revprop-XYZ.sh scripts"
    echo "AGGregate the fix-revprop-XYZ.sh scripts into a single script"
    exit 1;
}

if test -z "$MODE"
then
    usage "invalid Mode: $MODE"
fi

AGG_OUTPUT=fix-revprops-all.sh

FILES=$(ls -l r*-filtered.dump | awk '{print $9}')

rm -f fix-revprops.log

rm -f $AGG_OUTPUT

for F in $FILES
do
    REV=$(basename $F -filtered.dump | sed 's/^r//')
    echo "Processing revision: $REV"
    OUTPUT=fix-revprops-$REV.sh
    if test "$MODE" == "GEN"
    then
        DUMP_FILE=r$REV.dump
        echo "Dump File: $DUMP_FILE"
        echo "Output File: $OUTPUT"
        java -cp ~/svn-history-tools-0.0.1-SNAPSHOT.jar org.kuali.student.svn.tools.revprops.ExtractRevisionPropsMain $DUMP_FILE $OUTPUT >> fix-revprops.log
    elif test "$MODE" == "AGG"
    then
        echo "echo \"Processing revision: $REV\"" >> $AGG_OUTPUT
        cat $OUTPUT >> $AGG_OUTPUT
        echo "echo \"Completed revision: $REV\"" >> $AGG_OUTPUT
    else
        usage "Invalid Mode: $MODE"
    fi

    
done
