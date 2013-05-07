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
package modifier;

/**
 * 
 * Holds a path, revision and the md5 hash of the source file.
 * 
 * @author Kuali Student Team
 *
 */
public class PathRevisionAndMD5AndSHA1 {

	private String path;
	
	private long revision;
	
	private String md5;
	
	private String sha1;
	
	/**
	 * 
	 */
	public PathRevisionAndMD5AndSHA1() {
		// TODO Auto-generated constructor stub
	}

	
	public PathRevisionAndMD5AndSHA1(String path, long revision, String md5, String sha1) {
		super();
		this.path = path;
		this.revision = revision;
		this.md5 = md5;
		this.sha1 = sha1;
	}


	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the revision
	 */
	public long getRevision() {
		return revision;
	}

	/**
	 * @param revision the revision to set
	 */
	public void setRevision(long revision) {
		this.revision = revision;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	
	/**
	 * @param sha1 the sha1 to set
	 */
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	
	


	/**
	 * @return the sha1
	 */
	public String getSha1() {
		return sha1;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PathRevisionAndMD5AndSHA1 [path=");
		builder.append(path);
		builder.append(", revision=");
		builder.append(revision);
		builder.append(", md5=");
		builder.append(md5);
		builder.append(", sha1=");
		builder.append(sha1);
		builder.append("]");
		return builder.toString();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((md5 == null) ? 0 : md5.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + (int) (revision ^ (revision >>> 32));
		result = prime * result + ((sha1 == null) ? 0 : sha1.hashCode());
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
		PathRevisionAndMD5AndSHA1 other = (PathRevisionAndMD5AndSHA1) obj;
		if (md5 == null) {
			if (other.md5 != null)
				return false;
		} else if (!md5.equals(other.md5))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (revision != other.revision)
			return false;
		if (sha1 == null) {
			if (other.sha1 != null)
				return false;
		} else if (!sha1.equals(other.sha1))
			return false;
		return true;
	}
	
	

}
