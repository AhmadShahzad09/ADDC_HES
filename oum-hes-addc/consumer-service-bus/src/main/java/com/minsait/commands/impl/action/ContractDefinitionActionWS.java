package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("ContractDefinitionActionWS")
public class ContractDefinitionActionWS extends AbstractAction {

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmContactedPower();
	}

}
