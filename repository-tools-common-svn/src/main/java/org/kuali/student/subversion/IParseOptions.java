/*
 *  Copyright 2013 The Kuali Foundation Licensed under the
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
package org.kuali.student.subversion;

import java.io.InputStream;
import java.util.Map;

import org.kuali.student.common.io.ReadLineData;
import org.kuali.student.subversion.model.INodeFilter;

/**
 * @author Kuali Student Team
 *
 */
public interface IParseOptions {

	void setFileInputStream(InputStream inputStream);
	
	void onStreamEnd(ReadLineData lineData);

	void onDumpFormatVersion(ReadLineData lineData);

	void onUUID(ReadLineData lineData);

	void onRevision(long currentRevision, ReadLineData lineData);

	void onRevisionContentLength(long currentRevision, long contentLength, long propContentLength,
			ReadLineData lineData);

	void onNode(ReadLineData lineData, String path);

	void onAfterNode(long currentRevision, String path,
			Map<String, String> nodeProperties, INodeFilter nodeFilter);

	public void onNodeContentLength(long currentRevision, String path,
			long contentLength, long propContentLength, Map<String, String> nodeProperties,
			INodeFilter nodeFilter);

}
