package com.minsait.oum.mdc.mqtt.input.gateway.ping;

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
public class PingGatewayAdapter extends AbstractMessageAdapter {
    @Override
    public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {
        request.setSerialnumber(requestJson.get("serialnumber").getAsString());
        request.setResult(requestJson.get("rssi").getAsString());
        return request;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GATEWAY_PING;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }
}
