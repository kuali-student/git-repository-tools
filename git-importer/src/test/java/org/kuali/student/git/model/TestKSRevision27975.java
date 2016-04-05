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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.importer.GitImporterMain;

/**
 * 
 * Test the importer on KS Revision 27975 to make sure the blob add is working properly
 * 
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestKSRevision27975 {

	/**
	 * @param name
	 */
	public TestKSRevision27975() {
	}

	@Test
	public void testRevision27975 () throws IOException {
		File gitRepository = new File ("target/ks-r27975");
		
		FileUtils.deleteDirectory(gitRepository);
		
		Repository repository = GitRepositoryUtils
				.buildFileRepository(gitRepository, true);
		
		GitImporterMain.main(new String [] {"src/test/resources/ks-r27975.dump.bz2", gitRepository.getAbsolutePath(), "target/ks-r27975-ks-veto.log", "target/ks-r27975-ks-copyFrom-skipped.log", "target/ks-r27975-blob.log", "0", "https://svn.kuali.org/repos/student", "uuid", "kuali.org"});
		
		RevWalk rw = new RevWalk(repository);
		
		Ref branch = repository.getRef("sandbox_ks-1.3-core-slice-demo_modules_ks-pom_trunk");
		
		Assert.assertNotNull(branch);

		RevCommit headCommit = rw.parseCommit(branch.getObjectId());
		
		TreeWalk tw = new TreeWalk(repository);
		
		tw.addTree(headCommit.getTree().getId());
		
		tw.setRecursive(true);
		
		int blobCount = 0;
		
		ObjectId blobId = null;
		
		while (tw.next()) {
			
			if (tw.getFileMode(0) == FileMode.REGULAR_FILE) {
				blobCount++;
				
				blobId = tw.getObjectId(0);
			}
		}
		
		Assert.assertEquals(1, blobCount);
		
		ObjectLoader blobLoader = repository.getObjectDatabase().newReader().open(blobId);
		
		List<String> lines = IOUtils.readLines(blobLoader.openStream());
		
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", lines.get(0));
		
		tw.close();
		
		rw.close();
		repository.close();
		
		
	}
	
	
	
}
