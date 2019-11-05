package devutility.external.redis.queue.stream;

import java.util.LinkedHashMap;
import java.util.Map;

import devutility.external.redis.com.Config;
import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.queue.JedisQueueProducer;
import devutility.internal.data.converter.Converter;
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
	/**
	 * Constructor
	 * @param redisQueueOption RedisQueueOption object.
	 * @param converter Converter instance used for transfer object to string which saved in queue. You can compress object
	 *            in instance. System will use toString for this transformation if converter parameter with null.
	 */
	public JedisStreamQueueProducer(RedisQueueOption redisQueueOption, Converter<Object, String> converter) {
		super(redisQueueOption, converter);
	}

	/**
	 * Constructor
	 * @param redisQueueOption RedisQueueOption object.
	 */
	public JedisStreamQueueProducer(RedisQueueOption redisQueueOption) {
		this(redisQueueOption, null);
	}

	@Override
	public Object enqueue(Jedis jedis, Object value) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(Config.QUEUE_ITEM_NAME, convert(value));
		return enqueue(jedis, map);
	}

	public StreamEntryID enqueue(Jedis jedis, final Map<String, String> hash) {
		return jedis.xadd(redisQueueOption.getKey(), null, hash, redisQueueOption.getMaxLength(), redisQueueOption.isApproximateLength());
	}
}