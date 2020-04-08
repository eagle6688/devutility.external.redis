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
	 * Cache key format for JedisPool.
	 * {@code First %s is class name, the second %s is host value, the third %d is port number}
	 */
	public final static String CACHE_KEY_FORMAT_JEDISPOOL = "%s.%s.%d";

	/**
	 * Default item key in stream queue.
	 */
	public final static String QUEUE_DEFAULT_ITEM_KEY = "du-data";

	/**
	 * Default group name in queue.
	 */
	public final static String QUEUE_DEFAULT_GROUP_NAME = "group-default";
}