package com.minsait.oum.mdc.data.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Task {

	private Long id;
	
	private Long parentId;

	private Long deviceId;

	private String deviceName;

	private String driverClass;

	private Map<String, String> protocol;
	
	private Map<String, Object> communication;
	
	private Status status;

	private List<Order> order;
	
	private Long initTime;
	
	private Long finishTime;
}
