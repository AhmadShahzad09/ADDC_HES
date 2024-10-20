package com.minsait.oum.mdc.domain;

//import com.minsait.common.enu.ProfileEntryCommon;

import lombok.Data;

@Data
public class Event /*extends ProfileEntryCommon*/ {
	private Long eventDateTime;
	private String description;
	private String obiscode;
	private String eventCode;
	private String device;
}
