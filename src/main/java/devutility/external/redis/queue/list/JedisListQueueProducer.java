package devutility.external.redis.queue.list;

import devutility.external.redis.queue.JedisQueueProducer;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisListQueueProducer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-12 23:54:18
 */
public class JedisListQueueProducer extends JedisQueueProducer {
	@Override
	public Object enqueue(Jedis jedis, String key, Object value) {
		return jedis.lpush(key, convert(value));
	}
}