package devutility.external.redis.queue;

import java.util.List;

import devutility.external.redis.model.RedisQueueOption;
import devutility.internal.data.converter.Converter;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisQueueProducer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 20:09:12
 */
public abstract class JedisQueueProducer extends JedisQueue {
	/**
	 * Converter for queue value.
	 */
	private Converter<Object, String> converter;

	/**
	 * Constructor
	 * @param redisQueueOption Configuration of Redis queue.
	 * @param converter Converter instance used for transfer object to string which saved in queue. You can compress object
	 *            in instance. System will use toString for this transformation if converter parameter with null.
	 */
	public JedisQueueProducer(RedisQueueOption redisQueueOption, Converter<Object, String> converter) {
		super(redisQueueOption);
		this.converter = converter;
	}

	/**
	 * Convert value to string type and save it into Redis queue.
	 * @param jedis Jedis object.
	 * @param value Redis queue data.
	 * @return Object
	 */
	public abstract Object enqueue(Jedis jedis, final Object value);

	/**
	 * Iterating each item in list object, convert item object to string type and save it into Reids queue. So this method
	 * would save list.size() times into Redis queue.
	 * @param jedis Jedis object.
	 * @param values List value.
	 * @return {@code List<Object>}
	 */
	public abstract List<Object> enqueue(Jedis jedis, final List<?> values);

	/**
	 * Convert value with Object type to String value.
	 * @param value Object value.
	 * @return String
	 */
	protected String convert(Object value) {
		if (converter == null) {
			return value.toString();
		}

		return converter.convert(value);
	}

	public Converter<Object, String> getConverter() {
		return converter;
	}

	public void setConverter(Converter<Object, String> converter) {
		this.converter = converter;
	}
}