package devutility.external.redis;

import devutility.external.redis.helpers.RedisStringHelper;
import devutility.external.redis.models.RedisInstance;

public class RedisHelperFactory {
	/**
	 * Create a RedisStringHelper object.
	 * @param propertiesFile: Properties file name.
	 * @param prefix: Prefix name.
	 * @return RedisStringHelper
	 */
	public static RedisStringHelper redisStringHelper(String propertiesFile, String prefix) {
		RedisInstance redisInstance = RedisInstanceUtils.get(propertiesFile, prefix);
		return new RedisStringHelper(redisInstance);
	}
}