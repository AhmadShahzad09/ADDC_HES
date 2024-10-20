package com.minsait.oum.mdc.mqtt.input.clocktime;

import com.minsait.oum.mdc.domain.Device;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class ReadRealTimeClockAdapter extends AbstractMessageAdapter {

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss");
	private static final ZoneId zoneId = ZoneId.of("UTC");

	@Override
	public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

		parseDevices(request, jsonRequest, "device", jsonElement -> {
			Device device = new Device();
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			device.setSerialNumber(jsonObject.get("device").getAsString());
//			long period = Long.valueOf(jsonObject.get("clock").getAsLong()) * 1000;
//			LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(period), zoneId);
//			device.setClock(localDateTime.format(dateTimeFormatter));
			device.setClock(jsonObject.get("clock").getAsLong());

			return device;
		});

		return request;
	}

	@Override
	public ResponseType getResponseType() {
		return ResponseType.GET_REAL_TIME_CLOCK;
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.SCHEDULED;
	}

}
