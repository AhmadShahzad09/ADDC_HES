package com.minsait.oum.mdc.domain.billing;

public enum BillingMagnitudeConverter {

	IA("BRM_IMPORT_ACTIVE_ENERGY"), EA("BRM_EXPORT_ACTIVE_ENERGY"), Q1("BRM_Q1_REACTIVE_ENERGY"),
	Q2("BRM_Q2_REACTIVE_ENERGY"), Q3("BRM_Q3_REACTIVE_ENERGY"), Q4("BRM_Q4_REACTIVE_ENERGY"), MDAA("BRM_MAX_DEMAND_EXPORT_ACTIVE"), MDA("BRM_MAX_DEMAND_IMPORT_ACTIVE"),
	APLUS("BRM_TOTAL_ACTIVE_ENERGY_IMPORT_A+"), ANEGATIVE("BRM_TOTAL_ACTIVE_ENERGY_EXPORT_A-"), MDAPLUS("BRM_MAX_DEMAND_ACTIVE_POWER_IMPORT"), MDANEGATIVE("BRM_MAX_DEMAND_ACTIVE_POWER_EXPORT");

	private String value;

	private BillingMagnitudeConverter(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static BillingMagnitudeConverter getBillingMagnitudeConverterByString(String value) {

		switch (value) {
		case "IA":
			return IA;
		case "EA":
			return EA;
		case "Q1":
			return Q1;
		case "Q2":
			return Q2;
		case "Q3":
			return Q3;
		case "Q4":
			return Q4;
		case "MDA":
			return MDA;
		case "MDAA":
			return MDAA;

			// Added for testing purpose
			case "+A":
				return APLUS;
			case "-A":
				return ANEGATIVE;
			case "+MDA":
				return MDAPLUS;
			case "-MDA":
				return MDANEGATIVE;

		default:
			return null;
		}
	}

}
