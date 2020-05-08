package devutility.external.redis.queue.stream.consumer.retryconnection.approver;

import devutility.external.redis.queue.stream.consumer.BaseStreamConsumerEvent;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * 
 * ConnectionRetry4ApproverConsumerEvent
 * 
 * @author: Aldwin Su
 * @version: 2020-05-08 18:34:41
 */
public class ConnectionRetry4ApproverConsumerEvent extends BaseStreamConsumerEvent {
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
		System.out.println(String.format("Id: %s retry connection for approver process!", streamEntryId.toString()));
		throw new JedisDataException("Connection retry exception, with JedisDataException!");
	}
}