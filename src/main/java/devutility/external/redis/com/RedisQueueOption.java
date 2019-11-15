package devutility.external.redis.com;

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
	private int connectionRetryTimes = 3;

	/**
	 * Interval milliseconds between two connection retry, default is 3 seconds.
	 */
	private int connectionRetryInterval = 3000;

	/**
	 * Max length of Redis queue, default is 100000. Only useful for Stream type.
	 */
	private int maxLength = 100000;

	/**
	 * Approximate length for Redis queue. Only useful for Stream
	 */
	private boolean approximateLength;

	/**
	 * Group name, optional field. If this field with null value, system will use default value. Only useful for Stream
	 * type.
	 */
	private String groupName;

	/**
	 * Consumer name, optional field. If this field with null value, system will use default value. Only useful for Stream
	 * type.
	 */
	private String consumerName;

	/**
	 * No need ack after consumer. Only useful for Stream
	 */
	private boolean noNeedAck;

	/**
	 * Automatic ack message.
	 */
	private boolean autoAck;

	/**
	 * Page size for read pending data, default is 1.
	 */
	private int pageSizeForReadPending = 1;

	/**
	 * Queue mode.
	 */
	private QueueMode mode;

	/**
	 * In debug mode, system will print all of debug information. Default is false.
	 */
	private boolean debug;

	/**
	 * Constructor
	 * @param key Redis key of queue.
	 * @param groupName Group name
	 * @param consumerName Consumer name
	 */
	public RedisQueueOption(String key, String groupName, String consumerName) {
		this.key = key;
		this.groupName = groupName;
		this.consumerName = consumerName;
	}

	/**
	 * Constructor
	 * @param key Redis key of queue.
	 * @param database Database of Redis, default is 0.
	 */
	public RedisQueueOption(String key, int database) {
		this.key = key;
		this.database = database;
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

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public boolean isApproximateLength() {
		return approximateLength;
	}

	public void setApproximateLength(boolean approximateLength) {
		this.approximateLength = approximateLength;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public boolean isNoNeedAck() {
		return noNeedAck;
	}

	public void setNoNeedAck(boolean noNeedAck) {
		this.noNeedAck = noNeedAck;
	}

	public boolean isAutoAck() {
		return autoAck;
	}

	public void setAutoAck(boolean autoAck) {
		this.autoAck = autoAck;
	}

	public int getPageSizeForReadPending() {
		return pageSizeForReadPending;
	}

	public void setPageSizeForReadPending(int pageSizeForReadPending) {
		this.pageSizeForReadPending = pageSizeForReadPending;
	}

	public QueueMode getMode() {
		return mode;
	}

	public void setMode(QueueMode mode) {
		this.mode = mode;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}