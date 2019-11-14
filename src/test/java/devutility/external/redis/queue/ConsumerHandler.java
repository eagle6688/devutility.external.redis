package devutility.external.redis.queue;

import devutility.external.redis.queue.JedisQueueConsumerEvent;

/**
 * 
 * ConsumerHandler
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:26:34
 */
public class ConsumerHandler implements JedisQueueConsumerEvent {
	@Override
	public boolean onMessage(String key, Object... values) {
		System.out.println(String.format("%s Triggering onMessage event, topic:\"%s\", message: \"%s\"", Thread.currentThread().getName(), key, values[0].toString()));
		return true;
	}
}