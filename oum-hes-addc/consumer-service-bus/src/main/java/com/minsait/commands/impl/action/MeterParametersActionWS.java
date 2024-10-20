package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("MeterParametersActionWS")
public class MeterParametersActionWS extends AbstractAction {

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmMeterParameter();
	}

}
