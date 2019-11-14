package devutility.external.redis.queue.stream;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.Config;
import devutility.external.redis.queue.ConsumerHandler;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.internal.test.TestExecutor;

/**
 * 
 * JedisStreamQueueConsumerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-14 16:01:55
 */
public class JedisStreamQueueConsumerTest extends BaseTestForDuer {
	private JedisQueueConsumerEvent consumerEvent = new ConsumerHandler();

	@Override
	public void run() {
		try (JedisQueueConsumer consumer = new JedisStreamQueueConsumer(jedis(), Config.QUEUE_KEY, consumerEvent)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(JedisStreamQueueConsumerTest.class);
	}
}