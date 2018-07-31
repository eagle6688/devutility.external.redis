package devutility.external.redis.utils;

import devutility.external.redis.RedisHelperFactory;
import devutility.external.redis.models.RedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisPoolUtil extends JedisBaseUtils {
	/**
	 * Get a singleton JedisPool object.
	 * @param redisInstance: RedisInstance object.
	 * @return JedisPool
	 */
	public static JedisPool jedisPool(RedisInstance redisInstance) {
		if (redisInstance == null || StringHelper.isNullOrEmpty(redisInstance.getHost())) {
			throw new IllegalArgumentException("Illegal parameter redisInstance!");
		}

		String key = getKey(redisInstance);
		JedisPool jedisPool = SingletonFactory.get(key, JedisPool.class);

		if (jedisPool != null) {
			return jedisPool;
		}

		synchronized (RedisHelperFactory.class) {
			if (jedisPool == null) {
				jedisPool = SingletonFactory.save(key, createJedisPool(redisInstance));
			}
		}

		return jedisPool;
	}

	/**
	 * Create a JedisPool object.
	 * @param redisInstance: RedisInstance object.
	 * @return JedisPool
	 */
	public static JedisPool createJedisPool(RedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = JedisPoolConfigUtil.jedisPoolConfig(redisInstance);

		if (redisInstance.getPort() > 0 && redisInstance.getConnectionTimeoutMillis() != 0 && redisInstance.getPassword() != null) {
			return new JedisPool(jedisPoolConfig, redisInstance.getHost(), redisInstance.getPort(), redisInstance.getConnectionTimeoutMillis(), redisInstance.getPassword());
		}

		if (redisInstance.getPort() > 0 && redisInstance.getConnectionTimeoutMillis() != 0) {
			return new JedisPool(jedisPoolConfig, redisInstance.getHost(), redisInstance.getPort(), redisInstance.getConnectionTimeoutMillis());
		}

		if (redisInstance.getPort() > 0) {
			return new JedisPool(jedisPoolConfig, redisInstance.getHost(), redisInstance.getPort());
		}

		return new JedisPool(jedisPoolConfig, redisInstance.getHost());
	}

	/**
	 * Create a Jedis instance.
	 * @param redisInstance: RedisInstance object
	 * @return Jedis
	 */
	public static Jedis jedis(RedisInstance redisInstance) {
		Jedis jedis = null;
		JedisPool jedisPool = jedisPool(redisInstance);

		if (jedisPool == null) {
			return null;
		}

		for (int i = 0; i < redisInstance.getMaxRetryCount(); i++) {
			try {
				jedis = jedisPool.getResource();

				if (jedisPool != null) {
					jedis.select(redisInstance.getDatabase());
					break;
				}
			} catch (Exception e) {
				if (!(e instanceof JedisConnectionException)) {
					throw e;
				}
			}
		}

		return jedis;
	}
}