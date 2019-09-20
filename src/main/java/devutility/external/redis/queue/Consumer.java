package devutility.external.redis.queue;

/**
 * 
 * Consumer for Redis queue.
 * 
 * @author: Aldwin Su
 * @version: 2019-09-20 17:15:55
 */
public interface Consumer {
	/**
	 * Triggered while message received in consumer side.
	 * @param topic Message key.
	 * @param message Message value.
	 */
	void onMessage(String topic, String message);
}