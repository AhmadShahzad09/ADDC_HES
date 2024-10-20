package com.minsait.oum.mdc.kafka.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.minsait.oum.mdc.domain.Request;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(HeartbeatMessageStream.class)
@Service
public class HeartbeatMessageService {

	@Autowired
	private HeartbeatMessageStream messageStream;

	public void send(final Request request) {
		log.debug("sending heartbeat request to kafka topic: {}", request);

		messageStream.toKafkaChannel().send(MessageBuilder.withPayload(request)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

	}
}
