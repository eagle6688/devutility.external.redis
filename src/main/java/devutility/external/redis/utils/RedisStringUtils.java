package devutility.external.redis.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import devutility.external.json.CompressUtils;
import devutility.external.redis.com.StatusCode;
import devutility.internal.data.SearchUtils;
import devutility.internal.lang.ArraysUtils;
import devutility.internal.lang.ClassUtils;
import devutility.internal.lang.StringUtils;
import devutility.internal.lang.models.EntityField;
import devutility.internal.util.CollectionUtils;
import devutility.internal.util.ListUtils;
import redis.clients.jedis.Jedis;

public class RedisStringUtils extends BaseRedisUtils {
	/**
	 * Set string value.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param value String value.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 */
	public static boolean set(Jedis jedis, String key, String value, int expire) {
		if (jedis == null || StringUtils.isNullOrEmpty(key) || value == null) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		String status = jedis.set(key, value);

		if (!StatusCode.isOk(status)) {
			return false;
		}

		if (expire > 0) {
			return jedis.expire(key, expire) != 1;
		}

		return true;
	}

	/**
	 * Get string value.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @return String
	 */
	public static String get(Jedis jedis, String key) {
		if (jedis == null || StringUtils.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		return jedis.get(key);
	}

	/**
	 * Set int value.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param value Int value.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 */
	public static boolean setInt(Jedis jedis, String key, int value, int expire) {
		return set(jedis, key, String.valueOf(value), expire);
	}

	/**
	 * Get int value.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @return int
	 */
	public static int getInt(Jedis jedis, String key) {
		String value = get(jedis, key);

		if (StringUtils.isNullOrEmpty(value)) {
			return 0;
		}

		return Integer.parseInt(value);
	}

	/**
	 * Set Object value. Convert object value to string format and compress it.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param value Object value.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean setObject(Jedis jedis, String key, Object value, int expire) throws IOException {
		if (value == null) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		String compressedValue = CompressUtils.compress(value);
		return set(jedis, key, compressedValue, expire);
	}

	/**
	 * Get Object value. Convert object value to string format and compress it.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param clazz Class object.
	 * @return {@code T}
	 * @throws IOException
	 */
	public static <T> T getObject(Jedis jedis, String key, Class<T> clazz) throws IOException {
		String value = get(jedis, key);

		if (StringUtils.isNullOrEmpty(value)) {
			return null;
		}

		return CompressUtils.decompress(value, clazz);
	}

	/**
	 * Set list data. Treat list as a single object, convert it to string format and compress it.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param list List data.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @param entityFields EntityField list for {@code T}.
	 * @return boolean
	 * @throws Exception
	 */
	public static <T> boolean setList(Jedis jedis, String key, List<T> list, int expire, List<EntityField> entityFields) throws Exception {
		String[][] arrays = ListUtils.toArrays(list, entityFields);
		return setObject(jedis, key, arrays, expire);
	}

	/**
	 * Set list data. Treat list as a single object, convert it to string format and compress it.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param list List data.
	 * @param excludeFields Exclude fields in Class {@code T}.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @param clazz Class object.
	 * @return boolean
	 * @throws Exception
	 */
	public static <T> boolean setList(Jedis jedis, String key, List<T> list, int expire, List<String> excludeFields, Class<T> clazz) throws Exception {
		List<EntityField> entityFields = ClassUtils.getSortedAndNonExcludedEntityFields(excludeFields, clazz);
		return setList(jedis, key, list, expire, entityFields);
	}

	/**
	 * Get list data from Redis. Treat list as a single object, convert it to string format and compress it.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param clazz Class object.
	 * @return {@code List<T>}
	 * @throws Exception
	 */
	public static <T> List<T> getList(Jedis jedis, String key, Class<T> clazz) throws Exception {
		return getList(jedis, key, ClassUtils.getSortedEntityFields(clazz), clazz);
	}

	/**
	 * Get list data from Redis. Treat list as a single object, convert it to string format and compress it.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param entityFields EntityField list.
	 * @param clazz Class object.
	 * @return {@code List<T>}
	 * @throws Exception From getObject and ListUtils.toEntities.
	 */
	public static <T> List<T> getList(Jedis jedis, String key, List<EntityField> entityFields, Class<T> clazz) throws Exception {
		String[][] arrays = getObject(jedis, key, String[][].class);
		return ListUtils.toEntities(arrays, clazz, entityFields);
	}

	/**
	 * Paging set list data. Separate list data to several page data accoding to the specified page size. We treat each page
	 * data as a sigle object and use setList method to save them.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param pageSize Page size.
	 * @param list List data.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @param entityFields EntityField list for {@code T}.
	 * @return boolean
	 * @throws Exception
	 */
	public static <T> boolean pagingSetList(Jedis jedis, String key, int pageSize, List<T> list, int expire, List<EntityField> entityFields) throws Exception {
		if (pageSize < 1 || list == null) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		int pagesCount = SearchUtils.calculatePagesCount(list.size(), pageSize);

		if (pagesCount == 1) {
			return setList(jedis, key, list, expire, entityFields);
		}

		for (int index = 0; index < pagesCount; index++) {
			String pageKey = pagingDataKey(key, index);
			List<T> pageData = CollectionUtils.paging(list, index + 1, pageSize);

			if (!setList(jedis, pageKey, pageData, expire, entityFields)) {
				return false;
			}
		}

		return setPagesCount(jedis, key, pagesCount, expire);
	}

	/**
	 * Paging set list data. Separate list data to several page data accoding to the specified page size. We treat each page
	 * data as a sigle object and use setList method to save them.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param pageSize Page size.
	 * @param list List data.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @param excludeFields Exclude fields in Class {@code T}.
	 * @param clazz Class object.
	 * @return boolean
	 * @throws Exception
	 */
	public static <T> boolean pagingSetList(Jedis jedis, String key, int pageSize, List<T> list, int expire, List<String> excludeFields, Class<T> clazz) throws Exception {
		List<EntityField> entityFields = ClassUtils.getSortedAndNonExcludedEntityFields(excludeFields, clazz);
		return pagingSetList(jedis, key, pageSize, list, expire, entityFields);
	}

	/**
	 * Get paging list data.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param clazz Class object.
	 * @return {@code List<T>}
	 * @throws Exception
	 */
	public static <T> List<T> pagingGetList(Jedis jedis, String key, Class<T> clazz) throws Exception {
		int pagesCount = getPagesCount(jedis, key);

		if (pagesCount < 1) {
			List<T> list = getList(jedis, key, clazz);

			if (list != null) {
				return list;
			}

			return new ArrayList<>();
		}

		List<T> list = new LinkedList<>();
		List<EntityField> entityFields = ClassUtils.getSortedEntityFields(clazz);

		for (int index = 0; index < pagesCount; index++) {
			String pageKey = pagingDataKey(key, index);
			List<T> pageData = getList(jedis, pageKey, entityFields, clazz);

			if (pageData != null) {
				list.addAll(pageData);
			}
		}

		return new ArrayList<>(list);
	}

	/**
	 * Paging set arrays data. Separate arrays data to several arrays data accoding to the specified page size. We treat
	 * each page data as a sigle object and use setList method to save them.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param arrays Arrays data.
	 * @param pageSize Page size.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean pagingSetArrays(Jedis jedis, String key, String[][] arrays, int pageSize, int expire) throws IOException {
		if (arrays == null || pageSize < 1) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		int pagesCount = SearchUtils.calculatePagesCount(arrays.length, pageSize);

		if (pagesCount == 1) {
			return setObject(jedis, key, arrays, expire);
		}

		for (int index = 0; index < pagesCount; index++) {
			String pageKey = pagingDataKey(key, index);
			String[][] pageData = ArraysUtils.pageArray(arrays, index + 1, pageSize);

			if (!setObject(jedis, pageKey, pageData, expire)) {
				return false;
			}
		}

		return setPagesCount(jedis, key, pagesCount, expire);
	}

	/**
	 * Get paging arrays data.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @return String[][]
	 * @throws IOException
	 */
	public static String[][] pagingGetArrays(Jedis jedis, String key) throws IOException {
		int pagesCount = getPagesCount(jedis, key);

		if (pagesCount < 1) {
			String[][] arrays = getObject(jedis, key, String[][].class);

			if (arrays != null) {
				return arrays;
			}

			return new String[0][];
		}

		List<String[]> list = new LinkedList<>();

		for (int index = 0; index < pagesCount; index++) {
			String pageKey = pagingDataKey(key, index);
			String[][] pageData = getObject(jedis, pageKey, String[][].class);

			if (pageData != null) {
				list.addAll(Arrays.asList(pageData));
			}
		}

		return list.toArray(new String[0][]);
	}

	/**
	 * Set pages count.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @param count Pages count.
	 * @param expire Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 */
	private static boolean setPagesCount(Jedis jedis, String key, int count, int expire) {
		if (count < 1) {
			throw new IllegalArgumentException("Illegal parameters!");
		}

		String realKey = pagesCountKey(key);
		return setInt(jedis, realKey, count, expire);
	}

	/**
	 * Get pages count.
	 * @param jedis Jedis object.
	 * @param key Redis key.
	 * @return int
	 */
	private static int getPagesCount(Jedis jedis, String key) {
		String realKey = pagesCountKey(key);
		return getInt(jedis, realKey);
	}

	/**
	 * Key of data pages count.
	 * @param key Prefix key.
	 * @return String
	 */
	private static String pagesCountKey(String key) {
		if (key == null) {
			throw new IllegalArgumentException("Illegal parameter!");
		}

		return String.format("%s:count", key);
	}

	/**
	 * Get key of paging data.
	 * @param key Prefix key.
	 * @param pageIndex Page index.
	 * @return String
	 */
	private static String pagingDataKey(String key, int pageIndex) {
		return String.format("%s:%d", key, pageIndex);
	}
}