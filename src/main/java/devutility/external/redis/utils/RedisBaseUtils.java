package devutility.external.redis.utils;

import devutility.internal.lang.StringHelper;
import redis.clients.jedis.Jedis;

public class RedisBaseUtils {
	/**
	 * Set expired time for the item with the specified key.
	 * @param jedis: Jedis object.
	 * @param key: Key value.
	 * @param seconds: Expired seconds.
	 * @return boolean
	 */
	public static boolean expire(Jedis jedis, String key, int seconds) {
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
	public static boolean remove(Jedis jedis, String key) {
		if (jedis == null || StringHelper.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		return jedis.del(key) > 0;
	}
}