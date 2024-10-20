package com.minsait.oum.mdc.dlms.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.indracompany.energy.dlms.cosem.addc.server.INotificationCallback;
import com.minsait.commands.CommandOutput;
import com.minsait.commands.impl.action.AbstractAction;
import com.minsait.mdc.data.model.Call;
import com.minsait.oum.mdc.application.InventoryClient;
import com.minsait.oum.mdc.application.ProfileApplication;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.platform.PlatformDevice;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.PlatformRequest;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

import gurux.common.GXCommon;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope(value = "prototype")
public class OnConnectivityNotificationCallback implements INotificationCallback {
	
	@Autowired
	private ProfileApplication application;
	
	@Autowired
	private InventoryClient inventoryClient;

	@Autowired
	private ApplicationContext appCntx;
	
	@Autowired
	private ObjectMapper objectMapper;

	
	@Override
	public void onData(String senderInfo, List<Object> values) {
		log.info("onConnectivity");
		log.debug("onConnectivity: senderInfo: " + senderInfo);
		
		Gson gson = new Gson() ;
		String logicalDeviceName = "";
		
		long totalActiveEnergyImport;
		long totalActiveEnergyExport;
		long totalReactiveEnergyImport;
		long totalReactiveEnergyExport;
		
		int index = 0;
		for (Object value : values) {
			if (value != null) {
				if (value instanceof byte[]) {
					log.info(index++ + ": " + GXCommon.bytesToHex((byte[]) value));
				} else if (value instanceof Number) {
					log.info(index++ + ": " + value);
				} else if (value instanceof String) {
					log.info(index++ + ": " + value);
				} else {
					log.info(index++ + ": " + value);
				}
			} else {
				log.info(index++ + ": is null");
			}
		}
		
		
		try {
			logicalDeviceName = new String((byte[]) values.get(0), "UTF-8");
			log.debug("Serial number: " + logicalDeviceName);
			values.set(0, logicalDeviceName);
		} catch (UnsupportedEncodingException|ClassCastException e) {
			logicalDeviceName = (String) values.get(0);
		}
		totalActiveEnergyImport = ((Number)values.get(4)).longValue();
		totalActiveEnergyExport = ((Number)values.get(5)).longValue();
		totalReactiveEnergyImport = ((Number)values.get(6)).longValue();
		totalReactiveEnergyExport = ((Number)values.get(7)).longValue();

		String equipmentCode = inventoryClient.getEquipmentPropsByLogicalDeviceName(logicalDeviceName).getString("code");
		
		RequestStatus requestStatus = RequestStatus.OK;
		
		if (equipmentCode == null) {
			log.error("equipment code not found for logical device name: " + logicalDeviceName);
			requestStatus = RequestStatus.ERROR;
		}
		
		List<Device> devices = new ArrayList<>();
		Device device = new Device();
		device.setSerialNumber(equipmentCode);
		device.setMeterStatus("OK");
		devices.add(device);

		Request onAlarmRequest = new Request();
		onAlarmRequest.setDevices(devices);
		
		onAlarmRequest.setRequestType(RequestType.ASYNCHRONOUS);
		onAlarmRequest.setResponseType(ResponseType.PUSH_ON_CONNECTIVITY);
		onAlarmRequest.setPayload(gson.toJson(values));
		onAlarmRequest.setSerialnumber(equipmentCode);
		onAlarmRequest.setStatus(requestStatus);
		// create call and update OnConnectivity notification
		Call callMonitor = this.application.createCallMonitorEntry(onAlarmRequest);
		
		storeReadings(equipmentCode, callMonitor.getPid(), totalActiveEnergyImport,totalActiveEnergyExport, totalReactiveEnergyImport, totalReactiveEnergyExport );
	}
	
	private void storeReadings(String meterCode, String pid, long totalActiveEnergyImport, long totalActiveEnergyExport, long totalReactiveEnergyImport, long totalReactiveEnergyExport) {
		
		List<PlatformMeter> list = new ArrayList<PlatformMeter>();
	    	
	    	Date currentDate = new Date();
	        long day = Long.valueOf(currentDate.getTime()/1000).longValue();

	    	list.add(LoadProfileMeter.builder()
	            .measurementIntervalCode("H")
	            .magnitudeCode("+A")
	            .day(day)
	            .value(totalActiveEnergyImport)
	            .obisCode("1.0.1.8.0.255")
	            .unitM(" ")
	            .build());
	        
			list.add(LoadProfileMeter.builder()
	            .measurementIntervalCode("H")
	            .magnitudeCode("-A")
	            .day(day)
	            .obisCode("1.0.2.8.0.255")
	            .value(totalActiveEnergyExport)
	            .build());
	        
			list.add(LoadProfileMeter.builder()
		            .measurementIntervalCode("H")
		            .magnitudeCode("-Q")
		            .day(day)
		            .obisCode("1.0.3.8.0.255")
		            .value(totalReactiveEnergyImport)
		            .build());
			list.add(LoadProfileMeter.builder()
		            .measurementIntervalCode("H")
		            .magnitudeCode("+Q")
		            .day(day)
		            .obisCode("1.0.4.8.0.255")
		            .value(totalReactiveEnergyExport)
		            .build());
			

			PlatformDevice platformDevice = PlatformDevice.builder()
					.meterCollector(list)
					.meterId(meterCode)
					.pid(pid)
					.taskId(1L)
					.orderId(1L)
					.requestId(1L)
					.build();

			PlatformRequest platformRequest = PlatformRequest.builder()
					.device(Arrays.asList(platformDevice))
					.build();

			sendThroughActionService("LoadProfileActionWS", platformRequest);
			
		
		}

	private void sendThroughActionService(String actionServiceName, String data) {
		final CommandOutput outputResult = new CommandOutput();
		outputResult.setResult(data);
		// Se envía a kafka - MDM
		log.debug("about to send new message to kafka using action '{}': {}", actionServiceName, data);
		final AbstractAction actionService = (AbstractAction) appCntx.getBean(actionServiceName);
		actionService.executeActions(outputResult);
		log.debug("new message sent to kafka succesfully using action '{}'", actionServiceName, data);
	}

	private void sendThroughActionService(String serviceName, final PlatformRequest platformRequest) {
		String platformRequestString = "";
		try {
			platformRequestString = objectMapper.writeValueAsString(Arrays.asList(platformRequest));
		} catch (JsonProcessingException jpe) {
			throw new UnsupportedOperationException(String.format(
					"error building load profile platform request:  %s ", platformRequest.toString()));
		}
		sendThroughActionService(serviceName, platformRequestString);
	}
}