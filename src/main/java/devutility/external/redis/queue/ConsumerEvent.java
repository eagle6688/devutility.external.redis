package devutility.external.redis.queue;

/**
 * 
 * Consumer event of Redis queue.
 * 
 * @author: Aldwin Su
 * @version: 2019-09-20 17:15:55
 */
public interface ConsumerEvent {
	/**
	 * Triggered while message received in consumer side.
	 * @param topic Redis key of queue.
	 * @param message Message value.
	 */
	void onMessage(String topic, String message);
}