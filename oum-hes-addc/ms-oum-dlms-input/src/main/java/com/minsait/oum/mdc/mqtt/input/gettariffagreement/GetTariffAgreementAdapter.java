package com.minsait.oum.mdc.mqtt.input.gettariffagreement;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import org.springframework.stereotype.Service;

@Service
public class GetTariffAgreementAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.GET_TARIFF_AGREEMENT;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		parseDevices(request, jsonRequest, "devices",
				jsonElement -> {
					Device device = new Device();
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					device.setSerialNumber(jsonObject.get("serialNumber").getAsString());

					if(jsonObject.get("activeCalenderName") != null)
						device.setActiveCalenderName(jsonObject.get("activeCalenderName").getAsString());

					if(jsonObject.get("currentActiveTariff") != null)
						device.setCurrentActiveTariff(jsonObject.get("currentActiveTariff").getAsString());

					return device;
				}
		);

		return request;
	}
}
