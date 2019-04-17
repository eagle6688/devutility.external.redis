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

	/**
	 * Is set Ok or not.
	 * @param status: Jedis status.
	 * @return boolean
	 */
	public static boolean isSetOk(String status) {
		return StatusCode.OK.getValue().equals(status) || StatusCode.MULTIOK.getValue().equals(status);
	}
}