package devutility.external.redis;

import java.util.Arrays;

public class PaginationUtils {
	/**
	 * Calculate pages count for given total count and page size.
	 * @param totalCount: Total records count.
	 * @param pageSize: Page size
	 * @return int
	 */
	public static int calculatePagesCount(int totalCount, int pageSize) {
		if (totalCount % pageSize == 0) {
			return totalCount / pageSize;
		}

		return totalCount / pageSize + 1;
	}

	/**
	 * Key of Pages count
	 * @param key: Redis key
	 * @return String
	 */
	public static String pagesCountKey(String key) {
		return String.format("%s:count", key);
	}

	/**
	 * Key of paging data
	 * @param key
	 * @param pageIndex
	 * @return String
	 */
	public static String pagingDataKey(String key, int pageIndex) {
		return String.format("%s:%d", key, pageIndex);
	}

	/**
	 * Paging array
	 * @param array: Array data
	 * @param pageIndex: Page index
	 * @param pageSize: Page size
	 * @return String[][]
	 */
	public static String[][] pagingArray(String[][] array, int pageIndex, int pageSize) {
		return (String[][]) Arrays.stream(array).skip(pageIndex * pageSize).limit(pageSize).toArray();
	}
}