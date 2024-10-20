package com.minsait.oum.mdc.listener.task;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface TaskStreams {
	String INPUT = "task-status-in";

	@Input(INPUT)
	SubscribableChannel inputTaskStatus();
}