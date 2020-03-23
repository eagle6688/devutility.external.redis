package devutility.external.redis.utils.pool;

import devutility.external.redis.models.RedisInstance;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolConfigUtil {
	/**
	 * Create a JedisPoolConfig object by RedisInstance
	 * @param redisInstance RedisInstance object
	 * @return JedisPoolConfig
	 */
	public static JedisPoolConfig jedisPoolConfig(RedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

		if (redisInstance.getMaxConnections() != 0) {
			jedisPoolConfig.setMaxTotal(redisInstance.getMaxConnections());
		}

		if (redisInstance.getMinIdle() != 0) {
			jedisPoolConfig.setMinIdle(redisInstance.getMinIdle());
		}

		if (redisInstance.getMaxIdle() != 0) {
			jedisPoolConfig.setMaxIdle(redisInstance.getMaxIdle());
		}

		if (redisInstance.getMaxWaitMillis() != 0) {
			jedisPoolConfig.setMaxWaitMillis(redisInstance.getMaxWaitMillis());
		}

		jedisPoolConfig.setTestOnBorrow(redisInstance.isTestOnBorrow());
		return jedisPoolConfig;
	}
}