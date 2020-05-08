package devutility.external.redis.com;

/**
 * 
 * ExceptionRetryApprover
 * 
 * @author: Aldwin Su
 * @version: 2020-05-08 11:00:26
 */
public interface ExceptionRetryApprover {
	/**
	 * Does Exception object approved or not?
	 * @param exception Exception object.
	 * @return boolean
	 */
	boolean isApproved(Exception exception);
}