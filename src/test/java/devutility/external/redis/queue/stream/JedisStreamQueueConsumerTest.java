package devutility.external.redis.queue.stream;

import com.fasterxml.jackson.core.JsonProcessingException;

import devutility.external.json.JsonUtils;
import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;
import devutility.external.redis.queue.list.ListConsumerHandler;
import devutility.internal.test.TestExecutor;

/**
 * 
 * JedisStreamQueueConsumerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-14 16:01:55
 */
public class JedisStreamQueueConsumerTest extends BaseTestForDuer {
	private JedisQueueConsumerEvent consumerEvent = new ListConsumerHandler();

	@Override
	public void run() {
		try {
			System.out.println(JsonUtils.serialize(redisQueueOption));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		try (JedisQueueConsumer consumer = new JedisStreamQueueConsumer(jedis(), redisQueueOption, consumerEvent)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(JedisStreamQueueConsumerTest.class);
	}
}