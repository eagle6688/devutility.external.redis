package devutility.external.redis.helper.single.singleredisstringhelper;

import java.util.List;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.model.User;
import devutility.internal.test.TestExecutor;

public class PagingSetListTest extends BaseTestForDuer {
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