package devutility.external.redis.ext;

import java.util.List;

import devutility.external.redis.BaseTestForDuer;
import devutility.external.redis.ext.model.GroupInfo;
import devutility.internal.test.TestExecutor;

/**
 * 
 * ExecXinfoGroupsTest
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 21:28:56
 */
public class XinfoGroupsTest extends BaseTestForDuer {
	@Override
	public void run() {
		try (DevJedis devJedis = new DevJedis(jedis())) {
			List<GroupInfo> list = devJedis.xInfoGroups(CONFIG_KEY_STREAM);

			list.forEach(i -> {
				System.out.println(String.format("name: %s, consumers: %d, pending: %d, LastDeliveredId: %s", i.getName(), i.getConsumers(), i.getPending(), i.getLastDeliveredId()));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(XinfoGroupsTest.class);
	}
}