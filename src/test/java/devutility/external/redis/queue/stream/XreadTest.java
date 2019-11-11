package devutility.external.redis.queue.stream;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;

import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

public class XreadTest extends BaseTestForStream {
	@Override
	public void run() {
		Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<String, StreamEntryID>(CONFIG_KEY_STREAM, StreamEntryID.LAST_ENTRY);

		try (Jedis jedis = jedis()) {
			@SuppressWarnings("unchecked")
			List<Entry<String, List<StreamEntry>>> list = jedis.xread(10, 0, stream);
			handleXreadGroup(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XreadTest.class);
	}
}