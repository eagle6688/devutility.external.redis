package devutility.external.redis.queue.stream.consumer.exception;

import devutility.external.redis.queue.stream.consumer.BaseStreamConsumerEvent;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * FailedStreamConsumerEvent
 * 
 * @author: Aldwin Su
 * @version: 2019-11-17 23:03:26
 */
public class StreamConsumerEvent4Exception extends BaseStreamConsumerEvent {
	@Override
	public boolean onPendingMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		logPendingMessage(key, values);
		return process(streamEntryId);
	}

	@Override
	public boolean onMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		logMessage(key, values);
		return process(streamEntryId);
	}

	private boolean process(StreamEntryID streamEntryId) {
		System.out.println(String.format("Id: %s exception before ack process！", streamEntryId.toString()));
		throw new RuntimeException("Process exception!");
	}
}