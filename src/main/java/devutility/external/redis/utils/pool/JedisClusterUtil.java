package devutility.external.redis.utils.pool;

import java.util.Set;

import devutility.external.redis.ConfigUtils;
import devutility.external.redis.model.ClusterRedisInstance;
import devutility.external.redis.utils.JedisUtils;
import devutility.internal.com.SingletonFactory;
import devutility.internal.lang.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterUtil extends JedisUtils {
	/**
	 * Get a singleton JedisCluster object.
	 * @param redisInstance ClusterRedisInstance object.
	 * @return JedisCluster
	 */
	public static JedisCluster jedisCluster(ClusterRedisInstance redisInstance) {
		if (redisInstance == null || StringUtils.isNullOrEmpty(redisInstance.getNodes())) {
			throw new IllegalArgumentException("Illegal parameter redisInstance!");
		}

		String key = getCacheKeyForClusterRedis(redisInstance);
		JedisCluster jedisCluster = SingletonFactory.get(key, JedisCluster.class);

		if (jedisCluster != null) {
			return jedisCluster;
		}

		synchronized (JedisClusterUtil.class) {
			if (jedisCluster == null) {
				jedisCluster = SingletonFactory.save(key, createJedisCluster(redisInstance));
			}
		}

		return jedisCluster;
	}

	/**
	 * Create a JedisCluster object.
	 * @param redisInstance ClusterRedisInstance object.
	 * @return JedisCluster
	 */
	public static JedisCluster createJedisCluster(ClusterRedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = JedisPoolConfigUtil.jedisPoolConfig(redisInstance);
		Set<HostAndPort> clusterNodes = ConfigUtils.hostAndPortSet(redisInstance.getNodes());

		if (redisInstance.getCommandTimeout() != 0) {
			return new JedisCluster(clusterNodes, redisInstance.getCommandTimeout(), jedisPoolConfig);
		}

		return new JedisCluster(clusterNodes, jedisPoolConfig);
	}
}