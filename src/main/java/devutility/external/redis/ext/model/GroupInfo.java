package devutility.external.redis.ext.model;

import redis.clients.jedis.StreamEntryID;

/**
 * 
 * GroupInfo
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 20:50:41
 */
public class GroupInfo {
	private String name;
	private int consumers;
	private long pending;
	private StreamEntryID lastDeliveredId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getConsumers() {
		return consumers;
	}

	public void setConsumers(int consumers) {
		this.consumers = consumers;
	}

	public void setConsumers(long consumers) {
		this.consumers = (int) consumers;
	}

	public long getPending() {
		return pending;
	}

	public void setPending(long pending) {
		this.pending = pending;
	}

	public StreamEntryID getLastDeliveredId() {
		return lastDeliveredId;
	}

	public void setLastDeliveredId(StreamEntryID lastDeliveredId) {
		this.lastDeliveredId = lastDeliveredId;
	}
}