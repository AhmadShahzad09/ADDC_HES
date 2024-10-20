package com.minsait.oum.mdc.mqtt.input.getprofileinterval;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GetProfileIntervalAdapter extends AbstractMessageAdapter {

	@Override
	public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {

		this.parseDevices(request, requestJson, "devices", jsonElement -> {
			JsonObject deviceJson = jsonElement.getAsJsonObject();
			Device device = new Device();
			device.setSerialNumber(deviceJson.get("serialNumber").getAsString());
			device.setName(deviceJson.get("serialNumber").getAsString());
			if (deviceJson.get("LoadProfile1Interval") != null) device.setLoadProfile1Interval(deviceJson.get("LoadProfile1Interval").getAsString());
			if (deviceJson.get("LoadProfile2Interval") != null) device.setLoadProfile2Interval(deviceJson.get("LoadProfile2Interval").getAsString());
			if (deviceJson.get("PowerQualityProfileInterval") != null) device.setPowerQualityProfileInterval(deviceJson.get("PowerQualityProfileInterval").getAsString());
			if (deviceJson.get("InstrumentationProfile") != null) device.setInstrumentationProfile(deviceJson.get("InstrumentationProfile").getAsString());

			return device;

		});

		return request;
	}

	@Override
	public ResponseType getResponseType() {
		return ResponseType.GET_LOAD_PROFILE_CAPTURE_PERIOD;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

}
