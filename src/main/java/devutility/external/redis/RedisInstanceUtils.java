package devutility.external.redis;

import java.io.InputStream;
import java.util.Properties;

import devutility.external.redis.models.RedisInstance;
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
	 * @param propertiesFile: File of properties.
	 * @param prefix: Prefix of key.
	 * @return RedisInstance
	 */
	public static RedisInstance get(String propertiesFile, String prefix) {
		Properties properties = PropertiesUtils.getProperties(propertiesFile);
		return get(properties, prefix);
	}

	/**
	 * Set RedisInstance.
	 * @param instance: RedisInstance object.
	 * @param properties: Properties object.
	 * @param prefix: Prefix of key.
	 */
	public static void set(RedisInstance instance, Properties properties, String prefix) {
		instance.setHost(PropertiesUtils.getProperty(properties, getPropertyKey(prefix, "host")));
		instance.setPort(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "port")));
		instance.setPassword(PropertiesUtils.getProperty(properties, getPropertyKey(prefix, "password")));
		instance.setNodes(PropertiesUtils.getProperty(properties, getPropertyKey(prefix, "nodes")));
		instance.setDatabase(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "database")));
		instance.setMaxConnections(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "max-active")));
		instance.setMinIdle(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "min-idle")));
		instance.setMaxIdle(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "max-idle")));
		instance.setMaxWaitMillis(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "max-wait")));
		instance.setConnectionTimeoutMillis(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "timeout")));
		instance.setCommandTimeout(PropertiesUtils.getIntProperty(properties, getPropertyKey(prefix, "commandTimeout")));
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