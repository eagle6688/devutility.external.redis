package devutility.external.redis.queue.stream;

import devutility.external.redis.queue.JedisQueueConsumerEvent;

/**
 * 
 * ConsumerHandler
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:26:34
 */
public class StreamConsumerHandler implements JedisQueueConsumerEvent {
	@Override
	public boolean onMessage(String key, Object... values) {
		System.out.println(String.format("%s Triggering onMessage event, key:\"%s\", id: \"%s\", value: %s", Thread.currentThread().getName(), key, values[0].toString(), values[1].toString()));
		return true;
	}
}