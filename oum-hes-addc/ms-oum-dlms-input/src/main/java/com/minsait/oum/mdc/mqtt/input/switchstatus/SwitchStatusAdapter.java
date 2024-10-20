package com.minsait.oum.mdc.mqtt.input.switchstatus;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

@Service
public class SwitchStatusAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.SWITCH_STATUS;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		parseDevice(request, "device", jsonRequest,
				(device, json) -> device.setMeterStatus(jsonRequest.get("output_state").getAsString()));

		return request;
	}

}
