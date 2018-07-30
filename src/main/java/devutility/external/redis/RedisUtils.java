package devutility.external.redis;

import java.util.HashSet;
import java.util.Set;

import devutility.external.redis.helpers.RedisStringHelper;
import devutility.external.redis.models.RedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringHelper;
import devutility.internal.security.SHA256Utils;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUtils {
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

		synchronized (RedisUtils.class) {
			if (jedisPool == null) {
				JedisPoolConfig jedisPoolConfig = jedisPoolConfig(redisInstance);
				jedisPool = SingletonFactory.save(key, new JedisPool(jedisPoolConfig, redisInstance.getHost(), redisInstance.getPort(), redisInstance.getConnectionTimeoutMillis()));
			}
		}

		return jedisPool;
	}

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

		synchronized (RedisUtils.class) {
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
		JedisPoolConfig jedisPoolConfig = jedisPoolConfig(redisInstance);
		Set<HostAndPort> sentinels = clusterNodes(redisInstance.getNodes());
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

		synchronized (RedisUtils.class) {
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
		JedisPoolConfig jedisPoolConfig = jedisPoolConfig(redisInstance);
		Set<HostAndPort> clusterNodes = clusterNodes(redisInstance.getNodes());

		if (redisInstance.getCommandTimeout() != 0) {
			return new JedisCluster(clusterNodes, redisInstance.getCommandTimeout(), jedisPoolConfig);
		}

		return new JedisCluster(clusterNodes, jedisPoolConfig);
	}

	/**
	 * Get key use RedisInstance object.
	 * @param redisInstance: RedisInstance object.
	 * @return String
	 */
	private static String getKey(RedisInstance redisInstance) {
		if (StringHelper.isNotEmpty(redisInstance.getNodes())) {
			String value = SHA256Utils.encipherToHex(redisInstance.getNodes());
			return String.format("%s.%s", RedisUtils.class.getName(), value);
		}

		return String.format("%s.%s.%d", RedisUtils.class.getName(), redisInstance.getHost(), redisInstance.getPort());
	}

	/**
	 * Create a JedisPoolConfig object by RedisInstance
	 * @param redisInstance: RedisInstance object
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

		return jedisPoolConfig;
	}

	/**
	 * Get a set of HostAndPort by nodes.
	 * @param nodes: Valid format as {server}:{port}[,{server}:{port}], this is a
	 *            string value.
	 * @return Set<HostAndPort>
	 */
	public static Set<HostAndPort> clusterNodes(String nodes) {
		if (StringHelper.isNullOrEmpty(nodes)) {
			throw new IllegalArgumentException("Nodes cannot be null!");
		}

		Set<HostAndPort> set = new HashSet<>();
		String[] servers = nodes.split(",");

		for (String server : servers) {
			String[] array = server.split(":");

			if (array.length != 2 || StringHelper.isNullOrEmpty(array[0])) {
				throw new IllegalArgumentException("Invalid nodes format! ");
			}

			int port = Integer.parseInt(array[1]);
			set.add(new HostAndPort(array[0], port));
		}

		return set;
	}

	/**
	 * Create a Jedis instance.
	 * @param redisInstance: RedisInstance object
	 * @return Jedis
	 */
	public static Jedis jedis(RedisInstance redisInstance) {
		Jedis jedis = null;

		int maxRetryCount = 3;
		JedisPool jedisPool = jedisPool(redisInstance);

		if (jedisPool == null) {
			return null;
		}

		for (int i = 0; i < maxRetryCount; i++) {
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

	/**
	 * Create a RedisStringHelper object.
	 * @param propertiesFile: Properties file name.
	 * @param prefix: Prefix name.
	 * @return RedisStringHelper
	 */
	public static RedisStringHelper redisStringHelper(String propertiesFile, String prefix) {
		RedisInstance redisInstance = RedisInstanceUtils.get(propertiesFile, prefix);
		return new RedisStringHelper(redisInstance);
	}
}