package devutility.external.redis.utils;

import java.util.Set;

import devutility.external.redis.RedisHelperFactory;
import devutility.external.redis.models.RedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringHelper;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinelPoolUtil extends JedisBaseUtils {
	/**
	 * Get a singleton JedisSentinelPool object.
	 * @param redisInstance: RedisInstance object.
	 * @return JedisSentinelPool
	 */
	public static JedisSentinelPool jedisSentinelPool(RedisInstance redisInstance) {
		if (redisInstance == null || StringHelper.isNullOrEmpty(redisInstance.getNodes())) {
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
	 * @param redisInstance: RedisInstance object.
	 * @return JedisSentinelPool
	 */
	public static JedisSentinelPool createJedisSentinelPool(RedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = JedisPoolConfigUtil.jedisPoolConfig(redisInstance);
		Set<HostAndPort> sentinels = hostAndPortSet(redisInstance.getNodes());
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
}