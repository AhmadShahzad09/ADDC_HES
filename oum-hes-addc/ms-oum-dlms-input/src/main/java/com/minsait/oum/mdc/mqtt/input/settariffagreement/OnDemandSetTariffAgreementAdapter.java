package com.minsait.oum.mdc.mqtt.input.settariffagreement;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import org.springframework.stereotype.Service;

@Service
public class OnDemandSetTariffAgreementAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SET_TARIFF_AGREEMENT;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

        this.parseDevices(request, jsonRequest, "devices", jsonElement -> {
            JsonObject deviceJsonObject = jsonElement.getAsJsonObject();

            Device device = new Device();
            device.setSerialNumber(deviceJsonObject.get("serialNumber").getAsString());
            device.setMeterStatus(deviceJsonObject.get("status").getAsString());

            return device;

        });

        return request;
    }
}
