package com.minsait.oum.mdc.mqtt.input.setvoltrangeup;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import org.springframework.stereotype.Service;

@Service
public class SetVoltRangeUpAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SET_VOLT_RANGE_UP;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

        parseDevices(request, jsonRequest, "devices", jsonElement -> {
            Device device = new Device();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            device.setSerialNumber(jsonObject.get("serialNumber").getAsString());
            device.setMeterStatus(jsonObject.get("status").getAsString());
            return device;
        });
        return request;
    }
}
