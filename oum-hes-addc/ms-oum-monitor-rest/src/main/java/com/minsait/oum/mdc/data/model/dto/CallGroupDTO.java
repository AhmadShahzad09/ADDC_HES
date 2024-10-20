package com.minsait.oum.mdc.data.model.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.CallGroup;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.Task;
import com.minsait.oum.mdc.util.DtoUtils;

import lombok.Data;

@Data
@Document
public class CallGroupDTO extends CallGroup {

	private Long duration;

//	private Long finishDate;

	private Long finishedTasksOk;

	private Long finishedTasksError;

	private Long finishedTasks;

	private Long finishedCallOk;

	private Long finishedCallWithError;

	private Long finishedCalls;

	private Long finishedEquipments;

	private Long finishedEquipmentsOk;

	private Long countBuilding;
	private Long countReady;
	private Long countRunning;
	private Long countCancelled;
	private Long countError;
	private Long countFinishedWithWarning;
	private Long countFinishedWithError;
	private Long countFinishedOk;
	
	private Long countTasksBuilding;
	private Long countTasksReady;
	private Long countTasksRunning;
	private Long countTasksCancelled;
	private Long countTasksError;
	private Long countTasksFinishedWithWarning;
	private Long countTasksFinishedWithError;
	private Long countTasksFinishedOk;
	
	private Long countOrdersBuilding;
	private Long countOrdersReady;
	private Long countOrdersRunning;
	private Long countOrdersCancelled;
	private Long countOrdersError;
	private Long countOrdersFinishedWithWarning;
	private Long countOrdersFinishedWithError;
	private Long countOrdersFinishedOk;
	
	private List<String> equipmentCodes;
	
	

//	private Long countOrdersByStatus(List<Order> orders, Status status) {
//		return orders.stream().filter(x -> {
//			if (null == status) return (null == x.getStatus()); // Contador de sin status
//			if (null == x.getStatus()) x.setStatus(Status.RUNNING); // TODO Vacía es running
//			return status.equals(x.getStatus());
//		}).count();
//	}
//
//	private Status calculateTaskStatus(Task task) {
//		final List<Order> orders = task.getOrder();
//		
//		final Long countBuilding            = countOrdersByStatus(orders, Status.BUILDING);
//		final Long countReady               = countOrdersByStatus(orders, Status.READY);
//		final Long countRunning             = countOrdersByStatus(orders, Status.RUNNING);
//		final Long countCancelled           = countOrdersByStatus(orders, Status.CANCELLED);
//		final Long countError               = countOrdersByStatus(orders, Status.ERROR);
//		final Long countFinishedWithWarning = countOrdersByStatus(orders, Status.FINISH_WARNING);
//		final Long countFinishedWithError   = countOrdersByStatus(orders, Status.FINISH_WITH_ERROR);
//		final Long countFinishedOk          = countOrdersByStatus(orders, Status.FINISH_OK);
//		
//		// Estado de la llamada: finalizado por defecto, a no ser que haya tareas en otro estado (ordenados por prioridad)
//		return Status.calculateStatusByCounters(countBuilding, countReady, countRunning, countCancelled, countError, countFinishedWithWarning, countFinishedWithError, countFinishedOk);
//	}
//
//	private Long countTasksByStatus(List<Task> tasks, Status status) {
//		return tasks.stream().map(x -> calculateTaskStatus(x)).filter(x -> status.equals(x)).count();
//	}
//
//	private Status calculateCallStatus(Call call) {
//		final List<Task> tasks = call.getTasks();
//		
//		final Long countBuilding            = countTasksByStatus(tasks, Status.BUILDING);
//		final Long countReady               = countTasksByStatus(tasks, Status.READY);
//		final Long countRunning             = countTasksByStatus(tasks, Status.RUNNING);
//		final Long countCancelled           = countTasksByStatus(tasks, Status.CANCELLED);
//		final Long countError               = countTasksByStatus(tasks, Status.ERROR);
//		final Long countFinishedWithWarning = countTasksByStatus(tasks, Status.FINISH_WARNING);
//		final Long countFinishedWithError   = countTasksByStatus(tasks, Status.FINISH_WITH_ERROR);
//		final Long countFinishedOk          = countTasksByStatus(tasks, Status.FINISH_OK);
//		
//		// Estado de la llamada: finalizado por defecto, a no ser que haya tareas en otro estado (ordenados por prioridad)
//		return Status.calculateStatusByCounters(countBuilding, countReady, countRunning, countCancelled, countError, countFinishedWithWarning, countFinishedWithError, countFinishedOk);
//	}
//
//	private Long countCallsByStatus(List<Call> calls, Status status) {
//		return calls.stream().map(x -> calculateCallStatus(x)).filter(x -> status.equals(x)).count();
//	}
//
//	private Long countCallTasksByStatus(List<Call> calls, Status status) {
//		return calls.stream().map(Call::getTasks).flatMap(Collection::stream).map(Task::getStatus).filter(x -> status.equals(x)).count();
//	}
//
//	private Long countCallTaskOrdersByStatus(List<Call> calls, Status status) {
//		return calls.stream().map(Call::getTasks).flatMap(Collection::stream).map(Task::getOrder).flatMap(Collection::stream).map(Order::getStatus).filter(x -> status.equals(x)).count();
//	}

