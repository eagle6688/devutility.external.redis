package devutility.external.redis.com;

/**
 * 
 * Config, some constant values.
 * 
 * @author: Aldwin Su
 * @version: 2019-09-26 15:56:45
 */
public class Config {
	/**
	 * JedisPool cache key format. {@code First %s is class name, the second %s is host value, the third %d is port number}
	 */
	public final static String JEDISPOOL_CACHE_KEY_FORMAT = "%s.%s.%d";

	/**
	 * Item name in queue.
	 */
	public final static String QUEUE_ITEM_NAME = "item";

	/**
	 * Default group name in queue.
	 */
	public final static String QUEUE_DEFAULT_GROUP_NAME = "group-default";

	/**
	 * Default consumer name in group.
	 */
	public final static String QUEUE_DEFAULT_CONSUMER_NAME = "consumer-default";
}