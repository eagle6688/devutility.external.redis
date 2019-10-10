package devutility.external.redis.queue;

import java.io.Closeable;

import devutility.external.redis.com.RedisQueueOption;

/**
 * 
 * JedisQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 19:24:50
 */
public abstract class JedisQueueConsumer implements Closeable {
	/**
	 * Custom consumer event implementation.
	 */
	protected JedisQueueConsumerEvent consumerEvent;

	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption;

	/**
	 * Status, default is true.
	 */
	protected boolean active = true;

	/**
	 * Constructor
	 * @param consumerEvent JedisQueueConsumerEvent object.
	 * @param redisQueueOption RedisQueueOption object.
	 */
	public JedisQueueConsumer(JedisQueueConsumerEvent consumerEvent, RedisQueueOption redisQueueOption) {
		this.consumerEvent = consumerEvent;
		this.redisQueueOption = redisQueueOption;
	}

	/**
	 * Constructor
	 */
	public JedisQueueConsumer() {
		this(null, null);
	}

	/**
	 * Listen message from Redis queue.
	 * @throws Exception maybe have.
	 */
	public abstract void listen() throws Exception;

	public void log(String message) {
		if (!redisQueueOption.isDebug()) {
			return;
		}

		System.out.println(message);
	}

	public void log(Throwable cause) {
		if (!redisQueueOption.isDebug()) {
			return;
		}

		cause.printStackTrace(System.out);
	}
}