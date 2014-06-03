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

import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 *
 */
public class RepositoryCleanerImpl implements RepositoryCleaner {

	private static final Logger log = LoggerFactory.getLogger(RepositoryCleanerImpl.class);
	
	/**
	 * 
	 */
	public RepositoryCleanerImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.RepositoryCleaner#execute()
	 */
	@Override
	public void execute(Repository repo) {
		
	}
	
	

}
