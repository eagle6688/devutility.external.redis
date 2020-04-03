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
	protected final static String CONFIG_FILE = "config.properties";

	/**
	 * Prefix of config item.
	 */
	protected final static String CONFIG_PREFIX = "redis";

	/**
	 * Prefix of config items for sentinel.
	 */
	protected final static String CONFIG_PREFIX_SENTINEL = "sentinel";

	/**
	 * JedisPool object.
	 */
	protected static JedisPool JEDIS_POOL = null;

	/**
	 * SingleRedisInstance object.
	 */
	protected SingleRedisInstance singleRedisInstance = RedisInstanceUtils.get(CONFIG_FILE, CONFIG_PREFIX, SingleRedisInstance.class);

	/**
	 * SingleRedisInstance object.
	 */
	protected SingleRedisInstance singleRedisInstance2 = RedisInstanceUtils.get(CONFIG_FILE, "redis2", SingleRedisInstance.class);

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
	protected RedisQueueOption redisQueueOption = new RedisQueueOption();

	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption2 = null;

	public BaseTestForDuer() {
		redisQueueOption.setKey("QUEUE-PART_ORDER-EMAIL");
		redisQueueOption.setDatabase(0);
		redisQueueOption.setApproximateLength(true);
		redisQueueOption.setGroupName("DEFAULT-GROUP");
		redisQueueOption.setConsumerName("Hyperscale-Email-Consumer1");
		redisQueueOption.setAutoAck(true);
		redisQueueOption.setWaitMilliseconds(3000);

		redisQueueOption2 = redisQueueOption("queue2.option");

		JEDIS_POOL = jedisPool();
	}

	/**
	 * Get RedisQueueOption object.
	 * @param prefix Prefix of
	 * @return RedisQueueOption
	 */
	protected RedisQueueOption redisQueueOption(String prefix) {
		try {
			return PropertiesUtils.toModel(CONFIG_FILE, prefix, RedisQueueOption.class);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Create an JedisPool object use singleRedisInstance;
	 * @param redisInstance SingleRedisInstance object.
	 * @return JedisPool
	 */
	protected JedisPool jedisPool(SingleRedisInstance redisInstance) {
		return JedisPoolUtil.jedisPool(redisInstance);
	}

	/**
	 * Create an JedisPool object use singleRedisInstance;
	 * @return JedisPool
	 */
	protected JedisPool jedisPool() {
		return jedisPool(singleRedisInstance);
	}

	/**
	 * Get Jedis object from JedisPool.
	 * @param redisInstance SingleRedisInstance object.
	 * @return Jedis
	 */
	protected Jedis jedis(SingleRedisInstance redisInstance) {
		return JedisPoolUtil.jedis(redisInstance);
	}

	/**
	 * Get Jedis object from JedisPool.
	 * @return Jedis
	 */
	protected Jedis jedis() {
		return jedis(singleRedisInstance);
	}
}