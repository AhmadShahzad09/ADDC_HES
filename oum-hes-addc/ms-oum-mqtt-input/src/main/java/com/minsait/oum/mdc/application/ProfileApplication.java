package com.minsait.oum.mdc.application;

import com.minsait.com.oum.mdc.repository.CallDomain;
import com.minsait.com.oum.mdc.repository.CallGroupDomain;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.data.model.CallGroup;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.adapter.CallAdapter;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.domain.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProfileApplication {
	
	public final static String FIELD_IP = "IP";
	public final static String FIELD_PORT = "PORT";

	@Autowired
	private CallAdapter adapter;

	@Autowired
	CallDomain callDomain;

	@Autowired
	CallGroupDomain callGroupDomain;

	public Call createCallMonitorEntry(Request request) {
		if (request == null)
			throw new IllegalArgumentException("Request is null");
		return createCallMonitorEntry(request, Status.RUNNING);
	}

	public Call createCallMonitorEntry(final Request request, final Status status) {
		Call call = null;
		String requestId = request.getIdRequest();
		log.info("[PROFILE_APPLICATION] Request Id: " + requestId);
		
		Optional<Call> callOpt = callDomain.findById(requestId);
		if (!callOpt.isPresent()) {
			call = adapter.convert(request);
			call.setPid(requestId);
			setCommunication(request, call);
			call = callDomain.save(call);
		} else {
			call = callOpt.get();
			setCommunication(request, call);
		}
		Optional<CallGroup> callGroupOpt = callGroupDomain.findById(call.getIdGroup());
		if (!callGroupOpt.isPresent())
			callGroupDomain.save(createNewGroup(call));
		
		return call;

	}

	private void setCommunication(final Request request, Call call) {
		List<Device> listDevice = request.getDevices();
		for (Device device : listDevice) {
			Map<String, Object> communication = new HashMap<String, Object>();
			List<Map<String, Object>> listProperties = new ArrayList<>();
			Map<String, Object> propertie1 = new HashMap<String, Object>();
			propertie1.put("name", FIELD_PORT);
			propertie1.put("value", "");
			listProperties.add(propertie1);
			Map<String, Object> propertie2 = new HashMap<String, Object>();
			propertie2.put("name", FIELD_IP);
			propertie2.put("value", device.getIp());
			listProperties.add(propertie2);
			communication.put("properties", listProperties);
			call.setCommunication(communication);
		}
	}

	private CallGroup createNewGroup(Call callMonitor) {
		CallGroup group;
		group = new CallGroup();
		group.setDatetime(callMonitor.getDatetime());
		group.setExecutionType(callMonitor.getExecutionType());
		group.setStatus(com.minsait.mdc.data.model.Status.READY);
		group.setName(callMonitor.getName());
		group.setPriority(callMonitor.getPriority());
		group.setPid(callMonitor.getIdGroup());
		group.setDirection(callMonitor.getDirection());
		group.setUserId(callMonitor.getUser());
		return group;
	}

	public Call updateCallStatus(Call call, Status status, String message) {
		return this.updateCallStatus(call, status, message, true);
	}

	public Call updateCallStatus(Call call, Status status, String message, boolean updateTasks) {
		long finishTime = Calendar.getInstance().getTimeInMillis();
		call.setStatus(status);
		call.setFinishDate(finishTime);
		if (message != null && !message.isEmpty()) {
			call.setGeneralError(message);
		}

		Map<Integer, String> errorMap = new HashMap<>();

		if (status.equals(Status.FINISH_WITH_ERROR) || status.equals(Status.FINISH_WARNING)
				|| status.equals(Status.ERROR))
			errorMap.put(0, message);

		if (updateTasks) {
			call.getTasks().stream().forEach(task -> {
				task.setFinishTime(finishTime);
				task.setStatus(status);
				task.getOrder().stream().forEach(order -> {
					order.setStatus(status);
					order.setError(errorMap);
					order.setFinishTime(finishTime);
				});
			});
		}
		updateFinishTimeCallGroup(call, finishTime);

		return callDomain.save(call);
	}

	public Call updateCallAndTaskStatus(Call call, Status callStatus, String callGeneralError, Request request) {
		long finishTime = Calendar.getInstance().getTimeInMillis();
		call.setStatus(callStatus);
		call.setFinishDate(finishTime);
		if (callGeneralError != null && !callGeneralError.isEmpty())
			call.setGeneralError(callGeneralError);

		Map<Integer, String> errorMap = new HashMap<>();

		if (callStatus.equals(Status.FINISH_WITH_ERROR) || callStatus.equals(Status.ERROR))
			errorMap.put(0, callGeneralError);

		for (Device requestDevice : request.getDevices()) {
			// find call task for device
			Optional<EquipmentTask> taskOpt = call.getTasks().stream()
					.filter(task -> task.getDeviceName().equals(requestDevice.getSerialNumber())).findFirst();
			if (taskOpt.isPresent()) {
				EquipmentTask task = taskOpt.get();
				task.setFinishTime(finishTime);
				task.setStatus(this.toTaskOrderStatus(requestDevice.getMeterStatus()));
				task.getOrder().stream()
						// find order in task according to device
						.filter(order -> order.getName().equals(request.getResponseType().getCode()))
						// set order status according to device status
						.forEach(order -> {
							order.setStatus(this.toTaskOrderStatus(requestDevice.getMeterStatus()));
							order.setFinishTime(finishTime);
							if (requestDevice.getErrorMessage() != null && !requestDevice.getErrorMessage().isEmpty())
								order.setError(Collections.singletonMap(0, requestDevice.getErrorMessage()));
						});
			} else {
				log.warn("cannot update task status: task for device {} not found in call {}",
						requestDevice.getSerialNumber(), call.getIdDC());
			}
		}
		updateFinishTimeCallGroup(call, finishTime);

		return callDomain.save(call);
	}

	private Status toTaskOrderStatus(String deviceMeterStatus) {
		return deviceMeterStatus.equals("OK") ? Status.FINISH_OK : Status.FINISH_WITH_ERROR;
	}

	private void updateFinishTimeCallGroup(final Call call, final long finishTime) {
		Optional<CallGroup> callGroupOpt = callGroupDomain.findById(call.getIdGroup());
		if (!callGroupOpt.isPresent()) {
			log.error("[PROFILE APPLICATION] Call Group not associated to Call with id: " + call.getIdGroup());
		} else {
			callGroupOpt.get().setFinishDate(finishTime);
			callGroupDomain.save(callGroupOpt.get());
		}
	}
}
