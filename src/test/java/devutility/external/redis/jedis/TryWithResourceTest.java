package devutility.external.redis.jedis;

import devutility.external.redis.BaseTestForDuer;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TryWithResourceTest extends BaseTestForDuer {
	@Override
	public void run() {
		JedisPool jedisPool = jedisPool();
		test(jedisPool);
		test(jedisPool);
		test(jedisPool);
		test(jedisPool);
		test(jedisPool);
		
		System.out.println("NumActive: " + jedisPool.getNumActive());
		System.out.println("NumIdle: " + jedisPool.getNumIdle());
	}

	void test(JedisPool jedisPool) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.select(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(TryWithResourceTest.class);
	}
}