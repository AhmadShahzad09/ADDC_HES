package com.minsait.oum.mdc.mqtt.input.getcurrentrangeup;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

@Service
public class GetCurrentRangeUpAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.GET_CURRENT_RANGE_UP;
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
			device.setCurrentThresholdUp(deviceJsonObject.get("threshold").getAsString());

			return device;

		});

		return request;
	}

}
