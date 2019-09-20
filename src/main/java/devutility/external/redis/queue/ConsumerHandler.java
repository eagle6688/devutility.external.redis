package devutility.external.redis.queue;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;

/**
 * 
 * Consumer handler for Redis queue.
 * 
 * @author: Aldwin Su
 * @version: 2019-09-20 17:12:56
 */
public class ConsumerHandler implements Runnable, Closeable {
	/**
	 * Jedis object to read data from Redis.
	 */
	private Jedis jedis;

	/**
	 * Custom consumer implementation.
	 */
	private Consumer consumer;

	/**
	 * Redis key for save queue.
	 */
	private String key;

	/**
	 * Timeout of Jedis connection.
	 */
	private int timeout;

	/**
	 * Constructor
	 * @param jedis Jedis object.
	 * @param consumer Custom consumer implementation.
	 * @param key Redis key to save queue.
	 * @param timeout Timeout of Jedis connection.
	 */
	public ConsumerHandler(Jedis jedis, Consumer consumer, String key, int timeout) {
		this.jedis = jedis;
		this.consumer = consumer;
		this.key = key;
		this.timeout = timeout;
	}

	/**
	 * Constructor
	 * @param jedis Jedis object.
	 * @param consumer Custom consumer implementation.
	 * @param key Redis key to save queue.
	 */
	public ConsumerHandler(Jedis jedis, Consumer consumer, String key) {
		this(jedis, consumer, key, 0);
	}

	private void process() {
		if (!jedis.isConnected()) {
			jedis.connect();
		}

		List<String> list = jedis.brpop(timeout, key);

		if (CollectionUtils.isNullOrEmpty(list)) {
			return;
		}

		callback(list.get(0), list.get(1));
	}

	private void callback(String key, String value) {
		if (consumer == null) {
			return;
		}

		consumer.onMessage(key, value);
	}

	@Override
	public void run() {
		while (true) {
			try {
				process();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {
		jedis.close();
	}
}