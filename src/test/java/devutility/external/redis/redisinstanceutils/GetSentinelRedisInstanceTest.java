package devutility.external.redis.redisinstanceutils;

import devutility.external.redis.ConfigUtils;
import devutility.external.redis.model.SentinelRedisInstance;
import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;

public class GetSentinelRedisInstanceTest extends BaseTest {
	@Override
	public void run() {
		SentinelRedisInstance instance = ConfigUtils.getRedisInstanceFromResources("dbconfig.properties", "sentinel", SentinelRedisInstance.class);
		println(instance.getMaxRetryCount());
		println(instance.getNodes());
		println(instance.getMasterName());
	}

	public static void main(String[] args) {
		TestExecutor.run(GetSentinelRedisInstanceTest.class);
	}
}