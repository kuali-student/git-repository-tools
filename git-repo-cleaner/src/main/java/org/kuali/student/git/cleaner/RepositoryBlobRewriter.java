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
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceiveCommand.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.ref.utils.GitRefUtils;
import org.kuali.student.git.model.tree.GitTreeData;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.kuali.student.git.utils.ExternalGitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Git Repositories are best kept under 1 GB for a variety of performance
 * reasons.
 * 
 * This tool can be used to split a repository into two parts.
 * 
 * @author ocleirig
 * 
 */
public class RepositoryBlobRewriter extends AbstractRepositoryCleaner {

	private static final Logger log = LoggerFactory
			.getLogger(RepositoryBlobRewriter.class);


	private Map<ObjectId, String> blobIdToReplacementContentMap = new HashMap<>();


	/**
	 * 
	 */
	public RepositoryBlobRewriter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.git.cleaner.RepositoryCleaner#validateArgs(java.lang
	 * .String[])
	 */
	@Override
	public void validateArgs(List<String> args) throws Exception {

		if (args.size() != 2 && args.size() != 4) {
			log.error("USAGE: <source git repository meta directory> <blob replacement input file> [<branchRefSpec> <git command path>]");
			log.error("\t<git repo meta directory> : the path to the meta directory of the source git repository");
			log.error("\t<blob replacement input file> : format: blob id <double colon ::> replacement message (single line)");
			log.error("\t<branchRefSpec> : git refspec from which to source the graph to be rewritten");
			log.error("\t<git command path> : the path to a native git ");
			throw new IllegalArgumentException("invalid arguments");
		}

		setRepo(GitRepositoryUtils.buildFileRepository(
				new File(args.get(0)).getAbsoluteFile(), false));

		List<String> lines = FileUtils.readLines(new File(args.get(1)));

		for (String line : lines) {

			String[] parts = line.split("::");

			ObjectId blobId = ObjectId.fromString(parts[0].trim());

			String replacementContent = parts[1].trim();

			this.blobIdToReplacementContentMap.put(blobId, replacementContent);
		}

		if (args.size() >= 3)
			setBranchRefSpec(args.get(2).trim());

		if (args.size() == 4)
			setExternalGitCommandPath(args.get(3).trim());
	}

	
	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#processCommitTree(org.eclipse.jgit.lib.ObjectId, org.kuali.student.git.model.tree.GitTreeData)
	 */
	@Override
	protected boolean processCommitTree(RevCommit commit, GitTreeData tree) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		
		boolean recreateCommit = false;
		
		for (Map.Entry<ObjectId, String> entry : this.blobIdToReplacementContentMap
				.entrySet()) {

			ObjectId blobId = entry.getKey();

			List<String> currentPaths = GitRepositoryUtils
					.findPathsForBlobInCommit(getRepo(), commit.getId(), blobId);

			if (currentPaths.size() > 0) {
				
				recreateCommit = true;
				
				ObjectId newBlobId = inserter.insert(Constants.OBJ_BLOB,
						entry.getValue().getBytes());

				for (String path : currentPaths) {

					tree.addBlob(path, newBlobId);
				}

				inserter.release();
			}

		}
		
		return recreateCommit;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.AbstractRepositoryCleaner#getFileNameSuffix()
	 */
	@Override
	protected String getFileNameSuffix() {
		return "blob-rewrite";
	}

	
}
