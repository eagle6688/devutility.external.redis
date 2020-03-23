package devutility.external.redis.model;

public class SentinelRedisInstance extends ClusterRedisInstance {
	/**
	 * Name of master Redis server.
	 */
	private String masterName;

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
}