package devutility.external.redis.queue;

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
	 * Enqueue
	 * @param jedis Jedis object.
	 * @param key Key of Redis queue.
	 * @param value Redis queue item.
	 * @return Object
	 */
	public abstract Object enqueue(Jedis jedis, String key, Object value);

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

	public Converter<?, String> getConverter() {
		return converter;
	}

	public void setConverter(Converter<Object, String> converter) {
		this.converter = converter;
	}
}