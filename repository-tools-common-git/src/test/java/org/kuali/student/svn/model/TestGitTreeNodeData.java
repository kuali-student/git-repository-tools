/**
 * 
 */
package org.kuali.student.svn.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.GitTreeNodeData;
import org.kuali.student.git.model.tree.GitTreeNodeInitializerImpl;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;

/**
 * @author ocleirig
 * 
 * Test case against GitTreeNodeData objects.
 * 
 */
public class TestGitTreeNodeData extends AbstractGitRespositoryTestCase {

	private GitTreeProcessor treeProcessor;
	private GitTreeNodeInitializerImpl treeNodeInitializer;



	/**
	 * @param name
	 */
	public TestGitTreeNodeData() {
		super("git-tree-node-data", true);
	}

	
	
	/* (non-Javadoc)
	 * @see org.kuali.student.svn.model.AbstractGitRespositoryTestCase#onBefore()
	 */
	@Override
	protected void onBefore() throws Exception {
		super.onBefore();
		
		this.treeProcessor = new GitTreeProcessor(repo);
		
		this.treeNodeInitializer = new GitTreeNodeInitializerImpl(treeProcessor);
		
	}



	@Test
	public void testTopLevelBlobMerge() throws IOException {
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		GitTreeData ABTree = new GitTreeData(treeNodeInitializer);
		
		storeFile(inserter, ABTree, "A.txt", "test content");
		storeFile(inserter, ABTree, "B.txt", "test content");
		
		ObjectId ABtreeId = ABTree.buildTree(inserter);
		
		inserter.flush();
		
		GitTreeData CDTree = new GitTreeData(treeNodeInitializer);
		
		storeFile(inserter, CDTree, "C.txt", "test content");
		storeFile(inserter, CDTree, "D.txt", "test content");
		
		ObjectId CDTreeId = CDTree.buildTree(inserter);
		
		inserter.flush();
		
		GitTreeNodeData loadedAB = treeProcessor.extractExistingTreeData(ABtreeId, "test");
		
		GitTreeNodeData loadedCD = treeProcessor.extractExistingTreeData(CDTreeId, "test");
		
		loadedAB.merge(loadedCD);
		
		ObjectId ABDCTreeId = loadedAB.buildTree(inserter);
		
		inserter.flush();
		
		
		TreeWalk tw = new TreeWalk(repo);
		
		tw.setRecursive(false);
		
		tw.addTree(ABDCTreeId);
		
		int counter = 0;
		
		while (tw.next()) {
			counter++;
		}
		
		tw.release();
		
		/*
		 * Check that we have merged the two trees togather and there are now 4 files in the tree.
		 */
		Assert.assertEquals(4, counter);
		
		
	}
	
	@Test
	public void testTopLevelOverlappingBlobMerge() throws IOException {
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		GitTreeData ABTree = new GitTreeData(treeNodeInitializer);
		
		storeFile(inserter, ABTree, "A.txt", "test content");
		storeFile(inserter, ABTree, "B.txt", "AB Tree content");
		
		ObjectId ABtreeId = ABTree.buildTree(inserter);
		
		inserter.flush();
		
		GitTreeData BDTree = new GitTreeData(treeNodeInitializer);
		
		storeFile(inserter, BDTree, "B.txt", "BD Tree content");
		storeFile(inserter, BDTree, "D.txt", "test content");
		
		ObjectId BDTreeId = BDTree.buildTree(inserter);
		
		inserter.flush();
		
		GitTreeNodeData loadedAB = treeProcessor.extractExistingTreeData(ABtreeId, "test");
		
		GitTreeNodeData loadedBD = treeProcessor.extractExistingTreeData(BDTreeId, "test");
		
		loadedAB.merge(loadedBD);
		
		ObjectId ABDCTreeId = loadedAB.buildTree(inserter);
		
		inserter.flush();
		
		TreeWalk tw = new TreeWalk(repo);
		
		tw.setRecursive(false);
		
		tw.addTree(ABDCTreeId);
		
		int counter = 0;
		
		ObjectId blobId = null;
		
		while (tw.next()) {
			counter++;
			
			String name = tw.getNameString();
			
			if (name.equals("B.txt"))
				blobId = tw.getObjectId(0);
		}
		
		tw.release();
		
		/*
		 * Check that we have merged the two trees together and there are now 3 files in the tree.
		 */
		Assert.assertEquals(3, counter);
		
		// TODO check the file content is AB Tree Content
		assertBlobContents(blobId, 0, "AB Tree content");
		
	}
	
	@Test
	public void testTopAndNestedDirectoryMerge() throws IOException {
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		GitTreeData AFBTree = new GitTreeData(treeNodeInitializer);
		
		storeFile(inserter, AFBTree, "A.txt", "test content");
		
		storeFile(inserter, AFBTree, "F/B.txt", "test content");
		
		ObjectId AFBtreeId = AFBTree.buildTree(inserter);
		
		inserter.flush();
		
		GitTreeData BFDTree = new GitTreeData(treeNodeInitializer);
		
		storeFile(inserter, BFDTree, "B.txt", "BD Tree content");
		storeFile(inserter, BFDTree, "F/D.txt", "test content");
		
		ObjectId BFDTreeId = BFDTree.buildTree(inserter);
		
		inserter.flush();
		
		GitTreeNodeData loadedAFB = treeProcessor.extractExistingTreeData(AFBtreeId, "test");
		
		GitTreeNodeData loadedBFD = treeProcessor.extractExistingTreeData(BFDTreeId, "test");
		
		loadedAFB.merge(loadedBFD);
		
		ObjectId mergedTreeId = loadedAFB.buildTree(inserter);
		
		inserter.flush();
		
		TreeWalk tw = new TreeWalk(repo);
		
		tw.setRecursive(false);
		
		tw.addTree(mergedTreeId);
		
		int depth = 0;
		
		Set<String>depthZeroExpectedFiles = new HashSet<>();
		
		depthZeroExpectedFiles.add("A.txt");
		depthZeroExpectedFiles.add("B.txt");
		
		Set<String> depthFExpectedFiles = new HashSet<>();
		
		depthFExpectedFiles.add("B.txt");
		depthFExpectedFiles.add("D.txt");
		
		
		while (tw.next()) {
			
			String name = tw.getNameString();
			
			if (tw.getFileMode(0).equals(FileMode.TYPE_FILE)) {
				
				if (depth == 0) {
					
					Assert.assertEquals(true, depthZeroExpectedFiles.contains(name));
				}
				else {
					Assert.assertEquals(true, depthFExpectedFiles.contains(name));
				}
				
			}
			else {
				tw.enterSubtree();
				depth++;
			}
		}
		
		tw.release();
		
	}
	

}
