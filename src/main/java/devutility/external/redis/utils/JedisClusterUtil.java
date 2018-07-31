package devutility.external.redis.utils;

import java.util.Set;

import devutility.external.redis.RedisHelperFactory;
import devutility.external.redis.models.RedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringHelper;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterUtil extends JedisBaseUtils {
	/**
	 * Get a singleton JedisCluster object.
	 * @param redisInstance: RedisInstance object.
	 * @return JedisCluster
	 */
	public static JedisCluster jedisCluster(RedisInstance redisInstance) {
		if (redisInstance == null || StringHelper.isNullOrEmpty(redisInstance.getNodes())) {
			throw new IllegalArgumentException("Illegal parameter redisInstance!");
		}

		String key = getKey(redisInstance);
		JedisCluster jedisCluster = SingletonFactory.get(key, JedisCluster.class);

		if (jedisCluster != null) {
			return jedisCluster;
		}

		synchronized (RedisHelperFactory.class) {
			if (jedisCluster == null) {
				jedisCluster = SingletonFactory.save(key, createJedisCluster(redisInstance));
			}
		}

		return jedisCluster;
	}

	/**
	 * Create a JedisCluster object.
	 * @param redisInstance: RedisInstance object.
	 * @return JedisCluster
	 */
	public static JedisCluster createJedisCluster(RedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = JedisPoolConfigUtil.jedisPoolConfig(redisInstance);
		Set<HostAndPort> clusterNodes = hostAndPortSet(redisInstance.getNodes());

		if (redisInstance.getCommandTimeout() != 0) {
			return new JedisCluster(clusterNodes, redisInstance.getCommandTimeout(), jedisPoolConfig);
		}

		return new JedisCluster(clusterNodes, jedisPoolConfig);
	}
}