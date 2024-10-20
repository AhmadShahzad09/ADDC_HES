package com.minsait.oum.mdc.util;

import java.util.Collection;
import java.util.List;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.Order;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.Task;

public class DtoUtils {
	
	public static Long countOrdersByStatus(List<Order> orders, Status status) {
		return orders.stream().filter(x -> {
			if (null == status) return (null == x.getStatus()); // Contador de sin status
			if (null == x.getStatus()) x.setStatus(Status.RUNNING); // TODO Vacía es running
			return status.equals(x.getStatus());
		}).count();
	}

	public static Status calculateTaskStatus(Task task) {
		final List<Order> orders = task.getOrder();
		
		final Long countBuilding            = countOrdersByStatus(orders, Status.BUILDING);
		final Long countReady               = countOrdersByStatus(orders, Status.READY);
		final Long countRunning             = countOrdersByStatus(orders, Status.RUNNING);
		final Long countCancelled           = countOrdersByStatus(orders, Status.CANCELLED);
		final Long countError               = countOrdersByStatus(orders, Status.ERROR);
		final Long countFinishedWithWarning = countOrdersByStatus(orders, Status.FINISH_WARNING);
		final Long countFinishedWithError   = countOrdersByStatus(orders, Status.FINISH_WITH_ERROR);
		final Long countFinishedOk          = countOrdersByStatus(orders, Status.FINISH_OK);
		
		// Estado de la llamada: finalizado por defecto, a no ser que haya tareas en otro estado (ordenados por prioridad)
		return Status.calculateStatusByCounters(countBuilding, countReady, countRunning, countCancelled, countError, countFinishedWithWarning, countFinishedWithError, countFinishedOk);
	}

	public static Long countTasksByStatus(List<Task> tasks, Status status) {
		return tasks.stream().map(x -> calculateTaskStatus(x)).filter(x -> status.equals(x)).count();
	}

	public static Status calculateCallStatus(Call call) {
		final List<Task> tasks = call.getTasks();
		
		final Long countBuilding            = countTasksByStatus(tasks, Status.BUILDING);
		final Long countReady               = countTasksByStatus(tasks, Status.READY);
		final Long countRunning             = countTasksByStatus(tasks, Status.RUNNING);
		final Long countCancelled           = countTasksByStatus(tasks, Status.CANCELLED);
		final Long countError               = countTasksByStatus(tasks, Status.ERROR);
		final Long countFinishedWithWarning = countTasksByStatus(tasks, Status.FINISH_WARNING);
		final Long countFinishedWithError   = countTasksByStatus(tasks, Status.FINISH_WITH_ERROR);
		final Long countFinishedOk          = countTasksByStatus(tasks, Status.FINISH_OK);
		
		// Estado de la llamada: finalizado por defecto, a no ser que haya tareas en otro estado (ordenados por prioridad)
		return Status.calculateStatusByCounters(countBuilding, countReady, countRunning, countCancelled, countError, countFinishedWithWarning, countFinishedWithError, countFinishedOk);
	}

	public static Long countCallsByStatus(List<Call> calls, Status status) {
		return calls.stream().map(x -> calculateCallStatus(x)).filter(x -> status.equals(x)).count();
	}

	public static Long countCallTasksByStatus(List<Call> calls, Status status) {
		return calls.stream().map(Call::getTasks).flatMap(Collection::stream).map(Task::getStatus).filter(x -> status.equals(x)).count();
	}

	public static Long countCallTaskOrdersByStatus(List<Call> calls, Status status) {
		return calls.stream().map(Call::getTasks).flatMap(Collection::stream).map(Task::getOrder).flatMap(Collection::stream).map(Order::getStatus).filter(x -> status.equals(x)).count();
	}

	public static Long countTaskOrdersByStatus(List<Task> tasks, Status status) {
		return tasks.stream().map(Task::getOrder).flatMap(Collection::stream).map(Order::getStatus).filter(x -> status.equals(x)).count();
	}
	

}
