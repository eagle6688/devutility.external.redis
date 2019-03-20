package devutility.external.redis.com;

public enum StatusCode {
	/**
	 * Successful operation.
	 */
	OK("OK"),

	/**
	 * Successful operations.
	 */
	MULTIOK("+OK");
	private String value;

	StatusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}