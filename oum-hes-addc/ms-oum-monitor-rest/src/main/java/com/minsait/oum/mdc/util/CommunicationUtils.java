package com.minsait.oum.mdc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CommunicationUtils {

	public final static String FIELD_PROPERTIES = "properties";
	public final static String FIELD_COMMUNICATIONS_EQUIPMENT = "communicationsEquipment";
	public final static String FIELD_COMMUNICATIONS_EQUIPMENT_ALTERNATIVE = "communicationsEquipmentAlternative";
	public final static String FIELD_COMMUNICATIONS_EQUIPMENT_PROPERTIES = "communicationsEquipmentTypeProperties";
	public final static String FIELD_IP = "IP";
	public final static String FIELD_PORT = "PORT";
	public final static String FIELD_PATH = "PATH";
	public final static String FIELD_PRIMARY = "primary";
	public final static String FIELD_SECONDARY = "secundary"; // sic
	public final static String FIELD_NAME = "name";
	public final static String FIELD_VALUE = "value";
	public final static String FIELD_CATEGORY = "category";
	
	public static String generateIpPort(Map<String, Object> communication) {
		String url = StringUtils.EMPTY;
		if (null != communication) {

			final Map<String, String> fields = generateFieldsProperties(communication);
			if (fields.isEmpty()) {
				fields.putAll(generateFields(communication));
			}
			
			String ip = fields.get(FIELD_IP);
			String port = fields.get(FIELD_PORT);
			String path = fields.get(FIELD_PATH);
//			if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
//				final Map<String, String> fieldsAlternative = CommunicationUtils.generateFieldsAlternative(communication);
//				ip = fieldsAlternative.get(FIELD_IP);
//				port = fieldsAlternative.get(FIELD_PORT);
//				path = fieldsAlternative.get(FIELD_PATH);
//			}
			
			if (StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(port)) {
				url = ip + ":" + port;
			} else if (StringUtils.isNotEmpty(ip) && StringUtils.isEmpty(port)){
				url = ip;
			}
			if (StringUtils.isNotEmpty(path)) {
				url += path;
			}
			
		}
		return url;
	}
	
	public static Map<String, String> generateFieldsProperties(Map<String, Object> communication) {
		return generateFieldsProperties(communication, null, FIELD_PROPERTIES);
	}
	
	@Deprecated
	public static Map<String, String> generateFields(Map<String, Object> communication) {
		return generateFields(communication, null, FIELD_COMMUNICATIONS_EQUIPMENT, FIELD_COMMUNICATIONS_EQUIPMENT_PROPERTIES);
	}
//	
//	@Deprecated
//	public static Map<String, String> generateFieldsAlternative(Map<String, Object> communication) {
//		return generateFields(communication, null, FIELD_COMMUNICATIONS_EQUIPMENT_ALTERNATIVE, FIELD_COMMUNICATIONS_EQUIPMENT_PROPERTIES);
//	}
//	
	@Deprecated
	public static Map<String, String> generateFields(Map<String, Object> communication, String category) {
		return generateFields(communication, category, FIELD_COMMUNICATIONS_EQUIPMENT, FIELD_COMMUNICATIONS_EQUIPMENT_PROPERTIES);
	}
//	
//	@Deprecated
//	public static Map<String, String> generateFieldsAlternative(Map<String, Object> communication, String category) {
//		return generateFields(communication, category, FIELD_COMMUNICATIONS_EQUIPMENT_ALTERNATIVE, FIELD_COMMUNICATIONS_EQUIPMENT_PROPERTIES);
//	}
//	
	@Deprecated
	public static Map<String, String> generateFields(Map<String, Object> communication, String category, String communicationsEquipmentKey, String communicationsEquipmentTypePropertiesKey) {
		final Map<String, String> mapFields = new HashMap<>();

		if (null != communication) {
			
			final Map<String, Object> communicationsEquipment = (Map<String, Object>) communication.get(communicationsEquipmentKey);
			if (null != communicationsEquipment) {
				mapFields.putAll(generateFieldsProperties(communicationsEquipment, category, communicationsEquipmentTypePropertiesKey));
			}
			// TODO Deprecated
			else {
				if (FIELD_COMMUNICATIONS_EQUIPMENT.equals(communicationsEquipmentKey)) {
					final String primary = (String) communication.get(FIELD_PRIMARY);
					if (StringUtils.isNotBlank(primary)) {
						final String[] ipAndPort = primary.split(":", 2);
						if (ipAndPort.length == 2) {
							mapFields.put(FIELD_IP, ipAndPort[0]);
							mapFields.put(FIELD_PORT, ipAndPort[1]);
						}
					}
				}
				else if (FIELD_COMMUNICATIONS_EQUIPMENT_ALTERNATIVE.equals(communicationsEquipmentKey)) {
					final String secondary = (String) communication.get(FIELD_SECONDARY);
					if (StringUtils.isNotBlank(secondary)) {
						final String[] ipAndPort = secondary.split(":", 2);
						if (ipAndPort.length == 2) {
							mapFields.put(FIELD_IP, ipAndPort[0]);
							mapFields.put(FIELD_PORT, ipAndPort[1]);
						}
					}
				}
			}
			
		}
		
		return mapFields;
	}
	
	public static Map<String, String> generateFieldsProperties(Map<String, Object> communication, String category, String fieldProperties) {
		final Map<String, String> mapFields = new HashMap<>();

		if (null != communication) {
			
			final List<Map<String, Object>> properties = (List<Map<String, Object>>) communication.get(fieldProperties);
			if (null != properties) {
				properties.forEach(x -> {
					final String key = (String) x.get(FIELD_NAME);
					final String value = (String) x.get(FIELD_VALUE);
					if (StringUtils.isBlank(category)) {
						mapFields.put(key, value);
					}
					else { 
						final String fieldCategory = (String) x.get(FIELD_CATEGORY);
						if (category.equals(fieldCategory)) {
							mapFields.put(key, value);
						}
					}
				});
			}
			
		}
		
		return mapFields;
	}

}
