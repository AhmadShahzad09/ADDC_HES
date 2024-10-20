package com.minsait.oum.mdc.kafka.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface HeartbeatMessageStream {

	@Output("heartbeat-out")
	public MessageChannel toKafkaChannel();
}
