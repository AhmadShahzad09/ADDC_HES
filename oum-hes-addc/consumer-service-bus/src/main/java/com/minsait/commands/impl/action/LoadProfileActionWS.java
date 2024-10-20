package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("LoadProfileActionWS")
public class LoadProfileActionWS extends AbstractAction {

	// @Override
	// public void handleExecute(CommandOutput outputResult) {
	// sendMessage((LPMeasureT) outputResult.getResult());
	// }

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmMeasure();
	}

}
