package com.minsait.oum.mdc.mqtt.input.getdemandintperiod;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

public class GetDemandIntegrationPeriodHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private GetDemandIntegrationPeriodAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "DemandInterval", device.getDemandInterval()+"")));

    }
}
