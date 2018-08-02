package devutility.external.redis.helpers;

import devutility.external.redis.models.RedisInstance;
import devutility.internal.lang.StringHelper;
import redis.clients.jedis.Jedis;

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

	/**
	 * Set expired time for the item with the specified key.
	 * @param jedis: Jedis object.
	 * @param key: Key value.
	 * @param seconds: Expired seconds.
	 * @return boolean
	 */
	public boolean expire(Jedis jedis, String key, int seconds) {
		if (jedis == null || StringHelper.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		return jedis.expire(key, seconds) == 1;
	}

	/**
	 * Remove the item with the specified key.
	 * @param jedis: Jedis object.
	 * @param key: Redis key.
	 * @return boolean
	 */
	public boolean remove(Jedis jedis, String key) {
		if (jedis == null || StringHelper.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		return jedis.del(key) > 0;
	}
}