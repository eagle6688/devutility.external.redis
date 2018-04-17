package devutility.external.redis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import devutility.external.json.CompressUtils;
import devutility.internal.dao.models.RedisInstance;
import devutility.internal.data.PaginationUtils;
import devutility.internal.lang.StringHelper;
import devutility.internal.util.ArraysUtils;
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
	 * Set list data
	 * @param key: Redis key
	 * @param pageSize: Page size
	 * @param list: Entities list
	 * @param clazz: Object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, int pageSize, List<T> list, Class<T> clazz, int expire) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		if (StringHelper.isNullOrEmpty(key) || list == null || clazz == null) {
			return false;
		}

		int pagesCount = PaginationUtils.calculatePagesCount(list.size(), pageSize);

		if (pagesCount == 0) {
			return setList(key, list, clazz, expire);
		}

		boolean result = true;

		for (int index = 0; index < pagesCount; index++) {
			List<T> listPage = ListHelper.paging(list, index, pageSize);
			result = setList(key, listPage, clazz, expire);

			if (!result) {
				return result;
			}
		}

		return setPagesCount(key, pagesCount);
	}

	/**
	 * Set list data
	 * @param key: Redis key
	 * @param pageSize: Page size
	 * @param list: Entities list
	 * @param clazz: Object
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, int pageSize, List<T> list, Class<T> clazz) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return setList(key, pageSize, list, clazz, 0);
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
	 * Get list from Redis
	 * @param key: Redis key
	 * @param pageSize: Page size
	 * @param clazz: Class object
	 * @return {@code List<T>}
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> List<T> getList(String key, int pageSize, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		int pagesCount = getPagesCount(key);

		if (pagesCount == 0) {
			List<T> list = getList(key, clazz);

			if (list != null) {
				return list;
			}

			return new ArrayList<>();
		}

		List<T> list = new LinkedList<>();

		for (int index = 0; index < pagesCount; index++) {
			List<T> listPage = getList(key, pageSize, clazz);

			if (listPage != null) {
				list.addAll(listPage);
			}
		}

		return new ArrayList<>(list);
	}

	/**
	 * Set pages count
	 * @param originalKey: Original key
	 * @param count: Pages count
	 * @return boolean
	 */
	private boolean setPagesCount(String originalKey, int count) {
		if (StringHelper.isNullOrEmpty(originalKey) || count < 1) {
			return false;
		}

		String key = pagesCountKey(originalKey);
		return setInt(key, count);
	}

	/**
	 * Get pages count
	 * @param originalKey: Original key
	 * @return int
	 */
	private int getPagesCount(String originalKey) {
		if (StringHelper.isNullOrEmpty(originalKey)) {
			return 0;
		}

		String key = pagesCountKey(originalKey);
		return getInt(key);
	}

	/**
	 * Set array page
	 * @param originalKey: Original key
	 * @param pageIndex: Page index
	 * @param array: Array data
	 * @return boolean
	 * @throws IOException
	 */
	private boolean setArrayPage(String originalKey, int pageIndex, String[][] array) throws IOException {
		String key = pagingDataKey(originalKey, pageIndex);
		return setObject(key, array);
	}

	/***
	 * Get array page
	 * @param originalKey: Original key
	 * @param pageIndex: Page index
	 * @return String[][]
	 * @throws IOException
	 */
	private String[][] getArrayPage(String originalKey, int pageIndex) throws IOException {
		String key = pagingDataKey(originalKey, pageIndex);
		return getObject(key, String[][].class);
	}

	/**
	 * Set arrays data
	 * @param originalKey: Original key
	 * @param pageSize: Page size
	 * @param array: Array data
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setArrays(String originalKey, int pageSize, String[][] array) throws IOException {
		if (StringHelper.isNullOrEmpty(originalKey) || pageSize == 0 || array == null) {
			return false;
		}

		int pagesCount = PaginationUtils.calculatePagesCount(array.length, pageSize);

		if (pagesCount == 1) {
			return setObject(originalKey, array);
		}

		boolean result = true;

		for (int index = 0; index < pagesCount; index++) {
			String[][] arrayPage = ArraysUtils.pageArray(array, index, pageSize);
			result = setArrayPage(originalKey, index, arrayPage);

			if (!result) {
				return result;
			}
		}

		return setPagesCount(originalKey, pagesCount);
	}

	/**
	 * Get arrays
	 * @param originalKey: Original key
	 * @param pageSize: Page size
	 * @return String[][]
	 * @throws IOException
	 */
	public String[][] getArrays(String originalKey, int pageSize) throws IOException {
		int pagesCount = getPagesCount(originalKey);

		if (pagesCount == 0) {
			String[][] arrays = getObject(originalKey, String[][].class);

			if (arrays != null) {
				return arrays;
			}

			return new String[0][];
		}

		List<String[]> list = new LinkedList<>();

		for (int index = 0; index < pagesCount; index++) {
			String[][] array = getArrayPage(originalKey, index);

			if (array != null) {
				list.addAll(Arrays.asList(array));
			}
		}

		return list.toArray(new String[0][]);
	}
}