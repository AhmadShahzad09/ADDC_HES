package com.minsait.oum.mdc.stream.heartbeat.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface HeartbeatStream {

	public static String HEARTBEAT_IN = "heartbeat-in";

	@Input(HEARTBEAT_IN)
	public SubscribableChannel getHeartbeatInboundChannel();

}
