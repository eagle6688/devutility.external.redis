package devutility.external.redis.ext;

import java.io.Closeable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.com.RedisType;
import devutility.external.redis.ext.com.BuilderFactory;
import devutility.external.redis.ext.constant.Command;
import devutility.external.redis.ext.constant.Keyword;
import devutility.external.redis.ext.model.ConsumerInfo;
import devutility.external.redis.ext.model.GroupInfo;
import redis.clients.jedis.Client;
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
	 * Constructor
	 * @param jedis Jedis object.
	 */
	public DevJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	/**
	 * Get RedisType of provided key.
	 * @param key Redis key.
	 * @return RedisType
	 */
	public RedisType type(String key) {
		String type = jedis.type(key);
		return RedisType.parse(type);
	}

	/**
	 * Use XINFO command to query information of groups belong to the provided key.
	 * @param key Redis key.
	 * @return {@code List<GroupInfo>}
	 */
	public List<GroupInfo> xInfoGroups(String key) {
		Client client = jedis.getClient();
		client.sendCommand(Command.XINFO, Keyword.GROUPS.raw, SafeEncoder.encode(key));
		List<Object> list = client.getObjectMultiBulkReply();
		return BuilderFactory.STREAM_GROUPINFO_LIST.build(list);
	}

	/**
	 * Use XINFO command to query information of consumers belong to the provided key and group.
	 * @param key Redis key.
	 * @param groupName Group name.
	 * @return {@code List<ConsumerInfo>}
	 */
	public List<ConsumerInfo> xInfoConsumers(String key, String groupName) {
		Client client = jedis.getClient();
		client.sendCommand(Command.XINFO, Keyword.CONSUMERS.raw, SafeEncoder.encode(key), SafeEncoder.encode(groupName));
		List<Object> list = client.getObjectMultiBulkReply();
		return BuilderFactory.STREAM_CONSUMERINFO_LIST.build(list);
	}

	public List<Entry<String, List<StreamEntry>>> xreadGroup(final String groupname, final String consumer, final int count, final long block, final boolean noAck, List<Entry<String, StreamEntryID>> streams) {
		@SuppressWarnings("unchecked")
		Entry<String, StreamEntryID>[] entries = (Entry<String, StreamEntryID>[]) Arrays.asList(streams).toArray();
		return xreadGroup(groupname, consumer, count, block, noAck, entries);
	}

	@SuppressWarnings("unchecked")
	public List<Entry<String, List<StreamEntry>>> xreadGroup(final String groupname, final String consumer, final int count, final long block, final boolean noAck, final Entry<String, StreamEntryID>... streams) {
		Client client = jedis.getClient();
		client.xreadGroup(groupname, consumer, count, block, noAck, streams);

		client.setTimeoutInfinite();
		List<Object> streamsEntries = client.getObjectMultiBulkReply();

		if (streamsEntries == null) {
			return null;
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
	 * Get GroupInfo objects from provided key.
	 * @param key Redis key.
	 * @return {@code List<GroupInfo>}
	 */
	public List<GroupInfo> safeGroupInfos(String key) {
		RedisType type = type(key);

		if (RedisType.STREAM != type) {
			return new LinkedList<GroupInfo>();
		}

		return xInfoGroups(key);
	}

	@Override
	public void close() {
		jedis.close();
	}
}