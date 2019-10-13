package devutility.external.redis.queue.list;

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
	public boolean onMessage(String key, Object value) {
		System.out.println(String.format("%s Triggering onMessage event, topic:\"%s\", message: \"%s\"", Thread.currentThread().getName(), key, value.toString()));
		return true;
	}
}