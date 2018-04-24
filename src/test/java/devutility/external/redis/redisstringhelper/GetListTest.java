package devutility.external.redis.redisstringhelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.devutility.test.json.User;

import devutility.external.json.JsonUtils;
import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;

public class GetListTest extends BaseTest {
	@Override
	public void run() {
		try {
			List<User> list = redisStringHelper.getList("Test-Users", 10, User.class);
			println(JsonUtils.serialize(list));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(GetListTest.class);
	}
}