package com.minsait.oum.mdc.application;

import org.json.JSONObject;

public interface InventoryClient {
	
	public JSONObject getEquipmentPropsByLogicalDeviceName(String logicalDeviceName);
	
	public JSONObject getEquipmentPropsBySerialNumber(String serialNumber);
	
	public JSONObject resetMeterAlarms(String meterCode);
	
}
