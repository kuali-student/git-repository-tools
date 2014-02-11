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
package org.kuali.student.git.utils;

import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.kuali.student.svn.tools.merge.tools.BranchUtils;
import org.kuali.student.svn.tools.merge.tools.BranchUtils.IBranchTagAssist;

/**
 * @author Kuali Student Team
 *
 */
public class GitBranchUtils {

	/**
	 * 
	 */
	public GitBranchUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static BranchData parse (String path) throws VetoBranchException {
		return BranchUtils.parse(0L, path, new IBranchTagAssist () {

			@Override
			public BranchData parseBranch(Long revision, String path,
					String[] parts) throws VetoBranchException {

				if (! (path.contains("tags") || path.contains("branches") || path.contains("trunk")))
						throw new VetoBranchException("Path does not contain tags, branches or trunk");
				
				return null;
			}} );
	}

}
