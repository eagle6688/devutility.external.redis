package devutility.external.redis.queue.p2p;

import devutility.external.redis.BaseTest;
import devutility.external.redis.queue.ConsumerEvent;
import devutility.internal.test.TestExecutor;

/**
 * 
 * ConsumerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:24:38
 */
public class ConsumerTest extends BaseTest {
	private final static String QUEUE_KEY = "p2p-queue-test";
	private ConsumerEvent consumerEvent = new ConsumerHandler();

	@Override
	public void run() {
		try (JedisP2PQueueConsumer consumer = new JedisP2PQueueConsumer(jedis(), consumerEvent, QUEUE_KEY)) {
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