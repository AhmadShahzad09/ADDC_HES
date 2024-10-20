package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("SyncronizationActionWS")
public class SyncronizationActionWS extends AbstractAction {

	// @Override
	// public void handleExecute(CommandOutput outputResult) {
	// sendMessage((DateTimeSyncr) outputResult.getResult());
	// }

	@Override
	public MessageChannel getMessageChannel() {
		return messageStream().messageOutMdmSynchronization();
	}

}
