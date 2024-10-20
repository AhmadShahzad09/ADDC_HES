package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat;

//import com.minsait.common.enu.ProfileEntryCommon;

import lombok.Data;

@Data
public class HeartBeatMDM /*extends ProfileEntryCommon*/ {
	private String date;
	private String meterCode;
}
