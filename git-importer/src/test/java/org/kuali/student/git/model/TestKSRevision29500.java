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
package org.kuali.student.git.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.importer.GitImporterMain;

/**
 * 
 * Test the importer on KS Revision 29500 to make sure the svn:mergeinfo properly is being indexed properly.
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestKSRevision29500 {

	/**
	 * @param name
	 */
	public TestKSRevision29500() {
	}

	@Test
	public void testRevision29500 () throws IOException {
		File gitRepository = new File ("target/ks-r29500");
		
		FileUtils.deleteDirectory(gitRepository);
		
		GitRepositoryUtils
				.buildFileRepository(gitRepository, true);
		
		GitImporterMain.main(new String [] {"src/test/resources/ks-r29500.dump.bz2", gitRepository.getAbsolutePath(), "target/ks-r29500-ks-veto.log", "target/ks-r29500-ks-copyFrom-skipped.log", "target/ks-r29500-blob.log", "0", "https://svn.kuali.org/repos/student", "uuid", "kuali.org"});
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(new BZip2CompressorInputStream(new FileInputStream("target/ks-r29500/jsvn/merge.map"))));
		
		String line1 = reader.readLine();
		
		Assert.assertNotNull(line1);
		
		Assert.assertEquals(true, line1.startsWith("29500::branches/ks-1.3::branches/ks-1.3-ec1::"));
		
		String line2 = reader.readLine();
		
		Assert.assertNotNull(line2);
		
		Assert.assertEquals(true, line2.startsWith("29500::branches/ks-1.3::branches/ks-1.3-merge:"));
		
		reader.close();
		
	}
	
}
