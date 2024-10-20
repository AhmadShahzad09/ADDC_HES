package com.minsait.oum.mdc.mqtt.input.synchronization;

import com.minsait.oum.mdc.domain.Request;

public class SynchronizationException extends Exception {
	private Request request;

	private static final long serialVersionUID = 6201539795511411857L;

	public SynchronizationException(String message, Request request, final Throwable throwable) {

		super(message, throwable);

		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

}
