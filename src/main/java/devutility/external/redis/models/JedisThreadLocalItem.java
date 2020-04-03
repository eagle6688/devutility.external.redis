package devutility.external.redis.models;

import redis.clients.jedis.Jedis;

/**
 * 
 * JedisThreadItem
 * 
 * @author: Aldwin Su
 * @version: 2020-04-03 00:00:40
 */
public class JedisThreadLocalItem {
	private Jedis jedis;
	private long lastUseTime;

	/**
	 * Constructor
	 * @param jedis Jedis object.
	 */
	public JedisThreadLocalItem(Jedis jedis) {
		this.jedis = jedis;
		lastUseTime = System.currentTimeMillis();
	}

	/**
	 * Check whether the current Jedis object has been expired or not?
	 * @param maxIdleMillis Max idle milliseconds for each Jedis link, default value 0 is not limited.
	 * @return boolean
	 */
	public boolean isExpired(int maxIdleMillis) {
		if (maxIdleMillis <= 0) {
			return false;
		}

		if ((System.currentTimeMillis() - lastUseTime) >= maxIdleMillis) {
			return true;
		}

		return false;
	}

	/**
	 * Refresh the last use time for current item.
	 */
	public void refresh() {
		this.setLastUseTime(System.currentTimeMillis());
	}

	public void close() {
		Jedis jedis = getJedis();

		if (jedis == null) {
			return;
		}

		try {
			jedis.close();
			jedis.resetState();
		} catch (Exception e) {
			System.err.println("Close Jedis failed!");
		}
	}

	public Jedis getJedis() {
		return jedis;
	}

	/**
	 * Get Jedis object from current item.
	 * @param maxIdleMillis Max idle milliseconds for each Jedis link, default value 0 is not limited.
	 * @return Jedis
	 */
	public Jedis getJedis(int maxIdleMillis) {
		Jedis jedis = this.getJedis();

		if (this.isExpired(maxIdleMillis)) {
			try {
				jedis.ping();
				refresh();
				return jedis;
			} catch (Exception e) {
				jedis.close();
				return null;
			}
		}

		if (jedis.getClient().isBroken()) {
			jedis.close();
			return null;
		}

		if (!jedis.isConnected()) {
			jedis.connect();
		}

		refresh();
		return jedis;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	public long getLastUseTime() {
		return lastUseTime;
	}

	public void setLastUseTime(long lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
}