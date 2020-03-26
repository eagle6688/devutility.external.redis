package devutility.external.redis.model;

import java.lang.reflect.InvocationTargetException;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.com.RedisQueueOption;
import devutility.internal.test.TestExecutor;
import devutility.internal.util.PropertiesUtils;

public class GetRedisQueueOptionTest extends BaseTestForDuer {
	@Override
	public void run() {
		try {
			RedisQueueOption redisQueueOption = PropertiesUtils.toModel(CONFIG_FILE, "queue.option", RedisQueueOption.class);
			System.out.println(redisQueueOption.getKey());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(GetRedisQueueOptionTest.class);
	}
}