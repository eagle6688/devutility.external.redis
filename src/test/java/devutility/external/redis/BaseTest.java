package devutility.external.redis;

import devutility.external.redis.helpers.single.SingleRedisStringHelper;
import devutility.external.redis.models.SentinelRedisInstance;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import redis.clients.jedis.Jedis;

public abstract class BaseTest extends devutility.internal.test.BaseTest {
	protected SingleRedisStringHelper singleRedisStringHelper = RedisHelperFactory.singleRedisStringHelper("dbconfig.properties", "redis");

	protected SentinelRedisInstance sentinel_RedisInstance = RedisInstanceUtils.get("dbconfig.properties", "sentinel", SentinelRedisInstance.class);

	protected Jedis jedis() {
		SingleRedisInstance redisInstance = RedisInstanceUtils.get("dbconfig.properties", "redis", SingleRedisInstance.class);
		return JedisPoolUtil.jedis(redisInstance);
	}
}