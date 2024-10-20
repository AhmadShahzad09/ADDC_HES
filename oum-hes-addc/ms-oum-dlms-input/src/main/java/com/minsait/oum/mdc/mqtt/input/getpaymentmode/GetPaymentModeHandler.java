package com.minsait.oum.mdc.mqtt.input.getpaymentmode;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class GetPaymentModeHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private GetPaymentModeAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
                        TelemetryRemoteConfigurationVO.from(device, "PaymentMode", device.getPaymentMode())));

    }
}
