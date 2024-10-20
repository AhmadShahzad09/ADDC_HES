package com.minsait.oum.mdc.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface DlmsMessageStream {

	@Output("dlms-out")
	MessageChannel messageOut();

}
