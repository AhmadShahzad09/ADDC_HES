package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat;

import lombok.Data;

@Data
public class ChangeIPVO {
	
	private String serialNumber;
	
	private String ip;
	
	private String port;

}
