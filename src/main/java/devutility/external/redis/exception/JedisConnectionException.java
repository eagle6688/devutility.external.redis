package devutility.external.redis.exception;

/**
 * 
 * JedisConnectionException
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 22:18:07
 */
public class JedisConnectionException extends RuntimeException {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 730169046333177260L;

	/**
	 * Constructor
	 * @param message Exception message.
	 * @param cause Exception object.
	 */
	public JedisConnectionException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param cause Exception object.
	 */
	public JedisConnectionException(Exception cause) {
		this(cause.getMessage(), cause);
	}

	/**
	 * Constructor
	 * @param message Exception message.
	 */
	public JedisConnectionException(String message) {
		super(message);
	}
}