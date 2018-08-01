package devutility.external.redis;

import java.io.InputStream;
import java.util.Properties;

import devutility.external.redis.models.ClusterRedisInstance;
import devutility.external.redis.models.RedisInstance;
import devutility.external.redis.models.SentinelRedisInstance;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.internal.lang.ClassHelper;
import devutility.internal.util.PropertiesUtils;

public class RedisInstanceUtils {
	/**
	 * Get a RedisInstance object.
	 * @param properties: Properties object.
	 * @param prefix: Prefix of key.
	 * @return RedisInstance
	 */
	public static RedisInstance get(Properties properties, String prefix) {
		RedisInstance instance = new RedisInstance();
		set(instance, properties, prefix);
		return instance;
	}

	/**
	 * Get a RedisInstance object.
	 * @param inputStream: InputStream object.
	 * @param prefix: Prefix of key.
	 * @return RedisInstance
	 */
	public static RedisInstance get(InputStream inputStream, String prefix) {
		Properties properties = PropertiesUtils.getProperties(inputStream);
		return get(properties, prefix);
	}

	/**
	 * Get a RedisInstance object.
	 * @param propertiesFile: Properties file.
	 * @param prefix: Prefix of key.
	 * @return RedisInstance
	 */
	public static RedisInstance get(String propertiesFile, String prefix) {
		Properties properties = PropertiesUtils.getProperties(propertiesFile);
		return get(properties, prefix);
	}

	/**
	 * Get a redis instance object.
	 * @param propertiesFile: Properties file.
	 * @param prefix: Prefix of key.
	 * @param clazz: Class object.
	 * @return {@code T}
	 */
	public static <T extends RedisInstance> T get(String propertiesFile, String prefix, Class<T> clazz) {
		T instance = ClassHelper.instance(clazz);
		Properties properties = PropertiesUtils.getProperties(propertiesFile);

		if (clazz.equals(RedisInstance.class)) {
			set((RedisInstance) instance, properties, prefix);
		}

		if (clazz.equals(SingleRedisInstance.class)) {
			set((SingleRedisInstance) instance, properties, prefix);
		}

		if (clazz.equals(ClusterRedisInstance.class)) {
			set((ClusterRedisInstance) instance, properties, prefix);
		}

		if (clazz.equals(SentinelRedisInstance.class)) {
			set((SentinelRedisInstance) instance, properties, prefix);
		}

		return instance;
	}

	/**
	 * Set RedisInstance.
	 * @param instance: RedisInstance object.
	 * @param properties: Properties object.
	 * @param prefix: Prefix of key.
	 */
	public static void set(RedisInstance instance, Properties properties, String prefix) {
		instance.setPassword(PropertiesUtils.getProperty(properties, getPropertyKey(prefix, "password")));
		instance.setDatabase(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "database")));
		instance.setMaxConnections(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "max-active")));
		instance.setMinIdle(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "min-idle")));
		instance.setMaxIdle(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "max-idle")));
		instance.setMaxWaitMillis(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "max-wait")));
		instance.setConnectionTimeoutMillis(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "timeout")));
		instance.setCommandTimeout(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "command-timeout")));
		instance.setMaxRetryCount(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "max-retry-count")));
	}

	/**
	 * Set SingleRedisInstance.
	 * @param instance: SingleRedisInstance object.
	 * @param properties: Properties object.
	 * @param prefix: Prefix of key.
	 */
	public static void set(SingleRedisInstance instance, Properties properties, String prefix) {
		set((RedisInstance) instance, properties, prefix);
		instance.setHost(PropertiesUtils.getProperty(properties, getPropertyKey(prefix, "host")));
		instance.setPort(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "port")));
	}

	/**
	 * Set ClusterRedisInstance.
	 * @param instance: ClusterRedisInstance object.
	 * @param properties: Properties object.
	 * @param prefix: Prefix of key.
	 */
	public static void set(ClusterRedisInstance instance, Properties properties, String prefix) {
		set((RedisInstance) instance, properties, prefix);
		instance.setNodes(PropertiesUtils.getProperty(properties, getPropertyKey(prefix, "nodes")));
	}

	/**
	 * Set SentinelRedisInstance.
	 * @param instance: SentinelRedisInstance object.
	 * @param properties: Properties object.
	 * @param prefix: Prefix of key.
	 */
	public static void set(SentinelRedisInstance instance, Properties properties, String prefix) {
		set((ClusterRedisInstance) instance, properties, prefix);
		instance.setMasterName(PropertiesUtils.getProperty(properties, getPropertyKey(prefix, "master-name")));
	}

	/**
	 * Get property key.
	 * @param prefix: Prefix of key.
	 * @param name: Key name.
	 * @return String
	 */
	public static String getPropertyKey(String prefix, String name) {
		return String.format("%s.%s", prefix, name);
	}
}