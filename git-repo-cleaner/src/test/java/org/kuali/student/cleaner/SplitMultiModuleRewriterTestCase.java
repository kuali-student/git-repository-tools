/*
 *  Copyright 2014 The Kuali Foundation Licensed under the
 *	Educational Community License, Version 2.0 (the "License"); you may
 *	not use this file except in compliance with the License. You may
 *	obtain a copy of the License at
 *
 *	http://www.osedu.org/licenses/ECL-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the License for the specific language governing
 *	permissions and limitations under the License.
 */
package org.kuali.student.cleaner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.TreeRevFilter;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.junit.Test;
import org.kuali.student.git.cleaner.SplitMultiModuleJGitRewriter;
import org.kuali.student.git.cleaner.SplitMultiModuleRewriter;
import org.kuali.student.git.model.DummyGitTreeNodeInitializer;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.svn.model.AbstractGitRespositoryTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test case for developing a Comparator based RevSort that will resemble a topo sort but also handle the fusion lateral branch
 * dependencies.
 * 
 * @author ocleirig
 *
 */
public class SplitMultiModuleRewriterTestCase extends AbstractGitRespositoryTestCase {

	private static final Logger log = LoggerFactory.getLogger(SplitMultiModuleRewriterTestCase.class);

	/**
	 * @param name
	 */
	public SplitMultiModuleRewriterTestCase() {
		super("split-multi-module-rewriter", true);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.model.AbstractGitRespositoryTestCase#onBefore()
	 */
	@Override
	protected void onBefore() throws Exception {
		/*
		 * Setup base commits
		 * 
		 * base commit
		 * 
		 * aggregate trunk
		 * 
		 * and the different modules
		 * 
		 */
		
		ObjectInserter inserter = repo.newObjectInserter();
		
		// create trunk
		GitTreeData trunk = new GitTreeData(new DummyGitTreeNodeInitializer());
		
		storeFile (inserter, trunk, "readme.txt", "first");
		
		ObjectId commitId = commit (inserter, trunk, "initial commit should be skipped over");
		
		inserter.flush();
		
		assertSmallBlobContents(trunk, "readme.txt", "first");
		 
		GitRefUtils.createOrUpdateBranch(repo, Constants.R_HEADS + "trunk", commitId);

		storeFile (inserter, trunk, "src/modules/my-module/example.txt", "test");
		storeFile (inserter, trunk, "readme.txt", "test");
		
		commitId = commit (inserter, trunk, "add my-module commit", commitId);
		
		inserter.flush();

        assertSmallBlobContents(trunk, "readme.txt", "test");
		
		GitRefUtils.createOrUpdateBranch(repo, Constants.R_HEADS + "trunk", commitId);
		
		storeFile (inserter, trunk, "readme.txt", "changes to readme.txt");
		
		commitId = commit (inserter, trunk, "This commit should be pruned out from the final repo", commitId);

		inserter.flush();
		
		GitRefUtils.createOrUpdateBranch(repo, Constants.R_HEADS + "trunk", commitId);
		

		storeFile (inserter, trunk, "readme.txt", "changes to readme.txt");
		
		commitId = commit (inserter, trunk, "changes only to readme.txt", commitId);

		inserter.flush();
		
		GitRefUtils.createOrUpdateBranch(repo, Constants.R_HEADS + "trunk", commitId);

        org.eclipse.jgit.lib.ObjectId treeId = GitRepositoryUtils.findInCommit(repo, commitId, "src/modules/my-module");

        GitTreeData release = new GitTreeData(new DummyGitTreeNodeInitializer());

        release.setGitTreeObjectId(treeId);

        commitId = commit (inserter, release, "make release", commitId);

        inserter.flush();

        GitRefUtils.createOrUpdateBranch(repo, Constants.R_HEADS + "release", commitId);

        inserter.close();

        repo.getRefDatabase().refresh();


    }
	
	@Test
	public void testRevWalkGeneratorRewriter () throws MissingObjectException, IncorrectObjectTypeException, IOException {

		SplitMultiModuleJGitRewriter rewriter = new SplitMultiModuleJGitRewriter(repo.getDirectory().getAbsolutePath(), true, "src/modules/my-module");
		
		rewriter.execute();
		
		
	}
	
	@Test
	public void testSplitMultiModuleRewriterOnRepo() throws Exception {

        SplitMultiModuleRewriter rewriter = new SplitMultiModuleRewriter();

        List<String> args = new ArrayList<String>();
        // <source git repository meta directory> <path to collapse> <git command path>

        args.add(repo.getDirectory().getAbsolutePath());

        args.add("src/modules/my-module");

        rewriter.validateArgs(args);

        rewriter.execute();

        rewriter.close();
        
//        PruneEmptyRewriter pruner = new PruneEmptyRewriter();
//        
//        List<String>prunerArgs = new ArrayList<String>();
//        
//        prunerArgs.add(repo.getDirectory().getAbsolutePath());
//        
//        pruner.validateArgs(prunerArgs);
//        
//        pruner.execute();
//        
//        pruner.close();
        
		
	}

	

}
