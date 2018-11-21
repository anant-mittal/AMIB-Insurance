package com.amx.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amx.utils.ArgUtil.EnumById;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;


/**
 * The Class JsonUtil.
 */
public final class JsonUtil {

	/** The Constant LOG. */
	static final Logger LOG = Logger.getLogger(JsonUtil.class);

	/**
	 * Instantiates a new json util.
	 */
	private JsonUtil() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	/**
	 * The Class JsonUtilConfigurable.
	 */
	public static class JsonUtilConfigurable {

		/** The mapper. */
		private final ObjectMapper mapper;

		/**
		 * Gets the mapper.
		 *
		 * @return the mapper
		 */
		public ObjectMapper getMapper() {
			return this.mapper;
		}

		/**
		 * Instantiates a new json util configurable.
		 *
		 * @param mapper
		 *            the mapper
		 */
		public JsonUtilConfigurable(ObjectMapper mapper) {
			this.mapper = mapper;
		}

		/**
		 * From json.
		 *
		 * @param <E>
		 *            the element type
		 * @param json
		 *            the json
		 * @param type
		 *            the type
		 * @return the e
		 */
		public <E> E fromJson(String json, Class<E> type) {
			if (json == null || "".equals(json.trim()) || "\"\"".equals(json.trim())) {
				return null;
			}
			try {
				return getMapper().readValue(json, type);
			} catch (IOException e) {
				LOG.warn("error converting from json=" + json, e);
			}
			return null;
		}

		@SuppressWarnings("rawtypes")
		public <E> E fromJson(String json, TypeReference valueTypeRef) {
			if (json == null || "".equals(json.trim()) || "\"\"".equals(json.trim())) {
				return null;
			}
			try {
				return getMapper().readValue(json, valueTypeRef);
			} catch (IOException e) {
				LOG.warn("error converting from json=" + json, e);
			}
			return null;
		}

		/**
		 * To json.
		 *
		 * @param object
		 *            the object
		 * @return the string
		 */
		public String toJson(Object object) {
			try {
				return getMapper().writeValueAsString(object);
			} catch (IOException e) {
				LOG.warn("error converting to json", e);
			}
			return null;
		}

		/**
		 * To map.
		 *
		 * @param object
		 *            the object
		 * @return the map
		 */
		@SuppressWarnings("unchecked")
		public Map<String, Object> toMap(Object object) {
			return getMapper().convertValue(object, Map.class);
		}

		/**
		 * To json.
		 *
		 * @param outputStream
		 *            the output stream
		 * @param object
		 *            the object
		 */
		public void toJson(OutputStream outputStream, Object object) {
			try {
				getMapper().writeValue(outputStream, object);
			} catch (IOException e) {
				LOG.warn("error converting to json", e);
			}
		}
	}

	/** The Constant instance. */
	public static final JsonUtil.JsonUtilConfigurable instance;
	static {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("MyModule", new Version(1, 0, 0, null, null, null));
		module.addSerializer(EnumById.class, new EnumByIdSerializer());
		module.addSerializer(JsonSerializerType.class, new JsonSerializerTypeSerializer());
		mapper.registerModule(module);
		instance = new JsonUtil.JsonUtilConfigurable(mapper);
	}

	/**
	 * Gets the mapper.
	 *
	 * @return the mapper
	 */
	public static ObjectMapper getMapper() {
		return instance.mapper;
	}

	/**
	 * From json.
	 *
	 * @param <E>
	 *            the element type
	 * @param json
	 *            the json
	 * @param type
	 *            the type
	 * @return the e
	 */
	public static <E> E fromJson(String json, Class<E> type) {
		return instance.fromJson(json, type);
	}

	@SuppressWarnings("rawtypes")
	public static <E> E fromJson(String json, TypeReference valueTypeRef) {
		return instance.fromJson(json, valueTypeRef);
	}

	/**
	 * To json.
	 *
	 * @param object
	 *            the object
	 * @return the string
	 */
	public static String toJson(Object object) {
		return instance.toJson(object);
	}

	/**
	 * To map.
	 *
	 * @param object
	 *            the object
	 * @return the map
	 */
	public static Map<String, Object> toMap(Object object) {
		return instance.toMap(object);
	}

	/**
	 * To json.
	 *
	 * @param outputStream
	 *            the output stream
	 * @param object
	 *            the object
	 */
	public static void toJson(OutputStream outputStream, Object object) {
		instance.toJson(outputStream, object);
	}

	/**
	 * Gets the linked map from json string.
	 *
	 * @param jsonString
	 *            the json string
	 * @return the linked map from json string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getLinkedMapFromJsonString(String jsonString) throws IOException {
		return (Map<String, Object>) instance.getMapper().readValue(jsonString, LinkedHashMap.class);
	}

	/**
	 * Gets the object list from json string.
	 *
	 * @param jsonStr
	 *            the json str
	 * @return the object list from json string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getObjectListFromJsonString(String jsonStr) throws IOException {
		return ((List<Object>) instance.getMapper().readValue(jsonStr, List.class));
	}

	/**
	 * Gets the json string object.
	 *
	 * @param jsonMap
	 *            the json map
	 * @return the json string object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String getJsonStringObject(Object jsonMap) throws IOException {
		return instance.getMapper().writeValueAsString(jsonMap);
	}

	/**
	 * Gets the json string from map.
	 *
	 * @param jsonMap
	 *            the json map
	 * @return the json string from map
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String getJsonStringFromMap(Map<String, Object> jsonMap) throws IOException {
		return instance.getMapper().writeValueAsString(jsonMap);
	}

	/**
	 * Gets the map from json string.
	 *
	 * @param jsonString
	 *            the json string
	 * @return the map from json string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapFromJsonString(String jsonString) throws IOException {
		return ((Map<String, Object>) instance.getMapper().readValue(jsonString, Map.class));
	}
	

	/**
	 * Gets the generic object list from json string.
	 *
	 * @param <T>
	 *            the generic type
	 * @param jsonStr
	 *            the json str
	 * @return the generic object list from json string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static <T> List<T> getListFromJsonString(String jsonStr) throws IOException {
		return instance.getMapper().readValue(jsonStr, new TypeReference<List<T>>() {
		});
	}

}

class EnumByIdSerializer extends JsonSerializer<EnumById> {

	@Override
	public void serialize(EnumById value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(value.getId());
	}
}


