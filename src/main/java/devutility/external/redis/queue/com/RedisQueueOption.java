package devutility.external.redis.queue.com;

/**
 * 
 * RedisQueueOption
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:03:40
 */
public class RedisQueueOption {
	/**
	 * Redis key of queue.
	 */
	private String key;

	/**
	 * Database of Redis, default is 0.
	 */
	private int database;

	/**
	 * Waiting message time in milliseconds after connect Redis, default is 0 means no time limited.
	 */
	private int waitMilliseconds;

	/**
	 * Connection retry times, default is 3 times.
	 */
	private int connectionRetryTimes;

	/**
	 * Interval milliseconds between two connection retry, default is 3 seconds.
	 */
	private int connectionRetryInterval;

	/**
	 * In debug mode, system will print all of debug information. Default is false.
	 */
	private boolean debug;

	/**
	 * Constructor
	 * @param key Redis key of queue.
	 * @param database Database of Redis, default is 0.
	 */
	public RedisQueueOption(String key, int database) {
		this.key = key;
		this.database = database;
		connectionRetryTimes = 3;
		connectionRetryInterval = 3000;
	}

	/**
	 * Constructor
	 * @param key Redis key of queue.
	 */
	public RedisQueueOption(String key) {
		this(key, 0);
	}

	/**
	 * Constructor
	 */
	public RedisQueueOption() {
		this(null);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public int getWaitMilliseconds() {
		return waitMilliseconds;
	}

	public void setWaitMilliseconds(int waitMilliseconds) {
		this.waitMilliseconds = waitMilliseconds;
	}

	public int getConnectionRetryTimes() {
		return connectionRetryTimes;
	}

	public void setConnectionRetryTimes(int connectionRetryTimes) {
		this.connectionRetryTimes = connectionRetryTimes;
	}

	public int getConnectionRetryInterval() {
		return connectionRetryInterval;
	}

	public void setConnectionRetryInterval(int connectionRetryInterval) {
		this.connectionRetryInterval = connectionRetryInterval;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}