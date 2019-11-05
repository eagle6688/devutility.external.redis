package devutility.external.redis.queue.list;

import java.util.List;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.queue.JedisQueueProducer;
import devutility.internal.data.converter.Converter;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisListQueueProducer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-12 23:54:18
 */
public class JedisListQueueProducer extends JedisQueueProducer {
	/**
	 * Constructor
	 * @param redisQueueOption Configuration of Redis queue.
	 * @param converter Converter instance used for transfer object to string which saved in queue. You can compress object
	 *            in instance. System will use toString for this transformation if converter parameter with null.
	 */
	public JedisListQueueProducer(RedisQueueOption redisQueueOption, Converter<Object, String> converter) {
		super(redisQueueOption, converter);
	}

	/**
	 * Constructor
	 * @param redisQueueOption Configuration of Redis queue.
	 */
	public JedisListQueueProducer(RedisQueueOption redisQueueOption) {
		this(redisQueueOption, null);
	}

	@Override
	public List<Object> enqueue(Jedis jedis, List<?> list) {
		return null;
	}

	@Override
	public Object enqueue(Jedis jedis, Object value) {
		return jedis.lpush(redisQueueOption.getKey(), convert(value));
	}
}