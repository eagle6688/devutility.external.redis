package devutility.external.redis.stream;

import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

/**
 * 
 * XlenTest
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 21:54:59
 */
public class XlenTest extends BaseTest {
	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			System.out.println(jedis.xlen(CONFIG_KEY_STREAM));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XlenTest.class);
	}
}