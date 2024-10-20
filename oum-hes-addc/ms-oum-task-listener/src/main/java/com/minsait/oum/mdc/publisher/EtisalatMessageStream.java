package com.minsait.oum.mdc.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EtisalatMessageStream {

	@Output("etisalat-out")
	MessageChannel messageOut();

}
