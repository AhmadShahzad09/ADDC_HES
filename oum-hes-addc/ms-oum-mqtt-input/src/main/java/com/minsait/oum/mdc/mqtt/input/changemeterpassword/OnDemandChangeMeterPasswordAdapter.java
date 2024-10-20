package com.minsait.oum.mdc.mqtt.input.changemeterpassword;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OnDemandChangeMeterPasswordAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.CHANGE_METER_PASSWORD;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		parseDevices(request, jsonRequest, "devices", jsonElement -> {
			Device device = new Device();
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			device.setSerialNumber(jsonObject.get("serialNumber").getAsString());
			device.setStatusCode(jsonObject.get("status").getAsString());

			return device;
		});
		return request;
	}

}
