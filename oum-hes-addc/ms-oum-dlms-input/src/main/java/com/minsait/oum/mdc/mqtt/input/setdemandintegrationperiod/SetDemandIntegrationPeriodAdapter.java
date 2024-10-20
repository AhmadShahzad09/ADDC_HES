package com.minsait.oum.mdc.mqtt.input.setdemandintegrationperiod;

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
public class SetDemandIntegrationPeriodAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SET_DEMAND_INTEGRATION_PERIOD;
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
