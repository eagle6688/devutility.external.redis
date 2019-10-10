package devutility.external.redis.queue.list;

import java.util.Date;

import devutility.external.redis.BaseTest;
import devutility.external.redis.queue.Config;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.external.redis.queue.list.JedisPoolP2PQueueConsumer;
import devutility.internal.test.TestExecutor;

public class PoolConsumerTest extends BaseTest {
	private JedisQueueConsumerEvent consumerEvent = new ConsumerHandler();

	@Override
	public void run() {
		System.out.println(new Date());

		try (JedisPoolP2PQueueConsumer consumer = new JedisPoolP2PQueueConsumer(jedisPool(), consumerEvent, Config.QUEUE_KEY, singleRedisInstance.getDatabase())) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Consumer listening completely!");
	}

	public static void main(String[] args) {
		TestExecutor.run(PoolConsumerTest.class);
	}
}