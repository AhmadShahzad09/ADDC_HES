package com.minsait.oum.mdc.mqtt.input.loadprofile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoadProfileAdapter extends AbstractMessageAdapter {

	@Autowired
	private Gson gson;

	@Override
	public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {

		this.parseDevices(request, requestJson, "meterReadingList", deviceJsonElement -> {

			Device device = this.getDevice(deviceJsonElement);
			device.setMeasures(
					this.getMeasures(deviceJsonElement.getAsJsonObject().get("intervalBlocks").getAsJsonArray()));

			return device;
		});

		return request;
	}

	@Override
	public ResponseType getResponseType() {
		return ResponseType.LOAD_PROFILE;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	private Device getDevice(JsonElement jsonDevice) throws MessageAdapterException {

		Device result = new Device();

		JsonObject jsonObject = gson.fromJson(jsonDevice.toString(), JsonObject.class);

		result.setModel(jsonObject.get("modelname").getAsString());
		try {
			result.setOwner(jsonObject.get("owner").getAsString());
		} catch (Exception e) {

		}
		if (jsonObject.get("devicename").getAsString() == null
				|| jsonObject.get("devicename").getAsString().length() == 0) {
			result.setName(jsonObject.get("serialNumber").getAsString());
			result.setSerialNumber(jsonObject.get("serialNumber").getAsString());
		} else {
			result.setName(jsonObject.get("devicename").getAsString());
			result.setSerialNumber(jsonObject.get("devicename").getAsString());
		}
		result.setVersion(jsonObject.get("version").getAsString());
		result.setIp(jsonObject.get("ipaddr").getAsString());
		JsonElement deviceError = jsonObject.get("deviceErrorMessage");
		if (deviceError != null) {
			result.setErrorMessage(deviceError.getAsString());
		}
		JsonElement maintenanceCode = jsonObject.get("maintenanceMode");
		if (maintenanceCode != null) {
			result.setInMaintenanceMode(maintenanceCode.getAsBoolean());
		}
		MediumType medium = MediumTypeConverter.convert(jsonObject.get("medium").getAsString());
		result.setMedium(medium);
		result.setProfileCode(jsonObject.get("profileCode").getAsString());
		try {
			IntervalType interval = IntervalTypeConverter.convert(jsonObject.get("interval").getAsString());
			result.setInterval(interval);
		} catch (Exception ex) {
			throw new MessageAdapterException(
					String.format("Time interval NULL for device %s", result.getSerialNumber()), ex);
		}

		return result;
	}

	private List<Measure> getMeasures(JsonArray jsonMeasures) {

		List<Measure> result = new ArrayList<>();

		for (JsonElement jsonMeasure : jsonMeasures) {

			Measure measure = this.getMeasure(jsonMeasure);
			result.add(measure);
		}

		return result;
	}

	private Measure getMeasure(JsonElement jsonMeasure) {

		Measure result = new Measure();
		JsonObject jsonObject = gson.fromJson(jsonMeasure.toString(), JsonObject.class);
		List<Measure.Block> blocks = new ArrayList<Measure.Block>();
		result.setProfileStatus(jsonObject.get("profileStatus").getAsString());
		if (jsonObject.get("captureTime") != null)
			result.setCaptureTime(jsonObject.get("captureTime").getAsString());

		JsonArray jsonBlocks = jsonObject.get("blocks").getAsJsonArray();
		for (JsonElement jsonBlock : jsonBlocks) {
			Measure.Block block = new Measure.Block();
			JsonObject jsonBlockObject = gson.fromJson(jsonBlock.toString(), JsonObject.class);

			block.setTimestamp(jsonBlockObject.get("time").getAsLong());
			block.setValue(jsonBlockObject.get("value").getAsDouble());
			String channel = jsonBlockObject.get("channel").getAsString();
			if ("IR".equals(channel)) {
				block.setChannel("Q1");
			} else if ("ER".equals(channel)) {
				block.setChannel("Q4");
			} else {
				block.setChannel(channel);
			}
			block.setMagnitude(jsonBlockObject.get("unitM").getAsString());
			block.setCode(jsonBlockObject.get("code").getAsString());
			blocks.add(block);
		}
		result.getBlocks().addAll(blocks);

		return result;
	}

}
