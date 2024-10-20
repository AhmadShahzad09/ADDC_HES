package com.minsait.oum.mdc.data.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Call {

	@Id
	private String id;
	
	private Status status;

	private String name;
	
	private Long datetime;
	
	private String executionType;
	
	private Map<String, String> protocol;
	
	private Map<String, Object> communication;
	
	@Indexed
	private String idGroup;
	
	private boolean commanded;
	
	private boolean synchronous;
	
	@Indexed
	private List<Task> tasks;
	
	private String idDC;
	
	
	
	private String generalError;
	
	private Integer priority;
	
	private Long finishDate;

	private Long duration;

	private Direction direction;

	//private List<String> reference;

	//Long attemps;

	private Long user;

	//private String synchronousData;

	//private Long finishOK;

	//private Long finishError;

	//private Long finishTasks;

}
