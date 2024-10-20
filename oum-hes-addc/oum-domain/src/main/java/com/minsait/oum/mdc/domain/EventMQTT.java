package com.minsait.oum.mdc.domain;

import lombok.Data;

@Data
public class EventMQTT {

	private String eventCode;
	private Long time;
	private String device;
	private String description;

}
