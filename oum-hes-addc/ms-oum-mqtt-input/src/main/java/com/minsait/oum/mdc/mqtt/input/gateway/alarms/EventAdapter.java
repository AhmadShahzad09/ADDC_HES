package com.minsait.oum.mdc.mqtt.input.gateway.alarms;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;


@Service
public class EventAdapter extends AbstractMessageAdapter {
	
	@Autowired
	private MdmRestClient _mdmRestClient;

	@Override
	public ResponseType getResponseType() {
		return ResponseType.EVENTS_AND_ALARMS;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		Device device = new Device();
		
		try {
			String eqCode = _mdmRestClient.getEqCodeBySerialNumber(jsonRequest.get("gWsn").getAsString());
			jsonRequest.addProperty("gWsn", eqCode);
		} catch (Exception e) {
			e.getMessage();
			jsonRequest.addProperty("status", RequestStatus.ERROR.name());
			jsonRequest.addProperty("reason", "serialNumber not found");
		}
		if (!_mdmRestClient.isVerticalDevice(jsonRequest.get("gWsn").getAsString())) {
			request.setStatus(RequestStatus.ERROR);
			request.setErrorMessage("Device " + jsonRequest.get("gWsn").getAsString()
					+ " not found: probably is managed by another MQTT microservice instance");
			throw new MessageAdapterException("Device " + jsonRequest.get("gWsn").getAsString()
					+ " not found: probably is managed by another MQTT microservice instance");
		}
		device.setSerialNumber(jsonRequest.get("gWsn").getAsString());
		device.setName(jsonRequest.get("gWsn").getAsString());
		device.setIp(jsonRequest.get("gWip").getAsString());
		JsonArray eventsArray=jsonRequest.get("events").getAsJsonArray();
		for(JsonElement element:eventsArray){
			JsonObject jsonObject = element.getAsJsonObject();
			EventMQTT event = new EventMQTT();
			event.setEventCode(jsonObject.get("eventCode").getAsString());
			event.setTime(jsonObject.get("timestamp").getAsLong());
			if (jsonObject.get("description") != null) {
				event.setDescription(jsonObject.get("description").getAsString());
			}
			device.getEvents().add(event);
			device.getMeasures().add(mapTo(event));
		}
		request.getDevices().add(device);
		return request;
	}


	private Measure mapTo(final EventMQTT event) {
		Measure result = new Measure();
		List<Measure.Block> blocks = new ArrayList<Measure.Block>();
		Measure.Block block = new Measure.Block();
		block.setTimestamp(event.getTime());
		block.setEventCode(event.getEventCode());
		block.setDescription(event.getDescription());
		blocks.add(block);
		result.getBlocks().addAll(blocks);

		return result;
	}


}

