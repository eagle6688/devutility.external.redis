package devutility.external.redis.queue.p2p;

import java.io.IOException;
import java.util.List;

import devutility.external.redis.queue.ConsumerEvent;
import devutility.external.redis.queue.com.JedisBrokenException;
import devutility.external.redis.queue.com.JedisFatalException;
import devutility.external.redis.queue.com.RedisQueueOption;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;

/**
 * 
 * Jedis consumer of Redis queue use P2P mode.
 * 
 * @author: Aldwin Su
 * @version: 2019-09-20 17:12:56
 */
class JedisP2PQueueConsumer extends JedisQueueConsumer {
	/**
	 * Jedis object to read data from Redis.
	 */
	private Jedis jedis;

	/**
	 * Connection retry times.
	 */
	private int connectionRetryTimes;

	/**
	 * Chaneg the Jedis object.
	 * @param jedis Jedis object to listen queue.
	 */
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param consumerEvent Custom consumer event implementation.
	 * @param redisQueueOption RedisQueueOption object.
	 */
	public JedisP2PQueueConsumer(Jedis jedis, ConsumerEvent consumerEvent, RedisQueueOption redisQueueOption) {
		this.jedis = jedis;
		this.consumerEvent = consumerEvent;
		this.redisQueueOption = redisQueueOption;
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param consumerEvent Custom consumer event implementation.
	 * @param key Redis key of queue.
	 */
	public JedisP2PQueueConsumer(Jedis jedis, ConsumerEvent consumerEvent, String key) {
		this(jedis, consumerEvent, new RedisQueueOption(key));
	}

	@Override
	public void listen() throws Exception {
		if (jedis == null) {
			throw new IllegalArgumentException("jedis can't be null!");
		}

		while (active) {
			try {
				process();
			} catch (Exception e) {
				e.printStackTrace();

				if (jedis.getClient().isBroken()) {
					throw new JedisBrokenException(e);
				}

				if (e instanceof JedisFatalException) {
					throw e;
				}
			}
		}
	}

	/**
	 * Main process.
	 * @throws InterruptedException
	 */
	private void process() throws InterruptedException {
		connect();
		List<String> list = jedis.brpop(redisQueueOption.getWaitMilliseconds(), redisQueueOption.getKey());

		if (CollectionUtils.isNullOrEmpty(list)) {
			return;
		}

		callback(list.get(0), list.get(1));
	}

	/**
	 * Connect Redis server.
	 * @throws InterruptedException from Thread sleep method.
	 */
	private void connect() throws InterruptedException {
		if (jedis.isConnected()) {
			return;
		}

		if (connectionRetryTimes >= redisQueueOption.getConnectionRetryTimes()) {
			throw new JedisFatalException("Exceed max connection retry times.");
		}

		Thread.sleep(redisQueueOption.getWaitMilliseconds());
		connectionRetryTimes++;
		jedis.connect();
		connect();
	}

	/**
	 * Callback method when new message arrived.
	 * @param key Redis key of queue.
	 * @param value Message.
	 */
	private void callback(String key, String value) {
		if (consumerEvent == null) {
			return;
		}

		consumerEvent.onMessage(key, value);
	}

	@Override
	public void close() throws IOException {
		active = false;
		connectionRetryTimes = 0;
		jedis.close();
	}
}