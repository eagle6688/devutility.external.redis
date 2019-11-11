package devutility.external.redis.queue.stream;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.com.Config;
import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.com.RedisType;
import devutility.external.redis.com.StatusCode;
import devutility.external.redis.exception.JedisBrokenException;
import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.ext.DevJedis;
import devutility.external.redis.ext.model.GroupInfo;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.internal.lang.StringUtils;
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
	 * Group name.
	 */
	private String groupName;

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent Custom consumer event implementation.
	 * @param groupName Group name of redis queue.
	 * @param consumerName Consumer name of provided group.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, final RedisQueueOption redisQueueOption, final JedisQueueConsumerEvent consumerEvent) {
		super(jedis, redisQueueOption, consumerEvent);
		this.devJedis = new DevJedis(jedis);
		this.groupName = getGroupName();
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param key Redis key of queue.
	 * @param groupName Group name of redis queue.
	 * @param consumerName Consumer name of provided group.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, final String key, final String groupName, final String consumerName, final JedisQueueConsumerEvent consumerEvent) {
		this(jedis, new RedisQueueOption(key, groupName, consumerName), consumerEvent);
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param key Redis key of queue.
	 * @param groupName Group name of redis queue.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, final String key, final String groupName, final JedisQueueConsumerEvent consumerEvent) {
		this(jedis, key, groupName, null, consumerEvent);
	}

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param key Redis key of queue.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, final String key, final JedisQueueConsumerEvent consumerEvent) {
		this(jedis, key, null, consumerEvent);
	}

	@Override
	public void listen() throws Exception {
		validate();
		initialize();
	}

	public void listen(int count, Entry<String, StreamEntryID>[] streams) throws Exception {
		validate();

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

	private void validate() {
		if (jedis == null) {
			throw new IllegalArgumentException("jedis can't be null!");
		}

		RedisType type = devJedis.type(redisQueueOption.getKey());

		if (RedisType.STREAM != type) {
			throw new IllegalArgumentException(String.format("Invalid Redis type of key \"%\"!", redisQueueOption.getKey()));
		}
	}

	private void initialize() {
		if (!Config.QUEUE_DEFAULT_GROUP_NAME.equals(groupName)) {
			GroupInfo groupInfo = devJedis.getGroupInfo(redisQueueOption.getKey(), groupName);

			if (groupInfo == null && devJedis.createGroup(redisQueueOption.getKey(), groupName) != StatusCode.OK) {
				throw new JedisFatalException("Create group failed!");
			}
		}
	}

	/**
	 * Get group name.
	 * @return String
	 */
	private String getGroupName() {
		if (StringUtils.isNotEmpty(redisQueueOption.getGroupName())) {
			return redisQueueOption.getGroupName();
		}

		return Config.QUEUE_DEFAULT_GROUP_NAME;
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
		List<Entry<String, List<StreamEntry>>> list = jedis.xreadGroup(groupName, redisQueueOption.getConsumerName(), count, redisQueueOption.getWaitMilliseconds(), redisQueueOption.isNoNeedAck(), streams);

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
		jedis.close();
	}
}