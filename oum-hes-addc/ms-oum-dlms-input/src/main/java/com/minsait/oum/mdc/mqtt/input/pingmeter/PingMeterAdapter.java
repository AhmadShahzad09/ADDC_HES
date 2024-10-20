package com.minsait.oum.mdc.mqtt.input.pingmeter;

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
public class PingMeterAdapter extends AbstractMessageAdapter {
    @Override
    public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {
        this.parseDevices(request, requestJson, "devices", jsonElement -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            Device device = new Device();
            device.setSerialNumber(obj.get("serialNumber").getAsString());
            device.setClock(obj.get("clock").getAsLong());
            return device;
        });
        return request;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.METER_PING;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }
}
