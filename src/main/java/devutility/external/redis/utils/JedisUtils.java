package devutility.external.redis.utils;

import java.util.Set;

import devutility.external.redis.com.StatusCode;
import devutility.external.redis.models.ClusterRedisInstance;
import devutility.internal.lang.StringUtils;
import devutility.internal.security.Sha256Utils;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisUtils
 * 
 * @author: Aldwin Su
 * @version: 2019-09-29 22:25:51
 */
public class JedisUtils {
	/**
	 * Set expired time for the item with the specified key.
	 * @param jedis Jedis object.
	 * @param key Key value.
	 * @param seconds Expired seconds.
	 * @return boolean
	 */
	public static boolean expire(Jedis jedis, String key, int seconds) {
		if (jedis == null || StringUtils.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		return jedis.expire(key, seconds) == 1;
	}

	/**
	 * Remove the item with the specified key.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @return boolean
	 */
	public static boolean remove(Jedis jedis, String key) {
		if (jedis == null || StringUtils.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		return jedis.del(key) > 0;
	}

	/**
	 * Remove items by key pattern.
	 * @param jedis Jedis object.
	 * @param pattern Redis key pattern.
	 * @return boolean
	 */
	public static boolean removeByPattern(Jedis jedis, String pattern) {
		if (jedis == null || StringUtils.isNullOrEmpty(pattern)) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		Set<String> keys = jedis.keys(pattern);

		if (CollectionUtils.isNullOrEmpty(keys)) {
			return false;
		}

		return jedis.del(keys.toArray(new String[0])) == keys.size();
	}

	/**
	 * Select a database in Redis.
	 * @param jedis Jedis object.
	 * @param database Redis database index.
	 * @return boolean
	 */
	public static boolean select(Jedis jedis, int database) {
		if (jedis == null || database < 0) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		if (jedis.getDB() == database) {
			return true;
		}

		return StatusCode.isOk(jedis.select(database));
	}

	/**
	 * Is current Jedis connection broken or not?
	 * @param jedis Jedis object.
	 * @return boolean
	 */
	public static boolean isBrokenJedis(Jedis jedis) {
		return !jedis.isConnected() || jedis.getClient().isBroken();
	}

	/**
	 * Get cache key for ClusterRedisInstance object.
	 * @param redisInstance ClusterRedisInstance object.
	 * @return String
	 */
	protected static String getCacheKeyForClusterRedis(ClusterRedisInstance redisInstance) {
		String value = Sha256Utils.encipherToHex(redisInstance.getNodes());
		return String.format("%s.%s", redisInstance.getClass().getName(), value);
	}
}