package devutility.external.redis;

import devutility.external.redis.helpers.RedisStringHelper;
import devutility.external.redis.models.RedisInstance;

public abstract class BaseTest extends devutility.internal.test.BaseTest {
	protected RedisStringHelper redisStringHelper = RedisHelperFactory.redisStringHelper("dbconfig.properties", "redis");

	protected RedisInstance sentinel_RedisInstance = RedisInstanceUtils.get("dbconfig.properties", "sentinel");
}