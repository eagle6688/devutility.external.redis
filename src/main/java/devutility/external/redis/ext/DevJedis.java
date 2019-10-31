package devutility.external.redis.ext;

import java.util.List;

import devutility.external.redis.ext.com.BuilderFactory;
import devutility.external.redis.ext.constant.Command;
import devutility.external.redis.ext.constant.Keyword;
import devutility.external.redis.ext.model.GroupInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * DevJedis
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 20:47:56
 */
public class DevJedis extends Jedis {
	/**
	 * Constructor
	 * @param jedis Jedis object.
	 */
	public DevJedis(Jedis jedis) {
		super.client = jedis.getClient();
		super.pipeline = jedis.pipelined();
	}

	public List<GroupInfo> xInfoGroups(String key) {
		checkIsInMultiOrPipeline();
		client.sendCommand(Command.XINFO, Keyword.GROUPS.raw, SafeEncoder.encode(key));
		List<Object> list = client.getObjectMultiBulkReply();
		return BuilderFactory.STREAM_GROUPINFO_LIST.build(list);
	}

	@Override
	public void close() {
		super.close();
	}
}