package devutility.external.redis.queue.list;

import java.util.Date;

import devutility.external.redis.BaseTest;
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
public class ConsumerTest extends BaseTest {
	private JedisQueueConsumerEvent consumerEvent = new ConsumerHandler();

	@Override
	public void run() {
		System.out.println(new Date());

		try (JedisP2PQueueConsumer consumer = new JedisP2PQueueConsumer(jedis(), consumerEvent, Config.QUEUE_KEY)) {
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