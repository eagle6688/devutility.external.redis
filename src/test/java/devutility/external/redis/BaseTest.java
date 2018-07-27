package devutility.external.redis;

import devutility.external.redis.helpers.RedisStringHelper;

public abstract class BaseTest extends devutility.internal.test.BaseTest {
	protected RedisStringHelper redisStringHelper = RedisUtils.redisStringHelper("dbconfig.properties", "redis");
}