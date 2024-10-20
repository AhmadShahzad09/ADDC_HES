package com.minsait.oum.mdc.mqtt.input.clocktime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadRealTimeClockHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private ReadRealTimeClockAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {

		this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
				(request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
						TelemetryRemoteConfigurationVO.from(device, "Fh-m", device.getClock().toString())));

	}

}
