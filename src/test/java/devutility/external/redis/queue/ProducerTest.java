package devutility.external.redis.queue;

import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

/**
 * 
 * ProducerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:38:30
 */
public class ProducerTest extends BaseTest {
	private int count = 100;

	private String getMessage(int index) {
		return String.format("Hello Word! Index %d", index);
	}

	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			for (int i = 0; i < count; i++) {
				jedis.lpush(Config.QUEUE_KEY, getMessage(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(ProducerTest.class);
	}
}