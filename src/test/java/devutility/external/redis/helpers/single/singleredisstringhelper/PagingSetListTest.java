package devutility.external.redis.helpers.single.singleredisstringhelper;

import java.util.List;

import com.devutility.test.json.User;

import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;

public class PagingSetListTest extends BaseTest {
	@Override
	public void run() {
		List<User> list = User.list(109);

		try {
			singleRedisStringHelper.pagingSetList("Test-Users", 10, list, 0, null, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		list = User.list(10);

		try {
			singleRedisStringHelper.pagingSetList("Test-Users-onepage", 10, list, 0, null, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(PagingSetListTest.class);
	}
}