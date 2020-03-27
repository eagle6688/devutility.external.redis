package devutility.external.redis.ext;

import java.io.Closeable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import devutility.external.redis.com.RedisType;
import devutility.external.redis.com.StatusCode;
import devutility.external.redis.ext.com.BuilderFactory;
import devutility.external.redis.ext.model.ConsumerInfo;
import devutility.external.redis.ext.model.GroupInfo;
import devutility.internal.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * DevJedis
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 20:47:56
 */
public class DevJedis implements Closeable {
	/**
	 * Jedis object.
	 */
	private Jedis jedis;

	/**
	 * DevJedisClient object.
	 */
	private DevJedisClient devJedisClient;

	/**
	 * Constructor
	 * @param jedis Jedis object.
	 */
	public DevJedis(Jedis jedis) {
		this.setJedis(jedis);
	}

	/**
	 * Get RedisType of provided key.
	 * @param key Redis key.
	 * @return RedisType
	 */
	public RedisType type(final String key) {
		String type = jedis.type(key);
		return RedisType.parse(type);
	}

	/**
	 * Use XINFO command to query information of groups belong to the provided key.
	 * @param key Redis key.
	 * @return {@code List<GroupInfo>}
	 */
	public List<GroupInfo> xInfoGroups(final String key) {
		devJedisClient.xInfoGroups(key);
		List<Object> list = devJedisClient.getObjectMultiBulkReply();
		return BuilderFactory.STREAM_GROUPINFO_LIST.build(list);
	}

	/**
	 * Get GroupInfo object use provided key and groupName.
	 * @param key Redis key.
	 * @param groupName Group name of Redis stream.
	 * @return GroupInfo
	 */
	public GroupInfo getGroupInfo(final String key, final String groupName) {
		List<GroupInfo> groupInfos = xInfoGroups(key);
		return CollectionUtils.find(groupInfos, i -> groupName.equals(i.getName()));
	}

	/**
	 * Whether the group exist or not?
	 * @param key Redis key.
	 * @param groupName Group name of Redis stream.
	 * @return boolean
	 */
	public boolean isGroupExist(final String key, final String groupName) {
		List<GroupInfo> groupInfos = xInfoGroups(key);
		return CollectionUtils.exist(groupInfos, i -> groupName.equals(i.getName()));
	}

	/**
	 * Use XINFO command to query information of consumers belong to the provided key and group.
	 * @param key Redis key.
	 * @param groupName Group name.
	 * @return {@code List<ConsumerInfo>}
	 */
	public List<ConsumerInfo> xInfoConsumers(final String key, final String groupName) {
		devJedisClient.xInfoConsumers(key, groupName);
		List<Object> list = devJedisClient.getObjectMultiBulkReply();
		return BuilderFactory.STREAM_CONSUMERINFO_LIST.build(list);
	}

	/**
	 * Get ConsumerInfo object use provided key and groupName.
	 * @param key Redis key.
	 * @param groupName Group name.
	 * @param consumerName Consumer group.
	 * @return ConsumerInfo
	 */
	public ConsumerInfo getConsumerInfo(final String key, final String groupName, String consumerName) {
		List<ConsumerInfo> list = xInfoConsumers(key, groupName);
		return CollectionUtils.find(list, i -> i.getName().equals(consumerName));
	}

	/**
	 * Create a new group in provided key. As the parameter of makeStream has been true, this indicate system will
	 * automaticly create a empty stream with provided key once it does exist.
	 * @param key Redis Stream key.
	 * @param groupName Group name in Stream.
	 * @return StatusCode
	 */
	public StatusCode createGroup(final String key, final String groupName) {
		String result = jedis.xgroupCreate(key, groupName, null, true);
		return StatusCode.parse(result);
	}

	@SuppressWarnings("unchecked")
	public List<Entry<String, List<StreamEntry>>> xread(final int count, final long block, final Entry<String, StreamEntryID>... streams) {
		devJedisClient.xread(count, block, streams);
		devJedisClient.setTimeoutInfinite();

		try {
			List<Object> streamsEntries = devJedisClient.getObjectMultiBulkReply();

			if (streamsEntries == null) {
				return new ArrayList<>();
			}

			List<Entry<String, List<StreamEntry>>> result = new ArrayList<>(streamsEntries.size());

			for (Object streamObj : streamsEntries) {
				List<Object> stream = (List<Object>) streamObj;
				String streamId = SafeEncoder.encode((byte[]) stream.get(0));
				List<StreamEntry> streamEntries = BuilderFactory.STREAM_ENTRY_LIST.build(stream.get(1));
				result.add(new AbstractMap.SimpleEntry<String, List<StreamEntry>>(streamId, streamEntries));
			}

			return result;
		} finally {
			devJedisClient.rollbackTimeout();
		}
	}

