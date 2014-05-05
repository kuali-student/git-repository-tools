/**
 * 
 */
package org.kuali.student.svn.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.student.git.model.DummyGitTreeNodeInitializer;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 * 
 * Test's related to adding and removing data from GitTreeData's and also in relation to loading and changing data from prior commits.
 * 
 *
 */
public class TestGitTreeData extends AbstractGitRespositoryTestCase {

	private static final Logger log = LoggerFactory.getLogger(TestGitTreeData.class);
	
	/**
	 * @param name
	 */
	public TestGitTreeData() {
		super("git-tree-data");
	}

	
	/* (non-Javadoc)
	 * @see org.kuali.student.svn.model.AbstractGitRespositoryTestCase#onAfter()
	 */
	@Override
	protected void onAfter() throws Exception {
	}


	/* (non-Javadoc)
	 * @see org.kuali.student.svn.model.AbstractGitRespositoryTestCase#onBefore()
	 */
	@Override
	protected void onBefore() throws Exception {
		
	}
	
	@Test
	public void testDirtyChecks() throws IOException {
		
		/*
		 * Verify that dirty checking isn't causing dataloss
		 */
		
		
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		GitTreeData branch = new GitTreeData(new DummyGitTreeNodeInitializer());

		List<String> lines = FileUtils.readLines(new File ("src/test/resources/ks-r21-expected-files.txt"));
		
		for (String line : lines) {
			
			String parts[] = line.split ("\t");
			
			String path = parts[1];
			
			storeFile(inserter, branch, path, Double.valueOf(Math.random()).toString());
		}
		
		ObjectId commitId = super.commit(inserter, branch, "commit");
		
		createBranch(commitId, "branch");
		
		inserter.flush();
		
		
		GitTreeProcessor treeProcessor = new GitTreeProcessor(repo);
		
		GitTreeData nextCommitBase = treeProcessor.extractExistingTreeDataFromCommit(commitId);
		
		nextCommitBase.resetDirtyFlag();
		
		storeFile(inserter, nextCommitBase, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeDefinition.java", Double.valueOf(Math.random()).toString());
		
		ObjectId blobId = nextCommitBase.find(repo, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeDefinition.java");
		
		Assert.assertNotNull(blobId);
		
		ObjectId treeId = nextCommitBase.find(repo, "src/main/java/org/kuali/student/poc/xsd/personidentity/person");
		
		Assert.assertNotNull(treeId);
		
		TreeWalk tw = new TreeWalk(repo);
		
		tw.addTree(treeId);
		
		boolean expectDto = true;
		
		List<String>expectedBlobNames = Arrays.asList(new String [] {"AttributeDataTypeDTO.java", "AttributeDefinitionDTO.java", "AttributeSetDTO.java", "PersonCreateInfo.java", 
				"PersonDTO.java", "PersonDisplayInfo.java", "PersonIdDTO.java", "PersonInfo.java", "PersonInfoDTO.java", "PersonTypeDTO.java", "PersonTypeInfoDTO.java", "PersonUpdateInfo.java"});
		
		List<String>actualBlobNames = new ArrayList<>();
		
		while (tw.next()) {
		
			if (expectDto) {
				
				Assert.assertEquals(FileMode.TREE, tw.getFileMode(0));
				
				Assert.assertEquals("dto", tw.getNameString());
			
				tw.enterSubtree();
				
				expectDto = false;
			}
			else {
				
				Assert.assertEquals(FileMode.REGULAR_FILE, tw.getFileMode(0));
				
				actualBlobNames.add(tw.getNameString());
				
			}
		}
		
		tw.release();

		Assert.assertArrayEquals(expectedBlobNames.toArray(), actualBlobNames.toArray());
		
		storeFile(inserter, nextCommitBase, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeSetDefinition.java", Double.valueOf(Math.random()).toString());
		
		storeFile(inserter, nextCommitBase, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/PersonCitizenship.java", Double.valueOf(Math.random()).toString());

		storeFile(inserter, nextCommitBase, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/PersonInfo.java", Double.valueOf(Math.random()).toString());
		
		storeFile(inserter, nextCommitBase, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/PersonName.java", Double.valueOf(Math.random()).toString());
		
		storeFile(inserter, nextCommitBase, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/PersonReferenceIdName.java", Double.valueOf(Math.random()).toString());
		
		storeFile(inserter, nextCommitBase, "src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/PersonType.java", Double.valueOf(Math.random()).toString());
		
		nextCommitBase.deletePath("src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeDataTypeDTO.java");
		
		nextCommitBase.deletePath("src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/AttributeDefinitionDTO.java");
		
		nextCommitBase.deletePath("src/main/java/org/kuali/student/poc/xsd/personidentity/person/dto/PersonTypeDTO.java");
		
		
		ObjectId nextCommitId = super.commit(inserter, nextCommitBase, "commit");
		
		RevCommit originalCommit = rw.parseCommit(commitId);
		
		RevCommit currentCommit = rw.parseCommit(nextCommitId);
		
		String result = super.diffTrees(originalCommit.getTree().getId(), currentCommit.getTree().getId());
		
		log.info(result);
		
		Assert.assertEquals(true, result.trim().length() > 0);
		
		
		
	}


}
