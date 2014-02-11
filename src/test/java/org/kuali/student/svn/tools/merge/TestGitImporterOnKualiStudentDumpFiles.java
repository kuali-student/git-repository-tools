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
package org.kuali.student.svn.tools.merge;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.kuali.student.git.importer.GitImporterMain;
import org.kuali.student.git.tools.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
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
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		
		DateTime dt = formatter.parseDateTime(dateString);

		LocalDateTime ldt = new LocalDateTime (dt);
	
		Date d = ldt.toDate();
		
		
		log.info("");
	}
	
	@Test
	public void testR1toR989() throws IOException {
		
		File gitRepository = new File ("target/git-repo-ks");
		
		FileUtils.deleteDirectory(gitRepository);
		
		GitRepositoryUtils
				.buildFileRepository(gitRepository, true);
		
		GitImporterMain.main(new String [] {"src/test/resources/ks-r1-to-r989.dump", gitRepository.getAbsolutePath()});
		
	}

}
