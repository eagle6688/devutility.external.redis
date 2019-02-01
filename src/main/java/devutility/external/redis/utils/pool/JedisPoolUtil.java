package devutility.external.redis.utils.pool;

import devutility.external.redis.RedisHelperFactory;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisPoolUtil {
	/**
	 * Get a singleton JedisPool object.
	 * @param redisInstance SingleRedisInstance object.
	 * @return JedisPool
	 */
	public static JedisPool jedisPool(SingleRedisInstance redisInstance) {
		if (redisInstance == null || StringUtils.isNullOrEmpty(redisInstance.getHost())) {
			throw new IllegalArgumentException("Illegal parameter redisInstance!");
		}

		String key = String.format("%s.%s.%d", JedisPoolUtil.class.getName(), redisInstance.getHost(), redisInstance.getPort());
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
	 * @param redisInstance SingleRedisInstance object.
	 * @return JedisPool
	 */
	public static JedisPool createJedisPool(SingleRedisInstance redisInstance) {
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
	 * @param redisInstance SingleRedisInstance object
	 * @return Jedis
	 */
	public static Jedis jedis(SingleRedisInstance redisInstance) {
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