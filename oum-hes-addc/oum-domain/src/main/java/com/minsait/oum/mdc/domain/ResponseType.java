package com.minsait.oum.mdc.domain;

public enum ResponseType {

	COMMISSION("Commission"),

	LOAD_PROFILE("LoadProfile"),

//	SYNCHRONIZATION("SynchronizationProfile"),

	ALARMS("AlarmsProfile"),

	ENERGY_PROFILE("EnergyProfile"),

	MAX_DEMAND_PROFILE("MaxDemandProfile"),

	INST_VALUES_PROFILE("InstValuesProfile"),

	CONNECT("OnDemandConnect"),

	DISCONNECT("OnDemandDisconnect"),

	RECONNECTION("OnDemandCutOffReconnection"),

	SWITCH_STATUS("OnDemandSwitchStatus"),

	BILLING_PROFILE("OnDemandBillingDataProfile"),

	CLEAR_ALARMS("OnDemandClearAlarms"),

	HEART_BEAT("HearBeat"),

	LOAD_LIMITATION("OnDemandSetLoadLimitation"),

	GET_PROFILE_INTERVAL("OnDemandGetProfileInterval"),

	GET_BILLING_DATE("OnDemandGetBillingDate"),
	SET_BILLING_DATE("OnDemandSetBillingDate"),

	GET_METER_STATUS("OnDemandGetMeterStatus"),

	GET_REAL_TIME_CLOCK("OnDemandReadRealTime"),

	GET_LOAD_LIMIT_THRESHOLD("OnDemandGetLoadLimitThreshold"),

	SET_MAXIMUM_DEMAND("OnDemandSetMaximumDemand"),

	CHANGE_METER_PASSWORD("OnDemandChangeMeterPassword"),

	GET_LOAD_PROFILE_CAPTURE_PERIOD("OnDemandReadLoadProfileCapturePeriod"),
	SET_LOAD_PROFILE_CAPTURE_PERIOD("OnDemandSetLoadProfileCapturePeriod"),

	GET_DEMAND_INTEGRATION_PERIOD("OnDemandGetDemandIntegrationPeriod"),
	SET_DEMAND_INTEGRATION_PERIOD("OnDemandSetDemandIntegrationPeriod"),

    SET_METERING_MODE("OnDemandSetMeteringMode"),
	GET_METERING_MODE("OnDemandGetMeteringMode"),

	GET_PAYMENT_MODE("OnDemandGetPaymentMode"),
	SET_PAYMENT_MODE("OnDemandSetPaymentMode"),

	GET_VOLT_RANGE_UP("OnDemandGetVoltRangeUp"),
	SET_VOLT_RANGE_UP("OnDemandSetVoltRangeUp"),

	GET_VOLT_RANGE_LOW("OnDemandGetVoltRangeLow"),
	SET_VOLT_RANGE_LOW("OnDemandSetVoltRangeLow"),

	GET_CURRENT_RANGE_UP("OnDemandGetCurrentRangeUp"),
	SET_CURRENT_RANGE_UP("OnDemandSetCurrentRangeUp"),

	GET_CURRENT_RANGE_LOW("OnDemandGetCurrentRangeLow"),
	SET_CURRENT_RANGE_LOW("OnDemandSetCurrentRangeLow"),
	
	GET_TARIFF_AGREEMENT("OnDemandGetTariffAgreement"),
	SET_TARIFF_AGREEMENT("OnDemandSetTariffAgreement"),
	
	METER_FIRMWARE_UPGRADE("OnDemandFirmwareUpgrade"),
	
	READ_NAME_PLATE_INFO_METER("OnDemandReadNamePlateInfoMeter"),
	
	EVENTS_AND_ALARMS("OnDemandEventLog"),
	
	METER_PING("OnDemandMeterPing"),
	
	GATEWAY_PING("OnDemandGatewayPing"),
	GATEWAY_NAME_PLATE_INFORMATION("OnDemandGatewayReadNamePlateInfo"),
	GATEWAY_FIRMWARE_UPDATE("OnDemandGatewayFirmwareUpdate"),
	GATEWAY_READ_TIME_DATE("OnDemandGatewayReadRealTime"),
	GATEWAY_SYNCHRONIZATION("OnDemandGatewaySynchronization"),
	GATEWAY_READ_INTERVAL_PERIOD("OnDemandGatewayReadIntervalPeriod"),
	GATEWAY_SET_PROFILE_INTERVAL("OnDemandGatewaySetIntervalPeriod"),
	GATEWAY_SET_MQTT_PASSWORD("OnDemandGatewaySetMqttPassword"),
	WATER_PROFILE("OnDemandWaterProfile"),

	SYNCHRONIZATION("OnDemandSynchronization"),

	GET_FIRMWARE_UPGRADE_STATUS("OnDemandReadFirmwareUpgradeStatus"),
	
	PUSH_ON_ALARM("OnAlarmNotification"),
	PUSH_ON_CONNECTIVITY("OnConnectivityNotification"),
	PUSH_ON_POWER_DOWN("OnPowerDownNotification"),
	PUSH_ON_INSTALLATION("OnInstallationNotification");
	

    private String code;

	private ResponseType(String code) {
		this.code = code;
	}

	public String getCode() {

		return this.code;
	}
}
