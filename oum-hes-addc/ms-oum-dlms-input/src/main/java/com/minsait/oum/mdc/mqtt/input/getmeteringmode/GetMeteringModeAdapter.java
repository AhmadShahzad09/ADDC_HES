package com.minsait.oum.mdc.mqtt.input.getmeteringmode;

import com.google.gson.*;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.*;
import org.springframework.stereotype.*;


@Service
public class GetMeteringModeAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.GET_METERING_MODE;
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
			device.setMeteringMode(jsonObject.get("meteringMode").getAsString());
			return device;
		});

		return request;
	}

}
