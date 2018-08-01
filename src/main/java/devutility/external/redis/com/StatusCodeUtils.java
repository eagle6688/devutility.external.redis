package devutility.external.redis.com;

public class StatusCodeUtils {
	/**
	 * Is set Ok or not.
	 * @param status: Jedis status.
	 * @return boolean
	 */
	public static boolean isSetOk(String status) {
		return StatusCode.OK.getValue().equals(status) || StatusCode.MULTIOK.getValue().equals(status);
	}
}