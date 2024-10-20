package com.minsait.oum.mdc.mqtt.input.getvoltrangelow;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import org.springframework.stereotype.Service;

@Service
public class GetVoltRangeLowAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GET_VOLT_RANGE_LOW;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

        parseDevices(request, jsonRequest, "devices",
                jsonElement -> {
                    Device device = new Device();
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    device.setSerialNumber(jsonObject.get("serialNumber").getAsString());

                    if(jsonObject.get("threshold") != null)
                        device.setThreshold(jsonObject.get("threshold").getAsInt());

                    return device;
                }
        );

        return request;
    }
}
