/**
 * 
 */
package modifier;

/**
 * @author ocleirig
 *
 */
public interface INodeModifier {

	/**
	 * Get the copy path, revision and md5 hash string that should be applied to te revision and node path provided
	 * 
	 * @param revision
	 * @param nodePath
	 * @return
	 */
	public PathRevisionAndMD5AndSHA1 getCopyPathAndRevision(long revision, String nodePath);
		

}
