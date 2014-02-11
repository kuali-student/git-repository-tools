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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.student.svn.tools.merge.tools.BranchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class MergeDetectorDataImpl implements MergeDetectorData {

	private static Logger log = LoggerFactory.getLogger(MergeDetectorDataImpl.class);
	
	private static class TargetData {
		
		private Long revision;
		private String path;
		/**
		 * @param revision
		 * @param path
		 */
		public TargetData(Long revision, String path) {
			super();
			this.revision = revision;
			this.path = path;
		}
		/**
		 * @return the revision
		 */
		public Long getRevision() {
			return revision;
		}
		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			result = prime * result
					+ ((revision == null) ? 0 : revision.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TargetData other = (TargetData) obj;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			if (revision == null) {
				if (other.revision != null)
					return false;
			} else if (!revision.equals(other.revision))
				return false;
			return true;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("TargetData [revision=");
			builder.append(revision);
			builder.append(", path=");
			builder.append(path);
			builder.append("]");
			return builder.toString();
		}
		
		
	}
	
	private static class SourceData extends TargetData {
		
		private String md5Hash;

		/**
		 * @param revision
		 * @param path
		 */
		public SourceData(Long revision, String path, String md5Hash) {
			super(revision, path);
			
			this.md5Hash = md5Hash;
		}

		/**
		 * @return the md5Hash
		 */
		public String getMd5Hash() {
			return md5Hash;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result
					+ ((md5Hash == null) ? 0 : md5Hash.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			SourceData other = (SourceData) obj;
			if (md5Hash == null) {
				if (other.md5Hash != null)
					return false;
			} else if (!md5Hash.equals(other.md5Hash))
				return false;
			return true;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("SourceData [md5Hash=");
			builder.append(md5Hash);
			builder.append(", getRevision()=");
			builder.append(getRevision());
			builder.append(", getPath()=");
			builder.append(getPath());
			builder.append("]");
			return builder.toString();
		}
		
		
		
	}
	
	private static class MergeData {
		private SourceData copyFrom;
		private TargetData targetData;
		/**
		 * @param copyFrom
		 * @param targetData
		 */
		public MergeData(SourceData copyFrom, TargetData targetData) {
			super();
			this.copyFrom = copyFrom;
			this.targetData = targetData;
		}
		/**
		 * @return the copyFrom
		 */
		public SourceData getCopyFrom() {
			return copyFrom;
		}
		/**
		 * @return the targetData
		 */
		public TargetData getTargetData() {
			return targetData;
		}
		
	}
	
	private List<MergeData>revisionMergeDataList = new ArrayList<MergeData>();
	
	private Map<TargetData, String>targetToMergeInfoMap = new HashMap<TargetData, String>();
	
	private StringBuilder svnMergeInfoBuilder = new StringBuilder();
	
	/**
	 * 
	 */
	public MergeDetectorDataImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.merge.model.MergeDetectorData#storePath(java.lang.Long, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	@Override
	public void storePath(Long copyFromRevision, String copyFromPath,
			String copyFromMD5, Long currentRevision, String currentPath) {

		SourceData sd = new SourceData(copyFromRevision, copyFromPath, copyFromMD5);
		
		TargetData td = new TargetData(currentRevision, currentPath);
		
		revisionMergeDataList.add(new MergeData(sd, td));
		
		
		
	}
	
	

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.merge.model.MergeDetectorData#storeSvnMergeInfo(long, java.lang.String, java.lang.String)
	 */
	@Override
	public void storeSvnMergeInfo(long currentRevision, String path,
			String svnMergeInfo) {
		
		TargetData td = new TargetData(currentRevision, path);
		
		targetToMergeInfoMap.put(td, svnMergeInfo);
		
		if (svnMergeInfoBuilder.length() > 0)
			svnMergeInfoBuilder.append("\n");
		
		svnMergeInfoBuilder.append(svnMergeInfo);
		
		
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.svn.tools.merge.model.MergeDetectorData#processRevision(java.io.PrintWriter, long)
	 */
	@Override
	public void processRevision(PrintWriter outputWriter, Long currentRevision) {

		//extract the merge path
		if (revisionMergeDataList.size() > 0) {
			for (MergeData md : revisionMergeDataList) {
				
				String targetPath = md.getTargetData().getPath();
				String copyFromPath = md.getCopyFrom().getPath();
				
//				log.info(String.format("copyFrom = %s, target = %s", copyFromPath, targetPath));
				
				BranchData targetData = BranchUtils.parse(md.getTargetData().getRevision(), targetPath);
				
				BranchData copyFromData = BranchUtils.parse(md.getCopyFrom().getRevision(), copyFromPath);
				
				if (targetData.isTag())
					continue;
				
				if (!targetData.getBranchPath().equals(copyFromData.getBranchPath())) {
					
					log.info(String.format("merge detected at rev:%d", currentRevision));
					log.info("copyFromPath : " + copyFromData.getBranchPath());
					log.info("targetpath: " + targetData.getBranchPath());
					
					outputWriter.println(String.format("detected;%d;%d;%s;%s;%s;%s", copyFromData.getRevision(), currentRevision,   copyFromData.getBranchPath(), targetData.getBranchPath(), copyFromData.getPath(), targetData.getPath()));
					
				}
			}

			
			if (svnMergeInfoBuilder.length() > 0) {
				outputWriter.println(String.format ("mergeinfo;%d;%s", currentRevision, svnMergeInfoBuilder.toString()));
			}
		}
		
		svnMergeInfoBuilder.delete(0, svnMergeInfoBuilder.length());
		
		revisionMergeDataList.clear();
		
	}

}
