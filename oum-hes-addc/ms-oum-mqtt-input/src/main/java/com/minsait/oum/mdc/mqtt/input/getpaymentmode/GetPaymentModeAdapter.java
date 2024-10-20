package com.minsait.oum.mdc.mqtt.input.getpaymentmode;

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
public class GetPaymentModeAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GET_PAYMENT_MODE;
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
            device.setPaymentMode(jsonObject.get("paymentMode").getAsString());
            return device;
        });

        return request;
    }
}
