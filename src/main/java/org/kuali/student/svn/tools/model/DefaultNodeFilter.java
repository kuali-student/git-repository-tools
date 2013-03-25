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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import modifier.PathRevisionAndMD5AndSHA1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Kuali Student Team
 *
 */
public class DefaultNodeFilter implements INodeFilter, InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(DefaultNodeFilter.class);
	
	private Map<Long, Map<String, PathRevisionAndMD5AndSHA1>>revisionToPathToDataMap = new LinkedHashMap<Long, Map<String,PathRevisionAndMD5AndSHA1>>();
	
	/**
	 * 
	 */
	public DefaultNodeFilter() {
		// TODO Auto-generated constructor stub
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		
		// need to determine which revision's and paths we are interested in so that we 
		// can acquire the md5 as the stream is processed.
		
		
		
	}




	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.model.INodeFilter#loadFilterData(java.io.File)
	 */
	public void loadFilterData(File joinDataFile) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(joinDataFile));
		
		long targetRevision = -1;
		long copyFromRevision = -1;
		
		String targetPath = null;
		String copyFromPath = null;
		
		String sha1 = null;
		String md5 = null;
		
		while (true) {
			String line = reader.readLine();
			
			if (line == null)
				break; // we are at the end of the stream (done)
			
			if (line.trim().length() == 0)
				continue; // skip blank lines
			
			if (line.startsWith("#")) {
				// starting on a new item.
				targetRevision = -1;
				copyFromRevision = -1;
				
				targetPath = null;
				copyFromPath = null;
				
				sha1 = null;
				md5 = null;
				
				continue;
			}
			
			if (targetRevision == -1) {
				// read target details
				
				String parts[] = line.split("||");
				
				targetRevision = Long.valueOf(parts[0]);
				
				targetPath = parts[1];
			}
			else if (copyFromRevision == -1) {
				// read the copy from details
				
				String parts[] = line.split("||");
				
				copyFromRevision = Long.valueOf(parts[0]);
				
				copyFromPath = parts[1];
				
			}
			else {
				// read in the hashes
				String parts[] = line.split("||");
				
				sha1 = parts[0].trim();
				
				md5  = parts[1].trim();
				
				// now save the revision
				PathRevisionAndMD5AndSHA1 data = new PathRevisionAndMD5AndSHA1(copyFromPath, copyFromRevision, md5, sha1);
				
				Map<String, PathRevisionAndMD5AndSHA1> dataMap = this.revisionToPathToDataMap.get(targetRevision);
				
				if (dataMap == null) {
					dataMap = new HashMap<String, PathRevisionAndMD5AndSHA1>();
					revisionToPathToDataMap.put(targetRevision, dataMap);
				}
				
				if (dataMap.containsKey(targetPath)) {
					// skip over the current row
					log.warn(String.format("path exists %s.  Skipping path details = %s", targetPath, data.toString()));
				}
				else {
					dataMap.put(targetPath, data);
				}
				
			}
		}
		
		reader.close();
		
	}


	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.model.INodeFilter#getCopyFromData(long, java.lang.String)
	 */
	public PathRevisionAndMD5AndSHA1 getCopyFromData(long currentRevision, String path) {
		
		Long revision = Long.valueOf(currentRevision);
		
		Map<String, PathRevisionAndMD5AndSHA1> pathToDataMap = revisionToPathToDataMap.get(revision);
		
		if (pathToDataMap == null)
			return null;
		
		PathRevisionAndMD5AndSHA1 data = pathToDataMap.get(path);
		
		return data;
	}

}
