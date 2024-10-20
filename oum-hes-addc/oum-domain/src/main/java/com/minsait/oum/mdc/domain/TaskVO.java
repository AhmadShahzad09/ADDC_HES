package com.minsait.oum.mdc.domain;

import java.util.Map;

import lombok.Data;

@Data
public class TaskVO {

	private String pid;

	private boolean applyToAllTasks;
	
	private Long taskId;	
	
	private Long orderId;

	private Map<Integer, String> error;

	private Map<Integer, String> debug;

	private Map<Integer, String> info;

	private Long requestId;

	private String meterId;

}
