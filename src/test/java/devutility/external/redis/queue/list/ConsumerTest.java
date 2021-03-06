package devutility.external.redis.queue.list;

import java.util.Date;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.Config;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.internal.test.TestExecutor;

/**
 * 
 * ConsumerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:24:38
 */
public class ConsumerTest extends BaseTestForDuer {
	private JedisQueueConsumerEvent consumerEvent = new ListConsumerHandler();

	@Override
	public void run() {
		System.out.println(new Date());

		try (JedisListQueueConsumer consumer = new JedisListQueueConsumer(jedis(), Config.QUEUE_KEY, consumerEvent)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Consumer listening completely!");
	}

	public static void main(String[] args) {
		TestExecutor.run(ConsumerTest.class);
	}
}