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

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.git.model.CopyFromOperation.OperationType;
import org.kuali.student.git.model.GitTreeProcessor.GitTreeBlobVisitor;
import org.kuali.student.git.model.SvnRevisionMapper.SvnRevisionMapResults;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.git.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.kuali.student.svn.tools.merge.model.BranchData;

/**
 * @author Kuali Student Team
 *
 */
public class CopyFromTreeBlobVisitor implements GitTreeBlobVisitor {

	private String path;
	private BranchDetector branchDetector;
	private GitBranchData targetBranch;
	private OperationType type;
	private SvnRevisionMapResults copyFromRevisionMapResults;
	private long currentRevision;
	private PrintWriter vetoLog;
	private PrintWriter blobLog;
	private ILargeBranchNameProvider largeBranchNameProvider;
	private IGitBranchDataProvider branchDataProvider;
	

		public CopyFromTreeBlobVisitor(long currentRevision, String path, GitBranchData targetBranch,
			OperationType type, SvnRevisionMapResults copyFromRevisionMapResults, ILargeBranchNameProvider largeBranchNameProvider, BranchDetector branchDetector, IGitBranchDataProvider branchDataProvider, PrintWriter vetoLog, PrintWriter blobLog) {
				this.currentRevision = currentRevision;
				this.path = path;
				this.targetBranch = targetBranch;
				this.type = type;
				this.copyFromRevisionMapResults = copyFromRevisionMapResults;
				this.largeBranchNameProvider = largeBranchNameProvider;
				this.branchDetector = branchDetector;
				this.branchDataProvider = branchDataProvider;
				this.vetoLog = vetoLog;
				this.blobLog = blobLog;
				
	}

		@Override
		public boolean visitBlob(ObjectId blobId,
				String blobPath, String name) throws MissingObjectException, IncorrectObjectTypeException, IOException {

			String alteredBlobPath = null;
			
			try {

				
				String adjustedCopyFromBranchPath = copyFromRevisionMapResults.getRevMap().getBranchPath().substring(Constants.R_HEADS.length());
				
				String copyFromBranchSubPath = copyFromRevisionMapResults.getSubPath();
				
				if (copyFromBranchSubPath.length() > 0) {
					adjustedCopyFromBranchPath = adjustedCopyFromBranchPath + "/" + copyFromBranchSubPath;
				}
				
				alteredBlobPath = GitBranchUtils
						.convertToTargetPath(path,
								copyFromRevisionMapResults.getRevMap().getRevision(), adjustedCopyFromBranchPath,
								blobPath, branchDetector);

				/*
				 * In most cases this blob path will live in the
				 * branch identified as data.
				 * 
				 * But in some cases the branch could be
				 * different. i.e. the full blob path shows a
				 * different branch.
				 * 
				 * Detect the branch path and if different from
				 * data then record this blob separately.
				 */

				BranchData alteredData = null;
				try {

					alteredData = branchDetector.parseBranch(
							currentRevision, alteredBlobPath);

				} catch (VetoBranchException e1) {
					vetoLog.print("vetoed alteredBlobPath = "
							+ alteredBlobPath);
					// even though this blob failed we still want to look at the others in case any can be applied.
					return true;
				}

				if (alteredData.getBranchPath().equals(
						targetBranch.getBranchPath())) {
					
					// same branch
					targetBranch.addBlob(alteredBlobPath,
							blobId.getName(), blobLog);
				} else {
					// a different branch
					GitBranchData alteredBranchData = branchDataProvider.getBranchData(
							GitBranchUtils.getCanonicalBranchName(
									alteredData.getBranchPath(),
									currentRevision,
									largeBranchNameProvider),
							currentRevision);

					alteredBranchData.addBlob(alteredBlobPath,
							blobId.getName(), blobLog);

				}

			} catch (VetoBranchException e) {
				vetoLog.println(String
						.format("tree walk add blob vetoed. CurrentRevision: %s, Current Branch Name: %s, Blob Path: %s",
								String.valueOf(currentRevision),
								targetBranch.getBranchName(), alteredBlobPath));
				// intentionally continue

			}

			// visit all of the blobs
			return true;
		}

}
