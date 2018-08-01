package devutility.external.redis.models;

public class ClusterRedisInstance extends RedisInstance {
	/**
	 * Redis cluster servers.
	 */
	private String nodes;

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
}