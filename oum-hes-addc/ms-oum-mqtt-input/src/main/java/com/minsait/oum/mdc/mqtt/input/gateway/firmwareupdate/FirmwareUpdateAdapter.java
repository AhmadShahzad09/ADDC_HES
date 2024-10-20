package com.minsait.oum.mdc.mqtt.input.gateway.firmwareupdate;

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
public class FirmwareUpdateAdapter extends AbstractMessageAdapter {
    @Override
    public ResponseType getResponseType() {
        return ResponseType.GATEWAY_FIRMWARE_UPDATE;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {
        request.setSerialnumber(jsonRequest.get("serialnumber").getAsString());
        request.setResult(jsonRequest.get("result").getAsString());
        request.setDescription(jsonRequest.get("description").getAsString());
        request.setVersion(jsonRequest.get("version").getAsString());
        return request;
    }
}
