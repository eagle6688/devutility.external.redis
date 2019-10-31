package devutility.external.redis.stream;

import devutility.external.redis.BaseTest;
import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

public class XaddTest extends BaseTest {
	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			StreamEntryID streamEntryID = jedis.xadd(CONFIG_KEY_STREAM, null, StreamData.DATA);
			System.out.println(streamEntryID.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XaddTest.class);
	}
}