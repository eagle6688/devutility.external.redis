package devutility.external.redis.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * StreamData
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 21:37:10
 */
public class StreamData {
	public static Map<String, String> DATA = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = -1242505356500441352L;
		{
			put("Name", "Aldwin Su");
			put("Age", "32");
			put("Time", new Date().toString());
		}
	};
}