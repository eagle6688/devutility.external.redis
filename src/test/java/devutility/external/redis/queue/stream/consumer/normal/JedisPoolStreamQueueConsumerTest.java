package devutility.external.redis.queue.stream.consumer.normal;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.stream.JedisPoolStreamQueueConsumer;
import devutility.external.redis.queue.stream.JedisStreamQueueConsumerEvent;
import devutility.internal.test.TestExecutor;

/**
 * 
 * JedisPoolStreamQueueConsumerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-15 19:50:05
 */
public class JedisPoolStreamQueueConsumerTest extends BaseTestForDuer {
	private JedisStreamQueueConsumerEvent consumerEvent = new StreamConsumerEvent();

	@Override
	public void run() {
		try (JedisQueueConsumer consumer = new JedisPoolStreamQueueConsumer(jedisPool(), redisQueueOption, consumerEvent)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(JedisPoolStreamQueueConsumerTest.class);
	}
}