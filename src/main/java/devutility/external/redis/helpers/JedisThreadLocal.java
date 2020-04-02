package devutility.external.redis.helpers;

import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.models.JedisThreadItem;
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
	private static ThreadLocal<JedisThreadItem> JEDIS_THREADLOCAL = new ThreadLocal<>();

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
	}

	/**
	 * Constructor
	 * @param jedisPool JedisPool object
	 */
	public JedisThreadLocal(JedisPool jedisPool) {
		this(jedisPool, 0);
	}

	/**
	 * Get Jedis object from ThreadLocal.
	 * @param database Redis database number
	 * @return Jedis
	 */
	public Jedis get(int database) {
		if (jedisPool == null) {
			throw new IllegalArgumentException("jedisPool can't be null!");
		}

		JedisThreadItem jedisThreadItem = JEDIS_THREADLOCAL.get();

		if (jedisThreadItem != null && jedisThreadItem.getJedis() != null) {
			Jedis jedis = jedisThreadItem.getJedis();

			if (jedisThreadItem.isExpired(maxIdleMillis)) {
				try {
					jedis.ping();
				} catch (Exception e) {
					jedis.close();
				}
			}

			if (jedis.getClient().isBroken()) {
				jedis.close();
			} else {
				if (!jedis.isConnected()) {
					jedis.connect();
				}

				if (!BaseRedisUtils.select(jedis, database)) {
					throw new JedisFatalException("Redis object can't select database!");
				}

				jedisThreadItem.setLastUseTime(System.currentTimeMillis());
				return jedis;
			}
		}

		synchronized (JEDIS_THREADLOCAL) {
			jedisThreadItem = new JedisThreadItem(jedisPool.getResource());
			JEDIS_THREADLOCAL.set(jedisThreadItem);
		}

		if (!BaseRedisUtils.select(jedisThreadItem.getJedis(), database)) {
			throw new JedisFatalException("Redis object can't select database!");
		}

		return jedisThreadItem.getJedis();
	}

	/**
	 * Close Jedis connection and remove it from ThreadLocal.
	 */
	public void close() {
		JedisThreadItem jedisThreadItem = JEDIS_THREADLOCAL.get();

		if (jedisThreadItem == null || jedisThreadItem.getJedis() == null) {
			return;
		}

		Jedis jedis = jedisThreadItem.getJedis();

		try {
			jedis.close();
			jedis.resetState();
		} catch (Exception e) {
			System.out.println("Close Jedis failed!");
		}

		jedis = null;
		JEDIS_THREADLOCAL.remove();
	}

	private Jedis getAvailableJedis(JedisThreadItem jedisThreadItem, int database) {
		if (jedisThreadItem == null || jedisThreadItem.getJedis() == null) {
			return null;
		}

		Jedis jedis = jedisThreadItem.getJedis();

		if (jedisThreadItem.isExpired(maxIdleMillis)) {
			try {
				jedis.ping();
			} catch (Exception e) {
				jedis.close();
				return null;
			}

			if (!BaseRedisUtils.select(jedis, database)) {
				throw new JedisFatalException("Redis object can't select database!");
			}

			return jedis;
		}

		if (jedis.getClient().isBroken()) {
			jedis.close();
		} else {
			if (!jedis.isConnected()) {
				jedis.connect();
			}

			if (!BaseRedisUtils.select(jedis, database)) {
				throw new JedisFatalException("Redis object can't select database!");
			}

			jedisThreadItem.setLastUseTime(System.currentTimeMillis());
			return jedis;
		}

		return null;
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