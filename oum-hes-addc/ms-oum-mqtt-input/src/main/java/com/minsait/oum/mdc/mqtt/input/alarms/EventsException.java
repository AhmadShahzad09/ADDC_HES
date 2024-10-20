package com.minsait.oum.mdc.mqtt.input.alarms;

import com.minsait.oum.mdc.domain.Request;

public class EventsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Request request;


	public EventsException(String message, Request request, final Throwable throwable) {

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
