package com.minsait.oum.mdc.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minsait.oum.mdc.util.LogExecutionTime;
import com.google.gson.Gson;
import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.CallGroup;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.Task;
import com.minsait.oum.mdc.data.model.dto.CallDTO;
import com.minsait.oum.mdc.data.model.dto.CallGroupDTO;
import com.minsait.oum.mdc.data.model.dto.CountPerIntervalListDTO;
import com.minsait.oum.mdc.data.model.dto.Pagination;
import com.minsait.oum.mdc.data.model.dto.RealTimeMonitorDTO;
import com.minsait.oum.mdc.data.model.dto.StatusDTO;
import com.minsait.oum.mdc.data.model.dto.TaskDTO;
import com.minsait.oum.mdc.data.model.filter.BaseFilter;
import com.minsait.oum.mdc.data.model.filter.CallFilter;
import com.minsait.oum.mdc.data.model.filter.GroupFilter;
import com.minsait.oum.mdc.data.model.filter.TaskFilter;
import com.minsait.oum.mdc.service.CallStatsService;
import com.minsait.oum.mdc.service.MonitorOperationService;
import com.minsait.oum.mdc.service.OperationService;

import lombok.extern.slf4j.Slf4j;

@Primary
@RestController()
@Slf4j
@RequestMapping(value = "/")
public class CallController {
	
	@Autowired
	private OperationService<CallGroup, CallGroupDTO, GroupFilter> groupOperationService;

	@Autowired
	private OperationService<Call, CallDTO, CallFilter> callOperationService;
	
	@Autowired
	private OperationService<Task, TaskDTO, TaskFilter> taskOperationService;
	
	@Autowired
	private MonitorOperationService monitorOperationService;
	
	@Autowired
	private CallStatsService callStatsService;
	
	
	private void checkFilter(BaseFilter baseFilter) {
//		log.info("checkFilter-> {}", baseFilter);
		if (baseFilter.getFirstResult() == null || baseFilter.getSizeNo() == null) {
			throw new Error("Parameters for pagination are missing. Either current page or size number is missing.");
		}
	}

	@LogExecutionTime
	@RequestMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StatusDTO> findStatus() {
		log.info("findStatus");
		return Arrays.asList(Status.values()).stream().filter(Status::isVisible).map(StatusDTO::new).collect(Collectors.toList());
	}
	
	@LogExecutionTime
	@PostMapping(value = "/group", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Pagination<CallGroupDTO> findGroupCallFilter(@RequestBody GroupFilter groupFilter) {
		log.info("findGroupCallFilter -> {}", new Gson().toJson(groupFilter));
		checkFilter(groupFilter);
		return groupOperationService.find(groupFilter);
	}

	@LogExecutionTime
	@PostMapping(value = "/call", produces = MediaType.APPLICATION_JSON_VALUE)
	public Pagination<CallDTO> findCallFilter(@RequestBody CallFilter callFilter) {
		log.info("findCallFilter -> {}", new Gson().toJson(callFilter));
		checkFilter(callFilter);
		return callOperationService.find(callFilter);
	}
	
	@LogExecutionTime
	@PostMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
	public Pagination<TaskDTO> findTaskFilterFilter(@RequestBody TaskFilter taskFilter) {
		log.info("findTaskFilterFilter -> {}", new Gson().toJson(taskFilter));
		checkFilter(taskFilter);
		return taskOperationService.find(taskFilter);
	}
	
	@LogExecutionTime
	@GetMapping(value = "/realTimeMonitorData", produces = MediaType.APPLICATION_JSON_VALUE)
	public RealTimeMonitorDTO generateRealTimeMonitorData() {
		log.info("realTimeMonitorData");
		return monitorOperationService.generate();
	}
	
	@LogExecutionTime
	@PostMapping(value = "/reschedule/group", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void rescheduleGroup(@RequestBody GroupFilter groupFilter) {
		log.info("rescheduleGroup -> {}", new Gson().toJson(groupFilter));
		groupOperationService.reschedule(groupFilter);
	}

	@LogExecutionTime
	@PostMapping(value = "/reschedule/call", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void rescheduleCall(@RequestBody CallFilter callFilter) {
		log.info("rescheduleCall -> {}", new Gson().toJson(callFilter));
		callOperationService.reschedule(callFilter);
	}
	
	@LogExecutionTime
	@GetMapping(value = "/cancel/group/{id}")
	public void cancelGroup(@PathVariable(name = "id") String id) {
		log.info("cancelGroup -> {}", id);
		groupOperationService.cancel(new GroupFilter(id));
	}

	@LogExecutionTime
	@GetMapping(value = "/cancel/call/{id}")
	public void cancelCall(@PathVariable(name = "id") String id) {
		log.info("cancelCall -> {}", id);
		callOperationService.cancel(new CallFilter(id));
	}
	
	@LogExecutionTime
	@PutMapping(value = "/group/{id}/{status}")
	public CallGroup updateGroupStatus(@PathVariable(name = "id") String id, @PathVariable(name = "status") String status) {
		log.info("updateGroupStatus -> {}", id + ", " + status);
//		try {
//			CallGroup callGroup = new CallGroup();
//			callGroup.setId(id);
//			callGroup.setStatus(Status.valueOf(status));
//			CallGroup callGroupUpdate = groupOperationService.updateStatus(callGroup);
//			return callGroupUpdate;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		

		CallGroup callGroup = new CallGroup();
		callGroup.setId(id);
		if (status != null) {
			callGroup.setStatus(Status.valueOf(status));
		}
		groupOperationService.updateStatus(callGroup);
		
		return callGroup;
	}
	
	@LogExecutionTime
	@PostMapping(value = "/call/stats/count24hourly", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountPerIntervalListDTO countLast24HoursCallsPerHourlyInterval() {
		log.info("countLast24HoursCallsPerHourlyInterval");
		return callStatsService.countLast24HoursCallsPerHourlyInterval();
	}
	
	@LogExecutionTime
	@PostMapping(value = "/call/stats/avgRun7daily", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountPerIntervalListDTO last7DaysAverageRuntimePerDailyInterval() {
		log.info("last7DaysAverageRuntimePerDailyInterval");
		return callStatsService.last7DaysAverageRuntimePerDailyInterval();
	}
	
	@LogExecutionTime
	@PostMapping(value = "/call/stats/avgRunRequestType", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountPerIntervalListDTO averageRunTimePerRequestType(@RequestBody CallFilter callFilter) {
		log.info("averageRunTimePerRequestType -> {}", new Gson().toJson(callFilter));
		return callStatsService.averageRunTimePerRequestType(callFilter);
	}
	
	
//	@LogExecutionTime
//	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ICall updateTask(@RequestBody String call, @PathVariable(name = "id") Long id) {
//		ICall iCallString = new Gson().fromJson(call, ICall.class);
//		callTaskService.updateCallById(iCallString, id);
//		return new ICall();
//	}
//	
//	@LogExecutionTime
//	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ICall insertTask(@RequestBody String call) {
//		ICall iCallString = new Gson().fromJson(call, ICall.class);
//		ICall callService = callTaskService.insertCallById(iCallString);
//		return callService;
//	}
//	
//	@RequestMapping("/execute")
//	public void execute() {
//		generateData.createCallService();
//	}
//
//	@RequestMapping("/delete")
//	public void delete() {
//		generateData.delete();
//	}

}
