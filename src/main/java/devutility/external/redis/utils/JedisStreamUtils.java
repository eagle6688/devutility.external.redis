package devutility.external.redis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * JedisStreamQueueUtils
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 20:33:50
 */
public class JedisStreamUtils {
	/**
	 * Convert Redis response to String type.
	 * @param data Redis response data.
	 * @return String
	 */
	public static String convertToString(Object data) {
		if (data instanceof byte[]) {
			return SafeEncoder.encode((byte[]) data);
		}

		if (data instanceof Long) {
			return String.valueOf(data);
		}

		return null;
	}

	/**
	 * Get group name of Jedis Stream queue.
	 * @param jedis Jedis object with selected database index.
	 * @return String
	 */
	public static String getGroupName(Jedis jedis) {
		return null;
	}
}