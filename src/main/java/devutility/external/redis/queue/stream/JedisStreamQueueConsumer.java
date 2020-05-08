package devutility.external.redis.queue.stream;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import devutility.external.redis.com.Config;
import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.com.RedisType;
import devutility.external.redis.com.StatusCode;
import devutility.external.redis.com.StreamMessageType;
import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.ext.model.ConsumerInfo;
import devutility.external.redis.queue.Acknowledger;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.utils.JedisUtils;
import devutility.internal.lang.StringUtils;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamPendingEntry;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 
 * JedisStreamQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 20:03:42
 */
public class JedisStreamQueueConsumer extends JedisQueueConsumer {
	/**
	 * Acknowledger object.
	 */
	private Acknowledger acknowledger;

	/**
	 * Consumed StreamEntryID strings.
	 */
	private Set<String> consumedIds = new LinkedHashSet<String>();

	/**
	 * Constructor
	 * @param jedis Jedis object to read data from Redis.
	 * @param redisQueueOption RedisQueueOption object.
	 * @param consumerEvent Custom consumer event implementation.
	 */
	public JedisStreamQueueConsumer(final Jedis jedis, final RedisQueueOption redisQueueOption, final Acknowledger acknowledger, final JedisStreamQueueConsumerEvent consumerEvent) {
		super(jedis, redisQueueOption, consumerEvent);
		this.acknowledger = acknowledger;
		consumerEvent.setAcknowledger(acknowledger);
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
				/**
				 * Connection issue should be handled firstly.
				 */
				if (JedisUtils.isBrokenJedis(jedis)) {
					throw new JedisConnectionException(e);
				}

				if (e instanceof JedisFatalException || isExceptionRetryApproved(e)) {
					throw e;
				}

				if (!isReasonableConsumerException()) {
					throw new JedisFatalException("Exceptions count excced the setting exceptionLimit and exceptionIntervalMillis in RedisQueueOption object.", e);
				}

				log(e);
			}
		}
	}

	/**
	 * Validate parameters that caller provided.
	 */
	protected void validate(RedisType type) {
		super.validate();

		if (StringUtils.isNullOrEmpty(redisQueueOption.getGroupName())) {
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
		consumedIds.clear();

		if (RedisType.NONE == type) {
			initializeGroup();
			return;
		}

		if (!devJedis.isGroupExist(redisQueueOption.getKey(), redisQueueOption.getGroupName())) {
			initializeGroup();
		}
	}

	/**
	 * Initialize consumer group.
	 */
	private void initializeGroup() {
		if (devJedis.createGroup(redisQueueOption.getKey(), redisQueueOption.getGroupName()) != StatusCode.OK) {
			throw new JedisFatalException("Create group failed!");
		}
	}

	/**
	 * Process entries in Pending list.
	 */
	private void processPending() {
		ConsumerInfo consumerInfo = devJedis.getConsumerInfo(redisQueueOption.getKey(), redisQueueOption.getGroupName(), redisQueueOption.getConsumerName());

		/**
		 * New consumer.
		 */
		if (consumerInfo == null) {
			return;
		}

		int pending = (int) consumerInfo.getPending();
		int count = redisQueueOption.getPageSizeForReadPending();
		int pageSize = pending / redisQueueOption.getPageSizeForReadPending();
		StreamEntryID startId = null;

		if (count % redisQueueOption.getPageSizeForReadPending() > 0) {
			pageSize += 1;
		}

		for (int i = 0; i < pageSize; i++) {
			List<StreamPendingEntry> list = jedis.xpending(redisQueueOption.getKey(), redisQueueOption.getGroupName(), startId, null, count, redisQueueOption.getConsumerName());

			for (StreamPendingEntry item : list) {
				Map<String, String> streamEntryMap = devJedis.xrangeOne(redisQueueOption.getKey(), item.getID());

				if (streamEntryMap != null) {
					onMessage(StreamMessageType.PENDING, item.getID(), streamEntryMap.get(Config.QUEUE_DEFAULT_ITEM_KEY));
					startId = new StreamEntryID(item.getID().getTime(), item.getID().getSequence() + 1);
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
		List<Entry<String, List<StreamEntry>>> list = devJedis.xreadGroup(redisQueueOption.getGroupName(), redisQueueOption.getConsumerName(), 1, redisQueueOption.getWaitMilliseconds(), redisQueueOption.isNoNeedAck(), stream);

		if (CollectionUtils.isNullOrEmpty(list)) {
			return;
		}

		List<StreamEntry> streamEntries = list.get(0).getValue();

		if (CollectionUtils.isNullOrEmpty(streamEntries)) {
			throw new JedisFatalException("Illegal StreamEntry list!");
		}

		StreamEntry streamEntry = streamEntries.get(0);
		Map<String, String> streamEntryMap = streamEntry.getFields();

		if (streamEntryMap == null || !streamEntryMap.containsKey(Config.QUEUE_DEFAULT_ITEM_KEY)) {
			throw new JedisFatalException("Illegal StreamEntry map!");
		}

		onMessage(StreamMessageType.NORMAL, streamEntry.getID(), streamEntryMap.get(Config.QUEUE_DEFAULT_ITEM_KEY));
	}

	/**
	 * Callback method when new message arrived. The first parameter is StreamEntryID string, the second one is its value.
	 * @param value Message.
	 */
	private void onMessage(StreamMessageType streamMessageType, Object... values) {
		StreamEntryID streamEntryID = (StreamEntryID) values[0];

		/**
		 * Avoid duplicated consume.
		 */
		if (consumedIds.contains(streamEntryID.toString())) {
			return;
		}

		switch (streamMessageType) {
		case NORMAL:
			if (!consumerEvent.onMessage(redisQueueOption.getKey(), values)) {
				return;
			}
			break;

		case PENDING:
			if (!consumerEvent.onPendingMessage(redisQueueOption.getKey(), values)) {
				return;
			}
			break;

		default:
			return;
		}

		consumedIds.add(streamEntryID.toString());
		autoAck(streamEntryID);
	}

	/**
	 * Automatic acknowledge one message.
	 * @param streamEntryId: StreamEntryID object.
	 */
	private void autoAck(StreamEntryID streamEntryId) {
		if (redisQueueOption.isNoNeedAck() || !redisQueueOption.isAutoAck()) {
			return;
		}

		acknowledger.ack(streamEntryId);
	}

	@Override
	public void close() throws IOException {
		jedis.close();
		devJedis.close();
	}
}