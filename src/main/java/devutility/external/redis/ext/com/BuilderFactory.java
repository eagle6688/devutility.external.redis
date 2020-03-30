package devutility.external.redis.ext.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import devutility.external.redis.exception.JedisFatalException;
import devutility.external.redis.ext.model.ConsumerInfo;
import devutility.external.redis.ext.model.GroupInfo;
import devutility.internal.data.converter.ConverterUtils;
import redis.clients.jedis.Builder;
import redis.clients.jedis.StreamEntry;
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
					throw new JedisFatalException("Illegal response format!");
				}

				GroupInfo model = new GroupInfo();
				model.setName(RedisStreamUtils.convertToString(object.get(1)));
				model.setConsumers(ConverterUtils.objectTolong(object.get(3)));
				model.setPending(ConverterUtils.objectTolong(object.get(5)));
				model.setLastDeliveredId(new StreamEntryID(RedisStreamUtils.convertToString(object.get(7))));
				list.add(model);
			}

			return list;
		}
	};

	public static final Builder<List<ConsumerInfo>> STREAM_CONSUMERINFO_LIST = new Builder<List<ConsumerInfo>>() {
		@Override
		public List<ConsumerInfo> build(Object data) {
			List<ConsumerInfo> list = new LinkedList<ConsumerInfo>();

			if (null == data || !(data instanceof ArrayList)) {
				return list;
			}

			@SuppressWarnings("unchecked")
			List<ArrayList<Object>> objectList = (List<ArrayList<Object>>) data;

			for (ArrayList<Object> object : objectList) {
				if (object.size() != 6) {
					throw new JedisFatalException("Illegal response format!");
				}

				ConsumerInfo model = new ConsumerInfo();
				model.setName(RedisStreamUtils.convertToString(object.get(1)));
				model.setPending(ConverterUtils.objectTolong(object.get(3)));
				model.setIdle(ConverterUtils.objectToint(object.get(5)));
				list.add(model);
			}

			return list;
		}
	};

	public static final Builder<List<StreamEntry>> STREAM_ENTRY_LIST = new Builder<List<StreamEntry>>() {
		@Override
		@SuppressWarnings("unchecked")
		public List<StreamEntry> build(Object data) {
			if (null == data) {
				return null;
			}

			List<ArrayList<Object>> objectList = (List<ArrayList<Object>>) data;
			List<StreamEntry> responses = new ArrayList<>(objectList.size() / 2);

			if (objectList.isEmpty()) {
				return responses;
			}

			for (ArrayList<Object> res : objectList) {
				String entryIdString = SafeEncoder.encode((byte[]) res.get(0));
				StreamEntryID entryID = new StreamEntryID(entryIdString);
				List<byte[]> hash = (List<byte[]>) res.get(1);

				Iterator<byte[]> hashIterator = hash.iterator();
				Map<String, String> map = new HashMap<>(hash.size() / 2);

				while (hashIterator.hasNext()) {
					map.put(SafeEncoder.encode((byte[]) hashIterator.next()), SafeEncoder.encode((byte[]) hashIterator.next()));
				}

				responses.add(new StreamEntry(entryID, map));
			}

			return responses;
		}

		@Override
		public String toString() {
			return "List<StreamEntry>";
		}
	};
}