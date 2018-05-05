package devutility.external.redis.redisstringhelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.devutility.test.json.User;

import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;

public class SetListTest extends BaseTest {
	@Override
	public void run() {
		List<User> list = User.list(109);

		try {
			redisStringHelper.setList("Test-Users", 10, list, User.class);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		}

		list = User.list(10);

		try {
			redisStringHelper.setList("Test-Users-onepage", 10, list, User.class);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(SetListTest.class);
	}
}