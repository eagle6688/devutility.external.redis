package devutility.external.redis.redisinstanceutils;

import java.util.Properties;

import devutility.external.redis.RedisInstanceUtils;
import devutility.external.redis.model.SentinelRedisInstance;
import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;
import devutility.internal.util.PropertiesUtils;

public class SetSentinelRedisInstanceTest extends BaseTest {
	@Override
	public void run() {
		Properties properties = PropertiesUtils.getPropertiesFromResource("dbconfig.properties");
		SentinelRedisInstance instance = new SentinelRedisInstance();
		RedisInstanceUtils.set(instance, properties, "sentinel");
		println(instance.getMaxRetryCount());
		println(instance.getNodes());
		println(instance.getMasterName());
	}

	public static void main(String[] args) {
		TestExecutor.run(SetSentinelRedisInstanceTest.class);
	}
}