package devutility.external.redis.queue.list;

import devutility.external.redis.queue.JedisQueueProducer;
import redis.clients.jedis.Jedis;

public class JedisListQueueProducer extends JedisQueueProducer {
	@Override
	public Object enqueue(Jedis jedis, String key, Object value) {
		return null;
	}
}