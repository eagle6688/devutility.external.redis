package devutility.external.redis.exception;

import redis.clients.jedis.exceptions.JedisException;

/**
 * 
 * JedisFatalException
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 00:11:32
 */
public class JedisFatalException extends JedisException {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -1497505979726088205L;

	/**
	 * Constructor
	 * @param message Exception message.
	 * @param cause Throwable object.
	 */
	public JedisFatalException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message Exception message.
	 */
	public JedisFatalException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 */
	public JedisFatalException() {
		this(null);
	}
}