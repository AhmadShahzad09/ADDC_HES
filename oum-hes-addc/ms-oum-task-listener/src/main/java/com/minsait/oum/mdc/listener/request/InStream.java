package com.minsait.oum.mdc.listener.request;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface InStream {

	String INPUT_REQUEST = "request";

	@Input(INPUT_REQUEST)
	SubscribableChannel messageInRequest();
}
