package devutility.external.redis.ext.constant;

import java.util.Locale;

import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * Keyword
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 21:13:21
 */
public enum Keyword {
	GROUPS, CONSUMERS;

	public final byte[] raw;

	Keyword() {
		raw = SafeEncoder.encode(this.name().toLowerCase(Locale.ENGLISH));
	}
}