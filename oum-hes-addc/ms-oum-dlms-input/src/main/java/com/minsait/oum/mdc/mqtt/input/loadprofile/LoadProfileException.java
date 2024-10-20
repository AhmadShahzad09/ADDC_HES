package com.minsait.oum.mdc.mqtt.input.loadprofile;

import com.minsait.oum.mdc.domain.Request;

public class LoadProfileException extends Exception {

	private Request request;


	public LoadProfileException(String message, Request request, final Throwable throwable) {

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
