package devutility.external.redis.queue.stream;

import devutility.external.redis.com.QueueMode;
import devutility.external.redis.ext.DevJedis;
import devutility.external.redis.model.RedisQueueOption;
import devutility.external.redis.queue.Acknowledger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * JedisPoolStreamQueueAcknowledger
 * 
 * @author: Aldwin Su
 * @version: 2020-04-01 15:47:25
 */
public class JedisPoolStreamQueueAcknowledger implements Acknowledger {
	/**
	 * JedisPool object.
	 */
	private JedisPool jedisPool;

	/**
	 * RedisQueueOption object.
	 */
	private RedisQueueOption redisQueueOption;

	/**
	 * Constructor
	 * @param jedisPool
	 */
	public JedisPoolStreamQueueAcknowledger(JedisPool jedisPool, final RedisQueueOption redisQueueOption) {
		this.jedisPool = jedisPool;
		this.redisQueueOption = redisQueueOption;
	}

	@Override
	public void ack(StreamEntryID streamEntryId) {
		try (Jedis jedis = jedisPool.getResource()) {
			if (QueueMode.P2P == redisQueueOption.getMode()) {
				new DevJedis(jedis).xack(redisQueueOption.getKey(), redisQueueOption.getGroupName(), streamEntryId);
			} else {
				jedis.xack(redisQueueOption.getKey(), redisQueueOption.getGroupName(), streamEntryId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}