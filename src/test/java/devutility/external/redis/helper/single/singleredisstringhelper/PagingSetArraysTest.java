package devutility.external.redis.helper.single.singleredisstringhelper;

import java.io.IOException;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.model.User;
import devutility.internal.test.TestExecutor;

public class PagingSetArraysTest extends BaseTestForDuer {
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