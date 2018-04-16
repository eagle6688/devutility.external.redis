package devutility.external.redis.jedisfactory;

import devutility.internal.base.SingletonFactory;
import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.JedisPool;

public class JedisPoolTest extends BaseTest {
	@Override
	public void run() {
		JedisPool jedisPool = SingletonFactory.create(JedisPool.class);

		if (jedisPool == null) {
			println("Null");
		}
	}

	public static void main(String[] args) {
		TestExecutor.concurrentRun(100, JedisPoolTest.class, (data) -> {
			System.out.println(data);
		});
	}
}