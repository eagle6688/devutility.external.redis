package devutility.external.redis.queue.stream.consumer.retryconnection;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.stream.JedisPoolStreamQueueConsumer;
import devutility.external.redis.queue.stream.JedisStreamQueueConsumerEvent;
import devutility.internal.test.TestExecutor;

public class ConnectionRetryTest extends BaseTestForDuer {
	private JedisStreamQueueConsumerEvent consumerEvent = new ConnectionRetryConsumerEvent();

	@Override
	public void run() {
		try (JedisQueueConsumer consumer = new JedisPoolStreamQueueConsumer(jedisPool(singleRedisInstance), redisQueueOption, consumerEvent)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(ConnectionRetryTest.class);
	}
}