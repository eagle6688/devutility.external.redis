package devutility.external.redis.helper;

import devutility.external.redis.model.RedisInstance;

public abstract class RedisHelper {
	/**
	 * RedisInstance object.
	 */
	protected RedisInstance redisInstance;

	/**
	 * Constructor.
	 * @param redisInstance: RedisInstance object.
	 */
	public RedisHelper(RedisInstance redisInstance) {
		this.redisInstance = redisInstance;
	}
}