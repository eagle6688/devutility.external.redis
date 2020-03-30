package devutility.external.redis;

import devutility.external.redis.helper.single.SingleJedisStringHelper;
import devutility.external.redis.model.RedisQueueOption;
import devutility.external.redis.model.SentinelRedisInstance;
import devutility.external.redis.model.SingleRedisInstance;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import devutility.internal.test.BaseTest;
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
	 * SingleRedisInstance object.
	 */
	protected SingleRedisInstance singleRedisInstance = ConfigUtils.getRedisInstanceFromResources(CONFIG_FILE, "redis", SingleRedisInstance.class);

	/**
	 * SingleRedisStringHelper object.
	 */
	protected SingleJedisStringHelper singleRedisStringHelper = new SingleJedisStringHelper(singleRedisInstance);

	/**
	 * SentinelRedisInstance object.
	 */
	protected SentinelRedisInstance sentinelRedisInstance = ConfigUtils.getRedisInstanceFromResources(CONFIG_FILE, "sentinel", SentinelRedisInstance.class);

	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption = ConfigUtils.getRedisQueueOptionFromResources(CONFIG_FILE, "queue.option");

	/**
	 * SingleRedisInstance object.
	 */
	protected SingleRedisInstance singleRedisInstance2 = ConfigUtils.getRedisInstanceFromResources(CONFIG_FILE, "redis2", SingleRedisInstance.class);

	/**
	 * RedisQueueOption object.
	 */
	protected RedisQueueOption redisQueueOption2 = ConfigUtils.getRedisQueueOptionFromResources(CONFIG_FILE, "queue2.option");

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