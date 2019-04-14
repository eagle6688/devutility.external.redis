package devutility.external.redis.utils.pool;

import java.util.Set;

import devutility.external.redis.RedisHelperFactory;
import devutility.external.redis.RedisInstanceUtils;
import devutility.external.redis.models.ClusterRedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringUtils;
import devutility.internal.security.Sha256Utils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterUtil {
	/**
	 * Get a singleton JedisCluster object.
	 * @param redisInstance ClusterRedisInstance object.
	 * @return JedisCluster
	 */
	public static JedisCluster jedisCluster(ClusterRedisInstance redisInstance) {
		if (redisInstance == null || StringUtils.isNullOrEmpty(redisInstance.getNodes())) {
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
	 * @param redisInstance ClusterRedisInstance object.
	 * @return JedisCluster
	 */
	public static JedisCluster createJedisCluster(ClusterRedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = JedisPoolConfigUtil.jedisPoolConfig(redisInstance);
		Set<HostAndPort> clusterNodes = RedisInstanceUtils.hostAndPortSet(redisInstance.getNodes());

		if (redisInstance.getCommandTimeout() != 0) {
			return new JedisCluster(clusterNodes, redisInstance.getCommandTimeout(), jedisPoolConfig);
		}

		return new JedisCluster(clusterNodes, jedisPoolConfig);
	}

	/**
	 * Get key use ClusterRedisInstance object.
	 * @param redisInstance ClusterRedisInstance object.
	 * @return String
	 */
	private static String getKey(ClusterRedisInstance redisInstance) {
		String value = Sha256Utils.encipherToHex(redisInstance.getNodes());
		return String.format("%s.%s", JedisClusterUtil.class.getName(), value);
	}
}