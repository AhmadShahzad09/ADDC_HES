package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("EnergyProfileAction")
public class EnergyProfileActionWS extends AbstractAction {

	// @Override
	// public void handleExecute(CommandOutput outputResult) {
	// sendMessage((EnergyProfile) outputResult.getResult());
	// }

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmRegister();
	}
}
