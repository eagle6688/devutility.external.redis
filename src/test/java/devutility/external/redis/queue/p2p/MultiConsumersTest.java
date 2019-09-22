package devutility.external.redis.queue.p2p;

import devutility.external.redis.BaseTest;
import devutility.external.redis.queue.Config;
import devutility.external.redis.queue.ConsumerEvent;
import devutility.internal.test.TestExecutor;

/**
 * 
 * MultiConsumersTest
 * 
 * @author: Aldwin Su
 * @version: 2019-09-22 12:59:05
 */
public class MultiConsumersTest extends BaseTest {
	private int threadCount = 5;
	private ConsumerEvent consumerEvent = new ConsumerHandler();

	private void startThread(int index) {
		Thread thread = new Thread(() -> {
			try (JedisP2PQueueConsumer consumer = new JedisP2PQueueConsumer(jedis(), consumerEvent, Config.QUEUE_KEY)) {
				consumer.listen();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, String.format("Thread-%d", index));

		thread.start();
	}

	@Override
	public void run() {
		for (int i = 0; i < threadCount; i++) {
			startThread(i);
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(MultiConsumersTest.class);
	}
}