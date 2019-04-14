package devutility.external.redis.utils.pool;

import java.util.Set;

import devutility.external.redis.RedisHelperFactory;
import devutility.external.redis.RedisInstanceUtils;
import devutility.external.redis.models.ClusterRedisInstance;
import devutility.external.redis.models.SentinelRedisInstance;
import devutility.external.redis.utils.RedisBaseUtils;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringUtils;
import devutility.internal.security.Sha256Utils;
import devutility.internal.util.CollectionUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinelPoolUtil extends RedisBaseUtils {
	/**
	 * Get a singleton JedisSentinelPool object.
	 * @param redisInstance SentinelRedisInstance object.
	 * @return JedisSentinelPool
	 */
	public static JedisSentinelPool jedisSentinelPool(SentinelRedisInstance redisInstance) {
		if (redisInstance == null || StringUtils.isNullOrEmpty(redisInstance.getNodes()) || StringUtils.isNullOrEmpty(redisInstance.getMasterName())) {
			throw new IllegalArgumentException("Illegal parameter redisInstance!");
		}

		String key = getKey(redisInstance);
		JedisSentinelPool jedisSentinelPool = SingletonFactory.get(key, JedisSentinelPool.class);

		if (jedisSentinelPool != null) {
			return jedisSentinelPool;
		}

		synchronized (RedisHelperFactory.class) {
			if (jedisSentinelPool == null) {
				jedisSentinelPool = SingletonFactory.save(key, createJedisSentinelPool(redisInstance));
			}
		}

		return jedisSentinelPool;
	}

	/**
	 * Create a JedisSentinelPool object.
	 * @param redisInstance RedisInstance object.
	 * @return JedisSentinelPool
	 */
	public static JedisSentinelPool createJedisSentinelPool(SentinelRedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = JedisPoolConfigUtil.jedisPoolConfig(redisInstance);
		Set<HostAndPort> sentinels = RedisInstanceUtils.hostAndPortSet(redisInstance.getNodes());
		Set<String> sentinelNodes = CollectionUtils.mapToSet(sentinels, i -> i.toString());

		if (redisInstance.getConnectionTimeoutMillis() != 0 && redisInstance.getPassword() != null) {
			return new JedisSentinelPool(redisInstance.getMasterName(), sentinelNodes, jedisPoolConfig, redisInstance.getConnectionTimeoutMillis(), redisInstance.getPassword());
		}

		if (redisInstance.getConnectionTimeoutMillis() != 0) {
			return new JedisSentinelPool(redisInstance.getMasterName(), sentinelNodes, jedisPoolConfig, redisInstance.getConnectionTimeoutMillis());
		}

		if (redisInstance.getPassword() != null) {
			return new JedisSentinelPool(redisInstance.getMasterName(), sentinelNodes, jedisPoolConfig, redisInstance.getPassword());
		}

		return new JedisSentinelPool(redisInstance.getMasterName(), sentinelNodes, jedisPoolConfig);
	}

	/**
	 * Get key use ClusterRedisInstance object.
	 * @param redisInstance ClusterRedisInstance object.
	 * @return String
	 */
	private static String getKey(ClusterRedisInstance redisInstance) {
		String value = Sha256Utils.encipherToHex(redisInstance.getNodes());
		return String.format("%s.%s", JedisSentinelPoolUtil.class.getName(), value);
	}
}