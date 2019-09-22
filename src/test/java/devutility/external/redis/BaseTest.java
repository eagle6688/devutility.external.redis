package devutility.external.redis;

import devutility.external.redis.helpers.single.SingleRedisStringHelper;
import devutility.external.redis.models.SentinelRedisInstance;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import redis.clients.jedis.Jedis;

/**
 * 
 * BaseTest
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:29:06
 */
public abstract class BaseTest extends devutility.internal.test.BaseTest {
	private final static String CONFIG_FILE = "config.properties";
	private final static String CONFIG_PREFIX = "redis";
	private final static String CONFIG_SENTINEL_PREFIX = "sentinel";

	/**
	 * SingleRedisStringHelper object.
	 */
	protected SingleRedisStringHelper singleRedisStringHelper = RedisHelperFactory.singleRedisStringHelper(CONFIG_FILE, CONFIG_PREFIX);

	/**
	 * SentinelRedisInstance object.
	 */
	protected SentinelRedisInstance sentinel_RedisInstance = RedisInstanceUtils.get(CONFIG_FILE, CONFIG_SENTINEL_PREFIX, SentinelRedisInstance.class);

	/**
	 * Get Jedis object from JedisPool.
	 * @return Jedis
	 */
	protected Jedis jedis() {
		SingleRedisInstance redisInstance = RedisInstanceUtils.get(CONFIG_FILE, CONFIG_PREFIX, SingleRedisInstance.class);
		return JedisPoolUtil.jedis(redisInstance);
	}
}