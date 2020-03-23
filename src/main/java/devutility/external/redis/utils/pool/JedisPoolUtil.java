package devutility.external.redis.utils.pool;

import devutility.external.redis.com.StatusCode;
import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.model.SingleRedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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

	/**
	 * Create a Jedis instance.
	 * @param redisInstance SingleRedisInstance object
	 * @return Jedis
	 */
	public static Jedis jedis(SingleRedisInstance redisInstance) {
		JedisPool jedisPool = jedisPool(redisInstance);
		return jedis(jedisPool, redisInstance.getDatabase());
	}
}