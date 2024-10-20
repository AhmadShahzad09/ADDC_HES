package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("AbsoluteLoadProfileActionWS")
public class AbsoluteLoadProfileActionWS extends AbstractAction {

	// @Override
	// public void handleExecute(CommandOutput outputResult) {
	// sendMessage((LPMeasureT) outputResult.getResult());
	// }

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmReading();
	}

}
