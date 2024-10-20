package com.minsait.oum.mdc.application;

import com.minsait.com.oum.mdc.repository.CallDomain;
import com.minsait.com.oum.mdc.repository.CallGroupDomain;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.data.model.CallGroup;
import com.minsait.mdc.util.Direction;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.adapter.CallAdapter;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.domain.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class ProfileApplication {

	@Autowired
	private CallAdapter adapter;

	@Autowired
	CallDomain callDomain;

	@Autowired
	CallGroupDomain callGroupDomain;

	private synchronized Long generateRandomId() {

		UUID uuid = UUID.randomUUID();
		Long result = uuid.getMostSignificantBits() + uuid.getLeastSignificantBits();
		if (result < 0) {
			return result * -1;
		}
		return result;
	}

	public Call createCallMonitorEntry(Request request) {
		return createCallMonitorEntry(request, Status.RUNNING);
	}

	public Call createCallMonitorEntry(Request request, Status status) {

		if (request == null) {
			log.error("Request is null");
			throw new Error("Request is null");
		}
		log.debug("Creating CallMonitor entry from : " + request.toString());

		try {
			log.info("[PROFILE_APPLICATION BEFORE]" + request.toString());

			log.info("[Received ID-REQUEST]:" + request.getIdRequest());

			String requestId = request.getIdRequest();
			
			if (requestId == null)  {
				requestId=generateRandomId().toString();
				request.setIdRequest(requestId);
			}
						
			Call call = null;
			try {
				call = callDomain.findById(requestId).get();
				// si llegamos aqui es que ya existe un call con ese id,
				// es decir, la peticion proviene de etisalat-topic
			} catch (NoSuchElementException e) {
				// mensaje enviado por el gateway sin previa solicitud
			}
			if (call == null) {
				call = adapter.convert(request);
				call.setPid(requestId);
				call = callDomain.save(call);
			}

			CallGroup callGroup = null;
			try {
				callGroup = callGroupDomain.findById(call.getIdGroup()).get();
			} catch (NoSuchElementException e) {
			}
			if (callGroup == null) {
				callGroup = createNewGroup(call);
				callGroupDomain.save(callGroup);
			}
			log.info("[PROFILE_APPLICATION AFTER SEND]" + request.toString());
			return call;

//			Call callMonitor = adapter.convert(request);
//			
//			CallGroup group = null;
//			boolean update = false;
//			try {
//				group = callGroupDomain.findById(callMonitor.getIdGroup()).get();
//				
//				//TODO: FIX see whether setCallMonitor is necessary
//				if(!group.getName().equals("COMMISSION")){
//					setCallMonitor(request, callMonitor, status);
//					
//					update = true;
//				}else{
//					callMonitor.setPid(UUID.randomUUID().toString());
//				}
//				// Group already exists.
//			} catch (NoSuchElementException e) {
//				if (group == null) {
//					group = createNewGroup(callMonitor);
//					callGroupDomain.save(group);
//				}
//			}
//			Call savedCall = null;
//			try {
//				if (!update) {
//					savedCall = callDomain.save(callMonitor);
//				} else {
//					//Fill in with the update
//					List<Call> savedCallList = callDomain.findByIdGroup(callMonitor.getIdGroup());
//				}
//			} catch (Exception e) {
//				log.error("Error inserting callMonitor with ID: " + callMonitor.getPid());
//				log.error(MdcUtils.getExceptioStackTrace(e));
//			}
//			log.info("[PROFILE_APPLICATION AFTER SEND]" + request.toString());
//			return savedCall;
		} catch (Exception e) {
			log.error("Error inserting in TimeScale", e);
		}
		return null;

	}

	private CallGroup createNewGroup(Call callMonitor) {
		CallGroup group;
		group = new CallGroup();
		group.setDatetime(System.currentTimeMillis());
		group.setExecutionType(callMonitor.getExecutionType());
		group.setStatus(com.minsait.mdc.data.model.Status.READY);
		group.setName(callMonitor.getName());
		group.setPriority(callMonitor.getPriority());
		group.setPid(callMonitor.getIdGroup());
		group.setDirection(callMonitor.getDirection());
		group.setUserId(callMonitor.getUser());
		return group;
	}

//	private void setCallMonitor(Request request, Call callMonitor) {
//		setCallMonitor(request, callMonitor, Status.RUNNING);
//	}

	private void setCallMonitor(Request request, Call callMonitor, Status status) {
		callMonitor.setDirection(Direction.IN);
		callMonitor.setDatetime(request.getTime());
		callMonitor.setName(callMonitor.getName());
		callMonitor.setPriority(1);
		callMonitor.setPid(UUID.randomUUID().toString());
		if (request.getIdRequest() != null) {
			callMonitor.setIdDC(request.getIdRequest());
		}
		if (callMonitor.getExecutionType() == null) {
			callMonitor.setExecutionType("AUTOMATIC");
			callMonitor.setDirection(Direction.IN);
		}
		callMonitor.getTasks().stream().forEach(task -> {
			task.setStatus(status);
			task.getOrder().stream().forEach(order -> {
				order.setRequestId(this.generateRandomId());
				order.setInitTime(LocalDate.now().toEpochDay());
				order.setStatus(status);
			});
		});
	}

//	public void updateCallMonitorEntryStatus(Request request, Status status) {
//
//		if (request == null) {
//			log.error("Request is null");
//			throw new Error("Request is null");
//		}
//		log.debug("Updating CallMonitor entry from : " + request.toString());
//
//		try {
//			log.info("[PROFILE_APPLICATION BEFORE]" + request.toString());
//			Call callMonitor = adapter.convert(request);
//			callMonitor.setStatus(status);
//			savedCall = callDomain.save(callMonitor);
//			return savedCall;
//		} catch (Exception e) {
//			log.error("Error updating callMonitor with ID: " + callMonitor.getPid());
//			log.error(MdcUtils.getExceptioStackTrace(e));
//		}
//	}

	public Call updateCallStatus(Call call, Status status, String message) {
		return this.updateCallStatus(call, status, message, true);
	}

	public Call updateCallStatus(Call call, Status status, String message, boolean updateTasks) {
		call.setStatus(status);
		if (message != null && !message.isEmpty()) {
			call.setGeneralError(message);
		}

		Map<Integer, String> errorMap = new HashMap<>();

		if (status.equals(Status.FINISH_WITH_ERROR) || status.equals(Status.FINISH_WARNING) || status.equals(Status.ERROR))
			errorMap.put(0, message);

		if (updateTasks)
			call.getTasks().stream().forEach(task -> {
				task.setFinishTime(Calendar.getInstance().getTimeInMillis());
				task.setStatus(status);
				task.getOrder().stream().forEach(order -> {
					order.setStatus(status);
					order.setError(errorMap);
				});
			});

		return callDomain.save(call);
	}

	public Call updateCallAndTaskStatus(Call call, Status callStatus, String callGeneralError, Request request) {
		call.setStatus(callStatus);
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
				long currentTimeMillis = System.currentTimeMillis();
				EquipmentTask task = taskOpt.get();
				task.setFinishTime(currentTimeMillis);
				task.setStatus(this.toTaskOrderStatus(requestDevice.getMeterStatus()));

				task.getOrder().stream()
						// find order in task according to device
						.filter(order -> order.getName().equals(request.getResponseType().getCode()))
						// set order status according to device status
						.forEach(order -> {
							order.setStatus(this.toTaskOrderStatus(requestDevice.getMeterStatus()));
							order.setFinishTime(currentTimeMillis);
							if (requestDevice.getErrorMessage() != null && !requestDevice.getErrorMessage().isEmpty())
								order.setError(Collections.singletonMap(0, requestDevice.getErrorMessage()));
						});

			} else {
				log.warn("cannot update task status: task for device {} not found in call {}",
						requestDevice.getSerialNumber(), call.getIdDC());
			}

		}

		return callDomain.save(call);
	}

	private Status toTaskOrderStatus(String deviceMeterStatus) {
		return deviceMeterStatus.equals("OK") ? Status.FINISH_OK : Status.FINISH_WITH_ERROR;
	}
}
