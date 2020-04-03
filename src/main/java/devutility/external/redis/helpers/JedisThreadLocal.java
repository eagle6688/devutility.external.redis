package devutility.external.redis.helpers;

import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.models.JedisThreadLocalItem;
import devutility.external.redis.utils.BaseRedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * JedisThreadLocal
 * 
 * @author: Aldwin Su
 * @version: 2019-09-29 20:46:36
 */
public class JedisThreadLocal {
	/**
	 * ThreadLocal object to save Jedis object.
	 */
	private static ThreadLocal<JedisThreadLocalItem> JEDIS_THREADLOCAL = new ThreadLocal<>();

	/**
	 * JedisPool object.
	 */
	private JedisPool jedisPool;

	/**
	 * Same field maxIdleMillis in RedisInstance object.
	 */
	private int maxIdleMillis;

	/**
	 * Constructor
	 * @param jedisPool JedisPool object.
	 * @param maxIdleMillis Max idle milliseconds for each Jedis link, default value 0 is not limited.
	 */
	public JedisThreadLocal(JedisPool jedisPool, int maxIdleMillis) {
		this.jedisPool = jedisPool;
		this.maxIdleMillis = maxIdleMillis;

		if (this.jedisPool == null) {
			throw new IllegalArgumentException("jedisPool can't be null!");
		}
	}

	/**
	 * Constructor
	 * @param jedisPool JedisPool object
	 */
	public JedisThreadLocal(JedisPool jedisPool) {
		this(jedisPool, 0);
	}

	/**
	 * Get an Jedis object from ThreadLocal or create a new one from JedisPool object.
	 * @return Jedis
	 */
	public Jedis get() {
		JedisThreadLocalItem threadLocalItem = JEDIS_THREADLOCAL.get();

		if (threadLocalItem != null) {
			Jedis jedis = threadLocalItem.getJedis(maxIdleMillis);

			if (jedis != null) {
				return jedis;
			}
		}

		synchronized (JEDIS_THREADLOCAL) {
			threadLocalItem = new JedisThreadLocalItem(jedisPool.getResource());
			JEDIS_THREADLOCAL.set(threadLocalItem);
		}

		return threadLocalItem.getJedis();
	}

	/**
	 * Get Jedis object from ThreadLocal.
	 * @param database Redis database number
	 * @return Jedis
	 */
	public Jedis get(int database) {
		Jedis jedis = get();

		if (!BaseRedisUtils.select(jedis, database)) {
			throw new JedisFatalException("Redis object can't select database!");
		}

		return jedis;
	}

	/**
	 * Close Jedis connection and remove it from ThreadLocal.
	 */
	public void close() {
		JedisThreadLocalItem threadLocalItem = JEDIS_THREADLOCAL.get();

		if (threadLocalItem == null) {
			return;
		}

		threadLocalItem.close();
		JEDIS_THREADLOCAL.remove();
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public int getMaxIdleMillis() {
		return maxIdleMillis;
	}

	public void setMaxIdleMillis(int maxIdleMillis) {
		this.maxIdleMillis = maxIdleMillis;
	}
}