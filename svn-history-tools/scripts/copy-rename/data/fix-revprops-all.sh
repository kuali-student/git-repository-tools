#!/bin/sh
#
# fix-revprops-all.sh
#
# Fix all of the revision properties that were lost duing the SVN 
# history repain in KSENROLL-3907
#
REPO_URL=$1
SVN_CMD="svn propset"

if test -z "$REPO_URL"
then
    echo "USAGE: <repo url>"
    exit 1
fi

#
#Generated
$SVN_CMD --revprop -r 17 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 17 svn:date "2008-03-26T17:28:59.619314Z" $REPO_URL
$SVN_CMD --revprop -r 17 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 20 svn:date "2008-03-26T17:31:59.172418Z" $REPO_URL
$SVN_CMD --revprop -r 20 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 21 svn:date "2008-03-26T17:32:58.889249Z" $REPO_URL
$SVN_CMD --revprop -r 21 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 34 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 34 svn:date "2008-03-26T20:04:36.020031Z" $REPO_URL
$SVN_CMD --revprop -r 34 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 36 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 36 svn:date "2008-03-26T20:26:12.689692Z" $REPO_URL
$SVN_CMD --revprop -r 36 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 39 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 39 svn:date "2008-03-26T20:41:40.238063Z" $REPO_URL
$SVN_CMD --revprop -r 39 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 40 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 40 svn:date "2008-03-26T21:27:14.812889Z" $REPO_URL
$SVN_CMD --revprop -r 40 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 41 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 41 svn:date "2008-03-27T14:17:41.335332Z" $REPO_URL
$SVN_CMD --revprop -r 41 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 42 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 42 svn:date "2008-03-27T14:20:55.686224Z" $REPO_URL
$SVN_CMD --revprop -r 42 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 44 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 44 svn:date "2008-03-27T15:25:09.142536Z" $REPO_URL
$SVN_CMD --revprop -r 44 svn:log "Split up context file into dao and service." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 47 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 47 svn:date "2008-03-27T15:45:33.812631Z" $REPO_URL
$SVN_CMD --revprop -r 47 svn:log "updated jaxws" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 69 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 69 svn:date "2008-03-31T17:27:56.156956Z" $REPO_URL
$SVN_CMD --revprop -r 69 svn:log "Import from old repository" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 75 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 75 svn:date "2008-03-31T17:31:04.819233Z" $REPO_URL
$SVN_CMD --revprop -r 75 svn:log "Import from old repository" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 77 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 77 svn:date "2008-03-31T17:34:28.476617Z" $REPO_URL
$SVN_CMD --revprop -r 77 svn:log "Import from old repository" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 79 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 79 svn:date "2008-03-31T17:36:45.405725Z" $REPO_URL
$SVN_CMD --revprop -r 79 svn:log "Import from old repository" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 82 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 82 svn:date "2008-04-01T13:01:10.826058Z" $REPO_URL
$SVN_CMD --revprop -r 82 svn:log "[NO JIRA] moved misplaced transactions.properties" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 111 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 111 svn:date "2008-04-03T19:42:33.233712Z" $REPO_URL
$SVN_CMD --revprop -r 111 svn:log "First version of BRMS Core and its Meta Data storage/retrieval" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 135 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 135 svn:date "2008-04-05T01:11:12.656460Z" $REPO_URL
$SVN_CMD --revprop -r 135 svn:log "Example for JUnit testing of JPA and Spring DAO. This is to be used as a starting point and or as an example it is not to be deployed in production. TestDummyDAO uses annotations from Spring 2.5, JUnit 4, JPA, and uses the jsr 250 Resource attribute instead of Spring's autowired attribute. It shows different tests on a dummy jpa entity. There are test lifecycle method stubs which are commented out. Config files are in resources/META-INF." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 172 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 172 svn:date "2008-04-10T19:17:17.207986Z" $REPO_URL
$SVN_CMD --revprop -r 172 svn:log "added common-test tester" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 184 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 184 svn:date "2008-04-11T07:23:05.351960Z" $REPO_URL
$SVN_CMD --revprop -r 184 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 212 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 212 svn:date "2008-04-15T15:32:39.993276Z" $REPO_URL
$SVN_CMD --revprop -r 212 svn:log "Added wrapper classes and updated hibernate version" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 221 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 221 svn:date "2008-04-15T22:00:22.783867Z" $REPO_URL
$SVN_CMD --revprop -r 221 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 235 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 235 svn:date "2008-04-16T20:00:30.327226Z" $REPO_URL
$SVN_CMD --revprop -r 235 svn:log "Fixed maven compile, using velocity 1.4 and added RuleTemplateTest.java" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 242 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 242 svn:date "2008-04-17T15:06:23.403775Z" $REPO_URL
$SVN_CMD --revprop -r 242 svn:log "Added Manager test which is like service test, but there is no jetty." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 253 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 253 svn:date "2008-04-17T18:34:08.737975Z" $REPO_URL
$SVN_CMD --revprop -r 253 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 261 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 261 svn:date "2008-04-17T19:55:30.917453Z" $REPO_URL
$SVN_CMD --revprop -r 261 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 271 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 271 svn:date "2008-04-18T14:46:00.686945Z" $REPO_URL
$SVN_CMD --revprop -r 271 svn:log "Added an exception" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 272 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 272 svn:date "2008-04-18T15:13:54.259112Z" $REPO_URL
$SVN_CMD --revprop -r 272 svn:log "Added an exception" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 273 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 273 svn:date "2008-04-18T15:41:31.516086Z" $REPO_URL
$SVN_CMD --revprop -r 273 svn:log "Added an exception" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 274 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 274 svn:date "2008-04-18T16:05:57.093723Z" $REPO_URL
$SVN_CMD --revprop -r 274 svn:log "added the jaxb map to list thing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 278 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 278 svn:date "2008-04-18T20:52:17.092355Z" $REPO_URL
$SVN_CMD --revprop -r 278 svn:log "Finished filling out all classes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 287 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 287 svn:date "2008-04-21T18:41:57.955665Z" $REPO_URL
$SVN_CMD --revprop -r 287 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 292 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 292 svn:date "2008-04-21T20:40:46.782605Z" $REPO_URL
$SVN_CMD --revprop -r 292 svn:log "Added parent poms to common. 
Update poms to use dependency management.
Use fine-grained spring jars." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 296 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 296 svn:date "2008-04-22T01:10:08.942464Z" $REPO_URL
$SVN_CMD --revprop -r 296 svn:log "Renamed from BRMSUtil to RuleEngineUtil" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 301 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 301 svn:date "2008-04-22T01:12:31.588744Z" $REPO_URL
$SVN_CMD --revprop -r 301 svn:log "Renamed and moved to new exceptions package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 328 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 328 svn:date "2008-04-22T15:33:33.170058Z" $REPO_URL
$SVN_CMD --revprop -r 328 svn:log "Began persistence work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 352 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 352 svn:date "2008-04-23T15:41:18.530762Z" $REPO_URL
$SVN_CMD --revprop -r 352 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 368 svn:author "ddean" $REPO_URL
$SVN_CMD --revprop -r 368 svn:date "2008-04-23T20:05:46.264440Z" $REPO_URL
$SVN_CMD --revprop -r 368 svn:log "init commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 373 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 373 svn:date "2008-04-23T21:17:47.601857Z" $REPO_URL
$SVN_CMD --revprop -r 373 svn:log "Add callback handlers & validators for metro WSIT." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 390 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 390 svn:date "2008-04-24T18:18:10.091816Z" $REPO_URL
$SVN_CMD --revprop -r 390 svn:log "Added more tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 455 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 455 svn:date "2008-04-28T17:34:56.202490Z" $REPO_URL
$SVN_CMD --revprop -r 455 svn:log "Add lui wrapper beans" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 456 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 456 svn:date "2008-04-28T18:30:18.806818Z" $REPO_URL
$SVN_CMD --revprop -r 456 svn:log "Add new find by list of person ids methods." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 460 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 460 svn:date "2008-04-28T19:45:06.827116Z" $REPO_URL
$SVN_CMD --revprop -r 460 svn:log "Added Correct Criteria for CluSets" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 469 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 469 svn:date "2008-04-29T00:00:35.497554Z" $REPO_URL
$SVN_CMD --revprop -r 469 svn:log "Moved away from Spring JPA wrapper and create appropriate package structure" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 481 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 481 svn:date "2008-04-29T03:22:18.614971Z" $REPO_URL
$SVN_CMD --revprop -r 481 svn:log "Moved to new drools package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 507 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 507 svn:date "2008-04-29T19:05:51.259911Z" $REPO_URL
$SVN_CMD --revprop -r 507 svn:log "Modify to use exceptions from common-ws." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 509 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 509 svn:date "2008-04-29T19:18:26.352578Z" $REPO_URL
$SVN_CMD --revprop -r 509 svn:log "Added fetchluidisplay" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 527 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 527 svn:date "2008-04-30T01:11:22.422969Z" $REPO_URL
$SVN_CMD --revprop -r 527 svn:log "Create Learning Unit war file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 531 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 531 svn:date "2008-04-30T03:37:54.781669Z" $REPO_URL
$SVN_CMD --revprop -r 531 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 533 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 533 svn:date "2008-04-30T17:35:51.750948Z" $REPO_URL
$SVN_CMD --revprop -r 533 svn:log "Created a utility class" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 541 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 541 svn:date "2008-04-30T20:43:52.521253Z" $REPO_URL
$SVN_CMD --revprop -r 541 svn:log "updated the war project and implemented some more service methods" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 547 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 547 svn:date "2008-05-01T14:01:30.744789Z" $REPO_URL
$SVN_CMD --revprop -r 547 svn:log "Add back the fault beans so metro works." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 548 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 548 svn:date "2008-05-01T14:02:13.551386Z" $REPO_URL
$SVN_CMD --revprop -r 548 svn:log "Add fault beans." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 564 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 564 svn:date "2008-05-01T22:02:08.018622Z" $REPO_URL
$SVN_CMD --revprop -r 564 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 580 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 580 svn:date "2008-05-02T04:19:10.393098Z" $REPO_URL
$SVN_CMD --revprop -r 580 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 588 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 588 svn:date "2008-05-02T19:25:22.508415Z" $REPO_URL
$SVN_CMD --revprop -r 588 svn:log "Upgraded to CXF 2.1
fixed Jetty problem with tests where two service tests run after each other failed" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 606 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 606 svn:date "2008-05-05T15:55:26.914411Z" $REPO_URL
$SVN_CMD --revprop -r 606 svn:log "Move ehcache from META-INF to parent folder. Remove test-resource from pom." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 618 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 618 svn:date "2008-05-05T18:30:04.943784Z" $REPO_URL
$SVN_CMD --revprop -r 618 svn:log "Validate Rule Module to merge all the different BRMS components." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 668 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 668 svn:date "2008-05-06T20:52:08.456729Z" $REPO_URL
$SVN_CMD --revprop -r 668 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 670 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 670 svn:date "2008-05-06T20:54:41.217994Z" $REPO_URL
$SVN_CMD --revprop -r 670 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 679 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 679 svn:date "2008-05-07T16:04:50.687988Z" $REPO_URL
$SVN_CMD --revprop -r 679 svn:log "trying to fix this error" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 695 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 695 svn:date "2008-05-08T18:21:55.450982Z" $REPO_URL
$SVN_CMD --revprop -r 695 svn:log "Moved caching to delegate and added test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 707 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 707 svn:date "2008-05-08T20:46:38.005583Z" $REPO_URL
$SVN_CMD --revprop -r 707 svn:log "\\- added copyrights
- updated comments
- added test cases" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 712 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 712 svn:date "2008-05-09T00:11:24.330887Z" $REPO_URL
$SVN_CMD --revprop -r 712 svn:log "Update test classes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 717 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 717 svn:date "2008-05-20T21:09:22.540557Z" $REPO_URL
$SVN_CMD --revprop -r 717 svn:log "Added some caching in the test as an example of client/server caching" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 718 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 718 svn:date "2008-05-21T15:15:37.639797Z" $REPO_URL
$SVN_CMD --revprop -r 718 svn:log "added wsdl and moved location of db in tomcat" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 724 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 724 svn:date "2008-05-22T17:28:03.588789Z" $REPO_URL
$SVN_CMD --revprop -r 724 svn:log "Switched to test framework introduced by services implementation team" $REPO_URL
$SVN_CMD --revprop -r 734 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 734 svn:date "2008-05-29T17:41:25.833569Z" $REPO_URL
$SVN_CMD --revprop -r 734 svn:log "changes to BRMS: added \"valueType\" field for LeftHandSide so that RightHandSide can be set with type matching the left hand side." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 737 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 737 svn:date "2008-05-29T21:26:59.565937Z" $REPO_URL
$SVN_CMD --revprop -r 737 svn:log "Added configuration to use an in-memory file system and persistence manager to speed up unit tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 738 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 738 svn:date "2008-05-29T21:39:59.974581Z" $REPO_URL
$SVN_CMD --revprop -r 738 svn:log "Added configuration to use an in-memory file system and persistence manager to speed up unit tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 751 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 751 svn:date "2008-06-03T00:32:34.912096Z" $REPO_URL
$SVN_CMD --revprop -r 751 svn:log "\- updated classpath and pom files for new brms-agenda module
- changed ComparisonOperatorType to ComparisonOperator" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 753 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 753 svn:date "2008-06-03T04:21:16.792965Z" $REPO_URL
$SVN_CMD --revprop -r 753 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 754 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 754 svn:date "2008-06-03T04:26:45.254100Z" $REPO_URL
$SVN_CMD --revprop -r 754 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 780 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 780 svn:date "2008-06-04T20:56:02.199417Z" $REPO_URL
$SVN_CMD --revprop -r 780 svn:log "Add ComparisonOperator locally to the package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 783 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 783 svn:date "2008-06-05T17:52:00.398840Z" $REPO_URL
$SVN_CMD --revprop -r 783 svn:log "replacing package BRMScore with brms.core (part2)
moving some methods from FunctionalBusinessRuleManagementService.java to FunctionalBusinessRule.java" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 824 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 824 svn:date "2008-06-11T03:25:00.222500Z" $REPO_URL
$SVN_CMD --revprop -r 824 svn:log "Cleaned up code for GenerateRuleSet" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 835 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 835 svn:date "2008-06-11T18:29:43.177883Z" $REPO_URL
$SVN_CMD --revprop -r 835 svn:log "rearranged dependencies" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 845 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 845 svn:date "2008-06-11T22:41:31.559629Z" $REPO_URL
$SVN_CMD --revprop -r 845 svn:log "Rename some of the FunctionalBusinessRule fields" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 857 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 857 svn:date "2008-06-16T17:26:24.062465Z" $REPO_URL
$SVN_CMD --revprop -r 857 svn:log "Added in-memory Jackrabbit repository.xml configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 863 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 863 svn:date "2008-06-16T20:16:04.020089Z" $REPO_URL
$SVN_CMD --revprop -r 863 svn:log "Merged brms-agenda into brms-core. Refactored FunctionalBusinessRuleService to be a spring singleton." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 876 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 876 svn:date "2008-06-17T00:15:48.363272Z" $REPO_URL
$SVN_CMD --revprop -r 876 svn:log "Added in-memory Jackrabbit repository.xml configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 924 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 924 svn:date "2008-06-24T14:18:37.024177Z" $REPO_URL
$SVN_CMD --revprop -r 924 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 943 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 943 svn:date "2008-06-25T17:25:36.968178Z" $REPO_URL
$SVN_CMD --revprop -r 943 svn:log "Additional work on using text-beans.xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 945 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 945 svn:date "2008-06-25T18:38:50.599156Z" $REPO_URL
$SVN_CMD --revprop -r 945 svn:log "moved LoadBeanListener.java and LoadDataBean.java to srm/main" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 946 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 946 svn:date "2008-06-25T18:44:42.474221Z" $REPO_URL
$SVN_CMD --revprop -r 946 svn:log "added test bean to rule-execution" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 952 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 952 svn:date "2008-06-25T23:35:33.025840Z" $REPO_URL
$SVN_CMD --revprop -r 952 svn:log "moved all test classes to use AbstractTransactionalDaoTest so that all test beans are automatically loaded and unloaded from database" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 953 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 953 svn:date "2008-06-26T13:38:05.194768Z" $REPO_URL
$SVN_CMD --revprop -r 953 svn:log "Ported PropertiesFactory to PropertiesFilterFactoryBean.  Moved data beans to separate context." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 964 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 964 svn:date "2008-06-27T05:21:41.318034Z" $REPO_URL
$SVN_CMD --revprop -r 964 svn:log "Example of how to have multiple rules and facts in the rule engine working memory" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 977 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 977 svn:date "2008-06-27T21:05:00.227157Z" $REPO_URL
$SVN_CMD --revprop -r 977 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1003 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 1003 svn:date "2008-06-30T14:00:24.452313Z" $REPO_URL
$SVN_CMD --revprop -r 1003 svn:log "jpa.adapter property wasn't working so moved to context.xml.  Commented out settings for other db's." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1007 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 1007 svn:date "2008-06-30T20:11:38.234761Z" $REPO_URL
$SVN_CMD --revprop -r 1007 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1028 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1028 svn:date "2008-07-04T19:52:49.251965Z" $REPO_URL
$SVN_CMD --revprop -r 1028 svn:log "Moved RuleTemplate.vm into velocity-templates" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1065 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1065 svn:date "2008-07-07T19:20:43.748718Z" $REPO_URL
$SVN_CMD --revprop -r 1065 svn:log "Added exception" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1081 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1081 svn:date "2008-07-08T21:36:51.949582Z" $REPO_URL
$SVN_CMD --revprop -r 1081 svn:log "Added Request interface" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1084 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1084 svn:date "2008-07-08T21:42:27.540705Z" $REPO_URL
$SVN_CMD --revprop -r 1084 svn:log "Added Math101PreReqRules.drl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1088 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1088 svn:date "2008-07-09T17:36:06.228447Z" $REPO_URL
$SVN_CMD --revprop -r 1088 svn:log "\\- updated all modules according to the latest BRMS Core entity diagram from July 7" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1104 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1104 svn:date "2008-07-09T23:52:09.215823Z" $REPO_URL
$SVN_CMD --revprop -r 1104 svn:log "Changed logging levels" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1145 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 1145 svn:date "2008-07-14T22:19:27.773948Z" $REPO_URL
$SVN_CMD --revprop -r 1145 svn:log "added paging table" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1147 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1147 svn:date "2008-07-15T08:42:11.478200Z" $REPO_URL
$SVN_CMD --revprop -r 1147 svn:log "Added Drools Jackrabbit repository in-memory configuration file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1150 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1150 svn:date "2008-07-15T17:27:31.550851Z" $REPO_URL
$SVN_CMD --revprop -r 1150 svn:log "renamed FunctionalBusinessRuleManagementService to BusinessRuleManagementService to correspond to BRMS service definition" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1155 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1155 svn:date "2008-07-16T00:30:15.701467Z" $REPO_URL
$SVN_CMD --revprop -r 1155 svn:log "Added a new Drools rule verifier (coming with Drools 5.0)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1159 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1159 svn:date "2008-07-16T04:29:04.341308Z" $REPO_URL
$SVN_CMD --revprop -r 1159 svn:log "Moved drls to drools/drls package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1197 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1197 svn:date "2008-07-20T04:37:00.821199Z" $REPO_URL
$SVN_CMD --revprop -r 1197 svn:log "Added AgendaDiscoveryException" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1220 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1220 svn:date "2008-07-23T21:39:22.074491Z" $REPO_URL
$SVN_CMD --revprop -r 1220 svn:log "KSTRUL-34 Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1225 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1225 svn:date "2008-07-23T22:26:53.668442Z" $REPO_URL
$SVN_CMD --revprop -r 1225 svn:log "KSTRUL-34 Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1226 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1226 svn:date "2008-07-24T01:27:00.366173Z" $REPO_URL
$SVN_CMD --revprop -r 1226 svn:log "KSTRUL-34 added 3 other modules" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1229 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1229 svn:date "2008-07-24T03:13:10.696353Z" $REPO_URL
$SVN_CMD --revprop -r 1229 svn:log "KSTRUL-34 Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1231 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1231 svn:date "2008-07-24T03:20:37.228605Z" $REPO_URL
$SVN_CMD --revprop -r 1231 svn:log "KSTRUL-34 Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1234 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1234 svn:date "2008-07-24T03:35:21.244879Z" $REPO_URL
$SVN_CMD --revprop -r 1234 svn:log "KSTRUL-34 Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1254 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1254 svn:date "2008-07-26T01:43:30.899116Z" $REPO_URL
$SVN_CMD --revprop -r 1254 svn:log "Renamed validate-rule module to integration-tests and added some performance tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1257 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 1257 svn:date "2008-07-29T21:17:56.554197Z" $REPO_URL
$SVN_CMD --revprop -r 1257 svn:log "converted course prereq rules from UBC JEM to Drools DRL file." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1268 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1268 svn:date "2008-08-01T20:37:03.178660Z" $REPO_URL
$SVN_CMD --revprop -r 1268 svn:log "Added Drools jackrabbit in-memory repository configuration file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1314 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1314 svn:date "2008-08-02T23:38:43.551514Z" $REPO_URL
$SVN_CMD --revprop -r 1314 svn:log "Added web.xml to compile module" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1339 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 1339 svn:date "2008-08-10T20:15:05.407198Z" $REPO_URL
$SVN_CMD --revprop -r 1339 svn:log "cutover from kualidev svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1347 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 1347 svn:date "2008-08-10T20:21:19.965501Z" $REPO_URL
$SVN_CMD --revprop -r 1347 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1365 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 1365 svn:date "2008-08-12T15:18:07.511182Z" $REPO_URL
$SVN_CMD --revprop -r 1365 svn:log "Updated project configuration for new eclipse maven plugin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1382 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1382 svn:date "2008-08-12T20:18:50.169148Z" $REPO_URL
$SVN_CMD --revprop -r 1382 svn:log "Added gui-developers module." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1405 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 1405 svn:date "2008-08-13T18:47:50.914860Z" $REPO_URL
$SVN_CMD --revprop -r 1405 svn:log "Implement PersonDisplayDTO" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1489 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 1489 svn:date "2008-08-20T13:52:15.270954Z" $REPO_URL
$SVN_CMD --revprop -r 1489 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1503 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 1503 svn:date "2008-08-20T19:47:49.764832Z" $REPO_URL
$SVN_CMD --revprop -r 1503 svn:log "refactor personidentity-api to conform to naming conventions described here
https://test.kuali.org/confluence/display/KULSTU/Project+Structure" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1507 svn:author "rratliff" $REPO_URL
$SVN_CMD --revprop -r 1507 svn:date "2008-08-20T20:01:39.150175Z" $REPO_URL
$SVN_CMD --revprop -r 1507 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1533 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1533 svn:date "2008-08-22T18:20:52.799042Z" $REPO_URL
$SVN_CMD --revprop -r 1533 svn:log "KSTRUL-76 - moved BooleanFunction antlr from old team3 svn project to rules-common" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1543 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 1543 svn:date "2008-08-26T21:00:21.830950Z" $REPO_URL
$SVN_CMD --revprop -r 1543 svn:log "Moved gui-developer from brms-imps-dev to brms-web-dev." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1546 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 1546 svn:date "2008-08-27T14:08:01.198535Z" $REPO_URL
$SVN_CMD --revprop -r 1546 svn:log "Added eclipselink repository and updated project settings for new maven plugin." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1584 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1584 svn:date "2008-08-27T20:57:49.297820Z" $REPO_URL
$SVN_CMD --revprop -r 1584 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1591 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1591 svn:date "2008-08-28T19:16:14.664013Z" $REPO_URL
$SVN_CMD --revprop -r 1591 svn:log "First version of rules management service and corresponding wsdl." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1613 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 1613 svn:date "2008-08-29T13:28:55.440911Z" $REPO_URL
$SVN_CMD --revprop -r 1613 svn:log "moved from fsu" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1619 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 1619 svn:date "2008-09-02T19:52:52.118263Z" $REPO_URL
$SVN_CMD --revprop -r 1619 svn:log "Moved from old repo" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1626 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1626 svn:date "2008-09-03T19:51:05.587943Z" $REPO_URL
$SVN_CMD --revprop -r 1626 svn:log "Rules management service impl with hard coded return values." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1640 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1640 svn:date "2008-09-04T21:43:15.919945Z" $REPO_URL
$SVN_CMD --revprop -r 1640 svn:log "Initial checkin of the repository service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1645 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1645 svn:date "2008-09-04T21:51:57.386170Z" $REPO_URL
$SVN_CMD --revprop -r 1645 svn:log "Initial checkin of service and adapter" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1661 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1661 svn:date "2008-09-05T19:09:38.138754Z" $REPO_URL
$SVN_CMD --revprop -r 1661 svn:log "Proper packaging" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1674 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1674 svn:date "2008-09-06T00:45:24.037453Z" $REPO_URL
$SVN_CMD --revprop -r 1674 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1695 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1695 svn:date "2008-09-09T00:21:21.951715Z" $REPO_URL
$SVN_CMD --revprop -r 1695 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1701 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1701 svn:date "2008-09-09T18:07:08.590045Z" $REPO_URL
$SVN_CMD --revprop -r 1701 svn:log "Updating entity definitions to add common Enums shared across rule modules" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1724 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 1724 svn:date "2008-09-10T01:26:41.957016Z" $REPO_URL
$SVN_CMD --revprop -r 1724 svn:log "Added tests and bad wsdl files for duplicate and extra wsdl operations, elements, complexTypes, and sequences. BadDuplicateTypePersonService is intentionally invalid wsdl." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1726 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1726 svn:date "2008-09-10T17:00:39.302046Z" $REPO_URL
$SVN_CMD --revprop -r 1726 svn:log "Moving rulesmanagement to rulemanagement (refactor)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1727 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1727 svn:date "2008-09-10T17:00:59.289358Z" $REPO_URL
$SVN_CMD --revprop -r 1727 svn:log "Moving rulesmanagement to rulemanagement (refactor)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1730 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1730 svn:date "2008-09-10T17:01:52.928855Z" $REPO_URL
$SVN_CMD --revprop -r 1730 svn:log "Moving rulesmanagement to rulemanagement (refactor)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1740 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1740 svn:date "2008-09-10T17:07:49.632060Z" $REPO_URL
$SVN_CMD --revprop -r 1740 svn:log "Moving rulesmanagement to rulemanagement (refactor)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1749 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1749 svn:date "2008-09-11T23:53:12.387194Z" $REPO_URL
$SVN_CMD --revprop -r 1749 svn:log "Reorganized configuration files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1766 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1766 svn:date "2008-09-16T19:05:01.225947Z" $REPO_URL
$SVN_CMD --revprop -r 1766 svn:log "refactor and upgrade to use web services" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1767 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1767 svn:date "2008-09-16T19:05:34.969585Z" $REPO_URL
$SVN_CMD --revprop -r 1767 svn:log "refactor and upgrade to use web services" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1798 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1798 svn:date "2008-09-18T01:54:09.078741Z" $REPO_URL
$SVN_CMD --revprop -r 1798 svn:log "Add so that GWT compiler can find DTO entities by including the entities as a new module" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1816 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 1816 svn:date "2008-09-18T19:35:53.136928Z" $REPO_URL
$SVN_CMD --revprop -r 1816 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1822 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 1822 svn:date "2008-09-18T19:52:20.242463Z" $REPO_URL
$SVN_CMD --revprop -r 1822 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1850 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1850 svn:date "2008-09-23T18:14:42.378093Z" $REPO_URL
$SVN_CMD --revprop -r 1850 svn:log "\\- added more Rules Management services for use by Gui
\\- work on Business Rules Type interface
\\- added test cases" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1851 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1851 svn:date "2008-09-23T18:17:46.701520Z" $REPO_URL
$SVN_CMD --revprop -r 1851 svn:log "\\- added configuration to make Rule Management a Google module" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1873 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1873 svn:date "2008-09-24T20:01:42.094383Z" $REPO_URL
$SVN_CMD --revprop -r 1873 svn:log "Added mock-service-context to service test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1886 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1886 svn:date "2008-09-25T14:36:20.703582Z" $REPO_URL
$SVN_CMD --revprop -r 1886 svn:log "Rules Management Phase-I" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1888 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1888 svn:date "2008-09-25T14:46:40.861318Z" $REPO_URL
$SVN_CMD --revprop -r 1888 svn:log "Rules Management Phase-I" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1898 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1898 svn:date "2008-09-26T00:10:37.215725Z" $REPO_URL
$SVN_CMD --revprop -r 1898 svn:log "Refactored to new brms structure" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1902 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1902 svn:date "2008-09-26T18:17:37.287494Z" $REPO_URL
$SVN_CMD --revprop -r 1902 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1918 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1918 svn:date "2008-09-29T17:20:09.872646Z" $REPO_URL
$SVN_CMD --revprop -r 1918 svn:log "Added rule execution context" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1920 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 1920 svn:date "2008-09-29T18:04:19.276699Z" $REPO_URL
$SVN_CMD --revprop -r 1920 svn:log "new test cases and tighter integration with rule management web services" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1940 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 1940 svn:date "2008-10-01T19:45:02.176713Z" $REPO_URL
$SVN_CMD --revprop -r 1940 svn:log "Restructure so we have one parent pom, getting rid of common-cxf, common-methro, and eclipselink/hibernate poms." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1953 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1953 svn:date "2008-10-02T20:14:59.786023Z" $REPO_URL
$SVN_CMD --revprop -r 1953 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1960 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 1960 svn:date "2008-10-03T20:47:25.772211Z" $REPO_URL
$SVN_CMD --revprop -r 1960 svn:log "Enabled application-context to read property file in classpath or another location. Fixed PropertyPlaceHolderConfigurer bean configuration to read from both files, changed datasource bean configuration, added new PropertiesOverrideFilterFactoryBean to read properties with a specified prefix from either file, changed beans that explicitly read only from classpath properties file to use new bean instead of PropertiesFilterFactoryBean, added new Test for Spring Application Context and added generic debug Spring helper class.

New classes are added to poc project instead of common because poc has shipped with previous version of common so only this project needs to be updated. PropertiesOverrideFilterFactoryBean and SpringContextDebugHelper will also be copied to current branch of common project.

Also changed pom to compile Java 6 classes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1962 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 1962 svn:date "2008-10-03T21:25:52.661006Z" $REPO_URL
$SVN_CMD --revprop -r 1962 svn:log "Rewrote the rule translation velocity templates to fix a Drools bug when using the Drools from keyword:" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1978 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1978 svn:date "2008-10-08T18:01:54.522317Z" $REPO_URL
$SVN_CMD --revprop -r 1978 svn:log "Fact Finder API First Draft" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1981 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1981 svn:date "2008-10-08T18:05:48.026757Z" $REPO_URL
$SVN_CMD --revprop -r 1981 svn:log "Merged with repository service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1988 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1988 svn:date "2008-10-08T19:05:46.264854Z" $REPO_URL
$SVN_CMD --revprop -r 1988 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 1997 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 1997 svn:date "2008-10-14T16:25:41.339171Z" $REPO_URL
$SVN_CMD --revprop -r 1997 svn:log "Added Rule Status Enum 
Fixed update/create rule to write to repository" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2007 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2007 svn:date "2008-10-17T13:32:08.064235Z" $REPO_URL
$SVN_CMD --revprop -r 2007 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2019 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2019 svn:date "2008-10-17T14:12:18.749942Z" $REPO_URL
$SVN_CMD --revprop -r 2019 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2022 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2022 svn:date "2008-10-17T14:29:13.712925Z" $REPO_URL
$SVN_CMD --revprop -r 2022 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2023 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2023 svn:date "2008-10-17T14:31:05.362379Z" $REPO_URL
$SVN_CMD --revprop -r 2023 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2024 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2024 svn:date "2008-10-17T14:31:34.478186Z" $REPO_URL
$SVN_CMD --revprop -r 2024 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2025 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2025 svn:date "2008-10-17T14:31:51.836669Z" $REPO_URL
$SVN_CMD --revprop -r 2025 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2029 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2029 svn:date "2008-10-17T18:39:06.528574Z" $REPO_URL
$SVN_CMD --revprop -r 2029 svn:log "Changed to match the new fact service contract" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2034 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2034 svn:date "2008-10-20T12:55:29.723657Z" $REPO_URL
$SVN_CMD --revprop -r 2034 svn:log "[NO JIRA] getting the parent pom stuff to work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2035 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2035 svn:date "2008-10-20T13:05:00.175092Z" $REPO_URL
$SVN_CMD --revprop -r 2035 svn:log "[NO JIRA] Adding more structure" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2037 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2037 svn:date "2008-10-20T13:16:20.528874Z" $REPO_URL
$SVN_CMD --revprop -r 2037 svn:log "[NO JIRA] trying to get the default settings to work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2057 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2057 svn:date "2008-10-20T14:49:44.542169Z" $REPO_URL
$SVN_CMD --revprop -r 2057 svn:log "[NO JIRA] generated ws artifacts" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2060 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2060 svn:date "2008-10-20T14:58:11.785742Z" $REPO_URL
$SVN_CMD --revprop -r 2060 svn:log "[NO JIRA] added default web.xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2078 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2078 svn:date "2008-10-22T17:38:56.944312Z" $REPO_URL
$SVN_CMD --revprop -r 2078 svn:log "[NO JIRA]" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2080 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 2080 svn:date "2008-10-22T17:40:10.639467Z" $REPO_URL
$SVN_CMD --revprop -r 2080 svn:log "[NO JIRA]" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2084 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 2084 svn:date "2008-10-22T19:11:03.922246Z" $REPO_URL
$SVN_CMD --revprop -r 2084 svn:log "Include fact-finder-api as a GWT module to inhert when building GUI component" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2090 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2090 svn:date "2008-10-23T18:08:36.859543Z" $REPO_URL
$SVN_CMD --revprop -r 2090 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2095 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2095 svn:date "2008-10-23T20:25:56.244998Z" $REPO_URL
$SVN_CMD --revprop -r 2095 svn:log "Changed FactStructure to FactTypeKey in BusinessRuleType" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2099 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2099 svn:date "2008-10-23T20:55:55.755893Z" $REPO_URL
$SVN_CMD --revprop -r 2099 svn:log "Added JAX-WS map adapters" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2111 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2111 svn:date "2008-10-24T17:40:00.890707Z" $REPO_URL
$SVN_CMD --revprop -r 2111 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2122 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2122 svn:date "2008-10-24T20:46:11.146865Z" $REPO_URL
$SVN_CMD --revprop -r 2122 svn:log "Created more YVF propositions and added test cases" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2123 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2123 svn:date "2008-10-24T21:07:52.119762Z" $REPO_URL
$SVN_CMD --revprop -r 2123 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2127 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2127 svn:date "2008-10-24T21:54:10.001033Z" $REPO_URL
$SVN_CMD --revprop -r 2127 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2128 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2128 svn:date "2008-10-24T21:54:27.237906Z" $REPO_URL
$SVN_CMD --revprop -r 2128 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2152 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2152 svn:date "2008-10-29T17:24:14.093058Z" $REPO_URL
$SVN_CMD --revprop -r 2152 svn:log "added for new design" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2172 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2172 svn:date "2008-10-31T20:55:21.608931Z" $REPO_URL
$SVN_CMD --revprop -r 2172 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2176 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2176 svn:date "2008-11-04T01:09:43.055206Z" $REPO_URL
$SVN_CMD --revprop -r 2176 svn:log "Added new simple comparable YVF proposition and added/changed FactContainer id and anchor" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2184 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2184 svn:date "2008-11-04T18:47:51.954491Z" $REPO_URL
$SVN_CMD --revprop -r 2184 svn:log "meta list" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2218 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2218 svn:date "2008-11-07T00:40:28.502629Z" $REPO_URL
$SVN_CMD --revprop -r 2218 svn:log "New dtos and changes to the dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2219 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2219 svn:date "2008-11-07T02:16:26.453853Z" $REPO_URL
$SVN_CMD --revprop -r 2219 svn:log "Refactored rule execution environment" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2223 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2223 svn:date "2008-11-07T02:23:26.012265Z" $REPO_URL
$SVN_CMD --revprop -r 2223 svn:log "Added rule management, rule repository and rule execution integration tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2232 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2232 svn:date "2008-11-07T20:28:17.142504Z" $REPO_URL
$SVN_CMD --revprop -r 2232 svn:log "New jaxws files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2233 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2233 svn:date "2008-11-07T20:28:40.808383Z" $REPO_URL
$SVN_CMD --revprop -r 2233 svn:log "new jaxws files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2242 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2242 svn:date "2008-11-10T15:50:48.898960Z" $REPO_URL
$SVN_CMD --revprop -r 2242 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2246 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2246 svn:date "2008-11-10T16:20:46.523911Z" $REPO_URL
$SVN_CMD --revprop -r 2246 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2267 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2267 svn:date "2008-11-10T23:23:08.825822Z" $REPO_URL
$SVN_CMD --revprop -r 2267 svn:log "wsdl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2268 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2268 svn:date "2008-11-10T23:24:51.278760Z" $REPO_URL
$SVN_CMD --revprop -r 2268 svn:log "generated java files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2286 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2286 svn:date "2008-11-11T18:30:13.543950Z" $REPO_URL
$SVN_CMD --revprop -r 2286 svn:log "Add rice-extra module for workflow utility services" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2288 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2288 svn:date "2008-11-11T20:12:29.816406Z" $REPO_URL
$SVN_CMD --revprop -r 2288 svn:log "Added spring injection of gwt servlets" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2292 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2292 svn:date "2008-11-12T18:43:21.786383Z" $REPO_URL
$SVN_CMD --revprop -r 2292 svn:log "Added gwt config files that do not define any servers" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2300 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2300 svn:date "2008-11-13T03:09:30.966873Z" $REPO_URL
$SVN_CMD --revprop -r 2300 svn:log "Implemented agenda rule execution (executeAgenda) and did some cleanup and refactoring." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2312 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2312 svn:date "2008-11-13T19:41:15.420144Z" $REPO_URL
$SVN_CMD --revprop -r 2312 svn:log "Integrated with factfinder service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2315 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2315 svn:date "2008-11-13T19:42:40.245055Z" $REPO_URL
$SVN_CMD --revprop -r 2315 svn:log "Implementation for findFactTypes and fetchFactType" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2316 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2316 svn:date "2008-11-13T19:42:59.436173Z" $REPO_URL
$SVN_CMD --revprop -r 2316 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2324 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2324 svn:date "2008-11-13T19:55:48.267965Z" $REPO_URL
$SVN_CMD --revprop -r 2324 svn:log "Initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2327 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2327 svn:date "2008-11-13T20:15:12.908782Z" $REPO_URL
$SVN_CMD --revprop -r 2327 svn:log "added some common dtos" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2332 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2332 svn:date "2008-11-13T20:50:34.549276Z" $REPO_URL
$SVN_CMD --revprop -r 2332 svn:log "added exception" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2343 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2343 svn:date "2008-11-13T21:57:15.554614Z" $REPO_URL
$SVN_CMD --revprop -r 2343 svn:log "added organization service/dtos" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2369 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2369 svn:date "2008-11-14T15:47:22.512669Z" $REPO_URL
$SVN_CMD --revprop -r 2369 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2371 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2371 svn:date "2008-11-14T15:56:31.032158Z" $REPO_URL
$SVN_CMD --revprop -r 2371 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2395 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2395 svn:date "2008-11-15T01:44:27.034718Z" $REPO_URL
$SVN_CMD --revprop -r 2395 svn:log "1. Switched to using test-beans.xml in rule-execution and integration-tests. 
2. Cleaned up bad data in test-beans.xml so beans can be persisted
3. Cleaned up rule execution" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2397 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2397 svn:date "2008-11-15T01:56:00.624722Z" $REPO_URL
$SVN_CMD --revprop -r 2397 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2398 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2398 svn:date "2008-11-15T01:56:29.690068Z" $REPO_URL
$SVN_CMD --revprop -r 2398 svn:log "Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2403 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2403 svn:date "2008-11-15T18:36:54.494402Z" $REPO_URL
$SVN_CMD --revprop -r 2403 svn:log "Added Entity to store LUIPerson for fact sample data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2418 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2418 svn:date "2008-11-17T21:29:04.773116Z" $REPO_URL
$SVN_CMD --revprop -r 2418 svn:log "Added implementation, test data and test cases for fetchFact service call" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2423 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2423 svn:date "2008-11-17T21:38:33.759019Z" $REPO_URL
$SVN_CMD --revprop -r 2423 svn:log "Added implementation, test data and test cases for fetchFact service call" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2428 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2428 svn:date "2008-11-18T01:24:53.939871Z" $REPO_URL
$SVN_CMD --revprop -r 2428 svn:log "web module setup" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2438 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2438 svn:date "2008-11-18T18:51:29.772853Z" $REPO_URL
$SVN_CMD --revprop -r 2438 svn:log "Add qualifier role type service Rich Diaz wrote." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2445 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2445 svn:date "2008-11-18T22:03:13.216113Z" $REPO_URL
$SVN_CMD --revprop -r 2445 svn:log "refactored exceptions to core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2446 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2446 svn:date "2008-11-18T22:06:33.606194Z" $REPO_URL
$SVN_CMD --revprop -r 2446 svn:log "added jaxws beans for dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2447 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2447 svn:date "2008-11-18T22:11:04.853689Z" $REPO_URL
$SVN_CMD --revprop -r 2447 svn:log "added jaxws beans for search" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2454 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2454 svn:date "2008-11-19T01:39:57.735406Z" $REPO_URL
$SVN_CMD --revprop -r 2454 svn:log "web module config" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2467 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2467 svn:date "2008-11-19T16:02:20.358434Z" $REPO_URL
$SVN_CMD --revprop -r 2467 svn:log "added jaxws beans for atp" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2469 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2469 svn:date "2008-11-19T16:13:31.917771Z" $REPO_URL
$SVN_CMD --revprop -r 2469 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2501 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2501 svn:date "2008-11-20T15:17:37.072537Z" $REPO_URL
$SVN_CMD --revprop -r 2501 svn:log "Added @WebParam" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2503 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2503 svn:date "2008-11-20T15:18:27.522481Z" $REPO_URL
$SVN_CMD --revprop -r 2503 svn:log "Cleaning up Jaxws for rulemanagement" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2509 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2509 svn:date "2008-11-20T15:21:46.067586Z" $REPO_URL
$SVN_CMD --revprop -r 2509 svn:log "Merged context and properties file for rulemanagement and factfinder as per ks-poc" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2511 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2511 svn:date "2008-11-20T15:22:59.713205Z" $REPO_URL
$SVN_CMD --revprop -r 2511 svn:log "Merged context and properties file for rulemanagement and factfinder as per ks-poc" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2518 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2518 svn:date "2008-11-20T17:25:21.664650Z" $REPO_URL
$SVN_CMD --revprop -r 2518 svn:log "Added transactions.properties and cleaned up application-context.xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2531 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2531 svn:date "2008-11-20T22:24:44.705595Z" $REPO_URL
$SVN_CMD --revprop -r 2531 svn:log "Add ability to load service data using dao loader class." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2532 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2532 svn:date "2008-11-20T22:25:41.869299Z" $REPO_URL
$SVN_CMD --revprop -r 2532 svn:log "Add beans and service tests for QualifierHierarchyRoleTypeService" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2567 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2567 svn:date "2008-11-21T21:22:30.067235Z" $REPO_URL
$SVN_CMD --revprop -r 2567 svn:log "Started coding org entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2568 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2568 svn:date "2008-11-21T22:34:58.945363Z" $REPO_URL
$SVN_CMD --revprop -r 2568 svn:log "web xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2570 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 2570 svn:date "2008-11-21T22:46:55.039134Z" $REPO_URL
$SVN_CMD --revprop -r 2570 svn:log "wsdl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2620 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2620 svn:date "2008-12-01T05:16:17.820894Z" $REPO_URL
$SVN_CMD --revprop -r 2620 svn:log "Added fact-finder to rule-execution" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2624 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2624 svn:date "2008-12-02T00:06:43.762324Z" $REPO_URL
$SVN_CMD --revprop -r 2624 svn:log "Refactored rule set validator and translator" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2625 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2625 svn:date "2008-12-02T00:06:53.280919Z" $REPO_URL
$SVN_CMD --revprop -r 2625 svn:log "Refactored rule set validator and translator" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2644 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 2644 svn:date "2008-12-04T20:10:55.090651Z" $REPO_URL
$SVN_CMD --revprop -r 2644 svn:log "\\- added ability to invoke rule execution rule
\\- added static facts to rule execution test form" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2666 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2666 svn:date "2008-12-10T08:14:46.271096Z" $REPO_URL
$SVN_CMD --revprop -r 2666 svn:log "Integration service test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2667 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2667 svn:date "2008-12-10T08:16:32.069143Z" $REPO_URL
$SVN_CMD --revprop -r 2667 svn:log "Added new integration unit test cases using the brms-ws war file deploy to Jetty " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2669 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2669 svn:date "2008-12-10T17:15:56.594560Z" $REPO_URL
$SVN_CMD --revprop -r 2669 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2670 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2670 svn:date "2008-12-10T17:16:30.929499Z" $REPO_URL
$SVN_CMD --revprop -r 2670 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2672 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2672 svn:date "2008-12-10T17:16:51.274509Z" $REPO_URL
$SVN_CMD --revprop -r 2672 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2678 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2678 svn:date "2008-12-10T19:46:49.071635Z" $REPO_URL
$SVN_CMD --revprop -r 2678 svn:log "Added system properties" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2684 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2684 svn:date "2008-12-12T19:10:10.389327Z" $REPO_URL
$SVN_CMD --revprop -r 2684 svn:log "Changed structure" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2699 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2699 svn:date "2008-12-19T19:14:07.471893Z" $REPO_URL
$SVN_CMD --revprop -r 2699 svn:log "Validation stuff" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2700 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2700 svn:date "2008-12-19T19:15:17.014229Z" $REPO_URL
$SVN_CMD --revprop -r 2700 svn:log "Validation added" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2706 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2706 svn:date "2009-01-05T18:00:34.820305Z" $REPO_URL
$SVN_CMD --revprop -r 2706 svn:log "Update BRMS to conform to wiki definitions. 
Added createNewVersion service call" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2709 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 2709 svn:date "2009-01-05T19:44:25.708933Z" $REPO_URL
$SVN_CMD --revprop -r 2709 svn:log "Despring-ified " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2718 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2718 svn:date "2009-01-06T21:59:49.506193Z" $REPO_URL
$SVN_CMD --revprop -r 2718 svn:log "Added rulesnapshot back" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2752 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2752 svn:date "2009-01-13T19:11:38.786963Z" $REPO_URL
$SVN_CMD --revprop -r 2752 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2769 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 2769 svn:date "2009-01-14T20:21:11.741792Z" $REPO_URL
$SVN_CMD --revprop -r 2769 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2784 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2784 svn:date "2009-01-16T06:28:25.110319Z" $REPO_URL
$SVN_CMD --revprop -r 2784 svn:log "Added updateBusinessRuleState" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2792 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2792 svn:date "2009-01-16T16:34:49.555563Z" $REPO_URL
$SVN_CMD --revprop -r 2792 svn:log "Initial import of organization service and reloaded definitions for search/enumeration and dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2799 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2799 svn:date "2009-01-16T17:23:53.720739Z" $REPO_URL
$SVN_CMD --revprop -r 2799 svn:log "Updated data bean with the changes to BRMS entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2801 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2801 svn:date "2009-01-16T17:24:31.638630Z" $REPO_URL
$SVN_CMD --revprop -r 2801 svn:log "Inserting a log4j file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2808 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2808 svn:date "2009-01-16T18:16:09.486100Z" $REPO_URL
$SVN_CMD --revprop -r 2808 svn:log "added jaxws for organization" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2809 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2809 svn:date "2009-01-16T18:35:56.052839Z" $REPO_URL
$SVN_CMD --revprop -r 2809 svn:log "Started adding org entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2810 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2810 svn:date "2009-01-16T19:23:35.214210Z" $REPO_URL
$SVN_CMD --revprop -r 2810 svn:log "Add Len's integration test runners and system properties" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2811 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2811 svn:date "2009-01-16T19:23:48.213805Z" $REPO_URL
$SVN_CMD --revprop -r 2811 svn:log "added more org entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2812 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2812 svn:date "2009-01-16T20:23:15.546746Z" $REPO_URL
$SVN_CMD --revprop -r 2812 svn:log "added more org entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2816 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2816 svn:date "2009-01-16T21:52:17.895450Z" $REPO_URL
$SVN_CMD --revprop -r 2816 svn:log "Add/update dictionary service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2818 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2818 svn:date "2009-01-16T22:01:51.481334Z" $REPO_URL
$SVN_CMD --revprop -r 2818 svn:log "Add dictionary resources" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2826 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2826 svn:date "2009-01-20T04:02:29.997373Z" $REPO_URL
$SVN_CMD --revprop -r 2826 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2839 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2839 svn:date "2009-01-20T21:07:17.667320Z" $REPO_URL
$SVN_CMD --revprop -r 2839 svn:log "Added translationKeys for the FactResult in FactStructureDTO" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2847 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 2847 svn:date "2009-01-21T16:08:47.167153Z" $REPO_URL
$SVN_CMD --revprop -r 2847 svn:log "Added translationKeys for the FactResult in FactStructureDTO" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2854 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2854 svn:date "2009-01-21T18:40:05.096595Z" $REPO_URL
$SVN_CMD --revprop -r 2854 svn:log "updated pom to generate wsdl for dict and org services and updated wsdls" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2855 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2855 svn:date "2009-01-21T18:41:08.333541Z" $REPO_URL
$SVN_CMD --revprop -r 2855 svn:log "added .settings" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2857 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2857 svn:date "2009-01-21T21:12:03.131898Z" $REPO_URL
$SVN_CMD --revprop -r 2857 svn:log "Added PositionRestriction entity" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2863 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2863 svn:date "2009-01-22T15:48:17.072184Z" $REPO_URL
$SVN_CMD --revprop -r 2863 svn:log "Add @WebFault annotation to exceptions. Add missing exception beans and fix exception namespace." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2883 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2883 svn:date "2009-01-23T22:01:59.839827Z" $REPO_URL
$SVN_CMD --revprop -r 2883 svn:log "began adding gwt stuff" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2896 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2896 svn:date "2009-01-26T21:11:57.349425Z" $REPO_URL
$SVN_CMD --revprop -r 2896 svn:log "Migrated enumeration service into core
Got rid of some warnings" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2897 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2897 svn:date "2009-01-26T21:13:02.751398Z" $REPO_URL
$SVN_CMD --revprop -r 2897 svn:log "Migrated enumeration service into core
Got rid of some warnings" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2903 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2903 svn:date "2009-01-26T22:39:35.996195Z" $REPO_URL
$SVN_CMD --revprop -r 2903 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2909 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2909 svn:date "2009-01-26T22:45:50.586444Z" $REPO_URL
$SVN_CMD --revprop -r 2909 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2910 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2910 svn:date "2009-01-26T22:45:57.039131Z" $REPO_URL
$SVN_CMD --revprop -r 2910 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2915 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2915 svn:date "2009-01-27T14:32:54.471000Z" $REPO_URL
$SVN_CMD --revprop -r 2915 svn:log "Fixed gwt compile sources missing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2916 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2916 svn:date "2009-01-27T14:40:22.178860Z" $REPO_URL
$SVN_CMD --revprop -r 2916 svn:log "added loading graphic" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2918 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2918 svn:date "2009-01-27T15:37:26.401343Z" $REPO_URL
$SVN_CMD --revprop -r 2918 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2919 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 2919 svn:date "2009-01-27T16:16:19.436239Z" $REPO_URL
$SVN_CMD --revprop -r 2919 svn:log "Fixed wrappers for spring beans" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2923 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2923 svn:date "2009-01-28T01:43:35.221923Z" $REPO_URL
$SVN_CMD --revprop -r 2923 svn:log "Added RuleReport which has a list of PropositionReport and modified propositions to get facts used" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2924 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 2924 svn:date "2009-01-28T01:43:52.670098Z" $REPO_URL
$SVN_CMD --revprop -r 2924 svn:log "Added RuleReport which has a list of PropositionReport and modified propositions to get facts used" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2927 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2927 svn:date "2009-01-28T16:09:14.801054Z" $REPO_URL
$SVN_CMD --revprop -r 2927 svn:log "Add beginnings of UI for org" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 2949 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 2949 svn:date "2009-01-29T21:51:59.949537Z" $REPO_URL
$SVN_CMD --revprop -r 2949 svn:log "Fix namespace issue for search dto. Add package.info for search dtos, so they don't get included in implementing service's dto namespace. Add namespace to dictionary FieldDescriptor dto used in search QueryParamInfo dto." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3002 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3002 svn:date "2009-02-03T15:59:07.481626Z" $REPO_URL
$SVN_CMD --revprop -r 3002 svn:log "added servletwrapping controller and spring-webmvc dependency" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3013 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 3013 svn:date "2009-02-03T23:13:41.384874Z" $REPO_URL
$SVN_CMD --revprop -r 3013 svn:log "Rules version tab changes." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3019 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3019 svn:date "2009-02-04T14:14:33.330475Z" $REPO_URL
$SVN_CMD --revprop -r 3019 svn:log "Add core-web-bundle module to build services & app ui in one war." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3057 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3057 svn:date "2009-02-06T16:20:27.970534Z" $REPO_URL
$SVN_CMD --revprop -r 3057 svn:log "Added get OrgTree service method" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3082 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3082 svn:date "2009-02-10T15:24:34.997436Z" $REPO_URL
$SVN_CMD --revprop -r 3082 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3083 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3083 svn:date "2009-02-10T15:25:27.662807Z" $REPO_URL
$SVN_CMD --revprop -r 3083 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3086 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3086 svn:date "2009-02-10T18:09:29.595183Z" $REPO_URL
$SVN_CMD --revprop -r 3086 svn:log "Added some entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3096 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3096 svn:date "2009-02-11T15:18:01.643908Z" $REPO_URL
$SVN_CMD --revprop -r 3096 svn:log "Added sql load listener" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3101 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 3101 svn:date "2009-02-11T16:50:26.783184Z" $REPO_URL
$SVN_CMD --revprop -r 3101 svn:log "Added a web method which retrieves only minimal rule data for GUI tree." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3122 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3122 svn:date "2009-02-12T03:53:31.469940Z" $REPO_URL
$SVN_CMD --revprop -r 3122 svn:log "Refactored rule report generation and rule execution" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3129 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 3129 svn:date "2009-02-12T16:04:06.056794Z" $REPO_URL
$SVN_CMD --revprop -r 3129 svn:log "initial commit of common-ui code being moved over from sandbox, still WIP" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3144 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3144 svn:date "2009-02-13T15:25:47.767466Z" $REPO_URL
$SVN_CMD --revprop -r 3144 svn:log "Created enumerable interface and removed enum method from dictionary service
updated ui click listeners to clickhandlers" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3148 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3148 svn:date "2009-02-13T18:20:45.362982Z" $REPO_URL
$SVN_CMD --revprop -r 3148 svn:log "Refactored enumerationservice->enumerationmanagement" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3153 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3153 svn:date "2009-02-13T21:18:19.742632Z" $REPO_URL
$SVN_CMD --revprop -r 3153 svn:log "Refactor seperate message project into ks-core-message
renamed MessageService namespace to be consistent with ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3155 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3155 svn:date "2009-02-14T00:33:32.183295Z" $REPO_URL
$SVN_CMD --revprop -r 3155 svn:log "Refactor seperate message project into ks-core-message
renamed MessageService namespace to be consistent with ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3157 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3157 svn:date "2009-02-14T01:42:09.797009Z" $REPO_URL
$SVN_CMD --revprop -r 3157 svn:log "Refactor seperate message project into ks-core-message
renamed MessageService namespace to be consistent with ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3165 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3165 svn:date "2009-02-16T16:28:59.574742Z" $REPO_URL
$SVN_CMD --revprop -r 3165 svn:log "Add empty web.xml for testing common-ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3175 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3175 svn:date "2009-02-17T15:52:45.253315Z" $REPO_URL
$SVN_CMD --revprop -r 3175 svn:log "Separate out module for testing of common ui widgets from the the actual common module. Ideally these should go in test directory." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3177 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3177 svn:date "2009-02-17T16:29:17.774021Z" $REPO_URL
$SVN_CMD --revprop -r 3177 svn:log "Added Lu service api initial" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3186 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3186 svn:date "2009-02-17T21:14:57.790565Z" $REPO_URL
$SVN_CMD --revprop -r 3186 svn:log "added jaxws wrapper beans" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3187 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3187 svn:date "2009-02-17T21:15:19.419223Z" $REPO_URL
$SVN_CMD --revprop -r 3187 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3188 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3188 svn:date "2009-02-17T21:16:44.406456Z" $REPO_URL
$SVN_CMD --revprop -r 3188 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3189 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3189 svn:date "2009-02-17T21:16:59.363181Z" $REPO_URL
$SVN_CMD --revprop -r 3189 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3190 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3190 svn:date "2009-02-17T21:17:12.301985Z" $REPO_URL
$SVN_CMD --revprop -r 3190 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3191 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3191 svn:date "2009-02-17T21:17:26.602881Z" $REPO_URL
$SVN_CMD --revprop -r 3191 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3192 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3192 svn:date "2009-02-17T21:17:43.684004Z" $REPO_URL
$SVN_CMD --revprop -r 3192 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3193 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3193 svn:date "2009-02-17T21:17:59.845857Z" $REPO_URL
$SVN_CMD --revprop -r 3193 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3199 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3199 svn:date "2009-02-17T23:53:28.274165Z" $REPO_URL
$SVN_CMD --revprop -r 3199 svn:log "Add more common widgets" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3204 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 3204 svn:date "2009-02-18T19:31:16.695498Z" $REPO_URL
$SVN_CMD --revprop -r 3204 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3207 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 3207 svn:date "2009-02-18T19:37:13.142189Z" $REPO_URL
$SVN_CMD --revprop -r 3207 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3217 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 3217 svn:date "2009-02-18T22:20:32.458526Z" $REPO_URL
$SVN_CMD --revprop -r 3217 svn:log "Changed modules to use project parent and added site." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3230 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3230 svn:date "2009-02-19T15:13:27.546022Z" $REPO_URL
$SVN_CMD --revprop -r 3230 svn:log "Took out create methods from dictionary object factory that were no longer in that package
moved search for results to search service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3244 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3244 svn:date "2009-02-19T20:01:17.413620Z" $REPO_URL
$SVN_CMD --revprop -r 3244 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3246 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3246 svn:date "2009-02-19T20:03:21.615975Z" $REPO_URL
$SVN_CMD --revprop -r 3246 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3253 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 3253 svn:date "2009-02-19T22:09:57.710352Z" $REPO_URL
$SVN_CMD --revprop -r 3253 svn:log "making entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3255 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3255 svn:date "2009-02-19T22:16:56.774278Z" $REPO_URL
$SVN_CMD --revprop -r 3255 svn:log "Add launche config for kitchen sink" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3257 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3257 svn:date "2009-02-19T23:00:22.326320Z" $REPO_URL
$SVN_CMD --revprop -r 3257 svn:log "Add more widgets
Switch main page to use KSAccordionPanel" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3262 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3262 svn:date "2009-02-20T14:20:45.498808Z" $REPO_URL
$SVN_CMD --revprop -r 3262 svn:log "Add selectable table list and add selection change handler." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3267 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3267 svn:date "2009-02-20T15:54:55.000080Z" $REPO_URL
$SVN_CMD --revprop -r 3267 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3268 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3268 svn:date "2009-02-20T16:01:48.295583Z" $REPO_URL
$SVN_CMD --revprop -r 3268 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3273 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3273 svn:date "2009-02-20T17:32:38.215276Z" $REPO_URL
$SVN_CMD --revprop -r 3273 svn:log "Refactored type so there are no type attributes and type is an abstract mapped superclass" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3275 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3275 svn:date "2009-02-20T19:02:22.545393Z" $REPO_URL
$SVN_CMD --revprop -r 3275 svn:log "Ad launch config for CommonUITest module." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3286 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3286 svn:date "2009-02-20T20:30:46.713225Z" $REPO_URL
$SVN_CMD --revprop -r 3286 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3287 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3287 svn:date "2009-02-20T20:35:35.413160Z" $REPO_URL
$SVN_CMD --revprop -r 3287 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3295 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 3295 svn:date "2009-02-20T21:24:09.330026Z" $REPO_URL
$SVN_CMD --revprop -r 3295 svn:log "new adds" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3296 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3296 svn:date "2009-02-20T21:28:49.038521Z" $REPO_URL
$SVN_CMD --revprop -r 3296 svn:log "abstract KSDatePicker" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3299 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3299 svn:date "2009-02-20T21:53:44.235010Z" $REPO_URL
$SVN_CMD --revprop -r 3299 svn:log "abstract KSDisclosureSection" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3300 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3300 svn:date "2009-02-21T00:38:15.840353Z" $REPO_URL
$SVN_CMD --revprop -r 3300 svn:log "abstract KSDropDown" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3304 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3304 svn:date "2009-02-21T02:42:11.407680Z" $REPO_URL
$SVN_CMD --revprop -r 3304 svn:log "abstract KSListBox KSRadioButton" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3305 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3305 svn:date "2009-02-21T03:15:22.869420Z" $REPO_URL
$SVN_CMD --revprop -r 3305 svn:log "abstract KSStackPanel KSTextBox" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3306 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3306 svn:date "2009-02-22T23:35:16.567924Z" $REPO_URL
$SVN_CMD --revprop -r 3306 svn:log "abstract widget " $REPO_URL
$SVN_CMD --revprop -r 3329 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3329 svn:date "2009-02-24T18:34:27.389769Z" $REPO_URL
$SVN_CMD --revprop -r 3329 svn:log "Implement FormLayoutPanel using \"proxy\" to allow impl to be replacable at compile time." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3334 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 3334 svn:date "2009-02-24T18:57:18.355739Z" $REPO_URL
$SVN_CMD --revprop -r 3334 svn:log "Moved rules dsl dto from ks-core to ks-lum. Also updated the generated jaxws classes and LuService.
StatementOperatorTypeKey enum added and integrated with DTO" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3341 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 3341 svn:date "2009-02-24T21:40:14.939367Z" $REPO_URL
$SVN_CMD --revprop -r 3341 svn:log "Added sandbox for initial work on LUM GUI before it will be moved into ks-lum project." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3345 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 3345 svn:date "2009-02-24T23:01:52.326748Z" $REPO_URL
$SVN_CMD --revprop -r 3345 svn:log "Rescrubbed OrganizationService to generate DTOs.
  Tagged classes that:
     have a 'key' or 'id' element as implementing Idable
     have 'type' and 'state' elements as implementing HasTypeState
     have attributes as implementing HasAttributes
  Changed 'key' in contracts to 'id' in DTO's and some entities that
     hadn't already been changed
  Org's 'desc' field was replaced with 'shortDesc' and 'longDesc'; 
     updated sql files and code
  Cleaned up some warnings" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3355 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3355 svn:date "2009-02-25T15:45:00.387195Z" $REPO_URL
$SVN_CMD --revprop -r 3355 svn:log "Added some base dynamic search stuff" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3362 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 3362 svn:date "2009-02-25T19:53:10.589570Z" $REPO_URL
$SVN_CMD --revprop -r 3362 svn:log "Textbar images" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3363 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3363 svn:date "2009-02-25T19:55:28.728099Z" $REPO_URL
$SVN_CMD --revprop -r 3363 svn:log "Took out automatic import of *mock-service-context.xml and added an annotation so in your test you can explicitly say which context file you want loaded

Updated OrgService so dictionary and searchmanager are injected" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3369 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3369 svn:date "2009-02-25T21:04:58.569528Z" $REPO_URL
$SVN_CMD --revprop -r 3369 svn:log "Add HelpLink classes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3372 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 3372 svn:date "2009-02-25T21:57:48.758731Z" $REPO_URL
$SVN_CMD --revprop -r 3372 svn:log "Initial layout of the UI components for LUM main screen" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3378 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3378 svn:date "2009-02-26T01:15:29.441616Z" $REPO_URL
$SVN_CMD --revprop -r 3378 svn:log "Refactored proposition messaging and added velocity templates to rule proposition success and failure messages/natural language." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3381 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3381 svn:date "2009-02-26T09:30:27.551719Z" $REPO_URL
$SVN_CMD --revprop -r 3381 svn:log "Added Oracle and Derby server repository configurations. Updated test cases." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3392 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 3392 svn:date "2009-02-26T17:35:42.416016Z" $REPO_URL
$SVN_CMD --revprop -r 3392 svn:log "Synch up with DTOs for reqComponent and LuStatement" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3398 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3398 svn:date "2009-02-26T18:44:09.805080Z" $REPO_URL
$SVN_CMD --revprop -r 3398 svn:log "Add CollapsableFloatPanel widget" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3405 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3405 svn:date "2009-02-26T20:10:41.848246Z" $REPO_URL
$SVN_CMD --revprop -r 3405 svn:log "Added attributes and meta to clurelation entity" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3409 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 3409 svn:date "2009-02-26T20:50:40.042719Z" $REPO_URL
$SVN_CMD --revprop -r 3409 svn:log "Added panel switching." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3414 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 3414 svn:date "2009-02-26T22:03:57.175710Z" $REPO_URL
$SVN_CMD --revprop -r 3414 svn:log "Replace LumStackPanel with LumSwitchPanel." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3480 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3480 svn:date "2009-02-28T00:44:47.893725Z" $REPO_URL
$SVN_CMD --revprop -r 3480 svn:log "KSDatePicker, KSAccordionPanel Will G's way temp change to KSAccordianMenu returns KSAccordianPanelImpl to work with recursive calls in panel" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3483 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3483 svn:date "2009-03-01T22:22:56.030879Z" $REPO_URL
$SVN_CMD --revprop -r 3483 svn:log "KSHelpLink Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3484 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3484 svn:date "2009-03-01T23:10:06.563672Z" $REPO_URL
$SVN_CMD --revprop -r 3484 svn:log "KSRichEditor Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3485 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3485 svn:date "2009-03-01T23:59:03.307052Z" $REPO_URL
$SVN_CMD --revprop -r 3485 svn:log "KSRichEditorToolbar Will G's way
KSRichTextToolbar$Strings.properties should be deleted from svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3488 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 3488 svn:date "2009-03-02T18:57:31.508163Z" $REPO_URL
$SVN_CMD --revprop -r 3488 svn:log "Split up css" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3494 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 3494 svn:date "2009-03-02T19:43:58.154778Z" $REPO_URL
$SVN_CMD --revprop -r 3494 svn:log "dialog panel" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3505 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3505 svn:date "2009-03-03T00:17:38.136490Z" $REPO_URL
$SVN_CMD --revprop -r 3505 svn:log "Add individual css files for each example widget" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3508 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3508 svn:date "2009-03-03T01:23:24.264378Z" $REPO_URL
$SVN_CMD --revprop -r 3508 svn:log "Refactored rule message/report building" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3509 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3509 svn:date "2009-03-03T01:36:08.354604Z" $REPO_URL
$SVN_CMD --revprop -r 3509 svn:log "KSRadioButtonList KSSelectableTableList Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3515 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3515 svn:date "2009-03-03T08:23:19.904919Z" $REPO_URL
$SVN_CMD --revprop -r 3515 svn:log "Refactored message builders" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3530 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3530 svn:date "2009-03-03T21:05:45.317916Z" $REPO_URL
$SVN_CMD --revprop -r 3530 svn:log "KSAccordionPanel Will G's way " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3554 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 3554 svn:date "2009-03-04T17:43:53.999085Z" $REPO_URL
$SVN_CMD --revprop -r 3554 svn:log "New global image bundle" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3570 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3570 svn:date "2009-03-05T01:04:41.038579Z" $REPO_URL
$SVN_CMD --revprop -r 3570 svn:log "KSDialogPanel Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3576 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 3576 svn:date "2009-03-05T15:51:47.925047Z" $REPO_URL
$SVN_CMD --revprop -r 3576 svn:log "first commit of kspicklist skeleton" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3602 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3602 svn:date "2009-03-06T01:33:09.110967Z" $REPO_URL
$SVN_CMD --revprop -r 3602 svn:log "Added Oracle and Derby profiles to Maven POMS so test cases can be run against a Derby or Oracle database." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3605 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3605 svn:date "2009-03-06T01:40:16.783333Z" $REPO_URL
$SVN_CMD --revprop -r 3605 svn:log "KSPopupPanel Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3614 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 3614 svn:date "2009-03-06T19:51:54.586822Z" $REPO_URL
$SVN_CMD --revprop -r 3614 svn:log "\\- updates to the requirement component
- fixes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3622 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3622 svn:date "2009-03-06T23:41:33.062403Z" $REPO_URL
$SVN_CMD --revprop -r 3622 svn:log "KSDialogPanel KSHelpDialog KSInfoDialogPanel KSModalDialogPanel KSPopupPanel Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3623 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3623 svn:date "2009-03-07T00:09:34.985652Z" $REPO_URL
$SVN_CMD --revprop -r 3623 svn:log "KSConformationDialog Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3654 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3654 svn:date "2009-03-10T14:44:23.548357Z" $REPO_URL
$SVN_CMD --revprop -r 3654 svn:log "added search stuff implementation, but it still needs config" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3668 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3668 svn:date "2009-03-10T20:20:02.971102Z" $REPO_URL
$SVN_CMD --revprop -r 3668 svn:log "Add ProgressIndicator widget.
Update ToolTip descriptions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3687 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3687 svn:date "2009-03-11T17:08:10.367080Z" $REPO_URL
$SVN_CMD --revprop -r 3687 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3689 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3689 svn:date "2009-03-11T17:40:50.236659Z" $REPO_URL
$SVN_CMD --revprop -r 3689 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3690 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3690 svn:date "2009-03-11T17:41:48.920744Z" $REPO_URL
$SVN_CMD --revprop -r 3690 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3691 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3691 svn:date "2009-03-11T17:42:32.607434Z" $REPO_URL
$SVN_CMD --revprop -r 3691 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3692 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3692 svn:date "2009-03-11T17:43:02.091709Z" $REPO_URL
$SVN_CMD --revprop -r 3692 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3695 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 3695 svn:date "2009-03-11T18:03:10.501918Z" $REPO_URL
$SVN_CMD --revprop -r 3695 svn:log "first cut of live CSS editing in kitchen sink" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3704 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3704 svn:date "2009-03-11T20:59:22.656662Z" $REPO_URL
$SVN_CMD --revprop -r 3704 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3706 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3706 svn:date "2009-03-11T21:18:34.107976Z" $REPO_URL
$SVN_CMD --revprop -r 3706 svn:log "Added api for person service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3715 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3715 svn:date "2009-03-11T23:26:08.713825Z" $REPO_URL
$SVN_CMD --revprop -r 3715 svn:log "Added Antlr, Velocity and configuration files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3720 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3720 svn:date "2009-03-12T00:35:20.607709Z" $REPO_URL
$SVN_CMD --revprop -r 3720 svn:log "KSRezisablePanel Will G's way" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3733 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3733 svn:date "2009-03-12T18:03:23.750559Z" $REPO_URL
$SVN_CMD --revprop -r 3733 svn:log "Refactored org.kuali.student.rules to org.kuali.student.brms" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3735 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3735 svn:date "2009-03-12T18:29:16.678200Z" $REPO_URL
$SVN_CMD --revprop -r 3735 svn:log "moved repository exceptions back to impl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3739 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 3739 svn:date "2009-03-12T19:52:24.744545Z" $REPO_URL
$SVN_CMD --revprop -r 3739 svn:log "initial setup for lum-web war and supporting files." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3753 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3753 svn:date "2009-03-13T14:48:46.339891Z" $REPO_URL
$SVN_CMD --revprop -r 3753 svn:log "Added gwt person module" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3755 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3755 svn:date "2009-03-13T16:03:50.219735Z" $REPO_URL
$SVN_CMD --revprop -r 3755 svn:log "Added ORG ui person relation 
added Person service stub
fixed org assembler OrgPosition bug" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3757 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3757 svn:date "2009-03-13T16:04:31.984011Z" $REPO_URL
$SVN_CMD --revprop -r 3757 svn:log "Added ORG ui person relation 
added Person service stub
fixed org assembler OrgPosition bug" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3769 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3769 svn:date "2009-03-13T18:33:35.116240Z" $REPO_URL
$SVN_CMD --revprop -r 3769 svn:log "Updated Antlr boolean function parser/lexer and message builder" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3796 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3796 svn:date "2009-03-17T01:31:04.482244Z" $REPO_URL
$SVN_CMD --revprop -r 3796 svn:log "Add tabPanel with kitchen-sink example" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3807 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 3807 svn:date "2009-03-17T21:47:20.940762Z" $REPO_URL
$SVN_CMD --revprop -r 3807 svn:log "Initial version of rule-related component of LUM GUI." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3830 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3830 svn:date "2009-03-18T22:26:40.771878Z" $REPO_URL
$SVN_CMD --revprop -r 3830 svn:log "Add DialogPanel widget.
Clarify popup panel descriptions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3847 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3847 svn:date "2009-03-19T18:50:48.729226Z" $REPO_URL
$SVN_CMD --revprop -r 3847 svn:log "Change KSListBox to use KSSelectItemWidgetAbstract" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3858 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3858 svn:date "2009-03-20T14:34:24.499715Z" $REPO_URL
$SVN_CMD --revprop -r 3858 svn:log "Add checkbox list example." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3867 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3867 svn:date "2009-03-21T00:52:20.407281Z" $REPO_URL
$SVN_CMD --revprop -r 3867 svn:log "Factored out functions from propositions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3895 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3895 svn:date "2009-03-25T17:26:06.474920Z" $REPO_URL
$SVN_CMD --revprop -r 3895 svn:log "Add message service configuration for common-ui testing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3897 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3897 svn:date "2009-03-25T18:45:12.424983Z" $REPO_URL
$SVN_CMD --revprop -r 3897 svn:log "Add configuration for loading messages." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3908 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 3908 svn:date "2009-03-26T20:01:30.355263Z" $REPO_URL
$SVN_CMD --revprop -r 3908 svn:log "Add KSBreadcrumb widget" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3918 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3918 svn:date "2009-03-27T20:42:34.346697Z" $REPO_URL
$SVN_CMD --revprop -r 3918 svn:log "Moved execution to it's own module" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3919 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3919 svn:date "2009-03-27T20:43:47.031742Z" $REPO_URL
$SVN_CMD --revprop -r 3919 svn:log "missed a couple poms" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3921 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3921 svn:date "2009-03-28T20:48:17.130719Z" $REPO_URL
$SVN_CMD --revprop -r 3921 svn:log "closer to getting tests to pass" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3924 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3924 svn:date "2009-03-29T17:00:25.085626Z" $REPO_URL
$SVN_CMD --revprop -r 3924 svn:log "Missed a couple files in the last commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3928 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3928 svn:date "2009-03-30T18:07:33.609005Z" $REPO_URL
$SVN_CMD --revprop -r 3928 svn:log "Added web modules" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3931 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 3931 svn:date "2009-03-30T20:20:24.362621Z" $REPO_URL
$SVN_CMD --revprop -r 3931 svn:log "Updated LU Service to add the natural language methods.
Updated ReqComponentType to store the nlTemplate and corresponding test cases." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3935 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3935 svn:date "2009-03-31T13:38:33.456003Z" $REPO_URL
$SVN_CMD --revprop -r 3935 svn:log "Add interfaces for security context." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3938 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3938 svn:date "2009-03-31T14:33:25.543210Z" $REPO_URL
$SVN_CMD --revprop -r 3938 svn:log "Add requires authorization interface" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3942 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3942 svn:date "2009-03-31T15:27:56.666060Z" $REPO_URL
$SVN_CMD --revprop -r 3942 svn:log "combined properties into one file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3948 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3948 svn:date "2009-03-31T19:50:28.140503Z" $REPO_URL
$SVN_CMD --revprop -r 3948 svn:log "Ititial Atp import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3950 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 3950 svn:date "2009-04-01T17:55:15.647930Z" $REPO_URL
$SVN_CMD --revprop -r 3950 svn:log "Atp improvments" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3960 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 3960 svn:date "2009-04-02T03:09:53.306695Z" $REPO_URL
$SVN_CMD --revprop -r 3960 svn:log "forgot to add a few things in last commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3962 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3962 svn:date "2009-04-02T15:40:53.657791Z" $REPO_URL
$SVN_CMD --revprop -r 3962 svn:log "Add support for toggling edit mode of form field widget between editable, uneditable, and view only." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3966 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 3966 svn:date "2009-04-02T15:51:08.416356Z" $REPO_URL
$SVN_CMD --revprop -r 3966 svn:log "Initial check in for LUM requirements" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3983 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 3983 svn:date "2009-04-02T23:33:57.118825Z" $REPO_URL
$SVN_CMD --revprop -r 3983 svn:log "Copied over all new files from brm-impl-dev" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3986 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 3986 svn:date "2009-04-02T23:56:35.090249Z" $REPO_URL
$SVN_CMD --revprop -r 3986 svn:log "Add text counting label to text entry widgets and limit textbox and textarea widgets to maximum length" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 3990 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 3990 svn:date "2009-04-03T14:10:48.015638Z" $REPO_URL
$SVN_CMD --revprop -r 3990 svn:log "Add entrypoint and rpc service for course client." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4002 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4002 svn:date "2009-04-06T14:38:15.229470Z" $REPO_URL
$SVN_CMD --revprop -r 4002 svn:log "Add save event and handler" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4013 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4013 svn:date "2009-04-07T14:53:07.883467Z" $REPO_URL
$SVN_CMD --revprop -r 4013 svn:log "Updated lum-web to use one properties file, fixed context path for services" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4031 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 4031 svn:date "2009-04-08T00:35:37.788002Z" $REPO_URL
$SVN_CMD --revprop -r 4031 svn:log "LUMApplication MVC beginning work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4043 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 4043 svn:date "2009-04-09T15:00:37.125562Z" $REPO_URL
$SVN_CMD --revprop -r 4043 svn:log "added velocityTemplateEngine" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4047 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 4047 svn:date "2009-04-09T17:02:26.161395Z" $REPO_URL
$SVN_CMD --revprop -r 4047 svn:log "added velocitytemplateenginetest" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4051 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4051 svn:date "2009-04-09T18:24:49.643377Z" $REPO_URL
$SVN_CMD --revprop -r 4051 svn:log "Redesign course screens" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4114 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 4114 svn:date "2009-04-16T19:11:55.617177Z" $REPO_URL
$SVN_CMD --revprop -r 4114 svn:log "BRMS Refactor to match KS-LUM" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4117 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 4117 svn:date "2009-04-16T19:45:38.505621Z" $REPO_URL
$SVN_CMD --revprop -r 4117 svn:log "code cleanup" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4152 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 4152 svn:date "2009-04-20T00:12:59.000215Z" $REPO_URL
$SVN_CMD --revprop -r 4152 svn:log "PageTable added to ScrollTable" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4155 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4155 svn:date "2009-04-20T14:49:48.549136Z" $REPO_URL
$SVN_CMD --revprop -r 4155 svn:log "Add dirty state handlers to form" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4158 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 4158 svn:date "2009-04-20T15:41:48.531241Z" $REPO_URL
$SVN_CMD --revprop -r 4158 svn:log "initial commit of configurable UI layout, still needs work to finish tying in with dictionary, etc" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4161 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4161 svn:date "2009-04-20T20:57:19.254173Z" $REPO_URL
$SVN_CMD --revprop -r 4161 svn:log "Add gwt.xml files for common util & validator and add gwtx depdendency (hopefully 1.5.3 works until 1.6 version released). " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4174 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 4174 svn:date "2009-04-21T17:39:25.117088Z" $REPO_URL
$SVN_CMD --revprop -r 4174 svn:log "reworked screen layouts and finish work on req. components" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4176 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 4176 svn:date "2009-04-21T18:32:52.298606Z" $REPO_URL
$SVN_CMD --revprop -r 4176 svn:log "table editor" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4180 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 4180 svn:date "2009-04-21T20:00:41.692255Z" $REPO_URL
$SVN_CMD --revprop -r 4180 svn:log "Kuali Student Rice Standalone Server Launcher Project initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4182 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 4182 svn:date "2009-04-21T20:21:30.722908Z" $REPO_URL
$SVN_CMD --revprop -r 4182 svn:log "Allow breadcrumb and links to be switched on and off" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4184 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 4184 svn:date "2009-04-21T20:34:28.813491Z" $REPO_URL
$SVN_CMD --revprop -r 4184 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4191 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 4191 svn:date "2009-04-21T23:08:58.783376Z" $REPO_URL
$SVN_CMD --revprop -r 4191 svn:log "Learning result: Generated files from service contract" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4198 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 4198 svn:date "2009-04-22T16:54:21.685695Z" $REPO_URL
$SVN_CMD --revprop -r 4198 svn:log "Kuali Student Rice Standalone Server Launcher Project initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4201 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4201 svn:date "2009-04-22T17:07:29.188457Z" $REPO_URL
$SVN_CMD --revprop -r 4201 svn:log "Remove hard-coding of context file from dictionary implementation." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4209 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 4209 svn:date "2009-04-22T18:51:52.693805Z" $REPO_URL
$SVN_CMD --revprop -r 4209 svn:log "Made UI components in Search View to be displayed in a KSModalDialogPanel instead." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4217 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 4217 svn:date "2009-04-22T23:39:37.831454Z" $REPO_URL
$SVN_CMD --revprop -r 4217 svn:log "Scroll and Paging tables" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4227 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4227 svn:date "2009-04-23T17:25:16.216247Z" $REPO_URL
$SVN_CMD --revprop -r 4227 svn:log "Copy over lum config examples from ks-core-ui." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4230 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4230 svn:date "2009-04-23T17:50:33.436452Z" $REPO_URL
$SVN_CMD --revprop -r 4230 svn:log "Add uncommitted work for thin slice for course proposal screens. (Most to be replaced by new configurable screens)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4253 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 4253 svn:date "2009-04-24T21:42:08.323583Z" $REPO_URL
$SVN_CMD --revprop -r 4253 svn:log "Learning result: Created entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4264 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 4264 svn:date "2009-04-27T21:55:43.268256Z" $REPO_URL
$SVN_CMD --revprop -r 4264 svn:log "Add logic for LU type/state detection" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4275 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4275 svn:date "2009-04-28T20:16:07.855422Z" $REPO_URL
$SVN_CMD --revprop -r 4275 svn:log "Add launch script" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4309 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 4309 svn:date "2009-05-01T12:01:49.603482Z" $REPO_URL
$SVN_CMD --revprop -r 4309 svn:log "added site configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4312 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 4312 svn:date "2009-05-02T00:32:41.720420Z" $REPO_URL
$SVN_CMD --revprop -r 4312 svn:log "Scroll and Paging tables showing selected rows" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4328 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 4328 svn:date "2009-05-06T06:40:18.886190Z" $REPO_URL
$SVN_CMD --revprop -r 4328 svn:log "initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4368 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 4368 svn:date "2009-05-08T01:11:04.219990Z" $REPO_URL
$SVN_CMD --revprop -r 4368 svn:log "LUM UI and Web integrated - Core UI started" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4379 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 4379 svn:date "2009-05-08T19:06:43.513812Z" $REPO_URL
$SVN_CMD --revprop -r 4379 svn:log "initial commit of separate ks-core web module using KSB" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4390 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 4390 svn:date "2009-05-09T00:07:16.498782Z" $REPO_URL
$SVN_CMD --revprop -r 4390 svn:log "update to get the project working with GWT using the KSB deployed service internally" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4395 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 4395 svn:date "2009-05-11T10:32:15.230272Z" $REPO_URL
$SVN_CMD --revprop -r 4395 svn:log "Learning result: ResultComponent/inheritance,  Scale, " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4397 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4397 svn:date "2009-05-11T14:27:04.704913Z" $REPO_URL
$SVN_CMD --revprop -r 4397 svn:log "Added ability to use ServiceTestClass runner without @Daos defined" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4417 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 4417 svn:date "2009-05-12T15:51:42.655380Z" $REPO_URL
$SVN_CMD --revprop -r 4417 svn:log "working deployment of core UI and LUM UI" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4423 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 4423 svn:date "2009-05-12T18:19:32.232057Z" $REPO_URL
$SVN_CMD --revprop -r 4423 svn:log "added launch for LUM Requirements screen" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4429 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4429 svn:date "2009-05-12T20:43:13.731973Z" $REPO_URL
$SVN_CMD --revprop -r 4429 svn:log "Added missing wrappers, deleted some that weren't needed anymore" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4494 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4494 svn:date "2009-05-15T17:25:47.249622Z" $REPO_URL
$SVN_CMD --revprop -r 4494 svn:log "Re scraped enumeration management.  This service needs some overhauling as to how it relates to the dictionary." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4527 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4527 svn:date "2009-05-19T21:11:51.082451Z" $REPO_URL
$SVN_CMD --revprop -r 4527 svn:log "Split out the advanced search window into a two versions, one using a confirm dialog, and another as a standalone panel." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4566 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 4566 svn:date "2009-05-22T06:04:39.971610Z" $REPO_URL
$SVN_CMD --revprop -r 4566 svn:log "Added LU statement type header template" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4580 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4580 svn:date "2009-05-25T22:20:51.346418Z" $REPO_URL
$SVN_CMD --revprop -r 4580 svn:log "Add proposal structures & rpc service interface." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4583 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4583 svn:date "2009-05-25T23:38:21.889721Z" $REPO_URL
$SVN_CMD --revprop -r 4583 svn:log "Add clu proposal impl." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4597 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 4597 svn:date "2009-05-27T20:39:35.567999Z" $REPO_URL
$SVN_CMD --revprop -r 4597 svn:log "LCS - initial move of brms-ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4606 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4606 svn:date "2009-05-29T14:53:48.326842Z" $REPO_URL
$SVN_CMD --revprop -r 4606 svn:log "Proposal Service DTO" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4613 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 4613 svn:date "2009-05-31T17:05:18.773968Z" $REPO_URL
$SVN_CMD --revprop -r 4613 svn:log "Added embedded API for all BRMS service (fact finder, rule management, rule repository and rule execution)" $REPO_URL
$SVN_CMD --revprop -r 4634 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 4634 svn:date "2009-06-02T21:12:28.569485Z" $REPO_URL
$SVN_CMD --revprop -r 4634 svn:log "Added translation language (e.g. \"en\", \"de\", etc.) to getNaturalLanguage service methods and Javadocs" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4654 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 4654 svn:date "2009-06-03T23:39:30.314051Z" $REPO_URL
$SVN_CMD --revprop -r 4654 svn:log "index jsp page for RPC decoding and encoding" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4678 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4678 svn:date "2009-06-05T14:38:55.108272Z" $REPO_URL
$SVN_CMD --revprop -r 4678 svn:log "Initial import authorization/permissions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4683 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4683 svn:date "2009-06-05T18:34:48.534276Z" $REPO_URL
$SVN_CMD --revprop -r 4683 svn:log "Comment Service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4686 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4686 svn:date "2009-06-05T19:18:34.351437Z" $REPO_URL
$SVN_CMD --revprop -r 4686 svn:log "Added security to lum-web project (just name and password need to match)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4691 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4691 svn:date "2009-06-05T19:42:46.917489Z" $REPO_URL
$SVN_CMD --revprop -r 4691 svn:log "Comment Service Wrappers" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4696 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4696 svn:date "2009-06-05T20:44:14.342502Z" $REPO_URL
$SVN_CMD --revprop -r 4696 svn:log "Updated context to use cluproposal servlet" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4698 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 4698 svn:date "2009-06-05T21:04:47.675107Z" $REPO_URL
$SVN_CMD --revprop -r 4698 svn:log "Comment Service: Set up the  infrastructure for CommentService implementation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4704 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 4704 svn:date "2009-06-06T00:32:01.487338Z" $REPO_URL
$SVN_CMD --revprop -r 4704 svn:log "Added complete rule execution test cases" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4709 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 4709 svn:date "2009-06-07T08:30:09.192169Z" $REPO_URL
$SVN_CMD --revprop -r 4709 svn:log "Comment Service: getComment" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4716 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4716 svn:date "2009-06-08T18:15:35.493972Z" $REPO_URL
$SVN_CMD --revprop -r 4716 svn:log "Comment Service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4720 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 4720 svn:date "2009-06-08T19:22:41.668774Z" $REPO_URL
$SVN_CMD --revprop -r 4720 svn:log "\\- refactor code and updated gui per wireframes. added initial search widget (hidden)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4750 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4750 svn:date "2009-06-10T19:04:02.389039Z" $REPO_URL
$SVN_CMD --revprop -r 4750 svn:log "Comment Service Reference Entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4781 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 4781 svn:date "2009-06-11T21:47:14.736198Z" $REPO_URL
$SVN_CMD --revprop -r 4781 svn:log "blue gradient" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4788 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4788 svn:date "2009-06-12T14:22:59.524340Z" $REPO_URL
$SVN_CMD --revprop -r 4788 svn:log "Add event state and event state callback for events." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4789 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4789 svn:date "2009-06-12T14:25:16.524442Z" $REPO_URL
$SVN_CMD --revprop -r 4789 svn:log "Add controllers to LUM views, add ability to add external buttons to default layout, and add workflow event." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4791 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 4791 svn:date "2009-06-12T17:28:21.308322Z" $REPO_URL
$SVN_CMD --revprop -r 4791 svn:log "init checking for nested sections" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4801 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 4801 svn:date "2009-06-12T23:40:05.091067Z" $REPO_URL
$SVN_CMD --revprop -r 4801 svn:log "Incubator PagingScrollTable for SelectableTable, PickList,SuggestBox" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4827 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4827 svn:date "2009-06-16T18:10:14.142820Z" $REPO_URL
$SVN_CMD --revprop -r 4827 svn:log "Initial import of scraped document service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4828 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4828 svn:date "2009-06-16T19:00:12.414986Z" $REPO_URL
$SVN_CMD --revprop -r 4828 svn:log "added wrappers and wsdl generation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4829 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 4829 svn:date "2009-06-16T23:09:49.116020Z" $REPO_URL
$SVN_CMD --revprop -r 4829 svn:log "Re-factored page that allow user to choose type of rules and added other rule types" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4835 svn:author "ddean" $REPO_URL
$SVN_CMD --revprop -r 4835 svn:date "2009-06-17T19:42:35.750573Z" $REPO_URL
$SVN_CMD --revprop -r 4835 svn:log "refactoring" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4845 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 4845 svn:date "2009-06-18T17:49:45.432854Z" $REPO_URL
$SVN_CMD --revprop -r 4845 svn:log "Initial scrape and impl. Will probably have to re-scrape." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4861 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 4861 svn:date "2009-06-19T04:42:10.518752Z" $REPO_URL
$SVN_CMD --revprop -r 4861 svn:log "LearningObjectiveService: initial scrape & impl
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4867 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 4867 svn:date "2009-06-19T18:05:56.949838Z" $REPO_URL
$SVN_CMD --revprop -r 4867 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4868 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 4868 svn:date "2009-06-19T18:10:08.864401Z" $REPO_URL
$SVN_CMD --revprop -r 4868 svn:log "first draft of ModelDTO conversion code" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4876 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 4876 svn:date "2009-06-19T22:38:48.599951Z" $REPO_URL
$SVN_CMD --revprop -r 4876 svn:log "Add single button support to button group" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4892 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4892 svn:date "2009-06-23T15:17:32.963726Z" $REPO_URL
$SVN_CMD --revprop -r 4892 svn:log "Added org service deployment to lum-web" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4893 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 4893 svn:date "2009-06-23T16:50:00.278094Z" $REPO_URL
$SVN_CMD --revprop -r 4893 svn:log "DTO for new dictionary model" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4896 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 4896 svn:date "2009-06-23T20:30:54.026263Z" $REPO_URL
$SVN_CMD --revprop -r 4896 svn:log "Picker updated to use the new table" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4908 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 4908 svn:date "2009-06-24T07:14:36.445647Z" $REPO_URL
$SVN_CMD --revprop -r 4908 svn:log "KSPLAN-23: DB load not working yet, but here's some dao-related skeletons." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4922 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 4922 svn:date "2009-06-24T18:47:46.145265Z" $REPO_URL
$SVN_CMD --revprop -r 4922 svn:log "Check in a version of Configurable UI which makes use of the MVC." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4933 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4933 svn:date "2009-06-24T20:17:20.806988Z" $REPO_URL
$SVN_CMD --revprop -r 4933 svn:log "updated post processor to new doc format and added preliminary collab post processor" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4934 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4934 svn:date "2009-06-24T20:20:42.740869Z" $REPO_URL
$SVN_CMD --revprop -r 4934 svn:log "Implementation classes for document service." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4936 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4936 svn:date "2009-06-24T20:41:12.980458Z" $REPO_URL
$SVN_CMD --revprop -r 4936 svn:log "Test classes for document service." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4937 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4937 svn:date "2009-06-24T20:41:27.972047Z" $REPO_URL
$SVN_CMD --revprop -r 4937 svn:log "Test classes for document service." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4938 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4938 svn:date "2009-06-24T20:41:41.389146Z" $REPO_URL
$SVN_CMD --revprop -r 4938 svn:log "Implementation classes for document service." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4948 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 4948 svn:date "2009-06-24T22:28:19.523516Z" $REPO_URL
$SVN_CMD --revprop -r 4948 svn:log "\\- continued work on using same screens for other rule types like co-req, anti-req and enrollment rules" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4950 svn:author "ddean" $REPO_URL
$SVN_CMD --revprop -r 4950 svn:date "2009-06-25T13:58:13.567160Z" $REPO_URL
$SVN_CMD --revprop -r 4950 svn:log "initial implementation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4954 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 4954 svn:date "2009-06-25T17:50:59.327608Z" $REPO_URL
$SVN_CMD --revprop -r 4954 svn:log "Org messages and entry point update, messages are now loaded in core/org" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4956 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 4956 svn:date "2009-06-25T18:52:16.941837Z" $REPO_URL
$SVN_CMD --revprop -r 4956 svn:log "renamed namespace for newmodel" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4959 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 4959 svn:date "2009-06-25T19:56:30.974892Z" $REPO_URL
$SVN_CMD --revprop -r 4959 svn:log "Entities for document service." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4981 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 4981 svn:date "2009-06-26T07:02:05.854055Z" $REPO_URL
$SVN_CMD --revprop -r 4981 svn:log "initial commit of branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 4992 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 4992 svn:date "2009-06-26T14:16:57.498815Z" $REPO_URL
$SVN_CMD --revprop -r 4992 svn:log "Updated to match xsd" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5020 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5020 svn:date "2009-06-29T16:00:31.584220Z" $REPO_URL
$SVN_CMD --revprop -r 5020 svn:log "Made document have multiple colleges" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5042 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5042 svn:date "2009-06-29T21:37:03.873385Z" $REPO_URL
$SVN_CMD --revprop -r 5042 svn:log "merged in KNS branch to trunk" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5043 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5043 svn:date "2009-06-29T21:39:58.887692Z" $REPO_URL
$SVN_CMD --revprop -r 5043 svn:log "merged in KNS branch to trunk" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5056 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5056 svn:date "2009-06-30T17:24:52.694524Z" $REPO_URL
$SVN_CMD --revprop -r 5056 svn:log "Add way to display current user in application header." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5057 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 5057 svn:date "2009-06-30T17:25:51.356047Z" $REPO_URL
$SVN_CMD --revprop -r 5057 svn:log "Fixed webapp application context and properties and added a service test client" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5066 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5066 svn:date "2009-07-01T18:16:29.771907Z" $REPO_URL
$SVN_CMD --revprop -r 5066 svn:log "Initial version of the multiplicity composite." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5070 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5070 svn:date "2009-07-01T20:44:39.740394Z" $REPO_URL
$SVN_CMD --revprop -r 5070 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5117 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5117 svn:date "2009-07-06T21:03:28.262382Z" $REPO_URL
$SVN_CMD --revprop -r 5117 svn:log "Added rice user search in iframe for collaborators" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5162 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 5162 svn:date "2009-07-09T20:14:35.321886Z" $REPO_URL
$SVN_CMD --revprop -r 5162 svn:log "Add additional Clu fields to DTOs, entities, services and tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5185 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 5185 svn:date "2009-07-10T23:19:10.962704Z" $REPO_URL
$SVN_CMD --revprop -r 5185 svn:log "Made natural language translation a separate service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5204 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 5204 svn:date "2009-07-14T04:37:45.401572Z" $REPO_URL
$SVN_CMD --revprop -r 5204 svn:log "Added configurations for rule execution service test for ks-cxf and ks-metro profiles" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5212 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5212 svn:date "2009-07-14T22:19:41.076694Z" $REPO_URL
$SVN_CMD --revprop -r 5212 svn:log "aegis binding files for rice DTOs" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5230 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 5230 svn:date "2009-07-15T23:17:03.701181Z" $REPO_URL
$SVN_CMD --revprop -r 5230 svn:log "Update Clu & CluInfo to reflect admin org changes in service contract msg structures" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5231 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 5231 svn:date "2009-07-15T23:18:00.833997Z" $REPO_URL
$SVN_CMD --revprop -r 5231 svn:log "Update Clu & CluInfo to reflect service contract changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5241 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 5241 svn:date "2009-07-20T20:22:12.640007Z" $REPO_URL
$SVN_CMD --revprop -r 5241 svn:log "Initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5252 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 5252 svn:date "2009-07-21T15:27:48.297408Z" $REPO_URL
$SVN_CMD --revprop -r 5252 svn:log "Initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5255 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5255 svn:date "2009-07-21T15:48:10.891426Z" $REPO_URL
$SVN_CMD --revprop -r 5255 svn:log "converted to ansi encoding" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5297 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 5297 svn:date "2009-07-23T20:42:46.784391Z" $REPO_URL
$SVN_CMD --revprop -r 5297 svn:log "
Eclipse svn plugin missed these. Time for a fresh eclipse install
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5324 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 5324 svn:date "2009-07-27T20:05:24.352603Z" $REPO_URL
$SVN_CMD --revprop -r 5324 svn:log "
Subclass RichText; don't share its table
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5338 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5338 svn:date "2009-07-29T18:17:29.234297Z" $REPO_URL
$SVN_CMD --revprop -r 5338 svn:log "Add ability to add tool widgets to a configurable layout." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5342 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5342 svn:date "2009-07-29T18:58:06.628559Z" $REPO_URL
$SVN_CMD --revprop -r 5342 svn:log "added shell qualifier resolvers" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5343 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5343 svn:date "2009-07-29T19:01:02.931258Z" $REPO_URL
$SVN_CMD --revprop -r 5343 svn:log "init import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5352 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 5352 svn:date "2009-07-29T20:29:01.235697Z" $REPO_URL
$SVN_CMD --revprop -r 5352 svn:log "update so that Rice can be used for authentication" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5358 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 5358 svn:date "2009-07-30T00:34:25.789944Z" $REPO_URL
$SVN_CMD --revprop -r 5358 svn:log "Comment tool work in progress, new images and required field marker widget" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5381 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5381 svn:date "2009-07-30T18:51:06.688032Z" $REPO_URL
$SVN_CMD --revprop -r 5381 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5385 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 5385 svn:date "2009-07-30T19:18:34.521754Z" $REPO_URL
$SVN_CMD --revprop -r 5385 svn:log "interfaces for CommentService" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5392 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 5392 svn:date "2009-07-30T20:05:27.817803Z" $REPO_URL
$SVN_CMD --revprop -r 5392 svn:log "Import logging service from old ks-commons-dev" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5397 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 5397 svn:date "2009-07-30T23:02:51.451693Z" $REPO_URL
$SVN_CMD --revprop -r 5397 svn:log "Import ErrorDialog from old ks-commons-ui project" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5421 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 5421 svn:date "2009-08-01T09:43:45.285592Z" $REPO_URL
$SVN_CMD --revprop -r 5421 svn:log "[College & Dept]CommitteeQualifierResolver
Totally fugly, and really needs:
  - refactoring to remove duplicate logic and too-deep nested if's
  - use tailored queries to cut down on db calls
  - DeptCommitteeQR has no test; behavior's suspect
 but... Dan said to check in what I had, so..." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5450 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 5450 svn:date "2009-08-05T19:30:36.580150Z" $REPO_URL
$SVN_CMD --revprop -r 5450 svn:log "Lum changes for comment tool" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5452 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5452 svn:date "2009-08-05T20:48:08.410523Z" $REPO_URL
$SVN_CMD --revprop -r 5452 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5473 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 5473 svn:date "2009-08-07T17:09:40.612183Z" $REPO_URL
$SVN_CMD --revprop -r 5473 svn:log "validation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5484 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5484 svn:date "2009-08-10T17:57:40.669711Z" $REPO_URL
$SVN_CMD --revprop -r 5484 svn:log "Update to use ksb to deploy and lookup services." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5487 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5487 svn:date "2009-08-10T21:10:40.283050Z" $REPO_URL
$SVN_CMD --revprop -r 5487 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5493 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5493 svn:date "2009-08-11T22:20:33.046968Z" $REPO_URL
$SVN_CMD --revprop -r 5493 svn:log "Initial load of dictionary generator
Not all the tests run successfully but we are close" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5494 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5494 svn:date "2009-08-11T22:21:47.327183Z" $REPO_URL
$SVN_CMD --revprop -r 5494 svn:log "Initial load of dictionary generator
Not all the tests run successfully but we are close" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5497 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5497 svn:date "2009-08-12T15:33:43.686685Z" $REPO_URL
$SVN_CMD --revprop -r 5497 svn:log "Initial load of dictionary generator
Not all the tests run successfully but we are close" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5520 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5520 svn:date "2009-08-18T14:05:18.853974Z" $REPO_URL
$SVN_CMD --revprop -r 5520 svn:log "added these back in, they got deleted in the merge" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5531 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 5531 svn:date "2009-08-18T22:07:50.684596Z" $REPO_URL
$SVN_CMD --revprop -r 5531 svn:log "helper class for dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5541 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 5541 svn:date "2009-08-19T21:00:35.265444Z" $REPO_URL
$SVN_CMD --revprop -r 5541 svn:log "Added required marker, document tool, comment panel changes, most changes are not final" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5554 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 5554 svn:date "2009-08-25T15:56:05.178536Z" $REPO_URL
$SVN_CMD --revprop -r 5554 svn:log "First rework of code to have the CourseReqManager to manage CourseRequisiteView as well. this will help with porting rules ui into lum ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5555 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5555 svn:date "2009-08-25T17:02:42.066025Z" $REPO_URL
$SVN_CMD --revprop -r 5555 svn:log "Added tester and used it to fix the pattern matcher for group types" $REPO_URL
$SVN_CMD --revprop -r 5556 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5556 svn:date "2009-08-25T18:30:55.538897Z" $REPO_URL
$SVN_CMD --revprop -r 5556 svn:log "Fixed compile problem that happeneded when I changed XmlWriter to be \"abstract\" but creating a TestXmlWriter" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5560 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 5560 svn:date "2009-08-26T17:57:28.660229Z" $REPO_URL
$SVN_CMD --revprop -r 5560 svn:log "Proposal Service - Initial checkin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5563 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5563 svn:date "2009-08-26T19:11:47.459123Z" $REPO_URL
$SVN_CMD --revprop -r 5563 svn:log "added learning objective wsdl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5569 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5569 svn:date "2009-08-27T02:02:32.883254Z" $REPO_URL
$SVN_CMD --revprop -r 5569 svn:log "1. Switched over to access excel using JExcelAPI
2. Improved indenting
3. Fixed testing so it closes the spreadsheet after done" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5572 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5572 svn:date "2009-08-27T16:05:59.708115Z" $REPO_URL
$SVN_CMD --revprop -r 5572 svn:log "updates for new workflow... needs kim sql to be updated" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5585 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5585 svn:date "2009-08-31T17:03:20.825185Z" $REPO_URL
$SVN_CMD --revprop -r 5585 svn:log "Create action events and fix some display issues with clu proposal screens." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5590 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5590 svn:date "2009-08-31T19:04:19.041084Z" $REPO_URL
$SVN_CMD --revprop -r 5590 svn:log "Updates to workflow (added division and multiple roles to responsibilities)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5622 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5622 svn:date "2009-09-02T19:02:17.172601Z" $REPO_URL
$SVN_CMD --revprop -r 5622 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5638 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 5638 svn:date "2009-09-02T23:35:05.680443Z" $REPO_URL
$SVN_CMD --revprop -r 5638 svn:log "Refactored translation service to use the lu service and added getRecomponents and getLuStatements methods to the lu service." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5641 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 5641 svn:date "2009-09-03T00:53:34.782264Z" $REPO_URL
$SVN_CMD --revprop -r 5641 svn:log "test sql data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5655 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5655 svn:date "2009-09-04T03:30:03.191793Z" $REPO_URL
$SVN_CMD --revprop -r 5655 svn:log "Added the mapping to the package path to the object-structure type.
So for example cluInfo now is written as org.kuali.student.lum.lu.dto.CluInfo." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5669 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 5669 svn:date "2009-09-04T23:54:46.841264Z" $REPO_URL
$SVN_CMD --revprop -r 5669 svn:log "Added test cases for requirements rpc gwt servlet" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5682 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5682 svn:date "2009-09-08T19:15:22.607145Z" $REPO_URL
$SVN_CMD --revprop -r 5682 svn:log "put away logic to create one-jar for command line generator" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5706 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5706 svn:date "2009-09-11T15:27:38.976629Z" $REPO_URL
$SVN_CMD --revprop -r 5706 svn:log "Add missing ks-orm file." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5730 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5730 svn:date "2009-09-15T18:25:39.485557Z" $REPO_URL
$SVN_CMD --revprop -r 5730 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5732 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 5732 svn:date "2009-09-15T19:37:45.785974Z" $REPO_URL
$SVN_CMD --revprop -r 5732 svn:log "added jpa conf files for easier db switching" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5768 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 5768 svn:date "2009-09-18T07:26:20.710616Z" $REPO_URL
$SVN_CMD --revprop -r 5768 svn:log "Added application state dao" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5772 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5772 svn:date "2009-09-18T19:56:49.949163Z" $REPO_URL
$SVN_CMD --revprop -r 5772 svn:log "Nested Config Factory " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5791 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5791 svn:date "2009-09-22T13:54:57.518081Z" $REPO_URL
$SVN_CMD --revprop -r 5791 svn:log "Merged in changes from trunk" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5799 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 5799 svn:date "2009-09-22T20:07:15.807187Z" $REPO_URL
$SVN_CMD --revprop -r 5799 svn:log "Changes for DocumentTool working with current relation implementation and validation layout/propagation, temporary test code still in place" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5802 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5802 svn:date "2009-09-22T20:16:33.566463Z" $REPO_URL
$SVN_CMD --revprop -r 5802 svn:log "got uberwar to deploy" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5818 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5818 svn:date "2009-09-23T19:59:21.716563Z" $REPO_URL
$SVN_CMD --revprop -r 5818 svn:log "Adding gwt-context to the core impl directory" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5819 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5819 svn:date "2009-09-23T20:00:17.334752Z" $REPO_URL
$SVN_CMD --revprop -r 5819 svn:log "Adding context and config files to lum impl for lum-web" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5822 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5822 svn:date "2009-09-23T20:08:48.531988Z" $REPO_URL
$SVN_CMD --revprop -r 5822 svn:log "Adding screens to uber war" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5829 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5829 svn:date "2009-09-23T22:23:06.450048Z" $REPO_URL
$SVN_CMD --revprop -r 5829 svn:log "Adding config file to core impl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5831 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5831 svn:date "2009-09-23T22:28:35.072041Z" $REPO_URL
$SVN_CMD --revprop -r 5831 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5836 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5836 svn:date "2009-09-24T02:01:24.222310Z" $REPO_URL
$SVN_CMD --revprop -r 5836 svn:log "initial load of initial search classes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5837 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5837 svn:date "2009-09-24T02:21:20.273867Z" $REPO_URL
$SVN_CMD --revprop -r 5837 svn:log "Got simple load test to work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5839 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5839 svn:date "2009-09-24T14:42:12.004742Z" $REPO_URL
$SVN_CMD --revprop -r 5839 svn:log "got build to generate xml but does not validate yet" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5864 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5864 svn:date "2009-09-25T15:17:35.098482Z" $REPO_URL
$SVN_CMD --revprop -r 5864 svn:log "Improved validation and got the xml to validate!!!" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5867 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5867 svn:date "2009-09-25T17:09:00.721994Z" $REPO_URL
$SVN_CMD --revprop -r 5867 svn:log "got command line generator working for search and fixed usage for dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5868 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5868 svn:date "2009-09-25T17:10:12.176816Z" $REPO_URL
$SVN_CMD --revprop -r 5868 svn:log "got command line generator working for search and fixed usage for dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5869 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 5869 svn:date "2009-09-25T17:14:54.078129Z" $REPO_URL
$SVN_CMD --revprop -r 5869 svn:log "got command line generator working for search and fixed usage for dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5874 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5874 svn:date "2009-09-25T17:56:34.087340Z" $REPO_URL
$SVN_CMD --revprop -r 5874 svn:log "Remove use of totsp gwt plugin." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5880 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 5880 svn:date "2009-09-25T19:56:31.661674Z" $REPO_URL
$SVN_CMD --revprop -r 5880 svn:log "Added collaborator request approve/disapprove screen" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5916 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5916 svn:date "2009-09-28T21:19:22.782301Z" $REPO_URL
$SVN_CMD --revprop -r 5916 svn:log "Add gwt rpc classes for proposal service, currently used to just expose proposal service dictionary & search methods." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5917 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 5917 svn:date "2009-09-28T23:02:31.841499Z" $REPO_URL
$SVN_CMD --revprop -r 5917 svn:log "add widget to display list of Strings." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5924 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 5924 svn:date "2009-09-29T17:33:18.623892Z" $REPO_URL
$SVN_CMD --revprop -r 5924 svn:log "Added application state manager and database configurations" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5943 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 5943 svn:date "2009-09-30T18:27:30.448764Z" $REPO_URL
$SVN_CMD --revprop -r 5943 svn:log "Separate out lum ui inheritences and entry point so lum ui components can be reused without inheriting the entry point." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5979 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5979 svn:date "2009-10-01T21:06:31.312226Z" $REPO_URL
$SVN_CMD --revprop -r 5979 svn:log "Startup file for uber war" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 5991 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 5991 svn:date "2009-10-02T15:14:31.311675Z" $REPO_URL
$SVN_CMD --revprop -r 5991 svn:log "Moving ks-nl-context into ks-lum-impl " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6008 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 6008 svn:date "2009-10-02T20:53:16.593812Z" $REPO_URL
$SVN_CMD --revprop -r 6008 svn:log "Update CluProposalRpcService interface to throw OperationFailedException so server side exceptions passed along to GWT client code." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6038 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6038 svn:date "2009-10-05T18:28:50.035505Z" $REPO_URL
$SVN_CMD --revprop -r 6038 svn:log "Merged in changes to ks-standalone" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6052 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6052 svn:date "2009-10-05T21:05:47.183251Z" $REPO_URL
$SVN_CMD --revprop -r 6052 svn:log "Moved uberwar/ks-standalone code and testing config to a new lum module" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6076 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6076 svn:date "2009-10-06T15:52:28.927295Z" $REPO_URL
$SVN_CMD --revprop -r 6076 svn:log "Moving gwt context and all refered files outside META-INF so that they can be included in the ui jar. " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6080 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6080 svn:date "2009-10-06T17:18:44.931432Z" $REPO_URL
$SVN_CMD --revprop -r 6080 svn:log "insert gwt context in resources so that it is included in the core ui jar." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6091 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6091 svn:date "2009-10-06T21:31:51.736236Z" $REPO_URL
$SVN_CMD --revprop -r 6091 svn:log "updating frm core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6182 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6182 svn:date "2009-10-09T19:01:08.750737Z" $REPO_URL
$SVN_CMD --revprop -r 6182 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6185 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6185 svn:date "2009-10-09T19:11:00.197507Z" $REPO_URL
$SVN_CMD --revprop -r 6185 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6195 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6195 svn:date "2009-10-09T20:05:57.851343Z" $REPO_URL
$SVN_CMD --revprop -r 6195 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6200 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6200 svn:date "2009-10-09T20:08:36.266859Z" $REPO_URL
$SVN_CMD --revprop -r 6200 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6202 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6202 svn:date "2009-10-09T20:09:40.851807Z" $REPO_URL
$SVN_CMD --revprop -r 6202 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6209 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6209 svn:date "2009-10-09T20:15:45.142017Z" $REPO_URL
$SVN_CMD --revprop -r 6209 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6214 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6214 svn:date "2009-10-09T20:18:14.559948Z" $REPO_URL
$SVN_CMD --revprop -r 6214 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6216 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6216 svn:date "2009-10-09T20:19:31.337094Z" $REPO_URL
$SVN_CMD --revprop -r 6216 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6231 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6231 svn:date "2009-10-12T21:09:47.127908Z" $REPO_URL
$SVN_CMD --revprop -r 6231 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6232 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6232 svn:date "2009-10-12T21:11:02.192870Z" $REPO_URL
$SVN_CMD --revprop -r 6232 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6233 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6233 svn:date "2009-10-13T13:49:23.326320Z" $REPO_URL
$SVN_CMD --revprop -r 6233 svn:log "switched around embedded and all" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6235 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 6235 svn:date "2009-10-13T13:53:57.812960Z" $REPO_URL
$SVN_CMD --revprop -r 6235 svn:log "Add default config file for embedded ksb using nested properties to use in core-ui and core-web modules." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6244 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6244 svn:date "2009-10-13T19:31:38.096081Z" $REPO_URL
$SVN_CMD --revprop -r 6244 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6246 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 6246 svn:date "2009-10-14T02:46:12.103435Z" $REPO_URL
$SVN_CMD --revprop -r 6246 svn:log "initial commit of rice impex data project" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6255 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6255 svn:date "2009-10-14T17:34:07.724358Z" $REPO_URL
$SVN_CMD --revprop -r 6255 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6272 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6272 svn:date "2009-10-15T15:25:27.423170Z" $REPO_URL
$SVN_CMD --revprop -r 6272 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6289 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6289 svn:date "2009-10-16T21:19:57.266921Z" $REPO_URL
$SVN_CMD --revprop -r 6289 svn:log "Hello World in Kuali Component Land" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6295 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6295 svn:date "2009-10-19T02:54:17.202855Z" $REPO_URL
$SVN_CMD --revprop -r 6295 svn:log "got swing and gwt impls clean compiles.  Swing works (sort of) and gwt invokes but I don't have an html file yet" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6308 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6308 svn:date "2009-10-19T15:13:15.750811Z" $REPO_URL
$SVN_CMD --revprop -r 6308 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6309 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 6309 svn:date "2009-10-19T15:19:53.036853Z" $REPO_URL
$SVN_CMD --revprop -r 6309 svn:log "update to poms to share property for all ks-core depend versions.  updated .classpath to use proper output directories." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6317 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 6317 svn:date "2009-10-19T15:59:22.605928Z" $REPO_URL
$SVN_CMD --revprop -r 6317 svn:log "updated poms to use property for ks-core versions.  updated javadoc report to use more properties." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6359 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 6359 svn:date "2009-10-22T15:45:45.152394Z" $REPO_URL
$SVN_CMD --revprop -r 6359 svn:log "Changes to create a new sandbox to test search widgets." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6369 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6369 svn:date "2009-10-22T20:57:20.716664Z" $REPO_URL
$SVN_CMD --revprop -r 6369 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6370 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 6370 svn:date "2009-10-22T21:00:49.533441Z" $REPO_URL
$SVN_CMD --revprop -r 6370 svn:log "added Learning Objective service to ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6389 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6389 svn:date "2009-10-23T14:58:25.661484Z" $REPO_URL
$SVN_CMD --revprop -r 6389 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6392 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6392 svn:date "2009-10-23T15:03:03.097066Z" $REPO_URL
$SVN_CMD --revprop -r 6392 svn:log "name change to ks-standalone from ks-all" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6401 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6401 svn:date "2009-10-23T19:25:01.120177Z" $REPO_URL
$SVN_CMD --revprop -r 6401 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6404 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 6404 svn:date "2009-10-26T08:29:28.949280Z" $REPO_URL
$SVN_CMD --revprop -r 6404 svn:log "Rescraped and refactored LearningObjectiveService
   https://test.kuali.org/confluence/display/KULSTU/Learning+Objective+Service+v1.0-rc2" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6422 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 6422 svn:date "2009-10-27T20:27:44.807772Z" $REPO_URL
$SVN_CMD --revprop -r 6422 svn:log "Add initial LOPicker with LO search capability" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6445 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6445 svn:date "2009-10-29T16:47:25.497300Z" $REPO_URL
$SVN_CMD --revprop -r 6445 svn:log "put away my compoent definition stuff into subversion" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6457 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 6457 svn:date "2009-10-30T00:54:53.468625Z" $REPO_URL
$SVN_CMD --revprop -r 6457 svn:log "UX/UI fixes for M1" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6471 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6471 svn:date "2009-10-30T19:41:40.536330Z" $REPO_URL
$SVN_CMD --revprop -r 6471 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6475 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 6475 svn:date "2009-10-31T18:34:44.546850Z" $REPO_URL
$SVN_CMD --revprop -r 6475 svn:log "Rules related fixes. Now rules screen appear in Lum UI and proposal can be saved without errors. Rules are also saved." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6478 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 6478 svn:date "2009-11-02T14:45:35.481292Z" $REPO_URL
$SVN_CMD --revprop -r 6478 svn:log "Add new bindings for new model implementation. (Multiplicity still needs fleshing out)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6479 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6479 svn:date "2009-11-02T15:40:45.827557Z" $REPO_URL
$SVN_CMD --revprop -r 6479 svn:log "Adding proposal table entries" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6480 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6480 svn:date "2009-11-02T15:42:31.926197Z" $REPO_URL
$SVN_CMD --revprop -r 6480 svn:log "Adding Comment table data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6497 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6497 svn:date "2009-11-03T17:11:58.601732Z" $REPO_URL
$SVN_CMD --revprop -r 6497 svn:log "put away before renaming" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6501 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 6501 svn:date "2009-11-03T19:00:50.633126Z" $REPO_URL
$SVN_CMD --revprop -r 6501 svn:log "adding rice keystore so security can be enabled" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6508 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 6508 svn:date "2009-11-04T00:07:39.737587Z" $REPO_URL
$SVN_CMD --revprop -r 6508 svn:log "launch file for ui test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6509 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 6509 svn:date "2009-11-04T00:34:12.294041Z" $REPO_URL
$SVN_CMD --revprop -r 6509 svn:log "CSS and image theming components and changes to files to use them, deprecation of old widgets and theming, layout and css fixes for M1" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6515 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 6515 svn:date "2009-11-04T01:09:14.505771Z" $REPO_URL
$SVN_CMD --revprop -r 6515 svn:log "KD8 look and feel changes and save dialog" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6517 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 6517 svn:date "2009-11-04T01:19:45.427399Z" $REPO_URL
$SVN_CMD --revprop -r 6517 svn:log "ATP new files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6521 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6521 svn:date "2009-11-04T11:01:37.400841Z" $REPO_URL
$SVN_CMD --revprop -r 6521 svn:log "got basic model generation of all service mapped Data structures working except for dealing with lists." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6522 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6522 svn:date "2009-11-04T14:57:14.157582Z" $REPO_URL
$SVN_CMD --revprop -r 6522 svn:log "ADded atp data to core-web" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6528 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6528 svn:date "2009-11-04T18:30:11.499885Z" $REPO_URL
$SVN_CMD --revprop -r 6528 svn:log "Updated index page" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6529 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6529 svn:date "2009-11-04T18:30:34.592640Z" $REPO_URL
$SVN_CMD --revprop -r 6529 svn:log "Updated index page" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6534 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6534 svn:date "2009-11-04T19:31:14.317061Z" $REPO_URL
$SVN_CMD --revprop -r 6534 svn:log "Moved StudentIdentityServiceImpl to lum" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6544 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6544 svn:date "2009-11-05T02:20:36.421645Z" $REPO_URL
$SVN_CMD --revprop -r 6544 svn:log "change model finder to ignore case.
Got orchestration object generator working" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6554 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 6554 svn:date "2009-11-05T06:56:50.940309Z" $REPO_URL
$SVN_CMD --revprop -r 6554 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6557 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6557 svn:date "2009-11-05T17:12:54.420732Z" $REPO_URL
$SVN_CMD --revprop -r 6557 svn:log "updated timestamps" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6563 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 6563 svn:date "2009-11-05T22:35:16.862317Z" $REPO_URL
$SVN_CMD --revprop -r 6563 svn:log "Initial rework to use new model impl for clu screens" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6572 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6572 svn:date "2009-11-06T16:56:13.872936Z" $REPO_URL
$SVN_CMD --revprop -r 6572 svn:log "Added data changes for rice 1.0.1" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6575 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6575 svn:date "2009-11-06T17:08:14.463206Z" $REPO_URL
$SVN_CMD --revprop -r 6575 svn:log "Switched to rice 1.0.1" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6579 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 6579 svn:date "2009-11-06T22:50:18.614132Z" $REPO_URL
$SVN_CMD --revprop -r 6579 svn:log "Initial work on a selectors (search components)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6580 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 6580 svn:date "2009-11-07T00:10:55.938081Z" $REPO_URL
$SVN_CMD --revprop -r 6580 svn:log "Initial work on a selectors (search components) (for Sherman)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6581 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 6581 svn:date "2009-11-07T00:34:36.554086Z" $REPO_URL
$SVN_CMD --revprop -r 6581 svn:log "Initial work on a selectors (search components) (for Sherman)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6596 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 6596 svn:date "2009-11-09T23:32:54.332289Z" $REPO_URL
$SVN_CMD --revprop -r 6596 svn:log "TabbedSectionLayout for configurable ui changes, header and footer (kswrapper), and container for tabs (TitleContainer)" $REPO_URL
$SVN_CMD --revprop -r 6617 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 6617 svn:date "2009-11-10T17:57:54.771528Z" $REPO_URL
$SVN_CMD --revprop -r 6617 svn:log "Make next button be \"Save & Continue\"" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6648 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 6648 svn:date "2009-11-11T06:14:42.550146Z" $REPO_URL
$SVN_CMD --revprop -r 6648 svn:log "updated core and lum version to 1.0.0-m1 and added site info to pom" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6661 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 6661 svn:date "2009-11-11T15:31:25.959999Z" $REPO_URL
$SVN_CMD --revprop -r 6661 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6676 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 6676 svn:date "2009-11-11T20:18:01.527398Z" $REPO_URL
$SVN_CMD --revprop -r 6676 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6720 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 6720 svn:date "2009-11-12T17:09:38.061997Z" $REPO_URL
$SVN_CMD --revprop -r 6720 svn:log "Pickers for ATP." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6722 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 6722 svn:date "2009-11-12T17:50:16.700714Z" $REPO_URL
$SVN_CMD --revprop -r 6722 svn:log "First cut of generic search component in Lum UI context (need lots of re-factoring)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6755 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6755 svn:date "2009-11-13T23:23:35.065937Z" $REPO_URL
$SVN_CMD --revprop -r 6755 svn:log "got data objects redone without codemodel" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6803 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6803 svn:date "2009-11-19T14:36:56.581045Z" $REPO_URL
$SVN_CMD --revprop -r 6803 svn:log "changed from extending Data to being a Helper class" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6805 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6805 svn:date "2009-11-19T16:45:07.149050Z" $REPO_URL
$SVN_CMD --revprop -r 6805 svn:log "got 1st draft of orchestration objects done" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6810 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6810 svn:date "2009-11-19T22:52:09.141620Z" $REPO_URL
$SVN_CMD --revprop -r 6810 svn:log "Got an agreed upon version of meta data from wil and Andy and I  and Len" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6811 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6811 svn:date "2009-11-19T22:53:17.778575Z" $REPO_URL
$SVN_CMD --revprop -r 6811 svn:log "go grand children helpers working" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6816 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6816 svn:date "2009-11-19T23:36:00.258707Z" $REPO_URL
$SVN_CMD --revprop -r 6816 svn:log "found I was updating an old dictionary got right one" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6817 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6817 svn:date "2009-11-19T23:42:08.384097Z" $REPO_URL
$SVN_CMD --revprop -r 6817 svn:log "found I was updating an old dictionary got right one" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6838 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6838 svn:date "2009-11-20T21:34:48.700339Z" $REPO_URL
$SVN_CMD --revprop -r 6838 svn:log "New Org Screen With update UI framework and Orchestration Layer" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6843 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6843 svn:date "2009-11-22T22:06:03.016011Z" $REPO_URL
$SVN_CMD --revprop -r 6843 svn:log "got 1st slice of constraints done" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6845 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6845 svn:date "2009-11-23T16:37:12.058302Z" $REPO_URL
$SVN_CMD --revprop -r 6845 svn:log "got metadata chaining done" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6856 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 6856 svn:date "2009-11-24T16:30:48.327802Z" $REPO_URL
$SVN_CMD --revprop -r 6856 svn:log "added eclipse config files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6866 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6866 svn:date "2009-11-25T02:40:39.991052Z" $REPO_URL
$SVN_CMD --revprop -r 6866 svn:log "fixed list " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6870 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6870 svn:date "2009-11-25T13:48:51.923100Z" $REPO_URL
$SVN_CMD --revprop -r 6870 svn:log "added LIST as a data type and added my pretty printer dumper" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6873 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 6873 svn:date "2009-11-25T16:41:13.098342Z" $REPO_URL
$SVN_CMD --revprop -r 6873 svn:log "added Norm's helpers and metadata builders, modified some assemblers to do passthrough to the metadata builders, and created a test page for browsing the metadata" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6886 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 6886 svn:date "2009-11-27T19:24:56.485686Z" $REPO_URL
$SVN_CMD --revprop -r 6886 svn:log "partial commit, Model.java refactor" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6890 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6890 svn:date "2009-11-27T22:59:39.131542Z" $REPO_URL
$SVN_CMD --revprop -r 6890 svn:log "got models aligned to what the assembler already creates and UI expecte 
Made modifications explicit fields and renamed them as _RuntimeData" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6901 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6901 svn:date "2009-11-30T19:41:00.862923Z" $REPO_URL
$SVN_CMD --revprop -r 6901 svn:log "Organization Screen GWT module definition and CSS changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6905 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 6905 svn:date "2009-11-30T20:52:24.956591Z" $REPO_URL
$SVN_CMD --revprop -r 6905 svn:log "new assembler/helper stuff, needs to be refactored still" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6907 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6907 svn:date "2009-11-30T21:13:38.619247Z" $REPO_URL
$SVN_CMD --revprop -r 6907 svn:log "updated to rice 1.0.1" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6908 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 6908 svn:date "2009-11-30T21:30:54.116232Z" $REPO_URL
$SVN_CMD --revprop -r 6908 svn:log "OrgData gwt module definition" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6915 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6915 svn:date "2009-12-01T16:32:12.237336Z" $REPO_URL
$SVN_CMD --revprop -r 6915 svn:log "updated data to non-client rice data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6949 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6949 svn:date "2009-12-02T16:28:00.321332Z" $REPO_URL
$SVN_CMD --revprop -r 6949 svn:log "Added a CONSTANTS writer so developers can use them when building configurers
Changed to write out to different directories and root package paths" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6950 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6950 svn:date "2009-12-02T16:50:10.016386Z" $REPO_URL
$SVN_CMD --revprop -r 6950 svn:log "Added a CONSTANTS writer so developers can use them when building configurers
Started reworking CourseConfigurer to use the new fields" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6951 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6951 svn:date "2009-12-02T16:51:18.087648Z" $REPO_URL
$SVN_CMD --revprop -r 6951 svn:log "Added a CONSTANTS writer so developers can use them when building configurers
Started reworking CourseConfigurer to use the new fields" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6957 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6957 svn:date "2009-12-02T17:22:27.550889Z" $REPO_URL
$SVN_CMD --revprop -r 6957 svn:log "fixed bug in placement of the Bank of constraints" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6958 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6958 svn:date "2009-12-02T17:24:51.723912Z" $REPO_URL
$SVN_CMD --revprop -r 6958 svn:log "fixed bug in placement of the Bank of constraints" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6963 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 6963 svn:date "2009-12-02T19:54:21.404119Z" $REPO_URL
$SVN_CMD --revprop -r 6963 svn:log "Add new button group" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6972 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 6972 svn:date "2009-12-02T22:25:55.544594Z" $REPO_URL
$SVN_CMD --revprop -r 6972 svn:log "Lum landing page and lum theme first pass" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6981 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 6981 svn:date "2009-12-03T03:19:30.435429Z" $REPO_URL
$SVN_CMD --revprop -r 6981 svn:log "Got as many OO  fields in as I could figoure out" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 6994 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 6994 svn:date "2009-12-04T15:28:31.112048Z" $REPO_URL
$SVN_CMD --revprop -r 6994 svn:log "Added some search functions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7054 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 7054 svn:date "2009-12-10T19:54:11.600255Z" $REPO_URL
$SVN_CMD --revprop -r 7054 svn:log "Missed LO search widget" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7063 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7063 svn:date "2009-12-10T22:25:28.889782Z" $REPO_URL
$SVN_CMD --revprop -r 7063 svn:log "Changes to Metadata to support lookups better.
Added LookupImpl metadata" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7064 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7064 svn:date "2009-12-10T22:26:16.775858Z" $REPO_URL
$SVN_CMD --revprop -r 7064 svn:log "Got Lookup Metadata working" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7082 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7082 svn:date "2009-12-11T20:04:51.724946Z" $REPO_URL
$SVN_CMD --revprop -r 7082 svn:log "added filter tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7085 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 7085 svn:date "2009-12-14T07:55:36.493341Z" $REPO_URL
$SVN_CMD --revprop -r 7085 svn:log "
Changes/additions for LearningObjectiveService v1.0-rc3
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7086 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 7086 svn:date "2009-12-14T08:07:07.913635Z" $REPO_URL
$SVN_CMD --revprop -r 7086 svn:log "New exception to support LearningObjectiveService v1.0-rc3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7087 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 7087 svn:date "2009-12-14T09:27:21.987776Z" $REPO_URL
$SVN_CMD --revprop -r 7087 svn:log "LearningObjectiveService v1.0-rc3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7091 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7091 svn:date "2009-12-14T18:48:11.486580Z" $REPO_URL
$SVN_CMD --revprop -r 7091 svn:log "Merged in orchestration branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7092 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7092 svn:date "2009-12-14T18:49:44.848698Z" $REPO_URL
$SVN_CMD --revprop -r 7092 svn:log "Merged in orchestration branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7098 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 7098 svn:date "2009-12-15T07:36:35.656036Z" $REPO_URL
$SVN_CMD --revprop -r 7098 svn:log "LearningObjectiveService v1.0-rc3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7136 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7136 svn:date "2009-12-17T12:32:42.354953Z" $REPO_URL
$SVN_CMD --revprop -r 7136 svn:log "tested recursion on LO's" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7142 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 7142 svn:date "2009-12-17T19:09:52.002574Z" $REPO_URL
$SVN_CMD --revprop -r 7142 svn:log "Uncomment LO section" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7148 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7148 svn:date "2009-12-17T21:02:16.139383Z" $REPO_URL
$SVN_CMD --revprop -r 7148 svn:log "made changes requestd by Len and Zdenek and Brian
See http://spreadsheets.google.com/ccc?key=0AvVLjful5tlbdDRWQ0hjdjZxM0tmYl9YV2JGTm5CLWc&hl=en
the Changes TAb
PLUS new OO objects for LO by Seth" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7153 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 7153 svn:date "2009-12-17T23:01:25.658650Z" $REPO_URL
$SVN_CMD --revprop -r 7153 svn:log "icons for LO" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7155 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 7155 svn:date "2009-12-18T00:44:34.795948Z" $REPO_URL
$SVN_CMD --revprop -r 7155 svn:log "Add initial category code picker" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7157 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 7157 svn:date "2009-12-18T18:35:54.298039Z" $REPO_URL
$SVN_CMD --revprop -r 7157 svn:log "LO toolbar icons" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7161 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 7161 svn:date "2009-12-18T22:00:46.658924Z" $REPO_URL
$SVN_CMD --revprop -r 7161 svn:log "new gif" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7164 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 7164 svn:date "2009-12-18T23:21:40.517218Z" $REPO_URL
$SVN_CMD --revprop -r 7164 svn:log "really add new button groups this time" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7167 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 7167 svn:date "2009-12-19T02:02:33.288909Z" $REPO_URL
$SVN_CMD --revprop -r 7167 svn:log "Multi-level LO's using OutlineManager (via Plan B; no LoAssembler), part 1
  persistence/retrieval issues, but late for the cutoff." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7170 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7170 svn:date "2010-01-04T16:30:21.375601Z" $REPO_URL
$SVN_CMD --revprop -r 7170 svn:log "Updated lum db" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7238 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7238 svn:date "2010-01-06T15:37:33.212469Z" $REPO_URL
$SVN_CMD --revprop -r 7238 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7242 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 7242 svn:date "2010-01-06T16:39:38.458736Z" $REPO_URL
$SVN_CMD --revprop -r 7242 svn:log "adding src and site.xml looks like the delete worked" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7243 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 7243 svn:date "2010-01-06T17:43:19.915050Z" $REPO_URL
$SVN_CMD --revprop -r 7243 svn:log "initial commit of reworked client validation code using new metadata structures" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7252 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7252 svn:date "2010-01-06T20:01:12.756799Z" $REPO_URL
$SVN_CMD --revprop -r 7252 svn:log "more refactoring to make model and implementation separate and combine excpetion processing between search and dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7269 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7269 svn:date "2010-01-07T20:25:47.385623Z" $REPO_URL
$SVN_CMD --revprop -r 7269 svn:log "simplified search model and fixed recursion" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7271 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7271 svn:date "2010-01-07T21:25:44.124390Z" $REPO_URL
$SVN_CMD --revprop -r 7271 svn:log "Added atp" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7284 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7284 svn:date "2010-01-08T16:19:14.770605Z" $REPO_URL
$SVN_CMD --revprop -r 7284 svn:log "Added Service and Projects tothe model and added validation to entities that reference them so we have referential integrity" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7285 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7285 svn:date "2010-01-08T16:20:05.348199Z" $REPO_URL
$SVN_CMD --revprop -r 7285 svn:log "Added Service and Projects tothe model and added validation to entities that reference them so we have referential integrity" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7287 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7287 svn:date "2010-01-08T16:24:37.607519Z" $REPO_URL
$SVN_CMD --revprop -r 7287 svn:log "added it back in because because I had orginally put away as binary on accident" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7291 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7291 svn:date "2010-01-08T19:07:19.708011Z" $REPO_URL
$SVN_CMD --revprop -r 7291 svn:log "added in course search that matches the existing search xml 
Also fixed recursion on base objects to work the same as oo objects even though their is no recursion in base objects" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7292 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7292 svn:date "2010-01-08T19:08:02.860818Z" $REPO_URL
$SVN_CMD --revprop -r 7292 svn:log "added in course search that matches the existing search xml " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7297 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 7297 svn:date "2010-01-11T11:11:01.626507Z" $REPO_URL
$SVN_CMD --revprop -r 7297 svn:log "First picker using new DOL search metadata and search UI and search implementation. The Course Picker works but a defect from past DOL transition effort prevents the selected course from being displayed. That has to be fixed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7302 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 7302 svn:date "2010-01-11T20:24:32.080835Z" $REPO_URL
$SVN_CMD --revprop -r 7302 svn:log "Add JAXB annotations" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7307 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7307 svn:date "2010-01-12T04:10:16.313638Z" $REPO_URL
$SVN_CMD --revprop -r 7307 svn:log "fixed (1) setting of default value, (2) ResultSortKey default logic, (3) Changed generator to use LookupMetadata.Usage instead of LookupParamMetadata.Usage per change Will Gomes made to the metedta merging the two concepts
Got rid of extra old OrchObjLookup object class.  BEgin looking at assembler helprs again." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7308 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7308 svn:date "2010-01-12T04:13:29.143501Z" $REPO_URL
$SVN_CMD --revprop -r 7308 svn:log "fixed (1) setting of default value, (2) ResultSortKey default logic, (3) Changed generator to use LookupMetadata.Usage instead of LookupParamMetadata.Usage per change Will Gomes made to the metedta merging the two concepts
Got rid of extra old OrchObjLookup object class.  BEgin looking at assembler helprs again." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7309 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7309 svn:date "2010-01-12T14:43:56.269600Z" $REPO_URL
$SVN_CMD --revprop -r 7309 svn:log "Added initial DOL base RPC servlet
refactored DataSaveResult to common-ui and removed duplicate of that class
added a search service to common-ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7319 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 7319 svn:date "2010-01-12T21:40:18.459838Z" $REPO_URL
$SVN_CMD --revprop -r 7319 svn:log "Initial checkin for read only multiplicity widgets" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7323 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 7323 svn:date "2010-01-13T15:57:28.426702Z" $REPO_URL
$SVN_CMD --revprop -r 7323 svn:log "Initial xml representation of orchestration metadata" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7334 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7334 svn:date "2010-01-14T03:47:19.656762Z" $REPO_URL
$SVN_CMD --revprop -r 7334 svn:log "Expanded model for service api creation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7336 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7336 svn:date "2010-01-14T03:48:34.745020Z" $REPO_URL
$SVN_CMD --revprop -r 7336 svn:log "generated atp service api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7337 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7337 svn:date "2010-01-14T16:05:22.575488Z" $REPO_URL
$SVN_CMD --revprop -r 7337 svn:log "added in missing comments" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7342 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7342 svn:date "2010-01-14T19:28:17.261431Z" $REPO_URL
$SVN_CMD --revprop -r 7342 svn:log "Initial commit of cucumber testing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7357 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7357 svn:date "2010-01-15T19:06:16.086650Z" $REPO_URL
$SVN_CMD --revprop -r 7357 svn:log "added enumeration management to lum-web context" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7373 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7373 svn:date "2010-01-19T16:18:38.433516Z" $REPO_URL
$SVN_CMD --revprop -r 7373 svn:log "added entityManager tables (fixed rename too) and em data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7376 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7376 svn:date "2010-01-19T17:16:29.768438Z" $REPO_URL
$SVN_CMD --revprop -r 7376 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7388 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 7388 svn:date "2010-01-20T01:36:37.674571Z" $REPO_URL
$SVN_CMD --revprop -r 7388 svn:log "Added new picker for admin org. (need to be integrated with suggest box widget). Improved on general search picker" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7391 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7391 svn:date "2010-01-20T03:21:38.570022Z" $REPO_URL
$SVN_CMD --revprop -r 7391 svn:log "add soap, local and remote (-r ... readme)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7455 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 7455 svn:date "2010-01-20T19:20:33.822379Z" $REPO_URL
$SVN_CMD --revprop -r 7455 svn:log "moved ks-em.sql file from lum and put an entry in the dataLoadListener" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7466 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 7466 svn:date "2010-01-20T21:54:53.795830Z" $REPO_URL
$SVN_CMD --revprop -r 7466 svn:log "Added statement service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7469 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 7469 svn:date "2010-01-21T06:38:24.798630Z" $REPO_URL
$SVN_CMD --revprop -r 7469 svn:log "LO persistence/retrieval via DOL
WIP:
- Need a LoListAssembler that is called by CreditCourseProposalAssembler
  and calls SingleUseLoInfoAssembler for each LO
- Need a LOCategoryInfoAssembler factored out of SingleUseLoInfoAssembler
- Need to push DOL farther down into OutlineManager
  (https://test.kuali.org/jira/browse/KSPLAN-198)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7472 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7472 svn:date "2010-01-21T16:00:38.860354Z" $REPO_URL
$SVN_CMD --revprop -r 7472 svn:log "New commit of light version if cucumber testing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7475 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7475 svn:date "2010-01-21T18:08:06.047435Z" $REPO_URL
$SVN_CMD --revprop -r 7475 svn:log "New commit of light version if cucumber testing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7477 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 7477 svn:date "2010-01-21T20:01:29.402060Z" $REPO_URL
$SVN_CMD --revprop -r 7477 svn:log "Updated statement service with proper entities and new statement service test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7484 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7484 svn:date "2010-01-22T01:20:55.283975Z" $REPO_URL
$SVN_CMD --revprop -r 7484 svn:log "Lots of refactoring.
(1) Compsosite Spreadsheets readers
(2) Wiki Scrapers to get contracts to compare to spreadsheet" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7488 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7488 svn:date "2010-01-22T02:14:13.549462Z" $REPO_URL
$SVN_CMD --revprop -r 7488 svn:log "doc small corrections" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7490 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7490 svn:date "2010-01-22T03:39:24.171302Z" $REPO_URL
$SVN_CMD --revprop -r 7490 svn:log "corrected doc" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7521 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 7521 svn:date "2010-01-22T20:46:20.595300Z" $REPO_URL
$SVN_CMD --revprop -r 7521 svn:log "Organization Screen with Configurable UI framework reading screen configuration from org_configure.xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7522 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7522 svn:date "2010-01-22T21:17:34.962761Z" $REPO_URL
$SVN_CMD --revprop -r 7522 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7524 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 7524 svn:date "2010-01-23T08:06:58.100162Z" $REPO_URL
$SVN_CMD --revprop -r 7524 svn:log "Added statement service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7580 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7580 svn:date "2010-01-24T22:53:59.247154Z" $REPO_URL
$SVN_CMD --revprop -r 7580 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7581 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7581 svn:date "2010-01-24T22:54:38.298594Z" $REPO_URL
$SVN_CMD --revprop -r 7581 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7582 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7582 svn:date "2010-01-24T22:55:24.113157Z" $REPO_URL
$SVN_CMD --revprop -r 7582 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7583 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7583 svn:date "2010-01-24T22:55:44.263438Z" $REPO_URL
$SVN_CMD --revprop -r 7583 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7584 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7584 svn:date "2010-01-24T22:56:05.696393Z" $REPO_URL
$SVN_CMD --revprop -r 7584 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7589 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7589 svn:date "2010-01-25T06:00:28.975864Z" $REPO_URL
$SVN_CMD --revprop -r 7589 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7591 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 7591 svn:date "2010-01-25T17:04:13.502430Z" $REPO_URL
$SVN_CMD --revprop -r 7591 svn:log "refactored configuration so that standard login or cas can be used by passing the corresponding xml config file to the Spring ContextLoaderListener in web.xml." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7598 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7598 svn:date "2010-01-25T22:28:57.249148Z" $REPO_URL
$SVN_CMD --revprop -r 7598 svn:log "Done through ks-common-impl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7607 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7607 svn:date "2010-01-26T00:16:25.737485Z" $REPO_URL
$SVN_CMD --revprop -r 7607 svn:log "Refactored so Jim's change to RichTextInfoHelper wouldn't be overwritten" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7608 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7608 svn:date "2010-01-26T00:25:40.494096Z" $REPO_URL
$SVN_CMD --revprop -r 7608 svn:log "Added in changes for LOS including searching for them
Also... copywrite upgraded to 2010" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7609 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7609 svn:date "2010-01-26T00:34:13.800787Z" $REPO_URL
$SVN_CMD --revprop -r 7609 svn:log "Added in changes for LOS including searching for them
Also... copywrite upgraded to 2010" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7628 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 7628 svn:date "2010-01-26T20:39:38.084692Z" $REPO_URL
$SVN_CMD --revprop -r 7628 svn:log "Initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7636 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 7636 svn:date "2010-01-27T10:02:17.300280Z" $REPO_URL
$SVN_CMD --revprop -r 7636 svn:log "Moved statement service from ks-core-dev to brms-dev" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7656 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 7656 svn:date "2010-01-28T00:36:38.669139Z" $REPO_URL
$SVN_CMD --revprop -r 7656 svn:log "Lu Service 1.4 Changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7657 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 7657 svn:date "2010-01-28T00:44:02.520013Z" $REPO_URL
$SVN_CMD --revprop -r 7657 svn:log "Lu Service 1.4 Changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7673 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7673 svn:date "2010-01-28T19:27:58.099030Z" $REPO_URL
$SVN_CMD --revprop -r 7673 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7676 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7676 svn:date "2010-01-28T19:36:42.628752Z" $REPO_URL
$SVN_CMD --revprop -r 7676 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7689 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7689 svn:date "2010-01-28T20:22:13.156049Z" $REPO_URL
$SVN_CMD --revprop -r 7689 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7692 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7692 svn:date "2010-01-28T20:24:55.476826Z" $REPO_URL
$SVN_CMD --revprop -r 7692 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7709 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 7709 svn:date "2010-01-28T21:21:33.544471Z" $REPO_URL
$SVN_CMD --revprop -r 7709 svn:log "Revert removal of lu rpc classes.  Apparently being used by LO screens." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7715 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7715 svn:date "2010-01-29T03:32:06.451912Z" $REPO_URL
$SVN_CMD --revprop -r 7715 svn:log "Got scraper working" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7727 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7727 svn:date "2010-01-30T18:07:26.477519Z" $REPO_URL
$SVN_CMD --revprop -r 7727 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7730 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7730 svn:date "2010-01-30T18:43:02.165334Z" $REPO_URL
$SVN_CMD --revprop -r 7730 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7732 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 7732 svn:date "2010-01-30T19:02:02.524051Z" $REPO_URL
$SVN_CMD --revprop -r 7732 svn:log "New commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7733 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 7733 svn:date "2010-02-01T17:00:55.795684Z" $REPO_URL
$SVN_CMD --revprop -r 7733 svn:log "[KSCOR-77] Initial work for converting service dictionary to metadata (only works for simple service object structures, e.g. Messages)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7751 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 7751 svn:date "2010-02-01T22:47:02.915283Z" $REPO_URL
$SVN_CMD --revprop -r 7751 svn:log "Added natural language usage types" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7759 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7759 svn:date "2010-02-02T19:08:54.818794Z" $REPO_URL
$SVN_CMD --revprop -r 7759 svn:log "temp put away of compare logic to work on refactoring for the abstract bean pattern" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7761 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 7761 svn:date "2010-02-02T19:47:46.459855Z" $REPO_URL
$SVN_CMD --revprop -r 7761 svn:log "Added cross search dtos and xsd enhancements, updated parser to read cross search" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7769 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 7769 svn:date "2010-02-02T23:24:38.029154Z" $REPO_URL
$SVN_CMD --revprop -r 7769 svn:log "Client Validation Update - New section layout, validation general" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7771 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7771 svn:date "2010-02-02T23:49:30.182753Z" $REPO_URL
$SVN_CMD --revprop -r 7771 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7796 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7796 svn:date "2010-02-03T16:18:39.082741Z" $REPO_URL
$SVN_CMD --revprop -r 7796 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7798 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7798 svn:date "2010-02-03T16:56:58.125035Z" $REPO_URL
$SVN_CMD --revprop -r 7798 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7800 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7800 svn:date "2010-02-03T17:01:35.050836Z" $REPO_URL
$SVN_CMD --revprop -r 7800 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7803 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7803 svn:date "2010-02-03T17:10:38.514638Z" $REPO_URL
$SVN_CMD --revprop -r 7803 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7804 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7804 svn:date "2010-02-03T17:11:37.417278Z" $REPO_URL
$SVN_CMD --revprop -r 7804 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7808 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7808 svn:date "2010-02-03T17:49:45.953295Z" $REPO_URL
$SVN_CMD --revprop -r 7808 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7814 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 7814 svn:date "2010-02-03T18:07:14.631523Z" $REPO_URL
$SVN_CMD --revprop -r 7814 svn:log "Move assembly classes from common-util module to common-impl module" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7840 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7840 svn:date "2010-02-04T03:01:04.870340Z" $REPO_URL
$SVN_CMD --revprop -r 7840 svn:log "temp put of abstract bean pattern half done types and states and dictionary left to write out" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7848 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7848 svn:date "2010-02-04T11:11:12.003573Z" $REPO_URL
$SVN_CMD --revprop -r 7848 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7852 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7852 svn:date "2010-02-04T12:24:36.618315Z" $REPO_URL
$SVN_CMD --revprop -r 7852 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7853 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7853 svn:date "2010-02-04T12:45:38.711766Z" $REPO_URL
$SVN_CMD --revprop -r 7853 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7870 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 7870 svn:date "2010-02-04T16:36:33.406250Z" $REPO_URL
$SVN_CMD --revprop -r 7870 svn:log "updated sql files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7877 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 7877 svn:date "2010-02-04T20:11:08.518108Z" $REPO_URL
$SVN_CMD --revprop -r 7877 svn:log "Added database entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7879 svn:author "rballew" $REPO_URL
$SVN_CMD --revprop -r 7879 svn:date "2010-02-04T20:26:35.887409Z" $REPO_URL
$SVN_CMD --revprop -r 7879 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7887 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7887 svn:date "2010-02-05T13:24:57.561405Z" $REPO_URL
$SVN_CMD --revprop -r 7887 svn:log "anoterh temp put of abstract bean pattern half done types and states and dictionary left to write out" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7891 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 7891 svn:date "2010-02-05T20:10:12.952731Z" $REPO_URL
$SVN_CMD --revprop -r 7891 svn:log "Added as part of CluFeeRecord update for LuService" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7892 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 7892 svn:date "2010-02-05T20:11:28.562481Z" $REPO_URL
$SVN_CMD --revprop -r 7892 svn:log "Added as part of CluFeeRecord update for LuService" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7894 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 7894 svn:date "2010-02-05T21:28:43.131096Z" $REPO_URL
$SVN_CMD --revprop -r 7894 svn:log "this the dynamic table from Wil. It will be moved to ks-core." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7900 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 7900 svn:date "2010-02-06T01:33:51.933241Z" $REPO_URL
$SVN_CMD --revprop -r 7900 svn:log "Statement service update statement tree view." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7903 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 7903 svn:date "2010-02-06T06:25:12.818951Z" $REPO_URL
$SVN_CMD --revprop -r 7903 svn:log "Updated UI side to use the new statement objects." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7943 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 7943 svn:date "2010-02-09T22:06:19.028935Z" $REPO_URL
$SVN_CMD --revprop -r 7943 svn:log "yet anotehr partial put away to make sure I don't lose stuff" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7959 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 7959 svn:date "2010-02-10T19:11:04.565892Z" $REPO_URL
$SVN_CMD --revprop -r 7959 svn:log "Add ability for devs to use a browser specific gwt module xml file def to reduce gwt compile permutations and speed up compile time." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7968 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 7968 svn:date "2010-02-10T23:22:01.907428Z" $REPO_URL
$SVN_CMD --revprop -r 7968 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7978 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 7978 svn:date "2010-02-11T04:04:33.207951Z" $REPO_URL
$SVN_CMD --revprop -r 7978 svn:log "New search widget (KSPicker) with suggest box configuration based on metadata. work in progress." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7980 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 7980 svn:date "2010-02-11T08:00:58.932994Z" $REPO_URL
$SVN_CMD --revprop -r 7980 svn:log "Added translation of statements and separated statement configuration files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 7993 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 7993 svn:date "2010-02-11T19:49:34.705289Z" $REPO_URL
$SVN_CMD --revprop -r 7993 svn:log "
Updated to new LuService and other changes (KSSTMT-related) needed.
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8017 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8017 svn:date "2010-02-12T01:01:15.716655Z" $REPO_URL
$SVN_CMD --revprop -r 8017 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8020 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8020 svn:date "2010-02-12T01:05:44.827615Z" $REPO_URL
$SVN_CMD --revprop -r 8020 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8037 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8037 svn:date "2010-02-12T04:44:09.592804Z" $REPO_URL
$SVN_CMD --revprop -r 8037 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8045 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8045 svn:date "2010-02-12T13:17:26.640886Z" $REPO_URL
$SVN_CMD --revprop -r 8045 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8058 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 8058 svn:date "2010-02-12T17:55:55.488543Z" $REPO_URL
$SVN_CMD --revprop -r 8058 svn:log "Added LU service configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8066 svn:author "gstruthe" $REPO_URL
$SVN_CMD --revprop -r 8066 svn:date "2010-02-12T19:13:59.803078Z" $REPO_URL
$SVN_CMD --revprop -r 8066 svn:log "Style changes to LO builder" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8068 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 8068 svn:date "2010-02-12T21:10:15.158845Z" $REPO_URL
$SVN_CMD --revprop -r 8068 svn:log "Updated statement configurations and added calls to the new StatementService statement/reqComponent translation methods." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8072 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 8072 svn:date "2010-02-12T23:59:24.096483Z" $REPO_URL
$SVN_CMD --revprop -r 8072 svn:log "Removed LU statement configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8073 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 8073 svn:date "2010-02-13T00:01:01.704904Z" $REPO_URL
$SVN_CMD --revprop -r 8073 svn:log "Added LU statement configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8110 svn:author "ssim" $REPO_URL
$SVN_CMD --revprop -r 8110 svn:date "2010-02-15T04:24:42.009639Z" $REPO_URL
$SVN_CMD --revprop -r 8110 svn:log "initial baseline from dev" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8119 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8119 svn:date "2010-02-15T16:20:17.164118Z" $REPO_URL
$SVN_CMD --revprop -r 8119 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8219 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8219 svn:date "2010-02-16T01:07:10.827248Z" $REPO_URL
$SVN_CMD --revprop -r 8219 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8221 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 8221 svn:date "2010-02-16T04:06:49.431124Z" $REPO_URL
$SVN_CMD --revprop -r 8221 svn:log "and yet anotehr partial put away to make sure I don't lose stuff" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8222 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8222 svn:date "2010-02-16T05:50:11.180026Z" $REPO_URL
$SVN_CMD --revprop -r 8222 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8282 svn:author "ssim" $REPO_URL
$SVN_CMD --revprop -r 8282 svn:date "2010-02-16T21:05:19.515416Z" $REPO_URL
$SVN_CMD --revprop -r 8282 svn:log "Template from UX Design" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8306 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8306 svn:date "2010-02-16T22:37:52.756214Z" $REPO_URL
$SVN_CMD --revprop -r 8306 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8308 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8308 svn:date "2010-02-16T22:47:16.584832Z" $REPO_URL
$SVN_CMD --revprop -r 8308 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8310 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 8310 svn:date "2010-02-16T23:20:51.225994Z" $REPO_URL
$SVN_CMD --revprop -r 8310 svn:log "finally got it done" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8321 svn:author "ssim" $REPO_URL
$SVN_CMD --revprop -r 8321 svn:date "2010-02-17T02:30:10.326154Z" $REPO_URL
$SVN_CMD --revprop -r 8321 svn:log "wireframes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8323 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 8323 svn:date "2010-02-17T07:15:46.235574Z" $REPO_URL
$SVN_CMD --revprop -r 8323 svn:log "Service Level Validation Fixes for M4.
1. ValidationResultContainer deprecated
2. List<ValidationResultInfo> new return type for validateTypeState" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8324 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 8324 svn:date "2010-02-17T07:18:23.396053Z" $REPO_URL
$SVN_CMD --revprop -r 8324 svn:log "Service Level Validation Fixes for M4.
Updated luservice impl to inject validator and validate clu message structures. " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8336 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8336 svn:date "2010-02-17T14:07:44.526393Z" $REPO_URL
$SVN_CMD --revprop -r 8336 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8428 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8428 svn:date "2010-02-17T21:22:01.108197Z" $REPO_URL
$SVN_CMD --revprop -r 8428 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8560 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8560 svn:date "2010-02-18T03:47:19.913708Z" $REPO_URL
$SVN_CMD --revprop -r 8560 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8609 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8609 svn:date "2010-02-18T16:01:44.666945Z" $REPO_URL
$SVN_CMD --revprop -r 8609 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8624 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8624 svn:date "2010-02-18T17:40:33.914595Z" $REPO_URL
$SVN_CMD --revprop -r 8624 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8633 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8633 svn:date "2010-02-18T17:50:06.881336Z" $REPO_URL
$SVN_CMD --revprop -r 8633 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8638 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 8638 svn:date "2010-02-18T19:06:36.822586Z" $REPO_URL
$SVN_CMD --revprop -r 8638 svn:log "More of Dan's fixes 
http://spreadsheets.google.com/ccc?key=0AvVLjful5tlbdGhocXJrZkJiVXpwMmVPSWJMNWpHUEE&hl=en" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8639 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 8639 svn:date "2010-02-18T19:12:44.141022Z" $REPO_URL
$SVN_CMD --revprop -r 8639 svn:log "synced with the version in lum-web." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8648 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 8648 svn:date "2010-02-18T21:29:48.965246Z" $REPO_URL
$SVN_CMD --revprop -r 8648 svn:log "Added natural language tests and cleanup code" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8665 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 8665 svn:date "2010-02-18T23:28:56.466210Z" $REPO_URL
$SVN_CMD --revprop -r 8665 svn:log "added norms new dictionary config, updated parser" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8681 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8681 svn:date "2010-02-19T00:01:36.182963Z" $REPO_URL
$SVN_CMD --revprop -r 8681 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8683 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8683 svn:date "2010-02-19T00:01:50.241074Z" $REPO_URL
$SVN_CMD --revprop -r 8683 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8734 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8734 svn:date "2010-02-19T03:17:03.039189Z" $REPO_URL
$SVN_CMD --revprop -r 8734 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8756 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 8756 svn:date "2010-02-19T18:29:12.403433Z" $REPO_URL
$SVN_CMD --revprop -r 8756 svn:log "Organization Screen - Adding Person Relations to Positions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8842 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 8842 svn:date "2010-02-20T04:46:19.592556Z" $REPO_URL
$SVN_CMD --revprop -r 8842 svn:log "KSLUM-86
KSLUM-91
plus, finally (duh, Jim) check in jaxws-generated messages" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8847 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8847 svn:date "2010-02-20T18:06:53.663843Z" $REPO_URL
$SVN_CMD --revprop -r 8847 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8851 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8851 svn:date "2010-02-20T18:08:20.103471Z" $REPO_URL
$SVN_CMD --revprop -r 8851 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8855 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8855 svn:date "2010-02-20T18:09:46.613617Z" $REPO_URL
$SVN_CMD --revprop -r 8855 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8863 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8863 svn:date "2010-02-20T18:33:45.936272Z" $REPO_URL
$SVN_CMD --revprop -r 8863 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8870 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8870 svn:date "2010-02-20T18:43:03.332108Z" $REPO_URL
$SVN_CMD --revprop -r 8870 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8883 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8883 svn:date "2010-02-20T18:50:53.793639Z" $REPO_URL
$SVN_CMD --revprop -r 8883 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8894 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8894 svn:date "2010-02-20T18:56:10.480073Z" $REPO_URL
$SVN_CMD --revprop -r 8894 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8900 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8900 svn:date "2010-02-20T19:02:52.499968Z" $REPO_URL
$SVN_CMD --revprop -r 8900 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8909 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8909 svn:date "2010-02-20T19:06:42.565616Z" $REPO_URL
$SVN_CMD --revprop -r 8909 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8924 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8924 svn:date "2010-02-20T19:27:22.440934Z" $REPO_URL
$SVN_CMD --revprop -r 8924 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8935 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8935 svn:date "2010-02-20T19:34:56.428282Z" $REPO_URL
$SVN_CMD --revprop -r 8935 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8953 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8953 svn:date "2010-02-20T19:59:57.493405Z" $REPO_URL
$SVN_CMD --revprop -r 8953 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8960 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8960 svn:date "2010-02-20T20:09:17.450054Z" $REPO_URL
$SVN_CMD --revprop -r 8960 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8962 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8962 svn:date "2010-02-20T20:09:29.950361Z" $REPO_URL
$SVN_CMD --revprop -r 8962 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8973 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8973 svn:date "2010-02-20T20:27:33.412385Z" $REPO_URL
$SVN_CMD --revprop -r 8973 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8979 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8979 svn:date "2010-02-20T20:29:28.087437Z" $REPO_URL
$SVN_CMD --revprop -r 8979 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8984 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8984 svn:date "2010-02-20T20:50:28.759720Z" $REPO_URL
$SVN_CMD --revprop -r 8984 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8990 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8990 svn:date "2010-02-20T21:02:04.441222Z" $REPO_URL
$SVN_CMD --revprop -r 8990 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 8996 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 8996 svn:date "2010-02-20T21:22:10.321996Z" $REPO_URL
$SVN_CMD --revprop -r 8996 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9006 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9006 svn:date "2010-02-20T21:25:49.019645Z" $REPO_URL
$SVN_CMD --revprop -r 9006 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9024 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9024 svn:date "2010-02-20T22:02:21.949267Z" $REPO_URL
$SVN_CMD --revprop -r 9024 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9032 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9032 svn:date "2010-02-20T22:06:02.162201Z" $REPO_URL
$SVN_CMD --revprop -r 9032 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9034 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9034 svn:date "2010-02-20T22:06:14.272458Z" $REPO_URL
$SVN_CMD --revprop -r 9034 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9042 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9042 svn:date "2010-02-20T22:11:42.820474Z" $REPO_URL
$SVN_CMD --revprop -r 9042 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9047 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9047 svn:date "2010-02-20T22:12:34.965396Z" $REPO_URL
$SVN_CMD --revprop -r 9047 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9049 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9049 svn:date "2010-02-20T22:12:47.878312Z" $REPO_URL
$SVN_CMD --revprop -r 9049 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9050 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9050 svn:date "2010-02-20T22:12:58.335842Z" $REPO_URL
$SVN_CMD --revprop -r 9050 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9069 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9069 svn:date "2010-02-20T23:01:54.152914Z" $REPO_URL
$SVN_CMD --revprop -r 9069 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9071 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9071 svn:date "2010-02-20T23:02:06.986963Z" $REPO_URL
$SVN_CMD --revprop -r 9071 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9074 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9074 svn:date "2010-02-20T23:02:42.866659Z" $REPO_URL
$SVN_CMD --revprop -r 9074 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9077 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9077 svn:date "2010-02-20T23:03:05.820566Z" $REPO_URL
$SVN_CMD --revprop -r 9077 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9083 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9083 svn:date "2010-02-20T23:33:42.294543Z" $REPO_URL
$SVN_CMD --revprop -r 9083 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9086 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9086 svn:date "2010-02-20T23:34:07.964750Z" $REPO_URL
$SVN_CMD --revprop -r 9086 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9087 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9087 svn:date "2010-02-20T23:34:23.150751Z" $REPO_URL
$SVN_CMD --revprop -r 9087 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9090 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9090 svn:date "2010-02-20T23:34:45.742323Z" $REPO_URL
$SVN_CMD --revprop -r 9090 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9091 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9091 svn:date "2010-02-20T23:34:56.314123Z" $REPO_URL
$SVN_CMD --revprop -r 9091 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9102 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9102 svn:date "2010-02-20T23:48:57.958614Z" $REPO_URL
$SVN_CMD --revprop -r 9102 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9112 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9112 svn:date "2010-02-20T23:53:09.475500Z" $REPO_URL
$SVN_CMD --revprop -r 9112 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9125 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9125 svn:date "2010-02-21T00:14:06.241280Z" $REPO_URL
$SVN_CMD --revprop -r 9125 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9127 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9127 svn:date "2010-02-21T00:14:18.459705Z" $REPO_URL
$SVN_CMD --revprop -r 9127 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9132 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9132 svn:date "2010-02-21T00:15:05.020556Z" $REPO_URL
$SVN_CMD --revprop -r 9132 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9133 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9133 svn:date "2010-02-21T00:15:14.719282Z" $REPO_URL
$SVN_CMD --revprop -r 9133 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9139 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9139 svn:date "2010-02-21T00:21:23.225228Z" $REPO_URL
$SVN_CMD --revprop -r 9139 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9146 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9146 svn:date "2010-02-21T00:23:25.886790Z" $REPO_URL
$SVN_CMD --revprop -r 9146 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9150 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9150 svn:date "2010-02-21T00:24:04.955864Z" $REPO_URL
$SVN_CMD --revprop -r 9150 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9153 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9153 svn:date "2010-02-21T00:24:28.433345Z" $REPO_URL
$SVN_CMD --revprop -r 9153 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9154 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9154 svn:date "2010-02-21T00:24:38.967534Z" $REPO_URL
$SVN_CMD --revprop -r 9154 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9168 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9168 svn:date "2010-02-21T00:48:56.966046Z" $REPO_URL
$SVN_CMD --revprop -r 9168 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9170 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9170 svn:date "2010-02-21T00:49:12.516617Z" $REPO_URL
$SVN_CMD --revprop -r 9170 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9181 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9181 svn:date "2010-02-21T00:59:01.092661Z" $REPO_URL
$SVN_CMD --revprop -r 9181 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9183 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9183 svn:date "2010-02-21T00:59:14.356521Z" $REPO_URL
$SVN_CMD --revprop -r 9183 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9199 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9199 svn:date "2010-02-21T01:17:12.804904Z" $REPO_URL
$SVN_CMD --revprop -r 9199 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9201 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9201 svn:date "2010-02-21T01:17:25.594739Z" $REPO_URL
$SVN_CMD --revprop -r 9201 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9202 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9202 svn:date "2010-02-21T01:17:37.226983Z" $REPO_URL
$SVN_CMD --revprop -r 9202 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9223 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9223 svn:date "2010-02-21T02:02:49.813546Z" $REPO_URL
$SVN_CMD --revprop -r 9223 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9226 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9226 svn:date "2010-02-21T02:03:12.972307Z" $REPO_URL
$SVN_CMD --revprop -r 9226 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9231 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9231 svn:date "2010-02-21T02:04:42.650872Z" $REPO_URL
$SVN_CMD --revprop -r 9231 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9234 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9234 svn:date "2010-02-21T02:05:06.670951Z" $REPO_URL
$SVN_CMD --revprop -r 9234 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9239 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9239 svn:date "2010-02-21T02:11:19.877400Z" $REPO_URL
$SVN_CMD --revprop -r 9239 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9242 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9242 svn:date "2010-02-21T02:11:43.529951Z" $REPO_URL
$SVN_CMD --revprop -r 9242 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9255 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9255 svn:date "2010-02-21T02:23:52.654051Z" $REPO_URL
$SVN_CMD --revprop -r 9255 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9258 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9258 svn:date "2010-02-21T02:24:18.048685Z" $REPO_URL
$SVN_CMD --revprop -r 9258 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9278 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9278 svn:date "2010-02-21T02:41:24.999817Z" $REPO_URL
$SVN_CMD --revprop -r 9278 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9281 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9281 svn:date "2010-02-21T02:41:49.012381Z" $REPO_URL
$SVN_CMD --revprop -r 9281 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9297 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9297 svn:date "2010-02-21T02:54:28.426674Z" $REPO_URL
$SVN_CMD --revprop -r 9297 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9300 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9300 svn:date "2010-02-21T02:54:54.033609Z" $REPO_URL
$SVN_CMD --revprop -r 9300 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9317 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9317 svn:date "2010-02-21T03:12:24.123562Z" $REPO_URL
$SVN_CMD --revprop -r 9317 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9320 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9320 svn:date "2010-02-21T03:12:48.878292Z" $REPO_URL
$SVN_CMD --revprop -r 9320 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9335 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9335 svn:date "2010-02-21T03:24:23.247697Z" $REPO_URL
$SVN_CMD --revprop -r 9335 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9338 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9338 svn:date "2010-02-21T03:24:46.507437Z" $REPO_URL
$SVN_CMD --revprop -r 9338 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9339 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9339 svn:date "2010-02-21T03:25:49.893250Z" $REPO_URL
$SVN_CMD --revprop -r 9339 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9361 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9361 svn:date "2010-02-21T04:00:17.380088Z" $REPO_URL
$SVN_CMD --revprop -r 9361 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9364 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9364 svn:date "2010-02-21T04:00:41.309558Z" $REPO_URL
$SVN_CMD --revprop -r 9364 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9366 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9366 svn:date "2010-02-21T04:01:07.405212Z" $REPO_URL
$SVN_CMD --revprop -r 9366 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9369 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9369 svn:date "2010-02-21T04:01:34.348971Z" $REPO_URL
$SVN_CMD --revprop -r 9369 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9372 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9372 svn:date "2010-02-21T04:02:00.773099Z" $REPO_URL
$SVN_CMD --revprop -r 9372 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9373 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9373 svn:date "2010-02-21T04:02:14.288764Z" $REPO_URL
$SVN_CMD --revprop -r 9373 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9375 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9375 svn:date "2010-02-21T04:02:29.795731Z" $REPO_URL
$SVN_CMD --revprop -r 9375 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9376 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9376 svn:date "2010-02-21T04:02:43.784408Z" $REPO_URL
$SVN_CMD --revprop -r 9376 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9378 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9378 svn:date "2010-02-21T04:02:59.590609Z" $REPO_URL
$SVN_CMD --revprop -r 9378 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9379 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9379 svn:date "2010-02-21T04:03:12.822470Z" $REPO_URL
$SVN_CMD --revprop -r 9379 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9381 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9381 svn:date "2010-02-21T04:03:29.101174Z" $REPO_URL
$SVN_CMD --revprop -r 9381 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9382 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9382 svn:date "2010-02-21T04:03:44.907333Z" $REPO_URL
$SVN_CMD --revprop -r 9382 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9384 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9384 svn:date "2010-02-21T04:04:01.267699Z" $REPO_URL
$SVN_CMD --revprop -r 9384 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9385 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9385 svn:date "2010-02-21T04:04:16.558816Z" $REPO_URL
$SVN_CMD --revprop -r 9385 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9387 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9387 svn:date "2010-02-21T04:04:32.416633Z" $REPO_URL
$SVN_CMD --revprop -r 9387 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9407 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9407 svn:date "2010-02-21T04:46:45.699921Z" $REPO_URL
$SVN_CMD --revprop -r 9407 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9410 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9410 svn:date "2010-02-21T04:47:11.387163Z" $REPO_URL
$SVN_CMD --revprop -r 9410 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9412 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9412 svn:date "2010-02-21T04:47:37.715612Z" $REPO_URL
$SVN_CMD --revprop -r 9412 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9415 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9415 svn:date "2010-02-21T04:48:07.002556Z" $REPO_URL
$SVN_CMD --revprop -r 9415 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9418 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9418 svn:date "2010-02-21T04:48:36.876853Z" $REPO_URL
$SVN_CMD --revprop -r 9418 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9419 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9419 svn:date "2010-02-21T04:48:49.799607Z" $REPO_URL
$SVN_CMD --revprop -r 9419 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9421 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9421 svn:date "2010-02-21T04:49:03.614727Z" $REPO_URL
$SVN_CMD --revprop -r 9421 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9422 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9422 svn:date "2010-02-21T04:49:18.569021Z" $REPO_URL
$SVN_CMD --revprop -r 9422 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9424 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9424 svn:date "2010-02-21T04:49:36.808847Z" $REPO_URL
$SVN_CMD --revprop -r 9424 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9425 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9425 svn:date "2010-02-21T04:49:51.417756Z" $REPO_URL
$SVN_CMD --revprop -r 9425 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9427 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9427 svn:date "2010-02-21T04:50:05.169067Z" $REPO_URL
$SVN_CMD --revprop -r 9427 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9428 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9428 svn:date "2010-02-21T04:50:19.031178Z" $REPO_URL
$SVN_CMD --revprop -r 9428 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9430 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9430 svn:date "2010-02-21T04:50:34.138035Z" $REPO_URL
$SVN_CMD --revprop -r 9430 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9431 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9431 svn:date "2010-02-21T04:50:49.360173Z" $REPO_URL
$SVN_CMD --revprop -r 9431 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9433 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9433 svn:date "2010-02-21T04:51:04.438428Z" $REPO_URL
$SVN_CMD --revprop -r 9433 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9453 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9453 svn:date "2010-02-21T05:00:04.142617Z" $REPO_URL
$SVN_CMD --revprop -r 9453 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9457 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9457 svn:date "2010-02-21T05:00:46.526229Z" $REPO_URL
$SVN_CMD --revprop -r 9457 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9460 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9460 svn:date "2010-02-21T05:01:13.286371Z" $REPO_URL
$SVN_CMD --revprop -r 9460 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9467 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9467 svn:date "2010-02-21T06:32:58.973797Z" $REPO_URL
$SVN_CMD --revprop -r 9467 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9470 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9470 svn:date "2010-02-21T06:33:21.064202Z" $REPO_URL
$SVN_CMD --revprop -r 9470 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9492 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9492 svn:date "2010-02-21T07:12:16.665901Z" $REPO_URL
$SVN_CMD --revprop -r 9492 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9495 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9495 svn:date "2010-02-21T07:12:39.516069Z" $REPO_URL
$SVN_CMD --revprop -r 9495 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9500 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9500 svn:date "2010-02-21T07:14:31.894549Z" $REPO_URL
$SVN_CMD --revprop -r 9500 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9503 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9503 svn:date "2010-02-21T07:14:55.154289Z" $REPO_URL
$SVN_CMD --revprop -r 9503 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9529 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9529 svn:date "2010-02-21T08:26:39.407237Z" $REPO_URL
$SVN_CMD --revprop -r 9529 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9532 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9532 svn:date "2010-02-21T08:27:03.412305Z" $REPO_URL
$SVN_CMD --revprop -r 9532 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9561 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9561 svn:date "2010-02-21T08:48:00.194121Z" $REPO_URL
$SVN_CMD --revprop -r 9561 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9567 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9567 svn:date "2010-02-21T08:49:49.451584Z" $REPO_URL
$SVN_CMD --revprop -r 9567 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9578 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9578 svn:date "2010-02-21T08:54:20.681941Z" $REPO_URL
$SVN_CMD --revprop -r 9578 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9594 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9594 svn:date "2010-02-21T15:53:15.131680Z" $REPO_URL
$SVN_CMD --revprop -r 9594 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9597 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9597 svn:date "2010-02-21T15:53:38.778169Z" $REPO_URL
$SVN_CMD --revprop -r 9597 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9602 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9602 svn:date "2010-02-21T15:58:25.782566Z" $REPO_URL
$SVN_CMD --revprop -r 9602 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9605 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9605 svn:date "2010-02-21T15:58:48.841404Z" $REPO_URL
$SVN_CMD --revprop -r 9605 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9610 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9610 svn:date "2010-02-21T16:02:50.793450Z" $REPO_URL
$SVN_CMD --revprop -r 9610 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9613 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9613 svn:date "2010-02-21T16:03:17.520069Z" $REPO_URL
$SVN_CMD --revprop -r 9613 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9614 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9614 svn:date "2010-02-21T16:03:30.573078Z" $REPO_URL
$SVN_CMD --revprop -r 9614 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9632 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9632 svn:date "2010-02-21T16:17:33.729804Z" $REPO_URL
$SVN_CMD --revprop -r 9632 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9633 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9633 svn:date "2010-02-21T16:17:47.632131Z" $REPO_URL
$SVN_CMD --revprop -r 9633 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9634 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9634 svn:date "2010-02-21T16:17:59.596638Z" $REPO_URL
$SVN_CMD --revprop -r 9634 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9640 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9640 svn:date "2010-02-21T17:08:30.235412Z" $REPO_URL
$SVN_CMD --revprop -r 9640 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9648 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9648 svn:date "2010-02-21T18:28:57.450329Z" $REPO_URL
$SVN_CMD --revprop -r 9648 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9651 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9651 svn:date "2010-02-21T18:29:23.770441Z" $REPO_URL
$SVN_CMD --revprop -r 9651 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9652 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9652 svn:date "2010-02-21T18:29:35.305510Z" $REPO_URL
$SVN_CMD --revprop -r 9652 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9655 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9655 svn:date "2010-02-21T18:30:12.626299Z" $REPO_URL
$SVN_CMD --revprop -r 9655 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9661 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9661 svn:date "2010-02-21T18:35:25.580241Z" $REPO_URL
$SVN_CMD --revprop -r 9661 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9667 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9667 svn:date "2010-02-21T18:36:26.213204Z" $REPO_URL
$SVN_CMD --revprop -r 9667 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9668 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9668 svn:date "2010-02-21T18:36:41.823288Z" $REPO_URL
$SVN_CMD --revprop -r 9668 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9671 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9671 svn:date "2010-02-21T18:37:09.433095Z" $REPO_URL
$SVN_CMD --revprop -r 9671 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9682 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9682 svn:date "2010-02-21T18:48:41.653264Z" $REPO_URL
$SVN_CMD --revprop -r 9682 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9688 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9688 svn:date "2010-02-21T18:49:43.465118Z" $REPO_URL
$SVN_CMD --revprop -r 9688 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9689 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9689 svn:date "2010-02-21T18:50:01.636126Z" $REPO_URL
$SVN_CMD --revprop -r 9689 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9691 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9691 svn:date "2010-02-21T18:50:19.589742Z" $REPO_URL
$SVN_CMD --revprop -r 9691 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9716 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9716 svn:date "2010-02-22T00:37:50.600566Z" $REPO_URL
$SVN_CMD --revprop -r 9716 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9719 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9719 svn:date "2010-02-22T00:38:14.245127Z" $REPO_URL
$SVN_CMD --revprop -r 9719 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9720 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9720 svn:date "2010-02-22T00:38:25.696266Z" $REPO_URL
$SVN_CMD --revprop -r 9720 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9729 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 9729 svn:date "2010-02-22T01:38:31.321613Z" $REPO_URL
$SVN_CMD --revprop -r 9729 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9736 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 9736 svn:date "2010-02-22T04:07:44.119954Z" $REPO_URL
$SVN_CMD --revprop -r 9736 svn:log "more refactoring and cleaning up" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9843 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 9843 svn:date "2010-02-23T15:47:43.114083Z" $REPO_URL
$SVN_CMD --revprop -r 9843 svn:log "Move Statement.gwt.xml to statement-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9848 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 9848 svn:date "2010-02-23T18:57:07.342283Z" $REPO_URL
$SVN_CMD --revprop -r 9848 svn:log "[KSCOR-102] Configured JAXBConfigImpl and reworked context property place holders and property files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9864 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 9864 svn:date "2010-02-24T16:06:36.073016Z" $REPO_URL
$SVN_CMD --revprop -r 9864 svn:log "updated lum data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9887 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 9887 svn:date "2010-02-25T14:04:42.595448Z" $REPO_URL
$SVN_CMD --revprop -r 9887 svn:log "Renamed DTO to Bean" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9888 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 9888 svn:date "2010-02-25T14:46:37.658965Z" $REPO_URL
$SVN_CMD --revprop -r 9888 svn:log "put away provisional Java api so Andy and Scott can review" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9904 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 9904 svn:date "2010-02-25T23:01:14.269804Z" $REPO_URL
$SVN_CMD --revprop -r 9904 svn:log "fixed bug in dynamic attribute processing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9905 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 9905 svn:date "2010-02-25T23:14:03.926824Z" $REPO_URL
$SVN_CMD --revprop -r 9905 svn:log "put away seth's dictionary changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9912 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 9912 svn:date "2010-02-26T15:56:42.790303Z" $REPO_URL
$SVN_CMD --revprop -r 9912 svn:log "Cleanup some empty packages
refactored rols service to proper package name
added in some refactoring of dictionary/search data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9915 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 9915 svn:date "2010-02-26T16:16:18.766733Z" $REPO_URL
$SVN_CMD --revprop -r 9915 svn:log "added missing jaxws wrapper beans and updated wsdls" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 9927 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 9927 svn:date "2010-02-26T20:00:48.876293Z" $REPO_URL
$SVN_CMD --revprop -r 9927 svn:log "added ksstmt data and removed old kslu stmt/req" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10028 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10028 svn:date "2010-03-01T05:33:09.237062Z" $REPO_URL
$SVN_CMD --revprop -r 10028 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10032 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10032 svn:date "2010-03-01T06:52:18.264168Z" $REPO_URL
$SVN_CMD --revprop -r 10032 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10033 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10033 svn:date "2010-03-01T08:12:39.310049Z" $REPO_URL
$SVN_CMD --revprop -r 10033 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10037 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10037 svn:date "2010-03-01T08:30:02.010006Z" $REPO_URL
$SVN_CMD --revprop -r 10037 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10042 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 10042 svn:date "2010-03-01T14:19:45.521784Z" $REPO_URL
$SVN_CMD --revprop -r 10042 svn:log "added eclipse settings and svn ignore for targets" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10049 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10049 svn:date "2010-03-01T20:38:57.446950Z" $REPO_URL
$SVN_CMD --revprop -r 10049 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10054 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10054 svn:date "2010-03-01T21:19:22.542083Z" $REPO_URL
$SVN_CMD --revprop -r 10054 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10109 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10109 svn:date "2010-03-01T23:00:06.955555Z" $REPO_URL
$SVN_CMD --revprop -r 10109 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10155 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10155 svn:date "2010-03-02T18:31:39.488851Z" $REPO_URL
$SVN_CMD --revprop -r 10155 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10167 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10167 svn:date "2010-03-02T19:30:54.641612Z" $REPO_URL
$SVN_CMD --revprop -r 10167 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10180 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10180 svn:date "2010-03-02T19:58:45.243240Z" $REPO_URL
$SVN_CMD --revprop -r 10180 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10183 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10183 svn:date "2010-03-02T19:59:08.597029Z" $REPO_URL
$SVN_CMD --revprop -r 10183 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10188 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10188 svn:date "2010-03-02T20:00:34.428094Z" $REPO_URL
$SVN_CMD --revprop -r 10188 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10191 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10191 svn:date "2010-03-02T20:01:04.697974Z" $REPO_URL
$SVN_CMD --revprop -r 10191 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10198 svn:author "ssim" $REPO_URL
$SVN_CMD --revprop -r 10198 svn:date "2010-03-02T20:35:12.956974Z" $REPO_URL
$SVN_CMD --revprop -r 10198 svn:log "Initial GWT import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10251 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10251 svn:date "2010-03-02T23:27:26.156296Z" $REPO_URL
$SVN_CMD --revprop -r 10251 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10270 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 10270 svn:date "2010-03-04T19:18:38.729233Z" $REPO_URL
$SVN_CMD --revprop -r 10270 svn:log "Adding Theme support to Organization screen. Adding Org.CSS. " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10284 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10284 svn:date "2010-03-05T00:59:32.966321Z" $REPO_URL
$SVN_CMD --revprop -r 10284 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10300 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10300 svn:date "2010-03-05T01:58:55.860471Z" $REPO_URL
$SVN_CMD --revprop -r 10300 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10319 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10319 svn:date "2010-03-05T16:59:55.563941Z" $REPO_URL
$SVN_CMD --revprop -r 10319 svn:log "committed the generated source Refactorme xxxHelpers xxxConstants and xxxMetadata to match latest Orchestration Dictionary that contains the new FEE structures plus reving service structure helpers.
Modified generators to generate code that was manually added to the helper classes. i.e. copyOfCourseId to course and Dirty to RunTimeData" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10320 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10320 svn:date "2010-03-05T17:01:59.798228Z" $REPO_URL
$SVN_CMD --revprop -r 10320 svn:log "committed the generated source Refactorme xxxHelpers xxxConstants and xxxMetadata to match latest Orchestration Dictionary that contains the new FEE structures plus reving service structure helpers.
Modified generators to generate code that was manually added to the helper classes. i.e. copyOfCourseId to course and Dirty to RunTimeData" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10325 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10325 svn:date "2010-03-05T19:20:28.532106Z" $REPO_URL
$SVN_CMD --revprop -r 10325 svn:log "Clu Set stuff create by Hugh" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10326 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10326 svn:date "2010-03-05T19:27:49.658727Z" $REPO_URL
$SVN_CMD --revprop -r 10326 svn:log "Clu Set stuff create by Hugh" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10334 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 10334 svn:date "2010-03-08T12:57:34.683958Z" $REPO_URL
$SVN_CMD --revprop -r 10334 svn:log "updating for statement table name changes and lo changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10338 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 10338 svn:date "2010-03-08T15:43:48.059961Z" $REPO_URL
$SVN_CMD --revprop -r 10338 svn:log "Adding Membership table to the Organization lookup tree" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10339 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 10339 svn:date "2010-03-08T16:04:40.828514Z" $REPO_URL
$SVN_CMD --revprop -r 10339 svn:log "updated with most recent core/lum/rice" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10345 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 10345 svn:date "2010-03-08T21:26:34.496843Z" $REPO_URL
$SVN_CMD --revprop -r 10345 svn:log "Initial Modify Course" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10346 svn:author "rjjohnson84" $REPO_URL
$SVN_CMD --revprop -r 10346 svn:date "2010-03-08T21:40:08.035395Z" $REPO_URL
$SVN_CMD --revprop -r 10346 svn:log "[KSCOR-119] Added getDefaultMetadata to Assembler interface and all implementors; added IdTranslator, IdTranslation, and IdTranslatorAssemblerFilter to translate IDs in DOL to prevent initial search from pickers; Added temporary code to HasDataValueBinding to pull translations from the runtimeData" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10351 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 10351 svn:date "2010-03-09T04:22:14.876526Z" $REPO_URL
$SVN_CMD --revprop -r 10351 svn:log "Added initial structure for CLU Sets Management tool. Additional tools like LO Management or LR Management can be added to this. For now using tab view to access management screens (later to be replaced with a new view used specifically for tools)." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10356 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 10356 svn:date "2010-03-09T12:39:24.832440Z" $REPO_URL
$SVN_CMD --revprop -r 10356 svn:log "update for rich_text refactor" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10359 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10359 svn:date "2010-03-09T18:58:03.888411Z" $REPO_URL
$SVN_CMD --revprop -r 10359 svn:log "add spring nature and spring beans" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10365 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10365 svn:date "2010-03-09T21:16:54.870666Z" $REPO_URL
$SVN_CMD --revprop -r 10365 svn:log "added more services" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10371 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10371 svn:date "2010-03-10T07:27:24.487122Z" $REPO_URL
$SVN_CMD --revprop -r 10371 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10385 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10385 svn:date "2010-03-10T18:33:28.249487Z" $REPO_URL
$SVN_CMD --revprop -r 10385 svn:log "hugh's changes or orchestration dictionary - plus better logic for calculating constants resulted in some changes to configurers as well L_OS to LOS" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10386 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10386 svn:date "2010-03-10T18:38:04.628570Z" $REPO_URL
$SVN_CMD --revprop -r 10386 svn:log "hugh's changes or orchestration dictionary - plus better logic for calculating constants resulted in some changes to configurers as well L_OS to LOS" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10402 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10402 svn:date "2010-03-10T21:07:50.305094Z" $REPO_URL
$SVN_CMD --revprop -r 10402 svn:log "added org message structures so we can configure them" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10408 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 10408 svn:date "2010-03-11T04:52:06.928373Z" $REPO_URL
$SVN_CMD --revprop -r 10408 svn:log "Added org" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10413 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 10413 svn:date "2010-03-11T15:43:10.199583Z" $REPO_URL
$SVN_CMD --revprop -r 10413 svn:log "[KSCOR-120] Update header nav drop down to match UIM" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10422 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10422 svn:date "2010-03-11T21:54:57.563763Z" $REPO_URL
$SVN_CMD --revprop -r 10422 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10438 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 10438 svn:date "2010-03-12T15:24:44.203353Z" $REPO_URL
$SVN_CMD --revprop -r 10438 svn:log "KSWEB-7 override KSBClientProxy to unwrap InvocationExceptions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10441 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 10441 svn:date "2010-03-12T17:01:17.065830Z" $REPO_URL
$SVN_CMD --revprop -r 10441 svn:log "Initial version of workflow drop down." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10483 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10483 svn:date "2010-03-15T22:11:03.386048Z" $REPO_URL
$SVN_CMD --revprop -r 10483 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10486 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10486 svn:date "2010-03-15T22:23:27.977511Z" $REPO_URL
$SVN_CMD --revprop -r 10486 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10488 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10488 svn:date "2010-03-15T22:34:33.952913Z" $REPO_URL
$SVN_CMD --revprop -r 10488 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10532 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10532 svn:date "2010-03-16T16:26:15.189303Z" $REPO_URL
$SVN_CMD --revprop -r 10532 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10556 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10556 svn:date "2010-03-17T21:10:46.545055Z" $REPO_URL
$SVN_CMD --revprop -r 10556 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10563 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10563 svn:date "2010-03-18T17:18:07.382532Z" $REPO_URL
$SVN_CMD --revprop -r 10563 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10568 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10568 svn:date "2010-03-18T18:04:11.224948Z" $REPO_URL
$SVN_CMD --revprop -r 10568 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10571 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10571 svn:date "2010-03-18T18:31:01.738906Z" $REPO_URL
$SVN_CMD --revprop -r 10571 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10603 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 10603 svn:date "2010-03-18T21:53:13.115298Z" $REPO_URL
$SVN_CMD --revprop -r 10603 svn:log "New field layout widgets, first cut, work in progress" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10636 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10636 svn:date "2010-03-19T02:34:19.649095Z" $REPO_URL
$SVN_CMD --revprop -r 10636 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10642 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10642 svn:date "2010-03-19T04:11:07.995896Z" $REPO_URL
$SVN_CMD --revprop -r 10642 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10643 svn:author "ssim" $REPO_URL
$SVN_CMD --revprop -r 10643 svn:date "2010-03-19T09:35:18.229364Z" $REPO_URL
$SVN_CMD --revprop -r 10643 svn:log "base line checkin " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10644 svn:author "wiljohns" $REPO_URL
$SVN_CMD --revprop -r 10644 svn:date "2010-03-19T14:40:56.154657Z" $REPO_URL
$SVN_CMD --revprop -r 10644 svn:log "implemented KSNotifier, KSNotification" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10663 svn:author "kcampos" $REPO_URL
$SVN_CMD --revprop -r 10663 svn:date "2010-03-19T21:36:02.866200Z" $REPO_URL
$SVN_CMD --revprop -r 10663 svn:log "added org/curriculm shell functionality" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10687 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10687 svn:date "2010-03-20T18:38:22.519013Z" $REPO_URL
$SVN_CMD --revprop -r 10687 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10696 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10696 svn:date "2010-03-20T18:42:24.548063Z" $REPO_URL
$SVN_CMD --revprop -r 10696 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10702 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10702 svn:date "2010-03-20T18:43:07.634263Z" $REPO_URL
$SVN_CMD --revprop -r 10702 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10727 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10727 svn:date "2010-03-20T18:54:47.222654Z" $REPO_URL
$SVN_CMD --revprop -r 10727 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10736 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10736 svn:date "2010-03-20T18:58:28.602495Z" $REPO_URL
$SVN_CMD --revprop -r 10736 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10738 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10738 svn:date "2010-03-20T18:58:42.672214Z" $REPO_URL
$SVN_CMD --revprop -r 10738 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10748 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10748 svn:date "2010-03-20T19:21:53.695389Z" $REPO_URL
$SVN_CMD --revprop -r 10748 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10755 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10755 svn:date "2010-03-20T19:23:19.302287Z" $REPO_URL
$SVN_CMD --revprop -r 10755 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10757 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10757 svn:date "2010-03-20T19:23:33.312163Z" $REPO_URL
$SVN_CMD --revprop -r 10757 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10760 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10760 svn:date "2010-03-20T19:31:53.897702Z" $REPO_URL
$SVN_CMD --revprop -r 10760 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10767 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10767 svn:date "2010-03-20T19:35:46.183631Z" $REPO_URL
$SVN_CMD --revprop -r 10767 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10768 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10768 svn:date "2010-03-20T19:35:58.288164Z" $REPO_URL
$SVN_CMD --revprop -r 10768 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10770 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10770 svn:date "2010-03-20T19:36:24.638518Z" $REPO_URL
$SVN_CMD --revprop -r 10770 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10784 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10784 svn:date "2010-03-21T02:53:12.054666Z" $REPO_URL
$SVN_CMD --revprop -r 10784 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10795 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10795 svn:date "2010-03-21T03:30:37.967450Z" $REPO_URL
$SVN_CMD --revprop -r 10795 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10814 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 10814 svn:date "2010-03-22T15:43:04.086476Z" $REPO_URL
$SVN_CMD --revprop -r 10814 svn:log "[KSCOR-140] added config param for lum orchestration dictionary and ability to add multiple context file locations" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10826 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10826 svn:date "2010-03-22T20:19:01.750455Z" $REPO_URL
$SVN_CMD --revprop -r 10826 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10850 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10850 svn:date "2010-03-23T01:53:50.136975Z" $REPO_URL
$SVN_CMD --revprop -r 10850 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10852 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10852 svn:date "2010-03-23T01:54:10.741013Z" $REPO_URL
$SVN_CMD --revprop -r 10852 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 10856 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 10856 svn:date "2010-03-23T06:09:29.109148Z" $REPO_URL
$SVN_CMD --revprop -r 10856 svn:log "''" $REPO_URL
$SVN_CMD --revprop -r 11012 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 11012 svn:date "2010-03-23T18:55:58.244577Z" $REPO_URL
$SVN_CMD --revprop -r 11012 svn:log "KSCOR-144 - added use of \"comment on document\" permission
KSCOR-145 - disallow editing and deleting comments" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11025 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 11025 svn:date "2010-03-23T22:21:47.315095Z" $REPO_URL
$SVN_CMD --revprop -r 11025 svn:log "KSLUM-160: Create Proposal Picker. Added metadata for proposal search" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11026 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 11026 svn:date "2010-03-23T22:21:58.183583Z" $REPO_URL
$SVN_CMD --revprop -r 11026 svn:log "KSLUM-160: Create Proposal Picker. Added metadata for proposal search" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11029 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11029 svn:date "2010-03-23T23:29:00.715443Z" $REPO_URL
$SVN_CMD --revprop -r 11029 svn:log "''" $REPO_URL
$SVN_CMD --revprop -r 11043 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 11043 svn:date "2010-03-24T16:49:37.153023Z" $REPO_URL
$SVN_CMD --revprop -r 11043 svn:log "KSCOR-147 - adding new perm data for \"Upload to Document\"" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11044 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 11044 svn:date "2010-03-24T16:57:33.961347Z" $REPO_URL
$SVN_CMD --revprop -r 11044 svn:log "[KSLUM-181] Validate full document before allowing user to submit to workflow." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11051 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11051 svn:date "2010-03-24T19:36:17.355353Z" $REPO_URL
$SVN_CMD --revprop -r 11051 svn:log "added rice keystore to core web" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11052 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11052 svn:date "2010-03-24T19:36:39.633458Z" $REPO_URL
$SVN_CMD --revprop -r 11052 svn:log "added rice keystore to lum web" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11057 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11057 svn:date "2010-03-24T21:21:05.430321Z" $REPO_URL
$SVN_CMD --revprop -r 11057 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11082 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11082 svn:date "2010-03-25T01:13:56.555210Z" $REPO_URL
$SVN_CMD --revprop -r 11082 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11085 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11085 svn:date "2010-03-25T01:14:24.654420Z" $REPO_URL
$SVN_CMD --revprop -r 11085 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11086 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11086 svn:date "2010-03-25T01:14:38.037226Z" $REPO_URL
$SVN_CMD --revprop -r 11086 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11087 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11087 svn:date "2010-03-25T01:14:51.768795Z" $REPO_URL
$SVN_CMD --revprop -r 11087 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11117 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 11117 svn:date "2010-03-25T21:26:10.437285Z" $REPO_URL
$SVN_CMD --revprop -r 11117 svn:log "Updated for new service lu service methods" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11121 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 11121 svn:date "2010-03-26T01:40:00.366145Z" $REPO_URL
$SVN_CMD --revprop -r 11121 svn:log "Regen'd orchestration xxxHelper, xxxConstaints xxxMetadata and the orch search config.
ALSO putting away initial cut at catalog brower" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11157 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 11157 svn:date "2010-03-29T15:46:52.402090Z" $REPO_URL
$SVN_CMD --revprop -r 11157 svn:log "KSButton -GWT create deferred binding enabled " $REPO_URL
$SVN_CMD --revprop -r 11216 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 11216 svn:date "2010-03-31T14:13:09.832186Z" $REPO_URL
$SVN_CMD --revprop -r 11216 svn:log "ability to set proposal to the \"submitted\" state when proposal is submitted." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11250 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 11250 svn:date "2010-04-01T17:44:04.178070Z" $REPO_URL
$SVN_CMD --revprop -r 11250 svn:log "Financial justification and revenue orgs; fees and expenditure orgs still in work.

Cardinalities of some entities have changed in orch. dictionary; will have to
backport to spreadsheet.

(plus, safari/chrome module xml files)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11253 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 11253 svn:date "2010-04-01T17:57:03.848737Z" $REPO_URL
$SVN_CMD --revprop -r 11253 svn:log "safari/chrome module xml file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11302 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11302 svn:date "2010-04-02T19:34:33.629367Z" $REPO_URL
$SVN_CMD --revprop -r 11302 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11304 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 11304 svn:date "2010-04-02T19:46:50.804951Z" $REPO_URL
$SVN_CMD --revprop -r 11304 svn:log "updating from core lum and rice" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11315 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 11315 svn:date "2010-04-04T22:35:45.515489Z" $REPO_URL
$SVN_CMD --revprop -r 11315 svn:log "Financial info display/persistence/retrieval. Known issues:
  - MultipleRateFee's have a validation problem.
  - Orphaned Fees are not deleted when a Clu is updated" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11327 svn:author "kcampos" $REPO_URL
$SVN_CMD --revprop -r 11327 svn:date "2010-04-05T16:47:01.971540Z" $REPO_URL
$SVN_CMD --revprop -r 11327 svn:log "Fixed <sessions> bug, added doc search test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11330 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 11330 svn:date "2010-04-05T18:05:35.588630Z" $REPO_URL
$SVN_CMD --revprop -r 11330 svn:log "KS WEB-10 moving classes to common-util so they can be reused" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11350 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 11350 svn:date "2010-04-05T21:38:02.241517Z" $REPO_URL
$SVN_CMD --revprop -r 11350 svn:log "restored so that test pass again, have to revisit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11356 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 11356 svn:date "2010-04-06T04:12:50.509209Z" $REPO_URL
$SVN_CMD --revprop -r 11356 svn:log "Configured LRC Service and added LRC RPC Services.  Made Assessment Scales use the KSSelectList and lookups." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11380 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11380 svn:date "2010-04-07T05:41:52.859912Z" $REPO_URL
$SVN_CMD --revprop -r 11380 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11384 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11384 svn:date "2010-04-07T05:46:03.874464Z" $REPO_URL
$SVN_CMD --revprop -r 11384 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11403 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11403 svn:date "2010-04-07T06:09:23.894996Z" $REPO_URL
$SVN_CMD --revprop -r 11403 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11405 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11405 svn:date "2010-04-07T06:09:48.123990Z" $REPO_URL
$SVN_CMD --revprop -r 11405 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11411 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11411 svn:date "2010-04-07T06:13:45.910226Z" $REPO_URL
$SVN_CMD --revprop -r 11411 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11413 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11413 svn:date "2010-04-07T06:14:02.436471Z" $REPO_URL
$SVN_CMD --revprop -r 11413 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11453 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11453 svn:date "2010-04-07T07:34:01.415647Z" $REPO_URL
$SVN_CMD --revprop -r 11453 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11454 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11454 svn:date "2010-04-07T07:34:14.339386Z" $REPO_URL
$SVN_CMD --revprop -r 11454 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11456 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11456 svn:date "2010-04-07T07:34:31.590604Z" $REPO_URL
$SVN_CMD --revprop -r 11456 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11470 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11470 svn:date "2010-04-07T08:22:00.617509Z" $REPO_URL
$SVN_CMD --revprop -r 11470 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11473 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11473 svn:date "2010-04-07T08:22:29.961965Z" $REPO_URL
$SVN_CMD --revprop -r 11473 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11489 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11489 svn:date "2010-04-07T09:48:16.478320Z" $REPO_URL
$SVN_CMD --revprop -r 11489 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11492 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11492 svn:date "2010-04-07T09:48:45.875899Z" $REPO_URL
$SVN_CMD --revprop -r 11492 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11493 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11493 svn:date "2010-04-07T09:48:58.873406Z" $REPO_URL
$SVN_CMD --revprop -r 11493 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11496 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11496 svn:date "2010-04-07T09:56:40.821402Z" $REPO_URL
$SVN_CMD --revprop -r 11496 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11504 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11504 svn:date "2010-04-07T10:02:34.409791Z" $REPO_URL
$SVN_CMD --revprop -r 11504 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11505 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11505 svn:date "2010-04-07T10:02:48.119421Z" $REPO_URL
$SVN_CMD --revprop -r 11505 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11506 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11506 svn:date "2010-04-07T10:03:03.037878Z" $REPO_URL
$SVN_CMD --revprop -r 11506 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11507 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11507 svn:date "2010-04-07T10:03:17.915991Z" $REPO_URL
$SVN_CMD --revprop -r 11507 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11524 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11524 svn:date "2010-04-07T16:31:32.630978Z" $REPO_URL
$SVN_CMD --revprop -r 11524 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11526 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11526 svn:date "2010-04-07T16:31:48.394046Z" $REPO_URL
$SVN_CMD --revprop -r 11526 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11551 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11551 svn:date "2010-04-07T17:36:58.913443Z" $REPO_URL
$SVN_CMD --revprop -r 11551 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11553 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11553 svn:date "2010-04-07T17:37:14.396138Z" $REPO_URL
$SVN_CMD --revprop -r 11553 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11563 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11563 svn:date "2010-04-07T18:23:57.102499Z" $REPO_URL
$SVN_CMD --revprop -r 11563 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11586 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11586 svn:date "2010-04-07T22:39:46.229505Z" $REPO_URL
$SVN_CMD --revprop -r 11586 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11587 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11587 svn:date "2010-04-07T22:40:12.096954Z" $REPO_URL
$SVN_CMD --revprop -r 11587 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11588 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11588 svn:date "2010-04-07T22:40:36.579352Z" $REPO_URL
$SVN_CMD --revprop -r 11588 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11589 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11589 svn:date "2010-04-07T22:40:59.976965Z" $REPO_URL
$SVN_CMD --revprop -r 11589 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11606 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 11606 svn:date "2010-04-08T17:54:24.490446Z" $REPO_URL
$SVN_CMD --revprop -r 11606 svn:log "Got Catalog browser working" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11616 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 11616 svn:date "2010-04-08T20:07:55.132214Z" $REPO_URL
$SVN_CMD --revprop -r 11616 svn:log "KSWEB-6 Standalone now support security and customization context properties" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11623 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 11623 svn:date "2010-04-09T18:52:51.210088Z" $REPO_URL
$SVN_CMD --revprop -r 11623 svn:log "Fix for Outcome types and credit fields in learning results section" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11641 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11641 svn:date "2010-04-12T04:46:32.658103Z" $REPO_URL
$SVN_CMD --revprop -r 11641 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11651 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11651 svn:date "2010-04-12T05:05:43.240781Z" $REPO_URL
$SVN_CMD --revprop -r 11651 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11655 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11655 svn:date "2010-04-12T05:06:13.266902Z" $REPO_URL
$SVN_CMD --revprop -r 11655 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11661 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11661 svn:date "2010-04-12T05:09:08.228425Z" $REPO_URL
$SVN_CMD --revprop -r 11661 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11665 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11665 svn:date "2010-04-12T05:09:49.909961Z" $REPO_URL
$SVN_CMD --revprop -r 11665 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11675 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11675 svn:date "2010-04-12T05:13:42.351639Z" $REPO_URL
$SVN_CMD --revprop -r 11675 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11679 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11679 svn:date "2010-04-12T05:14:24.217260Z" $REPO_URL
$SVN_CMD --revprop -r 11679 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11688 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11688 svn:date "2010-04-12T07:23:13.012075Z" $REPO_URL
$SVN_CMD --revprop -r 11688 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11695 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 11695 svn:date "2010-04-12T15:44:16.602183Z" $REPO_URL
$SVN_CMD --revprop -r 11695 svn:log "moved auth providers beans from kss-auth-providers.xml to ks-spring-security.xml and renamed kss-auth-providers.xml to kss-common.xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11709 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 11709 svn:date "2010-04-12T20:53:12.200798Z" $REPO_URL
$SVN_CMD --revprop -r 11709 svn:log "updating for org expiration dates, proposal doc type, and some new enums" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11733 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 11733 svn:date "2010-04-14T01:37:48.590352Z" $REPO_URL
$SVN_CMD --revprop -r 11733 svn:log "Added clu result type and meta data to CluResult in lu service for learning results" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11748 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 11748 svn:date "2010-04-14T09:09:09.849904Z" $REPO_URL
$SVN_CMD --revprop -r 11748 svn:log "Learning Results assembler fixes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11750 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11750 svn:date "2010-04-14T15:17:54.912191Z" $REPO_URL
$SVN_CMD --revprop -r 11750 svn:log "Added ECL license header to files using the maven license plugin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11751 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11751 svn:date "2010-04-14T15:21:53.027115Z" $REPO_URL
$SVN_CMD --revprop -r 11751 svn:log "Added ECL license header to files using the maven license plugin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11752 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11752 svn:date "2010-04-14T15:25:22.016543Z" $REPO_URL
$SVN_CMD --revprop -r 11752 svn:log "Added ECL license header to files using the maven license plugin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11753 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11753 svn:date "2010-04-14T15:30:45.361072Z" $REPO_URL
$SVN_CMD --revprop -r 11753 svn:log "Added ECL license header to files using the maven license plugin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11761 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11761 svn:date "2010-04-14T18:21:30.042546Z" $REPO_URL
$SVN_CMD --revprop -r 11761 svn:log "Added ECL license header to files using the maven license plugin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11765 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 11765 svn:date "2010-04-14T19:24:12.793521Z" $REPO_URL
$SVN_CMD --revprop -r 11765 svn:log "updated lum impex to latest sql" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11773 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 11773 svn:date "2010-04-14T22:54:04.046621Z" $REPO_URL
$SVN_CMD --revprop -r 11773 svn:log "initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11774 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 11774 svn:date "2010-04-14T23:00:57.510302Z" $REPO_URL
$SVN_CMD --revprop -r 11774 svn:log "initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11776 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 11776 svn:date "2010-04-14T23:33:42.768010Z" $REPO_URL
$SVN_CMD --revprop -r 11776 svn:log "updated with blank schema.xml file to get around impex bug" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11777 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 11777 svn:date "2010-04-14T23:41:42.908900Z" $REPO_URL
$SVN_CMD --revprop -r 11777 svn:log "updated with blank schema.xml file to get around impex bug" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11782 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11782 svn:date "2010-04-15T16:54:35.179495Z" $REPO_URL
$SVN_CMD --revprop -r 11782 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11793 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11793 svn:date "2010-04-15T21:28:02.168255Z" $REPO_URL
$SVN_CMD --revprop -r 11793 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11797 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11797 svn:date "2010-04-15T21:35:53.945352Z" $REPO_URL
$SVN_CMD --revprop -r 11797 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11801 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11801 svn:date "2010-04-15T21:44:33.925009Z" $REPO_URL
$SVN_CMD --revprop -r 11801 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11811 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11811 svn:date "2010-04-15T21:58:07.411870Z" $REPO_URL
$SVN_CMD --revprop -r 11811 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11814 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11814 svn:date "2010-04-15T21:58:33.184453Z" $REPO_URL
$SVN_CMD --revprop -r 11814 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11825 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11825 svn:date "2010-04-15T22:08:07.946866Z" $REPO_URL
$SVN_CMD --revprop -r 11825 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11828 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11828 svn:date "2010-04-15T22:08:32.835456Z" $REPO_URL
$SVN_CMD --revprop -r 11828 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11829 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11829 svn:date "2010-04-15T22:08:44.738674Z" $REPO_URL
$SVN_CMD --revprop -r 11829 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11830 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11830 svn:date "2010-04-15T22:08:56.537094Z" $REPO_URL
$SVN_CMD --revprop -r 11830 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11837 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11837 svn:date "2010-04-15T22:22:02.164110Z" $REPO_URL
$SVN_CMD --revprop -r 11837 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11839 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11839 svn:date "2010-04-15T22:22:15.884976Z" $REPO_URL
$SVN_CMD --revprop -r 11839 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11841 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11841 svn:date "2010-04-15T22:22:40.339908Z" $REPO_URL
$SVN_CMD --revprop -r 11841 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11847 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11847 svn:date "2010-04-15T22:56:58.910456Z" $REPO_URL
$SVN_CMD --revprop -r 11847 svn:log "Initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11848 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 11848 svn:date "2010-04-15T23:09:14.116234Z" $REPO_URL
$SVN_CMD --revprop -r 11848 svn:log "updated with blank schema.xml file to get around impex bug" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11851 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11851 svn:date "2010-04-15T23:22:42.281402Z" $REPO_URL
$SVN_CMD --revprop -r 11851 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11858 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11858 svn:date "2010-04-15T23:29:57.308897Z" $REPO_URL
$SVN_CMD --revprop -r 11858 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11871 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11871 svn:date "2010-04-16T08:15:24.391124Z" $REPO_URL
$SVN_CMD --revprop -r 11871 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11874 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 11874 svn:date "2010-04-16T09:12:46.251318Z" $REPO_URL
$SVN_CMD --revprop -r 11874 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11884 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 11884 svn:date "2010-04-16T18:21:50.832111Z" $REPO_URL
$SVN_CMD --revprop -r 11884 svn:log "added spring security to ks-rice and cas" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11915 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 11915 svn:date "2010-04-19T13:50:57.263053Z" $REPO_URL
$SVN_CMD --revprop -r 11915 svn:log "added StandaloneSpringBeans.xml to ks-rice so we have proper resolving of config properties" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 11997 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 11997 svn:date "2010-04-22T22:10:11.513035Z" $REPO_URL
$SVN_CMD --revprop -r 11997 svn:log "[KSLUM-161] Create Cross Search Picker which searches courses and proposals
Moved course and proposal picker to use orch. dict. configuration; cleanup" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12059 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12059 svn:date "2010-04-24T19:23:43.686443Z" $REPO_URL
$SVN_CMD --revprop -r 12059 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12073 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12073 svn:date "2010-04-24T21:33:06.482557Z" $REPO_URL
$SVN_CMD --revprop -r 12073 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12077 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12077 svn:date "2010-04-24T21:34:42.978235Z" $REPO_URL
$SVN_CMD --revprop -r 12077 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12079 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12079 svn:date "2010-04-24T21:34:57.021195Z" $REPO_URL
$SVN_CMD --revprop -r 12079 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12083 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12083 svn:date "2010-04-24T21:35:43.436161Z" $REPO_URL
$SVN_CMD --revprop -r 12083 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12084 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12084 svn:date "2010-04-24T21:35:54.570469Z" $REPO_URL
$SVN_CMD --revprop -r 12084 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12088 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12088 svn:date "2010-04-24T21:36:47.500106Z" $REPO_URL
$SVN_CMD --revprop -r 12088 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12090 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12090 svn:date "2010-04-24T21:37:01.089944Z" $REPO_URL
$SVN_CMD --revprop -r 12090 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12091 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12091 svn:date "2010-04-24T21:37:12.478730Z" $REPO_URL
$SVN_CMD --revprop -r 12091 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12096 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12096 svn:date "2010-04-24T21:37:58.216110Z" $REPO_URL
$SVN_CMD --revprop -r 12096 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12097 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12097 svn:date "2010-04-24T21:38:09.229412Z" $REPO_URL
$SVN_CMD --revprop -r 12097 svn:log "Upload by wagon-svn" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12110 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12110 svn:date "2010-04-25T00:39:10.145182Z" $REPO_URL
$SVN_CMD --revprop -r 12110 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12179 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 12179 svn:date "2010-04-26T23:04:32.503742Z" $REPO_URL
$SVN_CMD --revprop -r 12179 svn:log "KSLAB-727 - Added new requirement component type (courseList.1of1) configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12188 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 12188 svn:date "2010-04-28T16:14:12.649401Z" $REPO_URL
$SVN_CMD --revprop -r 12188 svn:log "Got data loader working to generate sql inserts for courses this is for Jira 
https://test.kuali.org/jira/browse/KSLUM-216
and 
https://test.kuali.org/jira/browse/KSLUM-217
So it the catalog browser can be tested" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12189 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 12189 svn:date "2010-04-28T16:14:30.057650Z" $REPO_URL
$SVN_CMD --revprop -r 12189 svn:log "Got data loader working to generate sql inserts for courses this is for Jira 
https://test.kuali.org/jira/browse/KSLUM-216
and 
https://test.kuali.org/jira/browse/KSLUM-217
So it the catalog browser can be tested" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12266 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12266 svn:date "2010-04-29T18:11:02.230691Z" $REPO_URL
$SVN_CMD --revprop -r 12266 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12267 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12267 svn:date "2010-04-29T18:11:44.470520Z" $REPO_URL
$SVN_CMD --revprop -r 12267 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12268 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12268 svn:date "2010-04-29T18:12:17.958492Z" $REPO_URL
$SVN_CMD --revprop -r 12268 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12275 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12275 svn:date "2010-04-29T18:22:20.543847Z" $REPO_URL
$SVN_CMD --revprop -r 12275 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12297 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12297 svn:date "2010-04-29T20:07:03.063031Z" $REPO_URL
$SVN_CMD --revprop -r 12297 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12304 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12304 svn:date "2010-04-29T20:18:28.831968Z" $REPO_URL
$SVN_CMD --revprop -r 12304 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12306 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12306 svn:date "2010-04-29T20:25:20.683772Z" $REPO_URL
$SVN_CMD --revprop -r 12306 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12308 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12308 svn:date "2010-04-29T20:27:13.263239Z" $REPO_URL
$SVN_CMD --revprop -r 12308 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12309 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12309 svn:date "2010-04-29T20:27:23.696096Z" $REPO_URL
$SVN_CMD --revprop -r 12309 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12317 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 12317 svn:date "2010-04-30T02:24:52.176076Z" $REPO_URL
$SVN_CMD --revprop -r 12317 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12580 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12580 svn:date "2010-04-30T22:23:03.807208Z" $REPO_URL
$SVN_CMD --revprop -r 12580 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12696 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12696 svn:date "2010-05-01T13:24:15.550874Z" $REPO_URL
$SVN_CMD --revprop -r 12696 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12795 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12795 svn:date "2010-05-02T15:49:50.647893Z" $REPO_URL
$SVN_CMD --revprop -r 12795 svn:log "Added basic project files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12839 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12839 svn:date "2010-05-02T17:57:53.388091Z" $REPO_URL
$SVN_CMD --revprop -r 12839 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12866 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12866 svn:date "2010-05-02T20:02:36.918248Z" $REPO_URL
$SVN_CMD --revprop -r 12866 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 12974 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 12974 svn:date "2010-05-02T21:49:27.614941Z" $REPO_URL
$SVN_CMD --revprop -r 12974 svn:log "Script to pull changes from /trunk/1.0.x into /branches/ks-cm-1.0.x" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13020 svn:author "rballew" $REPO_URL
$SVN_CMD --revprop -r 13020 svn:date "2010-05-03T20:15:59.688237Z" $REPO_URL
$SVN_CMD --revprop -r 13020 svn:log "original version" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13057 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 13057 svn:date "2010-05-04T06:22:04.644109Z" $REPO_URL
$SVN_CMD --revprop -r 13057 svn:log "Included ks-cfg-dbs in the maven build process.  No impex yet..." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13066 svn:author "rballew" $REPO_URL
$SVN_CMD --revprop -r 13066 svn:date "2010-05-04T17:35:29.178402Z" $REPO_URL
$SVN_CMD --revprop -r 13066 svn:log "original import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13068 svn:author "rballew" $REPO_URL
$SVN_CMD --revprop -r 13068 svn:date "2010-05-04T18:44:22.021317Z" $REPO_URL
$SVN_CMD --revprop -r 13068 svn:log "oringinal import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13268 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 13268 svn:date "2010-05-10T15:32:17.643944Z" $REPO_URL
$SVN_CMD --revprop -r 13268 svn:log "KSLUM-264: Add dynamic CLUs to CLUset.  Added code that uses the KSPicker parameter editor." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13379 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 13379 svn:date "2010-05-12T17:12:46.981999Z" $REPO_URL
$SVN_CMD --revprop -r 13379 svn:log "[KSCOR-187, KSLAB-707] New FieldLayout and Section integration, this update breaks CluSets until additional section/field widgets are designed and created." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13422 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 13422 svn:date "2010-05-12T20:05:26.542499Z" $REPO_URL
$SVN_CMD --revprop -r 13422 svn:log "Cucumber watir testing for ks kuali-rc2" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13433 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 13433 svn:date "2010-05-12T20:23:34.806519Z" $REPO_URL
$SVN_CMD --revprop -r 13433 svn:log "KSCOR-221 - Move BRMS statement service and message builder from brms-dev to ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13502 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 13502 svn:date "2010-05-12T23:04:31.686606Z" $REPO_URL
$SVN_CMD --revprop -r 13502 svn:log "added test rails project, with scenarios for RC2" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13562 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 13562 svn:date "2010-05-13T16:11:35.491195Z" $REPO_URL
$SVN_CMD --revprop -r 13562 svn:log "RC2 testing finished" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13563 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 13563 svn:date "2010-05-13T16:23:31.320571Z" $REPO_URL
$SVN_CMD --revprop -r 13563 svn:log "added ubc specific impex database project" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13696 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 13696 svn:date "2010-05-14T20:31:14.051752Z" $REPO_URL
$SVN_CMD --revprop -r 13696 svn:log "[KSLUM-272] 
Added isReusable to the CluSetInfo structure 
Added isCluSetDynamic to LuService " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13783 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 13783 svn:date "2010-05-17T19:56:16.552239Z" $REPO_URL
$SVN_CMD --revprop -r 13783 svn:log "Added CourseService that Neerav Scrubbed" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13802 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 13802 svn:date "2010-05-18T20:54:43.962154Z" $REPO_URL
$SVN_CMD --revprop -r 13802 svn:log "[KSCOR-241]

Updated course service interface, dtos and service impl class. 

Added wsdl and jaxws package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13817 svn:author "rballew" $REPO_URL
$SVN_CMD --revprop -r 13817 svn:date "2010-05-19T17:27:53.973875Z" $REPO_URL
$SVN_CMD --revprop -r 13817 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13821 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 13821 svn:date "2010-05-19T22:55:12.197377Z" $REPO_URL
$SVN_CMD --revprop -r 13821 svn:log "Initial commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13857 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 13857 svn:date "2010-05-20T18:00:48.388885Z" $REPO_URL
$SVN_CMD --revprop -r 13857 svn:log "Initial commit of the Spring source for this project.

Obtained from http://repo1.maven.org/maven2/org/springframework/aws/spring-aws-maven/1.2

This version only works with Maven 2.0.9.  Produces an AbstractMethodError on 2.2.1 and  3.0.  3.0 bombs out before this happens with an Authentication Error since it does not handle <passphrase> and <privateKey> from settings.xml." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13957 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 13957 svn:date "2010-05-24T18:59:33.550884Z" $REPO_URL
$SVN_CMD --revprop -r 13957 svn:log "Restore test class accidentally deleted." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13961 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 13961 svn:date "2010-05-24T22:29:23.063381Z" $REPO_URL
$SVN_CMD --revprop -r 13961 svn:log "Initializing KSB on the client side." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13977 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 13977 svn:date "2010-05-25T17:36:14.396952Z" $REPO_URL
$SVN_CMD --revprop -r 13977 svn:log "Added new poc validation and tests" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13983 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 13983 svn:date "2010-05-25T19:28:46.926468Z" $REPO_URL
$SVN_CMD --revprop -r 13983 svn:log "[KSCOR-235] Add a mapper interface and impl to allow conversion b/w a dto bean and a data map." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 13984 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 13984 svn:date "2010-05-25T19:55:00.159383Z" $REPO_URL
$SVN_CMD --revprop -r 13984 svn:log "Added new dictionary service to course service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14032 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 14032 svn:date "2010-05-27T16:40:24.856712Z" $REPO_URL
$SVN_CMD --revprop -r 14032 svn:log "updating impex projects for removed and added tables for KSEM_ENUM prefix and KSEM_CTX prefix" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14081 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14081 svn:date "2010-05-28T18:32:25.260061Z" $REPO_URL
$SVN_CMD --revprop -r 14081 svn:log "Parent site.xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14102 svn:author "ssim" $REPO_URL
$SVN_CMD --revprop -r 14102 svn:date "2010-06-02T02:56:50.930950Z" $REPO_URL
$SVN_CMD --revprop -r 14102 svn:log "SectionTable until we get GWT 2.1 Table" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14105 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14105 svn:date "2010-06-02T06:23:42.018032Z" $REPO_URL
$SVN_CMD --revprop -r 14105 svn:log "Initial commit." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14108 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14108 svn:date "2010-06-02T07:10:03.747931Z" $REPO_URL
$SVN_CMD --revprop -r 14108 svn:log "Switched to Kuali parent pom for release" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14127 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 14127 svn:date "2010-06-02T17:27:17.578122Z" $REPO_URL
$SVN_CMD --revprop -r 14127 svn:log "Add response wrappers for dicitionary methods" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14226 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 14226 svn:date "2010-06-09T18:15:23.979492Z" $REPO_URL
$SVN_CMD --revprop -r 14226 svn:log "Initial checking for wiring in UI calls to new business services." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14230 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 14230 svn:date "2010-06-09T19:04:17.184681Z" $REPO_URL
$SVN_CMD --revprop -r 14230 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14280 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 14280 svn:date "2010-06-11T17:43:39.235282Z" $REPO_URL
$SVN_CMD --revprop -r 14280 svn:log "added the test project updated for RC2 testing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14281 svn:author "rdiaz" $REPO_URL
$SVN_CMD --revprop -r 14281 svn:date "2010-06-11T17:49:00.156050Z" $REPO_URL
$SVN_CMD --revprop -r 14281 svn:log "Initial check in of WS-Trust Security Token Service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14330 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 14330 svn:date "2010-06-15T00:04:42.836786Z" $REPO_URL
$SVN_CMD --revprop -r 14330 svn:log "TThis is new rc2 version for appserv  " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14350 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 14350 svn:date "2010-06-16T14:14:53.922155Z" $REPO_URL
$SVN_CMD --revprop -r 14350 svn:log "Moving common-lookup-context to ks-common-impl from lum-ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14354 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 14354 svn:date "2010-06-16T17:20:46.161134Z" $REPO_URL
$SVN_CMD --revprop -r 14354 svn:log "Initial commits to support KSCOR-268 & KSCOR-269: Added new data & dto filter using a data TransformationManager
New IdTranslatorFilter 
Refactored package for DataBeanMapper
Added a CourseProposalFilter so course proposal search works.
Added spring config for new filters
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14355 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 14355 svn:date "2010-06-16T19:58:52.334626Z" $REPO_URL
$SVN_CMD --revprop -r 14355 svn:log "KSLUM-155 - left labels for View Course" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14358 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 14358 svn:date "2010-06-16T23:13:59.373864Z" $REPO_URL
$SVN_CMD --revprop -r 14358 svn:log "[KSPLAA-245] added cluSetTreeView

update LU Service API and Impl. 

Added referencable flag in CluSet" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14359 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 14359 svn:date "2010-06-17T01:11:23.742791Z" $REPO_URL
$SVN_CMD --revprop -r 14359 svn:log "KSPLAA-245 - Updated wsdl and jaxws for new LU service method getCluSetTreeView" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14366 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14366 svn:date "2010-06-17T16:08:34.334702Z" $REPO_URL
$SVN_CMD --revprop -r 14366 svn:log "Dump m2eclipse until it matures a bit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14387 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 14387 svn:date "2010-06-17T23:22:56.602931Z" $REPO_URL
$SVN_CMD --revprop -r 14387 svn:log "KSCOR-238: Add Statement Service Dictionary. StatementInfo and ReqComponentInfo" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14421 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14421 svn:date "2010-06-20T19:45:10.212155Z" $REPO_URL
$SVN_CMD --revprop -r 14421 svn:log "Import individual projects" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14422 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14422 svn:date "2010-06-20T19:45:39.805133Z" $REPO_URL
$SVN_CMD --revprop -r 14422 svn:log "Import individual projects" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14425 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14425 svn:date "2010-06-20T19:55:12.335542Z" $REPO_URL
$SVN_CMD --revprop -r 14425 svn:log "Import individual projects" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14426 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14426 svn:date "2010-06-20T20:02:27.281325Z" $REPO_URL
$SVN_CMD --revprop -r 14426 svn:log "Import individual projects" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14428 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14428 svn:date "2010-06-20T20:05:09.703259Z" $REPO_URL
$SVN_CMD --revprop -r 14428 svn:log "Import individual projects" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14475 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14475 svn:date "2010-06-22T13:45:50.182436Z" $REPO_URL
$SVN_CMD --revprop -r 14475 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14545 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 14545 svn:date "2010-06-29T22:03:24.422362Z" $REPO_URL
$SVN_CMD --revprop -r 14545 svn:log "Controller and LayoutController changes, initial pass, left nav menu, left handed sliding drawer, MenuSectionController implementation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14556 svn:author "kcampos" $REPO_URL
$SVN_CMD --revprop -r 14556 svn:date "2010-06-30T16:19:59.392379Z" $REPO_URL
$SVN_CMD --revprop -r 14556 svn:log "1.0.0 updates" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14558 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 14558 svn:date "2010-06-30T20:29:33.443436Z" $REPO_URL
$SVN_CMD --revprop -r 14558 svn:log "KSCOR-211 - Investigate options for new multiplicities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14566 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 14566 svn:date "2010-07-01T01:03:01.462862Z" $REPO_URL
$SVN_CMD --revprop -r 14566 svn:log "adding the skeleton ProgramServiceImpl and DTO's." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14567 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 14567 svn:date "2010-07-01T02:02:38.904833Z" $REPO_URL
$SVN_CMD --revprop -r 14567 svn:log "[KSLUM-287] [KSLUM-294] new req. component types for rules; first pass" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14570 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 14570 svn:date "2010-07-01T03:13:35.707520Z" $REPO_URL
$SVN_CMD --revprop -r 14570 svn:log "Added ks-lum-program module." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14576 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 14576 svn:date "2010-07-02T20:23:44.032486Z" $REPO_URL
$SVN_CMD --revprop -r 14576 svn:log "KSCOR-310: Moved requirement component field type keys from ks-core-api to ks-lum-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14600 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 14600 svn:date "2010-07-07T15:27:17.728276Z" $REPO_URL
$SVN_CMD --revprop -r 14600 svn:log "Added some classes to help load small bits test data in spring" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14602 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 14602 svn:date "2010-07-07T15:59:00.991885Z" $REPO_URL
$SVN_CMD --revprop -r 14602 svn:log "moved metadata test to testCOurse so we dont have to wait for the same context to load twice. Copied lookup context to test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14607 svn:author "jyin" $REPO_URL
$SVN_CMD --revprop -r 14607 svn:date "2010-07-07T20:58:14.070954Z" $REPO_URL
$SVN_CMD --revprop -r 14607 svn:log "fixed the image path for table header" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14611 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 14611 svn:date "2010-07-07T22:51:56.340994Z" $REPO_URL
$SVN_CMD --revprop -r 14611 svn:log "KSLUM-298: Create New Program Requirement Business Service Methods. Set up web service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14614 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 14614 svn:date "2010-07-08T15:11:56.075627Z" $REPO_URL
$SVN_CMD --revprop -r 14614 svn:log "refactored out poc packages, moved old code to .old package to be removed later" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14625 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 14625 svn:date "2010-07-08T23:18:19.207696Z" $REPO_URL
$SVN_CMD --revprop -r 14625 svn:log "Refactor CourseServiceMethodInvoker to here for both course and program so far." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14652 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 14652 svn:date "2010-07-10T02:16:38.285566Z" $REPO_URL
$SVN_CMD --revprop -r 14652 svn:log "for testing MajorDiscipline in TestProgramServiceImpl." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14664 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 14664 svn:date "2010-07-12T18:22:44.869915Z" $REPO_URL
$SVN_CMD --revprop -r 14664 svn:log "[KSCOR-316]  Ask user to save when fields are dirty - This works for course, we may need to make it more generic so it can be reused for other areas" $REPO_URL
$SVN_CMD --revprop -r 14693 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 14693 svn:date "2010-07-14T00:42:06.195272Z" $REPO_URL
$SVN_CMD --revprop -r 14693 svn:log "[KSLUM-311] Create New Course Requirement Component Type C-5.1; [KSLUM-300] P-6.1: Create new program req component type \"Must be admitted to <program> prior to earning <n> credits\"
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14718 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 14718 svn:date "2010-07-15T00:42:13.360580Z" $REPO_URL
$SVN_CMD --revprop -r 14718 svn:log "KSCOR-212  Incremental checkin of multiplicity changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14761 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 14761 svn:date "2010-07-19T20:10:32.653509Z" $REPO_URL
$SVN_CMD --revprop -r 14761 svn:log "[KSLUM-336,KSLUM-339] Added new program requirement component types and fixed impex foreign keys for tables KSST_STMT_TYP_JN_RC_TYP and KSST_STMT_TYP_JN_STMT_TYP" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14763 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 14763 svn:date "2010-07-19T20:46:31.352039Z" $REPO_URL
$SVN_CMD --revprop -r 14763 svn:log "added cyclic marshalling test" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14772 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 14772 svn:date "2010-07-20T01:29:57.955389Z" $REPO_URL
$SVN_CMD --revprop -r 14772 svn:log "Introduced ConfigurationManager. Provides methods for managing Configurations For Configurer." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14780 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 14780 svn:date "2010-07-20T18:40:35.640458Z" $REPO_URL
$SVN_CMD --revprop -r 14780 svn:log "Add MajorDisciplineDataGenerator to main to provide data via service call on a short-term basis
  REMOVE IT when data available from DB/Assembler" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14782 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 14782 svn:date "2010-07-20T19:42:33.188453Z" $REPO_URL
$SVN_CMD --revprop -r 14782 svn:log "pulled multiplicity from trunk" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14790 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 14790 svn:date "2010-07-21T03:09:23.743449Z" $REPO_URL
$SVN_CMD --revprop -r 14790 svn:log "[KSLUM-343] Created a simplified message builder for natural language translation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14944 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14944 svn:date "2010-07-22T23:03:40.446265Z" $REPO_URL
$SVN_CMD --revprop -r 14944 svn:log "Initial commit.  Running mvn impex:schemasql manually works" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14952 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 14952 svn:date "2010-07-23T01:46:56.264207Z" $REPO_URL
$SVN_CMD --revprop -r 14952 svn:log "[KSCOR-326] Updated wsdl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14964 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14964 svn:date "2010-07-23T16:24:13.092002Z" $REPO_URL
$SVN_CMD --revprop -r 14964 svn:log "Pulled in ks-embedded" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14969 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 14969 svn:date "2010-07-23T16:38:19.365252Z" $REPO_URL
$SVN_CMD --revprop -r 14969 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14973 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 14973 svn:date "2010-07-23T17:59:36.166210Z" $REPO_URL
$SVN_CMD --revprop -r 14973 svn:log "[KSCOR-59][KSCOR-60][KSCOR-303][KSCOR-327]  History support, breadcrumb support, and direct linking support.  Use direct linking by using a hyperlink or calling Application.navigate().  Context is saved in the link for objects with ids, and can be directly linked to.  Removed LumApplicationManager, migrating towards an ApplicationController solution, first step.  Restructured controllers - specifically LayoutController to be a view to retain a hierarchy of history and breadcrumbing as the user navigates." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 14983 svn:author "miloskarhanek" $REPO_URL
$SVN_CMD --revprop -r 14983 svn:date "2010-07-24T18:46:44.135903Z" $REPO_URL
$SVN_CMD --revprop -r 14983 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15023 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 15023 svn:date "2010-07-28T15:56:33.669597Z" $REPO_URL
$SVN_CMD --revprop -r 15023 svn:log "[KSCOR-252] Added Clu fee service changes,
implemented Fee in course service
updated impex db" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15059 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 15059 svn:date "2010-07-29T20:40:44.197857Z" $REPO_URL
$SVN_CMD --revprop -r 15059 svn:log "initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15096 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 15096 svn:date "2010-07-30T18:46:37.107832Z" $REPO_URL
$SVN_CMD --revprop -r 15096 svn:log "Get some majordiscipline persistence working
 " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15102 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15102 svn:date "2010-07-30T19:04:56.090140Z" $REPO_URL
$SVN_CMD --revprop -r 15102 svn:log "Initial commit - empty file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15108 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15108 svn:date "2010-07-30T19:10:59.426590Z" $REPO_URL
$SVN_CMD --revprop -r 15108 svn:log "Initial commit - empty file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15113 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15113 svn:date "2010-07-30T22:45:56.581707Z" $REPO_URL
$SVN_CMD --revprop -r 15113 svn:log "Initial commit - Empty file" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15118 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15118 svn:date "2010-07-30T23:21:11.544770Z" $REPO_URL
$SVN_CMD --revprop -r 15118 svn:log "Empty files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15158 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 15158 svn:date "2010-08-04T00:14:20.307645Z" $REPO_URL
$SVN_CMD --revprop -r 15158 svn:log "Add CredentailProgramDataGenerator" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15162 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 15162 svn:date "2010-08-04T13:36:29.671007Z" $REPO_URL
$SVN_CMD --revprop -r 15162 svn:log "Refactor workflow rpc classes and widgets. Update to move to core-ui packages and remove unnecessary methods." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15200 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 15200 svn:date "2010-08-05T19:25:43.549530Z" $REPO_URL
$SVN_CMD --revprop -r 15200 svn:log "RadioButtonList new widget, fixed some logic issues with checkbox" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15210 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15210 svn:date "2010-08-05T21:37:01.386132Z" $REPO_URL
$SVN_CMD --revprop -r 15210 svn:log "pom cleanup, refactoring" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15300 svn:author "kcampos" $REPO_URL
$SVN_CMD --revprop -r 15300 svn:date "2010-08-06T21:13:12.655255Z" $REPO_URL
$SVN_CMD --revprop -r 15300 svn:log "Updated files for course/proposal search" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15301 svn:author "kcampos" $REPO_URL
$SVN_CMD --revprop -r 15301 svn:date "2010-08-06T21:14:47.917756Z" $REPO_URL
$SVN_CMD --revprop -r 15301 svn:log "course search xml" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15305 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15305 svn:date "2010-08-07T01:56:23.114487Z" $REPO_URL
$SVN_CMD --revprop -r 15305 svn:log "\\- Split SQL execution out into its own module, can execute arbitrary files full of SQL statements now outside of a maven plugin
\\- SQLExecutor fires events about what it is up to.  This allows ImportMojo to emit appropriate log messages and otherwise provide feedback about what is going on" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15324 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 15324 svn:date "2010-08-09T19:06:12.151185Z" $REPO_URL
$SVN_CMD --revprop -r 15324 svn:log "renamed test files to avoid confusion" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15363 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 15363 svn:date "2010-08-10T23:13:36.351473Z" $REPO_URL
$SVN_CMD --revprop -r 15363 svn:log "Refactored ProgramDataGenerators" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15411 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 15411 svn:date "2010-08-13T02:50:20.714268Z" $REPO_URL
$SVN_CMD --revprop -r 15411 svn:log "1. Renamed ManagingBodiesConfiguration -> ManagingBodiesViewConfiguration
2. Added fields to the ManagingBodiesViewConfiguration.
3. Created ManagingBodiesEditConfiguration and wired it to the ProgramEditConfigurer." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15413 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 15413 svn:date "2010-08-13T09:25:20.034037Z" $REPO_URL
$SVN_CMD --revprop -r 15413 svn:log "KSLUM-298: Create New Program Requirement Business Service Methods. Created StatementTreeViewAssembler" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15414 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15414 svn:date "2010-08-13T16:45:38.558799Z" $REPO_URL
$SVN_CMD --revprop -r 15414 svn:log "Added Spring nature.  Added kuali-jdbc-context.xml to the IDE files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15415 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15415 svn:date "2010-08-13T16:46:50.207144Z" $REPO_URL
$SVN_CMD --revprop -r 15415 svn:log "Place .settings under version control" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15456 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15456 svn:date "2010-08-14T04:36:51.834030Z" $REPO_URL
$SVN_CMD --revprop -r 15456 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15460 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 15460 svn:date "2010-08-14T08:15:13.006246Z" $REPO_URL
$SVN_CMD --revprop -r 15460 svn:log "[KSLUM-308] Create new Program Requirements Rules View; initial work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15469 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 15469 svn:date "2010-08-15T06:06:13.124327Z" $REPO_URL
$SVN_CMD --revprop -r 15469 svn:log "[KSLUM-308] Create new Program Requirements Rules View; ongoing work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15471 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 15471 svn:date "2010-08-16T05:32:19.714535Z" $REPO_URL
$SVN_CMD --revprop -r 15471 svn:log "[KSLUM-308] Create new Program Requirements Rules View; ongoing work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15481 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 15481 svn:date "2010-08-16T18:02:13.799907Z" $REPO_URL
$SVN_CMD --revprop -r 15481 svn:log "[KSCOR-351] Add a requiredForNextState flag
[KSCOR-277] Add tests for MetadataServiceImpl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15498 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15498 svn:date "2010-08-17T15:05:04.863205Z" $REPO_URL
$SVN_CMD --revprop -r 15498 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15507 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 15507 svn:date "2010-08-17T19:31:05.519609Z" $REPO_URL
$SVN_CMD --revprop -r 15507 svn:log "(1) moved ks-base-dictionary-context to ks-core-impl 
So I could (2) Create a ks-proposalInfo-dictionary-context.xml that depended on it
(3) Had to move all my test harness stuff as well and fix the search configs to only do the searches related to core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15523 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 15523 svn:date "2010-08-17T23:37:51.090630Z" $REPO_URL
$SVN_CMD --revprop -r 15523 svn:log "updated CDM to M6, configured workflow with a logic node, also passing properties from workflow to QualifierResolver classes, workflow working from department curriculum committee all the way to publication office" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15544 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15544 svn:date "2010-08-19T00:19:55.175037Z" $REPO_URL
$SVN_CMD --revprop -r 15544 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15549 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 15549 svn:date "2010-08-19T03:26:45.816131Z" $REPO_URL
$SVN_CMD --revprop -r 15549 svn:log "1. Renamed existing configuration to stick to general naming convention.
2. Added empty configuration for Specializations and Learning Objectives and wired them to the configurer." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15565 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15565 svn:date "2010-08-19T17:23:27.583733Z" $REPO_URL
$SVN_CMD --revprop -r 15565 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15566 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15566 svn:date "2010-08-19T17:26:17.206464Z" $REPO_URL
$SVN_CMD --revprop -r 15566 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15567 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15567 svn:date "2010-08-19T17:27:35.395479Z" $REPO_URL
$SVN_CMD --revprop -r 15567 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15636 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15636 svn:date "2010-08-23T14:24:40.724376Z" $REPO_URL
$SVN_CMD --revprop -r 15636 svn:log "KSQST-31 Initial commit.  Basic pom setup" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15667 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 15667 svn:date "2010-08-24T17:00:32.985833Z" $REPO_URL
$SVN_CMD --revprop -r 15667 svn:log "(1) Added  CluSet dictionary
(2) Updated creditOptions to not override ResultComponentInfo in lrc dictionary
(3) Commented out search.cluSets so tests run until we figure out how searches work
(4) Added test of LRC dictionary that Dan created" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15718 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15718 svn:date "2010-08-25T18:16:00.177565Z" $REPO_URL
$SVN_CMD --revprop -r 15718 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15730 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15730 svn:date "2010-08-25T19:59:38.760175Z" $REPO_URL
$SVN_CMD --revprop -r 15730 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15740 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15740 svn:date "2010-08-25T20:11:08.700552Z" $REPO_URL
$SVN_CMD --revprop -r 15740 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15751 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15751 svn:date "2010-08-25T21:33:56.627413Z" $REPO_URL
$SVN_CMD --revprop -r 15751 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15762 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 15762 svn:date "2010-08-25T23:43:48.264423Z" $REPO_URL
$SVN_CMD --revprop -r 15762 svn:log "KSLUM-321 - setup impex test data for Program Specializations" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15785 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 15785 svn:date "2010-08-26T17:15:16.447266Z" $REPO_URL
$SVN_CMD --revprop -r 15785 svn:log "got wsdl2java working and putting away the gnerated java" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15812 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 15812 svn:date "2010-08-27T20:27:40.002422Z" $REPO_URL
$SVN_CMD --revprop -r 15812 svn:log "KSCOR-359

Updated LuService Interface.

Started making changes to LuServiceImpl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15816 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 15816 svn:date "2010-08-27T22:10:07.261183Z" $REPO_URL
$SVN_CMD --revprop -r 15816 svn:log "1. Create new module ks-lum-common-ui.
2. Extricated LO from ks-lum-ui to ks-lum-common-ui.
3. Reused LO in programs." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15824 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 15824 svn:date "2010-08-27T23:20:07.908987Z" $REPO_URL
$SVN_CMD --revprop -r 15824 svn:log "KSCOR-359

Changed the logic such that Id points to unique Id and versionInfo has version independent Id. Created a separated read only structure called versionDisplayInfo which has both id and versionIndId along with objectTypeURI.

This makes alignment with entities in DB cleaner with the service contract levels. " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15854 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 15854 svn:date "2010-08-30T19:10:34.176238Z" $REPO_URL
$SVN_CMD --revprop -r 15854 svn:log "added the sis_cdm_migration project, migrates UBC's SIS courses in Kuali Student" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15879 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15879 svn:date "2010-08-31T19:08:40.467656Z" $REPO_URL
$SVN_CMD --revprop -r 15879 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15883 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 15883 svn:date "2010-08-31T20:19:12.703751Z" $REPO_URL
$SVN_CMD --revprop -r 15883 svn:log "KSPLAA-256

Added IllegalVersionSequencingException" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15897 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 15897 svn:date "2010-09-01T14:00:48.293683Z" $REPO_URL
$SVN_CMD --revprop -r 15897 svn:log "added document relationships to document service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15910 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15910 svn:date "2010-09-01T20:20:26.606490Z" $REPO_URL
$SVN_CMD --revprop -r 15910 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15912 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 15912 svn:date "2010-09-01T22:18:05.717850Z" $REPO_URL
$SVN_CMD --revprop -r 15912 svn:log "[KSLUM-378] Created additional course requirement component types" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15948 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15948 svn:date "2010-09-02T19:35:56.616196Z" $REPO_URL
$SVN_CMD --revprop -r 15948 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15950 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 15950 svn:date "2010-09-02T19:51:56.369450Z" $REPO_URL
$SVN_CMD --revprop -r 15950 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 15972 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 15972 svn:date "2010-09-03T01:18:11.327274Z" $REPO_URL
$SVN_CMD --revprop -r 15972 svn:log "(1) Created base line statement dictionary
(2) Modified formatter structure to handle enumerations instead of string
(3) Moved rich-text definition from cluInfo dictionary to base so it could be referenced by dictionaries in core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16073 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16073 svn:date "2010-09-08T17:55:51.802580Z" $REPO_URL
$SVN_CMD --revprop -r 16073 svn:log "Added a new transactional layer between the GWT servlet code and the servlet logic.  This will fix proposals from getting committed after other service calls in the save method failed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16091 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 16091 svn:date "2010-09-09T16:17:44.981553Z" $REPO_URL
$SVN_CMD --revprop -r 16091 svn:log "Update CourseRpcService to use CourseDataService.
Remove old course and proposal gwt servlets." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16146 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16146 svn:date "2010-09-13T06:10:52.573789Z" $REPO_URL
$SVN_CMD --revprop -r 16146 svn:log "1. Simplified framework for Configurations.
2. Added VariationRegistry
3. Added VariationViewAllConfiguration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16173 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 16173 svn:date "2010-09-13T22:04:26.686980Z" $REPO_URL
$SVN_CMD --revprop -r 16173 svn:log "[KSLUM-380] Created additional program requirement component types, updated gpa context and updated nl usage types" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16177 svn:author "delyea" $REPO_URL
$SVN_CMD --revprop -r 16177 svn:date "2010-09-14T06:28:23.972807Z" $REPO_URL
$SVN_CMD --revprop -r 16177 svn:log "KSCOR-398 - involves allowing customization of QualifierResolver classes by only editing XML in the KEW document type xml configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16202 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16202 svn:date "2010-09-14T21:09:38.462128Z" $REPO_URL
$SVN_CMD --revprop -r 16202 svn:log "Added Supporting Documents section to Major Discipline" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16206 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16206 svn:date "2010-09-14T22:46:00.915023Z" $REPO_URL
$SVN_CMD --revprop -r 16206 svn:log "Added infrastructure for program variation edit functionality." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16219 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16219 svn:date "2010-09-15T20:52:25.679777Z" $REPO_URL
$SVN_CMD --revprop -r 16219 svn:log "Used correct objectURI" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16226 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 16226 svn:date "2010-09-16T16:17:57.075084Z" $REPO_URL
$SVN_CMD --revprop -r 16226 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16305 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 16305 svn:date "2010-09-21T01:07:45.493961Z" $REPO_URL
$SVN_CMD --revprop -r 16305 svn:log "[KSLUM-417]  Program Requirements - Add to View All Sections page
[KSLUM-376]  Create ReqCompFieldType UI Widgets
various fixes and enhancements" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16313 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16313 svn:date "2010-09-21T17:15:47.107346Z" $REPO_URL
$SVN_CMD --revprop -r 16313 svn:log "added module for ks-lum sql files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16357 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 16357 svn:date "2010-09-23T02:22:15.643757Z" $REPO_URL
$SVN_CMD --revprop -r 16357 svn:log "Retrieved and restored and updated the Metadata formater and dictionary tester
Changed lookup on accrediting agencies to look at accreditingbodies only
==> Had to update POM to include lum-impl as a scope test dependency" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16364 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 16364 svn:date "2010-09-23T14:33:17.685519Z" $REPO_URL
$SVN_CMD --revprop -r 16364 svn:log "(1) Fixed unit test failure  on TestStatementServiceImpl
(2) Removed parentId from dictionary
(3) Removed hasMetadata from reqCompField from dictionary
(4) Added when case statement for cluSet.id" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16391 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 16391 svn:date "2010-09-24T15:06:42.820537Z" $REPO_URL
$SVN_CMD --revprop -r 16391 svn:log "[KSPLANA-175] - Decision Rationale functionality" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16395 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 16395 svn:date "2010-09-24T16:16:48.572690Z" $REPO_URL
$SVN_CMD --revprop -r 16395 svn:log "[KSPLANA-175] - Decision Rationale functionality
Adding decision tool to CoreUI. deleting it from common UI" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16402 svn:author "jkneal" $REPO_URL
$SVN_CMD --revprop -r 16402 svn:date "2010-09-24T17:37:18.678251Z" $REPO_URL
$SVN_CMD --revprop -r 16402 svn:log "Committing project to new location" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16403 svn:author "neerav" $REPO_URL
$SVN_CMD --revprop -r 16403 svn:date "2010-09-24T18:46:15.288598Z" $REPO_URL
$SVN_CMD --revprop -r 16403 svn:log "[KSPLANA-175] - Decision Rationale functionality" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16406 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 16406 svn:date "2010-09-24T19:33:22.066983Z" $REPO_URL
$SVN_CMD --revprop -r 16406 svn:log "Modified MetadataServiceImpl to match on object and type in addition to just path before attaching the ui lookup config
Reworked test harness so I could check results
Moved metadata formatter to here so can view output of metdata." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16439 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 16439 svn:date "2010-09-28T02:04:05.740108Z" $REPO_URL
$SVN_CMD --revprop -r 16439 svn:log "[KSLUM-460] Update Course Summary To Include Rules
Various fixes and enhancements" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16463 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 16463 svn:date "2010-09-29T03:32:31.806830Z" $REPO_URL
$SVN_CMD --revprop -r 16463 svn:log "Added LRC letter grade" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16483 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16483 svn:date "2010-09-30T00:10:58.361534Z" $REPO_URL
$SVN_CMD --revprop -r 16483 svn:log "Added edit configurations for Core Program." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16486 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16486 svn:date "2010-09-30T05:46:25.909339Z" $REPO_URL
$SVN_CMD --revprop -r 16486 svn:log "Added baccalaureate edit configurations." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16497 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 16497 svn:date "2010-09-30T18:32:59.469163Z" $REPO_URL
$SVN_CMD --revprop -r 16497 svn:log "Updated enumeration and web content" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16506 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16506 svn:date "2010-09-30T21:47:08.940912Z" $REPO_URL
$SVN_CMD --revprop -r 16506 svn:log "Fixed KSLUM-471" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16514 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16514 svn:date "2010-09-30T23:13:36.955107Z" $REPO_URL
$SVN_CMD --revprop -r 16514 svn:log "Added status to the programs and specializations." $REPO_URL
$SVN_CMD --revprop -r 16515 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16515 svn:date "2010-09-30T23:43:38.221449Z" $REPO_URL
$SVN_CMD --revprop -r 16515 svn:log "1. Improved configuration mini framework by introducing AbstractControllerConfiguration
2. Added \"Program Summary\" item to the program edit menu." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16522 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16522 svn:date "2010-10-01T07:00:25.949881Z" $REPO_URL
$SVN_CMD --revprop -r 16522 svn:log "Done with KSLUM-442 (Approve/Activate - enable/disable buttons based on auth)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16535 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16535 svn:date "2010-10-01T20:19:19.453306Z" $REPO_URL
$SVN_CMD --revprop -r 16535 svn:log "[KSLAB-1035] updated data with latest from impex. added delete scripts to strip ks types" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16605 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 16605 svn:date "2010-10-06T15:16:56.534701Z" $REPO_URL
$SVN_CMD --revprop -r 16605 svn:log "https://jira.kuali.org/browse/KSPLANA-154
Adding in dictionary for program requirement but also partially converting over rest of old dictionary to new format" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16618 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16618 svn:date "2010-10-07T07:04:46.388533Z" $REPO_URL
$SVN_CMD --revprop -r 16618 svn:log "Programs: added light box for dates." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16622 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16622 svn:date "2010-10-07T14:52:08.427533Z" $REPO_URL
$SVN_CMD --revprop -r 16622 svn:log "added object sub types back in" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16637 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16637 svn:date "2010-10-08T03:18:08.160302Z" $REPO_URL
$SVN_CMD --revprop -r 16637 svn:log "1. Created events for working with Specializations.
2. Implemented transition to new specialization when clicking Add Specialization button." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16646 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 16646 svn:date "2010-10-08T17:54:58.375326Z" $REPO_URL
$SVN_CMD --revprop -r 16646 svn:log "Course set integration code to Course Requisite." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16651 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 16651 svn:date "2010-10-08T19:01:36.847094Z" $REPO_URL
$SVN_CMD --revprop -r 16651 svn:log "added dynamic attribute for durationOther for both majorDiscipline and programVariation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16654 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 16654 svn:date "2010-10-08T22:21:16.385071Z" $REPO_URL
$SVN_CMD --revprop -r 16654 svn:log "[KSCOR-417] Version compare, version history search, version view" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16680 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 16680 svn:date "2010-10-12T15:28:49.201578Z" $REPO_URL
$SVN_CMD --revprop -r 16680 svn:log "[KSCOR-460] Update Authorization filter to work with new dictionary and add initial support for field masking.
Split up filter tests and add tests for authorization filter." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16687 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16687 svn:date "2010-10-12T19:46:15.894036Z" $REPO_URL
$SVN_CMD --revprop -r 16687 svn:log "Added obj id and version numbers to all entities.  Made all entities extend BaseEntity in some fashion. Cleaned up some unused entities." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16697 svn:author "stse" $REPO_URL
$SVN_CMD --revprop -r 16697 svn:date "2010-10-13T00:00:18.639211Z" $REPO_URL
$SVN_CMD --revprop -r 16697 svn:log "Moved CluSet widgets from ks-lum-ui to ks-lum-ui-common" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16701 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 16701 svn:date "2010-10-13T02:40:43.940011Z" $REPO_URL
$SVN_CMD --revprop -r 16701 svn:log "Rules stuff" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16787 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16787 svn:date "2010-10-15T06:12:02.850976Z" $REPO_URL
$SVN_CMD --revprop -r 16787 svn:log "Fixed KSLUM-514" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16825 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 16825 svn:date "2010-10-16T00:23:47.127803Z" $REPO_URL
$SVN_CMD --revprop -r 16825 svn:log "[KSCOR-479] Split ks-core into ks-common & ks-cor
[KSCOR-459] Move common dictionary/search/messages packages from core-api to new common-apin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16831 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 16831 svn:date "2010-10-18T12:00:57.850193Z" $REPO_URL
$SVN_CMD --revprop -r 16831 svn:log "Updated config from ks-test to ks-admin
Changed to maven eclipse project
Updated for integration in KS project" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16846 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16846 svn:date "2010-10-19T05:55:18.401449Z" $REPO_URL
$SVN_CMD --revprop -r 16846 svn:log "1. Fixed KSLUM-552
2. Added VariationSummaryConfiguration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16851 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 16851 svn:date "2010-10-19T15:06:40.868615Z" $REPO_URL
$SVN_CMD --revprop -r 16851 svn:log "Fix to gwt source inheritence to reduce gwt compile errors." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16871 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16871 svn:date "2010-10-19T22:12:37.021959Z" $REPO_URL
$SVN_CMD --revprop -r 16871 svn:log "Fixed KSLUM-567" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16873 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 16873 svn:date "2010-10-20T02:42:31.904991Z" $REPO_URL
$SVN_CMD --revprop -r 16873 svn:log "Fixed KSLUM-565" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16884 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 16884 svn:date "2010-10-20T17:59:53.574048Z" $REPO_URL
$SVN_CMD --revprop -r 16884 svn:log "updated cdm-embedded to M8 configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16887 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16887 svn:date "2010-10-20T18:19:33.859369Z" $REPO_URL
$SVN_CMD --revprop -r 16887 svn:log "added tests and config for enumeration mgmt" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16888 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 16888 svn:date "2010-10-20T18:22:50.873662Z" $REPO_URL
$SVN_CMD --revprop -r 16888 svn:log "updated to M8 configuration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16891 svn:author "dchew" $REPO_URL
$SVN_CMD --revprop -r 16891 svn:date "2010-10-20T18:31:12.131177Z" $REPO_URL
$SVN_CMD --revprop -r 16891 svn:log "froze rails version 2.3.8" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16895 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 16895 svn:date "2010-10-20T18:45:08.136789Z" $REPO_URL
$SVN_CMD --revprop -r 16895 svn:log "Moved the messageServiceImpl to an admin package so there is no chance it will collide with the jpa impl.
Pruned some directories that are no longer needed" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16919 svn:author "sgibson" $REPO_URL
$SVN_CMD --revprop -r 16919 svn:date "2010-10-21T03:14:32.633939Z" $REPO_URL
$SVN_CMD --revprop -r 16919 svn:log "KSADM-3 Added ks-admin dependency, altered to portal with links to new maintenance apps" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16929 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 16929 svn:date "2010-10-21T15:03:45.542339Z" $REPO_URL
$SVN_CMD --revprop -r 16929 svn:log "Add icon for rice" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16939 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16939 svn:date "2010-10-21T18:51:41.796213Z" $REPO_URL
$SVN_CMD --revprop -r 16939 svn:log "Added bootstrap data and refactored directory" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16941 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16941 svn:date "2010-10-21T18:56:41.557310Z" $REPO_URL
$SVN_CMD --revprop -r 16941 svn:log "svn rename" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16943 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16943 svn:date "2010-10-21T19:07:26.099964Z" $REPO_URL
$SVN_CMD --revprop -r 16943 svn:log "Added bootstrap data and refactored directory" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 16984 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 16984 svn:date "2010-10-22T15:13:43.624113Z" $REPO_URL
$SVN_CMD --revprop -r 16984 svn:log "refactored directory structure, separated data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17008 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 17008 svn:date "2010-10-22T19:05:15.280234Z" $REPO_URL
$SVN_CMD --revprop -r 17008 svn:log "[KSLAB-1160] Modified statement type and requirement component type order" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17011 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 17011 svn:date "2010-10-22T19:22:02.069115Z" $REPO_URL
$SVN_CMD --revprop -r 17011 svn:log "added in rice db and standalone" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17013 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 17013 svn:date "2010-10-22T19:37:55.453151Z" $REPO_URL
$SVN_CMD --revprop -r 17013 svn:log "fixed bad data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17160 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17160 svn:date "2010-10-27T17:47:01.056432Z" $REPO_URL
$SVN_CMD --revprop -r 17160 svn:log "added test for lo dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17184 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17184 svn:date "2010-10-28T02:01:33.399380Z" $REPO_URL
$SVN_CMD --revprop -r 17184 svn:log "Initial load of source for data loading tool" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17191 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17191 svn:date "2010-10-28T14:46:22.638972Z" $REPO_URL
$SVN_CMD --revprop -r 17191 svn:log "Added ATP service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17211 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17211 svn:date "2010-10-28T19:56:34.082746Z" $REPO_URL
$SVN_CMD --revprop -r 17211 svn:log "Got laoder working and met w/ MPG" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17250 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 17250 svn:date "2010-10-29T14:19:56.246414Z" $REPO_URL
$SVN_CMD --revprop -r 17250 svn:log "Add a pom for ks-standalone" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17286 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17286 svn:date "2010-10-30T01:06:47.843723Z" $REPO_URL
$SVN_CMD --revprop -r 17286 svn:log "began work on programs and changed from atp types to atp seasons" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17296 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 17296 svn:date "2010-10-31T15:48:20.689476Z" $REPO_URL
$SVN_CMD --revprop -r 17296 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17317 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 17317 svn:date "2010-11-02T00:12:37.287353Z" $REPO_URL
$SVN_CMD --revprop -r 17317 svn:log "KSLUM-433 - Initial implementation and wiring of DataService et. al. for CredentialProgram." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17318 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 17318 svn:date "2010-11-02T00:19:48.491283Z" $REPO_URL
$SVN_CMD --revprop -r 17318 svn:log "Somehow the antlr file was missing in action" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17343 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 17343 svn:date "2010-11-02T20:25:26.467198Z" $REPO_URL
$SVN_CMD --revprop -r 17343 svn:log "Programs: fixed problem with major state." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17382 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 17382 svn:date "2010-11-04T17:39:57.510013Z" $REPO_URL
$SVN_CMD --revprop -r 17382 svn:log "KSLAB-1308 KSLAB-1309 Custom Validation for Revenues and Expenditure added" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17422 svn:author "glindhol" $REPO_URL
$SVN_CMD --revprop -r 17422 svn:date "2010-11-05T18:36:45.978557Z" $REPO_URL
$SVN_CMD --revprop -r 17422 svn:log "KSLUM-503: For rule preview, we need natural language translation that corresponds to wireframe" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17452 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 17452 svn:date "2010-11-09T07:01:48.591591Z" $REPO_URL
$SVN_CMD --revprop -r 17452 svn:log "[KSLAB-1407] Create/Update New Requirements Logic Expression Editor (first part)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17459 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 17459 svn:date "2010-11-09T17:30:39.037049Z" $REPO_URL
$SVN_CMD --revprop -r 17459 svn:log "Initial wiring in of Program Versions in the UI." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17477 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 17477 svn:date "2010-11-10T03:00:57.639658Z" $REPO_URL
$SVN_CMD --revprop -r 17477 svn:log "Added SummaryPage for Core program." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17483 svn:author "lcarlsen" $REPO_URL
$SVN_CMD --revprop -r 17483 svn:date "2010-11-10T22:00:42.307234Z" $REPO_URL
$SVN_CMD --revprop -r 17483 svn:log "Added sample tests to show specific rule configurations" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17497 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 17497 svn:date "2010-11-11T20:14:25.981215Z" $REPO_URL
$SVN_CMD --revprop -r 17497 svn:log "[KSLAB-1247] added ks admin to standalone/rice projects." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17503 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 17503 svn:date "2010-11-12T14:47:27.953870Z" $REPO_URL
$SVN_CMD --revprop -r 17503 svn:log "Replace rice favicon with one from KS." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17565 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 17565 svn:date "2010-11-16T17:02:03.769379Z" $REPO_URL
$SVN_CMD --revprop -r 17565 svn:log "Add versioning, approve and activate for bacc (credential programs)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17604 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 17604 svn:date "2010-11-17T13:18:29.038201Z" $REPO_URL
$SVN_CMD --revprop -r 17604 svn:log "Add .classpath .project and .settings to ks-lum-ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17605 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 17605 svn:date "2010-11-17T13:30:12.064867Z" $REPO_URL
$SVN_CMD --revprop -r 17605 svn:log "Add .classpath .project and .settings to sub-modules
Modify pathing in LumGWT.launch " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17630 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17630 svn:date "2010-11-18T03:08:11.658854Z" $REPO_URL
$SVN_CMD --revprop -r 17630 svn:log "Added standardized testing lu types" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17663 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 17663 svn:date "2010-11-19T14:35:31.960942Z" $REPO_URL
$SVN_CMD --revprop -r 17663 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17725 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17725 svn:date "2010-11-23T23:03:33.843413Z" $REPO_URL
$SVN_CMD --revprop -r 17725 svn:log "https://jira.kuali.org/browse/KSLAB-1323
Added dictionary for atp and tester" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17726 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17726 svn:date "2010-11-23T23:04:12.573532Z" $REPO_URL
$SVN_CMD --revprop -r 17726 svn:log "https://jira.kuali.org/browse/KSLAB-1323
Added dictionary for lrc and fleshed out lo dictionary" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17731 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17731 svn:date "2010-11-23T23:32:03.310293Z" $REPO_URL
$SVN_CMD --revprop -r 17731 svn:log "https://jira.kuali.org/browse/KSLAB-1323
Added dictionary Document and Comment" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17772 svn:author "zzraly" $REPO_URL
$SVN_CMD --revprop -r 17772 svn:date "2010-11-29T22:23:29.589531Z" $REPO_URL
$SVN_CMD --revprop -r 17772 svn:log "[KSLAB-1301] FIND COURSE, SINGLE COURSE REQ:: Can add more than one to list" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17798 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 17798 svn:date "2010-11-30T18:33:36.523396Z" $REPO_URL
$SVN_CMD --revprop -r 17798 svn:log "small fix for login on hosted mode" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17802 svn:author "hjohnson" $REPO_URL
$SVN_CMD --revprop -r 17802 svn:date "2010-11-30T20:02:50.309117Z" $REPO_URL
$SVN_CMD --revprop -r 17802 svn:log "KSLAB-1497: Create LoCategoryDataService to allow return of validation messages to catch duplicate lo category create attempts" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17818 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 17818 svn:date "2010-12-01T13:46:35.511657Z" $REPO_URL
$SVN_CMD --revprop -r 17818 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17860 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 17860 svn:date "2010-12-01T22:01:51.629548Z" $REPO_URL
$SVN_CMD --revprop -r 17860 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17868 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 17868 svn:date "2010-12-02T03:51:29.027768Z" $REPO_URL
$SVN_CMD --revprop -r 17868 svn:log "Huge commit:
1. Invariants for programs were changed: the metadata is loaded based on the data.
2. Implemented mechanism for sync metadata for sections." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17915 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17915 svn:date "2010-12-03T02:50:52.337433Z" $REPO_URL
$SVN_CMD --revprop -r 17915 svn:log "save" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17934 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 17934 svn:date "2010-12-03T16:48:27.455257Z" $REPO_URL
$SVN_CMD --revprop -r 17934 svn:log "https://jira.kuali.org/browse/KSLAB-1578
Consolidated GetAtp logic into a helper.
Changed logic so a failed lookup results in a validation error for just that row" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 17937 svn:author "kcampos" $REPO_URL
$SVN_CMD --revprop -r 17937 svn:date "2010-12-03T16:53:53.687645Z" $REPO_URL
$SVN_CMD --revprop -r 17937 svn:log "adding login suite" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18011 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18011 svn:date "2010-12-06T00:21:05.366160Z" $REPO_URL
$SVN_CMD --revprop -r 18011 svn:log "https://jira.kuali.org/browse/KSLAB-1619
Completed Program Load also uploaded one-jar loader to wiki
See https://wiki.kuali.org/display/KULSTG/How+to+Load+Program+Data" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18042 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18042 svn:date "2010-12-07T02:38:26.331525Z" $REPO_URL
$SVN_CMD --revprop -r 18042 svn:log "https://jira.kuali.org/browse/KSLAB-1645
Created spreadsheet with New orgs that are needed for reference data.
Added those into the tab so the ids can be looked up" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18098 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18098 svn:date "2010-12-07T11:17:19.226899Z" $REPO_URL
$SVN_CMD --revprop -r 18098 svn:log "https://jira.kuali.org/browse/KSLAB-1647
Got missing subject areas and created spreadsheet to load them" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18100 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18100 svn:date "2010-12-07T15:21:24.718652Z" $REPO_URL
$SVN_CMD --revprop -r 18100 svn:log "https://jira.kuali.org/browse/KSLAB-1512
Version 0.91 of loader that lincludes Li's validation of orgs
Put ks-loader.jar into resources directory" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18143 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18143 svn:date "2010-12-08T20:52:15.038588Z" $REPO_URL
$SVN_CMD --revprop -r 18143 svn:log "commit latest version .92 version " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18223 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18223 svn:date "2010-12-09T17:52:07.423069Z" $REPO_URL
$SVN_CMD --revprop -r 18223 svn:log "added a tester to run org or relations loader" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18226 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18226 svn:date "2010-12-09T18:15:48.826809Z" $REPO_URL
$SVN_CMD --revprop -r 18226 svn:log "https://jira.kuali.org/browse/KSLAB-1454
Committed the loader jar v 0.93 built Dec 9th" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18234 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 18234 svn:date "2010-12-10T02:50:32.443731Z" $REPO_URL
$SVN_CMD --revprop -r 18234 svn:log "load core & bacc " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18244 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 18244 svn:date "2010-12-10T15:13:47.348651Z" $REPO_URL
$SVN_CMD --revprop -r 18244 svn:log "https://jira.kuali.org/browse/KSLAB-1512
(1) Releasing latest version of loader v 0.94 Dec 10, 2010
(2) Updated ReferencePrograms.xls core and bacc so they can be loaded based on MPG updates
(3) Updated Organizations.xls so PositionRestrictions tab can be loaded" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18366 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18366 svn:date "2010-12-16T17:10:09.161700Z" $REPO_URL
$SVN_CMD --revprop -r 18366 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18380 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18380 svn:date "2010-12-16T20:22:24.218447Z" $REPO_URL
$SVN_CMD --revprop -r 18380 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18440 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 18440 svn:date "2010-12-17T19:13:59.954231Z" $REPO_URL
$SVN_CMD --revprop -r 18440 svn:log "[KSLAB-1706] Add required login.jsp" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18628 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18628 svn:date "2010-12-19T18:21:58.375873Z" $REPO_URL
$SVN_CMD --revprop -r 18628 svn:log "Version 45 of the kuali parent pom" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18637 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18637 svn:date "2010-12-19T18:41:11.558700Z" $REPO_URL
$SVN_CMD --revprop -r 18637 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18669 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 18669 svn:date "2010-12-20T21:09:38.457741Z" $REPO_URL
$SVN_CMD --revprop -r 18669 svn:log "KSLAB-1639 ATP Service validation.  KSLAB-1595 CrossListingInfo now has code and follows the same logic as 'code' on CourseInfo" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18684 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 18684 svn:date "2010-12-21T16:22:04.699705Z" $REPO_URL
$SVN_CMD --revprop -r 18684 svn:log "added identitymanagmementservice override back in after continued jaxws errors" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18723 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18723 svn:date "2010-12-22T14:49:56.972596Z" $REPO_URL
$SVN_CMD --revprop -r 18723 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18729 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 18729 svn:date "2010-12-22T18:23:01.614679Z" $REPO_URL
$SVN_CMD --revprop -r 18729 svn:log "KSLAB-1548 Added dynamic attributes to CluIdentifier and in turn to Clu's official identifier and alternate identifier" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18849 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18849 svn:date "2010-12-30T01:48:09.896138Z" $REPO_URL
$SVN_CMD --revprop -r 18849 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18858 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18858 svn:date "2010-12-30T02:46:09.643975Z" $REPO_URL
$SVN_CMD --revprop -r 18858 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18863 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18863 svn:date "2010-12-30T15:57:58.088664Z" $REPO_URL
$SVN_CMD --revprop -r 18863 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18876 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 18876 svn:date "2010-12-30T16:55:22.822585Z" $REPO_URL
$SVN_CMD --revprop -r 18876 svn:log "Use slf4 + log4j to get control over log statements issued by libraries called by the plugin (in this case Amazon's AWS SDK)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 18914 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 18914 svn:date "2011-01-03T20:24:59.740196Z" $REPO_URL
$SVN_CMD --revprop -r 18914 svn:log "[KSLAB-1491] took out extraneous statement service classes. 
Made statemenet rpc use read only transactions
pulled out change state method into its own transactional class" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19093 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 19093 svn:date "2011-01-12T21:06:06.718316Z" $REPO_URL
$SVN_CMD --revprop -r 19093 svn:log "updated contract version" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19311 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19311 svn:date "2011-01-26T17:44:02.749837Z" $REPO_URL
$SVN_CMD --revprop -r 19311 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19330 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19330 svn:date "2011-01-28T12:54:57.459753Z" $REPO_URL
$SVN_CMD --revprop -r 19330 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19333 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19333 svn:date "2011-01-28T13:10:09.640646Z" $REPO_URL
$SVN_CMD --revprop -r 19333 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19354 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19354 svn:date "2011-01-28T16:32:43.490742Z" $REPO_URL
$SVN_CMD --revprop -r 19354 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19379 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 19379 svn:date "2011-01-28T22:12:10.366257Z" $REPO_URL
$SVN_CMD --revprop -r 19379 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19393 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 19393 svn:date "2011-01-31T15:32:06.079459Z" $REPO_URL
$SVN_CMD --revprop -r 19393 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19394 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 19394 svn:date "2011-01-31T15:33:27.873231Z" $REPO_URL
$SVN_CMD --revprop -r 19394 svn:log "Initial import." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19419 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 19419 svn:date "2011-02-03T15:30:24.368387Z" $REPO_URL
$SVN_CMD --revprop -r 19419 svn:log "Got the logic to read the model using QDox basically working so wanted to save" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19443 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19443 svn:date "2011-02-04T21:03:45.500268Z" $REPO_URL
$SVN_CMD --revprop -r 19443 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19479 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19479 svn:date "2011-02-06T12:50:57.891915Z" $REPO_URL
$SVN_CMD --revprop -r 19479 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19487 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19487 svn:date "2011-02-06T13:26:24.352962Z" $REPO_URL
$SVN_CMD --revprop -r 19487 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19657 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 19657 svn:date "2011-02-09T22:57:24.051579Z" $REPO_URL
$SVN_CMD --revprop -r 19657 svn:log "Got the generation of pure java interfaces working" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19781 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 19781 svn:date "2011-02-16T15:24:32.096472Z" $REPO_URL
$SVN_CMD --revprop -r 19781 svn:log "KSLAB-1262 Move around core ui packages and update/cleanup inherits in gwt.xml files." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19840 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 19840 svn:date "2011-02-17T23:09:41.366653Z" $REPO_URL
$SVN_CMD --revprop -r 19840 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19841 svn:author "bsmith" $REPO_URL
$SVN_CMD --revprop -r 19841 svn:date "2011-02-17T23:14:05.767489Z" $REPO_URL
$SVN_CMD --revprop -r 19841 svn:log "ignores" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19871 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19871 svn:date "2011-02-19T14:28:06.649776Z" $REPO_URL
$SVN_CMD --revprop -r 19871 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 19873 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 19873 svn:date "2011-02-19T16:47:53.852737Z" $REPO_URL
$SVN_CMD --revprop -r 19873 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20004 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20004 svn:date "2011-02-21T15:53:41.869162Z" $REPO_URL
$SVN_CMD --revprop -r 20004 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20043 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20043 svn:date "2011-02-24T23:44:07.471577Z" $REPO_URL
$SVN_CMD --revprop -r 20043 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20092 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 20092 svn:date "2011-03-01T17:36:06.768907Z" $REPO_URL
$SVN_CMD --revprop -r 20092 svn:log "Creating the maven ks contract doc plugin for converting to HTML from Java service contracts source for wiki documentation " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20097 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20097 svn:date "2011-03-01T20:46:03.421417Z" $REPO_URL
$SVN_CMD --revprop -r 20097 svn:log "Default Eclipse config files generated by m2eclipse (no springnature yet)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20100 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20100 svn:date "2011-03-01T20:57:35.607273Z" $REPO_URL
$SVN_CMD --revprop -r 20100 svn:log "With Spring nature, and validation on Spring config files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20107 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 20107 svn:date "2011-03-01T22:02:44.050964Z" $REPO_URL
$SVN_CMD --revprop -r 20107 svn:log "Got the build path configured" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20109 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20109 svn:date "2011-03-01T22:29:30.390947Z" $REPO_URL
$SVN_CMD --revprop -r 20109 svn:log "Remove top level eclipse config files, add Spring nature, .springBeans with config sets" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20111 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20111 svn:date "2011-03-01T22:41:11.592149Z" $REPO_URL
$SVN_CMD --revprop -r 20111 svn:log "Spring nature and .springBeans with config sets" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20113 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20113 svn:date "2011-03-02T01:07:47.482316Z" $REPO_URL
$SVN_CMD --revprop -r 20113 svn:log "With Spring nature" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20114 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 20114 svn:date "2011-03-02T01:08:34.915751Z" $REPO_URL
$SVN_CMD --revprop -r 20114 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20115 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20115 svn:date "2011-03-02T01:11:15.526519Z" $REPO_URL
$SVN_CMD --revprop -r 20115 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20133 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 20133 svn:date "2011-03-03T16:20:16.712797Z" $REPO_URL
$SVN_CMD --revprop -r 20133 svn:log "First checkin for LPR service artifacts. Only JAX-WS annotated service opps and JAXB annotated DTOs" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20137 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 20137 svn:date "2011-03-03T17:54:03.963647Z" $REPO_URL
$SVN_CMD --revprop -r 20137 svn:log "added LU service (sans LUI) with ContextInfo" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20141 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 20141 svn:date "2011-03-03T20:14:00.164420Z" $REPO_URL
$SVN_CMD --revprop -r 20141 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20142 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 20142 svn:date "2011-03-03T20:14:38.921929Z" $REPO_URL
$SVN_CMD --revprop -r 20142 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20160 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 20160 svn:date "2011-03-04T00:22:47.515955Z" $REPO_URL
$SVN_CMD --revprop -r 20160 svn:log "elcipse artifacts" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20196 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20196 svn:date "2011-03-07T00:59:05.330295Z" $REPO_URL
$SVN_CMD --revprop -r 20196 svn:log "Got initial cut at Types Enum Done" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20221 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20221 svn:date "2011-03-08T16:13:53.677629Z" $REPO_URL
$SVN_CMD --revprop -r 20221 svn:log "Got rid of files we don't need" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20226 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20226 svn:date "2011-03-08T17:01:16.329661Z" $REPO_URL
$SVN_CMD --revprop -r 20226 svn:log "fixed LuiPersonRealtionSErvice test to point to right context
added version for hsqldb to pom" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20229 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20229 svn:date "2011-03-08T17:52:13.892487Z" $REPO_URL
$SVN_CMD --revprop -r 20229 svn:log "Added missing parameter checker and runtimeexception catcher adaptors" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20231 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 20231 svn:date "2011-03-08T18:06:51.047039Z" $REPO_URL
$SVN_CMD --revprop -r 20231 svn:log "an example federating adapter for LPR" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20238 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20238 svn:date "2011-03-08T18:58:11.931224Z" $REPO_URL
$SVN_CMD --revprop -r 20238 svn:log "example of how we don't have to copy data if we use pure java interfaces" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20255 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 20255 svn:date "2011-03-09T06:32:57.047779Z" $REPO_URL
$SVN_CMD --revprop -r 20255 svn:log "eclipse artifacts and minor  POM mods" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20260 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20260 svn:date "2011-03-09T13:47:22.247695Z" $REPO_URL
$SVN_CMD --revprop -r 20260 svn:log "moved mock impl to services-impl project" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20268 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 20268 svn:date "2011-03-09T16:56:35.425816Z" $REPO_URL
$SVN_CMD --revprop -r 20268 svn:log "checking refactored files that were earlier missed" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20270 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 20270 svn:date "2011-03-09T17:04:28.310887Z" $REPO_URL
$SVN_CMD --revprop -r 20270 svn:log "Test cases-refactored package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20281 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20281 svn:date "2011-03-09T21:21:02.396680Z" $REPO_URL
$SVN_CMD --revprop -r 20281 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20289 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20289 svn:date "2011-03-09T23:56:31.238559Z" $REPO_URL
$SVN_CMD --revprop -r 20289 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20311 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20311 svn:date "2011-03-10T15:14:32.685006Z" $REPO_URL
$SVN_CMD --revprop -r 20311 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20340 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 20340 svn:date "2011-03-10T21:01:03.111441Z" $REPO_URL
$SVN_CMD --revprop -r 20340 svn:log "Added alternative module ks-services-alternative" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20361 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20361 svn:date "2011-03-11T15:57:57.037771Z" $REPO_URL
$SVN_CMD --revprop -r 20361 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20362 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20362 svn:date "2011-03-11T16:03:29.752015Z" $REPO_URL
$SVN_CMD --revprop -r 20362 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20372 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 20372 svn:date "2011-03-11T19:24:30.671864Z" $REPO_URL
$SVN_CMD --revprop -r 20372 svn:log "Updated Info to implement the Infc" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20398 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20398 svn:date "2011-03-12T01:47:45.284699Z" $REPO_URL
$SVN_CMD --revprop -r 20398 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20410 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20410 svn:date "2011-03-13T14:23:08.173695Z" $REPO_URL
$SVN_CMD --revprop -r 20410 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20415 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20415 svn:date "2011-03-13T14:37:05.157619Z" $REPO_URL
$SVN_CMD --revprop -r 20415 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20422 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20422 svn:date "2011-03-13T19:26:39.845794Z" $REPO_URL
$SVN_CMD --revprop -r 20422 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20480 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20480 svn:date "2011-03-16T22:32:37.897290Z" $REPO_URL
$SVN_CMD --revprop -r 20480 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20482 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20482 svn:date "2011-03-16T22:42:07.434338Z" $REPO_URL
$SVN_CMD --revprop -r 20482 svn:log "added tests and testing jar" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20491 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 20491 svn:date "2011-03-17T14:41:23.420281Z" $REPO_URL
$SVN_CMD --revprop -r 20491 svn:log "Refactor the common.ui.client.widgets package containg rules to core.statement.ui package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20496 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20496 svn:date "2011-03-17T16:50:06.037561Z" $REPO_URL
$SVN_CMD --revprop -r 20496 svn:log "Add default site descriptor" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20502 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20502 svn:date "2011-03-17T18:33:30.358739Z" $REPO_URL
$SVN_CMD --revprop -r 20502 svn:log "Add site descriptors" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20544 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20544 svn:date "2011-03-18T11:41:29.301183Z" $REPO_URL
$SVN_CMD --revprop -r 20544 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20547 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20547 svn:date "2011-03-18T15:56:14.086722Z" $REPO_URL
$SVN_CMD --revprop -r 20547 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20553 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 20553 svn:date "2011-03-18T22:07:42.939563Z" $REPO_URL
$SVN_CMD --revprop -r 20553 svn:log "
Remote service test, sans interfaces (sigh)
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20613 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20613 svn:date "2011-03-21T17:39:51.464014Z" $REPO_URL
$SVN_CMD --revprop -r 20613 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20616 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20616 svn:date "2011-03-21T17:53:07.845144Z" $REPO_URL
$SVN_CMD --revprop -r 20616 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20641 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20641 svn:date "2011-03-22T16:28:41.335643Z" $REPO_URL
$SVN_CMD --revprop -r 20641 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20642 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20642 svn:date "2011-03-22T17:10:41.164528Z" $REPO_URL
$SVN_CMD --revprop -r 20642 svn:log "Got dictionary formatter working" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20650 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20650 svn:date "2011-03-22T20:39:41.414747Z" $REPO_URL
$SVN_CMD --revprop -r 20650 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20693 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20693 svn:date "2011-03-24T02:56:09.405008Z" $REPO_URL
$SVN_CMD --revprop -r 20693 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20768 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20768 svn:date "2011-03-26T22:44:56.884015Z" $REPO_URL
$SVN_CMD --revprop -r 20768 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20779 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20779 svn:date "2011-03-27T21:27:15.976756Z" $REPO_URL
$SVN_CMD --revprop -r 20779 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20781 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20781 svn:date "2011-03-28T15:48:00.386293Z" $REPO_URL
$SVN_CMD --revprop -r 20781 svn:log "Got dictionary completey integrated" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20818 svn:author "cmann" $REPO_URL
$SVN_CMD --revprop -r 20818 svn:date "2011-03-30T14:34:07.308469Z" $REPO_URL
$SVN_CMD --revprop -r 20818 svn:log "KSLAB-1476: Supporting Documents page - listing of existing attached documents needs column headers as per wireframe (Course and Program)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20823 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 20823 svn:date "2011-03-30T17:38:21.987377Z" $REPO_URL
$SVN_CMD --revprop -r 20823 svn:log "Added validation to the service and test." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20824 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 20824 svn:date "2011-03-30T17:55:26.955265Z" $REPO_URL
$SVN_CMD --revprop -r 20824 svn:log "Change DTO builders' setters to just the property's name
  (e.g. setFoo() -> foo())
Move HasAttributesAndMetaInfc to the right package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20879 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20879 svn:date "2011-04-04T02:28:03.444685Z" $REPO_URL
$SVN_CMD --revprop -r 20879 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20888 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20888 svn:date "2011-04-04T14:12:47.424366Z" $REPO_URL
$SVN_CMD --revprop -r 20888 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20896 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 20896 svn:date "2011-04-04T15:34:45.841041Z" $REPO_URL
$SVN_CMD --revprop -r 20896 svn:log "KSLAB-1902 Add new proposal reports." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20913 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 20913 svn:date "2011-04-04T20:12:25.900617Z" $REPO_URL
$SVN_CMD --revprop -r 20913 svn:log "ATP service with context and mapping of Milestones" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20924 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 20924 svn:date "2011-04-05T14:56:35.397208Z" $REPO_URL
$SVN_CMD --revprop -r 20924 svn:log "Cleaned up searchParam and AtpService documentation" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20934 svn:author "ikuplevich" $REPO_URL
$SVN_CMD --revprop -r 20934 svn:date "2011-04-05T20:18:50.060290Z" $REPO_URL
$SVN_CMD --revprop -r 20934 svn:log "Added daos, data loader and test for one of the method of LPRService." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20935 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 20935 svn:date "2011-04-05T20:33:08.858417Z" $REPO_URL
$SVN_CMD --revprop -r 20935 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20952 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 20952 svn:date "2011-04-06T04:41:18.424560Z" $REPO_URL
$SVN_CMD --revprop -r 20952 svn:log "new layering and refactoring existing code" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20967 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 20967 svn:date "2011-04-06T15:36:09.394469Z" $REPO_URL
$SVN_CMD --revprop -r 20967 svn:log "[KSLAB-1898] Added lu program search, added screens for browse program, added filter for results" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20971 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 20971 svn:date "2011-04-06T17:26:58.958581Z" $REPO_URL
$SVN_CMD --revprop -r 20971 svn:log "KSLAB-1824 - All work done by Bonnie for POC" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20977 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 20977 svn:date "2011-04-06T22:40:50.484913Z" $REPO_URL
$SVN_CMD --revprop -r 20977 svn:log "Updated BaseType to have refObjectURI" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 20979 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 20979 svn:date "2011-04-06T22:49:50.450208Z" $REPO_URL
$SVN_CMD --revprop -r 20979 svn:log "Changed to make it generic single type for LPR service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21035 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 21035 svn:date "2011-04-07T15:29:26.816130Z" $REPO_URL
$SVN_CMD --revprop -r 21035 svn:log "Created ModelBuilder to enforce build method patter in all the Builders" $REPO_URL
$SVN_CMD --revprop -r 21038 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 21038 svn:date "2011-04-07T20:41:18.794217Z" $REPO_URL
$SVN_CMD --revprop -r 21038 svn:log "Tests disabled until JPA mapping fixed
Entity names now end with \"Entity\"
Lots o' stuff moved around" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21043 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21043 svn:date "2011-04-07T22:05:02.203327Z" $REPO_URL
$SVN_CMD --revprop -r 21043 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21048 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21048 svn:date "2011-04-07T22:58:25.851162Z" $REPO_URL
$SVN_CMD --revprop -r 21048 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21059 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21059 svn:date "2011-04-08T16:04:23.056775Z" $REPO_URL
$SVN_CMD --revprop -r 21059 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21126 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 21126 svn:date "2011-04-11T11:20:41.958413Z" $REPO_URL
$SVN_CMD --revprop -r 21126 svn:log "KSLAB-1883 - Course Information displayed on to the right when using IE - Working fine in WInXP and IE8, still needs to confirm it working in WIn7 and IE8" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21134 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 21134 svn:date "2011-04-11T13:36:46.251905Z" $REPO_URL
$SVN_CMD --revprop -r 21134 svn:log "(1) saving partial work on permission service mock impl
(2) atp dictionary updates 
(3) finished refactoring of getType () and getState () to getTypeKey () and getStateKey ()" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21152 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 21152 svn:date "2011-04-11T17:20:37.367538Z" $REPO_URL
$SVN_CMD --revprop -r 21152 svn:log "added new goal ksdictionarydoc that generates dictionary documentation out on the site" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21215 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 21215 svn:date "2011-04-12T16:36:12.202616Z" $REPO_URL
$SVN_CMD --revprop -r 21215 svn:log "fixed Infc interfaces" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21286 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 21286 svn:date "2011-04-13T14:51:43.407108Z" $REPO_URL
$SVN_CMD --revprop -r 21286 svn:log "refactored classes used in testing and cleaned up
fixed calculation of main classes to strip *List from end of type to get the real type that was being missed" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21288 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 21288 svn:date "2011-04-13T15:11:23.566794Z" $REPO_URL
$SVN_CMD --revprop -r 21288 svn:log "removed Infc extensions in interfaces for academic calendar
fixed builder setter method in HolidayInfo" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21291 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 21291 svn:date "2011-04-13T15:25:31.142479Z" $REPO_URL
$SVN_CMD --revprop -r 21291 svn:log "moved to root so on class path" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21302 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21302 svn:date "2011-04-13T20:21:29.029804Z" $REPO_URL
$SVN_CMD --revprop -r 21302 svn:log "KS-1932 - Updated IDE files for ks-common" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21308 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21308 svn:date "2011-04-13T21:25:41.485185Z" $REPO_URL
$SVN_CMD --revprop -r 21308 svn:log "KS-1932 - Updated IDE files " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21310 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21310 svn:date "2011-04-13T21:28:30.792201Z" $REPO_URL
$SVN_CMD --revprop -r 21310 svn:log "KS-1932 - Updated IDE files " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21312 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21312 svn:date "2011-04-13T21:32:50.688144Z" $REPO_URL
$SVN_CMD --revprop -r 21312 svn:log "KS-1932 - Updated IDE files " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21313 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21313 svn:date "2011-04-13T21:34:44.960233Z" $REPO_URL
$SVN_CMD --revprop -r 21313 svn:log "KS-1932 - Updated IDE files " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21314 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21314 svn:date "2011-04-13T21:37:29.912651Z" $REPO_URL
$SVN_CMD --revprop -r 21314 svn:log "KS-1932 - Updated IDE files " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21315 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21315 svn:date "2011-04-13T21:43:45.615892Z" $REPO_URL
$SVN_CMD --revprop -r 21315 svn:log "KS-1932 - Updated IDE files " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21348 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21348 svn:date "2011-04-14T19:41:51.031527Z" $REPO_URL
$SVN_CMD --revprop -r 21348 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21569 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 21569 svn:date "2011-04-21T17:52:44.935142Z" $REPO_URL
$SVN_CMD --revprop -r 21569 svn:log "ATP Mock perisstence impl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21743 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21743 svn:date "2011-05-06T17:09:02.896003Z" $REPO_URL
$SVN_CMD --revprop -r 21743 svn:log "These 2 were being .svnignore'd from the prior setup.  " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21850 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 21850 svn:date "2011-05-16T11:29:32.748442Z" $REPO_URL
$SVN_CMD --revprop -r 21850 svn:log "KSLAB-1899 - Add new Analysis report template for dependency analysis type views." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 21886 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 21886 svn:date "2011-05-18T14:59:16.541490Z" $REPO_URL
$SVN_CMD --revprop -r 21886 svn:log "Add .classpath and .project Eclipse IDE files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 22701 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 22701 svn:date "2011-07-11T14:52:55.390490Z" $REPO_URL
$SVN_CMD --revprop -r 22701 svn:log "[KSLAB-2092] fixed Org ui launch not working, added missing login jsp and refactored rice configurer class" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 22707 svn:author "sambit" $REPO_URL
$SVN_CMD --revprop -r 22707 svn:date "2011-07-11T16:59:31.890453Z" $REPO_URL
$SVN_CMD --revprop -r 22707 svn:log "KSPROJPLAN-576 Course Reg service review changes" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 22805 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 22805 svn:date "2011-07-15T20:21:33.223879Z" $REPO_URL
$SVN_CMD --revprop -r 22805 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 22997 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 22997 svn:date "2011-07-24T23:46:08.157506Z" $REPO_URL
$SVN_CMD --revprop -r 22997 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23023 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 23023 svn:date "2011-07-26T19:28:46.389230Z" $REPO_URL
$SVN_CMD --revprop -r 23023 svn:log "KSPROJPLAN-614  -- Ton's of changes to fix bugs and extend the dictionary to work with sub-objects
Now has 2 other goals in addition to the contractdoc
createdictionary - creates krad dictionary files automatically from the contract definitions
documentdictionary - generates html documentation for the dictionary an publishes to the site alongside the contract docs
KSPROJPLAN-76 - Added in dictionary html report generator to the SITE" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23050 svn:author "jim7" $REPO_URL
$SVN_CMD --revprop -r 23050 svn:date "2011-07-27T21:51:57.214658Z" $REPO_URL
$SVN_CMD --revprop -r 23050 svn:log "
Changes to LUI, and move Lui-related tables to their own module directory
" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23062 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 23062 svn:date "2011-07-28T03:22:01.889973Z" $REPO_URL
$SVN_CMD --revprop -r 23062 svn:log "renamed setters so they include the is to match the getters  
removed try/catch so can see errors" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23094 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 23094 svn:date "2011-07-29T19:10:50.992483Z" $REPO_URL
$SVN_CMD --revprop -r 23094 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23153 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 23153 svn:date "2011-08-01T22:39:04.390381Z" $REPO_URL
$SVN_CMD --revprop -r 23153 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23203 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 23203 svn:date "2011-08-03T20:37:09.164067Z" $REPO_URL
$SVN_CMD --revprop -r 23203 svn:log "modified dictionary formatter to use controlField instead of control
added logic to parse the XSD file from pesc to generate contract docs" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23330 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 23330 svn:date "2011-08-10T19:55:48.553964Z" $REPO_URL
$SVN_CMD --revprop -r 23330 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 23827 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 23827 svn:date "2011-09-06T19:38:03.260169Z" $REPO_URL
$SVN_CMD --revprop -r 23827 svn:log "https://jira.kuali.org/browse/KSPROJPLAN-614
Refactored for M7 upgrade
Changed how complex sub-objects work -- they now use ComplexAttributeDefinition instead of entryName and dot notation to point to them
Similar (but different) for CollectionDefinition..." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24160 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24160 svn:date "2011-09-27T00:14:01.529166Z" $REPO_URL
$SVN_CMD --revprop -r 24160 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24166 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24166 svn:date "2011-09-27T04:56:58.051802Z" $REPO_URL
$SVN_CMD --revprop -r 24166 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24360 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24360 svn:date "2011-10-07T20:46:24.875627Z" $REPO_URL
$SVN_CMD --revprop -r 24360 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24386 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24386 svn:date "2011-10-10T16:52:21.762216Z" $REPO_URL
$SVN_CMD --revprop -r 24386 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24400 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24400 svn:date "2011-10-11T17:52:06.904836Z" $REPO_URL
$SVN_CMD --revprop -r 24400 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24414 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 24414 svn:date "2011-10-12T14:01:44.897225Z" $REPO_URL
$SVN_CMD --revprop -r 24414 svn:log "https://jira.kuali.org/browse/KSPROJPLAN-478
Aligned constants and sql inserts to match wiki
Did not add missing sql inserts just fixed ones that did not match" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24518 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24518 svn:date "2011-10-18T05:20:22.072899Z" $REPO_URL
$SVN_CMD --revprop -r 24518 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24560 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24560 svn:date "2011-10-20T00:14:54.382658Z" $REPO_URL
$SVN_CMD --revprop -r 24560 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24564 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24564 svn:date "2011-10-20T00:51:55.012315Z" $REPO_URL
$SVN_CMD --revprop -r 24564 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24630 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24630 svn:date "2011-10-22T06:03:43.946215Z" $REPO_URL
$SVN_CMD --revprop -r 24630 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24666 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 24666 svn:date "2011-10-25T02:13:29.875533Z" $REPO_URL
$SVN_CMD --revprop -r 24666 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24725 svn:author "komandur" $REPO_URL
$SVN_CMD --revprop -r 24725 svn:date "2011-10-27T22:37:07.990532Z" $REPO_URL
$SVN_CMD --revprop -r 24725 svn:log "Copying document service from 1.1 directory to r2" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24782 svn:author "sambit" $REPO_URL
$SVN_CMD --revprop -r 24782 svn:date "2011-11-01T21:47:57.967491Z" $REPO_URL
$SVN_CMD --revprop -r 24782 svn:log "Revert: KSPROJPLAN-651 revert First level of changes for DTO" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24784 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 24784 svn:date "2011-11-01T21:57:38.958884Z" $REPO_URL
$SVN_CMD --revprop -r 24784 svn:log "Initial import" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24786 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 24786 svn:date "2011-11-01T22:01:14.254083Z" $REPO_URL
$SVN_CMD --revprop -r 24786 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24810 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 24810 svn:date "2011-11-03T14:26:26.514076Z" $REPO_URL
$SVN_CMD --revprop -r 24810 svn:log "This is the configuration project set up that should be created when 
after completing 
KS Training - Curriculum Management - Sample Config Project Setup

It should be used as the basis for the rest of the configuration training" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 24832 svn:author "komandur" $REPO_URL
$SVN_CMD --revprop -r 24832 svn:date "2011-11-04T16:56:43.729860Z" $REPO_URL
$SVN_CMD --revprop -r 24832 svn:log "KSPROJPLAN-647 1.2 to ENR migration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25034 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 25034 svn:date "2011-11-13T21:48:06.466426Z" $REPO_URL
$SVN_CMD --revprop -r 25034 svn:log "myschool-config-project after completing the dictionary validation training" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25144 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 25144 svn:date "2011-11-21T15:38:23.180360Z" $REPO_URL
$SVN_CMD --revprop -r 25144 svn:log "Ran it on the Rice2.0 beta service" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25192 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 25192 svn:date "2011-11-22T05:56:34.812348Z" $REPO_URL
$SVN_CMD --revprop -r 25192 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25193 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 25193 svn:date "2011-11-22T06:03:10.794106Z" $REPO_URL
$SVN_CMD --revprop -r 25193 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25194 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 25194 svn:date "2011-11-22T06:13:59.795159Z" $REPO_URL
$SVN_CMD --revprop -r 25194 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25195 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 25195 svn:date "2011-11-22T06:25:55.139981Z" $REPO_URL
$SVN_CMD --revprop -r 25195 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25233 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 25233 svn:date "2011-11-25T08:24:15.401739Z" $REPO_URL
$SVN_CMD --revprop -r 25233 svn:log "Change Service-API to use seperate version - <version>\${kuali.student.api.version}</version>" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25291 svn:author "komandur" $REPO_URL
$SVN_CMD --revprop -r 25291 svn:date "2011-12-01T22:31:39.687589Z" $REPO_URL
$SVN_CMD --revprop -r 25291 svn:log "KSPROJPLAN-100 Merge 1.2 Message Service to ENR" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25359 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 25359 svn:date "2011-12-07T01:35:20.038265Z" $REPO_URL
$SVN_CMD --revprop -r 25359 svn:log "Synch the services branch from ks-1.3 " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25379 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 25379 svn:date "2011-12-07T22:15:56.762982Z" $REPO_URL
$SVN_CMD --revprop -r 25379 svn:log "Synch the services branch from ks-1.3 " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25400 svn:author "bobhurt" $REPO_URL
$SVN_CMD --revprop -r 25400 svn:date "2011-12-08T23:39:29.225261Z" $REPO_URL
$SVN_CMD --revprop -r 25400 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25425 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 25425 svn:date "2011-12-10T01:23:21.773285Z" $REPO_URL
$SVN_CMD --revprop -r 25425 svn:log "Merged ks-1.3-services back to ks-1.3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25426 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 25426 svn:date "2011-12-10T01:23:45.470717Z" $REPO_URL
$SVN_CMD --revprop -r 25426 svn:log "Merged ks-1.3-services back to ks-1.3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25461 svn:author "paulheald" $REPO_URL
$SVN_CMD --revprop -r 25461 svn:date "2011-12-13T17:05:22.383503Z" $REPO_URL
$SVN_CMD --revprop -r 25461 svn:log "Test commit" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25468 svn:author "tapresco" $REPO_URL
$SVN_CMD --revprop -r 25468 svn:date "2011-12-13T18:37:27.571380Z" $REPO_URL
$SVN_CMD --revprop -r 25468 svn:log "KSPROJPLAN-740 Moving to impl directory" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25486 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 25486 svn:date "2011-12-14T01:35:54.505508Z" $REPO_URL
$SVN_CMD --revprop -r 25486 svn:log "saving all the changes I accidently made in the 1.3 branch instead of the poc branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25515 svn:author "komandur" $REPO_URL
$SVN_CMD --revprop -r 25515 svn:date "2011-12-15T03:51:51.811296Z" $REPO_URL
$SVN_CMD --revprop -r 25515 svn:log "KSPROJPLAN-771  Initial copy before migration work" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25675 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 25675 svn:date "2011-12-21T00:11:34.614853Z" $REPO_URL
$SVN_CMD --revprop -r 25675 svn:log "changes on rice 2-b1 upgrade" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25676 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 25676 svn:date "2011-12-21T00:15:56.091266Z" $REPO_URL
$SVN_CMD --revprop -r 25676 svn:log "changes on rice 2-b1 upgrade" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25800 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 25800 svn:date "2011-12-31T19:35:24.239777Z" $REPO_URL
$SVN_CMD --revprop -r 25800 svn:log "added even better error message to indicate the source of the problem
Added testing to test Bean2DictionaryConverter.java" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25801 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 25801 svn:date "2011-12-31T19:41:03.781910Z" $REPO_URL
$SVN_CMD --revprop -r 25801 svn:log "Did global replaces to convert all references from Key to Id in all enroll projects including enroll-ui.

https://jira.kuali.org/browse/KSPROJPLAN-855

Ran dictionary creator on all main objects.

Documented dictionary creation process out on wiki
https://wiki.kuali.org/display/STUDENT/Data+Dictionary+File+Generation+and+Maintenance

fixed lots of issues found in source code" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25846 svn:author "lperthel" $REPO_URL
$SVN_CMD --revprop -r 25846 svn:date "2012-01-04T22:41:08.222802Z" $REPO_URL
$SVN_CMD --revprop -r 25846 svn:log "KSLAB-2518:Adding new folder for new version of KS" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25860 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 25860 svn:date "2012-01-06T07:27:18.001828Z" $REPO_URL
$SVN_CMD --revprop -r 25860 svn:log "Internationalization poc." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25869 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 25869 svn:date "2012-01-06T07:56:09.264949Z" $REPO_URL
$SVN_CMD --revprop -r 25869 svn:log "Internationalization poc." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25870 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 25870 svn:date "2012-01-06T07:59:39.806009Z" $REPO_URL
$SVN_CMD --revprop -r 25870 svn:log "Internationalization poc." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25882 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 25882 svn:date "2012-01-06T08:19:48.221440Z" $REPO_URL
$SVN_CMD --revprop -r 25882 svn:log "Internationalization poc." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25888 svn:author "kmuthusw" $REPO_URL
$SVN_CMD --revprop -r 25888 svn:date "2012-01-06T17:57:20.022815Z" $REPO_URL
$SVN_CMD --revprop -r 25888 svn:log "Added Myplan-api and myplan-impl modules" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 25924 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 25924 svn:date "2012-01-09T15:27:39.509320Z" $REPO_URL
$SVN_CMD --revprop -r 25924 svn:log "added separate Type service in core package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26049 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 26049 svn:date "2012-01-13T14:36:04.598453Z" $REPO_URL
$SVN_CMD --revprop -r 26049 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26093 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26093 svn:date "2012-01-15T21:22:31.600166Z" $REPO_URL
$SVN_CMD --revprop -r 26093 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26094 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26094 svn:date "2012-01-15T21:27:31.948817Z" $REPO_URL
$SVN_CMD --revprop -r 26094 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26096 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26096 svn:date "2012-01-15T21:30:07.822529Z" $REPO_URL
$SVN_CMD --revprop -r 26096 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26097 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26097 svn:date "2012-01-15T21:40:50.699109Z" $REPO_URL
$SVN_CMD --revprop -r 26097 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26099 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26099 svn:date "2012-01-16T07:45:47.737225Z" $REPO_URL
$SVN_CMD --revprop -r 26099 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26101 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26101 svn:date "2012-01-16T07:50:44.767794Z" $REPO_URL
$SVN_CMD --revprop -r 26101 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26119 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26119 svn:date "2012-01-16T19:14:18.017261Z" $REPO_URL
$SVN_CMD --revprop -r 26119 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26120 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26120 svn:date "2012-01-16T20:06:01.429022Z" $REPO_URL
$SVN_CMD --revprop -r 26120 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26121 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26121 svn:date "2012-01-16T20:09:09.765248Z" $REPO_URL
$SVN_CMD --revprop -r 26121 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26122 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26122 svn:date "2012-01-16T20:12:29.275388Z" $REPO_URL
$SVN_CMD --revprop -r 26122 svn:log "kscm-131 - fix compile errors and package names refering to .r2.
Also add dependency on temp ks-core-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26138 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26138 svn:date "2012-01-17T10:48:19.320750Z" $REPO_URL
$SVN_CMD --revprop -r 26138 svn:log "KSCM-146 merged ks-enroll-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26190 svn:author "johan.oosthuysen" $REPO_URL
$SVN_CMD --revprop -r 26190 svn:date "2012-01-18T10:58:15.980796Z" $REPO_URL
$SVN_CMD --revprop -r 26190 svn:log "KSCM-115 - removed *.jaxws packages in ks-common-temp" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26210 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 26210 svn:date "2012-01-18T15:00:07.346879Z" $REPO_URL
$SVN_CMD --revprop -r 26210 svn:log "KSCM-137 - Add base decorator." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26238 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 26238 svn:date "2012-01-19T09:03:53.420847Z" $REPO_URL
$SVN_CMD --revprop -r 26238 svn:log "KSCM-154" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26239 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 26239 svn:date "2012-01-19T09:26:22.969591Z" $REPO_URL
$SVN_CMD --revprop -r 26239 svn:log "KSCM-156" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26271 svn:author "christoff.botha" $REPO_URL
$SVN_CMD --revprop -r 26271 svn:date "2012-01-20T13:05:31.909110Z" $REPO_URL
$SVN_CMD --revprop -r 26271 svn:log "Trying to get rice deployed to Jetty from workspace." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26276 svn:author "jacobusroos" $REPO_URL
$SVN_CMD --revprop -r 26276 svn:date "2012-01-20T14:27:20.426259Z" $REPO_URL
$SVN_CMD --revprop -r 26276 svn:log "KSCM-163" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26298 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26298 svn:date "2012-01-22T18:34:20.471077Z" $REPO_URL
$SVN_CMD --revprop -r 26298 svn:log "KSCM-166 fIX cOMPILE ERRORS" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26318 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 26318 svn:date "2012-01-24T00:56:20.051376Z" $REPO_URL
$SVN_CMD --revprop -r 26318 svn:log "KSPROJPLAN-861 Process POC branch merged to ks-1.3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26337 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26337 svn:date "2012-01-24T14:26:34.107040Z" $REPO_URL
$SVN_CMD --revprop -r 26337 svn:log "KSCM-210 Fix compile errors - Copy Enumeration from ks-1.3 after SW implemented it" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26364 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26364 svn:date "2012-01-24T20:21:54.958497Z" $REPO_URL
$SVN_CMD --revprop -r 26364 svn:log "KSCM-210 Fix compile errors - Copy Enumeration from ks-1.3 after SW implemented it" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26365 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26365 svn:date "2012-01-24T20:32:04.739271Z" $REPO_URL
$SVN_CMD --revprop -r 26365 svn:log "KSCM-210 Fix compile errors - Copy Enumeration from ks-1.3 after SW implemented it" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26372 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26372 svn:date "2012-01-24T21:22:41.641797Z" $REPO_URL
$SVN_CMD --revprop -r 26372 svn:log "KSCM-210 Fix compile errors - Copy from ks-1.3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26373 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26373 svn:date "2012-01-24T21:24:58.062923Z" $REPO_URL
$SVN_CMD --revprop -r 26373 svn:log "KSCM-210 Fix compile errors - Copy from ks-1.3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26577 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 26577 svn:date "2012-02-01T15:10:07.558677Z" $REPO_URL
$SVN_CMD --revprop -r 26577 svn:log "Fix code for rice upgrade" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26578 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 26578 svn:date "2012-02-01T15:11:53.655119Z" $REPO_URL
$SVN_CMD --revprop -r 26578 svn:log "Fix code for rice upgrade" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26580 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 26580 svn:date "2012-02-01T15:13:15.576137Z" $REPO_URL
$SVN_CMD --revprop -r 26580 svn:log "Fix code for rice upgrade" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26581 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 26581 svn:date "2012-02-01T15:13:42.855287Z" $REPO_URL
$SVN_CMD --revprop -r 26581 svn:log "Fix code for rice upgrade" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26637 svn:author "alubbers" $REPO_URL
$SVN_CMD --revprop -r 26637 svn:date "2012-02-02T22:43:59.630266Z" $REPO_URL
$SVN_CMD --revprop -r 26637 svn:log "KSENROLL-457 Further fixes to try to get impex data up to current data set" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26674 svn:author "alubbers" $REPO_URL
$SVN_CMD --revprop -r 26674 svn:date "2012-02-03T16:15:32.512005Z" $REPO_URL
$SVN_CMD --revprop -r 26674 svn:log "KSENROLL-457 Adding ks-enroll upgrade sql scripts to ks-core-sql for now to make impex pick them up" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26679 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 26679 svn:date "2012-02-03T17:11:04.985391Z" $REPO_URL
$SVN_CMD --revprop -r 26679 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26721 svn:author "lpan" $REPO_URL
$SVN_CMD --revprop -r 26721 svn:date "2012-02-04T20:55:51.586473Z" $REPO_URL
$SVN_CMD --revprop -r 26721 svn:log "ksenroll-432: add package org.kuali.student.enrollment.acal &courseoffering packages; move hceditview.xml to the new location" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26779 svn:author "johan.oosthuysen" $REPO_URL
$SVN_CMD --revprop -r 26779 svn:date "2012-02-07T10:33:24.453999Z" $REPO_URL
$SVN_CMD --revprop -r 26779 svn:log "Copied from KS-1.2" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26813 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26813 svn:date "2012-02-07T21:49:54.675480Z" $REPO_URL
$SVN_CMD --revprop -r 26813 svn:log "Replace ks-common-ui with cm-1.2 after it was upgrade to rice-2.0" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26841 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26841 svn:date "2012-02-08T11:09:04.616490Z" $REPO_URL
$SVN_CMD --revprop -r 26841 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26854 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 26854 svn:date "2012-02-08T13:46:10.692805Z" $REPO_URL
$SVN_CMD --revprop -r 26854 svn:log "KSCM-272 Compare ks-embedded-db in ks-cfg-db with enrollment" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26910 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 26910 svn:date "2012-02-09T10:32:33.679225Z" $REPO_URL
$SVN_CMD --revprop -r 26910 svn:log "KSCM-276: Merged ks-core-ui with the latest from KS trunk." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26944 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 26944 svn:date "2012-02-10T11:02:18.440538Z" $REPO_URL
$SVN_CMD --revprop -r 26944 svn:log "KSCM-301: Merged the lum module with the latest from KS Trunk." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26952 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 26952 svn:date "2012-02-10T13:19:17.489392Z" $REPO_URL
$SVN_CMD --revprop -r 26952 svn:log "Initial import of a blank ks-core-merged folder structure." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26956 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 26956 svn:date "2012-02-10T13:41:19.502072Z" $REPO_URL
$SVN_CMD --revprop -r 26956 svn:log "Initial import of a blank ks-common-merged folder structure." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 26958 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 26958 svn:date "2012-02-10T14:54:21.571859Z" $REPO_URL
$SVN_CMD --revprop -r 26958 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27004 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27004 svn:date "2012-02-13T09:39:07.717873Z" $REPO_URL
$SVN_CMD --revprop -r 27004 svn:log "Initial import of ks-core-ui code from the ks-core-temp-ui project." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27008 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27008 svn:date "2012-02-13T11:17:06.883481Z" $REPO_URL
$SVN_CMD --revprop -r 27008 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27009 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27009 svn:date "2012-02-13T11:41:21.424185Z" $REPO_URL
$SVN_CMD --revprop -r 27009 svn:log "Initial import of ks-common-ui code from the ks-common-temp-ui project." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27011 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27011 svn:date "2012-02-13T12:26:37.405972Z" $REPO_URL
$SVN_CMD --revprop -r 27011 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27025 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27025 svn:date "2012-02-13T17:48:39.366466Z" $REPO_URL
$SVN_CMD --revprop -r 27025 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27042 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27042 svn:date "2012-02-14T07:49:30.140747Z" $REPO_URL
$SVN_CMD --revprop -r 27042 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27051 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27051 svn:date "2012-02-14T14:17:38.960886Z" $REPO_URL
$SVN_CMD --revprop -r 27051 svn:log "KSCM - 138 - Create Orgranization Service test." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27073 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27073 svn:date "2012-02-15T07:26:38.715051Z" $REPO_URL
$SVN_CMD --revprop -r 27073 svn:log "KSCM-311: Merged the ks-2.0-cm-rice branch into the CM2 code base." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27123 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27123 svn:date "2012-02-16T13:15:20.628640Z" $REPO_URL
$SVN_CMD --revprop -r 27123 svn:log "KSCM - 138 - Merge organization service to services branch." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27124 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27124 svn:date "2012-02-16T14:13:38.810511Z" $REPO_URL
$SVN_CMD --revprop -r 27124 svn:log "Merged TRUNK as r1, ENROL as R2 and what was in common ks-1.3 as just the common package
common - ui was copied from trunk and altered" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27152 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 27152 svn:date "2012-02-17T15:02:04.761126Z" $REPO_URL
$SVN_CMD --revprop -r 27152 svn:log "fixing compile bugs so I can get the contract docs to deploy
I don't have an F'n jira because I shouldn't be doing this it should be done by the people causing the errors but no one but Me and Cathy seem to care about the contractdocs!" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27174 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27174 svn:date "2012-02-18T18:44:03.618195Z" $REPO_URL
$SVN_CMD --revprop -r 27174 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27179 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27179 svn:date "2012-02-20T07:21:07.142596Z" $REPO_URL
$SVN_CMD --revprop -r 27179 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27180 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27180 svn:date "2012-02-20T07:24:02.550998Z" $REPO_URL
$SVN_CMD --revprop -r 27180 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27191 svn:author "johan.oosthuysen" $REPO_URL
$SVN_CMD --revprop -r 27191 svn:date "2012-02-20T12:27:24.202019Z" $REPO_URL
$SVN_CMD --revprop -r 27191 svn:log "KSCM-314:  Add @Deprecated to interfaces in ks-common-impl." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27193 svn:author "johan.oosthuysen" $REPO_URL
$SVN_CMD --revprop -r 27193 svn:date "2012-02-20T12:34:42.911841Z" $REPO_URL
$SVN_CMD --revprop -r 27193 svn:log "KSCM-314:  Add @Deprecated to interfaces in ks-common-test." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27196 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27196 svn:date "2012-02-20T14:13:42.807870Z" $REPO_URL
$SVN_CMD --revprop -r 27196 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27201 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27201 svn:date "2012-02-20T14:31:30.694152Z" $REPO_URL
$SVN_CMD --revprop -r 27201 svn:log "Merged code from trunk, cm2.0 and ks-1.3-service - cleanup the code according to ks-ernoll-api r2 package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27228 svn:author "johan.oosthuysen" $REPO_URL
$SVN_CMD --revprop -r 27228 svn:date "2012-02-21T14:46:54.537661Z" $REPO_URL
$SVN_CMD --revprop -r 27228 svn:log "KSCM-320:  Check if TODOs in ks-core-ui can be uncommented without causing build errors." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27240 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27240 svn:date "2012-02-22T08:05:36.073578Z" $REPO_URL
$SVN_CMD --revprop -r 27240 svn:log "KSCM-321" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27243 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27243 svn:date "2012-02-22T09:07:21.987928Z" $REPO_URL
$SVN_CMD --revprop -r 27243 svn:log "kscm-284 added new ks-lum2" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27253 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27253 svn:date "2012-02-22T10:31:56.694069Z" $REPO_URL
$SVN_CMD --revprop -r 27253 svn:log "kscm-321 - merged from ks-1.3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27255 svn:author "jacobusroos" $REPO_URL
$SVN_CMD --revprop -r 27255 svn:date "2012-02-22T10:50:45.582888Z" $REPO_URL
$SVN_CMD --revprop -r 27255 svn:log "KSCM-323" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27265 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27265 svn:date "2012-02-22T12:09:36.516093Z" $REPO_URL
$SVN_CMD --revprop -r 27265 svn:log "kscm-284 added new ks-lum2" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27272 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27272 svn:date "2012-02-22T19:30:26.796239Z" $REPO_URL
$SVN_CMD --revprop -r 27272 svn:log "kscm-284 added new ks-lum2 - fix compile errors in lum-common-ui
Fixed the ValidationResultInfo imports" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27278 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27278 svn:date "2012-02-22T21:52:04.619623Z" $REPO_URL
$SVN_CMD --revprop -r 27278 svn:log "kscm-284 added new ks-lum2 - fix compile errors in lum-common-ui
Fixed the ValidationResultInfo imports" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27330 svn:author "jacobusroos" $REPO_URL
$SVN_CMD --revprop -r 27330 svn:date "2012-02-24T08:10:16.203565Z" $REPO_URL
$SVN_CMD --revprop -r 27330 svn:log "''" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27358 svn:author "jacobusroos" $REPO_URL
$SVN_CMD --revprop -r 27358 svn:date "2012-02-24T10:30:41.471279Z" $REPO_URL
$SVN_CMD --revprop -r 27358 svn:log "KSCM-331" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27359 svn:author "jacobusroos" $REPO_URL
$SVN_CMD --revprop -r 27359 svn:date "2012-02-24T10:31:04.954599Z" $REPO_URL
$SVN_CMD --revprop -r 27359 svn:log "KSCM-331" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27360 svn:author "jacobusroos" $REPO_URL
$SVN_CMD --revprop -r 27360 svn:date "2012-02-24T10:36:00.278427Z" $REPO_URL
$SVN_CMD --revprop -r 27360 svn:log "KSCM-323" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27367 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27367 svn:date "2012-02-24T12:53:24.991696Z" $REPO_URL
$SVN_CMD --revprop -r 27367 svn:log "KSCM-329 See ks-lum-impl : package org.kuali.student.lum.statement.config.context" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27573 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27573 svn:date "2012-02-29T10:36:40.608890Z" $REPO_URL
$SVN_CMD --revprop -r 27573 svn:log "KSCM-344 Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-admin" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27574 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 27574 svn:date "2012-02-29T10:37:55.878626Z" $REPO_URL
$SVN_CMD --revprop -r 27574 svn:log "KSCM-343" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27577 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 27577 svn:date "2012-02-29T10:46:34.283495Z" $REPO_URL
$SVN_CMD --revprop -r 27577 svn:log "KSCM-343" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27579 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27579 svn:date "2012-02-29T10:53:54.631289Z" $REPO_URL
$SVN_CMD --revprop -r 27579 svn:log "KSCM-345  Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27587 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27587 svn:date "2012-02-29T11:55:54.463382Z" $REPO_URL
$SVN_CMD --revprop -r 27587 svn:log "KSCM-345  Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27589 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27589 svn:date "2012-02-29T11:59:50.792519Z" $REPO_URL
$SVN_CMD --revprop -r 27589 svn:log "KSCM-345  Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27591 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27591 svn:date "2012-02-29T12:06:36.655072Z" $REPO_URL
$SVN_CMD --revprop -r 27591 svn:log "KSCM-345  Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27593 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27593 svn:date "2012-02-29T12:17:14.563860Z" $REPO_URL
$SVN_CMD --revprop -r 27593 svn:log "KSCM-345  Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27594 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27594 svn:date "2012-02-29T12:19:04.681952Z" $REPO_URL
$SVN_CMD --revprop -r 27594 svn:log "KSCM-345  Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27596 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 27596 svn:date "2012-02-29T12:29:52.357939Z" $REPO_URL
$SVN_CMD --revprop -r 27596 svn:log "KSCM-345  Copy over changes in ks-2.0-cm-rice to merged ks-2.0-cm : \ks-core" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27601 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 27601 svn:date "2012-02-29T13:07:44.872887Z" $REPO_URL
$SVN_CMD --revprop -r 27601 svn:log "KSCM-356" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27603 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 27603 svn:date "2012-02-29T13:25:56.633110Z" $REPO_URL
$SVN_CMD --revprop -r 27603 svn:log "KSCM-356" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27604 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 27604 svn:date "2012-02-29T13:43:01.067013Z" $REPO_URL
$SVN_CMD --revprop -r 27604 svn:log "KSCM-356" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27623 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 27623 svn:date "2012-03-01T07:59:07.623696Z" $REPO_URL
$SVN_CMD --revprop -r 27623 svn:log "KSCM-356" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27655 svn:author "adriaanengelbrecht" $REPO_URL
$SVN_CMD --revprop -r 27655 svn:date "2012-03-02T14:02:07.804023Z" $REPO_URL
$SVN_CMD --revprop -r 27655 svn:log "KSCM-328 : also addedorg.kuali.student.lum.course.service.impl classes
KSCM-358 : Initial comit for assemble method" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27752 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27752 svn:date "2012-03-05T07:33:57.607619Z" $REPO_URL
$SVN_CMD --revprop -r 27752 svn:log "KSCM-313: Added r1 resources to common-merged2, core-merged2 and lum2 projects in order to import GWT resources." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27793 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27793 svn:date "2012-03-06T06:18:03.409157Z" $REPO_URL
$SVN_CMD --revprop -r 27793 svn:log "KSCM-313: Added r1 resources to common-merged2, core-merged2 and lum2 projects in order to import GWT resources." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27795 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27795 svn:date "2012-03-06T06:26:40.125619Z" $REPO_URL
$SVN_CMD --revprop -r 27795 svn:log "KSCM-313: Added r1 resources to common-merged2, core-merged2 and lum2 projects in order to import GWT resources." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27796 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27796 svn:date "2012-03-06T06:31:23.213801Z" $REPO_URL
$SVN_CMD --revprop -r 27796 svn:log "KSCM-313: Added r1 resources to common-merged2, core-merged2 and lum2 projects in order to import GWT resources." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27797 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27797 svn:date "2012-03-06T06:34:52.944861Z" $REPO_URL
$SVN_CMD --revprop -r 27797 svn:log "KSCM-313: Added r1 resources to common-merged2, core-merged2 and lum2 projects in order to import GWT resources." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27806 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27806 svn:date "2012-03-06T13:35:02.832747Z" $REPO_URL
$SVN_CMD --revprop -r 27806 svn:log "KSCM-313: Edited/Added GWT Module XML files to the merged2 projects. " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27855 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27855 svn:date "2012-03-07T07:46:32.381118Z" $REPO_URL
$SVN_CMD --revprop -r 27855 svn:log "KSCM - 313 - Fix GWT code in merged branches" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27860 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27860 svn:date "2012-03-07T08:21:37.470867Z" $REPO_URL
$SVN_CMD --revprop -r 27860 svn:log "KSCM - 313 - Fix GWT code in merged branches." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27862 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27862 svn:date "2012-03-07T08:25:30.193189Z" $REPO_URL
$SVN_CMD --revprop -r 27862 svn:log "KSCM - 313 - Fix GWT code in merged branches." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27877 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27877 svn:date "2012-03-07T09:47:29.116209Z" $REPO_URL
$SVN_CMD --revprop -r 27877 svn:log "KSCM - 313 - Fix GWT code." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27880 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27880 svn:date "2012-03-07T10:09:07.610946Z" $REPO_URL
$SVN_CMD --revprop -r 27880 svn:log "KSCM - 313 - Fix GWT code." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27882 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27882 svn:date "2012-03-07T10:12:43.763929Z" $REPO_URL
$SVN_CMD --revprop -r 27882 svn:log "KSCM - 313 - Fix GWT code." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27883 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 27883 svn:date "2012-03-07T10:13:07.129528Z" $REPO_URL
$SVN_CMD --revprop -r 27883 svn:log "KSCM - 313 - Fix GWT code." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27898 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 27898 svn:date "2012-03-07T12:03:29.964006Z" $REPO_URL
$SVN_CMD --revprop -r 27898 svn:log "KSCM-313: Updated Maven dependencies to make use exclusively of the 0.0.6-SNAPSHOT core, common and lum version. Also added the missing ks-core-web project content." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 27913 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 27913 svn:date "2012-03-07T13:41:37.592112Z" $REPO_URL
$SVN_CMD --revprop -r 27913 svn:log "Fix TEst cases" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28071 svn:author "carelmeintjes" $REPO_URL
$SVN_CMD --revprop -r 28071 svn:date "2012-03-08T08:52:26.839744Z" $REPO_URL
$SVN_CMD --revprop -r 28071 svn:log "\\-ks-lum test copied from trunc" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28080 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 28080 svn:date "2012-03-08T09:46:07.019350Z" $REPO_URL
$SVN_CMD --revprop -r 28080 svn:log "KSCM-367" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28114 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 28114 svn:date "2012-03-08T13:07:00.118907Z" $REPO_URL
$SVN_CMD --revprop -r 28114 svn:log "KSCM-313: Fixed some GWT compile errors." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28161 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28161 svn:date "2012-03-08T23:47:56.883747Z" $REPO_URL
$SVN_CMD --revprop -r 28161 svn:log "KSPROJPLAN-921 Added code and dependencies for common-ws and common-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28163 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28163 svn:date "2012-03-09T00:49:52.164202Z" $REPO_URL
$SVN_CMD --revprop -r 28163 svn:log "KSPROJPLAN-921 Added ks-core-api code" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28170 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 28170 svn:date "2012-03-09T12:38:57.951672Z" $REPO_URL
$SVN_CMD --revprop -r 28170 svn:log "KSCM-313: Fixed some errors while trying to get the app running in Tomcat." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28181 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28181 svn:date "2012-03-09T18:08:41.384514Z" $REPO_URL
$SVN_CMD --revprop -r 28181 svn:log "KSPROJPLAN-921 Added ks-lum-api and ks-enroll-api files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28189 svn:author "wgomes" $REPO_URL
$SVN_CMD --revprop -r 28189 svn:date "2012-03-09T19:58:10.633940Z" $REPO_URL
$SVN_CMD --revprop -r 28189 svn:log "KSLAB-2518 Refactor and fix Permission SQL Files, will be later used for impex." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28203 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28203 svn:date "2012-03-10T00:05:10.791883Z" $REPO_URL
$SVN_CMD --revprop -r 28203 svn:log "KSPROJPLAN-946 Updated ks-enroll-api code to latest rice-2.0 integration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28204 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28204 svn:date "2012-03-10T00:05:32.136737Z" $REPO_URL
$SVN_CMD --revprop -r 28204 svn:log "KSPROJPLAN-946 Updated ks-enroll-api code to latest rice-2.0 integration" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28276 svn:author "garettg" $REPO_URL
$SVN_CMD --revprop -r 28276 svn:date "2012-03-12T17:16:58.536168Z" $REPO_URL
$SVN_CMD --revprop -r 28276 svn:log "Added images for css" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28279 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28279 svn:date "2012-03-12T18:17:44.867059Z" $REPO_URL
$SVN_CMD --revprop -r 28279 svn:log "KSPROJPLAN-946 Added code and dependencies for ks-common-util" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28292 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28292 svn:date "2012-03-12T21:07:55.273610Z" $REPO_URL
$SVN_CMD --revprop -r 28292 svn:log "KSPROJPLAN-952 Added ks-common-test code and dependencies." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28395 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28395 svn:date "2012-03-13T16:43:41.758331Z" $REPO_URL
$SVN_CMD --revprop -r 28395 svn:log "KSPROJPLAN-952 Added ks-common-util test code and added ks-metro dependencies" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28397 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28397 svn:date "2012-03-13T17:15:18.270238Z" $REPO_URL
$SVN_CMD --revprop -r 28397 svn:log "KSPROJPLAN-952 Added ks-common-impl code and dependencies" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28398 svn:author "sudduth" $REPO_URL
$SVN_CMD --revprop -r 28398 svn:date "2012-03-13T17:24:11.709968Z" $REPO_URL
$SVN_CMD --revprop -r 28398 svn:log "Added stub file for static HTML audit report to be used by DegreeAuditServiceMockImpl.java." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28429 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28429 svn:date "2012-03-13T23:41:57.180916Z" $REPO_URL
$SVN_CMD --revprop -r 28429 svn:log "KSPROJPLAN-952 Added ks-common-ui code and dependencies" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28430 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 28430 svn:date "2012-03-14T00:38:09.861197Z" $REPO_URL
$SVN_CMD --revprop -r 28430 svn:log "KSPROJPLAN-953: added files from exporting r28388 of ks-lum directory from ks-1.3 branch
Does not compile yet." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28431 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 28431 svn:date "2012-03-14T01:31:30.989332Z" $REPO_URL
$SVN_CMD --revprop -r 28431 svn:log "KSPROJPLAN-952: Added directories ks-core-impl and ks-core-ui from
export of r28388 of ks-1.3 branch. Compiles fine (skipped tests)." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28483 svn:author "sambitpa" $REPO_URL
$SVN_CMD --revprop -r 28483 svn:date "2012-03-14T19:36:04.849244Z" $REPO_URL
$SVN_CMD --revprop -r 28483 svn:log "KSENROLL-559 Compilable code from ks-1.3-services, first pass of merge" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28488 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28488 svn:date "2012-03-14T20:02:33.301403Z" $REPO_URL
$SVN_CMD --revprop -r 28488 svn:log "KSPROJPLAN-952 Added code and dependencies for ks-standard-sec" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28492 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 28492 svn:date "2012-03-14T20:59:50.845355Z" $REPO_URL
$SVN_CMD --revprop -r 28492 svn:log "KSPROJPLAN-954: Added content for ks-enroll-impl (export of r28388 from
ks-1.3 branch)
2 files added here from ks-core-api (may need refactoring of package name
later on):
ks-enroll-impl/src/main/java/org/kuali/student/core/statement/util/RulesEvaluationUtil.java
ks-enroll-impl/src/main/java/org/kuali/student/core/statement/util/PropositionBuilder.java" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28496 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 28496 svn:date "2012-03-14T21:27:27.260729Z" $REPO_URL
$SVN_CMD --revprop -r 28496 svn:log "KSPROJPLAN-954: Added content for ks-enroll-ui (export of r28388 from ks-1.3 branch)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28497 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28497 svn:date "2012-03-14T21:27:36.085008Z" $REPO_URL
$SVN_CMD --revprop -r 28497 svn:log "KSPROJPLAN-952 Added code and dependencies for ks-sts and ks-core-web" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28498 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 28498 svn:date "2012-03-14T21:55:05.914515Z" $REPO_URL
$SVN_CMD --revprop -r 28498 svn:log "KSPROJPLAN-954: Added content for ks-enroll-web (export of r28388 from
ks-1.3 branch)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28509 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 28509 svn:date "2012-03-15T08:00:18.840723Z" $REPO_URL
$SVN_CMD --revprop -r 28509 svn:log "kscm-367 - Fix test cases" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28682 svn:author "jo25" $REPO_URL
$SVN_CMD --revprop -r 28682 svn:date "2012-03-19T17:37:02.193617Z" $REPO_URL
$SVN_CMD --revprop -r 28682 svn:log "moving to correct package" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28766 svn:author "mivanov" $REPO_URL
$SVN_CMD --revprop -r 28766 svn:date "2012-03-20T23:58:56.010547Z" $REPO_URL
$SVN_CMD --revprop -r 28766 svn:log "applying KRAD...." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28779 svn:author "mivanov" $REPO_URL
$SVN_CMD --revprop -r 28779 svn:date "2012-03-21T16:36:36.596305Z" $REPO_URL
$SVN_CMD --revprop -r 28779 svn:log "added images" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28786 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28786 svn:date "2012-03-21T21:22:29.133297Z" $REPO_URL
$SVN_CMD --revprop -r 28786 svn:log "KSPROJPLAN-954 Updated ks-enroll-ui code to latest from ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28787 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 28787 svn:date "2012-03-21T21:24:11.394636Z" $REPO_URL
$SVN_CMD --revprop -r 28787 svn:log "KSPROJPLAN-952 Committed some test files missed from ks-sts" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28791 svn:author "mivanov" $REPO_URL
$SVN_CMD --revprop -r 28791 svn:date "2012-03-21T22:29:40.314156Z" $REPO_URL
$SVN_CMD --revprop -r 28791 svn:log "added sampleu components" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28852 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 28852 svn:date "2012-03-22T14:24:43.860661Z" $REPO_URL
$SVN_CMD --revprop -r 28852 svn:log "KSCM-484" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28879 svn:author "mivanov" $REPO_URL
$SVN_CMD --revprop -r 28879 svn:date "2012-03-23T00:07:50.175915Z" $REPO_URL
$SVN_CMD --revprop -r 28879 svn:log "refactoring, OJB stuff fixing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 28991 svn:author "mivanov" $REPO_URL
$SVN_CMD --revprop -r 28991 svn:date "2012-03-26T21:33:59.225103Z" $REPO_URL
$SVN_CMD --revprop -r 28991 svn:log "Update a few Kuali UI resources from Rice 2.0" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29005 svn:author "dedewet" $REPO_URL
$SVN_CMD --revprop -r 29005 svn:date "2012-03-27T08:28:50.171756Z" $REPO_URL
$SVN_CMD --revprop -r 29005 svn:log "KSCM-49" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29049 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 29049 svn:date "2012-03-27T19:48:31.574754Z" $REPO_URL
$SVN_CMD --revprop -r 29049 svn:log "KSENROLL-686 added api code" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29067 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 29067 svn:date "2012-03-28T08:50:37.374912Z" $REPO_URL
$SVN_CMD --revprop -r 29067 svn:log "KSCM-499: Merged the latest code from trunk (up to date with 27 March 2012)." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29104 svn:author "tcoppeto" $REPO_URL
$SVN_CMD --revprop -r 29104 svn:date "2012-03-28T21:34:21.633048Z" $REPO_URL
$SVN_CMD --revprop -r 29104 svn:log "KSENROLL-719: added rest of Lui entities" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29113 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 29113 svn:date "2012-03-29T07:46:18.145544Z" $REPO_URL
$SVN_CMD --revprop -r 29113 svn:log "KSCM-334: Fixed converter tests after the ks-conversion refactor." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29114 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 29114 svn:date "2012-03-29T08:03:41.802196Z" $REPO_URL
$SVN_CMD --revprop -r 29114 svn:log "KSCM-334: Fixed converter tests after the ks-conversion refactor." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29117 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 29117 svn:date "2012-03-29T08:40:18.974892Z" $REPO_URL
$SVN_CMD --revprop -r 29117 svn:log "KSCM-334: Fixed converter tests after the ks-conversion refactor." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29192 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29192 svn:date "2012-03-30T19:41:07.509499Z" $REPO_URL
$SVN_CMD --revprop -r 29192 svn:log "KSPROJPLAN-954 Updated ks-enroll-ui and ks-enroll-impl code to latest from ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29212 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29212 svn:date "2012-03-30T23:29:27.330882Z" $REPO_URL
$SVN_CMD --revprop -r 29212 svn:log "KSPROJPLAN-960 Added initial-db data files for appropriate modules" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29321 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29321 svn:date "2012-04-04T00:02:54.243022Z" $REPO_URL
$SVN_CMD --revprop -r 29321 svn:log "KSPROJPLAN-960 Moved ks-lum initial statement data in ks-1.3 to ks-core-sql" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29365 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 29365 svn:date "2012-04-04T12:31:44.154525Z" $REPO_URL
$SVN_CMD --revprop -r 29365 svn:log "KSCM-563: Copied a missing class from trunk." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29411 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 29411 svn:date "2012-04-04T17:14:14.914112Z" $REPO_URL
$SVN_CMD --revprop -r 29411 svn:log "KSENROLL-559: manual impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29425 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29425 svn:date "2012-04-05T00:09:48.314034Z" $REPO_URL
$SVN_CMD --revprop -r 29425 svn:log "KSPROJPLAN-960 Added core upgrade scripts and introduced rice db upgrade scripts into a separate folder" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29480 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29480 svn:date "2012-04-05T20:34:19.528346Z" $REPO_URL
$SVN_CMD --revprop -r 29480 svn:log "KSPROJPLAN-954 Updated ks-enroll-ui and ks-enroll-impl code to latest from ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29485 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29485 svn:date "2012-04-05T22:25:31.460936Z" $REPO_URL
$SVN_CMD --revprop -r 29485 svn:log "KSPROJPLAN-954 Updated ks-enroll-impl with new tests from ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29487 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29487 svn:date "2012-04-05T23:21:48.761627Z" $REPO_URL
$SVN_CMD --revprop -r 29487 svn:log "KSPROJPLAN-960 Added some lum upgrade scripts, moved core-only sql files into core-sql" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29545 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 29545 svn:date "2012-04-08T15:33:18.038670Z" $REPO_URL
$SVN_CMD --revprop -r 29545 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29547 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 29547 svn:date "2012-04-08T15:33:32.888632Z" $REPO_URL
$SVN_CMD --revprop -r 29547 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29576 svn:author "lsymms" $REPO_URL
$SVN_CMD --revprop -r 29576 svn:date "2012-04-09T20:06:17.044674Z" $REPO_URL
$SVN_CMD --revprop -r 29576 svn:log "KSENROLL-790 - still fixing LprTransationItem" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29590 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29590 svn:date "2012-04-09T22:59:28.352380Z" $REPO_URL
$SVN_CMD --revprop -r 29590 svn:log "KSPROJPLAN-960 Added some lum upgrade scripts, moved core-only sql files into core-sql" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29591 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29591 svn:date "2012-04-10T00:02:08.412662Z" $REPO_URL
$SVN_CMD --revprop -r 29591 svn:log "KSPROJPLAN-960 Updated ks-enroll-sql scripts to the latest merge result on the ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29639 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29639 svn:date "2012-04-10T17:52:12.651381Z" $REPO_URL
$SVN_CMD --revprop -r 29639 svn:log "KSPROJPLAN-960 Adding latest merge result from ks-1.3 branch for ks-enroll-api" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29643 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29643 svn:date "2012-04-10T18:14:20.602964Z" $REPO_URL
$SVN_CMD --revprop -r 29643 svn:log "KSPROJPLAN-960 Adding latest merge result from ks-1.3 branch for ks-enroll-impl" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29644 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29644 svn:date "2012-04-10T18:23:50.322085Z" $REPO_URL
$SVN_CMD --revprop -r 29644 svn:log "KSPROJPLAN-960 Adding latest merge result from ks-1.3 branch for ks-enroll-ui" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29655 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29655 svn:date "2012-04-10T19:19:22.937680Z" $REPO_URL
$SVN_CMD --revprop -r 29655 svn:log "KSPROJPLAN-960 Updating ks-enroll-impl to synch with latest ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29672 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 29672 svn:date "2012-04-11T01:29:06.041105Z" $REPO_URL
$SVN_CMD --revprop -r 29672 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29674 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 29674 svn:date "2012-04-11T01:29:21.555858Z" $REPO_URL
$SVN_CMD --revprop -r 29674 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29700 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 29700 svn:date "2012-04-11T14:58:27.616670Z" $REPO_URL
$SVN_CMD --revprop -r 29700 svn:log "KSENROLL-826: manual impex update (until db-automation is fixed)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29794 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 29794 svn:date "2012-04-12T22:02:38.202447Z" $REPO_URL
$SVN_CMD --revprop -r 29794 svn:log "KSPROJPLAN-960 Updating files in ks-enroll-impl to synch with latest ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29818 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 29818 svn:date "2012-04-13T10:29:46.766842Z" $REPO_URL
$SVN_CMD --revprop -r 29818 svn:log "KSCM-313 - Get app in running state." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 29843 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 29843 svn:date "2012-04-13T22:02:17.087444Z" $REPO_URL
$SVN_CMD --revprop -r 29843 svn:log "Wrote AppointmentServiceMockImpl with associated tests
Ran those tests against existing JPA persistence impl and fixed jpa persistence impl where it failed
Implemented more ops
Implemented dynamic attributes on appointment
Making these same changes in ks-1.3 merge
https://jira.kuali.org/browse/KSENROLL-839 " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30033 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 30033 svn:date "2012-04-19T11:56:49.490408Z" $REPO_URL
$SVN_CMD --revprop -r 30033 svn:log "KSCM-550 - Upgrade to official Rice 2.0.0 release." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30066 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 30066 svn:date "2012-04-19T22:24:19.299726Z" $REPO_URL
$SVN_CMD --revprop -r 30066 svn:log "KSPROJPLAN-955 Updating ks-enroll-sql scripts to synch with latest ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30072 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 30072 svn:date "2012-04-19T22:52:31.065244Z" $REPO_URL
$SVN_CMD --revprop -r 30072 svn:log "KSPROJPLAN-955 Adding generated impex files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30084 svn:author "andrewlubbers" $REPO_URL
$SVN_CMD --revprop -r 30084 svn:date "2012-04-20T15:30:25.980750Z" $REPO_URL
$SVN_CMD --revprop -r 30084 svn:log "KSPROJPLAN-955 Adding generated impex files" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30220 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 30220 svn:date "2012-04-24T07:29:53.461316Z" $REPO_URL
$SVN_CMD --revprop -r 30220 svn:log "KSCM-550 - Upgrade to official Rice 2.0.0 release." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30222 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 30222 svn:date "2012-04-24T07:31:50.868121Z" $REPO_URL
$SVN_CMD --revprop -r 30222 svn:log "KSCM-550 - Upgrade to official Rice 2.0.0 release." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30264 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 30264 svn:date "2012-04-24T20:59:48.742853Z" $REPO_URL
$SVN_CMD --revprop -r 30264 svn:log "KSPROJPLAN-954: added from ks-core-api directly to ks-enroll-impl (as we do
not want to introduce extra KRMS dependencies in ks-api)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30292 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 30292 svn:date "2012-04-25T14:26:41.757521Z" $REPO_URL
$SVN_CMD --revprop -r 30292 svn:log "KSCM-313 - Get app in running state." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30313 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 30313 svn:date "2012-04-25T20:35:05.044991Z" $REPO_URL
$SVN_CMD --revprop -r 30313 svn:log "KSPROJPLAN-960: removed ks-rice, added ks-rice-sql and integrated
ks-rice-sql into impex processing" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30335 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 30335 svn:date "2012-04-26T15:19:33.983205Z" $REPO_URL
$SVN_CMD --revprop -r 30335 svn:log "KSPROJPLAN-955: manual impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30345 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 30345 svn:date "2012-04-26T21:14:00.753182Z" $REPO_URL
$SVN_CMD --revprop -r 30345 svn:log "KSPROJPLAN-955: added ks-web (only ks-embedded works for now, rest are
placeholders)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30470 svn:author "mivanov" $REPO_URL
$SVN_CMD --revprop -r 30470 svn:date "2012-04-30T23:49:01.866372Z" $REPO_URL
$SVN_CMD --revprop -r 30470 svn:log "added more GWT-related resources" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30583 svn:author "kirk.bridger" $REPO_URL
$SVN_CMD --revprop -r 30583 svn:date "2012-05-03T13:31:21.218477Z" $REPO_URL
$SVN_CMD --revprop -r 30583 svn:log "Adding new rollover page to begin splitting out the rollover audit and course schedule page

<AXCHECKIN>---- Check In Summary ----
[CHANGED]  Sitemap
[ADDED]    (Page)      07a_Rollover
--------------------------</AXCHECKIN>" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30585 svn:author "peggy" $REPO_URL
$SVN_CMD --revprop -r 30585 svn:date "2012-05-03T13:46:31.003098Z" $REPO_URL
$SVN_CMD --revprop -r 30585 svn:log "KSCM-583 CreditsOptions" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30680 svn:author "elmien.spoelstra" $REPO_URL
$SVN_CMD --revprop -r 30680 svn:date "2012-05-07T13:03:58.630830Z" $REPO_URL
$SVN_CMD --revprop -r 30680 svn:log "KSCM-608 - Running: org.kuali.student.r2.core.class1.organization.service.impl.TestOrganizationServiceImpl fails" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30837 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 30837 svn:date "2012-05-09T20:50:09.713476Z" $REPO_URL
$SVN_CMD --revprop -r 30837 svn:log "KSLAB-2595 ran scripts ingested doctypes and exported" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 30951 svn:author "dcrouse" $REPO_URL
$SVN_CMD --revprop -r 30951 svn:date "2012-05-11T12:57:28.060123Z" $REPO_URL
$SVN_CMD --revprop -r 30951 svn:log "Added Name.java, NameInfo.java and NameOwner.java as a base for I8N. JIRA ICAS-132" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31177 svn:author "haroon.rafique" $REPO_URL
$SVN_CMD --revprop -r 31177 svn:date "2012-05-16T13:38:07.312918Z" $REPO_URL
$SVN_CMD --revprop -r 31177 svn:log "KSPROJPLAN-921: updated code to r30956 from ks-1.3 branch" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31249 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 31249 svn:date "2012-05-17T12:24:16.523910Z" $REPO_URL
$SVN_CMD --revprop -r 31249 svn:log "KSCM-504 - Include TypeService from r2." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31261 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 31261 svn:date "2012-05-17T14:27:44.717765Z" $REPO_URL
$SVN_CMD --revprop -r 31261 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31308 svn:author "sw" $REPO_URL
$SVN_CMD --revprop -r 31308 svn:date "2012-05-18T09:05:23.367090Z" $REPO_URL
$SVN_CMD --revprop -r 31308 svn:log "KSCM-541 - Fix Org SearchManager implementation." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31466 svn:author "kirk.bridger" $REPO_URL
$SVN_CMD --revprop -r 31466 svn:date "2012-05-23T05:49:26.002870Z" $REPO_URL
$SVN_CMD --revprop -r 31466 svn:log "Shared Project Admin_Course_Reg first check in" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31491 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 31491 svn:date "2012-05-23T13:38:26.858934Z" $REPO_URL
$SVN_CMD --revprop -r 31491 svn:log "Jira KSCM-561: Merge CM specific code from ks-1.3.
Also fixed:
KSCM-645
KSCM-648
KSCM-649
Also corrected errors on ks-lum-api's DTO classes: Empty method were removed. " $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31510 svn:author "kbatiste" $REPO_URL
$SVN_CMD --revprop -r 31510 svn:date "2012-05-23T18:49:38.436525Z" $REPO_URL
$SVN_CMD --revprop -r 31510 svn:log "Shared Project KRMS_Phase_1 first check in" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31587 svn:author "kirk.bridger" $REPO_URL
$SVN_CMD --revprop -r 31587 svn:date "2012-05-24T16:54:30.229259Z" $REPO_URL
$SVN_CMD --revprop -r 31587 svn:log "Upgraded Shared Project to 6.5.0.3004" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31771 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 31771 svn:date "2012-05-30T08:32:50.823507Z" $REPO_URL
$SVN_CMD --revprop -r 31771 svn:log "ksenroll-1246 - defining beans for the term resolvers" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31787 svn:author "pmarais" $REPO_URL
$SVN_CMD --revprop -r 31787 svn:date "2012-05-30T15:26:10.103446Z" $REPO_URL
$SVN_CMD --revprop -r 31787 svn:log "KSCM-664: Merge code from ks-1.2 (trunk). Merge all changes from 28 March 2012 until today (30 May 2012). I just excluded the parent change in the root pom. (More details about that in the Jira)" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 31814 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 31814 svn:date "2012-05-30T21:06:23.549525Z" $REPO_URL
$SVN_CMD --revprop -r 31814 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32108 svn:author "michael.ocleirigh" $REPO_URL
$SVN_CMD --revprop -r 32108 svn:date "2012-06-07T18:28:41.551523Z" $REPO_URL
$SVN_CMD --revprop -r 32108 svn:log "KSENROLL-1381 Sync Merge ks-1.3 into ks-1.3-services" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32204 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 32204 svn:date "2012-06-11T11:51:03.116999Z" $REPO_URL
$SVN_CMD --revprop -r 32204 svn:log "KSENROLL-1352 Create Test Template for execution of KRMS agendas in KS" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32226 svn:author "sudduth" $REPO_URL
$SVN_CMD --revprop -r 32226 svn:date "2012-06-11T17:32:21.781503Z" $REPO_URL
$SVN_CMD --revprop -r 32226 svn:log "Updated from Rice 2.0.0-rc1 to 2.0.1 and created myplan-standalone project." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32285 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 32285 svn:date "2012-06-12T21:02:57.826218Z" $REPO_URL
$SVN_CMD --revprop -r 32285 svn:log "KSENROLL-1373 updated some of the data so rollover works" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32288 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 32288 svn:date "2012-06-12T21:19:47.258722Z" $REPO_URL
$SVN_CMD --revprop -r 32288 svn:log "Automated Impex update" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32360 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32360 svn:date "2012-06-14T02:17:27.684320Z" $REPO_URL
$SVN_CMD --revprop -r 32360 svn:log "Fix ATP types in KSAP_ATP and fix other places that use the ATP_ID as FK." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32361 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32361 svn:date "2012-06-14T02:18:04.149409Z" $REPO_URL
$SVN_CMD --revprop -r 32361 svn:log "Fix ATP types in KSAP_ATP and fix other places that use the ATP_ID as FK." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32362 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32362 svn:date "2012-06-14T02:18:59.754665Z" $REPO_URL
$SVN_CMD --revprop -r 32362 svn:log "Fix ATP types in KSAP_ATP and fix other places that use the ATP_ID as FK." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32365 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32365 svn:date "2012-06-14T02:21:12.299933Z" $REPO_URL
$SVN_CMD --revprop -r 32365 svn:log "Fix ATP types in KSAP_ATP and fix other places that use the ATP_ID as FK." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32373 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32373 svn:date "2012-06-14T02:33:07.703632Z" $REPO_URL
$SVN_CMD --revprop -r 32373 svn:log "Add more courses, a few special test cases, fix up ATPs where needed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32375 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32375 svn:date "2012-06-14T02:34:24.772651Z" $REPO_URL
$SVN_CMD --revprop -r 32375 svn:log "Add more courses, a few special test cases, fix up ATPs where needed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32376 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32376 svn:date "2012-06-14T02:35:35.424111Z" $REPO_URL
$SVN_CMD --revprop -r 32376 svn:log "Add more courses, a few special test cases, fix up ATPs where needed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32377 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32377 svn:date "2012-06-14T02:36:34.191643Z" $REPO_URL
$SVN_CMD --revprop -r 32377 svn:log "Add more courses, a few special test cases, fix up ATPs where needed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32378 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32378 svn:date "2012-06-14T02:37:27.205726Z" $REPO_URL
$SVN_CMD --revprop -r 32378 svn:log "Add more courses, a few special test cases, fix up ATPs where needed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32379 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32379 svn:date "2012-06-14T02:37:52.341518Z" $REPO_URL
$SVN_CMD --revprop -r 32379 svn:log "Add more courses, a few special test cases, fix up ATPs where needed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32380 svn:author "dbmc" $REPO_URL
$SVN_CMD --revprop -r 32380 svn:date "2012-06-14T02:38:14.530496Z" $REPO_URL
$SVN_CMD --revprop -r 32380 svn:log "Add more courses, a few special test cases, fix up ATPs where needed." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32498 svn:author "nwright" $REPO_URL
$SVN_CMD --revprop -r 32498 svn:date "2012-06-15T21:16:07.565003Z" $REPO_URL
$SVN_CMD --revprop -r 32498 svn:log "KSENROLL-1399
Wrote CRUD Unit tests against mock found more contract issues and fixed them moved mock to impl package moved over testing utilities to ks-1.3" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32524 svn:author "gtaylor" $REPO_URL
$SVN_CMD --revprop -r 32524 svn:date "2012-06-16T19:09:23.359245Z" $REPO_URL
$SVN_CMD --revprop -r 32524 svn:log "KSENROLL-1399: Renamed files so KS could compile." $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32556 svn:author "depstein" $REPO_URL
$SVN_CMD --revprop -r 32556 svn:date "2012-06-18T19:39:14.586573Z" $REPO_URL
$SVN_CMD --revprop -r 32556 svn:log "KSENROLL-1373 refreshed numbered files from ksenroll" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32587 svn:author "kirk.bridger" $REPO_URL
$SVN_CMD --revprop -r 32587 svn:date "2012-06-19T17:38:41.088722Z" $REPO_URL
$SVN_CMD --revprop -r 32587 svn:log "Shared Project RegGroups_SeatPool first check in" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32589 svn:author "kirk.bridger" $REPO_URL
$SVN_CMD --revprop -r 32589 svn:date "2012-06-19T17:41:52.177942Z" $REPO_URL
$SVN_CMD --revprop -r 32589 svn:log "Shared Project RegGroups&SeatPools first check in" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32689 svn:author "nina" $REPO_URL
$SVN_CMD --revprop -r 32689 svn:date "2012-06-22T01:33:16.754332Z" $REPO_URL
$SVN_CMD --revprop -r 32689 svn:log "KSENROLL-1246 Create Term Resolvers for KS KRMS CM" $REPO_URL

#
#Generated
$SVN_CMD --revprop -r 32732 svn:author "jcaddel" $REPO_URL
$SVN_CMD --revprop -r 32732 svn:date "2012-06-22T20:29:31.871122Z" $REPO_URL
$SVN_CMD --revprop -r 32732 svn:log "Automated Impex update" $REPO_URL
