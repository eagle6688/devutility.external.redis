package devutility.external.redis.ext.model;

/**
 * 
 * ConsumerInfo
 * 
 * @author: Aldwin Su
 * @version: 2019-11-01 15:57:54
 */
public class ConsumerInfo {
	private String name;
	private long pending;
	private long idle;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPending() {
		return pending;
	}

	public void setPending(long pending) {
		this.pending = pending;
	}

	public long getIdle() {
		return idle;
	}

	public void setIdle(long idle) {
		this.idle = idle;
	}
}