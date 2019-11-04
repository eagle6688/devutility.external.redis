package devutility.external.redis.ext;

import java.util.List;

import redis.clients.jedis.Client;

/**
 * 
 * DevJedisConnection
 * 
 * @author: Aldwin Su
 * @version: 2019-11-04 20:31:00
 */
public class DevJedisConnection {
	/**
	 * Jedis Client object.
	 */
	protected Client client;

	/**
	 * Constructor
	 * @param client
	 */
	public DevJedisConnection(Client client) {
		this.client = client;
	}

	public List<Object> getObjectMultiBulkReply() {
		return client.getObjectMultiBulkReply();
	}

	public void setTimeoutInfinite() {
		client.setTimeoutInfinite();
	}

	public void rollbackTimeout() {
		client.rollbackTimeout();
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}