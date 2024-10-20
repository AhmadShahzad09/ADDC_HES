package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.v09;

import java.util.UUID;

import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.HeartBeatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service("HeartBeatAdapter_V9")
public class HeartBeatAdapter {
	@Autowired
	private Gson gson;

	public Request convert(String jsonRequest) throws HeartBeatException {

		String normalizedJsonRequest = jsonRequest.replaceAll("\\^M", "").replaceAll("\\^", "");

		JsonObject jsonObjectRequest = gson.fromJson(normalizedJsonRequest, JsonObject.class);
		Request result = this.convertToRequest(jsonObjectRequest);
		result.setResponseType(ResponseType.HEART_BEAT);

		// Cuando se reciba este código debe ser "dinamico" dentro de
		// convertToRequest
		result.setRequestType(RequestType.SCHEDULED);
		result.setPayload(jsonRequest);

		HeartBeat hb = new HeartBeat();
		Device device = new Device();

		try {
			hb.setModelName(jsonObjectRequest.get("modelname").getAsString());
			hb.setDeviceName(jsonObjectRequest.get("devicename").getAsString());
			hb.setVersion(jsonObjectRequest.get("version").getAsString());
			try {
				hb.setMacAddress(jsonObjectRequest.get("mac_address").getAsString());
			}catch(Exception e) {
				hb.setMacAddress(jsonObjectRequest.get("mac address").getAsString());
			}
			try {
				hb.setSerialNumber(jsonObjectRequest.get("serial_number").getAsString());
			}catch(Exception e) {
				hb.setSerialNumber(jsonObjectRequest.get("serial number").getAsString());
			}
			hb.setWwanIP(jsonObjectRequest.get("wwan_ip").getAsString());

			device.setIp(hb.getWwanIP());
			device.setMacAddress(hb.getMacAddress());
			device.setSerialNumber(hb.getSerialNumber());
			device.setModel(hb.getModelName());
			device.setVersion(hb.getVersion());
			device.setName(hb.getDeviceName());

		} catch (Exception dex) {
			HeartBeatException heartBeatException = new HeartBeatException(
					"Message from gateway comes with empty/NULL values", result, dex);
			throw heartBeatException;
		}
		result.getDevices().add(device);
		return (result);
	}

	private Request convertToRequest(JsonObject jsonRequest) {

		Request result = new Request();

		String uuidRequest = UUID.randomUUID().toString();
		result.setIdRequest(uuidRequest);
		result.setStatus(RequestStatus.OK);

		try {
			result.setTime(jsonRequest.get("time").getAsLong());
		} catch (Exception e) {

		}
		return (result);
	}


}
