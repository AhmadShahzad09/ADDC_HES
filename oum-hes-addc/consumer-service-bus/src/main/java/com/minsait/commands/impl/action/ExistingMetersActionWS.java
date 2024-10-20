package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("ExistingMetersActionWS")
public class ExistingMetersActionWS extends AbstractAction {

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmExistingMeters();
	}

}
