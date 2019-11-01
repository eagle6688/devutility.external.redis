package devutility.external.redis.ext;

import java.util.List;

import devutility.external.redis.BaseTest;
import devutility.external.redis.ext.model.GroupInfo;
import devutility.internal.test.TestExecutor;

/**
 * 
 * SafeGroupInfosTest
 * 
 * @author: Aldwin Su
 * @version: 2019-11-01 14:53:58
 */
public class SafeGroupInfosTest extends BaseTest {
	@Override
	public void run() {
		try (DevJedis devJedis = new DevJedis(jedis())) {
			List<GroupInfo> list = devJedis.safeGroupInfos(CONFIG_KEY_STREAM);

			list.forEach(i -> {
				System.out.println(String.format("name: %s, consumers: %d, pending: %d, LastDeliveredId: %s", i.getName(), i.getConsumers(), i.getPending(), i.getLastDeliveredId()));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(SafeGroupInfosTest.class);
	}
}