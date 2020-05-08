package devutility.external.redis.queue.stream.consumer.retryconnection.approver;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.com.ExceptionRetryApprover;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.stream.JedisPoolStreamQueueConsumer;
import devutility.external.redis.queue.stream.JedisStreamQueueConsumerEvent;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.exceptions.JedisDataException;

public class ConnectionRetry4ApproverTest extends BaseTestForDuer {
	private JedisStreamQueueConsumerEvent consumerEvent = new ConnectionRetry4ApproverConsumerEvent();

	@Override
	public void run() {
		ExceptionRetryApprover exceptionRetryApprover = (exception) -> {
			return exception instanceof JedisDataException;
		};

		try (JedisQueueConsumer consumer = new JedisPoolStreamQueueConsumer(jedisPool(singleRedisInstance), redisQueueOption, consumerEvent, exceptionRetryApprover)) {
			consumer.listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(ConnectionRetry4ApproverTest.class);
	}
}