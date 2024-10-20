package com.minsait.oum.mdc.mqtt.input.getmeterstatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetMeterStatusHandler extends AbstractMessageHandlerProcessor {
    @Autowired
    private GetMeterStatusAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {

        // updated Status from MeterStatus to RelayStatus. as meter status is coming in relaystatus in JSON.
        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "Status", device.getRelayStatus())));

    }
}
