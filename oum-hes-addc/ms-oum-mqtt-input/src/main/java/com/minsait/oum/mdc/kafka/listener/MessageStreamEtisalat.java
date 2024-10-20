package com.minsait.oum.mdc.kafka.listener;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MessageStreamEtisalat {

	public static String ETISALAT = "etisalat-in";

	@Input(ETISALAT)
	SubscribableChannel etisalatMessageIn();

}
