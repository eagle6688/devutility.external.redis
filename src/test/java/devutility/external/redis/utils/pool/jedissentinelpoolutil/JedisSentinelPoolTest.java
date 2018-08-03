package devutility.external.redis.utils.pool.jedissentinelpoolutil;

import devutility.external.redis.BaseTest;
import devutility.external.redis.utils.pool.JedisSentinelPoolUtil;
import devutility.internal.test.TestExecutor;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinelPoolTest extends BaseTest {
	@Override
	public void run() {
		JedisSentinelPool jedisSentinelPool = JedisSentinelPoolUtil.jedisSentinelPool(sentinel_RedisInstance);
		HostAndPort master = jedisSentinelPool.getCurrentHostMaster();
		println(master.toString());
	}

	public static void main(String[] args) {
		TestExecutor.run(JedisSentinelPoolTest.class);
	}
}