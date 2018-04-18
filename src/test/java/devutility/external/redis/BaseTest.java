package devutility.external.redis;

public abstract class BaseTest extends devutility.internal.test.BaseTest {
	protected RedisStringHelper redisStringHelper = RedisUtils.redisStringHelper("dbconfig.properties", "redis");
}