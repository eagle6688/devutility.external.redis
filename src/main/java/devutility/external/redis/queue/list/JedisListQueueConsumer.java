package devutility.external.redis.queue.list;

import java.io.IOException;
import java.util.List;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.exception.JedisConnectionException;
import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;

/**
 * 
 * Jedis consumer of Redis queue use P2P mode.
 * 
 * @author: Aldwin Su
 * @version: 2019-09-20 17:12:56
 */
public final class JedisListQueueConsumer extends JedisQueueConsumer {
	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisListQueueConsumer(Jedis jedis, RedisQueueOption redisQueueOption, JedisQueueConsumerEvent consumerEvent) {
		super(jedis, redisQueueOption, consumerEvent);
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param key Redis key of queue.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisListQueueConsumer(Jedis jedis, String key, JedisQueueConsumerEvent consumerEvent) {
		this(jedis, new RedisQueueOption(key), consumerEvent);
	}

	@Override
	public void listen() throws Exception {
		if (jedis == null) {
			throw new IllegalArgumentException("jedis can't be null!");
		}

		while (isActive()) {
			try {
				process();
			} catch (Exception e) {
				if (jedis.getClient().isBroken()) {
					throw new JedisConnectionException(e);
				}

				if (e instanceof JedisFatalException) {
					throw e;
				}

				log(e);
			}
		}
	}

	/**
	 * Main process.
	 * @throws InterruptedException
	 */
	private void process() throws InterruptedException {
		connect(jedis);
		List<String> list = jedis.brpop(redisQueueOption.getWaitMilliseconds(), redisQueueOption.getKey());

		if (CollectionUtils.isNullOrEmpty(list)) {
			return;
		}

		callback(list.get(0), list.get(1));
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
		setActive(false);
		setConnectionRetriedTimes(0);
		jedis.close();
	}
}