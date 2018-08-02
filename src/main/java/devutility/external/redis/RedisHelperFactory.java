package devutility.external.redis;

import devutility.external.redis.helpers.single.SingleRedisStringHelper;
import devutility.external.redis.models.SingleRedisInstance;

public class RedisHelperFactory {
	/**
	 * Create a RedisStringHelper object.
	 * @param propertiesFile: Properties file name.
	 * @param prefix: Prefix name.
	 * @return RedisStringHelper
	 */
	public static SingleRedisStringHelper singleRedisStringHelper(String propertiesFile, String prefix) {
		SingleRedisInstance redisInstance = RedisInstanceUtils.get(propertiesFile, prefix, SingleRedisInstance.class);
		return new SingleRedisStringHelper(redisInstance);
	}
}