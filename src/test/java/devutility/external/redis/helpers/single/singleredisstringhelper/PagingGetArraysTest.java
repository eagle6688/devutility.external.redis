package devutility.external.redis.helpers.single.singleredisstringhelper;

import java.io.IOException;
import java.util.Arrays;

import devutility.external.redis.BaseTestForDuer;
import devutility.internal.test.TestExecutor;

public class PagingGetArraysTest extends BaseTestForDuer {
	@Override
	public void run() {
		try {
			String[][] arrays = singleRedisStringHelper.pagingGetArrays("Test-setArrays");
			println(arrays.length);

			for (String[] array : arrays) {
				println(Arrays.toString(array));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			String[][] arrays = singleRedisStringHelper.pagingGetArrays("Test-setArrays-onepage");
			println(arrays.length);

			for (String[] array : arrays) {
				println(Arrays.toString(array));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(PagingGetArraysTest.class);
	}
}