package devutility.external.redis.utils.pool.redisbaseutils;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.utils.JedisStringUtils;
import devutility.internal.test.TestExecutor;

public class RemoveByPatternTest extends BaseTestForDuer {
	@Override
	public void run() {
		String pattern = "asd*";
		System.out.println(JedisStringUtils.removeByPattern(jedis(), pattern));
	}

	public static void main(String[] args) {
		TestExecutor.run(RemoveByPatternTest.class);
	}
}