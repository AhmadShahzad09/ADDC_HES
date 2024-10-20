package com.minsait.oum.mdc.mqtt.input.setmeteringmode;

import com.google.gson.*;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.*;
import org.springframework.stereotype.*;


@Service
public class SetMeteringModeAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.SET_METERING_MODE;
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
			device.setSerialNumber(jsonObject.get("device").getAsString());
			device.setMeteringModeStatus(jsonObject.get("status").getAsString());
			return device;
		});

		return request;
	}

}
