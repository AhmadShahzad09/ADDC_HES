package com.minsait.oum.mdc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.dto.RealTimeMonitorDTO;

import lombok.extern.slf4j.Slf4j;

@Service
public class MonitorOperationServiceImpl implements MonitorOperationService {

	@Autowired
	private MongoOperations mongoOperations;
	
	@Override
	public RealTimeMonitorDTO generate() {
		
		final RealTimeMonitorDTO realTimeMonitorDTO = new RealTimeMonitorDTO();

		// Calls Filters
		final Query callFilterStatusReady = new Query(Criteria.where("status").is(Status.READY));
		final Query callFilterStatusRunning = new Query(Criteria.where("status").is(Status.RUNNING));
		final Query callFilterStatusBuilding = new Query(Criteria.where("status").is(Status.BUILDING));

		final Long callCountStatusReady = this.mongoOperations.count(callFilterStatusReady, Call.class);
		final Long callCountStatusRunning = this.mongoOperations.count(callFilterStatusRunning, Call.class);
		final Long callCountStatusBuilding = this.mongoOperations.count(callFilterStatusBuilding, Call.class);

		realTimeMonitorDTO.setReadyCalls(callCountStatusReady);
		realTimeMonitorDTO.setRunningCalls(callCountStatusRunning);
		realTimeMonitorDTO.setBuildingCalls(callCountStatusBuilding);

		// Tasks Filters
		final Query taskFilterStatusReady = new Query(Criteria.where("tasks.status").is(Status.READY));
		final Query taskFilterStatusRunning = new Query(Criteria.where("tasks.status").is(Status.RUNNING));
		final Query taskFilterStatusBuilding = new Query(Criteria.where("tasks.status").is(Status.BUILDING));

		List<Call> callsTaskStatusReady = this.mongoOperations.find(taskFilterStatusReady, Call.class);
		List<Call> callsTaskStatusRunning = this.mongoOperations.find(taskFilterStatusRunning, Call.class);
		List<Call> callsTaskStatusBuilding = this.mongoOperations.find(taskFilterStatusBuilding, Call.class);

		final Long taskCountStatusReady = Long.valueOf(callsTaskStatusReady.stream().mapToInt(x -> x.getTasks().size()).sum());
		final Long taskCountStatusRunning = Long.valueOf(callsTaskStatusRunning.stream().mapToInt(x -> x.getTasks().size()).sum());
		final Long taskCountStatusBuilding = Long.valueOf(callsTaskStatusBuilding.stream().mapToInt(x -> x.getTasks().size()).sum());

		realTimeMonitorDTO.setReadyTasks(taskCountStatusReady);
		realTimeMonitorDTO.setRunningTasks(taskCountStatusRunning);
		realTimeMonitorDTO.setBuildingTasks(taskCountStatusBuilding);

		return realTimeMonitorDTO;
	}

	
}
