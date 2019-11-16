package devutility.external.redis.queue.list;

import java.io.IOException;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.exception.JedisBrokenException;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * JedisPoolListQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 20:24:25
 */
public final class JedisPoolListQueueConsumer extends JedisQueueConsumer {
	/**
	 * JedisPool object for connecting Redis server.
	 */
	private JedisPool jedisPool;

	/**
	 * Constructor
	 * @param jedisPool JedisPool object to get Jedis object.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisPoolListQueueConsumer(JedisPool jedisPool, RedisQueueOption redisQueueOption, JedisQueueConsumerEvent consumerEvent) {
		super(null, redisQueueOption, consumerEvent);
		this.jedisPool = jedisPool;
	}

	/**
	 * Constructor
	 * @param jedisPool JedisPool object to get Jedis object.
	 * @param key Redis key of queue.
	 * @param database Redis database.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisPoolListQueueConsumer(JedisPool jedisPool, String key, int database, JedisQueueConsumerEvent consumerEvent) {
		this(jedisPool, new RedisQueueOption(key, database), consumerEvent);
	}

	@Override
	public void listen() throws Exception {
		while (isActive()) {
			Jedis jedis = JedisPoolUtil.jedis(jedisPool, redisQueueOption.getDatabase());

			try (JedisListQueueConsumer jedisListQueueConsumer = new JedisListQueueConsumer(jedis, redisQueueOption, consumerEvent)) {
				jedisListQueueConsumer.listen();
			} catch (Exception e) {
				if (e instanceof JedisBrokenException) {
					log("System try to created a new connection and continue working because Jedis connection has broken due to the following reason:");
					log(e.getCause());
					continue;
				}

				throw e;
			}
		}
	}

	@Override
	public void close() throws IOException {
		setActive(false);
	}
}