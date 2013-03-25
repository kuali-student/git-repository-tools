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
package org.kuali.student.svn.tools;

import java.io.FileInputStream;
import java.util.Map;

import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;

/**
 * @author Kuali Student Team
 *
 */
public abstract class AbstractParseOptions implements IParseOptions {

	protected FileInputStream inputStream;

	/**
	 * 
	 */
	public AbstractParseOptions() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#setFileInputStream(java.io.FileInputStream)
	 */
	public void setFileInputStream(FileInputStream inputStream) {
		this.inputStream = inputStream;
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onStreamEnd(org.kuali.student.svn.tools.model.ReadLineData)
	 */
	public void onStreamEnd(ReadLineData lineData) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onDumpFormatVersion(org.kuali.student.svn.tools.model.ReadLineData)
	 */
	public void onDumpFormatVersion(ReadLineData lineData) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onUUID(org.kuali.student.svn.tools.model.ReadLineData)
	 */
	public void onUUID(ReadLineData lineData) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onRevision(long, org.kuali.student.svn.tools.model.ReadLineData)
	 */
	public void onRevision(long currentRevision, ReadLineData lineData) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onRevisionPropContentLength(long, long, org.kuali.student.svn.tools.model.ReadLineData)
	 */
	public void onRevisionPropContentLength(long currentRevision,
			long propContentLength, ReadLineData lineData) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onRevisionContentLength(long, long, org.kuali.student.svn.tools.model.ReadLineData)
	 */
	public void onRevisionContentLength(long currentRevision,
			long contentLength, ReadLineData lineData) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onNode(org.kuali.student.svn.tools.model.ReadLineData, java.lang.String)
	 */
	public void onNode(ReadLineData lineData, String path) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onAfterNode(long, java.lang.String, java.util.Map, org.kuali.student.svn.tools.model.INodeFilter)
	 */
	public void onAfterNode(long currentRevision, String path,
			Map<String, String> nodeProperties, INodeFilter nodeFilter) {
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.IParseOptions#onNodeContentLength(long, java.lang.String, long, java.util.Map, org.kuali.student.svn.tools.model.INodeFilter)
	 */
	public void onNodeContentLength(long currentRevision, String path,
			long contentLength, Map<String, String> nodeProperties,
			INodeFilter nodeFilter) {
		
	}
	
	

	
	
	
	

	

}
