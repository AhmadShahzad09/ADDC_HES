package com.minsait.oum.mdc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.Order;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.Task;
import com.minsait.oum.mdc.data.model.dto.Pagination;
import com.minsait.oum.mdc.data.model.dto.TaskDTO;
import com.minsait.oum.mdc.data.model.filter.TaskFilter;
import com.minsait.oum.mdc.data.repository.CallRepository;

import lombok.extern.slf4j.Slf4j;

@Service
public class TaskOperationServiceImpl implements OperationService<Task, TaskDTO, TaskFilter> {

	@Value("${execution.timeout}")
	private Long executionTimeout;

	@Autowired
	private CallRepository callRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Pagination<TaskDTO> find(TaskFilter filter) {
		final Pagination<TaskDTO> result = new Pagination<>();
		
		final Call call = callRepository.findById(filter.getCallId()).orElse(null);
		if (null != call) {
			final Page<Task> pageResult = callRepository.findTasksByFilter(call, filter);
			
			final List<TaskDTO> data = pageResult.getContent().stream().map(x -> mapTask(x)).collect(Collectors.toList());
			
			result.setData(data);
			result.setPages(pageResult.getTotalPages());
			result.setPageSize(pageResult.getNumberOfElements());
			result.setTotal(pageResult.getTotalElements());
		}
		
		return result;
	}
	
	private TaskDTO mapTask(Task task) {
		final Status status = calculateTaskStatus(task);
		task.setStatus(status);

		return modelMapper.map(task, TaskDTO.class);
	}

	private Long countOrdersByStatus(List<Order> orders, Status status) {
		return orders.stream().filter(x -> {
			if (null == status) return (null == x.getStatus()); // Contador de sin status
			if (null == x.getStatus()) x.setStatus(Status.RUNNING); // TODO Vacía es running
			return status.equals(x.getStatus());
		}).count();
	}
	
	private Status calculateTaskStatus(Task task) {
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
	
	@Override
	public void reschedule(TaskFilter filter) {
		
	}

	@Override
	public boolean cancel(TaskFilter filter) {
		return false;
	}

	@Override
	public Task updateStatus(Task object) {
		return null;
	}
	
}

