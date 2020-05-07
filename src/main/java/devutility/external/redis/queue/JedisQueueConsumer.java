package devutility.external.redis.queue;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicInteger;

import devutility.external.redis.ext.DevJedis;
import devutility.external.redis.model.RedisQueueOption;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 19:24:50
 */
public abstract class JedisQueueConsumer extends JedisQueue implements Closeable {
	/**
	 * Jedis object to read data from Redis.
	 */
	protected Jedis jedis;

	/**
	 * DevJedis object.
	 */
	protected DevJedis devJedis;

	/**
	 * Custom consumer event implementation.
	 */
	protected JedisQueueConsumerEvent consumerEvent;

	/**
	 * Status, default is true.
	 */
	private boolean active = true;

	/**
	 * Exception start time in milliseconds.
	 */
	private long exceptionStartMillis = System.currentTimeMillis();

	/**
	 * Count of exceptions from consuming function.
	 */
	private AtomicInteger exceptionCount = new AtomicInteger(0);

	/**
	 * Constructor
	 * @param jedis Jedis object.
	 * @param redisQueueOption Configuration of Redis queue.
	 * @param consumerEvent JedisQueueConsumerEvent object.
	 */
	public JedisQueueConsumer(Jedis jedis, RedisQueueOption redisQueueOption, JedisQueueConsumerEvent consumerEvent) {
		super(redisQueueOption);
		this.jedis = jedis;
		this.devJedis = new DevJedis(jedis);
		this.consumerEvent = consumerEvent;
	}

	/**
	 * Validate parameters that caller provided.
	 */
	@Override
	protected void validate() {
		super.validate();

		if (jedis == null) {
			throw new IllegalArgumentException("Jedis object not found!");
		}
	}

	/**
	 * Listen message from Redis queue.
	 * @throws Exception maybe have.
	 */
	public abstract void listen() throws Exception;

	/**
	 * Reasonable exception means exceptions count from consuming function not exceed the exceptionLimit value(in
	 * redisQueueOption object) in exceptionIntervalMillis time.
	 * @return boolean
	 */
	protected boolean isReasonableConsumerException() {
		/**
		 * No limitation.
		 */
		if (redisQueueOption.getExceptionIntervalMillis() == 0 || redisQueueOption.getExceptionLimit() == 0) {
			return true;
		}

		long currentTime = System.currentTimeMillis();
		long intervalTime = currentTime - exceptionStartMillis;

		if (intervalTime > redisQueueOption.getExceptionIntervalMillis()) {
			this.exceptionStartMillis = currentTime;
			this.exceptionCount.set(0);
			return true;
		}

		int count = this.exceptionCount.incrementAndGet();

		if (count <= redisQueueOption.getExceptionLimit()) {
			return true;
		}

		return false;
	}

	/**
	 * Retry connect Redis after interval time.
	 * @throws InterruptedException from Thread.sleep
	 */
	protected void retryInterval() throws InterruptedException {
		if (redisQueueOption.getConnectionRetryInterval() < 1) {
			return;
		}

		Thread.sleep(redisQueueOption.getConnectionRetryInterval());
	}

	/**
	 * Set Jedis object.
	 * @param jedis Jedis object to listen queue.
	 */
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
		this.devJedis.setJedis(jedis);
	}

	public boolean isActive() {
		return active;
	}

	protected void setActive(boolean active) {
		this.active = active;
	}
}