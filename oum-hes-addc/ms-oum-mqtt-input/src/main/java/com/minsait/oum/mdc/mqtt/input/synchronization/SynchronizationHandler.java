package com.minsait.oum.mdc.mqtt.input.synchronization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import com.minsait.oum.mdc.mqtt.input.gateway.settimesync.SetGatewayTimeSyncAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SynchronizationHandler extends AbstractMessageHandlerProcessor {
	 @Autowired
	    private SynchronizationAdapter adapter;

	    @Override
	    public void handleMessage(Message<?> message) {
	        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
	                (request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
	                        TelemetryRemoteConfigurationVO.from(device, "Fh-m", request.getClock().toString())));
	    }
}
