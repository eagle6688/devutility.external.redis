package devutility.external.redis.ext.com;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.ext.model.GroupInfo;
import redis.clients.jedis.Builder;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 
 * BuilderFactory
 * 
 * @author: Aldwin Su
 * @version: 2019-10-31 22:08:25
 */
public final class BuilderFactory {
	public static final Builder<List<GroupInfo>> STREAM_GROUPINFO_LIST = new Builder<List<GroupInfo>>() {
		@Override
		public List<GroupInfo> build(Object data) {
			List<GroupInfo> list = new LinkedList<GroupInfo>();

			if (null == data || !(data instanceof ArrayList)) {
				return list;
			}

			@SuppressWarnings("unchecked")
			List<ArrayList<Object>> objectList = (List<ArrayList<Object>>) data;

			for (ArrayList<Object> object : objectList) {
				if (object.size() != 8) {
					throw new JedisFatalException("Illegal response!");
				}

				GroupInfo groupInfo = new GroupInfo();
				groupInfo.setName(SafeEncoder.encode((byte[]) object.get(1)));

				if (object.get(3) instanceof Long) {
					groupInfo.setConsumers((Long) object.get(3));
				}

				if (object.get(5) instanceof Long) {
					groupInfo.setPending((Long) object.get(5));
				}

				groupInfo.setLastDeliveredId(new StreamEntryID(SafeEncoder.encode((byte[]) object.get(7))));
				list.add(groupInfo);
			}

			return list;
		}
	};
}