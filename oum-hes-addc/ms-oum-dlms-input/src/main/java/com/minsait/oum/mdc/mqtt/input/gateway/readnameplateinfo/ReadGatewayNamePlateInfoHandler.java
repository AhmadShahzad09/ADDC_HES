package com.minsait.oum.mdc.mqtt.input.gateway.readnameplateinfo;

import com.google.gson.GsonBuilder;
import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class ReadGatewayNamePlateInfoHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private ReadGatewayNamePlateInfoAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {
		log.debug(ToStringBuilder.reflectionToString(message));
    	
        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "GatewayInfo", 
                        		new GsonBuilder().create().toJson(device.getGatewayInfo()))));
    }
}