	/**
	 * Overwrite xreadGroup method in Jedis, because it has a bug causes SocketTimeoutException once use block mode.
	 * @param groupname Group name of Reids queue.
	 * @param consumer Consumer name of provided group.
	 * @param count Message count of one Redis queue.
	 * @param block Block time in milliseconds.
	 * @param noAck True will avoid adding the message to the PEL.
	 * @param streams Two types format, The special {@code >} ID, which means that the consumer want to receive only
	 *            messages that were never delivered to any other consumer. It just means, give me new messages. Any other
	 *            ID, that is, 0 or any other valid ID or incomplete ID (just the millisecond time part), will have the
	 *            effect of returning entries that are pending for the consumer sending the command with IDs equal or
	 *            greater to the one provided. So basically if the ID is not {@code >}, then the command will just let the
	 *            client access its pending entries: messages delivered to it, but not yet acknowledged. Note that in this
	 *            case, both BLOCK and NOACK are ignored.
	 * @return {@code List<Entry<String,List<StreamEntry>>>}
	 */
	public List<Entry<String, List<StreamEntry>>> xreadGroup(final String groupname, final String consumer, final int count, final long block, final boolean noAck, List<Entry<String, StreamEntryID>> streams) {
		@SuppressWarnings("unchecked")
		Entry<String, StreamEntryID>[] entries = (Entry<String, StreamEntryID>[]) Arrays.asList(streams).toArray();
		return xreadGroup(groupname, consumer, count, block, noAck, entries);
	}

	/**
	 * Overwrite xreadGroup method in Jedis, because it has a bug causes SocketTimeoutException once use block mode.
	 * @param groupname Group name of Reids queue.
	 * @param consumer Consumer name of provided group.
	 * @param count Message count of one Redis queue.
	 * @param block Block time in milliseconds.
	 * @param noAck True will avoid adding the message to the PEL.
	 * @param streams Two types format, The special {@code >} ID, which means that the consumer want to receive only
	 *            messages that were never delivered to any other consumer. It just means, give me new messages. Any other
	 *            ID, that is, 0 or any other valid ID or incomplete ID (just the millisecond time part), will have the
	 *            effect of returning entries that are pending for the consumer sending the command with IDs equal or
	 *            greater to the one provided. So basically if the ID is not {@code >}, then the command will just let the
	 *            client access its pending entries: messages delivered to it, but not yet acknowledged. Note that in this
	 *            case, both BLOCK and NOACK are ignored.
	 * @return {@code List<Entry<String,List<StreamEntry>>>}
	 */
	@SuppressWarnings("unchecked")
	public List<Entry<String, List<StreamEntry>>> xreadGroup(final String groupname, final String consumer, final int count, final long block, final boolean noAck, final Entry<String, StreamEntryID>... streams) {
		devJedisClient.xreadGroup(groupname, consumer, count, block, noAck, streams);
		devJedisClient.setTimeoutInfinite();
		List<Object> streamsEntries = devJedisClient.getObjectMultiBulkReply();

		if (streamsEntries == null) {
			return new LinkedList<>();
		}

		List<Entry<String, List<StreamEntry>>> result = new ArrayList<>(streamsEntries.size());

		for (Object streamObj : streamsEntries) {
			List<Object> stream = (List<Object>) streamObj;
			String streamId = SafeEncoder.encode((byte[]) stream.get(0));
			List<StreamEntry> streamEntries = BuilderFactory.STREAM_ENTRY_LIST.build(stream.get(1));
			result.add(new AbstractMap.SimpleEntry<String, List<StreamEntry>>(streamId, streamEntries));
		}

		return result;
	}

	/**
	 * Acknowledge provided StreamEntryID list.
	 * @param key Redis stream key.
	 * @param groupName Group name.
	 * @param ids StreamEntryID objects.
	 * @return long
	 */
	public long xack(final String key, final String groupName, final StreamEntryID... ids) {
		jedis.xack(key, groupName, ids);
		return jedis.xdel(key, ids);
	}

	/**
	 * Use Jedis xrange to get Map for one StreamEntryID.
	 * @param key Redis stream key.
	 * @param id StreamEntryID object.
	 * @return {@code Map<String, String>}
	 */
	public Map<String, String> xrangeOne(final String key, final StreamEntryID id) {
		List<StreamEntry> list = jedis.xrange(key, id, id, 1);

		if (CollectionUtils.isNullOrEmpty(list)) {
			return null;
		}

		StreamEntry streamEntry = list.get(0);
		return streamEntry.getFields();
	}

	@Override
	public void close() {
		jedis.close();
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
		this.devJedisClient = null;

		if (jedis != null) {
			this.devJedisClient = new DevJedisClient(jedis.getClient());
		}
	}
}