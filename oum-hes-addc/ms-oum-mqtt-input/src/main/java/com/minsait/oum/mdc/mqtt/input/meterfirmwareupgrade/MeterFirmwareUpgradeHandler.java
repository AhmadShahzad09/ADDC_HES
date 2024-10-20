package com.minsait.oum.mdc.mqtt.input.meterfirmwareupgrade;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class MeterFirmwareUpgradeHandler extends AbstractMessageHandlerProcessor {
    @Autowired
    private MeterFirmwareUpgradeAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {
        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "Status", device.getImageTransferStatus())));
    }
}
