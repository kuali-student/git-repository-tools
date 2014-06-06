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
package org.kuali.student.git.importer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NoMergeBaseException;
import org.eclipse.jgit.errors.NoMergeBaseException.MergeBaseFailureReason;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.AsyncObjectSizeQueue;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class ReportBlobSizePerBranch {

	private static final Logger log = LoggerFactory.getLogger(ReportBlobSizePerBranch.class);
	
	/**
	 * 
	 */
	public ReportBlobSizePerBranch() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 3 && args.length != 4) {
			System.err.println("USAGE: <git repository> <bare: 0 or 1> <output file name>[<refs prefix: default to refs/heads >]");
			System.exit(-1);
		}
		
		String gitRepositoryPath = args[0];
		
		String bareString = args[1].trim();
		
		boolean bare = false;
		
		if (bareString.equals("1"))
			bare = true;
		
		String refPrefix = Constants.R_HEADS;
		
		String outputFileName = args[2].trim();
		
		if (args.length == 4)
			refPrefix = args[3].trim();
		
		try {
			
			PrintWriter outputWriter = new PrintWriter (outputFileName);
			Repository repo = GitRepositoryUtils.buildFileRepository(new File (gitRepositoryPath).getAbsoluteFile(), false, bare);
			
			Map<String, Ref> branchHeads = repo.getRefDatabase().getRefs(refPrefix);
			
			ObjectReader objectReader = repo.newObjectReader();
			
			RevWalk rw = new RevWalk(objectReader);
			
			TreeWalk tw = new TreeWalk(objectReader);
			
			tw.setRecursive(true);
			
			String header = String.format("Branch Name :: Total Commits in Graph :: Total Blob Size in Bytes :: Total Blob Size in Mega Bytes :: Total Blob Size in Giga Bytes");
			
			System.out.println(header);
			
			outputWriter.println(header);
			
			for (Map.Entry<String, Ref> entry : branchHeads.entrySet()) {
				
				String branchName = entry.getKey();
				Ref branchRef = entry.getValue();

				Set<ObjectId>blobIds = new HashSet<>();
				
				RevCommit commit = rw.parseCommit(branchRef.getObjectId());

				RevWalk commitHistoryWalk = new RevWalk(objectReader);
				
				commitHistoryWalk.markStart(commit);
				
				processCommit(commit, tw, blobIds);
				
				int totalReachableCommits = 0;
				
				while ((commit = commitHistoryWalk.next()) != null) {
					
					processCommit(commit, tw, blobIds);
					
					totalReachableCommits++;
				}
				
				long totalSize = 0L;
				
				AsyncObjectSizeQueue<ObjectId> sq = objectReader.getObjectSize(
						blobIds, true);

				while (sq.next())
					totalSize += sq.getSize();
				
				BigDecimal totalCounter = new BigDecimal(totalSize);
				
				String output = String.format("%s::%d::%s::%s::%s", branchName, totalReachableCommits, totalCounter.toString(), getMB(totalCounter).toString(), getGB (totalCounter).toString());
				
				System.out.println(output);
				outputWriter.println(output);
				
				commitHistoryWalk.release();
				
				
				
			}
			
			tw.release();
			rw.release();
			objectReader.release();
			
			outputWriter.close();
			
		} catch (Exception e) {
			
			log.error ("unexpected exception", e);
			
		}
		
		
	}

	private static BigDecimal getGB(BigDecimal counter) {
		
		return counter.divide(new BigDecimal(FileUtils.ONE_GB));
		
	}

	private static void processCommit(RevCommit commit, TreeWalk tw, Set<ObjectId>blobIdSet) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		tw.reset(commit.getTree().getId());
		
		while (tw.next()) {
			
			if (!tw.getFileMode(0).equals(FileMode.REGULAR_FILE)) 
				continue;

			ObjectId blobId = tw.getObjectId(0);
			
			blobIdSet.add(blobId);
			
		}
		
		
	}
	
	/*
	 * Copied from JGit's MergeCommand this will find the merge base between two commits.
	 * 
	 * Because the size of the graph below the merge base is shared between the commits.
	 * 
	 * 
	 */
	private static RevCommit getMergeBaseCommit(RevWalk walk, RevCommit a, RevCommit b)
			throws IncorrectObjectTypeException, IOException {
		walk.reset();
		walk.setRevFilter(RevFilter.MERGE_BASE);
		walk.markStart(a);
		walk.markStart(b);
		final RevCommit base = walk.next();
		if (base == null)
			return null;
		final RevCommit base2 = walk.next();
		if (base2 != null) {
			throw new NoMergeBaseException(
					MergeBaseFailureReason.MULTIPLE_MERGE_BASES_NOT_SUPPORTED,
					MessageFormat.format(
					JGitText.get().multipleMergeBasesFor, a.name(), b.name(),
					base.name(), base2.name()));
		}
		return base;
	}

	private static BigDecimal getMB(BigDecimal counter) {
		
		return counter.divide(new BigDecimal(FileUtils.ONE_MB));
		
	}

}
