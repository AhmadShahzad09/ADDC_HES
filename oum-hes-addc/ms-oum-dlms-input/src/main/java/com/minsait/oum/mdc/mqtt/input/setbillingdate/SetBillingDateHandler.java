package com.minsait.oum.mdc.mqtt.input.setbillingdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetBillingDateHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private SetBillingDateAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {

		// no need to set billing date status response to mdm
		this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()), MdmNotifier.empty);

	}

}
