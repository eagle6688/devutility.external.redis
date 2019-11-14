package devutility.external.redis.ext;

import static redis.clients.jedis.Protocol.toByteArray;
import static redis.clients.jedis.Protocol.Command.XREAD;
import static redis.clients.jedis.Protocol.Command.XREADGROUP;

import java.util.Map;
import java.util.Map.Entry;

import devutility.external.redis.ext.constant.DevJedisCommand;
import devutility.external.redis.ext.constant.DevJedisKeyword;
import redis.clients.jedis.Client;
import redis.clients.jedis.Protocol.Keyword;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * DevJedisCommandClient
 * 
 * @author: Aldwin Su
 * @version: 2019-11-04 20:06:36
 */
public class DevJedisCommandClient extends DevJedisConnection {
	/**
	 * Constructor
	 * @param client Jedis Client object.
	 */
	public DevJedisCommandClient(Client client) {
		super(client);
	}

	/**
	 * Use XINFO command to query information of groups belong to the provided key.
	 * @param key Redis key.
	 */
	public void xInfoGroups(final String key) {
		client.sendCommand(DevJedisCommand.XINFO, DevJedisKeyword.GROUPS.raw, SafeEncoder.encode(key));
	}

	/**
	 * Use XINFO command to query information of consumers belong to the provided key and group.
	 * @param key Redis key.
	 * @param groupName Group name.
	 * @return {@code List<ConsumerInfo>}
	 */
	public void xInfoConsumers(final String key, final String groupName) {
		client.sendCommand(DevJedisCommand.XINFO, DevJedisKeyword.CONSUMERS.raw, SafeEncoder.encode(key), SafeEncoder.encode(groupName));
	}

	public void xread(final int count, final long block, final Map<byte[], byte[]> streams) {
		final byte[][] params = new byte[3 + streams.size() * 2 + (block > -1 ? 2 : 0)][];

		int streamsIndex = 0;
		params[streamsIndex++] = Keyword.COUNT.raw;
		params[streamsIndex++] = toByteArray(count);

		if (block > -1) {
			params[streamsIndex++] = Keyword.BLOCK.raw;
			params[streamsIndex++] = toByteArray(block);
		}

		params[streamsIndex++] = Keyword.STREAMS.raw;
		int idsIndex = streamsIndex + streams.size();

		for (final Entry<byte[], byte[]> entry : streams.entrySet()) {
			params[streamsIndex++] = entry.getKey();
			params[idsIndex++] = entry.getValue();
		}

		client.sendCommand(XREAD, params);
	}

	public void xreadGroup(byte[] groupname, byte[] consumer, int count, long block, boolean noAck, Map<byte[], byte[]> streams) {
		int optional = 0;

		if (count > 0) {
			optional += 2;
		}

		if (block > -1) {
			optional += 2;
		}

		if (noAck) {
			optional += 1;
		}

		final byte[][] params = new byte[4 + optional + streams.size() * 2][];
		int streamsIndex = 0;

		params[streamsIndex++] = Keyword.GROUP.raw;
		params[streamsIndex++] = groupname;
		params[streamsIndex++] = consumer;

		if (count > 0) {
			params[streamsIndex++] = Keyword.COUNT.raw;
			params[streamsIndex++] = toByteArray(count);
		}

		if (block > -1) {
			params[streamsIndex++] = Keyword.BLOCK.raw;
			params[streamsIndex++] = toByteArray(block);
		}

		if (noAck) {
			params[streamsIndex++] = Keyword.NOACK.raw;
		}

		params[streamsIndex++] = Keyword.STREAMS.raw;
		int idsIndex = streamsIndex + streams.size();

		for (final Entry<byte[], byte[]> entry : streams.entrySet()) {
			params[streamsIndex++] = entry.getKey();
			params[idsIndex++] = entry.getValue();
		}

		client.sendCommand(XREADGROUP, params);
	}
}