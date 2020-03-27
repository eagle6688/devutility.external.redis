package devutility.external.redis.queue.stream;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

/**
 * 
 * XlenTest
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 21:54:59
 */
public class XlenTest extends BaseTestForDuer {
	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			System.out.println(jedis.xlen(StreamData.KEY));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XlenTest.class);
	}
}