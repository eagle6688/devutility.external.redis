package devutility.external.redis.queue;

import java.io.Closeable;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.ext.DevJedis;
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
	 * Exception start time in milliseconds.
	 */
	protected long exceptionStartMillis = System.currentTimeMillis();

	/**
	 * Count of exceptions from consuming function.
	 */
	protected int exceptionCount;

	/**
	 * Status, default is true.
	 */
	private boolean active = true;

	/**
	 * Connection retried times.
	 */
	private int connectionRetriedTimes;

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
	 * Connect Redis server.
	 * @throws InterruptedException from Thread sleep method.
	 */
	protected void connect(Jedis jedis) throws InterruptedException {
		if (jedis.getClient() == null || jedis.getClient().isBroken()) {
			throw new JedisFatalException("Jedis connection has broken.");
		}

		if (jedis.isConnected()) {
			return;
		}

		if (connectionRetriedTimes >= redisQueueOption.getConnectionRetryTimes()) {
			throw new JedisFatalException("Exceed max connection retry times.");
		}

		Thread.sleep(redisQueueOption.getWaitMilliseconds());
		connectionRetriedTimes++;
		jedis.connect();
		connect(jedis);
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

		cause.printStackTrace(System.out);
	}

	/**
	 * Set Jedis object.
	 * @param jedis Jedis object to listen queue.
	 */
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
		this.devJedis.setJedis(jedis);
	}

	/**
	 * Reasonable exception means exceptions count from consuming function not exceed the exceptionLimit value(in
	 * redisQueueOption object) in exceptionIntervalMillis time.
	 * @return boolean
	 */
	protected boolean isReasonableException() {
		long currentTime = System.currentTimeMillis();
		long intervalTime = currentTime - exceptionStartMillis;

		if (intervalTime > redisQueueOption.getExceptionIntervalMillis()) {
			this.exceptionStartMillis = currentTime;
			this.exceptionCount = 0;
			return true;
		}

		this.exceptionCount += 1;

		if (this.exceptionCount <= redisQueueOption.getExceptionLimit()) {
			return true;
		}

		return false;
	}

	public boolean isActive() {
		return active;
	}

	protected void setActive(boolean active) {
		this.active = active;
	}

	public int getConnectionRetriedTimes() {
		return connectionRetriedTimes;
	}

	protected void setConnectionRetriedTimes(int connectionRetriedTimes) {
		this.connectionRetriedTimes = connectionRetriedTimes;
	}
}