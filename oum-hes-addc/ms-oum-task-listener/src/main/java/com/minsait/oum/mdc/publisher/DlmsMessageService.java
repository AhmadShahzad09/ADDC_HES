package com.minsait.oum.mdc.publisher;

import java.util.Random;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.minsait.oum.mdc.driver.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DlmsMessageService {

	private final DlmsMessageStream messageStreams;

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private static final String[] data = generatePartions(2);// new String[] { "dlmsp1", "dlmsp2", "dlmsp3", "dlmsp4",
																// "dlmsp5", "dlmsp6", "dlmsp7", "dlmsp8"};

	public DlmsMessageService(final DlmsMessageStream messageStreams) {
		this.messageStreams = messageStreams;
	}

	private static String[] generatePartions(int maxPartitions) {
		String[] partitions = new String[maxPartitions];
		String prefix = "dlmsp";
		for (int i = 0; i < maxPartitions; i++) {
			String partition = prefix + i;
			partitions[i] = partition;
		}
		return partitions;
	}

	public void send(final Message message) {
		log.debug("send->Sending message to kafka {}", message);

		String value = data[RANDOM.nextInt(data.length)];
		log.info("[PARTION] -> {}", value);

		final MessageChannel messageChannel = messageStreams.messageOut();
		MessageBuilder<Message> header = MessageBuilder.withPayload(message)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
				.setHeader("partitionKey", value);
		messageChannel.send(header.build());
	}
}
