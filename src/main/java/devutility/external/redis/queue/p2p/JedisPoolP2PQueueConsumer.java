package devutility.external.redis.queue.p2p;

import java.io.IOException;

import devutility.external.redis.queue.ConsumerEvent;
import devutility.external.redis.queue.com.JedisBrokenException;
import devutility.external.redis.queue.com.RedisQueueOption;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import redis.clients.jedis.JedisPool;

/**
 * 
 * JedisPoolP2PQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-09-25 20:24:25
 */
public final class JedisPoolP2PQueueConsumer extends JedisQueueConsumer {
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
	public JedisPoolP2PQueueConsumer(JedisPool jedisPool, ConsumerEvent consumerEvent, RedisQueueOption redisQueueOption) {
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
	public JedisPoolP2PQueueConsumer(JedisPool jedisPool, ConsumerEvent consumerEvent, String key, int database) {
		this(jedisPool, consumerEvent, new RedisQueueOption(key, database));
	}

	@Override
	public void listen() throws Exception {
		while (active) {
			try (JedisP2PQueueConsumer jedisP2PQueueConsumer = new JedisP2PQueueConsumer(JedisPoolUtil.jedis(jedisPool, redisQueueOption.getDatabase()), consumerEvent, redisQueueOption)) {
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