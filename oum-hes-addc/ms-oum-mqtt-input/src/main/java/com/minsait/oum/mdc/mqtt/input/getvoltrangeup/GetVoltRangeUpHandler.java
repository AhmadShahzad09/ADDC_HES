package com.minsait.oum.mdc.mqtt.input.getvoltrangeup;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class GetVoltRangeUpHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private GetVoltRangeUpAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {

		this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
				(request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
						TelemetryRemoteConfigurationVO.from(device, "Uswell", device.getThreshold()+"")));
	}
}
