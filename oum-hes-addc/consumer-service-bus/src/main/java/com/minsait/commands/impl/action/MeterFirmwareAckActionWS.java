package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("MeterFirmwareAckActionWS")
public class MeterFirmwareAckActionWS extends AbstractAction {

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmMeterFirmwareAck();
	}

}
