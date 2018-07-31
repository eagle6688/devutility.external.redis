package devutility.external.redis.models;

public class RedisInstance {
	/**
	 * Redis server host.
	 */
	private String host;

	/**
	 * Redis server port.
	 */
	private int port;

	/**
	 * Redis server password.
	 */
	private String password;

	/**
	 * Name of master Redis server.
	 */
	private String masterName;

	/**
	 * Redis cluster servers.
	 */
	private String nodes;

	/**
	 * Database index.
	 */
	private int database = 0;

	/**
	 * Max count of connections for redis, -1 is not limitation, default value is
	 * 8.
	 */
	private int maxConnections;

	/**
	 * Min count of idle connections for redis.
	 */
	private int minIdle;

	/**
	 * Max count of idle connections for redis, default value is 8.
	 */
	private int maxIdle;

	/**
	 * Max waiting milliseconds when get a redis connection, -1 is not limitation.
	 */
	private long maxWaitMillis;

	/**
	 * Timeout milliseconds for redis connection.
	 */
	private int connectionTimeoutMillis;

	/**
	 * Timeout milliseconds for command.
	 */
	private int commandTimeout;

	/**
	 * Max retry count after get Jedis failed.
	 */
	private int maxRetryCount = 3;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public int getConnectionTimeoutMillis() {
		return connectionTimeoutMillis;
	}

	public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
		this.connectionTimeoutMillis = connectionTimeoutMillis;
	}

	public int getCommandTimeout() {
		return commandTimeout;
	}

	public void setCommandTimeout(int commandTimeout) {
		this.commandTimeout = commandTimeout;
	}

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}
}