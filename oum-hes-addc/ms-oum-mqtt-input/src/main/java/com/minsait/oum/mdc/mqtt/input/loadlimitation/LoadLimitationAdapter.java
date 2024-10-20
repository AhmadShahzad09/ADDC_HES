package com.minsait.oum.mdc.mqtt.input.loadlimitation;

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
public class LoadLimitationAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.LOAD_LIMITATION;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {
        this.parseDevices(request, jsonRequest, "devices", jsonElement -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            Device device = new Device();
            device.setSerialNumber(obj.get("serialNumber").getAsString());
            device.setMeterStatus(obj.get("status").getAsString());
            return device;
        });
        return request;
    }
}
