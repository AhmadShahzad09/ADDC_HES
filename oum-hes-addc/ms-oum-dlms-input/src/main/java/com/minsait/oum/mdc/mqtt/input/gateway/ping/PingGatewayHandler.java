package com.minsait.oum.mdc.mqtt.input.gateway.ping;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

@Slf4j
public class PingGatewayHandler extends AbstractMessageHandlerProcessor {
    @Autowired
    private PingGatewayAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
		log.debug(ToStringBuilder.reflectionToString(message));

        this.processTelemetryCallToTaskListener(
        	adapter.convert(message.getPayload().toString()),
            (request, call, device) -> this.getMdmRestClient()
                .setTelemetryRemoteConfig(
                		TelemetryRemoteConfigurationVO.from(device, "rssi", request.getResult())));

    }
}
