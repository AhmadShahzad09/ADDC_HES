package com.minsait.oum.mdc.mqtt.input.alarms;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventsAdapter extends AbstractMessageAdapter {

	@Autowired
	private Gson gson;

	@Override
	public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {
		Request result = this.convertToRequest(requestJson);
		result.setResponseType(ResponseType.ALARMS);

		result.setRequestType(RequestType.SCHEDULED);
		result.setStatus(RequestStatus.OK);
		result.setPayload(requestJson.toString());

		return (result);
	}

	@Override
	public ResponseType getResponseType() {
		return ResponseType.ALARMS;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	private Request convertToRequest(JsonObject jsonRequest) {

		Request result = new Request();

		String uuidRequest = jsonRequest.get("idRequest").getAsString();
		result.setIdRequest(uuidRequest);

		try {
			result.setTime(jsonRequest.get("time").getAsLong());
		} catch (Exception e) {

		}
		JsonArray events = jsonRequest.get("events").getAsJsonArray();

		Map<Integer, Device> mapDevices = new HashMap<Integer, Device>();
		for (JsonElement actual : events) {
			String deviceName = actual.getAsJsonObject().get("device").getAsString();
			Device device = mapDevices.get(deviceName.hashCode());
			if (device != null) {
				EventMQTT eventType = new EventMQTT();
				eventType.setEventCode(actual.getAsJsonObject().get("eventCode").getAsString());
				eventType.setTime(jsonRequest.get("time").getAsLong());
				device.getEvents().add(eventType);
				device.getMeasures().add(mapTo(eventType));
			} else {
				Device newDevice = new Device();
				newDevice.setName(deviceName);
				newDevice.setSerialNumber(deviceName);
				EventMQTT eventType = new EventMQTT();
				eventType.setEventCode(actual.getAsJsonObject().get("eventCode").getAsString());
				eventType.setTime(jsonRequest.get("time").getAsLong());
				newDevice.getEvents().add(eventType);
				newDevice.getMeasures().add(mapTo(eventType));
				mapDevices.putIfAbsent(deviceName.hashCode(), newDevice);
			}
		}
		mapDevices.forEach((k, device) -> {
			result.getDevices().add(device);
		});
		return result;
	}

	private Measure mapTo(final EventMQTT event) {
		Measure result = new Measure();
		List<Measure.Block> blocks = new ArrayList<Measure.Block>();
		Measure.Block block = new Measure.Block();
		block.setDevice(event.getDevice());
		block.setTimestamp(event.getTime());
		block.setEventCode(event.getEventCode());
		blocks.add(block);
		result.getBlocks().addAll(blocks);

		return result;
	}
}

