package com.minsait.oum.mdc.mqtt.input.billingreset;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

@Component
public class BillingResetAdapter extends AbstractMessageAdapter {

	@Override
	public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {

		this.parseDevices(request, requestJson, "profiles", jsonElement -> {
			Device device = new Device();
			JsonObject billingResetJson = jsonElement.getAsJsonObject();
			String deviceID = billingResetJson.get("device").getAsString();
			JsonArray billingResetList = billingResetJson.get("mdi_eob").getAsJsonArray();

			if (billingResetList.isEmpty())
				throw new MessageAdapterException(String.format(
						"incorrect max demand billing reset response: cannot find reset values for device %s",
						deviceID));

			JsonObject billingResetStatus = billingResetList.get(0).getAsJsonObject();

			device.setBillingResetStatus(billingResetStatus.get("MDI_EOB").getAsString());
			device.setSerialNumber(deviceID);
			device.setName(deviceID);

			return device;
		});

		return request;
	}

	@Override
	public ResponseType getResponseType() {
		return ResponseType.SET_MAXIMUM_DEMAND;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

}
