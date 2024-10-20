package com.minsait.oum.mdc.adapter;

import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.Direction;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CallAdapter {

	public Call convert(Request request) {
		log.info("[CALL ADAPTER, PROCESSING REQUEST->]" + request.toString());

		int idTask = 1;
		List<EquipmentTask> tasks = new ArrayList<EquipmentTask>();

		Call result = new Call();

		result.setIdGroup(request.getIdRequest());
		result.setTasks(tasks);
		result.setUser(999L);
		result.setDatetime(Calendar.getInstance().getTimeInMillis());
		// TODO Chapuza no modificar la request, revisar ocmo tener la misma hora de inicio en tasks y orders
		request.setTime(result.getDatetime());
		if (request.getErrorMessage() != null && !request.getErrorMessage().isEmpty()) {
			result.setStatus(Status.ERROR);
			if (request.getErrorMessage() != null) {
				result.setGeneralError(request.getErrorMessage());
			}
		} else {
			result.setStatus(Status.FINISH_OK);
		}
		for (Device device : request.getDevices()) {

			EquipmentTask task = convertDeviceToEquipmentTask(request, device, idTask);
			tasks.add(task);

			idTask++;
		}
		result.setDirection(Direction.IN);
		String name = request.getResponseType().name();
		log.info("[CALL ADAPTER] The id name is:" + name);
		result.setName(name);
		result.setPriority(0);

		return result;
	}

	private EquipmentTask convertDeviceToEquipmentTask(Request request, Device device, int idTask) {

		EquipmentTask result = new EquipmentTask();
		result.setId(Long.valueOf(idTask));
		result.setDeviceName(device.getSerialNumber());
		result.setStatus(Status.FINISH_OK);
		result.setInitTime(request.getTime());
		result.setDriverClass("Minsait_Etisalat_1");
		List<Order> orderList = new ArrayList<Order>();
		Order newOrder = new Order();
		newOrder.setId(1l);
		newOrder.setName(request.getResponseType().getCode());
		newOrder.setPayload(request.getPayload());
		newOrder.setInitTime(request.getTime());
//		newOrder.setRequestId(Request.buildIdRequestFromUUID(UUID.randomUUID().toString()));
		Status status =  Status.FINISH_OK;
		String errorMessage = Optional.ofNullable(device.getErrorMessage()).orElse("");

		if (isError(request, device)) {
			
			status = Status.ERROR;
			
			if(!errorMessage.isEmpty()){
				Map<Integer, String> mapError = new HashMap<Integer, String>();
				mapError.put(0, request.getErrorMessage());
				newOrder.setError(mapError);
			}
			
			long finishTime = System.currentTimeMillis() + 50000l; //?
			newOrder.setFinishTime(finishTime);
			
			log.info("[CallAdapter]: Error: time is: " + finishTime);
			
		} else if ("OK".equals(device.getMeterStatus()) && errorMessage != null && errorMessage.length() > 0) {
			status = Status.FINISH_OK;
			Map<Integer, String> mapInfo = new HashMap<Integer, String>();
			mapInfo.put(0, errorMessage);
			newOrder.setInfo(mapInfo);
			
		} 
		
		newOrder.setStatus(status);

		orderList.add(newOrder);
		result.setOrder(orderList);

		return result;

	}
	
	private boolean isError(Request request, Device device){
	
		//String errorMessage = Optional.of(device.getErrorMessage()).orElse("");
		String meterStatus = Optional.ofNullable(device.getMeterStatus()).orElse("");
		
		//If I don't have any information about the device
		//Check request status
		if(meterStatus.isEmpty()){
			
			RequestStatus requesStatus = request.getStatus();
			
			if(requesStatus != null && 
					(requesStatus.equals(RequestStatus.FAIL))){
				
				return true;
			}
		}
		
		//is error if the error message is not empty or the meter status is ERROR
		return (meterStatus.equals("ERROR"));
	}
	
}
