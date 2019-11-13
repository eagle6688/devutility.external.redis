package devutility.external.redis.queue.stream;

import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * XdelTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-13 16:43:39
 */
public class XdelTest extends BaseTestForStream {
	@Override
	public void run() {
		StreamEntryID streamEntryID = new StreamEntryID("1572529192472-0");

		try (Jedis jedis = jedis()) {
			long result = jedis.xdel(CONFIG_KEY_STREAM, streamEntryID);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XdelTest.class);
	}
}