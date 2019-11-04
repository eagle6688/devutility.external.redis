package devutility.external.redis.queue.stream;

import devutility.external.redis.BaseTest;
import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * XackTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-04 13:54:59
 */
public class XackTest extends BaseTest {
	@Override
	public void run() {
		StreamEntryID streamEntryID = new StreamEntryID("1572529192472-0");

		try (Jedis jedis = jedis()) {
			long result = jedis.xack(CONFIG_KEY_STREAM, StreamData.GROUPNAME, streamEntryID);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XackTest.class);
	}
}