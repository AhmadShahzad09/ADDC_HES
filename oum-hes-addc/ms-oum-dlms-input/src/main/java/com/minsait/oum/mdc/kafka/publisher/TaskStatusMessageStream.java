package com.minsait.oum.mdc.kafka.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface TaskStatusMessageStream {

	@Output("task-status-out")
	MessageChannel messageOut();

}
