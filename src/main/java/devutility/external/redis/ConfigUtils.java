package devutility.external.redis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import devutility.external.redis.model.RedisInstance;
import devutility.external.redis.model.RedisQueueOption;
import devutility.internal.lang.StringUtils;
import devutility.internal.util.PropertiesUtils;
import redis.clients.jedis.HostAndPort;

/**
 * 
 * ConfigUtils
 * 
 * @author: Aldwin Su
 * @version: 2020-03-27 14:56:52
 */
public class ConfigUtils {
	/**
	 * Get an RedisInstance object or its subtype object from provided Properties object.
	 * @param properties Properties object.
	 * @param prefix Prefix of key.
	 * @param clazz Class object.
	 * @return {@code T}
	 */
	public static <T extends RedisInstance> T getRedisInstance(Properties properties, String prefix, Class<T> clazz) {
		try {
			return PropertiesUtils.toObject(properties, prefix, clazz);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get an RedisInstance object or its subtype object from provided Properties file.
	 * @param file Properties file located in resources folder.
	 * @param prefix Prefix of key.
	 * @param clazz Class object.
	 * @return {@code: T}
	 */
	public static <T extends RedisInstance> T getRedisInstanceFromResources(String file, String prefix, Class<T> clazz) {
		try {
			return PropertiesUtils.toObjectFromResource(file, prefix, clazz);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get an RedisQueueOption object from provided Properties file.
	 * @param properties Properties object.
	 * @param prefix Prefix of key.
	 * @return RedisQueueOption
	 */
	public static RedisQueueOption getRedisQueueOption(Properties properties, String prefix) {
		try {
			return PropertiesUtils.toObject(properties, prefix, RedisQueueOption.class);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get an RedisQueueOption object from provided Properties file.
	 * @param file Properties file located in resources folder.
	 * @param prefix Prefix of key.
	 * @return RedisQueueOption
	 */
	public static RedisQueueOption getRedisQueueOptionFromResources(String file, String prefix) {
		try {
			return PropertiesUtils.toObjectFromResource(file, prefix, RedisQueueOption.class);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get a set of HostAndPort by nodes.
	 * @param nodes Valid format as {server}:{port}[,{server}:{port}], this is a string value.
	 * @return {@code: Set<HostAndPort>}
	 */
	public static Set<HostAndPort> hostAndPortSet(String nodes) {
		if (StringUtils.isNullOrEmpty(nodes)) {
			throw new IllegalArgumentException("Nodes cannot be null!");
		}

		Set<HostAndPort> set = new HashSet<>();
		String[] servers = nodes.split(",");

		for (String server : servers) {
			String[] array = server.split(":");

			if (array.length != 2 || StringUtils.isNullOrEmpty(array[0])) {
				throw new IllegalArgumentException("Invalid nodes format! ");
			}

			int port = Integer.parseInt(array[1]);
			set.add(new HostAndPort(array[0], port));
		}

		return set;
	}
}