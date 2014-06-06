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

import java.io.IOException;
import java.util.Date;

import org.eclipse.jgit.lib.Repository;

/**
 * @author ocleirig
 *
 */
public interface RepositoryCleaner {

	/**
	 * Execute the split of the repository about the split date.
	 * 
	 * The right side will be decoupled from the left side.
	 * 
	 * 
	 * 
	 * @param repo the source repository
	 * @param splitDate commits before are on the left side, commits after are on the right side.
	 * @param externalGitCommandPath 
	 * @throws IOException 
	 * 
	 */
	void execute(Repository repo, String branchRefSpec, Date splitDate, String externalGitCommandPath)
			throws IOException;
	
}
