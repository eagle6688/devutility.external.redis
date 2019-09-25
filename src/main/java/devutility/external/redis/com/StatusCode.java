package devutility.external.redis.com;

/**
 * 
 * StatusCode
 * 
 * @author: Aldwin Su
 * @version: 2019-09-20 16:33:58
 */
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
	 * Is Ok or not.
	 * @param status Jedis status.
	 * @return boolean
	 */
	public static boolean isOk(String status) {
		return StatusCode.OK.getValue().equals(status) || StatusCode.MULTIOK.getValue().equals(status);
	}
}