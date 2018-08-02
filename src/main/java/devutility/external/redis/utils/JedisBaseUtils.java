package devutility.external.redis.utils;

import java.util.HashSet;
import java.util.Set;

import devutility.external.redis.RedisHelperFactory;
import devutility.external.redis.models.ClusterRedisInstance;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.internal.lang.StringHelper;
import devutility.internal.security.SHA256Utils;
import redis.clients.jedis.HostAndPort;

public abstract class JedisBaseUtils {
	/**
	 * Get key use SingleRedisInstance object.
	 * @param redisInstance: SingleRedisInstance object.
	 * @return String
	 */
	protected static String getKey(SingleRedisInstance redisInstance) {
		return String.format("%s.%s.%d", RedisHelperFactory.class.getName(), redisInstance.getHost(), redisInstance.getPort());
	}

	/**
	 * Get key use ClusterRedisInstance object.
	 * @param redisInstance: ClusterRedisInstance object.
	 * @return String
	 */
	protected static String getKey(ClusterRedisInstance redisInstance) {
		String value = SHA256Utils.encipherToHex(redisInstance.getNodes());
		return String.format("%s.%s", RedisHelperFactory.class.getName(), value);
	}

	/**
	 * Get a set of HostAndPort by nodes.
	 * @param nodes: Valid format as {server}:{port}[,{server}:{port}], this is a string value.
	 * @return Set<HostAndPort>
	 */
	public static Set<HostAndPort> hostAndPortSet(String nodes) {
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
}