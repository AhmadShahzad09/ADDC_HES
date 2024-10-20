package com.minsait.oum.mdc.data.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Order {

	@Id
	private Long id;

	private String name;

	private Map<String, String> ordersParams;

	private Status status;

	private String errorInfo;

	private Map<Integer, String> error;

	private Map<Integer, String> debug;

	private Map<Integer, String> info;
	
	private Long requestId;

	private Long initTime;

	private Long finishTime;
	
	private Long executionId;
	
	private String payload;
	
}
