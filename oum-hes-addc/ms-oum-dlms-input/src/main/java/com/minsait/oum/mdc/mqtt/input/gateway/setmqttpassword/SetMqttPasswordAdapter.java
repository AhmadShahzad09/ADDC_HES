package com.minsait.oum.mdc.mqtt.input.gateway.setmqttpassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

@Service
public class SetMqttPasswordAdapter extends AbstractMessageAdapter {
	
	@Autowired
	private MdmRestClient _mdmRestClient;

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GATEWAY_SET_MQTT_PASSWORD;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {
    	try {
 			String eqCode = _mdmRestClient.getEqCodeBySerialNumber(requestJson.get("serialNumber").getAsString());
 			requestJson.addProperty("serialNumber", eqCode);
 		} catch (Exception e) {
 			e.getMessage();
 			requestJson.addProperty("status", RequestStatus.ERROR.name());
 			requestJson.addProperty("reason", "serialNumber not found");
 		}
        if (!_mdmRestClient.isVerticalDevice(requestJson.get("serialnumber").getAsString())) {
            request.setStatus(RequestStatus.ERROR);
            request.setErrorMessage("Device " + requestJson.get("serialnumber").getAsString()
                    + " not found: probably is managed by another MQTT microservice instance");
            throw new MessageAdapterException("Device " + requestJson.get("serialnumber").getAsString()
                    + " not found: probably is managed by another MQTT microservice instance");
        }
    	
        request.setSerialnumber(requestJson.get("serialNumber").getAsString());
        request.setGwPasswordStatus(requestJson.get("status").getAsString());
        return request;
    }
}
