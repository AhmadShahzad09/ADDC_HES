package com.minsait.oum.mdc.kafka.listener;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MessageStreamDlms {

	public static String DLMS = "dlms-in";

	@Input(DLMS)
	SubscribableChannel dlmsMessageIn();

}
