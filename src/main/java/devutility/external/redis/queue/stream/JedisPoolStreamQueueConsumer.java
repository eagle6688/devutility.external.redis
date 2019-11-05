package devutility.external.redis.queue.stream;

import java.io.IOException;

import devutility.external.redis.com.RedisQueueOption;
import devutility.external.redis.queue.JedisQueueConsumer;
import devutility.external.redis.queue.JedisQueueConsumerEvent;

/**
 * 
 * JedisPoolStreamQueueConsumer
 * 
 * @author: Aldwin Su
 * @version: 2019-10-10 20:12:17
 */
public class JedisPoolStreamQueueConsumer extends JedisQueueConsumer {
	public JedisPoolStreamQueueConsumer(RedisQueueOption redisQueueOption, JedisQueueConsumerEvent consumerEvent) {
		super(redisQueueOption, consumerEvent);
	}

	@Override
	public void listen() throws Exception {

	}

	@Override
	public void close() throws IOException {

	}
}