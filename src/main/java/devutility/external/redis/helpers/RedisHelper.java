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

	/**
	 * Set database index.
	 * @param database: Redis database index.
	 */
	public void setDatabase(int database) {
		redisInstance.setDatabase(database);
	}

	/**
	 * Get database index.
	 * @return int
	 */
	public int getDatabase() {
		return redisInstance.getDatabase();
	}
}