package devutility.external.redis.redisstringhelper;

import java.io.IOException;

import com.devutility.test.array.ArrayData;

import devutility.external.redis.BaseTest;
import devutility.internal.test.TestExecutor;

public class SetArraysTest extends BaseTest {
	@Override
	public void run() {
		String[][] arrays = ArrayData.getArrays(109);

		try {
			redisStringHelper.setArrays("Test", 10, arrays);
		} catch (IOException e) {
			e.printStackTrace();
		}

		println("OK");
	}

	public static void main(String[] args) {
		TestExecutor.run(SetArraysTest.class);
	}
}