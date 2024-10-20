package com.minsait.oum.mdc.mqtt.input.getmeterstatus;

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
public class GetMeterStatusAdapter extends AbstractMessageAdapter {
    @Override
    public ResponseType getResponseType() {
        return ResponseType.GET_METER_STATUS;
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
            device.setBatteryLevel(jsonObject.get("batteryLevel").getAsString());
            device.setDirection_P(jsonObject.get("direction_P").getAsString());
            device.setDirection_Q(jsonObject.get("direction_Q").getAsString());
            device.setActiveRate(jsonObject.get("activeRate").getAsString());
            device.setRelayStatus(jsonObject.get("relayStatus").getAsString());
            return device;
        });
        return request;
    }
}
