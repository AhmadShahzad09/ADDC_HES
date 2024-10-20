package com.minsait.oum.mdc.stream.heartbeat.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.stream.heartbeat.service.HeartbeatService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HeartbeatStreamListener {

	@Autowired
	private HeartbeatService _service;
	
	@StreamListener(HeartbeatStream.HEARTBEAT_IN)
	public void consumeHeartbeat(@Payload final Request heartbeatRequest) {
		Thread thread = new Thread() {
			public void run() {
				log.info("new heartbeat message received with request {}", heartbeatRequest);
				_service.handle(heartbeatRequest);
			}
		};

		thread.start();

	}
}
