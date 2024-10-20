package com.minsait.oum.mdc.mqtt.input.clearalarms;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
@Slf4j
public class ClearAlarmsHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private ClearAlarmsAdapter adapter;
	

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {

		log.error("DEVICES TO CLEAR:", message);
			this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
					(request, call, device) -> this.getMdmRestClient().setTelemetryRemoteConfig(
							TelemetryRemoteConfigurationVO.from(device, "clear_alarm_status", device.getAlarmRegister())));


	}
}
