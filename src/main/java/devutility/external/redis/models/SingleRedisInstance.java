package devutility.external.redis.models;

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

	public String cacheKey() {
		return String.format(Config.JEDISPOOL_CACHE_KEY_FORMAT, RedisInstance.class.getName(), getHost(), getPort());
	}
}