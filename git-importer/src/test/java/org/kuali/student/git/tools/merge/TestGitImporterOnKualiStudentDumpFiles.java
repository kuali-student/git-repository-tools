/*
 * Copyright 2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.git.tools.merge;

import java.io.File;
import java.io.IOException;
import java.util.Date;



import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.importer.GitImporterMain;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.utils.GitImporterDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestGitImporterOnKualiStudentDumpFiles {
	
	private static final Logger log = LoggerFactory.getLogger(TestGitImporterOnKualiStudentDumpFiles.class);

	/**
	 * 
	 */
	public TestGitImporterOnKualiStudentDumpFiles() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testDateConversion() {
	
		String dateString = "2008-03-26T14:40:00.680311Z";
		
		Date d = GitImporterDateUtils.convertDateString(dateString);
		
		assertDateEquals (d, 2008, 03, 26, 14, 40);
		
		Assert.assertEquals("EDT", GitImporterDateUtils.getTimeZoneShortFormName(d));
		
		dateString = "2011-02-21T14:55:36.976704Z";
		
		 d = GitImporterDateUtils.convertDateString(dateString);
			
		assertDateEquals (d, 2011, 02, 21, 14, 55);
		
		Assert.assertEquals("EST", GitImporterDateUtils.getTimeZoneShortFormName(d));
		
		dateString = "2011-02-21T15:59:54.387299Z";
		
		d = GitImporterDateUtils.convertDateString(dateString);
			
		assertDateEquals (d, 2011, 02, 21, 15, 59);
		
		Assert.assertEquals("EST", GitImporterDateUtils.getTimeZoneShortFormName(d));

		dateString = "2014-04-01T18:04:49.833911Z";
		
		d = GitImporterDateUtils.convertDateString(dateString);
		
		assertDateEquals (d, 2014, 04, 01, 18, 04);
		
		Assert.assertEquals("EDT", GitImporterDateUtils.getTimeZoneShortFormName(d));
		
		log.info("");
	}
	
	private void assertDateEquals(Date d, int year, int month, int dayOfMonth, int hour, int minute) {

		LocalDateTime ldt = new LocalDateTime(d);
		
		Assert.assertEquals(year, ldt.getYear());
		
		Assert.assertEquals(month, ldt.getMonthOfYear());
		
		Assert.assertEquals(dayOfMonth, ldt.getDayOfMonth());
		
		Assert.assertEquals(hour, ldt.getHourOfDay());
		
		Assert.assertEquals(minute, ldt.getMinuteOfHour());
		
	}

	@Test
	@Ignore
	public void testR1toR989() throws IOException {
		
		File gitRepository = new File ("target/git-repo-ks");
		
		FileUtils.deleteDirectory(gitRepository);
		
		GitRepositoryUtils
				.buildFileRepository(gitRepository, true);
		
		GitImporterMain.main(new String [] {"src/test/resources/r0%3A15128.dump.snappy", gitRepository.getAbsolutePath(), "target/git-repo-ks-veto.log", "target/git-repo-ks-copyFrom-skipped.log", "target/blob.log", "https://svn.kuali.org/repos/student"});
		
	}

}
