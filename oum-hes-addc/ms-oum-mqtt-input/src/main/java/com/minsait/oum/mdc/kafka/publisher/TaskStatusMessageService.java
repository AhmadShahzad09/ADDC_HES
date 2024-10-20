package com.minsait.oum.mdc.kafka.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.minsait.oum.mdc.listener.task.PlatformTaskVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskStatusMessageService {

	@Autowired
	private TaskStatusMessageStream messageStreams;

//	public TaskStatusMessageService(final TaskStatusMessageStream messageStreams) {
//		this.messageStreams = messageStreams;
//	}

	public void send(final PlatformTaskVO platformTaskVO) {
		log.debug("send->Sending message to kafka {}", platformTaskVO);

		final MessageChannel messageChannel = messageStreams.messageOut();
		MessageBuilder<PlatformTaskVO> header = MessageBuilder.withPayload(platformTaskVO)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		messageChannel.send(header.build());
	}
}
