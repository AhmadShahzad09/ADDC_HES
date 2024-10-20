package com.minsait.oum.mdc.mqtt.input.getmeteringmode;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

@Slf4j
public class GetMeteringModeHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private GetMeteringModeAdapter adapter;
	

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {

			this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
					(request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
							TelemetryRemoteConfigurationVO.from(device, "metering_mode", device.getMeteringMode())));


	}
}
