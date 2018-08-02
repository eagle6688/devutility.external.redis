package devutility.external.redis;

import devutility.external.redis.helpers.single.SingleRedisStringHelper;
import devutility.external.redis.models.SentinelRedisInstance;

public abstract class BaseTest extends devutility.internal.test.BaseTest {
	protected SingleRedisStringHelper singleRedisStringHelper = RedisHelperFactory.singleRedisStringHelper("dbconfig.properties", "redis");

	protected SentinelRedisInstance sentinel_RedisInstance = null;//RedisInstanceUtils.get("dbconfig.properties", "sentinel");
}