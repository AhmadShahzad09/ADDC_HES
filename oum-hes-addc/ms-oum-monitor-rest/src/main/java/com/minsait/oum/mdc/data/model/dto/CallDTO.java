package com.minsait.oum.mdc.data.model.dto;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.CallGroup;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.Task;
import com.minsait.oum.mdc.util.CommunicationUtils;
import com.minsait.oum.mdc.util.DtoUtils;

import lombok.Data;

@Data
public class CallDTO extends Call {

	private Long finishOK;
	private Long finishError;
	private Long finishTasks;
	
	private String endPoint;
	
	private Long initDate; // TODO call.datetime
	private Long endDate; // TODO call.finishDate

	private Long countBuilding;
	private Long countReady;
	private Long countRunning;
	private Long countCancelled;
	private Long countError;
	private Long countFinishedWithWarning;
	private Long countFinishedWithError;
	private Long countFinishedOk;
	
	private Long countOrdersBuilding;
	private Long countOrdersReady;
	private Long countOrdersRunning;
	private Long countOrdersCancelled;
	private Long countOrdersError;
	private Long countOrdersFinishedWithWarning;
	private Long countOrdersFinishedWithError;
	private Long countOrdersFinishedOk;
	
	public void updateCounters(CallGroup callGroup, Long executionTimeout) {
		
		final Map<String, Object> communication = this.getCommunication();
		final String endPoint = CommunicationUtils.generateIpPort(communication);
		this.setEndPoint(endPoint);

		//this.setIdDC(actual.getIdDC()); // No hay

//		if (actual.getFinishOK() != null) {
//			level1.setFinishOK(actual.getFinishOK());
//		}
//
//		if (actual.getFinishError() != null) {
//			level1.setFinishError(actual.getFinishError());
//		}
//
//		if (actual.getFinishTasks() != null) {
//			level1.setFinishTasks(actual.getFinishTasks());
//		}

		final List<Task> tasks = this.getTasks();
		
		final Long countBuilding            = DtoUtils.countTasksByStatus(tasks, Status.BUILDING);
		final Long countReady               = DtoUtils.countTasksByStatus(tasks, Status.READY);
		final Long countRunning             = DtoUtils.countTasksByStatus(tasks, Status.RUNNING);
		final Long countCancelled           = DtoUtils.countTasksByStatus(tasks, Status.CANCELLED);
		final Long countError               = DtoUtils.countTasksByStatus(tasks, Status.ERROR);
		final Long countFinishedWithWarning = DtoUtils.countTasksByStatus(tasks, Status.FINISH_WARNING);
		final Long countFinishedWithError   = DtoUtils.countTasksByStatus(tasks, Status.FINISH_WITH_ERROR);
		final Long countFinishedOk          = DtoUtils.countTasksByStatus(tasks, Status.FINISH_OK);
		
		this.setCountBuilding(countBuilding);
		this.setCountReady(countReady);
		this.setCountRunning(countRunning);
		this.setCountCancelled(countCancelled);
		this.setCountError(countError);
		this.setCountFinishedWithWarning(countFinishedWithWarning);
		this.setCountFinishedWithError(countFinishedWithError);
		this.setCountFinishedOk(countFinishedOk);

		// Contadores de estados de órdenes
		final Long countOrdersBuilding            = DtoUtils.countTaskOrdersByStatus(tasks, Status.BUILDING);
		final Long countOrdersReady               = DtoUtils.countTaskOrdersByStatus(tasks, Status.READY);
		final Long countOrdersRunning             = DtoUtils.countTaskOrdersByStatus(tasks, Status.RUNNING);
		final Long countOrdersCancelled           = DtoUtils.countTaskOrdersByStatus(tasks, Status.CANCELLED);
		final Long countOrdersError               = DtoUtils.countTaskOrdersByStatus(tasks, Status.ERROR);
		final Long countOrdersFinishedWithWarning = DtoUtils.countTaskOrdersByStatus(tasks, Status.FINISH_WARNING);
		final Long countOrdersFinishedWithError   = DtoUtils.countTaskOrdersByStatus(tasks, Status.FINISH_WITH_ERROR);
		final Long countOrdersFinishedOk          = DtoUtils.countTaskOrdersByStatus(tasks, Status.FINISH_OK);
		
		this.setCountOrdersBuilding(countOrdersBuilding);
		this.setCountOrdersReady(countOrdersReady);
		this.setCountOrdersRunning(countOrdersRunning);
		this.setCountOrdersCancelled(countOrdersCancelled);
		this.setCountOrdersError(countOrdersError);
		this.setCountOrdersFinishedWithWarning(countOrdersFinishedWithWarning);
		this.setCountOrdersFinishedWithError(countOrdersFinishedWithError);
		this.setCountOrdersFinishedOk(countOrdersFinishedOk);

		// Estado de la llamada: finalizado por defecto, a no ser que haya tareas en otro estado (ordenados por prioridad)
		Status status = Status.calculateStatusByCounters(countBuilding, countReady, countRunning, countCancelled, countError, countFinishedWithWarning, countFinishedWithError, countFinishedOk);
		this.setStatus(status);

		// Duración y finalización
		final Long minTaskInitTime = this.getDatetime(); //group.getDatetime(); //tasks.stream().filter(x -> null != x.getInitTime()).map(x -> x.getInitTime()).min(Long::compare).orElse(0L);
//			final Long maxTaskFinishTime = tasks.stream().filter(x -> null != x.getFinishTime()).map(x -> x.getFinishTime()).max(Long::compare).orElse(new Date().getTime());
//		final Long maxTaskFinishTime = tasks.stream().map(Task::getOrder).flatMap(Collection::stream).filter(x -> null != x.getFinishTime()).map(x -> x.getFinishTime()).max(Long::compare).orElse(new Date().getTime());
		

		if (this.getFinishDate() != null) {
			final Long maxTaskFinishTime = this.getFinishDate();
			final Long duration = maxTaskFinishTime - minTaskInitTime;
			this.setDuration( Status.RUNNING.equals(status) ? null : duration );
			this.setEndDate( Status.RUNNING.equals(status) ? null : maxTaskFinishTime );
		}
		this.setInitDate(minTaskInitTime);

		// Las que llevan mucho tiempo en ejecución las marcamos como ERROR
		if ((null == status || Status.RUNNING.equals(status)) && (minTaskInitTime < (System.currentTimeMillis() - executionTimeout))) {
			final Long runningToError = this.getCountRunning() + this.getCountError();
			this.setCountError(runningToError);
			this.setCountRunning(0L);
			this.setStatus(Status.ERROR);
			this.setEndDate(minTaskInitTime + executionTimeout);
			this.setDuration(executionTimeout);
		}
	}

}
