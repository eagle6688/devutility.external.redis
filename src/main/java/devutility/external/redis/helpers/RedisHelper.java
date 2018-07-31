package devutility.external.redis.helpers;

import devutility.external.redis.models.RedisInstance;
import devutility.external.redis.utils.JedisPoolUtil;
import devutility.internal.lang.StringHelper;
import redis.clients.jedis.Jedis;

public abstract class RedisHelper {
	/**
	 * RedisInstance object
	 */
	protected RedisInstance redisInstance;

	/**
	 * RedisHelper
	 * @param redisInstance
	 */
	public RedisHelper(RedisInstance redisInstance) {
		this.redisInstance = redisInstance;
	}

	/**
	 * Key of Pages count
	 * @param key: Redis key
	 * @return String
	 */
	protected String pagesCountKey(String key) {
		return String.format("%s:count", key);
	}

	/**
	 * Key of paging data
	 * @param key
	 * @param pageIndex
	 * @return String
	 */
	protected String pagingDataKey(String key, int pageIndex) {
		return String.format("%s:%d", key, pageIndex);
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
	 * Expire one item.
	 * @param key: Redis key
	 * @param seconds: Expire seconds
	 * @return boolean
	 */
	public boolean expire(String key, int seconds) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return jedis.expire(key, seconds) == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Expire one item.
	 * @param jedis: Jedis object.
	 * @param key: Key value.
	 * @param seconds: Expired seconds.
	 * @return boolean
	 */
	public boolean expire(Jedis jedis, String key, int seconds) {
		if (jedis == null || StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		return jedis.expire(key, seconds) == 1;
	}

	/**
	 * Remove one item from Redis.
	 * @param key: Redis key.
	 * @return boolean
	 */
	public boolean remove(String key) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return jedis.del(key) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove one item from Redis.
	 * @param jedis: Jedis object.
	 * @param key: Redis key.
	 * @return boolean
	 */
	public boolean remove(Jedis jedis, String key) {
		if (jedis == null || StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		return jedis.del(key) > 0;
	}
}