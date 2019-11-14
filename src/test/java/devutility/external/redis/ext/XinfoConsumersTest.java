package devutility.external.redis.ext;

import java.util.List;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.ext.model.ConsumerInfo;
import devutility.external.redis.model.StreamData;
import devutility.internal.test.TestExecutor;

/**
 * 
 * XinfoConsumersTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-01 19:46:59
 */
public class XinfoConsumersTest extends BaseTestForDuer {
	String groupName = "";

	@Override
	public void run() {
		try (DevJedis devJedis = new DevJedis(jedis())) {
			List<ConsumerInfo> list = devJedis.xInfoConsumers(CONFIG_KEY_STREAM, StreamData.GROUPNAME);

			list.forEach(i -> {
				System.out.println(String.format("name: %s, pending: %d, idle: %d", i.getName(), i.getPending(), i.getIdle()));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XinfoConsumersTest.class);
	}
}