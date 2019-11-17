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
	 * Triggered while pending message received in consumer side.
	 * @param key Redis key of queue.
	 * @param values Message values.
	 * @return boolean This result is very important for Stream queue, if true will send 'ACK' to redis server and the
	 *         message will be removed otherwise will not.
	 */
	boolean onPendingMessage(String key, Object... values);

	/**
	 * Triggered while message received in consumer side.
	 * @param key Redis key of queue.
	 * @param values Message values.
	 * @return boolean This result is very important for Stream queue, if true will send 'ACK' to redis server and the
	 *         message will be removed otherwise will not.
	 */
	boolean onMessage(String key, Object... values);
}