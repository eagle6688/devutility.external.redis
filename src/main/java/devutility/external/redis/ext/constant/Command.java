package devutility.external.redis.ext.constant;

import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * Command
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 21:11:55
 */
public enum Command implements ProtocolCommand {
	XINFO;

	private final byte[] raw;

	Command() {
		raw = SafeEncoder.encode(this.name());
	}

	@Override
	public byte[] getRaw() {
		return raw;
	}
}