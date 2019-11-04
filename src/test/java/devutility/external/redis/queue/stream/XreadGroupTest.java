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
 * XreadGroupTest Note: assign a StreamEntryID when call xreadGroup will not block any request, this action will queue
 * PEL.
 * 
 * @author: Aldwin Su
 * @version: 2019-11-04 15:03:51
 */
public class XreadGroupTest extends BaseTestForStream {
	@Override
	public void run() {
		Entry<String, StreamEntryID> stream1 = new AbstractMap.SimpleEntry<String, StreamEntryID>(CONFIG_KEY_STREAM, new StreamEntryID("0-0"));
		Entry<String, StreamEntryID> stream2 = new AbstractMap.SimpleEntry<String, StreamEntryID>(CONFIG_KEY_STREAM, null);

		try (Jedis jedis = jedis()) {
			@SuppressWarnings("unchecked")
			List<Entry<String, List<StreamEntry>>> list = jedis.xreadGroup(StreamData.GROUPNAME, StreamData.CONSUMERNAME, 6, 0, false, stream1, stream2);
			handleXreadGroup(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XreadGroupTest.class);
	}
}