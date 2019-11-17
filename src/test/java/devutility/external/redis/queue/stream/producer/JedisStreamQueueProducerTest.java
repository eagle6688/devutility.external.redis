package devutility.external.redis.queue.stream.producer;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.queue.JedisQueueProducer;
import devutility.external.redis.queue.stream.JedisStreamQueueProducer;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

/**
 * 
 * JedisStreamQueueProducerTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-14 17:13:16
 */
public class JedisStreamQueueProducerTest extends BaseTestForDuer {
	private JedisQueueProducer producer = new JedisStreamQueueProducer(redisQueueOption);

	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			producer.enqueue(jedis, "Hello World!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(JedisStreamQueueProducerTest.class);
	}
}