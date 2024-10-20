package com.minsait.dcu;

import lombok.Data;

@Data
public class DcuParameterEntry {

	private String parameter; 

	private String value;

	private Long equipmentId;

	private String meterCode;

	public DcuParameterEntry() {
	}

	public DcuParameterEntry(String parameter, String value, String meterCode) {
		this.parameter = parameter;
		this.value = value;
		this.meterCode = meterCode;
	}
	
}
