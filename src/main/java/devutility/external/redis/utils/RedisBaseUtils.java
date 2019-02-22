package devutility.external.redis.utils;

import java.util.Set;

import devutility.internal.lang.StringUtils;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;

public class RedisBaseUtils {
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
}