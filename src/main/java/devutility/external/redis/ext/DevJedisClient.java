package devutility.external.redis.ext;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Client;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * DevJedisClient
 * 
 * @author: Aldwin Su
 * @version: 2019-11-04 20:13:23
 */
public class DevJedisClient extends DevJedisCommandClient {
	/**
	 * Constructor
	 * @param client Jedis Client object.
	 */
	public DevJedisClient(Client client) {
		super(client);
	}

	public void xread(final int count, final long block, @SuppressWarnings("unchecked") final Entry<String, StreamEntryID>... streams) {
		final Map<byte[], byte[]> bhash = new HashMap<byte[], byte[]>(streams.length);

		for (final Entry<String, StreamEntryID> entry : streams) {
			bhash.put(SafeEncoder.encode(entry.getKey()), SafeEncoder.encode(entry.getValue() == null ? "0-0" : entry.getValue().toString()));
		}

		xread(count, block, bhash);
	}

	public void xreadGroup(String groupname, String consumer, int count, long block, boolean noAck, @SuppressWarnings("unchecked") Entry<String, StreamEntryID>... streams) {
		final Map<byte[], byte[]> bhash = new HashMap<>(streams.length);

		for (final Entry<String, StreamEntryID> entry : streams) {
			bhash.put(SafeEncoder.encode(entry.getKey()), SafeEncoder.encode(entry.getValue() == null ? ">" : entry.getValue().toString()));
		}

		xreadGroup(SafeEncoder.encode(groupname), SafeEncoder.encode(consumer), count, block, noAck, bhash);
	}
}