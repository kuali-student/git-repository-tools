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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.importer.GitImporterMain;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMap;
import org.kuali.student.svn.model.ExternalModuleInfo;

/**
 * 
 * Test the importer on KS Revision 27974 to make sure the svn:mergeinfo properly is being indexed properly.
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestKSRevision27974 {

	/**
	 * @param name
	 */
	public TestKSRevision27974() {
	}

	@Test
	public void testRevision27974 () throws IOException {
		File gitRepository = new File ("target/ks-r27974");
		
		FileUtils.deleteDirectory(gitRepository);
		
		Repository repository = GitRepositoryUtils
				.buildFileRepository(gitRepository, true);
		
		GitImporterMain.main(new String [] {"src/test/resources/ks-r27974.dump.bz2", gitRepository.getAbsolutePath(), "target/ks-r27974-ks-veto.log", "target/ks-r27974-ks-copyFrom-skipped.log", "target/ks-r27974-blob.log", "0", "https://svn.kuali.org/repos/student", "uuid"});
	
		// get the fusion-maven-plugin.dat file and check its contents are what we expect.
		
		SvnRevisionMapper revisionMapper = new SvnRevisionMapper(repository);
		
		revisionMapper.initialize();
		
		List<SvnRevisionMap> heads = revisionMapper.getRevisionHeads(27974L);
		
		Assert.assertNotNull(heads);

		Assert.assertEquals(1, heads.size());
		
		SvnRevisionMap revMap = heads.get(0);
		
		ObjectId branchHead = ObjectId.fromString(revMap.getCommitId());
		
		RevWalk rw = new RevWalk(repository);
		
		RevCommit commit = rw.parseCommit(branchHead);
				
		TreeWalk tw = new TreeWalk(repository);
		
		tw.addTree(commit.getTree().getId());
		
		Assert.assertTrue("should have been one file", tw.next());
		
		Assert.assertEquals(FileMode.REGULAR_FILE, tw.getFileMode(0));
		
		ObjectId blobId = tw.getObjectId(0);
		
		ObjectLoader loader = repository.newObjectReader().open(blobId, Constants.OBJ_BLOB);
		
		List<String> lines = IOUtils.readLines(loader.openStream(), "UTF-8");
		
		Assert.assertEquals(2, lines.size());
		
		String firstLine = lines.get(0);
		
		Assert.assertEquals("# module = ks-api branch Path = sandbox/ks-1.3-core-slice-demo/modules/ks-api/trunk revision = 27974", firstLine);
				
		String secondLine = lines.get(1);
				
		Assert.assertEquals("ks-api::sandbox_ks-1.3-core-slice-demo_modules_ks-api_trunk::UNKNOWN", secondLine);
		
		tw.release();
				
		rw.release();
		
		revisionMapper.shutdown();
		
		
	}
	
	@Test
	public void testReorderingExternals() {
		
		GitBranchData A = new GitBranchData("A", 10, null, null);
		
		GitBranchData B = new GitBranchData("B", 10, null, null);
		
		B.setExternals(Arrays.asList(new ExternalModuleInfo [] {new ExternalModuleInfo("A", "A", 10)}));
		
		
		List<GitBranchData> unorderedList = Arrays.asList(new GitBranchData[] {B, A});
		
		List<GitBranchData> orderedList = ExternalsUtils.computeExternalsAwareOrdering(unorderedList);
		
		Assert.assertEquals(orderedList.size(), unorderedList.size());
		
		Assert.assertEquals("A", orderedList.get(0).getBranchPath()); 
		Assert.assertEquals("B", orderedList.get(1).getBranchPath());
		
		GitBranchData C = new GitBranchData("C", 10, null, null);
		
		unorderedList = Arrays.asList(new GitBranchData[] {B, C, A});
		
		// intentional typo on the path
		B.setExternals(Arrays.asList(new ExternalModuleInfo [] {new ExternalModuleInfo("A", "a", 10)}));
		
		orderedList = ExternalsUtils.computeExternalsAwareOrdering(unorderedList);
		
		Assert.assertEquals(orderedList.size(), unorderedList.size());
		
		Assert.assertEquals("B", orderedList.get(0).getBranchPath()); 
		Assert.assertEquals("C", orderedList.get(1).getBranchPath());
		Assert.assertEquals("A", orderedList.get(2).getBranchPath());
		
		unorderedList = Arrays.asList(new GitBranchData[] {B, C, A});
		
		B.setExternals(Arrays.asList(new ExternalModuleInfo [] {new ExternalModuleInfo("A", "A", 10)}));
		
		A.setExternals(Arrays.asList(new ExternalModuleInfo [] {new ExternalModuleInfo("C", "C", 10)}));
		
		orderedList = ExternalsUtils.computeExternalsAwareOrdering(unorderedList);
		
		Assert.assertEquals(orderedList.size(), unorderedList.size());
		
		Assert.assertEquals("C", orderedList.get(0).getBranchPath()); 
		Assert.assertEquals("A", orderedList.get(1).getBranchPath());
		Assert.assertEquals("B", orderedList.get(2).getBranchPath());
	}
	
}
