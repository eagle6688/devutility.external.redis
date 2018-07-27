package devutility.external.redis;

import java.util.HashSet;
import java.util.Set;

import devutility.external.redis.helpers.RedisStringHelper;
import devutility.external.redis.models.RedisInstance;
import devutility.internal.base.SingletonFactory;
import devutility.internal.lang.StringHelper;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
	/**
	 * Create a singleton JedisPool object.
	 * @param redisInstance: RedisInstance object.
	 * @return JedisPool
	 */
	public static JedisPool jedisPool(RedisInstance redisInstance) {
		if (redisInstance == null || StringHelper.isNullOrEmpty(redisInstance.getHost())) {
			return null;
		}

		String key = String.format("%s.%s", JedisPool.class.getName(), redisInstance.getHost());
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
	 * @throws NullPointerException
	 */
	public static Jedis jedis(RedisInstance redisInstance) throws NullPointerException {
		JedisPool jedisPool = jedisPool(redisInstance);

		if (jedisPool == null) {
			return null;
		}

		Jedis jedis = jedisPool.getResource();

		if (jedis != null) {
			jedis.select(redisInstance.getDatabase());
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
		//return new RedisStringHelper(redisInstance);
		return null;
	}
}