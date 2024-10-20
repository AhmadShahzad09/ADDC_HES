package com.minsait.oum.mdc.mqtt.input.gateway.readtimeanddate;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReadTimeAndDateAdapter extends AbstractMessageAdapter {
    @Override
    public ResponseType getResponseType() {
        return ResponseType.GATEWAY_READ_TIME_DATE;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {
        request.setSerialnumber(jsonRequest.get("serialnumber").getAsString());
        request.setClock(jsonRequest.get("clock").getAsLong());
        return request;
    }
}
