package devutility.external.redis.helpers.single.singleredisstringhelper;

import java.util.List;

import com.devutility.test.json.User;

import devutility.external.json.JsonUtils;
import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;

public class PagingGetListTest extends BaseTest {
	@Override
	public void run() {
		try {
			List<User> list = singleRedisStringHelper.pagingGetList("Test-Users", User.class);
			println(list.size());
			println(JsonUtils.serialize(list));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			List<User> list = singleRedisStringHelper.pagingGetList("Test-Users-onepage", User.class);
			println(list.size());
			println(JsonUtils.serialize(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(PagingGetListTest.class);
	}
}