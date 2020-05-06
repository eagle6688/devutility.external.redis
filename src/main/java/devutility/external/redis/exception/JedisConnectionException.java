package devutility.external.redis.exception;

/**
 * 
 * JedisBrokenException
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 22:18:07
 */
public class JedisConnectionException extends RuntimeException {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 730169046333177260L;

	public JedisConnectionException(String message, Exception cause) {
		super(message, cause);
	}

	public JedisConnectionException(Exception cause) {
		this(cause.getMessage(), cause);
	}
}