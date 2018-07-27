package devutility.external.redis;

import devutility.external.redis.helpers.RedisStringHelper;
import devutility.internal.base.SingletonFactory;
import devutility.internal.dao.RedisInstanceUtils;
import devutility.internal.dao.models.RedisInstance;
import devutility.internal.lang.StringHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
	/**
	 * Create a JedisPoolConfig object by RedisInstance
	 * @param redisInstance: RedisInstance object
	 * @return JedisPoolConfig
	 */
	public static JedisPoolConfig jedisPoolConfig(RedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(redisInstance.getMaxConnections());
		return jedisPoolConfig;
	}

	/**
	 * Create a singleton JedisPool object.
	 * @param redisInstance: RedisInstance object
	 * @return JedisPool
	 */
	public static JedisPool jedisPool(RedisInstance redisInstance) {
		if (redisInstance == null || StringHelper.isNullOrEmpty(redisInstance.getHost())) {
			return null;
		}

		String key = String.format("%s.%s", JedisPool.class.getName(), redisInstance.getHost());
		JedisPool jedisPool = SingletonFactory.get(key, JedisPool.class);

		if (jedisPool != null) {
			return jedisPool;
		}

		synchronized (RedisUtils.class) {
			if (jedisPool == null) {
				JedisPoolConfig jedisPoolConfig = jedisPoolConfig(redisInstance);
				jedisPool = SingletonFactory.save(key, new JedisPool(jedisPoolConfig, redisInstance.getHost(), redisInstance.getPort(), redisInstance.getTimeout()));
			}
		}

		return jedisPool;
	}

	/**
	 * Create a Jedis instance.
	 * @param redisInstance: RedisInstance object
	 * @return Jedis
	 * @throws NullPointerException
	 */
	public static Jedis jedis(RedisInstance redisInstance) throws NullPointerException {
		JedisPool jedisPool = jedisPool(redisInstance);

		if (jedisPool == null) {
			return null;
		}

		Jedis jedis = jedisPool.getResource();

		if (jedis != null) {
			jedis.select(redisInstance.getDBIndex());
		}

		return jedis;
	}

	/**
	 * Create a RedisStringHelper object.
	 * @param propertiesFile: Properties file name.
	 * @param prefix: Prefix name.
	 * @return RedisStringHelper
	 */
	public static RedisStringHelper redisStringHelper(String propertiesFile, String prefix) {
		RedisInstance redisInstance = RedisInstanceUtils.getInstance(propertiesFile, prefix);
		return new RedisStringHelper(redisInstance);
	}
}