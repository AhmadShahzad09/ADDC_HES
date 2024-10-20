package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("EventActionWS")
public class EventActionWS extends AbstractAction {

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmMeterEvent();
	}

}
