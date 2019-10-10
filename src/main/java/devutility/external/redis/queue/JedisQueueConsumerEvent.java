package devutility.external.redis.queue;

/**
 * 
 * Consumer event of Redis queue.
 * 
 * @author: Aldwin Su
 * @version: 2019-09-20 17:15:55
 */
public interface JedisQueueConsumerEvent {
	/**
	 * Triggered while message received in consumer side.
	 * @param topic Redis key of queue.
	 * @param message Message value.
	 * @return boolean This result is very important for Stream queue, if true will send 'ACK' to redis server and the
	 *         message will be removed otherwise will not.
	 */
	boolean onMessage(String topic, String message);
}