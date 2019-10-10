package devutility.external.redis.queue.stream;

import java.util.LinkedHashMap;
import java.util.Map;

import devutility.external.redis.queue.JedisQueueProducer;
import redis.clients.jedis.Jedis;

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
		map.put("", convert(value));
		return jedis.xadd(key, null, map);
	}
}