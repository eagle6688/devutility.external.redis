package devutility.external.redis.queue.list;

import devutility.external.redis.BaseTest;
import devutility.external.redis.queue.Config;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.external.redis.queue.list.JedisListQueueConsumer;
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
	private JedisQueueConsumerEvent consumerEvent = new ConsumerHandler();

	private void startThread(int index) {
		Thread thread = new Thread(() -> {
			try (JedisListQueueConsumer consumer = new JedisListQueueConsumer(jedis(), consumerEvent, Config.QUEUE_KEY)) {
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