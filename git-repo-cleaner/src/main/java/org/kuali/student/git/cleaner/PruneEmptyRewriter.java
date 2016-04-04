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
package org.kuali.student.git.cleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.kuali.student.git.cleaner.model.SkipOverCommitException;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.tree.GitTreeData;

/**
 * Prune non merge commits that don't have any difference with their parent.
 * 
 */
public class PruneEmptyRewriter extends org.kuali.student.git.cleaner.AbstractRepositoryCleaner {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory
			.getLogger(PruneEmptyRewriter.class);

	private Map<ObjectId, ObjectId>prunedCommitIdToParentCommitMap = new LinkedHashMap<ObjectId, ObjectId>();
	
    /**
	 *
	 */
	public PruneEmptyRewriter() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.kuali.student.git.cleaner.RepositoryCleaner#validateArgs(java.lang
	 * .String[])
	 */
	@Override
	public void validateArgs(java.util.List<String> args) throws Exception {

		if (args.size() != 1 && args.size() != 2) {
			log.error("USAGE: <source git repository meta directory> [<git command path>]");
			log.error("\t<git repo meta directory> : the path to the meta directory of the source git repository");
			log.error("\t<git command path> : the path to a native git ");
			throw new IllegalArgumentException("invalid arguments");
		}

		setRepo(GitRepositoryUtils.buildFileRepository(
                new File(args.get(0)).getAbsoluteFile(), false));

		setBranchRefSpec(Constants.R_HEADS);

        if (args.size() == 2)
            setExternalGitCommandPath(args.get(1).trim());
        
	}

    @Override
    protected boolean processCommitTree(org.eclipse.jgit.revwalk.RevCommit commit, org.kuali.student.git.model.tree.GitTreeData tree) throws org.eclipse.jgit.errors.MissingObjectException, org.eclipse.jgit.errors.IncorrectObjectTypeException, org.eclipse.jgit.errors.CorruptObjectException, java.io.IOException, SkipOverCommitException {

    	/*
    	 * If the current commit has one parent and the tree id of the current commit is the same as its parent we can colapse it.
    	 * 
    	 * Unless it is pointed at by a branch; in that case keep the commit.
    	 */
    	
    	
        if (commit.getParentCount() == 1) {

        	ObjectId commitTreeId = commit.getTree().getId();
        	ObjectId parentTreeId = commit.getParent(0).getTree().getId();
        	
        	if (parentTreeId.equals(commitTreeId) && !super.commitToBranchMap.containsKey(commit.getId())) {
        		prunedCommitIdToParentCommitMap.put(commit.getId(), commit.getParent(0).getId());
        		throw new SkipOverCommitException();
        	}
        	else {
        		return true;
        	}
        }
        else
        	return true;
    	
    }
    
    private ObjectId getNonPrunedCommitId (ObjectId candidateCommitId) {

    	ObjectId currentCommitId = candidateCommitId;
    	
    	while (true) {
    		
    		ObjectId parent = prunedCommitIdToParentCommitMap.get(currentCommitId);
    		
    		if (parent == null)
    			return currentCommitId;
    		else
    			currentCommitId = parent;
    		
    	}
    	
    	
    }

    @Override
	protected Set<ObjectId> processParents(RevCommit commit) {
    	/*
    	 * Filter the parents so that we don't refer to commits that were skipped.  
    	 */
    	Set<ObjectId> parents = getParentCommitIds(commit);
    	
    	Set<ObjectId>nonPrunedParents = new LinkedHashSet<ObjectId>();
    	
    	for (ObjectId parentId : parents) {
			
    		ObjectId nonPrunedParentId = getNonPrunedCommitId(parentId);
    		
    		nonPrunedParents.add(nonPrunedParentId);
    		
		}
    	
		return super.processParents(nonPrunedParents);
	}

	

    /* (non-Javadoc)
                 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#getFileNameSuffix()
                 */
	@Override
	protected String getFileNameSuffix() {
		return "prune-empty";
	}

	
}
