package devutility.external.redis.queue.com;

/**
 * 
 * JedisFatalException
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 00:11:32
 */
public class JedisFatalException extends RuntimeException {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 7965277650720222517L;

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