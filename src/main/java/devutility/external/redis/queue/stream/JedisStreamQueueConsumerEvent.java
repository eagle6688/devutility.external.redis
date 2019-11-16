package devutility.external.redis.queue.stream;

import devutility.external.redis.queue.Acknowledger;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * JedisStreamQueueConsumerEvent
 * 
 * @author: Aldwin Su
 * @version: 2019-11-16 16:51:46
 */
public abstract class JedisStreamQueueConsumerEvent implements JedisQueueConsumerEvent {
	/**
	 * Used for acknowledge message.
	 */
	private Acknowledger acknowledger;

	public void setAcknowledger(Acknowledger acknowledger) {
		this.acknowledger = acknowledger;
	}

	/**
	 * Acknowledge one message.
	 * @param streamEntryId: StreamEntryID object.
	 */
	protected void ack(StreamEntryID streamEntryId) {
		if (acknowledger == null) {
			return;
		}

		acknowledger.ack(streamEntryId);
	}
}