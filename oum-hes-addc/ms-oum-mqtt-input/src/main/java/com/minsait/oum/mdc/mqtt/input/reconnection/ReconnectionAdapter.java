package com.minsait.oum.mdc.mqtt.input.reconnection;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.domain.reconnection.Reconnection;
import com.minsait.oum.mdc.domain.reconnection.ReconnectionReading;
import com.minsait.oum.mdc.domain.reconnection.ReconnectionStatus;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReconnectionAdapter extends AbstractMessageAdapter {

	@Autowired
	private Gson gson;
	
	@Autowired
	private MdmRestClient _restClient;

	@Override
	public ResponseType getResponseType() {
		return ResponseType.RECONNECTION;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {
		return convert(requestJson);
	}

	public Request convert(JsonObject jsonObjectRequest) throws MessageAdapterException {
		Request result = convertToRequest(jsonObjectRequest);
		result.setPayload(jsonObjectRequest.toString());

		return result;
	}

	private Request convertToRequest(final JsonObject jsonRequest) throws MessageAdapterException {
		Request result = new Request();
		result.setIdRequest(jsonRequest.get("idRequest").getAsString());
		if (hasJsonObject(jsonRequest, "time")) {
			result.setTime(jsonRequest.get("time").getAsLong());
		}
		result.setStatus(checkRequestStatus(jsonRequest));
		result.setRequestType(RequestType.SCHEDULED);
		Device device = buildDevice(jsonRequest);
		if (ConnectionType.CONNECTED.name().toLowerCase()
				.equals(device.getReconnection().getStatus().getOutputState().toLowerCase())) {
			result.setResponseType(ResponseType.CONNECT);
		} else {
			result.setResponseType(ResponseType.DISCONNECT);
		}
		result.getDevices().add(device);

		return result;
	}

	private RequestStatus checkRequestStatus(final JsonObject jsonRequest) {
		RequestStatus status;
		if(hasJsonObject(jsonRequest, "statusCode")) {
			try {
				status = RequestStatus.valueOf(jsonRequest.get("statusCode").getAsString().toUpperCase());
			} catch (IllegalArgumentException illegalArgumentException) {
				log.error("ERROR setting Request Status for Reconnection command: " + illegalArgumentException.getMessage());
				status = RequestStatus.ERROR;
			}
		} else {
			status = RequestStatus.OK;
		}

		return status;
	}

	private Device buildDevice(final JsonObject jsonRequest) throws MessageAdapterException {
		Device device = new Device();
		try {
			String eqCode = _restClient.getEqCodeBySerialNumber(jsonRequest.getAsJsonObject().get("device").getAsString());
			jsonRequest.getAsJsonObject().addProperty("device", eqCode);
		}catch (Exception e) {
			log.error(e.getMessage());
			jsonRequest.getAsJsonObject().addProperty("status", RequestStatus.ERROR.name());
			jsonRequest.getAsJsonObject().addProperty("reason", "serialNumber not found");
		}
		if (!_restClient.isVerticalDevice(jsonRequest.get("device").getAsString())) {
			jsonRequest.getAsJsonObject().addProperty("status", RequestStatus.ERROR.name());
			jsonRequest.getAsJsonObject().addProperty("reason", "Device " + jsonRequest.get("device").getAsString()
					+ " not found: probably is managed by another MQTT microservice instance");
			throw new MessageAdapterException("Device " + jsonRequest.get("serialnumber").getAsString()
					+ " not found: probably is managed by another MQTT microservice instance");
		}
		device.setSerialNumber(jsonRequest.get("device").getAsString());
		device.setName(jsonRequest.get("device").getAsString());
		Reconnection reconnection = buildReconnection(jsonRequest);
		device.setMeterStatus(reconnection.getStatus().getOutputState());
		device.setReconnection(reconnection);

		return device;
	}

	private Reconnection buildReconnection(final JsonObject jsonObject) {
		return Reconnection.builder()
				.status(buildReconnectionStatus(jsonObject))
				.reading(buildReconnectionReading(jsonObject.getAsJsonObject("Reading")))
				.build();
	}

	private ReconnectionStatus buildReconnectionStatus(final JsonObject jsonObject) {
		return ReconnectionStatus.builder()
				.outputState(jsonObject.get("output_state").getAsString())
				.controlState(jsonObject.get("control_state").getAsString())
				.mode(jsonObject.get("mode").getAsString())
				.build();
	}

	private ReconnectionReading buildReconnectionReading(final JsonObject jsonObject) {
		return ReconnectionReading.builder()
				.value(jsonObject.get("value").getAsString())
				.channel(jsonObject.get("channel").getAsString())
				.unitM(jsonObject.get("unitM").getAsString())
				.code(jsonObject.get("code").getAsString())
				.build();
	}

	public boolean hasJsonObject(final JsonObject jsonObject, final String property) {
		return jsonObject.has(property) && !(jsonObject.get(property) instanceof JsonNull);
	}

}
