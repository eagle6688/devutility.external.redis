package devutility.external.redis.redisstringhelper;

import java.io.IOException;
import java.util.Arrays;

import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;

public class GetArraysTest extends BaseTest {
	@Override
	public void run() {
		try {
			String[][] arrays = redisStringHelper.getArrays("Test", 10);

			for (String[] array : arrays) {
				println(Arrays.toString(array));
			}

			println(arrays.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(GetArraysTest.class);
	}
}