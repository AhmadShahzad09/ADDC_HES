package com.minsait.oum.mdc.mqtt.input.loadprofile;

import com.minsait.oum.mdc.domain.Device;

public class DeviceException extends Exception {

	private Device device;

	private static final long serialVersionUID = 6201539795511411857L;

	public DeviceException(String message, Device device, final Throwable throwable) {

		super(message, throwable);

		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

}
