package devutility.external.redis.queue.list;

import devutility.external.redis.queue.JedisQueueConsumerEvent;

/**
 * 
 * ConsumerHandler
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:26:34
 */
public class ListConsumerHandler implements JedisQueueConsumerEvent {
	@Override
	public boolean onMessage(String key, Object... values) {
		System.out.println(String.format("%s Triggering onMessage event, key:\"%s\", message: \"%s\"", Thread.currentThread().getName(), key, values[0].toString()));
		return true;
	}

	@Override
	public boolean onPendingMessage(String key, Object... values) {
		return false;
	}

	@Override
	public void onError(Throwable cause) {

	}
}