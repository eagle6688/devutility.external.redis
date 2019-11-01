package devutility.external.redis.ext;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;

import devutility.external.redis.com.RedisType;
import devutility.external.redis.ext.com.BuilderFactory;
import devutility.external.redis.ext.constant.Command;
import devutility.external.redis.ext.constant.Keyword;
import devutility.external.redis.ext.model.GroupInfo;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
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