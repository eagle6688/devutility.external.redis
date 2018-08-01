package devutility.external.redis.models;

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
}