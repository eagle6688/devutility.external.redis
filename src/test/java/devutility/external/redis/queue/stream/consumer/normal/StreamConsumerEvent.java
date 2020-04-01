package devutility.external.redis.queue.stream.consumer.normal;

import devutility.external.redis.queue.stream.consumer.BaseStreamConsumerEvent;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * NormalStreamConsumerEvent
 * 
 * @author: Aldwin Su
 * @version: 2019-11-17 23:11:47
 */
public class StreamConsumerEvent extends BaseStreamConsumerEvent {
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
		System.out.println(String.format("Id: %s normal process！", streamEntryId.toString()));

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return true;
	}
}