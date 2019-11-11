package devutility.external.redis.queue.stream;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * XreadGroupOnlyUnreceivedTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-11 17:06:02
 */
public class XreadGroupOnlyUnreceivedTest extends BaseTestForStream {
	@Override
	public void run() {
		Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<String, StreamEntryID>(CONFIG_KEY_STREAM, StreamEntryID.UNRECEIVED_ENTRY);

		try (Jedis jedis = jedis()) {
			@SuppressWarnings("unchecked")
			List<Entry<String, List<StreamEntry>>> list = jedis.xreadGroup(StreamData.GROUPNAME, StreamData.CONSUMERNAME, 6, 0, false, stream);
			handleXreadGroup(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XreadGroupOnlyUnreceivedTest.class);
	}
}