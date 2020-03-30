package devutility.external.redis.helper;

import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.utils.BaseJedisUtils;
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
	private static ThreadLocal<Jedis> JEDIS_THREADLOCAL = new ThreadLocal<>();

	/**
	 * JedisPool object.
	 */
	private JedisPool jedisPool;

	/**
	 * Constructor
	 * @param jedisPool JedisPool object
	 */
	public JedisThreadLocal(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
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

		Jedis jedis = JEDIS_THREADLOCAL.get();

		if (jedis != null && !jedis.getClient().isBroken()) {
			if (!jedis.isConnected()) {
				jedis.connect();
			}

			if (BaseJedisUtils.select(jedis, database)) {
				return jedis;
			}
		}

		synchronized (JEDIS_THREADLOCAL) {
			jedis = jedisPool.getResource();
			JEDIS_THREADLOCAL.set(jedis);
		}

		if (!BaseJedisUtils.select(jedis, database)) {
			throw new JedisFatalException("Can't select database in Redis!");
		}

		return jedis;
	}

	/**
	 * Close Jedis connection and remove it from ThreadLocal.
	 */
	public void close() {
		Jedis jedis = JEDIS_THREADLOCAL.get();

		if (jedis == null) {
			return;
		}

		try {
			jedis.close();
			jedis.resetState();
		} catch (Exception e) {
			System.out.println("Close Jedis failed!");
		}

		jedis = null;
		JEDIS_THREADLOCAL.remove();
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}