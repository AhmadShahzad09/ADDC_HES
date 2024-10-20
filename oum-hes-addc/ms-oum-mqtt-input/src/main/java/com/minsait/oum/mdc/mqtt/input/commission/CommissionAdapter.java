package com.minsait.oum.mdc.mqtt.input.commission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommissionAdapter  {

	@Autowired
	private Gson gson;
	
	public Request convert(String jsonRequest) throws CommissionException {

		String normalizedJsonRequest = jsonRequest.replaceAll("\\^M", "").replaceAll("\\^", "");

		JsonObject jsonObjectRequest = gson.fromJson(normalizedJsonRequest, JsonObject.class);
		Request result = this.convertToRequest(jsonObjectRequest);

		result.setRequestType(RequestType.SCHEDULED);
		result.setResponseType(ResponseType.COMMISSION);
		result.setPayload(jsonRequest);
		
		JsonArray jsonDevices = jsonObjectRequest.get("meters").getAsJsonArray();
		for (JsonElement jsonDevice : jsonDevices) {
			Device device = this.getDevice(jsonDevice);
			result.getDevices().add(device);
		}
		return result;
	}

	private Request convertToRequest(JsonObject jsonRequest) {

		Request result = new Request();

		String uuidRequest = jsonRequest.get("idRequest").getAsString();
		result.setIdRequest(uuidRequest);

		try {
			result.setTime(jsonRequest.get("time").getAsLong());
		} catch (Exception e) {

		}

		return result;
	}

	private Device getDevice(JsonElement jsonDevice) {

		Device result = new Device();

		JsonObject jsonObject = gson.fromJson(jsonDevice.toString(), JsonObject.class);

		result.setSerialNumber(jsonObject.get("serialNumber").getAsString());
		result.setMeterStatus(jsonObject.get("status").getAsString() != null
				&& jsonObject.get("status").getAsString().toUpperCase().equals("OK") ? RequestStatus.OK.name()
						: RequestStatus.ERROR.name());
		try {
			result.setErrorMessage(jsonObject.get("reason").getAsString());
		} catch (Exception e) {
			result.setErrorMessage("Meter commissioned");
		}
		result.setName(jsonObject.get("serialNumber").getAsString());

		return result;
	}

}
