package com.minsait.oum.mdc.mqtt.input.gateway.setmqttpassword;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

public class SetMqttPasswordHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private SetMqttPasswordAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> {
                    this.getMdmRestClient().setTelemetryRemoteConfig(
                            TelemetryRemoteConfigurationVO.from(device, "GatewayPasswordStatus", request.getGwPasswordStatus()));
                    this.getMdmRestClient().confirmKeysDeployment(device.getSerialNumber());
                });
    }
}
