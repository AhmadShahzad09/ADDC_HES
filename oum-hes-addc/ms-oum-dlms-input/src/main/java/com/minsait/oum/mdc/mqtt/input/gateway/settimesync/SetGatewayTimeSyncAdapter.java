package com.minsait.oum.mdc.mqtt.input.gateway.settimesync;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetGatewayTimeSyncAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GATEWAY_SYNCHRONIZATION;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

    	request.setSerialnumber(jsonRequest.get("serialnumber").getAsString());
    	
    	Long clock = jsonRequest.get("clock").getAsLong();
    	
		request.setClock(clock);
		
		return request;

//        GatewayInfo gatewayInfo = new GatewayInfo();
//        gatewayInfo.setSerialNumber(jsonRequest.get("serialnumber").getAsString());
//        Long clock = jsonRequest.get("date").getAsLong();
//
//        JsonArray deviceAry=jsonRequest.get("device").getAsJsonArray();
//
//        for(JsonElement element:deviceAry) {
//            JsonObject jsonObject = element.getAsJsonObject();
//            Device device = new Device();
//
//            device.setDate(clock);
//            device.setGatewayInfo(gatewayInfo);
//            if (jsonObject.get("device1") != null) {
//	            device.setSerialNumber(jsonObject.get("device1").getAsString());
//	            device.setName(jsonObject.get("device1").getAsString());
//            }
//            if (jsonObject.get("device") != null) {
//	            device.setSerialNumber(jsonObject.get("device").getAsString());
//	            device.setName(jsonObject.get("device").getAsString());
//            }
//
//            request.getDevices().add(device);
//        }
//     return request;
    }
}
