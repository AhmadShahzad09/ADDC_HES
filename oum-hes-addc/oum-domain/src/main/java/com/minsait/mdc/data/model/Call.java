package com.minsait.mdc.data.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.minsait.mdc.util.Direction;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.domain.EquipmentTask;

import lombok.Data;

@Data
@Document
public class Call {

	@Id
	private String pid;
	
	private Long newId;

	private Status status;

	private String generalError;

	private Integer priority;

	private String name;

	private Long datetime;

	private Long finishDate;

	private Long duration;

	private String executionType;

	private Direction direction;

	private List<String> reference;

	private Map<String, String> protocol;

	private Map<String, Object> communication;

	@Indexed
	private String idGroup;

	private Long user;

	private boolean commanded;

	private boolean synchronous;

	private String synchronousData;

//	private Direction direction;

	private Long finishOK;

	private Long finishError;

	private Long finishTasks;

	@Indexed
	private List<EquipmentTask> tasks;

	private Long countBuilding;

	private Long countReady;

	private Long countRunning;

	private Long countCancelled;

	private Long countError;

	private Long countFinishWarning;

	private Long countFinishError;

	private Long countFinishOk;

	private String idDC;

}
