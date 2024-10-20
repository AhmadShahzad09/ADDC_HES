package com.minsait.mdc.util;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.google.gson.Gson;
import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.ChangeIPVO;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.ChangeStatusVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MdmRestClient {

	private static final String GATEWAY_NOT_FOUND_MSG = "Equipment does not exist";

	@Value("${param.gateway.params}")
	private String gatewayParam;

	@Value("${param.find.meter}")
	private String findMeter;

	@Value("${changeip.gateway}")
	private String changeIpWS;

	@Value("${changestatus.gateway}")
	private String changeStatusWS;

	@Value("${finish.commission}")
	private String finishCommission;

	@Value("${add.telemetry.remote.configuration}")
	private String setTelemetryRemoteConfigurationURL;

	@Value("${user.hb}")
	private String userHb;

	@Value("${pass.hb}")
	private String passHb;
	
	@Value("${find.equipment.by.serialnumber}")
	private String equipmentBySerialNumber;

	@Value("${confirm.kms.password}")
	private String confirmKeysDeployment;

	private RestTemplate _restTemplate = new RestTemplate();

	public Optional<DeviceStatus> getDeviceStatus(String deviceName) {
		try {
			ResponseEntity<String> response = this
					.checkErrorResponse(this.mdmRequest(UriUtils.encodePath(findMeter + deviceName, "UTF-8"),
							HttpMethod.GET, createMdmGetRequest(), String.class));
			JSONObject responseBody = new JSONObject(response.getBody());

			return Optional.of(DeviceStatus.valueOf(responseBody.getString("status")));
		} catch (RestClientException rcex) {
			// not the best way to do it, but the quickest
			if (rcex.getMessage().equalsIgnoreCase(GATEWAY_NOT_FOUND_MSG)) {
				return Optional.empty();
			} else {
				throw rcex;
			}

		}

	}

	public ResponseEntity<String> getGatewayParams(String deviceName) {
		return this.checkErrorResponse(this.mdmRequest(UriUtils.encodePath(gatewayParam + deviceName, "UTF-8"),
				HttpMethod.GET, createMdmGetRequest(), String.class));

	}

	public ResponseEntity<String> setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO config) {
		log.info("setting device {} telemetry remote config data type '{}' with value {} ", config.getSerialNumber(),
				config.getDataType(), config.getValue());

		return this.checkErrorResponse(this.mdmRequest(setTelemetryRemoteConfigurationURL, HttpMethod.POST,
				createMdmPostRequest(new Gson().toJson(config)), String.class));
	}

	public ResponseEntity<String> setDeviceStatus(String deviceName, DeviceStatus status) {
		log.info("setting device {} status to {} ", deviceName, status);

		ChangeStatusVO changeStatusVO = new ChangeStatusVO();
		changeStatusVO.setStatus(status.name());
		changeStatusVO.setSerialNumber(deviceName);

		return this.checkErrorResponse(this.mdmRequest(changeIpWS, HttpMethod.POST,
				createMdmPostRequest(new Gson().toJson(changeStatusVO)), String.class));
	}

	public ResponseEntity<String> setDeviceIp(String deviceName, String ip, int port) {
		log.info("setting device {} address to {} ", deviceName, ip);

		ChangeIPVO changeIpVO = new ChangeIPVO();
		changeIpVO.setIp(ip);
		changeIpVO.setPort(port + "");
		changeIpVO.setSerialNumber(deviceName);

		return this.checkErrorResponse(this.mdmRequest(changeIpWS, HttpMethod.POST,
				createMdmPostRequest(new Gson().toJson(changeIpVO)), String.class));
	}

	public ResponseEntity<String> setFinishComission(String finishComissionMessage) {
		log.info("==> Message: COMMISSIONING -> updating status in MDM" + finishComissionMessage);

		HttpHeaders headers = AppUtils.createHeaders(this.userHb, this.passHb);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return this.checkErrorResponse(this.mdmRequest(finishCommission, HttpMethod.POST,
				createMdmPostRequest(finishComissionMessage, headers), String.class));

	}

	public boolean isChangeDeviceStatusWebServiceEnabled() {
		return StringUtils.isNotBlank(changeStatusWS);
	}

	public boolean isChangeDeviceIpWebServiceEnabled() {
		return StringUtils.isNotBlank(changeIpWS);
	}

	private HttpEntity<String> createMdmGetRequest() {
		return new HttpEntity<>(AppUtils.createHeaders(this.userHb, this.passHb));
	}

	private HttpEntity<String> createMdmPostRequest(String body, HttpHeaders headers) {
		return new HttpEntity<>(body, headers);
	}

	private HttpEntity<String> createMdmPostRequest(String body) {
		return this.createMdmPostRequest(body, AppUtils.createHeaders(this.userHb, this.passHb));
	}

	private ResponseEntity<String> checkErrorResponse(ResponseEntity<String> responseEntity) {

		// response body is not a valid json, immediately return response entity
		if (!JSONUtils.isValid(responseEntity.getBody().trim()))
			return responseEntity;

		JSONObject jsonBody = new JSONObject(responseEntity.getBody());
		boolean errorResponse = jsonBody.has("errorNumber") && jsonBody.has("errorCode") && jsonBody.has("stackTrace");

		if (errorResponse)
			throw new RestClientException(jsonBody.getString("stackTrace"));

		return responseEntity;
	}

	private <T> ResponseEntity<T> mdmRequest(String url, HttpMethod method, HttpEntity<T> requestEntity,
			Class<T> responseType) {
		log.info("new MDM HeartBeat {} request: {} ", method, url);
		ResponseEntity<T> result = _restTemplate.exchange(url, method, requestEntity, responseType);
		log.info("new MDM HeartBeat {} response[code = {}]: {}", method, result.getStatusCode(), result.getBody());

		return result;
	}

	@Cacheable(value="codeBySerialNumber", key="#serialNumber")
	public String getEqCodeBySerialNumber(final String serialNumber) {
		try {
			ResponseEntity<String> response = this
					.checkErrorResponse(this.mdmRequest(UriUtils.encodePath(equipmentBySerialNumber + serialNumber, "UTF-8"),
							HttpMethod.GET, createMdmGetRequest(), String.class));
			JSONObject responseBody = new JSONObject(response.getBody());

			return responseBody.getString("code");
		} catch (RestClientException rcex) {
			// not the best way to do it, but the quickest
			throw rcex;

		}
	}

	public ResponseEntity<String> confirmKeysDeployment(final String meterId) {
		log.info("confirm device {} set the new password ", meterId);

		return this.checkErrorResponse(this.mdmRequest(confirmKeysDeployment, HttpMethod.POST,
				createMdmPostRequest(new Gson().toJson(meterId)), String.class));
	}

	@Cacheable(value="verticalSerialNumber", key="#serialNumber")
	public boolean isVerticalDevice(final String serialNumber) {
		boolean found = true;
		Optional<DeviceStatus> statusOpt = getDeviceStatus(serialNumber);

		if (!statusOpt.isPresent()) {
			log.warn("Device {} not found: probably is managed by another MQTT microservice instance", serialNumber);
			found = false;
		}

		return found;
	}
}
