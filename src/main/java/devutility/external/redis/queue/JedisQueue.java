package devutility.external.redis.queue;

import devutility.external.redis.model.RedisQueueOption;
import devutility.internal.lang.StringUtils;

/**
 * 
 * JedisQueue
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 21:16:42
 */
public abstract class JedisQueue {
	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption;

	/**
	 * Constructor
	 * @param redisQueueOption RedisQueueOption object.
	 */
	public JedisQueue(RedisQueueOption redisQueueOption) {
		this.redisQueueOption = redisQueueOption;
	}

	/**
	 * Validate parameters that caller provided.
	 */
	protected void validate() {
		if (StringUtils.isNullOrEmpty(redisQueueOption.getKey())) {
			throw new IllegalArgumentException("Redis key can't be empty!");
		}
	}

	/**
	 * Print log messages.
	 * @param message Log message.
	 */
	protected void log(String message) {
		if (!redisQueueOption.isDebug()) {
			return;
		}

		System.out.println(message);
	}

	/**
	 * Print log messages.
	 * @param cause Throwable object.
	 */
	protected void log(Throwable cause) {
		if (!redisQueueOption.isDebug()) {
			return;
		}

		cause.printStackTrace(System.err);
	}

	/**
	 * Print log messages.
	 * @param message Log message.
	 * @param cause Throwable object.
	 */
	protected void log(String message, Throwable cause) {
		if (!redisQueueOption.isDebug()) {
			return;
		}

		System.out.println(message);

		if (cause != null) {
			cause.printStackTrace(System.err);
		}
	}

	/**
	 * Print log messages.
	 * @param message Log message.
	 * @param exception Exception object.
	 */
	protected void log(String message, Exception exception) {
		log(message, exception.getCause());
	}
}