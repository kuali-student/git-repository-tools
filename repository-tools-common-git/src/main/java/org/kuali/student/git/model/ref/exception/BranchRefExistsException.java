/**
 * 
 */
package org.kuali.student.git.model.ref.exception;

/**
 * @author ocleirig
 *
 */
public class BranchRefExistsException extends Exception {

	/**
	 * 
	 */
	public BranchRefExistsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public BranchRefExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public BranchRefExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BranchRefExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BranchRefExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
