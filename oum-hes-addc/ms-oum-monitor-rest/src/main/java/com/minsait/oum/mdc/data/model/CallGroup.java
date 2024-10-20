package com.minsait.oum.mdc.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class CallGroup {

	@Id
	private String id;
	
	private Status status;

	private Integer priority;

	private String name;

	private Direction direction;

	private String executionType;

	private Long datetime;

	private Long userId;
	
	private Long finishDate;

}
