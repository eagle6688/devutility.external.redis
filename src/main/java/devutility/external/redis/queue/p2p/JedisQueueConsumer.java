package devutility.external.redis.queue.p2p;

import java.io.Closeable;

import devutility.external.redis.queue.ConsumerEvent;
import devutility.external.redis.queue.com.RedisQueueOption;

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
	protected ConsumerEvent consumerEvent;

	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption;

	/**
	 * Status, default is true.
	 */
	protected boolean active = true;

	public JedisQueueConsumer(ConsumerEvent consumerEvent, RedisQueueOption redisQueueOption) {
		this.consumerEvent = consumerEvent;
		this.redisQueueOption = redisQueueOption;
	}

	/**
	 * Listen message from Redis queue.
	 * @throws Exception maybe have.
	 */
	public abstract void listen() throws Exception;

	public JedisQueueConsumer() {
		this(null, null);
	}
}