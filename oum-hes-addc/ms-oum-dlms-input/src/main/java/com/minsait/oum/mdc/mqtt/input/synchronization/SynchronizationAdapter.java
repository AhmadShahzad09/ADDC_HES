package com.minsait.oum.mdc.mqtt.input.synchronization;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

@Service
public class SynchronizationAdapter extends AbstractMessageAdapter {

	@Override
    public ResponseType getResponseType() {
        return ResponseType.SYNCHRONIZATION;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {
    	Device device = new Device();
    	device.setSerialNumber(jsonRequest.get("device").getAsString());
    	device.setName(jsonRequest.get("device").getAsString());
		request.setClock(jsonRequest.get("clock").getAsLong());
		request.getDevices().add(device);
		return request;
    }
}
