package devutility.external.redis.queue;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.exception.JedisFatalException;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 19:24:50
 */
public abstract class JedisQueueConsumer extends JedisQueue {
	/**
	 * Custom consumer event implementation.
	 */
	private JedisQueueConsumerEvent consumerEvent;

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
	 */
	public JedisQueueConsumer() {
	}

	/**
	 * Constructor
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent JedisQueueConsumerEvent object.
	 */
	public JedisQueueConsumer(RedisQueueOption redisQueueOption, JedisQueueConsumerEvent consumerEvent) {
		super(redisQueueOption);
		this.setConsumerEvent(consumerEvent);
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

		if (connectionRetriedTimes >= getRedisQueueOption().getConnectionRetryTimes()) {
			throw new JedisFatalException("Exceed max connection retry times.");
		}

		Thread.sleep(getRedisQueueOption().getWaitMilliseconds());
		connectionRetriedTimes++;
		jedis.connect();
		connect(jedis);
	}

	/**
	 * Print log messages.
	 * @param message Log message.
	 */
	protected void log(String message) {
		if (!getRedisQueueOption().isDebug()) {
			return;
		}

		System.out.println(message);
	}

	/**
	 * Print log messages.
	 * @param cause Throwable object.
	 */
	protected void log(Throwable cause) {
		if (!getRedisQueueOption().isDebug()) {
			return;
		}

		cause.printStackTrace(System.out);
	}

	public JedisQueueConsumerEvent getConsumerEvent() {
		return consumerEvent;
	}

	public void setConsumerEvent(JedisQueueConsumerEvent consumerEvent) {
		this.consumerEvent = consumerEvent;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getConnectionRetriedTimes() {
		return connectionRetriedTimes;
	}

	public void setConnectionRetriedTimes(int connectionRetriedTimes) {
		this.connectionRetriedTimes = connectionRetriedTimes;
	}
}