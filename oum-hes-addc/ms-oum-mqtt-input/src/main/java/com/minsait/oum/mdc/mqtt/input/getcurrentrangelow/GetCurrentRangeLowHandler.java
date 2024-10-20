package com.minsait.oum.mdc.mqtt.input.getcurrentrangelow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetCurrentRangeLowHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private GetCurrentRangeLowAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {

		this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
				(request, call, device) -> this.getMdmRestClient()
						.setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "CurrentThresholdLow",
								device.getCurrentThresholdLow())));

	}

}
