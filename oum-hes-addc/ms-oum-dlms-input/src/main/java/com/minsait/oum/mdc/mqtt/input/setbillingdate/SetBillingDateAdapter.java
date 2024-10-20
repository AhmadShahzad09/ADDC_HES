package com.minsait.oum.mdc.mqtt.input.setbillingdate;

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
public class SetBillingDateAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.SET_BILLING_DATE;
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
			String billingDateStatus = jsonObject.get("status").getAsString();
			if (!billingDateStatus.equalsIgnoreCase("OK"))
				device.setErrorMessage(
						String.format("cannot set billing date: meter responds with status '%s'", billingDateStatus));
			return device;
		});

		return request;
	}

}
