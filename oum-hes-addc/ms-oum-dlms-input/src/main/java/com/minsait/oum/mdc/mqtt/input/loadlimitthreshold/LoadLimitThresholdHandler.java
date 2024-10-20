package com.minsait.oum.mdc.mqtt.input.loadlimitthreshold;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class LoadLimitThresholdHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private LoadLimitThresholdAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "ThresholdActive", device.getThresholdAct())));

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "ThresholdDuration", device.getThreshDuration())));

    }

}
