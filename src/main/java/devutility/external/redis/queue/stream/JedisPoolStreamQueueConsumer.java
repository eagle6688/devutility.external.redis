package devutility.external.redis.queue.stream;

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
 * JedisPoolStreamQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 20:12:17
 */
public class JedisPoolStreamQueueConsumer extends JedisQueueConsumer {
	/**
	 * JedisPool object for connecting Redis server.
	 */
	private JedisPool jedisPool;

	/**
	 * Constructor
	 * @param jedisPool JedisPool object to get Jedis object.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent JedisQueueConsumerEvent object.
	 */
	public JedisPoolStreamQueueConsumer(JedisPool jedisPool, RedisQueueOption redisQueueOption, JedisQueueConsumerEvent consumerEvent) {
		super(null, redisQueueOption, consumerEvent);
		this.jedisPool = jedisPool;
	}

	@Override
	public void listen() throws Exception {
		while (isActive()) {
			Jedis jedis = JedisPoolUtil.jedis(jedisPool, getRedisQueueOption().getDatabase());

			try (JedisStreamQueueConsumer consumer = new JedisStreamQueueConsumer(jedis, redisQueueOption, consumerEvent)) {
				consumer.listen();
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