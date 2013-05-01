#!/bin/bash
#
# fetch.sh
#
# fetch the analysis data for each directory being analyized.
#
#
ls -ld wc-analysis-* | grep ^d | awk '{print }' | while read R; do echo "FETCH "; python analyze.py FETCH ; done;
