package com.minsait.oum.mdc.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.minsait.common.enu.ProfileCommon;

import lombok.Data;

@Data
public class EventProfile extends ProfileCommon {

	public enum DeviceType {
		METER, GATEWAY
	}

	private List<Event> meterCollector;

	private String deviceId;

	@SerializedName(value = "deviceType", alternate = { "deviceTYpe" })
	private String deviceTYpe;

}
