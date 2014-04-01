#!/bin/sh
#
# report.sh
#
# Generate reports on each of the modules
#

export KS=ks-api
python analyze.py REPORT wc-analysis-filtered-$KS-trunk wc-analysis-original-$KS-trunk > $KS-trunk-report.txt

cat $KS-trunk-report.txt | egrep "^(diff|same)" | cut -d: -f3 | sort | uniq -c | sed 's/^\ *//' | sort -g > $KS-trunk-paths-per-commit-report.txt

rm -f $KS-trunk-paths.txt; cat $KS-trunk-paths-per-commit-report.txt | cut -d' ' -f2 | while read REV; do echo "START-REV:" >> $KS-trunk-paths.txt;svn log -l 1 --verbose file:///home/mike/subversion/ksenroll-3907/staged-repo@$REV >> $KS-trunk-paths.txt; echo "END-REV:" >> $KS-trunk-paths.txt; done;
export KS=ks-core

python analyze.py REPORT wc-analysis-filtered-$KS-trunk wc-analysis-original-$KS-trunk > $KS-trunk-report.txt

cat $KS-trunk-report.txt | egrep "^(diff|same)" | cut -d: -f3 | sort | uniq -c | sed 's/^\ *//' | sort -g > $KS-trunk-paths-per-commit-report.txt

rm -f $KS-trunk-paths.txt; cat $KS-trunk-paths-per-commit-report.txt | cut -d' ' -f2 | while read REV; do echo "START-REV:" >> $KS-trunk-paths.txt;svn log -l 1 --verbose file:///home/mike/subversion/ksenroll-3907/staged-repo@$REV >> $KS-trunk-paths.txt; echo "END-REV:" >> $KS-trunk-paths.txt; done;
export KS=ks-lum
python analyze.py REPORT wc-analysis-filtered-$KS-trunk wc-analysis-original-$KS-trunk > $KS-trunk-report.txt

cat $KS-trunk-report.txt | egrep "^(diff|same)" | cut -d: -f3 | sort | uniq -c | sed 's/^\ *//' | sort -g > $KS-trunk-paths-per-commit-report.txt

rm -f $KS-trunk-paths.txt; cat $KS-trunk-paths-per-commit-report.txt | cut -d' ' -f2 | while read REV; do echo "START-REV:" >> $KS-trunk-paths.txt;svn log -l 1 --verbose file:///home/mike/subversion/ksenroll-3907/staged-repo@$REV >> $KS-trunk-paths.txt; echo "END-REV:" >> $KS-trunk-paths.txt; done;
export KS=ks-enroll
python analyze.py REPORT wc-analysis-filtered-$KS-trunk wc-analysis-original-$KS-trunk > $KS-trunk-report.txt

cat $KS-trunk-report.txt | egrep "^(diff|same)" | cut -d: -f3 | sort | uniq -c | sed 's/^\ *//' | sort -g > $KS-trunk-paths-per-commit-report.txt

rm -f $KS-trunk-paths.txt; cat $KS-trunk-paths-per-commit-report.txt | cut -d' ' -f2 | while read REV; do echo "START-REV:" >> $KS-trunk-paths.txt;svn log -l 1 --verbose file:///home/mike/subversion/ksenroll-3907/staged-repo@$REV >> $KS-trunk-paths.txt; echo "END-REV:" >> $KS-trunk-paths.txt; done;
export KS=ks-deployments
python analyze.py REPORT wc-analysis-filtered-$KS-trunk wc-analysis-original-$KS-trunk > $KS-trunk-report.txt

cat $KS-trunk-report.txt | egrep "^(diff|same)" | cut -d: -f3 | sort | uniq -c | sed 's/^\ *//' | sort -g > $KS-trunk-paths-per-commit-report.txt
rm -f $KS-trunk-paths.txt; cat $KS-trunk-paths-per-commit-report.txt | cut -d' ' -f2 | while read REV; do echo "START-REV:" >> $KS-trunk-paths.txt;svn log -l 1 --verbose file:///home/mike/subversion/ksenroll-3907/staged-repo@$REV >> $KS-trunk-paths.txt; echo "END-REV:" >> $KS-trunk-paths.txt; done;
