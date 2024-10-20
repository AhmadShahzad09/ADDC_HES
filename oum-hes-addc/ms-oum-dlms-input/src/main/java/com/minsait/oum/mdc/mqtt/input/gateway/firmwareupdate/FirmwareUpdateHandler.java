package com.minsait.oum.mdc.mqtt.input.gateway.firmwareupdate;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import com.minsait.oum.mdc.mqtt.input.getmeterstatus.GetMeterStatusAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class FirmwareUpdateHandler extends AbstractMessageHandlerProcessor {
    @Autowired
    private FirmwareUpdateAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {
        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "FirmwareVersion", request.getVersion())));
    }
}
