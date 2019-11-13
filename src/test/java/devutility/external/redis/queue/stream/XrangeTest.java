package devutility.external.redis.queue.stream;

import java.util.List;
import java.util.Map;

import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * XrangeTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-13 16:35:45
 */
public class XrangeTest extends BaseTestForStream {
	@Override
	public void run() {
		StreamEntryID streamEntryID = new StreamEntryID("1572529192472-0");

		try (Jedis jedis = jedis()) {
			/**
			 * After user uses xack method to ack one message, it still can fetch from Redis with Jedis xrange method. But if user
			 * uses xdel method to del one message, it can't fetch again.
			 */
			List<StreamEntry> list = jedis.xrange(CONFIG_KEY_STREAM, streamEntryID, streamEntryID, 1);

			list.forEach(i -> {
				System.out.println(String.format("ID: %s", i.getID().toString()));
				Map<String, String> map = i.getFields();

				for (Map.Entry<String, String> item : map.entrySet()) {
					System.out.println(String.format("key: %s, value: %s", item.getKey(), item.getValue()));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XrangeTest.class);
	}
}