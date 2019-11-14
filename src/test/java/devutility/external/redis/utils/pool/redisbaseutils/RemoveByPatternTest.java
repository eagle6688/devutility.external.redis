package devutility.external.redis.utils.pool.redisbaseutils;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.utils.RedisStringUtils;
import devutility.internal.test.TestExecutor;

public class RemoveByPatternTest extends BaseTestForDuer {
	@Override
	public void run() {
		String pattern = "asd*";
		System.out.println(RedisStringUtils.removeByPattern(jedis(), pattern));
	}

	public static void main(String[] args) {
		TestExecutor.run(RemoveByPatternTest.class);
	}
}