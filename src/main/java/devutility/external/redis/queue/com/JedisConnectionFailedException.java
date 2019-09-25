package devutility.external.redis.queue.com;

/**
 * 
 * JedisConnectionFailedException
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 00:11:32
 */
public class JedisConnectionFailedException extends RuntimeException {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 7965277650720222517L;

	/**
	 * Constructor
	 */
	public JedisConnectionFailedException() {
		this("Redis can't connect!");
	}

	/**
	 * Constructor
	 * @param message Exception message.
	 */
	public JedisConnectionFailedException(String message) {
		super(message);
	}
}