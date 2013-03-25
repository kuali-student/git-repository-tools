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

import java.util.LinkedHashMap;
import java.util.Map;

import modifier.PathRevisionAndMD5;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Kuali Student Team
 *
 */
public class DefaultNodeFilter implements INodeFilter, InitializingBean {

	private Map<Long, Map<String, String>>revisionToPathToMD5Map = new LinkedHashMap<Long, Map<String,String>>();
	
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
	 * @see org.kuali.student.svn.tools.model.INodeFilter#storeMD5(long, java.lang.String, java.lang.String)
	 */
	public void storeMD5(long currentRevision, String path, String md5) {
		
		Long revision = Long.valueOf(currentRevision);
		
		Map<String, String> pathToMD5Map = revisionToPathToMD5Map.get(revision);
		
		if (pathToMD5Map == null) // the map needs to be initialized before we get to this point so we know we want this md5.
			return;
		else
			pathToMD5Map.put(path, md5);

	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.model.INodeFilter#getCopyFromData(long, java.lang.String)
	 */
	public PathRevisionAndMD5 getCopyFromData(long currentRevision, String path) {
		
		Long revision = Long.valueOf(currentRevision);
		
		Map<String, String> pathToMD5Map = revisionToPathToMD5Map.get(revision);
		
		if (pathToMD5Map == null)
			return null;
		
		String md5 = pathToMD5Map.get(path);
		
		
		return new PathRevisionAndMD5(path, currentRevision, md5);
	}

}
