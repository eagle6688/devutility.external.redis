package devutility.external.redis.queue.p2p;

import devutility.external.redis.queue.ConsumerEvent;

/**
 * 
 * ConsumerHandler
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:26:34
 */
public class ConsumerHandler implements ConsumerEvent {
	@Override
	public void onMessage(String topic, String message) {
		System.out.println(String.format("%s Triggering onMessage event, topic:\"%s\", message: \"%s\"", Thread.currentThread().getName(), topic, message));
	}
}