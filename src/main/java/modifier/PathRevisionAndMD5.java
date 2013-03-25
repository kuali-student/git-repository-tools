/**
 * 
 */
package modifier;

/**
 * 
 * Holds a path, revision and the md5 hash of the source file.
 * 
 * @author ocleirig
 *
 */
public class PathRevisionAndMD5 {

	private String path;
	
	private long revision;
	
	private String md5;
	
	private boolean supportsDelta = false;
	
	/**
	 * 
	 */
	public PathRevisionAndMD5() {
		// TODO Auto-generated constructor stub
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
	 * @return the supportsDelta
	 */
	public boolean isSupportsDelta() {
		return supportsDelta;
	}

	/**
	 * @param supportsDelta the supportsDelta to set
	 */
	public void setSupportsDelta(boolean supportsDelta) {
		this.supportsDelta = supportsDelta;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PathRevisionAndMD5 [path=");
		builder.append(path);
		builder.append(", revision=");
		builder.append(revision);
		builder.append(", md5=");
		builder.append(md5);
		builder.append(", supportsDelta=");
		builder.append(supportsDelta);
		builder.append("]");
		return builder.toString();
	}
	
	

}
