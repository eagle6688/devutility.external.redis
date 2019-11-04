package devutility.external.redis.ext;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.model.StreamData;
import devutility.external.redis.queue.stream.BaseTestForStream;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * XreadGroupWithBlockTest, you can't assign any StreamEntryID when call xreadGroup.
 * 
 * @author: Aldwin Su
 * @version: 2019-11-04 15:11:12
 */
public class XreadGroupWithBlockTest extends BaseTestForStream {
	@Override
	public void run() {
		Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<String, StreamEntryID>(CONFIG_KEY_STREAM, null);

		try (DevJedis jedis = new DevJedis(jedis())) {
			@SuppressWarnings("unchecked")
			List<Entry<String, List<StreamEntry>>> list = jedis.xreadGroup(StreamData.GROUPNAME, StreamData.CONSUMERNAME, 1, 20000, false, stream);
			handleXreadGroup(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XreadGroupWithBlockTest.class);
	}
}