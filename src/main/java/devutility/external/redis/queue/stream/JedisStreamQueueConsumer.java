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
import devutility.external.redis.ext.model.ConsumerInfo;
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
		this.groupName = redisQueueOption.getGroupName();
	}

	@Override
	public void listen() throws Exception {
		RedisType type = devJedis.type(redisQueueOption.getKey());
		validate(type);
		initialize(type);

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
	protected void validate(RedisType type) {
		super.validate();

		if (StringUtils.isNullOrEmpty(groupName)) {
			throw new IllegalArgumentException("Group name can't be empty!");
		}

		if (StringUtils.isNullOrEmpty(redisQueueOption.getConsumerName())) {
			throw new IllegalArgumentException("Consumer name can't be empty!");
		}

		if (RedisType.NONE != type && RedisType.STREAM != type) {
			throw new IllegalArgumentException(String.format("Invalid Redis type for key \"%s\"!", redisQueueOption.getKey()));
		}
	}

	/**
	 * Initialize for data consumption.
	 */
	protected void initialize(RedisType type) {
		if (RedisType.NONE == type) {
			initializeGroup();
			return;
		}

		if (!devJedis.isGroupExist(redisQueueOption.getKey(), groupName)) {
			initializeGroup();
		}
	}

	/**
	 * Initialize consumer group.
	 */
	private void initializeGroup() {
		if (devJedis.createGroup(redisQueueOption.getKey(), groupName) != StatusCode.OK) {
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

		int count = (int) consumerInfo.getPending();
		int pageSize = count / redisQueueOption.getPageSizeForReadPending();
		StreamEntryID startId = null;

		if (count % redisQueueOption.getPageSizeForReadPending() > 0) {
			pageSize += 1;
		}

		for (int i = 0; i < pageSize; i++) {
			List<StreamPendingEntry> list = jedis.xpending(redisQueueOption.getKey(), groupName, startId, null, count, redisQueueOption.getConsumerName());

			for (StreamPendingEntry item : list) {
				Map<String, String> streamEntryMap = devJedis.xrangeOne(redisQueueOption.getKey(), item.getID());

				if (streamEntryMap != null) {
					callback(item.getID(), streamEntryMap.get(Config.QUEUE_DEFAULT_ITEM_KEY));
					startId = item.getID();
				}
			}
		}
	}

	/**
	 * Main process.
	 * @throws InterruptedException
	 */
	private void process() throws InterruptedException {
		/**
		 * Key of queue in Redis, value is StreamEntryID object which indicate the beginning ID of queue items.
		 */
		Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<String, StreamEntryID>(redisQueueOption.getKey(), StreamEntryID.UNRECEIVED_ENTRY);

		@SuppressWarnings("unchecked")
		List<Entry<String, List<StreamEntry>>> list = devJedis.xreadGroup(groupName, redisQueueOption.getConsumerName(), 1, redisQueueOption.getWaitMilliseconds(), redisQueueOption.isNoNeedAck(), stream);

		if (CollectionUtils.isNullOrEmpty(list) || list.size() != 1) {
			throw new JedisFatalException("Illegal Entry data!");
		}

		List<StreamEntry> streamEntries = list.get(0).getValue();

		if (CollectionUtils.isNullOrEmpty(streamEntries) || streamEntries.size() != 1) {
			throw new JedisFatalException("Illegal StreamEntry list!");
		}

		StreamEntry streamEntry = streamEntries.get(0);
		Map<String, String> streamEntryMap = streamEntry.getFields();

		if (streamEntryMap == null || !streamEntryMap.containsKey(Config.QUEUE_DEFAULT_ITEM_KEY)) {
			throw new JedisFatalException("Illegal StreamEntry map!");
		}

		callback(streamEntry.getID(), streamEntryMap.get(Config.QUEUE_DEFAULT_ITEM_KEY));
	}

	/**
	 * Callback method when new message arrived. The first parameter is StreamEntryID string, the second one is its value.
	 * @param value Message.
	 */
	private void callback(Object... values) {
		if (consumerEvent == null) {
			/**
			 * No consume.
			 */
			return;
		}

		if (!consumerEvent.onMessage(redisQueueOption.getKey(), values)) {
			/**
			 * Failed consume.
			 */
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