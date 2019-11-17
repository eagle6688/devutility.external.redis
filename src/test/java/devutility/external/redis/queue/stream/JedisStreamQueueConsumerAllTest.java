package devutility.external.redis.queue.stream;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.StreamEntryID;

/**
 * 
 * JedisStreamQueueConsumerAllTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-16 18:05:25
 */
public class JedisStreamQueueConsumerAllTest extends BaseTestForDuer {
	private StreamConsumerHandlerForAll consumerHandler = new StreamConsumerHandlerForAll();

	@Override
	public void run() {
		try (JedisQueueConsumer consumer = new JedisStreamQueueConsumer(jedis(), redisQueueOption, consumerHandler)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(JedisStreamQueueConsumerAllTest.class);
	}
}

class StreamConsumerHandlerForAll extends JedisStreamQueueConsumerEvent {
	@Override
	public boolean onMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		System.out.println(String.format("%s Triggering onMessage event, key:\"%s\", id: \"%s\", value: %s", Thread.currentThread().getName(), key, streamEntryId, values[1].toString()));
		return true;
	}

	@Override
	public boolean onPendingMessage(String key, Object... values) {
		StreamEntryID streamEntryId = (StreamEntryID) values[0];
		System.out.println(String.format("%s Triggering onPendingMessage event, key:\"%s\", id: \"%s\", value: %s", Thread.currentThread().getName(), key, streamEntryId, values[1].toString()));
		return true;
	}
}