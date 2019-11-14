package devutility.external.redis;

import java.lang.reflect.InvocationTargetException;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.helpers.single.SingleRedisStringHelper;
import devutility.external.redis.models.SentinelRedisInstance;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import devutility.internal.test.BaseTest;
import devutility.internal.util.PropertiesUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * BaseTest
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:29:06
 */
public abstract class BaseTestForDuer extends BaseTest {
	/**
	 * Config file name.
	 */
	private final static String CONFIG_FILE = "config.properties";

	/**
	 * Prefix of config item.
	 */
	private final static String CONFIG_PREFIX = "redis";

	/**
	 * Prefix of config items for sentinel.
	 */
	private final static String CONFIG_PREFIX_SENTINEL = "sentinel";

	/**
	 * Redis test key for stream.
	 */
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
	protected SentinelRedisInstance sentinel_RedisInstance = RedisInstanceUtils.get(CONFIG_FILE, CONFIG_PREFIX_SENTINEL, SentinelRedisInstance.class);

	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption = null;

	public BaseTestForDuer() {
		try {
			redisQueueOption = PropertiesUtils.toModel(CONFIG_FILE, "", RedisQueueOption.class);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
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