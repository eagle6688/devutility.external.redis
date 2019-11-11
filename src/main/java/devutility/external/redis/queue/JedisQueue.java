package devutility.external.redis.queue;

import devutility.external.redis.com.RedisQueueOption;

/**
 * 
 * JedisQueue
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 21:16:42
 */
public abstract class JedisQueue {
	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption;

	/**
	 * Constructor
	 * @param redisQueueOption RedisQueueOption object.
	 */
	public JedisQueue(RedisQueueOption redisQueueOption) {
		this.redisQueueOption = redisQueueOption;
	}

	public RedisQueueOption getRedisQueueOption() {
		return redisQueueOption;
	}
}