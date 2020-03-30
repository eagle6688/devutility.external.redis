package devutility.external.redis.helper.single;

import java.io.IOException;
import java.util.List;

import devutility.external.redis.model.SingleRedisInstance;
import devutility.external.redis.utils.JedisStringUtils;
import devutility.external.redis.utils.pool.JedisPoolUtil;
import devutility.internal.model.EntityField;
import redis.clients.jedis.Jedis;

public class SingleJedisStringHelper extends SingleJedisHelper {
	/**
	 * Constructor.
	 * @param redisInstance
	 */
	public SingleJedisStringHelper(SingleRedisInstance redisInstance) {
		super(redisInstance);
	}

	/**
	 * Set string value.
	 * @param key: Redis key.
	 * @param value: String value.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 */
	public boolean set(String key, String value, int expire) {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.set(jedis, key, value, expire);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set string value.
	 * @param key: Redis key.
	 * @param value: String value.
	 * @return boolean
	 */
	public boolean set(String key, String value) {
		return set(key, value, 0);
	}

	/**
	 * Get string value.
	 * @param key: Redis key.
	 * @return String
	 */
	public String get(String key) {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.get(jedis, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Set int value.
	 * @param key: Redis key.
	 * @param value: Int value.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 */
	public boolean setInt(String key, int value, int expire) {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.setInt(jedis, key, value, expire);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set int value.
	 * @param key: Redis key.
	 * @param value: Int value.
	 * @return boolean
	 */
	public boolean setInt(String key, int value) {
		return setInt(key, value, 0);
	}

	/**
	 * Get int value.
	 * @param key: Redis key.
	 * @return int
	 */
	public int getInt(String key) {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.getInt(jedis, key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Set object value.
	 * @param key: Redis key.
	 * @param value: Object value.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setObject(String key, Object value, int expire) throws IOException {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.setObject(jedis, key, value, expire);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Set object value.
	 * @param key: Redis key.
	 * @param value: Object value.
	 * @return boolean
	 * @throws IOException
	 */
	public boolean setObject(String key, Object value) throws IOException {
		return setObject(key, value, 0);
	}

	/**
	 * Get object.
	 * @param key: Redis key.
	 * @param clazz: Class object.
	 * @return {@code T}
	 * @throws IOException
	 */
	public <T> T getObject(String key, Class<T> clazz) throws IOException {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.getObject(jedis, key, clazz);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Set list data. Treat list as a single object, convert it to string format and compress it.
	 * @param key: Redis key.
	 * @param list: List data.
	 * @param entityFields: EntityField list for {@code T}.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 * @throws Exception
	 */
	public <T> boolean setList(String key, List<T> list, int expire, List<EntityField> entityFields) throws Exception {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.setList(jedis, key, list, expire, entityFields);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Set list data. Treat list as a single object, convert it to string format and compress it.
	 * @param key: Redis key.
	 * @param list: List data.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @param excludeFields: Exclude fields in Class {@code T}.
	 * @param clazz: Class object.
	 * @return boolean
	 * @throws Exception
	 */
	public <T> boolean setList(String key, List<T> list, int expire, List<String> excludeFields, Class<T> clazz) throws Exception {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.setList(jedis, key, list, expire, excludeFields, clazz);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Get list data from Redis.
	 * @param key: Redis key.
	 * @param clazz: Class object.
	 * @return {@code List<T>}
	 * @throws Exception
	 */
	public <T> List<T> getList(String key, Class<T> clazz) throws Exception {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.getList(jedis, key, clazz);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Paging set list data. Separate list data to several page data accoding to the specified page size. We treat each page
	 * data as a sigle object and use setList method to save them.
	 * @param key: Redis key.
	 * @param pageSize: Page size.
	 * @param list: List data.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @param excludeFields: Exclude fields in Class {@code T}.
	 * @param clazz: Class object.
	 * @return boolean
	 * @throws Exception
	 */
	public <T> boolean pagingSetList(String key, int pageSize, List<T> list, int expire, List<String> excludeFields, Class<T> clazz) throws Exception {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.pagingSetList(jedis, key, pageSize, list, expire, excludeFields, clazz);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Paging set list data. Separate list data to several page data accoding to the specified page size. We treat each page
	 * data as a sigle object and use setList method to save them.
	 * @param key: Redis key.
	 * @param pageSize: Page size.
	 * @param list: List data.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @param entityFields: Exclude fields in Class {@code T}.
	 * @return boolean
	 * @throws Exception
	 */
	public <T> boolean pagingSetList(String key, int pageSize, List<T> list, int expire, List<EntityField> entityFields) throws Exception {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.pagingSetList(jedis, key, pageSize, list, expire, entityFields);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Get paging list data.
	 * @param key: Redis key.
	 * @param clazz: Class object.
	 * @return {@code List<T>}
	 * @throws Exception
	 */
	public <T> List<T> pagingGetList(String key, Class<T> clazz) throws Exception {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.pagingGetList(jedis, key, clazz);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Paging set arrays data. Separate arrays data to several arrays data accoding to the specified page size. We treat
	 * each page data as a sigle object and use setList method to save them.
	 * @param key: Redis key.
	 * @param arrays: Arrays data.
	 * @param pageSize: Page size.
	 * @param expire: Expire time in seconds, 0 is permanent item.
	 * @return boolean
	 * @throws IOException
	 */
	public boolean pagingSetArrays(String key, String[][] arrays, int pageSize, int expire) throws IOException {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.pagingSetArrays(jedis, key, arrays, pageSize, expire);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Get paging arrays data.
	 * @param key: Redis key.
	 * @return String[][]
	 * @throws IOException
	 */
	public String[][] pagingGetArrays(String key) throws IOException {
		try (Jedis jedis = JedisPoolUtil.jedis(redisInstance)) {
			return JedisStringUtils.pagingGetArrays(jedis, key);
		} catch (IOException e) {
			throw e;
		}
	}
}