package devutility.external.redis.model;

import devutility.external.redis.com.Config;

/**
 * 
 * SingleRedisInstance
 * 
 * @author: Aldwin Su
 * @version: 2019-09-26 15:59:04
 */
public class SingleRedisInstance extends RedisInstance {
	/**
	 * Redis server host.
	 */
	private String host;

	/**
	 * Redis server port.
	 */
	private int port;

	/**
	 * Database index.
	 */
	private int database = 0;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		if (database < 0) {
			return;
		}

		this.database = database;
	}

	public String cacheKey() {
		return String.format(Config.JEDISPOOL_CACHE_KEY_FORMAT, RedisInstance.class.getName(), getHost(), getPort());
	}
}