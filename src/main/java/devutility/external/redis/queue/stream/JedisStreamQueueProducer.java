package devutility.external.redis.queue.stream;

import java.util.LinkedHashMap;
import java.util.Map;

import devutility.external.redis.com.Config;
import devutility.external.redis.queue.JedisQueueProducer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * JedisStreamQueueProducer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 20:08:50
 */
public class JedisStreamQueueProducer extends JedisQueueProducer {
	@Override
	public Object enqueue(Jedis jedis, String key, Object value) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(Config.QUEUE_ITEM_NAME, convert(value));
		return jedis.xadd(key, null, map);
	}

	public StreamEntryID xadd(Jedis jedis, final String key, final Map<String, String> hash, final long maxLen, final boolean approximateLength) {
		return jedis.xadd(key, null, hash, maxLen, approximateLength);
	}

	public StreamEntryID xadd(Jedis jedis, final String key, final Map<String, String> hash, final long maxLen) {
		return xadd(jedis, key, hash, maxLen, false);
	}
}