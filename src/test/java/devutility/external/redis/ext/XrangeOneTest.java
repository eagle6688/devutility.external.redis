package devutility.external.redis.ext;

import java.util.Map;

import devutility.external.redis.queue.stream.BaseTestForStream;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * XrangeTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-13 18:24:36
 */
public class XrangeOneTest extends BaseTestForStream {
	@Override
	public void run() {
		StreamEntryID streamEntryID = new StreamEntryID("1572847649042-0");

		try (DevJedis devJedis = new DevJedis(jedis())) {
			Map<String, String> map = devJedis.xrangeOne(CONFIG_KEY_STREAM, streamEntryID);

			if (map == null) {
				System.out.println("No data!");
				return;
			}

			for (Map.Entry<String, String> item : map.entrySet()) {
				System.out.println(String.format("key: %s, value: %s", item.getKey(), item.getValue()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XrangeOneTest.class);
	}
}