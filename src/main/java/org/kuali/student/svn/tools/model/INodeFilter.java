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
package org.kuali.student.svn.tools.model;

import java.io.File;

import modifier.PathRevisionAndMD5AndSHA1;

/**
 * @author Kuali Student Team
 *
 */
public interface INodeFilter {
	
	/**
	 * Load the filter data for a specific join file
	 * @param joinDataFile
	 * @throws Exception 
	 */
	void loadFilterData (File joinDataFile) throws Exception;
	
	/**
	 * Extract the copy from data for the path and revision specified.
	 * 
	 * @param currentRevision
	 * @param path
	 * @return the copy from details if any that exist for the provided parameters.
	 */

	PathRevisionAndMD5AndSHA1 getCopyFromData(long currentRevision, String path);

}
