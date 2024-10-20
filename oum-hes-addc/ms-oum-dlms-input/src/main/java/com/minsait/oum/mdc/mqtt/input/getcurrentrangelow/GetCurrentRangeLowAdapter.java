package com.minsait.oum.mdc.mqtt.input.getcurrentrangelow;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

@Service
public class GetCurrentRangeLowAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.GET_CURRENT_RANGE_LOW;
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
			device.setName(deviceJsonObject.get("serialNumber").getAsString());
			device.setCurrentThresholdLow(deviceJsonObject.get("threshold").getAsString());

			return device;

		});

		return request;
	}

}
