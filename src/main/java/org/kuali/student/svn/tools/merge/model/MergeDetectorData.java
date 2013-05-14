/*
 * Copyright 2013 The Kuali Foundation
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
package org.kuali.student.svn.tools.merge.model;

import java.io.PrintWriter;

/**
 * @author Kuali Student Team
 *
 */
public interface MergeDetectorData {
	
	/**
	 * Store the copyFrom details for a single path in the current revision.
	 * 
	 * @param copyFromRevision
	 * @param copyFromPath
	 * @param copyFromMD5
	 * @param currentRevision
	 * @param currentPath
	 */
	public void storePath(Long copyFromRevision, String copyFromPath,
			String copyFromMD5, Long currentRevision, String currentPath);

	/**
	 * At the end of the revision process all of the stored paths to compute where the merge points are.
	 * @param currentRevision
	 */
	public void processRevision(PrintWriter outputWriter, Long currentRevision);

	/**
	 * Store the svn:mergeinfo revision property vaqlue for the path and revision given.
	 * 
	 * @param currentRevision
	 * @param path
	 * @param svnMergeInfo
	 */
	public void storeSvnMergeInfo(long currentRevision, String path,
			String svnMergeInfo);

}
