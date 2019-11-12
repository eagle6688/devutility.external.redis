package devutility.external.redis.queue.stream;

import java.util.List;

import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamPendingEntry;

/**
 * 
 * XpendingTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-12 23:25:30
 */
public class XpendingTest extends BaseTestForStream {
	@Override
	public void run() {
		try (Jedis jedis = jedis()) {
			List<StreamPendingEntry> list = jedis.xpending(CONFIG_KEY_STREAM, StreamData.GROUPNAME, null, null, 10, StreamData.CONSUMERNAME);
			System.out.println(String.format("list size: %d", list.size()));

			list.forEach(i -> {
				System.out.println(String.format("Consumer: %s, ID: %s, DeliveredTimes: %d, IdleTime: %d", i.getConsumerName(), i.getID().toString(), i.getDeliveredTimes(), i.getIdleTime()));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XpendingTest.class);
	}
}