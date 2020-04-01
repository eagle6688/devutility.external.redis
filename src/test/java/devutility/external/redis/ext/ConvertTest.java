package devutility.external.redis.ext;

import devutility.external.redis.BaseTestForDuer;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.Jedis;

public class ConvertTest extends BaseTestForDuer {
	@Override
	public void run() {
		ExtJedis extJedis = (ExtJedis) jedis();
		System.out.println(extJedis.getName());
	}

	public static void main(String[] args) {
		TestExecutor.run(ConvertTest.class);
	}
}

class ExtJedis extends Jedis {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}