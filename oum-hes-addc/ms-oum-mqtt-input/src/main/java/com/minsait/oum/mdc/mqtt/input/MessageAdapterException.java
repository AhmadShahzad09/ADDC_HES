package com.minsait.oum.mdc.mqtt.input;

@SuppressWarnings("serial")
public class MessageAdapterException extends Exception {

	public MessageAdapterException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageAdapterException(String message) {
		super(message);
	}

	public MessageAdapterException(Throwable cause) {
		super(cause);
	}

}
