package devutility.external.redis.queue.stream;

import redis.clients.jedis.StreamEntryID;

/**
 * 
 * ConsumerHandler
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:26:34
 */
public class StreamConsumerHandler extends JedisStreamQueueConsumerEvent {
	@Override
	public boolean onPendingMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		System.out.println(String.format("%s Triggering onPendingMessage event, key:\"%s\", id: \"%s\", value: %s", Thread.currentThread().getName(), key, streamEntryId, values[1].toString()));
		return process(streamEntryId);
	}

	@Override
	public boolean onMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		System.out.println(String.format("%s Triggering onMessage event, key:\"%s\", id: \"%s\", value: %s", Thread.currentThread().getName(), key, streamEntryId, values[1].toString()));
		return process(streamEntryId);
	}

	@Override
	public void onError(Throwable cause) {

	}

	private boolean process(StreamEntryID streamEntryId) {
		//return normal(streamEntryId);
		//return failed(streamEntryId);
		//return internalAck(streamEntryId);
		return exceptionBeforeAck(streamEntryId);
	}

	boolean normal(StreamEntryID streamEntryId) {
		System.out.println(String.format("Id: %s normal process！", streamEntryId.toString()));
		return true;
	}

	boolean failed(StreamEntryID streamEntryId) {
		System.out.println(String.format("Id: %s failed process！", streamEntryId.toString()));
		return false;
	}

	boolean internalAck(StreamEntryID streamEntryId) {
		System.out.println(String.format("Id: %s internalAck process！", streamEntryId.toString()));
		ack(streamEntryId);
		return false;
	}

	boolean exceptionBeforeAck(StreamEntryID streamEntryId) {
		System.out.println(String.format("Id: %s exceptionBeforeAck process！", streamEntryId.toString()));
		throw new RuntimeException("Process exception!");
	}

}