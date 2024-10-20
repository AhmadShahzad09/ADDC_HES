package com.minsait.oum.mdc.mqtt.input.setpaymentmode;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import org.springframework.stereotype.Service;

@Service
public class SetPaymentModeAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.SET_PAYMENT_MODE;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		parseDevice(request, "device", jsonRequest,
				(device, json) -> device.setMeterStatus(jsonRequest.get("status").getAsString()));

		return request;
	}

}
