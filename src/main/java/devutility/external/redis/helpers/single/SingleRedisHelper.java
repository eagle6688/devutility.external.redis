package devutility.external.redis.helpers.single;

import devutility.external.redis.helpers.RedisHelper;
import devutility.external.redis.models.SingleRedisInstance;
import devutility.external.redis.utils.BaseRedisUtils;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import redis.clients.jedis.Jedis;

public abstract class SingleRedisHelper extends RedisHelper {
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
	 * Set database index.
	 * @param database: Redis database index.
	 */
	public void setDatabase(int database) {
		redisInstance.setDatabase(database);
	}

	/**
	 * Get database index.
	 * @return int
	 */
	public int getDatabase() {
		return redisInstance.getDatabase();
	}

	/**
	 * Set expired time for the item with the specified key.
	 * @param key: Redis key.
	 * @param seconds: Expire seconds.
	 * @return boolean
	 */
	public boolean expire(String key, int seconds) {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return BaseRedisUtils.expire(jedis, key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove the item with the specified key.
	 * @param key: Redis key.
	 * @return boolean
	 */
	public boolean remove(String key) {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return BaseRedisUtils.remove(jedis, key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}