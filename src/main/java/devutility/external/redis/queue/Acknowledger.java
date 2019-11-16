package devutility.external.redis.queue;

import redis.clients.jedis.StreamEntryID;

/**
 * 
 * Acknowledger
 * 
 * @author: Aldwin Su
 * @version: 2019-11-16 16:57:51
 */
public interface Acknowledger {
	/**
	 * Acknowledge one message.
	 * @param streamEntryId: StreamEntryID object.
	 */
	void ack(StreamEntryID streamEntryId);
}