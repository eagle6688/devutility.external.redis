package devutility.external.redis;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
	 * Get string value.
	 * @param key: Redis key
	 * @return String
	 */
	public String get(String key) {
		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			return get(key, jedis);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Set string value, used for loop.
	 * @param key: Redis key
	 * @param value: Value
	 * @param jedis: Jedis object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 */
	public boolean set(String key, String value, Jedis jedis, int expire) {
		if (StringHelper.isNullOrEmpty(key) || value == null || jedis == null) {
			return false;
		}

		jedis.set(key, value);

		if (expire > 0) {
			jedis.expire(key, expire);
		}

		return true;
	}

	/**
	 * Set string value.
	 * @param key: Redis key
	 * @param value: Value
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 */
	public boolean set(String key, String value, int expire) {
		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			return set(key, value, jedis, expire);
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
	 * Set Object, used for loop.
	 * @param key: Redis key
	 * @param object: Object
	 * @param jedis: Jedis object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setObject(String key, Object object, Jedis jedis, int expire) throws IOException {
		if (object == null) {
			return false;
		}

		String value = CompressUtils.compress(object);
		return set(key, value, jedis, expire);
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
		if (object == null) {
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
	 * Set list data, used for loop.
	 * @param key: Redis key
	 * @param list: Entities list
	 * @param clazz: Object
	 * @param excludeAnnotations: Annotations want to be excluded.
	 * @param jedis: Jedis object
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, List<T> list, Class<T> clazz, List<Annotation> excludeAnnotations, Jedis jedis, int expire)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		String[][] arrays = ListHelper.toArrays(list, clazz, excludeAnnotations);
		return setObject(key, arrays, jedis, expire);
	}

	/**
	 * Set list data
	 * @param key: Redis key
	 * @param list: Entities list
	 * @param clazz: Object
	 * @param excludeAnnotations: Annotations want to be excluded.
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, List<T> list, Class<T> clazz, List<Annotation> excludeAnnotations, int expire) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		String[][] arrays = ListHelper.toArrays(list, clazz, excludeAnnotations);
		return setObject(key, arrays, expire);
	}

	/**
	 * Set list data
	 * @param key: Redis key
	 * @param list: Entities list
	 * @param clazz: Object
	 * @param excludeAnnotations: Annotations want to be excluded.
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, List<T> list, Class<T> clazz, List<Annotation> excludeAnnotations) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return setList(key, list, clazz, excludeAnnotations, 0);
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
		return setList(key, list, clazz, null, expire);
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
		return setList(key, list, clazz, 0);
	}

	/**
	 * Set list data
	 * @param key: Redis key
	 * @param pageSize: Page size
	 * @param list: Entities list
	 * @param clazz: Object
	 * @param excludeAnnotations: Annotations want to be excluded.
	 * @param expire: Expire time in seconds.
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public <T> boolean setList(String key, int pageSize, List<T> list, Class<T> clazz, List<Annotation> excludeAnnotations, int expire)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		if (StringHelper.isNullOrEmpty(key) || pageSize < 1 || list == null || clazz == null) {
			return false;
		}

		int pagesCount = PaginationUtils.calculatePagesCount(list.size(), pageSize);

		if (pagesCount == 1) {
			return setList(key, list, clazz, excludeAnnotations, expire);
		}

		boolean result = true;

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			for (int index = 0; index < pagesCount; index++) {
				String pageKey = pagingDataKey(key, index);
				List<T> listPage = ListHelper.paging(list, index + 1, pageSize);
				result = setList(pageKey, listPage, clazz, excludeAnnotations, jedis, expire);

				if (!result) {
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return setPagesCount(key, pagesCount);
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
		return setList(key, pageSize, list, clazz, null, expire);
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

		if (pagesCount == 1) {
			List<T> list = getList(key, clazz);

			if (list != null) {
				return list;
			}

			return new ArrayList<>();
		}

		List<T> list = new LinkedList<>();

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			for (int index = 0; index < pagesCount; index++) {
				String pageKey = pagingDataKey(key, index);
				List<T> listPage = getList(pageKey, pageSize, clazz);

				if (listPage != null) {
					list.addAll(listPage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

		return new ArrayList<>(list);
	}

	/**
	 * Set pages count
	 * @param key: Redis key
	 * @param count: Pages count
	 * @return boolean
	 */
	private boolean setPagesCount(String key, int count) {
		if (StringHelper.isNullOrEmpty(key) || count < 1) {
			return false;
		}

		String realKey = pagesCountKey(key);
		return setInt(realKey, count);
	}

	/**
	 * Get pages count
	 * @param key: Redis key
	 * @return int
	 */
	private int getPagesCount(String key) {
		if (StringHelper.isNullOrEmpty(key)) {
			return 0;
		}

		String realKey = pagesCountKey(key);
		return getInt(realKey);
	}

	/**
	 * Set array page
	 * @param key: Redis key
	 * @param pageIndex: Page index
	 * @param array: Array data
	 * @param jedis: Jedis object
	 * @return boolean
	 * @throws IOException
	 */
	private boolean setArrayPage(String key, int pageIndex, String[][] array, Jedis jedis) throws IOException {
		String realKey = pagingDataKey(key, pageIndex);
		return setObject(realKey, array, jedis, 0);
	}

	/***
	 * Get page array
	 * @param key: Redis key
	 * @param pageIndex: Page index
	 * @param jedis: Jedis object
	 * @return String[][]
	 * @throws IOException
	 */
	private String[][] getPageArray(String key, int pageIndex, Jedis jedis) throws IOException {
		String realKey = pagingDataKey(key, pageIndex);
		return getObject(realKey, String[][].class, jedis);
	}

	/**
	 * Set arrays data
	 * @param key: Redis key
	 * @param pageSize: Page size
	 * @param array: Array data
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setArrays(String key, int pageSize, String[][] array) throws IOException {
		if (pageSize < 1 || array == null) {
			return false;
		}

		int pagesCount = PaginationUtils.calculatePagesCount(array.length, pageSize);

		if (pagesCount == 1) {
			return setObject(key, array);
		}

		boolean result = true;

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			for (int index = 0; index < pagesCount; index++) {
				String[][] pageArray = ArraysUtils.pageArray(array, index + 1, pageSize);
				result = setArrayPage(key, index, pageArray, jedis);

				if (!result) {
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return setPagesCount(key, pagesCount);
	}

	/**
	 * Get arrays
	 * @param key: Redis key
	 * @param pageSize: Page size
	 * @return String[][]
	 * @throws IOException
	 */
	public String[][] getArrays(String key, int pageSize) throws IOException {
		int pagesCount = getPagesCount(key);

		if (pagesCount == 1) {
			String[][] arrays = getObject(key, String[][].class);

			if (arrays != null) {
				return arrays;
			}

			return new String[0][];
		}

		List<String[]> list = new LinkedList<>();

		try (Jedis jedis = RedisUtils.jedis(redisInstance)) {
			for (int index = 0; index < pagesCount; index++) {
				String[][] array = getPageArray(key, index, jedis);

				if (array != null) {
					list.addAll(Arrays.asList(array));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new String[0][];
		}

		return list.toArray(new String[0][]);
	}
}