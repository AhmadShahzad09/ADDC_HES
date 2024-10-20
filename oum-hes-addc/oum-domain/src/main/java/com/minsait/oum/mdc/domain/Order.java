package com.minsait.oum.mdc.domain;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.minsait.mdc.util.Status;

import lombok.Data;

@Document
@Data
public class Order {

	@Id
	private Long id;

	private String name;

	private Map<String, String> ordersParams;

	private Status status;

	private Map<Integer, String> error;

	private Map<Integer, String> debug;

	private Map<Integer, String> info;
	
	private Long requestId;

	private Long initTime;

	private Long finishTime;
	
	private Long executionId;
	
	private String payload;

}
