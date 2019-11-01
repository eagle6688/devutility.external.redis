package devutility.external.redis.stream;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.BaseTest;
import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

public class XreadGroupTest extends BaseTest {
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Entry<String, StreamEntryID> stream = new AbstractMap.SimpleEntry<String, StreamEntryID>(CONFIG_KEY_STREAM, null);

		try (Jedis jedis = jedis()) {
			List<Entry<String, List<StreamEntry>>> list = jedis.xreadGroup(StreamData.GROUPNAME, StreamData.CONSUMERNAME, 1, 0, false, stream);

			if (list == null) {
				println("No data in queue.");
				return;
			}

			list.forEach(i -> {
				println("======================");
				println(i.getKey());
				println(i.getValue().size());

				i.getValue().forEach(j -> {
					println(j.getID().toString());

					j.getFields().entrySet().forEach(k -> {
						println(String.format("%s: %s", k.getKey(), k.getValue()));
					});
				});
			});

			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XreadGroupTest.class);
	}
}