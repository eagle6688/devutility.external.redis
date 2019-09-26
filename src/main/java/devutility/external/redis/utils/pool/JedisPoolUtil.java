package devutility.external.redis.utils.pool;

import devutility.external.redis.com.StatusCode;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.external.redis.queue.com.JedisFatalException;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 
 * JedisPoolUtil
 * 
 * @author: Aldwin Su
 * @version: 2019-09-26 15:35:18
 */
public class JedisPoolUtil {
	/**
	 * Create an JedisPool object use SingleRedisInstance object.
	 * @param redisInstance SingleRedisInstance object.
	 * @return JedisPool
	 */
	public static JedisPool jedisPool(SingleRedisInstance redisInstance) {
		if (redisInstance == null || StringUtils.isNullOrEmpty(redisInstance.getHost())) {
			throw new IllegalArgumentException("Illegal SingleRedisInstance object, host can't be null!");
		}

		String key = redisInstance.cacheKey();
		JedisPool jedisPool = SingletonFactory.get(key, JedisPool.class);

		if (jedisPool != null) {
			return jedisPool;
		}

		synchronized (JedisPoolUtil.class) {
			if (jedisPool == null) {
				jedisPool = SingletonFactory.save(key, createJedisPool(redisInstance));
			}
		}

		return jedisPool;
	}

	/**
	 * Create an JedisPool object.
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

				if (jedis != null) {
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

	/**
	 * Get Jedis object from provided JedisPool object.
	 * @param jedisPool JedisPool object.
	 * @param database Database number in Reids.
	 * @return Jedis
	 */
	public static Jedis jedis(JedisPool jedisPool, int database) {
		Jedis jedis = jedisPool.getResource();

		if (jedis == null) {
			throw new JedisFatalException("Can't get Jedis resource from JedisPool object!");
		}

		if (!StatusCode.isOk(jedis.select(database))) {
			throw new JedisFatalException("Jedis object can't select database!");
		}

		return jedis;
	}
}