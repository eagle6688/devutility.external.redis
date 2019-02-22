package devutility.external.redis.utils.pool.redisbaseutils;

import devutility.external.redis.BaseTest;
import devutility.external.redis.utils.RedisStringUtils;
import devutility.internal.test.TestExecutor;

public class RemoveByPatternTest extends BaseTest {
	@Override
	public void run() {
		String pattern = "asd*";
		System.out.println(RedisStringUtils.removeByPattern(jedis(), pattern));
	}

	public static void main(String[] args) {
		TestExecutor.run(RemoveByPatternTest.class);
	}
}