package devutility.external.redis.stream;

import java.util.List;
import java.util.Map.Entry;

import devutility.external.redis.BaseTest;
import redis.clients.jedis.StreamEntry;

/**
 * 
 * BaseTestForStream
 * 
 * @author: Aldwin Su
 * @version: 2019-11-04 15:07:19
 */
public abstract class BaseTestForStream extends BaseTest {
	protected void handleXreadGroup(List<Entry<String, List<StreamEntry>>> list) {
		if (list == null) {
			println("No data in queue.");
			return;
		}

		System.out.println(String.format("Size of list is %d", list.size()));

		list.forEach(i -> {
			println("============================================");
			println(String.format("Redis Queue key: %s", i.getKey()));
			println(String.format("Size of StreamEntry is %d", i.getValue().size()));

			i.getValue().forEach(j -> {
				println(String.format("StreamEntryID: %s", j.getID().toString()));

				j.getFields().entrySet().forEach(k -> {
					println(String.format("%s: %s", k.getKey(), k.getValue()));
				});
			});
		});
	}
}