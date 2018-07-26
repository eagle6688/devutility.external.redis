package devutility.external.redis.helper;

import devutility.external.redis.RedisUtils;
import devutility.internal.dao.models.RedisInstance;
import devutility.internal.lang.StringHelper;
import redis.clients.jedis.Jedis;

public abstract class RedisHelper {
	/**
	 * RedisInstance object
	 */
	protected RedisInstance redisInstance;

	/**
	 * RedisBulkHelper
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
	 * Set DbIndex
	 * @param dbIndex: Redis Db index
	 */
	public void setDbIndex(int dbIndex) {
		redisInstance.setDBIndex(dbIndex);
	}

	/**
	 * Get DbIndex
	 * @return int
	 */
	public int getDbIndex() {
		return redisInstance.getDBIndex();
	}

	/**
	 * Expire one item
	 * @param key: Redis key
	 * @param seconds: Expire seconds
	 * @return boolean
	 */
	public boolean expire(String key, int seconds) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			return jedis.expire(key, seconds) == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove one item from Redis
	 * @param key: Redis key
	 * @return boolean
	 */
	public boolean remove(String key) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			return jedis.del(key) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}