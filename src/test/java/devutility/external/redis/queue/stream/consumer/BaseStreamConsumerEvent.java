package devutility.external.redis.queue.stream.consumer;

import devutility.external.redis.queue.stream.JedisStreamQueueConsumerEvent;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * ConsumerHandler
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:26:34
 */
public abstract class BaseStreamConsumerEvent extends JedisStreamQueueConsumerEvent {
	protected void logPendingMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		System.out.println(String.format("%s Triggering onPendingMessage event, key:\"%s\", id: \"%s\", value: %s", Thread.currentThread().getName(), key, streamEntryId, values[1].toString()));
	}

	protected void logMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		System.out.println(String.format("%s Triggering onMessage event, key:\"%s\", id: \"%s\", value: %s", Thread.currentThread().getName(), key, streamEntryId, values[1].toString()));
	}
}