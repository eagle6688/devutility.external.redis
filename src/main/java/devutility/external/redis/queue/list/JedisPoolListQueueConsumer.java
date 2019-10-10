package devutility.external.redis.queue.list;

import java.io.IOException;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.exception.JedisBrokenException;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.utils.pool.JedisPoolUtil;
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
	 * @param consumerEvent Custom consumer event implementation.
	 * @param redisQueueOption RedisQueueOption object.
	 */
	public JedisPoolListQueueConsumer(JedisPool jedisPool, JedisQueueConsumerEvent consumerEvent, RedisQueueOption redisQueueOption) {
		super(consumerEvent, redisQueueOption);
		this.jedisPool = jedisPool;
	}

	/**
	 * Constructor
	 * @param jedisPool JedisPool object to get Jedis object.
	 * @param consumerEvent Custom consumer event implementation.
	 * @param key Redis key of queue.
	 * @param database Redis database.
	 */
	public JedisPoolListQueueConsumer(JedisPool jedisPool, JedisQueueConsumerEvent consumerEvent, String key, int database) {
		this(jedisPool, consumerEvent, new RedisQueueOption(key, database));
	}

	@Override
	public void listen() throws Exception {
		while (active) {
			try (JedisListQueueConsumer jedisP2PQueueConsumer = new JedisListQueueConsumer(JedisPoolUtil.jedis(jedisPool, redisQueueOption.getDatabase()), consumerEvent, redisQueueOption)) {
				jedisP2PQueueConsumer.listen();
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
		active = false;
	}
}