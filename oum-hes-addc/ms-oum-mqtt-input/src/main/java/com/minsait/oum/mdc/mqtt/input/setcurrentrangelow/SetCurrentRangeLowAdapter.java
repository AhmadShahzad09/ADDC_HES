package com.minsait.oum.mdc.mqtt.input.setcurrentrangelow;

import com.google.gson.*;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.*;
import org.springframework.stereotype.*;

@Service
public class SetCurrentRangeLowAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.SET_CURRENT_RANGE_LOW;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		this.parseDevices(request, jsonRequest, "devices", jsonElement -> {
			JsonObject deviceJsonObject = jsonElement.getAsJsonObject();

			Device device = new Device();
			device.setSerialNumber(deviceJsonObject.get("serialNumber").getAsString());
			device.setCurrentRangeLowStatus(deviceJsonObject.get("status").getAsString());

			return device;

		});

		return request;
	}

}
