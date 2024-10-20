package com.minsait.commands.impl.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.minsait.commands.CommandOutput;
import com.minsait.commands.config.MessageStream;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service
public abstract class AbstractAction {

	@Autowired(required = false)
	private MessageStream messageStream;

	protected Map<Integer, String> error;

	protected Map<Integer, String> debug;

	public void executeActions(CommandOutput outputResult) {

		log.info("Executing action: {}", outputResult);

		log.info("Information to be sent: {}", outputResult);

		log.info("Sending requested information...");

		// handleExecute(outputResult);
		sendMessage(outputResult.getResult());

		log.info("Information has been sent!!");
	}

	// public abstract void handleExecute(CommandOutput outputResult);
	public abstract MessageChannel getMessageChannel();

	protected MessageStream messageStream() {
		if (messageStream == null)
			throw new UnsupportedOperationException(
					"Message Stream is not available. Check that property 'mdm.etisalat.platform.useBusProducers' is true or is not present on your propoerties to enable MessageStreams");

		return messageStream;
	}

	protected <T> void sendMessage(T data) {

		log.info("\nSending {} through channel {}", data, getMessageChannel());
		getMessageChannel().send(MessageBuilder.withPayload(data).build());
	}

}
