package devutility.external.redis.queue.stream;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.internal.test.TestExecutor;

/**
 * 
 * JedisPoolStreamQueueConsumerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-15 19:50:05
 */
public class JedisPoolStreamQueueConsumerTest extends BaseTestForDuer {
	private JedisQueueConsumerEvent consumerEvent = new StreamConsumerHandler();

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