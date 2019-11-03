package devutility.external.redis.queue.stream;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.exception.JedisBrokenException;
import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

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
	 * Group name of current instance.
	 */
	private String groupName;

	/**
	 * Consumer name of current instance.
	 */
	private String consumerName;

	/**
	 * No need Ack
	 */
	private boolean noNeedAck;

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent Custom consumer event implementation.
	 * @param groupName Group name of redis queue.
	 * @param consumerName Consumer name of provided group.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, RedisQueueOption redisQueueOption, JedisQueueConsumerEvent consumerEvent) {
		super(redisQueueOption, consumerEvent);
		this.jedis = jedis;
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param key Redis key of queue.
	 * @param groupName Group name of redis queue.
	 * @param consumerName Consumer name of provided group.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, String key, String groupName, String consumerName, JedisQueueConsumerEvent consumerEvent) {
		this(jedis, new RedisQueueOption(key, groupName, consumerName), consumerEvent);
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param key Redis key of queue.
	 * @param groupName Group name of redis queue.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, String key, String groupName, JedisQueueConsumerEvent consumerEvent) {
		this(jedis, key, groupName, null, consumerEvent);
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param key Redis key of queue.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, String key, JedisQueueConsumerEvent consumerEvent) {
		this(jedis, key, null, consumerEvent);
	}

	@Override
	public void listen() throws Exception {
		if (jedis == null) {
			throw new IllegalArgumentException("jedis can't be null!");
		}
	}

	public void listen(int count, Entry<String, StreamEntryID>[] streams) throws Exception {
		if (jedis == null) {
			throw new IllegalArgumentException("jedis can't be null!");
		}

		while (isActive()) {
			try {
				process(count, streams);
			} catch (Exception e) {
				if (jedis.getClient().isBroken()) {
					throw new JedisBrokenException(e);
				}

				if (e instanceof JedisFatalException) {
					throw e;
				}

				log(e);
			}
		}
	}

	public void listen(Entry<String, StreamEntryID>[] streams) throws Exception {
		this.listen(1, streams);
	}

	public void listen(Entry<String, StreamEntryID> stream) throws Exception {
		@SuppressWarnings("unchecked")
		Entry<String, StreamEntryID>[] entries = (Entry<String, StreamEntryID>[]) Arrays.asList(stream).toArray();
		this.listen(entries);
	}

	public void listen(String key, StreamEntryID streamEntryID) throws Exception {
		Entry<String, StreamEntryID> entry = new AbstractMap.SimpleEntry<String, StreamEntryID>(key, streamEntryID);
		this.listen(entry);
	}

	/**
	 * Main process.
	 * @param count Items count that Jedis need to read.
	 * @param streams Its a Entry array. For each Entry object, key is key of queue in Redis, value is StreamEntryID object
	 *            which indicate the beginning ID of queue items.
	 * @throws InterruptedException
	 */
	private void process(int count, Entry<String, StreamEntryID>[] streams) throws InterruptedException {
		connect(jedis);
		List<Entry<String, List<StreamEntry>>> list = jedis.xreadGroup(groupName, consumerName, count, getRedisQueueOption().getWaitMilliseconds(), noNeedAck, streams);

		if (CollectionUtils.isNullOrEmpty(list)) {
			return;
		}

		callback(list.get(0).getKey(), list.get(1).getValue());
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}
}