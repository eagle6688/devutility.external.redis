package devutility.external.redis.helper.jedisthreadlocal;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.helpers.JedisThreadLocal;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

public class GetJedisTest extends BaseTestForDuer {
	private int sleepMillis = 6000;
	private String threadName = Thread.currentThread().getName();

	@Override
	public void run() {
		System.out.println("Starting exec " + threadName + " ...");
		JedisThreadLocal jedisThreadLocal = new JedisThreadLocal(JEDIS_POOL, 3000);
		ping(jedisThreadLocal.get());

		try {
			Thread.sleep(sleepMillis);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		ping(jedisThreadLocal.get());

		try {
			Thread.sleep(sleepMillis);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		jedisThreadLocal.get().close();
		JEDIS_POOL.destroy();
		System.out.println("Executing " + threadName + " completed.");
	}

	void ping(Jedis jedis) {
		try {
			jedis.ping();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.concurrentRun(10, GetJedisTest.class, i -> {
			System.out.println("Finished!");
		});
	}
}