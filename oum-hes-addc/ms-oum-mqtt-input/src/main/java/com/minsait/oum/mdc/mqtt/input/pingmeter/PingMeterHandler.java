package com.minsait.oum.mdc.mqtt.input.pingmeter;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

@Slf4j
public class PingMeterHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private PingMeterAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "Fh-m", device.getClock().toString())));

    }
}
