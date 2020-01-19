package devutility.external.redis.helpers;

import devutility.external.redis.models.RedisInstance;

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