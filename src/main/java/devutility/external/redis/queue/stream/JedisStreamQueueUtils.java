package devutility.external.redis.queue.stream;

import redis.clients.jedis.Jedis;

/**
 * 
 * JedisStreamQueueUtils
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 20:33:50
 */
public class JedisStreamQueueUtils {
	/**
	 * Get group name of Jedis Stream queue.
	 * @param jedis Jedis object with selected database index.
	 * @return String
	 */
	public static String getGroupName(Jedis jedis) {
		return null;
	}
}