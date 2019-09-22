package devutility.external.redis.queue.com;

/**
 * 
 * RedisConnectionFailedException
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 00:11:32
 */
public class RedisConnectionFailedException extends RuntimeException {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 7965277650720222517L;

	/**
	 * Constructor
	 */
	public RedisConnectionFailedException() {
		this("Redis can't connect!");
	}

	/**
	 * Constructor
	 * @param message Exception message.
	 */
	public RedisConnectionFailedException(String message) {
		super(message);
	}
}