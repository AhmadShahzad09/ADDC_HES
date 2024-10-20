package com.minsait.oum.mdc.coap.commands.bean;

import lombok.Data;

import java.util.List;

@Data
public class OnDemandCommandVO {

	private String idRequest;

	private Long time;

	private List<String> devices;

	private OnDemandParameterVO parameters;

	private Long date;

	private String type;

	private List<SchedulerConfig> schedulerConfig;

	private String serialnumber;

	private String gWsn;// gateway Serial number;

	private String gWip; // gateway IP

	private String gUti; // Utility code

	private String paymentMode;

	private Long operationTime;

	private String operation;

	private String Threshold;

	private String limitThreshold;

	private String threshDuration;

	private Long demandIntPeriod;

	private List<String> groups;

	private String meteringMode;

	private String cronB1;

	private String cronB2;

	private String mode;

	private List<FirmWare> firmWare;

	private String secret;

	private String name;

	private String calendarName;

	private String activateCalendarTime;

	private List<String> profile;

	private String billingPeriod;

	private List<MeterConfig> meterList;

	private String filePath;

	private List<SeasonProfile> seasonProfile;

	private List<WeekProfile> weekProfile;

	private List<DayProfile> dayProfile;

	private List<String> EnergyProfile;

	private List<String> Instantaneous;
}
