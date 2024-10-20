package com.minsait.oum.mdc.domain;

import com.minsait.oum.mdc.domain.billing.BillingProfileMQTT;
import com.minsait.oum.mdc.domain.reconnection.Reconnection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Device {

	public Device() {
		this.measures = new ArrayList<>();
		this.events = new ArrayList<EventMQTT>();
		this.billing = new ArrayList<BillingProfileMQTT>();
		this.clearedProfiles = new ArrayList<String>();
		this.maxDemands = new ArrayList<MaxDemand>();
		this.instantaneousValues = new ArrayList<InstantaneousValue>();
	}

	private Long date;

	
	// DO NOT DELETE THIS ATTRIBUTE: it's used as device status code
	private String statusCode;
	
	private String idEquipment;

	private String model;

	private MediumType medium;

	private String owner;

	private String name;

	private String version;

	private String serialNumber;

	private String macAddress;

	private String ip;

	private IntervalType interval;

	private String errorMessage;

	private boolean inMaintenanceMode;

	private List<String> clearedProfiles;

	private List<Measure> measures;

	private List<EventMQTT> events;

	private List<BillingProfileMQTT> billing;

	private List<MaxDemand> maxDemands;
	private List<InstantaneousValue> instantaneousValues;

	private String meterStatus;

	private HeartBeat heartBeat;

	private String profileObis;

	private String profileCode;

	private String powerSource;
	private String group;
	private String thresholdAct;
	private String threshDuration;

	private String loadProfile1Interval;
	private String loadProfile2Interval;
	private String powerQualityProfileInterval;
	private String instrumentationProfile;

	private String loadProfile1CapturePeriod;
	private String loadProfile2CapturePeriod;
	private String powerQualityProfileCapturePeriod;
	private String instrumentationProfileCapturePeriod;

	private String batteryLevel;
	private String direction_P;
	private String direction_Q;
	private String activeRate;
	private String relayStatus;

	private String billingDate;

	private Long clock;

	private String billingResetStatus;

	private String alarmRegister;
	private String alarmRegisterError;

	private int demandInterval;

	private String meteringModeStatus;
	private String meteringMode;


	private String currentThresholdUp;
	private String currentThresholdLow;

	private String currentRangeLowStatus;

	private String paymentMode;

	private int threshold;

	private GatewayInfo gatewayInfo;

	private String manufacturerName;
	private String deviceID;
	private String ctNumerator;
	private String ctDenominator;
	private String vtNumerator;
	private String vtDenominator;
	private String ctRatio;
	private String vtTransferRatio;
	private String currentRating;
	private String yearOfManufacture;
	private String firmwareVersion;
	private String address;
	private String totaliserSerialNumber;
	private String tranducerSerialNumber;
	private String manufacturerCode;
	private String type;

	private String activeCalenderName;
	private String currentActiveTariff;
	private String imageTransferEnable;
	private String imageTransferStatus;
	
	private String moduleFirmwareVersion;
	private String coreFirmwareVersion;
	private String upgradeCounter;
	private String upgradeTime;

	private String waterProfileInterval;
	private String instrumentationProfileInterval;
	private String heartBeatInterval;
	private String gwEventsInterval;
	private String eventsInterval;
	private String maxDemandProfileInterval;
	private String energyProfileGroup1Interval;
	private String energyProfileGroup2Interval;
	private String energyProfileGroup3Interval;
	private String instantaneousGroup1Interval;
	private String instantaneousGroup2Interval;
	private String instantaneousGroup3Interval;
	private String instantaneousGroup4Interval;
	private String eMonthlyBillingProfile;
	private String eDailyBillingProfile;
	private String wMonthlyBillingProfile;

	private Reconnection reconnection;
	
	public boolean hasErrors() {
		return this.getErrorMessage() != null && !this.getErrorMessage().isEmpty();
	}
}
