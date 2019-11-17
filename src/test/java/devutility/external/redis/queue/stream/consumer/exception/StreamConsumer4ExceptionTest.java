package devutility.external.redis.queue.stream.consumer.exception;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.stream.JedisPoolStreamQueueConsumer;
import devutility.external.redis.queue.stream.JedisStreamQueueConsumerEvent;
import devutility.internal.test.TestExecutor;

/**
 * 
 * JedisStreamQueueConsumer4FailedTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-17 23:09:55
 */
public class StreamConsumer4ExceptionTest extends BaseTestForDuer {
	private JedisStreamQueueConsumerEvent consumerEvent = new StreamConsumerEvent4Exception();

	@Override
	public void run() {
		try (JedisQueueConsumer consumer = new JedisPoolStreamQueueConsumer(jedisPool(), redisQueueOption, consumerEvent)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(StreamConsumer4ExceptionTest.class);
	}
}