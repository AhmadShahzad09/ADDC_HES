package com.minsait.mdc.util;

public class AppConstants {

	public static String PROTOCOL_V09 = "v09";
	public static String PROTOCOL_V11 = "v11";
	public static String PROTOCOL_V13 = "v13";
	public static String DISCONNECTION_MODE = "Mode4";

	public static class DcuParameterKey {
		public static String HEART_BEAT = "heartbeat";
	}

	public static class OnDemand {

		public static class COMMAND_TYPE {
			public static String DISCONNECTION = "DISCONNECTION";
			public static String CONNECTION = "CONNECTION";
			public static String CURRENT_RANGE_LOW = "currentRangeLow";
			public static String CURRENT_RANGE_UP = "currentRangeUp";
			public static String VOLT_RANGE_LOW = "voltRangeLow";
			public static String VOLT_RANGE_UP = "voltRangeUp";
			public static String LOAD_LIMIT = "LoadLimit";
		}

		public static class OPERATION_TYPE {
			public static String DEMAND_INTEGRATION_PERIOD = "DemandIntegrationPeriod";
		}

		public static class COAP_PATH {
			public static String GATEWAY_NAME_PLATE = "/gwnameplate";
			public static String RECONNECTION = "/reconnection";
			public static String ENERGY_PROFILE = "/energyprofile";
			public static String WATER_PROFILE = "/waterprofile";
			public static String LOAD_PROFILE_1 = "/loadprofile1";
			public static String LOAD_PROFILE_2 = "/loadprofile2";
			public static String INSTRUMENTATION_PROFILE = "/instrumentationprofile";
			public static String POWER_QUALITY_PROFILE = "/powerquality";
			public static String SET_PROFILE_INTERVAL = "/setgwinterval";
			public static String READ_PROFILE_INTERVAL = "/getgwinterval";
			public static String GATEWAY_SET_TIME_SYNCHRONIZATION = "/gwsynchronization";
			public static String GATEWAY_FIRMWARE_UPDATE = "/gwfirmwareupdate";
			public static String SET_SYNCHRONIZATION = "/synchronization";
			public static String SWITCH_STATUS = "/switchstatus";
			public static String SET_BILLING_DATE = "/setbillingDate";
			public static String CLEAR_ALARMS = "/clearalarms";
			public static String MAXIMUM_DEMAND_RESET = "/mdieobreset";
			public static String MAXIMUM_DEMAND_REGISTERS = "/maxdemand";
			public static String GATEWAY_READ_CLOCK_TIME = "/getgwclocktime";
			public static String INSTANTANEOUS_LOAD_PROFILE = "/instvalues";
			public static String READ_BILLING_PROFILE = "/billingprofile";
			public static String READ_REAL_CLOCK_TIME = "/clocktime";
			public static String READ_NAME_PLATE = "/meternameplate";
			public static String METER_PING = "/meterping";
			public static String GATEWAY_PING = "/ping";
			public static String METER_NAME_PLATE = "/meternameplate";
			public static String GET_PAYMENT_MODE = "/readpaymentmode";
			public static String SET_PAYMENT_MODE = "/setPaymentmode";
			public static String SET_DEMAND_INTEGRATION_PERIOD = "/setdemandInterval";
			public static String GET_DEMAND_INTEGRATION_PERIOD = "/getdemandInterval";
			public static String GET_BILLING_DATE = "/getbillingDate";
			public static String GET_LOAD_LIMIT_THRESHOLD = "/readloadlimit";
			public static String SET_CURRENT_RANGE_LOW = "/currentrangeLow";
			public static String GET_CURRENT_RANGE_LOW = "/getcurrentrangelow";
			public static String SET_CURRENT_RANGE_UP = "/currentrangeUp";
			public static String GET_CURRENT_RANGE_UP = "/getcurrentrangeup";
			public static String SET_VOLT_RANGE_LOW = "/setvoltrangeLow";
			public static String GET_VOLT_RANGE_LOW = "/getvoltrangeLow";
			public static String SET_LOAD_LIMITATION = "/loadlimit";
			public static String GET_METERING_MODE = "/getmeteringmode";
			public static String SET_METERING_MODE = "/setMeteringmode";
			public static String FIRM_WARE_UPGRADE = "/firmwareupdate";
			public static String CHANGE_METER_PASSWORD = "/setmeterpassword";
			public static String GET_VOLT_RANGE_UP = "/getvoltrangeup";
			public static String SET_VOLT_RANGE_UP = "/setvoltrangeUp";
			public static String SEND_METERS_INFORMATION = "/gatewayconfig";
			public static String READ_LOAD_PROFILE_CAPTURE_PERIOD = "/getprofileinterval";
			public static String SET_TARIFF_AGREEMENT = "/toutariff";
			public static String GET_TARIFF_AGREEMENT = "/getactivetariff";
			public static String SET_LOAD_PROFILE_CAPTURE_PERIOD = "/setprofileinterval";
			public static String GET_METER_STATUS = "/meterstatus";
			public static String GATEWAY_SET_MQTT_PASSWORD = "/setmqttpassword";
			public static String GET_FIRMWARE_UPGRADE_STATUS = "/firmwareupgradestatus";

		}
	}

}
