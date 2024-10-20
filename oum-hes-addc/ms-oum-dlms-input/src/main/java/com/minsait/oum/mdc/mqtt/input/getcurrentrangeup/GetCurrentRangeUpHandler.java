package com.minsait.oum.mdc.mqtt.input.getcurrentrangeup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetCurrentRangeUpHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private GetCurrentRangeUpAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {

		this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
				(request, call, device) -> this.getMdmRestClient()
						.setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "CurrentThresholdUp",
								device.getCurrentThresholdUp())));

	}

}
