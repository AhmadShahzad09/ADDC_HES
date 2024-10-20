package com.minsait.oum.mdc.application;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.google.gson.Gson;
import com.minsait.oum.mdc.dlms.server.OnAlarmNotificationCallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryClientImpl implements InventoryClient {

	
	@Value("${equipmentByLogicalDeviceName.url}")
	private String equipmentByLogicalDeviceNameURL;
	
	@Value("${equipmentBySerialNumber.url}")
	private String equipmentBySerialNumberURL;

	@Value("${resetMeterAlarms.url}")
	private String resetMeterAlarmsURL;
	
	@Value("${addTelemetryRemoteConfiguration.username}")
	private String addTelemetryRemoteConfigurationUsername;
	
	@Value("${addTelemetryRemoteConfiguration.password}")
	private String addTelemetryRemoteConfigurationPassword;
	
	private static String RESET_ALARMS_REQUEST = "OnDemandClearAlarmsDescriptor";
	
	@Cacheable(value="equipmentPropsByLogicalDeviceName", key="#logicalDeviceName", unless="#result == null")
	public JSONObject getEquipmentPropsByLogicalDeviceName(String logicalDeviceName) {
		if (StringUtils.isNotBlank(equipmentByLogicalDeviceNameURL)) {
			HttpHeaders httpHeaders = new HttpHeaders() {
				{
					String auth = addTelemetryRemoteConfigurationUsername + ":" + addTelemetryRemoteConfigurationPassword;
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
			
			log.info("equipmentByLogicalDeviceName: ");
			ResponseEntity<String> response = new RestTemplate().exchange(
					UriUtils.encodePath(equipmentByLogicalDeviceNameURL + "/" + logicalDeviceName, "UTF-8"), 
					HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				log.info("equipmentByLogicalDeviceName: " + response.toString());
				JSONObject result = new JSONObject(response.getBody());
				if (result.has("errorCode") || result.has("errorNumber")) {
					return null;
				} else {
					return result;
				}
			}
		}
		return null;

	}
	

	@Cacheable(value="equipmentPropsBySerialNumber", key="#serialNumber", unless="#result == null")
	public JSONObject getEquipmentPropsBySerialNumber(String serialNumber) {
		if (StringUtils.isNotBlank(equipmentBySerialNumberURL)) {
			HttpHeaders httpHeaders = new HttpHeaders() {
				{
					String auth = addTelemetryRemoteConfigurationUsername + ":" + addTelemetryRemoteConfigurationPassword;
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
			
			log.info("equipmentBySerialNumber: ");
			ResponseEntity<String> response = new RestTemplate().exchange(
					UriUtils.encodePath(equipmentBySerialNumberURL + "/" + serialNumber, "UTF-8"), 
					HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				log.info("equipmentBySerialNumber: " + response.toString());
				JSONObject result = new JSONObject(response.getBody());
				if (result.has("errorCode") || result.has("errorNumber")) {
					return null;
				} else {
					return result;
				}
			}
		}
		return null;

	}

	public JSONObject resetMeterAlarms(String meterCode) {
		
		if (StringUtils.isNotBlank(resetMeterAlarmsURL)) {
			log.info("Sending new data to WS");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("equipmentCode", meterCode);
			jsonObject.put("requestCode", RESET_ALARMS_REQUEST);

			HttpHeaders httpHeaders = new HttpHeaders() {
				{
					String auth = addTelemetryRemoteConfigurationUsername + ":" + addTelemetryRemoteConfigurationPassword;
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), httpHeaders);
			log.info("resetMeterAlamrs: ");
			ResponseEntity<String> resetAlarmsResponse = new RestTemplate().exchange(resetMeterAlarmsURL, HttpMethod.POST, httpEntity, String.class);
			if (resetAlarmsResponse.getStatusCode().equals(HttpStatus.OK)) {
				log.info("resetMeterAlamrs: " + resetAlarmsResponse.toString());
			}
		}
		
		return null;
	}

	private Map<String, Object> getEquipmentProps(String jsonString) {
		return new Gson().fromJson(jsonString, Map.class);
	}
	
}
