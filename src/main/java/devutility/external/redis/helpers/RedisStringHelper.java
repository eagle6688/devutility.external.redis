package devutility.external.redis.helpers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import devutility.external.json.CompressUtils;
import devutility.external.redis.com.StatusCodeUtils;
import devutility.external.redis.models.RedisInstance;
import devutility.internal.lang.StringHelper;
import devutility.internal.lang.models.EntityField;
import devutility.internal.util.ListUtils;
import redis.clients.jedis.Jedis;

public class RedisStringHelper extends RedisHelper {
	/**
	 * Constructor
	 * @param redisInstance
	 */
	public RedisStringHelper(RedisInstance redisInstance) {
		super(redisInstance);
	}

	/**
	 * Get string value, used for loop.
	 * @param key: Redis key
	 * @param jedis: Jedis object
	 * @return String
	 */
	public String get(String key, Jedis jedis) {
		if (StringHelper.isNullOrEmpty(key) || jedis == null) {
			return null;
		}

		return jedis.get(key);
	}

	/**
	 * Set string value.
	 * @param key: Redis key.
	 * @param value: Value
	 * @param jedis: Jedis object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 */
	public boolean set(String key, String value, Jedis jedis, int expire) {
		if (StringHelper.isNullOrEmpty(key) || value == null || jedis == null) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		String status = jedis.set(key, value);

		if (!StatusCodeUtils.isSetOk(status)) {
			return false;
		}

		if (expire > 0 && jedis.expire(key, expire) != 1) {
			return false;
		}

		return true;
	}

	/**
	 * Set Object value.
	 * @param key: Redis key.
	 * @param value: Object value.
	 * @param jedis: Jedis object.
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setObject(String key, Object value, Jedis jedis, int expire) throws IOException {
		if (value == null) {
			return false;
		}

		String compressedValue = CompressUtils.compress(value);
		return set(key, compressedValue, jedis, expire);
	}

	/**
	 * Get object.
	 * @param key: Redis key
	 * @param clazz: Object
	 * @param jedis: Jedis object
	 * @return {@code T}
	 * @throws IOException
	 */
	public <T> T getObject(String key, Class<T> clazz, Jedis jedis) throws IOException {
		String value = get(key, jedis);

		if (StringHelper.isNullOrEmpty(value)) {
			return null;
		}

		return CompressUtils.decompress(value, clazz);
	}

	/**
	 * Set list data, used for loop.
	 * @param key: Redis key
	 * @param list: Entities list
	 * @param clazz: Object
	 * @param entityFields: EntityField list for {@code T}.
	 * @param jedis: Jedis object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, List<T> list, Class<T> clazz, List<EntityField> entityFields, Jedis jedis, int expire)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		String[][] arrays = ListUtils.toArrays(list, entityFields);
		return setObject(key, arrays, jedis, expire);
	}

	/**
	 * Get list from Redis, used for loop.
	 * @param key: Redis key
	 * @param clazz: Class object
	 * @param jedis: Jedis object
	 * @return {@code List<T>}
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> List<T> getList(String key, Class<T> clazz, Jedis jedis) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String[][] arrays = getObject(key, String[][].class, jedis);
		return ListUtils.toEntities(arrays, clazz);
	}

	public <T> boolean pagingSetList(String key, int pageSize, List<T> list, List<String> excludeFields, int expire, Jedis jedis, Class<T> clazz) {
		if (StringHelper.isNullOrEmpty(key) || pageSize < 1 || list == null || clazz == null) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		return true;
	}

	/**
	 * Set array page.
	 * @param key: Redis key
	 * @param pageIndex: Page index
	 * @param array: Array data
	 * @param jedis: Jedis object
	 * @return boolean
	 * @throws IOException
	 */
	protected boolean setArrayPage(String key, int pageIndex, String[][] array, Jedis jedis) throws IOException {
		String realKey = pagingDataKey(key, pageIndex);
		return setObject(realKey, array, jedis, 0);
	}

	/***
	 * Get page array.
	 * @param key: Redis key
	 * @param pageIndex: Page index
	 * @param jedis: Jedis object
	 * @return String[][]
	 * @throws IOException
	 */
	protected String[][] getPageArray(String key, int pageIndex, Jedis jedis) throws IOException {
		String realKey = pagingDataKey(key, pageIndex);
		return getObject(realKey, String[][].class, jedis);
	}
}