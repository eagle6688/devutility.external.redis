package devutility.external.redis.queue.stream;

import java.io.IOException;

import devutility.external.redis.com.ExceptionRetryApprover;
import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.queue.Acknowledger;
import devutility.external.redis.queue.JedisQueueConsumer;
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
	 * @param exceptionRetryApprover ExceptionRetryApprover object.
	 */
	public JedisPoolStreamQueueConsumer(JedisPool jedisPool, RedisQueueOption redisQueueOption, JedisStreamQueueConsumerEvent consumerEvent, ExceptionRetryApprover exceptionRetryApprover) {
		super(null, redisQueueOption, consumerEvent);
		this.jedisPool = jedisPool;
		setExceptionRetryApprover(exceptionRetryApprover);
	}

	/**
	 * Constructor
	 * @param jedisPool JedisPool object to get Jedis object.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent JedisQueueConsumerEvent object.
	 */
	public JedisPoolStreamQueueConsumer(JedisPool jedisPool, RedisQueueOption redisQueueOption, JedisStreamQueueConsumerEvent consumerEvent) {
		this(jedisPool, redisQueueOption, consumerEvent, null);
	}

	@Override
	public void listen() throws Exception {
		Acknowledger acknowledger = new JedisPoolStreamQueueAcknowledger(jedisPool, redisQueueOption);

		while (isActive()) {
			try (JedisStreamQueueConsumer consumer = new JedisStreamQueueConsumer(getJedis(), redisQueueOption, acknowledger, (JedisStreamQueueConsumerEvent) consumerEvent)) {
				consumer.listen();
			} catch (Exception e) {
				if (!isExceptionRetryApproved(e)) {
					throw e;
				}

				log("System try to create a new connection and continue working due to the following information:", e);
			}

			retryInterval();
		}
	}

	/**
	 * Return an Jedis object.
	 * @return Jedis
	 */
	private Jedis getJedis() {
		return JedisPoolUtil.jedis(jedisPool, redisQueueOption.getDatabase());
	}

	@Override
	public void close() throws IOException {
		setActive(false);
	}
}