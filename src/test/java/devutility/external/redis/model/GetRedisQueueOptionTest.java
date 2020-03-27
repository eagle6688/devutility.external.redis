package devutility.external.redis.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import devutility.external.redis.BaseTestForDuer;
import devutility.internal.test.TestExecutor;
import devutility.internal.util.PropertiesUtils;

public class GetRedisQueueOptionTest extends BaseTestForDuer {
	@Override
	public void run() {
		try {
			RedisQueueOption redisQueueOption = PropertiesUtils.toObjectFromResource(CONFIG_FILE, "queue.option", RedisQueueOption.class);
			System.out.println(redisQueueOption.getKey());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(GetRedisQueueOptionTest.class);
	}
}