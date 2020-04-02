package devutility.external.redis.models;

import redis.clients.jedis.Jedis;

/**
 * 
 * JedisThreadItem
 * 
 * @author: Aldwin Su
 * @version: 2020-04-03 00:00:40
 */
public class JedisThreadItem {
	private Jedis jedis;
	private long lastUseTime;

	public JedisThreadItem(Jedis jedis) {
		this.jedis = jedis;
		lastUseTime = System.currentTimeMillis();
	}

	public boolean isExpired(int maxIdleMillis) {
		if (maxIdleMillis <= 0) {
			return false;
		}

		if ((System.currentTimeMillis() - lastUseTime) >= maxIdleMillis) {
			return true;
		}

		return false;
	}

	public Jedis getJedis() {
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