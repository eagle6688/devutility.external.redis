package devutility.external.redis.queue.stream.consumer.retryconnection;

import devutility.external.redis.exception.JedisConnectionException;
import devutility.external.redis.queue.stream.consumer.BaseStreamConsumerEvent;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * ConnectionRetryConsumerEvent
 * 
 * @author: Aldwin Su
 * @version: 2020-05-07 17:16:00
 */
public class ConnectionRetryConsumerEvent extends BaseStreamConsumerEvent {
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
		System.out.println(String.format("Id: %s retry connection process！", streamEntryId.toString()));
		throw new JedisConnectionException("Connection retry exception!");
	}
}