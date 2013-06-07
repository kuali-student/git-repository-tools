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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

	private static final String FIELD_SEPERATOR = "\\|\\|";

	private static final Logger log = LoggerFactory
			.getLogger(DefaultNodeFilter.class);

	private Map<Long, Map<String, PathRevisionAndMD5AndSHA1>> revisionToPathToDataMap = new LinkedHashMap<Long, Map<String, PathRevisionAndMD5AndSHA1>>();

	private Map<Long, Map<String,PathRevisionAndMD5AndSHA1>>copyFromRevisionToCopyFromPathToDataMap = new LinkedHashMap<Long, Map<String,PathRevisionAndMD5AndSHA1>>();
	
	// we assume only one join per target revision is allowed
	private List<JoinedRevision>joinedRevisionList = new ArrayList<JoinedRevision>();
	
	private Map<Long, String>copyFromRevPathPrefixMap = new HashMap<Long, String>();
	
	/**
	 * 
	 */
	public DefaultNodeFilter() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {

		// need to determine which revision's and paths we are interested in so
		// that we
		// can acquire the md5 as the stream is processed.

	}

	public String getFullCopyFromPath (long copyFromRevision, String partialPath) {
		
		String prefix = copyFromRevPathPrefixMap.get(copyFromRevision);
		
		if (prefix == null)
			return partialPath;
		
		return prefix + "/" + partialPath;
		
	}
	
	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.model.INodeFilter#getRevisionsToBeJoined()
	 */
	public List<JoinedRevision> getRevisionsToBeJoined() {
		return new ArrayList<JoinedRevision> (this.joinedRevisionList);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.model.INodeFilter#storeChecksumData(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void storeChecksumData(long currentRevision, String path,
			String sha1, String md5) {
		
		Map<String, PathRevisionAndMD5AndSHA1> copyFromMap = copyFromRevisionToCopyFromPathToDataMap.get(currentRevision);
		
		if (copyFromMap == null)
			return;
		
		PathRevisionAndMD5AndSHA1 data = copyFromMap.get(path);
		
		if (data == null) {
			/*
			 * This can happen because we only want to collect the md5 for the paths involved in the join
			 * 
			 * but the v3 dump can have a lot more paths that that.
			 */
			log.debug(String.format("SKIPPING:%d, %s (Data is unexpectantly null)", currentRevision, path));
			return;
		}
		
		data.setSha1(sha1);
		data.setMd5(md5);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kuali.student.svn.tools.model.INodeFilter#loadFilterData(java.io.
	 * File)
	 */
	public void loadFilterData(File joinDataFile) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(joinDataFile));
		
		Map<Long, Map<String, List<PathRevisionAndMD5AndSHA1>>>duplicateDataMap = new HashMap<Long, Map<String,List<PathRevisionAndMD5AndSHA1>>>();
		
		String copyFromPathPrefix = null;

		long targetRevision = -1;
		long copyFromRevision = -1;

		String targetPath = null;
		String copyFromPath = null;

		String sha1 = null;
		String md5 = null;
		
		int lineCounter = 0;

		String line = null;

		try {
			while (true) {
				line = reader.readLine();

				lineCounter++;

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

					String parts[] = line.split(FIELD_SEPERATOR);
					
					if (parts.length == 1) {

						copyFromPathPrefix = parts[0];
						
						/*
						 * This supports the bulk repair scenario where we have the absolute paths already.
						 */
						if (copyFromPathPrefix.equals("PLACEHOLDER"))
							copyFromPathPrefix = null;
						
					}
					else {

						targetRevision = Long.valueOf(parts[0].trim());

						targetPath = parts[1].trim();
					}
					
				} else if (copyFromRevision == -1) {
					// read the copy from details

					String parts[] = line.split(FIELD_SEPERATOR);

					copyFromRevision = Long.valueOf(parts[0].trim());

					copyFromPath = parts[1].trim();

				} else {
					// read in the hashes
					String parts[] = line.split(FIELD_SEPERATOR);

					sha1 = parts[0].trim();

					md5 = parts[1].trim();

					// now save the revision
					PathRevisionAndMD5AndSHA1 data = new PathRevisionAndMD5AndSHA1(
							copyFromPath, copyFromRevision, md5, sha1);

					Map<String, PathRevisionAndMD5AndSHA1> dataMap = this.revisionToPathToDataMap
							.get(targetRevision);

					if (dataMap == null) {
						dataMap = new HashMap<String, PathRevisionAndMD5AndSHA1>();
						revisionToPathToDataMap.put(targetRevision, dataMap);
					}

					if (dataMap.containsKey(targetPath)) {
						// skip over the current row
						
						Map<String, List<PathRevisionAndMD5AndSHA1>> dupMap = duplicateDataMap.get(targetRevision);
						
						if (dupMap == null) {
							dupMap = new HashMap<String, List<PathRevisionAndMD5AndSHA1>>();
							
							duplicateDataMap.put(targetRevision, dupMap);
						}
						
						List<PathRevisionAndMD5AndSHA1> duplicates = dupMap.get(targetPath);
						
						if (duplicates == null) {
							duplicates = new ArrayList<PathRevisionAndMD5AndSHA1>();
							dupMap.put(targetPath, duplicates);
						}
						
						duplicates.add(data);
						
					} else {
						dataMap.put(targetPath, data);
						
						// store the copyFromPath to data now
						
						Map<String, PathRevisionAndMD5AndSHA1> copyFromMap = this.copyFromRevisionToCopyFromPathToDataMap.get(copyFromRevision);
						
						if (copyFromMap == null) {
							copyFromMap = new HashMap<String, PathRevisionAndMD5AndSHA1>();
							this.copyFromRevisionToCopyFromPathToDataMap.put(copyFromRevision, copyFromMap);
						}
						
						PathRevisionAndMD5AndSHA1 originalData = copyFromMap.put(copyFromPath, data);
						
						if (originalData != null) {
							log.warn("OVERWRITING original data: " + originalData.toString());
						}
						
					}

				}
			}

			reader.close();

		} catch (RuntimeException e) {
			log.error(String.format("problem on line (%d) = %s", lineCounter,
					line));
			throw e;
		}
		
		// now append the duplicate data to the join file
		
		PrintWriter pw = new PrintWriter(new FileOutputStream(String.format("r%d-r%d-duplicates.log", targetRevision, copyFromRevision), true));
		
		for (Long revision : duplicateDataMap.keySet()) {
			
			Map<String, List<PathRevisionAndMD5AndSHA1>> dupMap = duplicateDataMap.get(revision);
			
			Map<String, PathRevisionAndMD5AndSHA1> dataMap = this.revisionToPathToDataMap.get(revision);
			
			for (String path : dupMap.keySet()) {
				
				PathRevisionAndMD5AndSHA1 used = dataMap.get(path);

				List<PathRevisionAndMD5AndSHA1> ignored = dupMap.get(path);
				
				if (ignored != null) {
					// log out the ignored data
					
					pw.println(String.format("REVISION:%d\tPATH:%s", revision, path));
					pw.println(String.format("USED:%d:%s", revision, used.toString()));
					
					for (PathRevisionAndMD5AndSHA1 pathRevisionAndMD5AndSHA1 : ignored) {
						pw.println(String.format("IGNORED:%d:%s", revision, pathRevisionAndMD5AndSHA1.toString()));	
					}
					
				}
			}
		}
		
		pw.close();
		
		// now record the joining of these two revisions:
		JoinedRevision jr = new JoinedRevision(targetRevision, copyFromRevision);
			
		this.joinedRevisionList.add(jr);
		
		if (copyFromPathPrefix != null)
			copyFromRevPathPrefixMap.put(copyFromRevision, copyFromPathPrefix);
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kuali.student.svn.tools.model.INodeFilter#getCopyFromData(long,
	 * java.lang.String)
	 */
	public PathRevisionAndMD5AndSHA1 getCopyFromData(long currentRevision,
			String path) {

		Long revision = Long.valueOf(currentRevision);

		Map<String, PathRevisionAndMD5AndSHA1> pathToDataMap = revisionToPathToDataMap
				.get(revision);

		if (pathToDataMap == null)
			return null;
		
		PathRevisionAndMD5AndSHA1 data = pathToDataMap.get(path);

		return data;
	}

}
