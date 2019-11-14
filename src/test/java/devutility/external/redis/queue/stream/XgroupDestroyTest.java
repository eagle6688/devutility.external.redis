package devutility.external.redis.queue.stream;

import devutility.external.redis.BaseTestForDuer;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

/**
 * 
 * XgroupDestroyTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-09 13:23:16
 */
public class XgroupDestroyTest extends BaseTestForDuer {
	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			long result = jedis.xgroupDestroy(CONFIG_KEY_STREAM, "asd");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XgroupDestroyTest.class);
	}
}