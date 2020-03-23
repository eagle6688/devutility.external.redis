package devutility.external.redis.redisinstanceutils;

import devutility.external.redis.BaseTestForDuer;
import devutility.internal.test.TestExecutor;

public class GetSingleRedisInstanceTest extends BaseTestForDuer {
	@Override
	public void run() {
		System.out.println(singleRedisInstance.isTestOnBorrow());
	}

	public static void main(String[] args) {
		TestExecutor.run(GetSingleRedisInstanceTest.class);
	}
}