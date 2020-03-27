package devutility.external.redis.ext;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.model.StreamData;
import devutility.external.redis.queue.stream.BaseTestForStream;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

public class XreadTest extends BaseTestForStream {
	@Override
	public void run() {
		Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<String, StreamEntryID>(StreamData.KEY, StreamEntryID.LAST_ENTRY);

		try (DevJedis jedis = new DevJedis(jedis())) {
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