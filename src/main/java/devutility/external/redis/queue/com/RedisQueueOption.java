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

	public RedisQueueOption(String key) {
		this.key = key;
		connectionRetryTimes = 3;
		connectionRetryInterval = 3000;
	}

	public RedisQueueOption() {
		this(null);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
}