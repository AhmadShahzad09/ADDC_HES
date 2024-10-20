package com.minsait.oum.mdc.coap.commands.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class OnDemandOrder {

	private String gatewaySerialNumber;

	private List<String> devices;

	private Map<String, String> parameters;

	private List<SchedulerConfig> schedulerConfig;
	
	private List<String> groups;

	public OnDemandOrder() {
		devices = new ArrayList<String>();
		parameters = new HashMap<String, String>();
		schedulerConfig = new ArrayList<>();
		groups = new ArrayList<>();
	}
}
