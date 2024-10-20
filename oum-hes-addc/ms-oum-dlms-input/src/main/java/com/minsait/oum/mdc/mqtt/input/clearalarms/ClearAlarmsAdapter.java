package com.minsait.oum.mdc.mqtt.input.clearalarms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.minsait.oum.mdc.mqtt.input.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;


@Service
public class ClearAlarmsAdapter extends AbstractMessageAdapter {

	@Override
	public ResponseType getResponseType() {
		return ResponseType.CLEAR_ALARMS;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		parseDevices(request, jsonRequest, "alarmsCleared", jsonElement -> {
			Device device = new Device();
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			device.setSerialNumber(jsonObject.get("device").getAsString());
			device.setAlarmRegister(jsonObject.get("alarm_register").getAsString());
			device.setAlarmRegisterError(jsonObject.get("error_register").getAsString());
			return device;
		});

		return request;
	}

}
