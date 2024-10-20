package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat;

import lombok.Data;

@Data
public class ChangeStatusVO {
	
	private String serialNumber;
	
	private String status;

}
