package com.minsait.oum.mdc.mqtt.input.changemeterpassword;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnDemandChangeMeterPasswordHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private OnDemandChangeMeterPasswordAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {
		log.debug(ToStringBuilder.reflectionToString(message));

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> {
                    this.getMdmRestClient().setTelemetryRemoteConfig(
                            TelemetryRemoteConfigurationVO.from(device, "MeterPasswordStatus", device.getStatusCode()));
                    this.getMdmRestClient().confirmKeysDeployment(device.getSerialNumber());
                });
	}

}
