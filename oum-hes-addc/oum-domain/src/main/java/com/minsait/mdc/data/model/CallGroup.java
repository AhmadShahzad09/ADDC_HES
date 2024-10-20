package com.minsait.mdc.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.minsait.mdc.util.Direction;

import lombok.Data;

@Data
@Document
public class CallGroup {

	@Id
	String pid;

	Status status;

	Integer priority;

	String name;

	Direction direction;

	String executionType;

	Long datetime;

	Long duration;

	Long finishDate;

	Long finishTaskOk;

	Long finishTaskError;

	Long finishTasks;

	Long finishCallOK;

	Long finishCallError;

	Long finishCalls;

	Long userId;

	Long finishEquipments;

	Long finishEquipmentsOK;

	Long countBuilding;

	Long countReady;

	Long countRunning;

	Long countCancelled;

	Long countError;

	Long countFinishWarning;

	Long countFinishError;

	Long countFinishOk;


}
