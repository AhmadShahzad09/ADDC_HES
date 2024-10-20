package com.minsait.oum.mdc.mqtt.input.gettariffagreement;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class GetTariffAgreementHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private GetTariffAgreementAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {
		log.debug(ToStringBuilder.reflectionToString(message));

		this.processTelemetryCallToTaskListener(
				adapter.convert(message.getPayload().toString()),
				(request, call, device) -> {
					this.getMdmRestClient().setTelemetryRemoteConfig(
							TelemetryRemoteConfigurationVO
								.from(device, "ActiveCalendar", device.getActiveCalenderName()));
					this.getMdmRestClient().setTelemetryRemoteConfig(
							TelemetryRemoteConfigurationVO
								.from(device, "CurrentActiveTariff", device.getCurrentActiveTariff()));
				}
		);
	}
}
