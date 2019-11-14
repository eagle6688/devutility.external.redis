package devutility.external.redis.queue;

import devutility.external.redis.com.RedisQueueOption;
import devutility.internal.lang.StringUtils;

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
	 * Validate parameters that caller provided.
	 */
	protected void validate() {
		if (StringUtils.isNullOrEmpty(redisQueueOption.getKey())) {
			throw new IllegalArgumentException("Redis key can't be empty!");
		}
	}

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