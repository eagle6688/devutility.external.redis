package devutility.external.redis;

import devutility.external.redis.helpers.single.SingleRedisStringHelper;
import devutility.external.redis.models.SentinelRedisInstance;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
	protected final static String CONFIG_KEY_STREAM = "test-stream-queue";

	/**
	 * SingleRedisInstance object.
	 */
	protected SingleRedisInstance singleRedisInstance = RedisInstanceUtils.get(CONFIG_FILE, CONFIG_PREFIX, SingleRedisInstance.class);

	/**
	 * SingleRedisStringHelper object.
	 */
	protected SingleRedisStringHelper singleRedisStringHelper = RedisHelperFactory.singleRedisStringHelper(CONFIG_FILE, CONFIG_PREFIX);

	/**
	 * SentinelRedisInstance object.
	 */
	protected SentinelRedisInstance sentinel_RedisInstance = RedisInstanceUtils.get(CONFIG_FILE, CONFIG_SENTINEL_PREFIX, SentinelRedisInstance.class);

	/**
	 * 
	 * Create an JedisPool object use singleRedisInstance;
	 * @return JedisPool
	 */
	protected JedisPool jedisPool() {
		return JedisPoolUtil.jedisPool(singleRedisInstance);
	}

	/**
	 * Get Jedis object from JedisPool.
	 * @return Jedis
	 */
	protected Jedis jedis() {
		return JedisPoolUtil.jedis(singleRedisInstance);
	}
}