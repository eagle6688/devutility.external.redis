package devutility.external.redis.helpers.single.singleredisstringhelper;

import java.io.IOException;

import devutility.external.redis.BaseTest;
import devutility.external.redis.model.User;
import devutility.internal.test.TestExecutor;

public class PagingSetArraysTest extends BaseTest {
	@Override
	public void run() {
		String[][] arrays = User.getArrays(109);

		try {
			singleRedisStringHelper.pagingSetArrays("Test-setArrays", arrays, 10, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		arrays = User.getArrays(10);

		try {
			singleRedisStringHelper.pagingSetArrays("Test-setArrays-onepage", arrays, 10, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		println("Finished!");
	}

	public static void main(String[] args) {
		TestExecutor.run(PagingSetArraysTest.class);
	}
}