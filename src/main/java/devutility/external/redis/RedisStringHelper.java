package devutility.external.redis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import devutility.external.json.CompressUtils;
import devutility.internal.dao.models.RedisInstance;
import devutility.internal.lang.StringHelper;
import devutility.internal.util.ListHelper;
import redis.clients.jedis.Jedis;

public class RedisStringHelper extends RedisHelper {
	/**
	 * RedisBulkHelper
	 * @param redisInstance
	 */
	public RedisStringHelper(RedisInstance redisInstance) {
		super(redisInstance);
	}

	/**
	 * Get string value
	 * @param key: Redis key
	 * @return String
	 */
	public String get(String key) {
		if (StringHelper.isNullOrEmpty(key)) {
			return null;
		}

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Set string value.
	 * @param key: Redis key
	 * @param value: Value
	 * @param expireSeconds: Expire time in seconds.
	 * @return boolean
	 */
	public boolean set(String key, String value, int expire) {
		if (StringHelper.isNullOrEmpty(key) || value == null) {
			return false;
		}

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			jedis.set(key, value);

			if (expire > 0) {
				jedis.expire(key, expire);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set string value.
	 * @param key: Redis key
	 * @param value: Value
	 * @return boolean
	 */
	public boolean set(String key, String value) {
		return set(key, value, 0);
	}

	/**
	 * Get int value
	 * @param key: Redis key
	 * @return int
	 */
	public int getInt(String key) {
		String value = get(key);

		if (StringHelper.isNullOrEmpty(value)) {
			return 0;
		}

		return Integer.parseInt(value);
	}

	/**
	 * Set int value
	 * @param key: Redis key
	 * @param value: Value
	 * @return boolean
	 */
	public boolean setInt(String key, int value) {
		return set(key, String.valueOf(value));
	}

	/**
	 * Set Object
	 * @param key: Redis key
	 * @param object: Object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setObject(String key, Object object, int expire) throws IOException {
		if (StringHelper.isNullOrEmpty(key) || object == null) {
			return false;
		}

		String value = CompressUtils.compress(object);
		return set(key, value, expire);
	}

	/**
	 * Set Object
	 * @param key: Redis key
	 * @param object: Object
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setObject(String key, Object object) throws IOException {
		return setObject(key, object, 0);
	}

	/**
	 * Get object.
	 * @param key: Redis key
	 * @param clazz: Object
	 * @return {@code T}
	 * @throws IOException
	 */
	public <T> T getObject(String key, Class<T> clazz) throws IOException {
		String value = get(key);

		if (StringHelper.isNullOrEmpty(value)) {
			return null;
		}

		return CompressUtils.decompress(value, clazz);
	}

	/**
	 * Set list data
	 * @param key: Redis key
	 * @param list: Entities list
	 * @param clazz: Object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, List<T> list, Class<T> clazz, int expire) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		String[][] arrays = ListHelper.toArrays(list, clazz);
		return setObject(key, arrays, expire);
	}

	/**
	 * Set list data
	 * @param key: Redis key
	 * @param list: Entities list
	 * @param clazz: Object
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	public <T> boolean setList(String key, List<T> list, Class<T> clazz) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		String[][] arrays = ListHelper.toArrays(list, clazz);
		return setObject(key, arrays);
	}

	/**
	 * Get list from Redis
	 * @param key: Redis key
	 * @param clazz: Class object
	 * @return {@code List<T>}
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> List<T> getList(String key, Class<T> clazz) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String[][] arrays = getObject(key, String[][].class);
		return ListHelper.toEntities(arrays, clazz);
	}

	/**
	 * Get pages count
	 * @param originalKey: Original key
	 * @return int
	 */
	public int getPagesCount(String originalKey) {
		if (StringHelper.isNullOrEmpty(originalKey)) {
			return 0;
		}

		String key = pagesCountKey(originalKey);
		return getInt(key);
	}

	/***
	 * Get page array
	 * @param originalKey: Original key
	 * @param pageIndex: Page index
	 * @return String[][]
	 * @throws IOException
	 */
	public String[][] getPageArray(String originalKey, int pageIndex) throws IOException {
		String key = pagingDataKey(originalKey, pageIndex);
		return getObject(key, String[][].class);
	}

	public List<String[]> getAllPages(String originalKey) throws IOException {
		List<String[]> list = new ArrayList<>();
		int pagesCount = getPagesCount(originalKey);

		if (pagesCount == 0) {
			String[][] array = getObject(originalKey, String[][].class);

			if (array == null) {
				return new ArrayList<String[]>();
			}

			return Arrays.asList(array);
		}

		for (int index = 0; index < pagesCount; index++) {
			String[][] array = getPageArray(originalKey, index);

			if (array != null) {
				list.addAll(Arrays.asList(array));
			}
		}

		return list;
	}
}