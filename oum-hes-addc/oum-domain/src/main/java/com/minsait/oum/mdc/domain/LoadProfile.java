package com.minsait.oum.mdc.domain;

import lombok.Data;

@Data
public class LoadProfile {

	private Measure measure;

	private Device device;

	private Request request;

	public LoadProfile() {

	}

	public LoadProfile(Measure measure, Device device, Request request) {
		this.measure = measure;
		this.device = device;
		this.request = request;
	}

}
