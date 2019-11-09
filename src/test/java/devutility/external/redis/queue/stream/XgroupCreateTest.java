package devutility.external.redis.queue.stream;

import devutility.external.redis.BaseTest;
import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

/**
 * 
 * XgroupCreateTest
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 21:58:35
 */
public class XgroupCreateTest extends BaseTest {
	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			String result = jedis.xgroupCreate(CONFIG_KEY_STREAM, StreamData.GROUPNAME, null, true);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XgroupCreateTest.class);
	}
}