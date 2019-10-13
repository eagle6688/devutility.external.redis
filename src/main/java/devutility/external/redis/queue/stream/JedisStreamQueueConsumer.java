package devutility.external.redis.queue.stream;

import java.io.IOException;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisStreamQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 20:03:42
 */
public class JedisStreamQueueConsumer extends JedisQueueConsumer {
	/**
	 * Jedis object to read data from Redis.
	 */
	private Jedis jedis;

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param consumerEvent Custom consumer event implementation.
	 * @param redisQueueOption RedisQueueOption object.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, JedisQueueConsumerEvent consumerEvent, RedisQueueOption redisQueueOption) {
		super(consumerEvent, redisQueueOption);
		this.jedis = jedis;
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param consumerEvent Custom consumer event implementation.
	 * @param key Redis key of queue.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, JedisQueueConsumerEvent consumerEvent, String key) {
		this(jedis, consumerEvent, new RedisQueueOption(key));
	}

	@Override
	public void listen() throws Exception {
		if (jedis == null) {
			throw new IllegalArgumentException("jedis can't be null!");
		}
	}

	/**
	 * Main process.
	 * @throws InterruptedException
	 */
	private void process() throws InterruptedException {
		connect(jedis);
//		List<Entry<String, List<StreamEntry>>> list = jedis.xreadGroup(groupname, consumer, count, getRedisQueueOption().getWaitMilliseconds(), noAck, streams);
//
//		if (CollectionUtils.isNullOrEmpty(list)) {
//			return;
//		}
//
//		List<StreamEntry> streamEntries = list.get(1).getValue();
//		StreamEntry streamEntry = streamEntries.get(0);
//		callback(list.get(0).getKey(), streamEntry.getFields().get(Config.QUEUE_ITEM_NAME));
	}

	/**
	 * Callback method when new message arrived.
	 * @param key Redis key of queue.
	 * @param value Message.
	 */
	private void callback(String key, Object value) {
		if (getConsumerEvent() == null) {
			return;
		}

		getConsumerEvent().onMessage(key, value);
	}

	@Override
	public void close() throws IOException {

	}

	/**
	 * Set Jedis object.
	 * @param jedis Jedis object to listen queue.
	 */
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
}