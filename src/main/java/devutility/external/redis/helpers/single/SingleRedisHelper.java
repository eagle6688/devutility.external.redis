package devutility.external.redis.helpers.single;

import devutility.external.redis.helpers.RedisStringHelper;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.external.redis.utils.JedisPoolUtil;
import devutility.internal.lang.StringHelper;
import redis.clients.jedis.Jedis;

public abstract class SingleRedisHelper extends RedisStringHelper {
	/**
	 * RedisInstance object
	 */
	protected SingleRedisInstance redisInstance;

	/**
	 * Constructor
	 * @param redisInstance
	 */
	public SingleRedisHelper(SingleRedisInstance redisInstance) {
		super(redisInstance);
		this.redisInstance = redisInstance;
	}

	/**
	 * Expire one item.
	 * @param key: Redis key
	 * @param seconds: Expire seconds
	 * @return boolean
	 */
	public boolean expire(String key, int seconds) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return jedis.expire(key, seconds) == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove one item from Redis.
	 * @param key: Redis key.
	 * @return boolean
	 */
	public boolean remove(String key) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return jedis.del(key) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}