package com.minsait.oum.mdc.mqtt.input.energyregister;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.domain.MediumType;
import com.minsait.oum.mdc.domain.MediumTypeConverter;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

@Service
public class EnergyRegistersAdapter extends AbstractMessageAdapter {

	@Autowired
	private Gson gson;

	@Override
	public ResponseType getResponseType() {
		return ResponseType.ENERGY_PROFILE;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {

		this.parseDevices(request, requestJson, "meterReadingList", deviceJsonElement -> {

			Device device = this.getDevice(deviceJsonElement);
			device.setMeasures(this.getMeasures(deviceJsonElement.getAsJsonObject().get("blocks").getAsJsonArray()));

			return device;
		});

		return request;
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
		if (deviceError != null && !deviceError.getAsString().isEmpty() && !deviceError.getAsString().equals("OK")) {
			result.setErrorMessage(deviceError.getAsString());
		}
		JsonElement maintenanceCode = jsonObject.get("maintenanceMode");
		if (maintenanceCode != null) {
			result.setInMaintenanceMode(maintenanceCode.getAsBoolean());
		}
		MediumType medium = MediumTypeConverter.convert(jsonObject.get("medium").getAsString());
		result.setMedium(medium);
		result.setGroup(jsonObject.get("groups").getAsString());
//		try {
//			IntervalType interval = IntervalTypeConverter.convert(jsonObject.get("interval").getAsString());
//			result.setInterval(interval);
//		} catch (Exception e) {
//			DeviceException dex = new DeviceException("Time interval NULL", result, e);
//			throw dex;
//		}

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

		Measure.Block block = new Measure.Block();
		block.setTimestamp(jsonObject.get("time").getAsLong());
		block.setValue(jsonObject.get("value").getAsDouble());
		String channel = jsonObject.get("channel").getAsString();
		if ("IR".equals(channel)) {
			block.setChannel("Q1");
		} else if ("ER".equals(channel)) {
			block.setChannel("Q4");
		} else {
			block.setChannel(channel);
		}
		block.setMagnitude(AppUtils.getStringValue(jsonObject.get("unitM")));
		block.setCode(AppUtils.getStringValue(jsonObject.get("code")));

		blocks.add(block);
		result.getBlocks().addAll(blocks);

		return result;
	}

}
