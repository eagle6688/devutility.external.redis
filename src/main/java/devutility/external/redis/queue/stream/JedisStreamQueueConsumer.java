package devutility.external.redis.queue.stream;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import devutility.external.redis.com.Config;
import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.com.RedisType;
import devutility.external.redis.com.StatusCode;
import devutility.external.redis.exception.JedisBrokenException;
import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.ext.DevJedis;
import devutility.external.redis.ext.model.ConsumerInfo;
import devutility.external.redis.ext.model.GroupInfo;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.internal.lang.StringUtils;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamPendingEntry;

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
	protected String groupName;

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(Jedis jedis, final RedisQueueOption redisQueueOption, final JedisQueueConsumerEvent consumerEvent) {
		super(jedis, redisQueueOption, consumerEvent);
		this.devJedis = new DevJedis(jedis);

		if (StringUtils.isNotEmpty(redisQueueOption.getGroupName())) {
			this.groupName = redisQueueOption.getGroupName();
		}
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

		while (isActive()) {
			try {
				processPending();
				process();
			} catch (Exception e) {
				if (jedis.getClient().isBroken()) {
					throw new JedisBrokenException(e);
				}

				if (e instanceof JedisFatalException) {
					throw e;
				}

				log(e);
				connect(jedis);
			}
		}
	}

	/**
	 * Validate parameters that caller provided.
	 */
	protected void validate() {
		if (jedis == null) {
			throw new IllegalArgumentException("jedis can't be null!");
		}

		if (StringUtils.isNullOrEmpty(redisQueueOption.getKey())) {
			throw new IllegalArgumentException("Redis key can't be empty!");
		}

		if (StringUtils.isNullOrEmpty(groupName)) {
			throw new IllegalArgumentException("Group name can't be empty!");
		}

		if (StringUtils.isNullOrEmpty(redisQueueOption.getConsumerName())) {
			throw new IllegalArgumentException("Consumer name can't be empty!");
		}

		RedisType type = devJedis.type(redisQueueOption.getKey());

		if (RedisType.STREAM != type) {
			throw new IllegalArgumentException(String.format("Invalid Redis type of key \"%\"!", redisQueueOption.getKey()));
		}
	}

	/**
	 * Initialize for data consumption.
	 */
	protected void initialize() {
		GroupInfo groupInfo = devJedis.getGroupInfo(redisQueueOption.getKey(), groupName);

		if (groupInfo == null && devJedis.createGroup(redisQueueOption.getKey(), groupName) != StatusCode.OK) {
			throw new JedisFatalException("Create group failed!");
		}
	}

	/**
	 * Process entries in Pending list.
	 */
	private void processPending() {
		ConsumerInfo consumerInfo = devJedis.getConsumerInfo(redisQueueOption.getKey(), groupName, redisQueueOption.getConsumerName());

		/**
		 * New consumer.
		 */
		if (consumerInfo == null) {
			return;
		}

		List<StreamPendingEntry> list = jedis.xpending(redisQueueOption.getKey(), groupName, null, null, (int) consumerInfo.getPending(), redisQueueOption.getConsumerName());

		for (StreamPendingEntry item : list) {
			Map<String, String> streamEntryMap = devJedis.xrangeOne(redisQueueOption.getKey(), item.getID());

			if (streamEntryMap != null) {
				callback(item.getID(), streamEntryMap.get(Config.QUEUE_DEFAULT_ITEM_KEY));
			}
		}
	}

	/**
	 * Main process.
	 * @throws InterruptedException
	 */
	private void process() throws InterruptedException {
		/**
		 * Key is key of queue in Redis, value is StreamEntryID object which indicate the beginning ID of queue items.
		 */
		Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<String, StreamEntryID>(redisQueueOption.getKey(), StreamEntryID.UNRECEIVED_ENTRY);

		@SuppressWarnings("unchecked")
		List<Entry<String, List<StreamEntry>>> list = devJedis.xreadGroup(groupName, redisQueueOption.getConsumerName(), 1, redisQueueOption.getWaitMilliseconds(), redisQueueOption.isNoNeedAck(), stream);

		if (CollectionUtils.isNullOrEmpty(list) || list.size() != 1) {
			throw new JedisFatalException("Illegal Entry data!");
		}

		List<StreamEntry> streamEntries = list.get(0).getValue();

		if (CollectionUtils.isNullOrEmpty(streamEntries) || streamEntries.size() != 1) {
			throw new JedisFatalException("Illegal StreamEntry list data!");
		}

		StreamEntry streamEntry = streamEntries.get(0);
		Map<String, String> streamEntryMap = streamEntry.getFields();

		if (streamEntryMap == null || !streamEntryMap.containsKey(Config.QUEUE_DEFAULT_ITEM_KEY)) {
			throw new JedisFatalException("Illegal StreamEntry list data!");
		}

		callback(streamEntry.getID(), streamEntryMap.get(Config.QUEUE_DEFAULT_ITEM_KEY));
	}

	/**
	 * Callback method when new message arrived. The first parameter is StreamEntryID string, the second one is its value.
	 * @param value Message.
	 */
	private void callback(Object... values) {
		if (getConsumerEvent() == null) {
			return;
		}

		if (!getConsumerEvent().onMessage(redisQueueOption.getKey(), values)) {
			return;
		}

		if (!redisQueueOption.isNoNeedAck() && redisQueueOption.isAutoAck()) {
			jedis.xack(redisQueueOption.getKey(), groupName, (StreamEntryID) values[0]);
		}
	}

	@Override
	public void close() throws IOException {
		jedis.close();
	}
}