package com.minsait.oum.mdc.domain;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class HeartBeat {

	String modelName;
	String deviceName;
	String version;
	@SerializedName(value = "macAddress", alternate = { "mac address", "mac_address" })
	String macAddress;
	@SerializedName(value = "serialNumber", alternate = { "serial number", "serial_number" })
	String serialNumber;
	@SerializedName(value = "wwan_ip", alternate = { "wwanIP", "wwan IP" })
	String wwanIP;
	String powerSource;
}