	public void updateCounters(List<Call> groupCalls, Long executionTimeout) {
		
		// Actualizamos las llamadas
		//updateCallCountersFromGroup(iCallGroup);

		// Contadores de equipos
		final List<String> equipmentCodes = groupCalls.stream().map(Call::getTasks).flatMap(Collection::stream).map(Task::getDeviceName)
				.distinct()
				.collect(Collectors.toList());
		this.setEquipmentCodes(equipmentCodes);
		final Long equipments   = groupCalls.stream().map(Call::getTasks).flatMap(Collection::stream).map(Task::getDeviceId).distinct().count();
		final Long equipmentsOk = groupCalls.stream().map(Call::getTasks).flatMap(Collection::stream).filter(x -> Status.FINISH_OK.equals(DtoUtils.calculateTaskStatus(x))).map(Task::getDeviceId).distinct().count();
		this.setFinishedEquipments(equipments);
		this.setFinishedEquipmentsOk(equipmentsOk);

		// Contadores de estados de llamadas
		final Long countBuilding            = DtoUtils.countCallsByStatus(groupCalls, Status.BUILDING);
		final Long countReady               = DtoUtils.countCallsByStatus(groupCalls, Status.READY);
		final Long countRunning             = DtoUtils.countCallsByStatus(groupCalls, Status.RUNNING);
		final Long countCancelled           = DtoUtils.countCallsByStatus(groupCalls, Status.CANCELLED);
		final Long countError               = DtoUtils.countCallsByStatus(groupCalls, Status.ERROR);
		final Long countFinishedWithWarning = DtoUtils.countCallsByStatus(groupCalls, Status.FINISH_WARNING);
		final Long countFinishedWithError   = DtoUtils.countCallsByStatus(groupCalls, Status.FINISH_WITH_ERROR);
		final Long countFinishedOk          = DtoUtils.countCallsByStatus(groupCalls, Status.FINISH_OK);

		this.setCountBuilding(countBuilding);
		this.setCountReady(countReady);
		this.setCountRunning(countRunning);
		this.setCountCancelled(countCancelled);
		this.setCountError(countError);
		this.setCountFinishedWithWarning(countFinishedWithWarning);
		this.setCountFinishedWithError(countFinishedWithError);
		this.setCountFinishedOk(countFinishedOk);

		// Contadores de estados de tareas
		final Long countTasksBuilding            = DtoUtils.countCallTasksByStatus(groupCalls, Status.BUILDING);
		final Long countTasksReady               = DtoUtils.countCallTasksByStatus(groupCalls, Status.READY);
		final Long countTasksRunning             = DtoUtils.countCallTasksByStatus(groupCalls, Status.RUNNING);
		final Long countTasksCancelled           = DtoUtils.countCallTasksByStatus(groupCalls, Status.CANCELLED);
		final Long countTasksError               = DtoUtils.countCallTasksByStatus(groupCalls, Status.ERROR);
		final Long countTasksFinishedWithWarning = DtoUtils.countCallTasksByStatus(groupCalls, Status.FINISH_WARNING);
		final Long countTasksFinishedWithError   = DtoUtils.countCallTasksByStatus(groupCalls, Status.FINISH_WITH_ERROR);
		final Long countTasksFinishedOk          = DtoUtils.countCallTasksByStatus(groupCalls, Status.FINISH_OK);

		this.setCountTasksBuilding(countTasksBuilding);
		this.setCountTasksReady(countTasksReady);
		this.setCountTasksRunning(countTasksRunning);
		this.setCountTasksCancelled(countTasksCancelled);
		this.setCountTasksError(countTasksError);
		this.setCountTasksFinishedWithWarning(countTasksFinishedWithWarning);
		this.setCountTasksFinishedWithError(countTasksFinishedWithError);
		this.setCountTasksFinishedOk(countTasksFinishedOk);

		// Contadores de estados de órdenes
		final Long countOrdersBuilding            = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.BUILDING);
		final Long countOrdersReady               = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.READY);
		final Long countOrdersRunning             = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.RUNNING);
		final Long countOrdersCancelled           = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.CANCELLED);
		final Long countOrdersError               = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.ERROR);
		final Long countOrdersFinishedWithWarning = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.FINISH_WARNING);
		final Long countOrdersFinishedWithError   = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.FINISH_WITH_ERROR);
		final Long countOrdersFinishedOk          = DtoUtils.countCallTaskOrdersByStatus(groupCalls, Status.FINISH_OK);
		
		this.setCountOrdersBuilding(countOrdersBuilding);
		this.setCountOrdersReady(countOrdersReady);
		this.setCountOrdersRunning(countOrdersRunning);
		this.setCountOrdersCancelled(countOrdersCancelled);
		this.setCountOrdersError(countOrdersError);
		this.setCountOrdersFinishedWithWarning(countOrdersFinishedWithWarning);
		this.setCountOrdersFinishedWithError(countOrdersFinishedWithError);
		this.setCountOrdersFinishedOk(countOrdersFinishedOk);

		// Estado del grupo: finalizado por defecto, a no ser que haya llamadas en otro estado (ordenados por prioridad)
		Status status = Status.calculateStatusByCounters(countBuilding, countReady, countRunning, countCancelled, countError, countFinishedWithWarning, countFinishedWithError, countFinishedOk);
		this.setStatus(status);

		// Duración y finalización
		final Long minTaskInitTime = this.getDatetime(); // groupCalls.stream().map(Call::getTasks).flatMap(Collection::stream).filter(x
															// -> null != x.getInitTime()).map(x ->
		// x.getInitTime()).min(Long::compare).orElse(0L);
		if (this.getFinishDate() != null) {
			final Long maxTaskFinishTime = this.getFinishDate();
			final Long duration = maxTaskFinishTime - minTaskInitTime;
			this.setDuration( Status.RUNNING.equals(status) ? null : duration );
			this.setFinishDate( Status.RUNNING.equals(status) ? null : maxTaskFinishTime );
		}

		// cambio finder por finishDate 
//		final Long maxTaskFinishTime = groupCalls.stream().map(Call::getTasks).flatMap(Collection::stream).filter(x -> null != x.getFinishTime()).map(x -> x.getFinishTime()).max(Long::compare).orElse(new Date().getTime());
		
		this.setDatetime(minTaskInitTime);

		// Los que llevan mucho tiempo en ejecución los marcamos como ERROR
		if ((null == status || Status.RUNNING.equals(status)) && (minTaskInitTime < (System.currentTimeMillis() - executionTimeout))) {
			final Long runningToError = this.getCountRunning() + this.getCountError();
			this.setCountError(runningToError);
			this.setCountRunning(0L);
			this.setStatus(Status.ERROR);
			this.setFinishDate(minTaskInitTime + executionTimeout);
			this.setDuration(executionTimeout);
		}

		// TODO En este momento podríamos hacer un save para tenerlo en BD
	}
	

}
