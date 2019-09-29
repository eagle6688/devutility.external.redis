package devutility.external.redis.com;

/**
 * 
 * JedisBrokenException
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 22:18:07
 */
public class JedisBrokenException extends RuntimeException {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 730169046333177260L;

	public JedisBrokenException(String message, Exception cause) {
		super(message, cause);
	}

	public JedisBrokenException(Exception cause) {
		this(cause.getMessage(), cause);
	}
}