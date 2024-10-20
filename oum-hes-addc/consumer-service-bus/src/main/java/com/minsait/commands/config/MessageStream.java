package com.minsait.commands.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface MessageStream {

//	String INPUT_DLMS = "dlms-in";
//	String INPUT_STG_ZIV = "stg-ziv-in";
//	String INPUT_IEC = "iec-in";
	String OUTPUT_REQUEST = "request-out";
	// String OUTPUT_STG_ZIV = "stg-ziv-out";

	String OUTPUT_MDM_BILLING_PROFILE = "billing_profile";
	String OUTPUT_MDM_DATE_TIME = "mdm-date-time";
	String OUTPUT_MDM_CONNETION_STATUS = "mdm-connection-status";
	String OUTPUT_MDM_INSTANTANEOUS_VALUE = "mdm-instantaneous-value";

	String OUTPUT_MDM_MEASURE = "mdm-measure";
	String OUTPUT_MDM_READING = "mdm-reading";
	String OUTPUT_MDM_REGISTER = "mdm-register";
	String OUTPUT_MDM_SIGNATURE = "mdm-signature";

	String OUTPUT_MDM_METER_PARAMETER = "mdm-meter-parameter";
	String OUTPUT_MDM_METER_EVENT = "mdm-meter-event";
	String OUTPUT_MDM_METER_SPONTANEOUS_EVENT = "mdm-meter-spontaneous-event";
	String OUTPUT_MDM_METER_UPDATE = "mdm-meter-update";
	String OUTPUT_MDM_METER_FIRMWARE_ACK = "mdm-meter-firmware-ack";

	String OUTPUT_MDM_DCU_PARAMETER = "mdm-dcu-parameter";
	String OUTPUT_MDM_DCU_EVENT = "mdm-dcu-event";
	String OUTPUT_MDM_DCU_SPONTANEOUS_EVENT = "mdm-dcu-spontaneous-event";
	String OUTPUT_MDM_DCU_UPDATE = "mdm-dcu-update";
	String OUTPUT_MDM_DCU_FIRMWARE_ACK = "mdm-dcu-firmware-ack";

	String OUTPUT_MDM_CONTRACTED_POWER = "mdm-contracted-power";
	String OUTPUT_MDM_SEASONS = "mdm-seasons";
	String OUTPUT_MDM_EXISTING_METERS = "mdm-existing-meters";

	String OUTPUT_MDM_CUT_OFF = "mdm-cut-off";
	String OUTPUT_MDM_RECONNECTION = "mdm-reconnection";
	String OUTPUT_MDM_SYNCHRONIZATION = "mdm-synchronization";

	String OUTPUT_MDM_PUSH_ALARM = "mdm-push-alarm";
	String OUTPUT_MDM_DISCONNECT_CONTROL = "mdm-disconnect-control";

	String OUTPUT_MDM_NOTIFY = "mdm-notify";

//	@Input(INPUT_STG_ZIV)
//	SubscribableChannel stgZivIn();
//
//	@Input(INPUT_DLMS)
//	SubscribableChannel messageIn();
//
//	@Input(INPUT_IEC)
//	SubscribableChannel messageIecIn();

	@Output(OUTPUT_REQUEST)
	SubscribableChannel messageOutRequest();

	@Output(OUTPUT_MDM_BILLING_PROFILE)
	SubscribableChannel messageOutMdmBillingProfile();

	@Output(OUTPUT_MDM_DATE_TIME)
	SubscribableChannel messageOutMdmDateTime();

	@Output(OUTPUT_MDM_CONNETION_STATUS)
	SubscribableChannel messageOutMdmConnectionStatus();

	@Output(OUTPUT_MDM_INSTANTANEOUS_VALUE)
	SubscribableChannel messageOutMdmInstantaneousValue();

	@Output(OUTPUT_MDM_MEASURE)
	SubscribableChannel messageOutMdmMeasure();

	@Output(OUTPUT_MDM_READING)
	SubscribableChannel messageOutMdmReading();

	@Output(OUTPUT_MDM_REGISTER)
	SubscribableChannel messageOutMdmRegister();

	@Output(OUTPUT_MDM_SIGNATURE)
	SubscribableChannel messageOutMdmSignature();

	@Output(OUTPUT_MDM_METER_PARAMETER)
	SubscribableChannel messageOutMdmMeterParameter();

	@Output(OUTPUT_MDM_METER_EVENT)
	SubscribableChannel messageOutMdmMeterEvent();

	@Output(OUTPUT_MDM_METER_SPONTANEOUS_EVENT)
	SubscribableChannel messageOutMdmMeterSpontaneousEvent();

	@Output(OUTPUT_MDM_METER_UPDATE)
	SubscribableChannel messageOutMdmMeterUpdate();

	@Output(OUTPUT_MDM_METER_FIRMWARE_ACK)
	SubscribableChannel messageOutMdmMeterFirmwareAck();

	@Output(OUTPUT_MDM_DCU_PARAMETER)
	SubscribableChannel messageOutMdmDcuParameter();

	@Output(OUTPUT_MDM_DCU_EVENT)
	SubscribableChannel messageOutMdmDcuEvent();

	@Output(OUTPUT_MDM_DCU_SPONTANEOUS_EVENT)
	SubscribableChannel messageOutMdmDcuSpontaneousEvent();

	@Output(OUTPUT_MDM_DCU_UPDATE)
	SubscribableChannel messageOutMdmDcuUpdate();

	@Output(OUTPUT_MDM_DCU_FIRMWARE_ACK)
	SubscribableChannel messageOutMdmDcuFirmwareAck();

	@Output(OUTPUT_MDM_CONTRACTED_POWER)
	SubscribableChannel messageOutMdmContactedPower();

	@Output(OUTPUT_MDM_SEASONS)
	SubscribableChannel messageOutMdmSeasons();

	@Output(OUTPUT_MDM_EXISTING_METERS)
	SubscribableChannel messageOutMdmExistingMeters();

	@Output(OUTPUT_MDM_CUT_OFF)
	SubscribableChannel messageOutMdmCutOff();

	@Output(OUTPUT_MDM_RECONNECTION)
	SubscribableChannel messageOutMdmReconnection();

	@Output(OUTPUT_MDM_SYNCHRONIZATION)
	SubscribableChannel messageOutMdmSynchronization();

	@Output(OUTPUT_MDM_PUSH_ALARM)
	SubscribableChannel messageOutMdmPushAlarm();

	@Output(OUTPUT_MDM_DISCONNECT_CONTROL)
	SubscribableChannel messageOutMdmDisconnectControl();

	@Output(OUTPUT_MDM_NOTIFY)
	SubscribableChannel messageOutMdmNotify();

}
