package devutility.external.redis.com;

/**
 * 
 * RedisType
 * 
 * @author: Aldwin Su
 * @version: 2019-11-01 14:05:53
 */
public enum RedisType {
	/**
	 * If the key does not exist
	 */
	NONE("none"),

	/**
	 * String type
	 */
	STRING("string"),

	/**
	 * List type
	 */
	LIST("list"),

	/**
	 * Set type
	 */
	SET("set"),

	/**
	 * Sorted Set type
	 */
	ZSET("zset"),

	/**
	 * Hash type
	 */
	HASH("hash"),

	/**
	 * Stream type
	 */
	STREAM("stream");

	private String value;

	private RedisType(String value) {
		this.value = value;
	}

	public static RedisType parse(String value) {
		RedisType[] array = RedisType.values();

		for (int i = 0; i < array.length; i++) {
			if (array[i].value.equals(value)) {
				return array[i];
			}
		}

		return RedisType.NONE;
	}

	public String getValue() {
		return value;
	}
}